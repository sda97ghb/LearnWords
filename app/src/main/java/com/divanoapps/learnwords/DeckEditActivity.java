package com.divanoapps.learnwords;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.divanoapps.learnwords.Data.DB;
import com.divanoapps.learnwords.Entities.Deck;

public class DeckEditActivity extends AppCompatActivity {

    public static String getDeckNameExtraName() {
        return "DECK_NAME";
    }

    private Deck mDeck = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            mDeck = DB.getDeck(getIntent().getExtras().getString(getDeckNameExtraName()));
        } catch (DB.DeckNotFoundException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "FAB in deck " + mDeck.getName(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
