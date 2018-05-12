package com.divanoapps.learnwords.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.adapters.CardListAdapter;
import com.divanoapps.learnwords.auxiliary.RussianNumberConjugation;
import com.divanoapps.learnwords.data.api2.ApiError;
import com.divanoapps.learnwords.data.local.Card;
import com.divanoapps.learnwords.data.local.Deck;
import com.divanoapps.learnwords.data.local.DeckSpecificationsFactory;
import com.divanoapps.learnwords.data.local.RepositoryModule;
import com.divanoapps.learnwords.dialogs.MessageOkDialogFragment;
import com.divanoapps.learnwords.dialogs.RenameDeckDialogFragment;
import com.divanoapps.learnwords.dialogs.YesNoMessageDialogFragment;
import com.divanoapps.learnwords.use_case.RenameDeck;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeckEditActivity extends AppCompatActivity implements
        RenameDeckDialogFragment.RenameDeckDialogListener {

    public static String getDeckExtraName() {
        return "DECK_EXTRA";
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

    private CardListAdapter cardListAdapter;

    private RepositoryModule repositoryModule;

    private Deck deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_edit);

        ButterKnife.bind(this);

        // Expand activity to make transparent notification bar
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                             WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        deck = getIntent().getParcelableExtra(getDeckExtraName());

        // Setup card list
        cardListView.setLayoutManager(new LinearLayoutManager(this));

        cardListAdapter = new CardListAdapter(getLayoutInflater());
        cardListAdapter.setEditCardClickedListener(this::onEditCardClicked);
        cardListAdapter.setToggleCardHiddenClickedListener(this::onToggleCardEnabledClicked);
        cardListAdapter.setDeleteCardsClickedListener(this::deleteCards);
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
        updateUi();
        repositoryModule.getDeckRxRepository()
            .query(DeckSpecificationsFactory.byName(deck.getName()))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(this::setDeckAndUpdateUi)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    private void setDeckAndUpdateUi(List<Deck> decks) {
        this.deck = decks.get(0);
        getIntent().putExtra(getDeckExtraName(), this.deck);
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
            case R.id.action_search: /* Nothing to do */  return true;
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
            repositoryModule.getDeckRxRepository()
                .delete(deck)
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
            String newDeckName = ((RenameDeckDialogFragment) dialog).getNewDeckName();

            Completable.fromAction(() -> {
                RenameDeck.execute(deck, newDeckName, repositoryModule.getDeckRepository(), repositoryModule.getCardRepository());
                deck = repositoryModule.getDeckRepository().query(DeckSpecificationsFactory.byName(newDeckName)).get(0);
                getIntent().putExtra(getDeckExtraName(), deck);
            })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(this::updateUi)
                .doOnError(this::showErrorMessage)
                .subscribe();
        }
    }

    @OnClick(R.id.fab)
    public void onAddCardClicked() {
        Intent intent = new Intent(this, CardAddActivity.class);
        intent.putExtra(CardAddActivity.getDeckExtraName(), deck);
        startActivity(intent);
    }

    public void onEditCardClicked(Card card) {
        Intent intent = new Intent(DeckEditActivity.this, CardEditActivity.class);
        intent.putExtra(CardEditActivity.getDeckExtraName(), deck);
        intent.putExtra(CardEditActivity.getCardExtraName(), card);
        startActivity(intent);
    }

    public void onToggleCardEnabledClicked(Card card) {
        // Get the card to know its visibility
        card.setHidden(!card.isHidden());
        repositoryModule.getCardRxRepository()
            .update(card)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(this::updateUi)
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    private void deleteCards(List<Card> cards) {
        repositoryModule.getCardRxRepository()
            .delete(cards.toArray(new Card[cards.size()]))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(this::updateUi)
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
