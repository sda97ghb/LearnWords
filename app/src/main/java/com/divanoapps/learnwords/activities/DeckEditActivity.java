package com.divanoapps.learnwords.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.view.WindowManager;
import android.widget.TextView;

import com.divanoapps.learnwords.Application;
import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.adapters.CardListAdapter;
import com.divanoapps.learnwords.auxiliary.RussianNumberConjugation;
import com.divanoapps.learnwords.data.api2.ApiCard;
import com.divanoapps.learnwords.data.api2.ApiError;
import com.divanoapps.learnwords.data.api2.ApiExpandedDeck;
import com.divanoapps.learnwords.data.local.Card;
import com.divanoapps.learnwords.data.local.Deck;
import com.divanoapps.learnwords.data.local.RepositoryModule;
import com.divanoapps.learnwords.dialogs.MessageOkDialogFragment;
import com.divanoapps.learnwords.dialogs.RenameDeckDialogFragment;
import com.divanoapps.learnwords.dialogs.YesNoMessageDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeckEditActivity extends AppCompatActivity implements
        RenameDeckDialogFragment.RenameDeckDialogListener {

    public static String getDeckNameExtraName() {
        return "DECK_NAME";
    }

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.card_list_view)
    RecyclerView cardListView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.language_from)
    TextView fromLanguageView;

    @BindView(R.id.language_to)
    TextView toLanguageView;

    @BindView(R.id.number_of_cards)
    TextView numberOfCardsView;

    @BindView(R.id.number_of_hidden_card)
    TextView numberOfHiddenCardsView;

    @BindString(R.string.version_language_code)
    String versionLanguageCode;

    @BindView(R.id.decorator_number_of_cards)
    TextView numberOfCardsDecorator;

    @BindView(R.id.decorator_number_of_hidden_card)
    TextView numberOfHiddenCardsDecorator;

    CardListAdapter cardListAdapter;

    RepositoryModule repositoryModule;

    private Deck deck = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_edit);

        ButterKnife.bind(this);

        // Expand activity to make transparent notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                             WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Setup card list
        cardListView.setLayoutManager(new LinearLayoutManager(this));

        cardListAdapter = new CardListAdapter(getLayoutInflater());
        cardListAdapter.setEditCardClickedListener(this::onEditCardClicked);
        cardListAdapter.setToggleCardEnabledClickedListener(this::onToggleCardEnabledClicked);
        cardListAdapter.setDeleteCardClickedListener(this::onDeleteCardClicked);
        cardListView.setAdapter(cardListAdapter);

        cardListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                    fab.hide();
                else
                    fab.show();
            }
        });

        repositoryModule = new RepositoryModule(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestDeck();
    }

    private void requestDeck() {
        String deckName = getIntent().getStringExtra(getDeckNameExtraName());
        repositoryModule.getDeckRepository().getByName(deckName)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(this::showDeck)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    private void showDeck(Deck deck) {
        this.deck = deck;
        updateUi();
    }

    @SuppressLint("SetTextI18n")
    private void updateUi() {
        toolbar.setTitle(deck.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(deck.getName());

        // Set language from and language to
        fromLanguageView.setText(deck.getFromLanguage());
        toLanguageView.setText(deck.getToLanguage());

        List<Card> cards = deck.getCards();

        int numberOfCards = 0;
        int numberOfHiddenCards = 0;

        if (cards != null) {
            numberOfCards = cards.size();
            numberOfHiddenCards = 0;
            for (Card card: cards)
                if (card.isHidden())
                    ++ numberOfHiddenCards;
        }

        // Set number of cards and number of hidden cards views
        numberOfCardsView.setText(Integer.valueOf(numberOfCards).toString());
        numberOfHiddenCardsView.setText(Integer.valueOf(numberOfHiddenCards).toString());

        if (versionLanguageCode.equals("ru")) {
            numberOfCardsDecorator.setText(RussianNumberConjugation.getCards(numberOfCards));
            numberOfHiddenCardsDecorator.setText("из них " + RussianNumberConjugation.getHidden(numberOfHiddenCards));
        }

        cardListAdapter.setCards(cards);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar.
        getMenuInflater().inflate(R.menu.menu_deck_edit_activity_toolbar, menu);

        // Add search
        MenuItem settingsMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) settingsMenuItem.getActionView();
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
        String uniqueDialogTag = "com.divanoapps.learnwords.dialogs.RenameDeckDialogFragment." + deck.getName();
        RenameDeckDialogFragment.newInstance(deck.getName())
                .show(getSupportFragmentManager(), uniqueDialogTag);
    }

    private void deleteCurrentDeck() {
        YesNoMessageDialogFragment.show(this, getString(R.string.are_you_shure_delete_deck_question), () ->
            repositoryModule.getDeckRepository().delete(deck.getName())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> NavUtils.navigateUpFromSameTask(this))
                .doOnError(this::showErrorMessage)
                .subscribe()
        );
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog instanceof RenameDeckDialogFragment) {
            String oldDeckName = deck.getName();
            String newDeckName = ((RenameDeckDialogFragment) dialog).getNewDeckName();

            deck.setName(newDeckName);

            repositoryModule.getDeckRepository().replace(oldDeckName, deck)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    getIntent().putExtra(getDeckNameExtraName(), newDeckName);
                    requestDeck();
                })
                .doOnError(this::showErrorMessage)
                .subscribe();
        }
    }

    @OnClick(R.id.fab)
    public void onAddCardClicked() {
        Intent intent = new Intent(this, CardAddActivity.class);
        intent.putExtra(CardAddActivity.getDeckNameExtraName(), deck.getName());
        startActivity(intent);
    }

    public void onEditCardClicked(String deckName, String word, String comment) {
        Intent intent = new Intent(DeckEditActivity.this, CardEditActivity.class);
        intent.putExtra(CardEditActivity.getDeckNameExtraName(), deckName);
        intent.putExtra(CardEditActivity.getWordExtraName(), word);
        intent.putExtra(CardEditActivity.getCommentExtraName(), comment);
        startActivity(intent);
    }

    public void onToggleCardEnabledClicked(String deckName, String word, String comment) {
        // Get the card to know its visibility
        repositoryModule.getCardRepository().find(deckName, word, comment)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(card -> {
                card.setHidden(!card.isHidden());
                repositoryModule.getCardRepository().replace(deckName, word, comment, card)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(this::requestDeck)
                    .doOnError(this::showErrorMessage)
                    .subscribe();
            })
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    public void onDeleteCardClicked(String deckName, String word, String comment) {
        repositoryModule.getCardRepository().delete(deckName, word, comment)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(this::requestDeck)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    private void showErrorMessage(String message) {
        MessageOkDialogFragment.show(this, message);
    }

    private void showErrorMessage(Throwable throwable) {
        if (throwable instanceof ApiError)
            showErrorMessage(((ApiError) throwable).getType() + ":" + throwable.getMessage());
        else
            showErrorMessage(throwable.getMessage());
    }
}
