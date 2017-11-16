package com.divanoapps.learnwords;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.divanoapps.learnwords.Data.DB;
import com.divanoapps.learnwords.Entities.DeckId;
import com.divanoapps.learnwords.Entities.DeckShort;

import java.util.List;

public class DeckListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   RenameDeckDialogFragment.RenameDeckDialogListener,
                   DeckListAdapter.EditDeckClickedListener,
                   DeckListAdapter.StartExerciseClickedListener {

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);

        // Action bar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Fab setup
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClicked();
            }
        });

        // Setup navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Expand activity to make transparent notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                             WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Setup deck list
        final RecyclerView deckListView = (RecyclerView) findViewById(R.id.DeckListView);
        deckListView.setLayoutManager(new LinearLayoutManager(this));

        final DeckListAdapter deckListAdapter = new DeckListAdapter(this);
        deckListAdapter.setEditDeckClickedListener(this);
        deckListAdapter.setStartExerciseClickedListener(this);

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

        // Load all decks
        DB.initialize()
            .setOnDoneListener(new DB.Request.OnDoneListener<Void>() {
                @Override
                public void onDone(Void result) {
                    onDbInitialized();
                }
            })
            .setOnErrorListener(new DB.Request.OnErrorListener() {
                @Override
                public void onError(DB.Error error) {
                    onDbInitializationError(error);
                }
            })
            .execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DB.getDecks()
            .setOnDoneListener(new DB.Request.OnDoneListener<List<DeckShort>>() {
                    @Override
                    public void onDone(List<DeckShort> result) {
                        onGetDeckListDone(result);
                    }
                })
            .setOnErrorListener(new DB.Request.OnErrorListener() {
                    @Override
                    public void onError(DB.Error error) {
                        onGetDeckListError(error);
                    }
                })
            .execute();
    }

    public void onDbInitialized() {
        Snackbar.make(findViewById(R.id.coordinator_layout), "Successfully initialized.",
                Snackbar.LENGTH_LONG).show();
        DB.getDecks()
            .setOnDoneListener(new DB.Request.OnDoneListener<List<DeckShort>>() {
                @Override
                public void onDone(List<DeckShort> result) {
                    onGetDeckListDone(result);
                }
            })
            .setOnErrorListener(new DB.Request.OnErrorListener() {
                @Override
                public void onError(DB.Error error) {
                    onGetDeckListError(error);
                }
            })
            .execute();
    }

    public void onDbInitializationError(DB.Error error) {
        Snackbar.make(findViewById(R.id.coordinator_layout), error.getMessage(),
                Snackbar.LENGTH_LONG).show();
    }

    private void onGetDeckListDone(List<DeckShort> decks) {
        Snackbar.make(findViewById(R.id.coordinator_layout), "Loaded list of decks.",
                Snackbar.LENGTH_LONG).show();
        RecyclerView deckListView = (RecyclerView) findViewById(R.id.DeckListView);
        DeckListAdapter deckListAdapter = (DeckListAdapter) deckListView.getAdapter();
        deckListAdapter.setDecks(decks);
    }

    private void onGetDeckListError(DB.Error error) {
        Snackbar.make(findViewById(R.id.coordinator_layout), "Unable to load list of decks.",
                Snackbar.LENGTH_LONG).show();
    }

    private void onFabClicked() {
        Snackbar.make(findViewById(R.id.coordinator_layout), "FAB", Snackbar.LENGTH_LONG).show();
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
        MenuItem settingsMenuItem = (MenuItem) menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(settingsMenuItem);
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
                Snackbar.make(findViewById(R.id.coordinator_layout), "Navigate settings.",
                        Snackbar.LENGTH_LONG).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onEditDeckClicked(DeckId id) {
        Intent intent = new Intent(DeckListActivity.this, DeckEditActivity.class);
        intent.putExtra(DeckEditActivity.getDeckIdExtraName(), id);
        startActivity(intent);
    }

    @Override
    public void onStartExerciseClicked(DeckId id, CardRetriever.Order order) {
        String orderString = "";
        switch (order) {
            case alphabetical: orderString = "alphabetical"; break;
            case file: orderString = "file"; break;
            case random: orderString = "random"; break;
            case higher30: orderString = "higher 30"; break;
            case lower30: orderString = "lower 30"; break;
        }
        Snackbar.make(findViewById(R.id.coordinator_layout), "Start exercise " + id.getName() +
                " in " + orderString + " order.", Snackbar.LENGTH_LONG).show();
    }
}
