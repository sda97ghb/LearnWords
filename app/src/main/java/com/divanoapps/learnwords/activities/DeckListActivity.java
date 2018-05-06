package com.divanoapps.learnwords.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
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
import com.divanoapps.learnwords.exercise.CardDispenserFactory;
import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.adapters.DeckListAdapter;
import com.divanoapps.learnwords.data.api2.ApiError;
import com.divanoapps.learnwords.data.local.Deck;
import com.divanoapps.learnwords.data.local.RepositoryModule;
import com.divanoapps.learnwords.data.local.TimestampFactory;
import com.divanoapps.learnwords.dialogs.AddDeckDialogFragment;
import com.divanoapps.learnwords.dialogs.MessageOkDialogFragment;
import com.divanoapps.learnwords.dialogs.RenameDeckDialogFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeckListActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        RenameDeckDialogFragment.RenameDeckDialogListener {

    RepositoryModule repositoryModule;

    GoogleApiClient googleSignInApiClient;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.DeckListView)
    RecyclerView deckListView;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    DeckListAdapter deckListAdapter;

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);

        ButterKnife.bind(this);

        // Action bar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        deckListView.setLayoutManager(new LinearLayoutManager(this));

        deckListAdapter = new DeckListAdapter(getLayoutInflater());
        deckListAdapter.setEditDeckClickedListener(this::onEditDeckClicked);
        deckListAdapter.setStartExerciseClickedListener(this::onStartExerciseClicked);
        deckListAdapter.setDeleteDecksClickedListener(this::deleteDecks);
//        deckListAdapter.setSelectionModeStartedListener(() -> getSupportActionBar().hide());
//        deckListAdapter.setSelectionModeFinishedListener(() -> getSupportActionBar().show());
        deckListView.setAdapter(deckListAdapter);

        deckListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                    floatingActionButton.hide();
                else
                    floatingActionButton.show();
            }
        });

        googleSignInApiClient = Application.getGoogleSignInApiClient(this,
            connectionResult -> showErrorMessage(connectionResult.getErrorMessage()));

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        if (prefs.getBoolean("preference_use_remote_db", false)) {
//            String serverAddress = prefs.getString("preference_server_address", RemoteDB.getDefaultServerAddress());
//            String username = prefs.getString("preference_username", RemoteDB.getDefaultServerAddress());
//            String password = prefs.getString("preference_password", RemoteDB.getDefaultServerAddress());
//            DB.setDb(new RemoteDB(serverAddress, username, password));
//        }
//        else
//            DB.setDb(new LocalDB());

        repositoryModule = new RepositoryModule(getApplicationContext());
    }

    private void deleteDecks(List<Deck> decks) {
        repositoryModule.getDeckRepository().delete(decks.toArray(new Deck[decks.size()]))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(this::requestDeckList)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestDeckList();
    }

    private void showDecks(List<Deck> decks) {
        deckListAdapter.setDecks(decks);
    }

    public void requestDeckList() {
        repositoryModule.getDeckRepository().getAllDecks()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(this::showDecks)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    @OnClick(R.id.fab)
    public void addDeckButtonClicked() {
        AddDeckDialogFragment dialog = new AddDeckDialogFragment();
        dialog.setOnOkClickedListener(this::addDeck);
        dialog.show(getFragmentManager(), AddDeckDialogFragment.getUniqueTag());
    }

    private void addDeck(String name, String languageFrom, String languageTo) {
        Deck deck = new Deck(TimestampFactory.getTimestamp(), name, languageFrom, languageTo);
        repositoryModule.getDeckRepository().insert(deck)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(this::requestDeckList)
            .doOnError(this::showErrorMessage)
            .subscribe();
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
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleSignInApiClient)
            .setResultCallback(status -> {
                startActivity(new Intent(this, LauncherActivity.class));
                finish();
            });
    }

    public void onEditDeckClicked(String deckName) {
        Intent intent = new Intent(this, DeckEditActivity.class);
        intent.putExtra(DeckEditActivity.getDeckNameExtraName(), deckName);
        startActivity(intent);
    }

    public void onStartExerciseClicked(String deckName, CardDispenserFactory.Order order) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra(ExerciseActivity.getDeckNameExtraName(), deckName);
        intent.putExtra(ExerciseActivity.getOrderExtraName(), order);
        startActivity(intent);
    }

    private void showErrorMessage(Throwable throwable) {
        if (throwable instanceof ApiError) {
            ApiError apiError = (ApiError) throwable;
            showErrorMessage(apiError.getType() + " " + apiError.getCode() + ": " + apiError.getMessage());
        }
        else
            showErrorMessage(throwable.getMessage());
    }

    private void showErrorMessage(String message) {
        MessageOkDialogFragment.show(this, message);
    }
}
