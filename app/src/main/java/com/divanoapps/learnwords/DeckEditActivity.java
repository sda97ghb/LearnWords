package com.divanoapps.learnwords;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.divanoapps.learnwords.Data.DB;
import com.divanoapps.learnwords.Entities.Card;
import com.divanoapps.learnwords.Entities.Deck;

public class DeckEditActivity extends AppCompatActivity implements RenameDeckDialogFragment.RenameDeckDialogListener {

    public static String getDeckNameExtraName() {
        return "DECK_NAME";
    }

    private Deck mDeck = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_edit);

        // Get deck with given name
        try {
            mDeck = DB.getDeck(getIntent().getExtras().getString(getDeckNameExtraName()));
        } catch (DB.DeckNotFoundException e) {
            e.printStackTrace();
            mDeck = new Deck.Builder().setName("ERROR:( Press back and select deck again.").build();
        }

        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mDeck.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup "Add card" FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "FAB in deck " + mDeck.getName(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Setup card list
        RecyclerView cardListView = (RecyclerView) findViewById(R.id.card_list_view);
        cardListView.setLayoutManager(new LinearLayoutManager(this));
        CardListAdapter cardListAdapter = new CardListAdapter(this, mDeck);
        cardListAdapter.setEditCardClickedListener(new CardListAdapter.EditCardClickedListener() {
            @Override
            public void onEditCardClicked(Card card) {
                Snackbar.make(findViewById(R.id.coordinator_layout), "Edit card " + card.getWord(), Snackbar.LENGTH_LONG).show();
            }
        });
        cardListAdapter.setToggleCardEnabledClickedListener(new CardListAdapter.ToggleCardEnabledClickedListener() {
            @Override
            public void onToggleCardEnabledClicked(Card card) {
                Snackbar.make(findViewById(R.id.coordinator_layout), "Toggle card enable " + card.getWord(), Snackbar.LENGTH_LONG).show();
            }
        });
        cardListAdapter.setDeleteCardClickedListener(new CardListAdapter.DeleteCardClickedListener() {
            @Override
            public void onDeleteCardClicked(Card card) {
                Snackbar.make(findViewById(R.id.coordinator_layout), "Delete card " + card.getWord(), Snackbar.LENGTH_LONG).show();
            }
        });
        cardListView.setAdapter(cardListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar.
        getMenuInflater().inflate(R.menu.menu_deck_edit_activity_toolbar, menu);

        // Add search
        MenuItem settingsMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(settingsMenuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

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
        switch (item.getItemId()) {
            case R.id.action_rename: renameCurrentDeck(); return true;
            case R.id.action_search: /* Nothing to do */ return true;
            case R.id.action_delete: deleteCurrentDeck(); return true;
            case android.R.id.home: NavUtils.navigateUpFromSameTask(this); return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    private void renameCurrentDeck() {
        Snackbar.make(findViewById(R.id.coordinator_layout), "Rename deck " + mDeck.getName(), Snackbar.LENGTH_LONG).show();
        String uniqueDialogTag = "com.divanoapps.learnwords.RenameDeckDialogFragment." + mDeck.getName();
        RenameDeckDialogFragment.newInstance(mDeck.getName()).show(getSupportFragmentManager(), uniqueDialogTag);
    }

    private void deleteCurrentDeck() {
        Snackbar.make(findViewById(R.id.coordinator_layout), "Delete deck " + mDeck.getName(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog instanceof RenameDeckDialogFragment) {
            RenameDeckDialogFragment renameDeckDialog = (RenameDeckDialogFragment) dialog;
            Snackbar.make(findViewById(R.id.coordinator_layout), "Rename deck " + mDeck.getName() +
                    " to " + renameDeckDialog.getNewDeckName(), Snackbar.LENGTH_LONG).show();
        }
    }
}
