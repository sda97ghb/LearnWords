package com.divanoapps.learnwords.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.divanoapps.learnwords.Application;
import com.divanoapps.learnwords.CardRetriever;
import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.adapters.DeckListAdapter;
import com.divanoapps.learnwords.data.api2.ApiDeck;
import com.divanoapps.learnwords.data.api2.ApiDeckInfo;
import com.divanoapps.learnwords.data.api2.ApiError;
import com.divanoapps.learnwords.data.api2.ApiExpandedUser;
import com.divanoapps.learnwords.dialogs.AddDeckDialogFragment;
import com.divanoapps.learnwords.dialogs.MessageOkDialogFragment;
import com.divanoapps.learnwords.dialogs.RenameDeckDialogFragment;
import com.divanoapps.learnwords.entities.DeckId;
import com.divanoapps.learnwords.entities.DeckShort;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Provides functions for management of decks.
 */
public class DeckListActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        RenameDeckDialogFragment.RenameDeckDialogListener {

    GoogleApiClient googleSignInApiClient;

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);
        ButterKnife.bind(this);

        // Action bar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Fab setup
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> onFabClicked());

        // Setup navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Expand activity to make transparent notification bar
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                             WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Setup deck list
        final RecyclerView deckListView = (RecyclerView) findViewById(R.id.DeckListView);
        deckListView.setLayoutManager(new LinearLayoutManager(this));

        final DeckListAdapter deckListAdapter = new DeckListAdapter(this);
        deckListAdapter.setEditDeckClickedListener(this::onEditDeckClicked);
        deckListAdapter.setStartExerciseClickedListener(this::onStartExerciseClicked);
        deckListAdapter.setDeleteDeckClickedListener(this::onDeleteDeckClicked);

        deckListView.setAdapter(deckListAdapter);

        deckListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                    fab.hide();
                else
                    fab.show();
            }
        });

        googleSignInApiClient = Application.getGoogleSignInApiClient(this,
            connectionResult -> showErrorMessage(connectionResult.getErrorMessage()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Application.getApi().getExpandedUser()
            .doOnSuccess(this::showUser)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    /**
     * Request deck list from the server and show.
     */
    public void requestDeckList() {
        Application.getApi().getExpandedUser()
            .doOnSuccess(this::showUser)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    /**
     * Sets deck list from decks of {@code apiExpandedUser}.
     * @param apiExpandedUser
     */
    private void showUser(ApiExpandedUser apiExpandedUser) {
        List<DeckShort> decks = new LinkedList<>();
        for (ApiDeckInfo info : apiExpandedUser.getPersonalDecks())
            decks.add(new DeckShort(info.getName(), info.getNumberOfCards(), info.getNumberOfHiddenCards(),
                info.getFromLanguage(), info.getToLanguage()));
        RecyclerView deckListView = findViewById(R.id.DeckListView);
        DeckListAdapter deckListAdapter = (DeckListAdapter) deckListView.getAdapter();
        deckListAdapter.setDecks(decks);
    }

    /**
     * Will be called when user clicked add deck button.
     */
    private void onFabClicked() {
        AddDeckDialogFragment dialog = new AddDeckDialogFragment();
        dialog.setOnOkClickedListener(this::addDeck);
        dialog.show(getFragmentManager(), AddDeckDialogFragment.getUniqueTag());
    }

    /**
     * Creates new deck and saves it to the server.
     * @param name
     * @param languageFrom
     * @param languageTo
     */
    private void addDeck(String name, String languageFrom, String languageTo) {
        ApiDeck deck = new ApiDeck();
        deck.setName(name);
        deck.setFromLanguage(languageFrom);
        deck.setToLanguage(languageTo);
        Application.getApi().saveDeck(deck)
            .doOnComplete(this::requestDeckList)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    private void showErrorMessage(Throwable throwable) {
        if (throwable instanceof ApiError)
            showErrorMessage(((ApiError) throwable).getType() + ":" + throwable.getMessage());
        else
            showErrorMessage(throwable.getMessage());
    }

    private void showErrorMessage(String message) {
        MessageOkDialogFragment.show(this, message);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar.
        getMenuInflater().inflate(R.menu.menu_deck_list_activity_toolbar, menu);
        MenuItem settingsMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) settingsMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Snackbar.make(findViewById(R.id.coordinator_layout), "Search: " + newText + ".",
                    Snackbar.LENGTH_SHORT).show();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_decks:
                Snackbar.make(findViewById(R.id.coordinator_layout), "Navigate decks.",
                        Snackbar.LENGTH_LONG).show();
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_sign_out:
                signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Sign outs from the application.
     */
    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleSignInApiClient)
            .setResultCallback(status -> {
                startActivity(new Intent(this, LauncherActivity.class));
                finish();
            });
    }

    /**
     * Starts DeckEdit activity for specified deck.
     * @param id id of deck.
     */
    public void onEditDeckClicked(DeckId id) {
        Intent intent = new Intent(this, DeckEditActivity.class);
        intent.putExtra(DeckEditActivity.getDeckIdExtraName(), id);
        startActivity(intent);
    }

    /**
     * Starts exercise for the deck.
     * @param id
     * @param order
     */
    public void onStartExerciseClicked(DeckId id, CardRetriever.Order order) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra(ExerciseActivity.getDeckIdExtraName(), id);
        intent.putExtra(ExerciseActivity.getOrderExtraName(), order);
        startActivity(intent);
    }

    /**
     * Deletes the deck.
     * @param id
     */
    public void onDeleteDeckClicked(DeckId id) {
        Application.getApi().deleteDeck(id.getName())
            .doOnComplete(this::requestDeckList)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }
}
