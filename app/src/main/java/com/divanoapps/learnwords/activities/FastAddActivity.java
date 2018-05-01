package com.divanoapps.learnwords.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.divanoapps.learnwords.Application;
import com.divanoapps.learnwords.R;
import com.divanoapps.learnwords.YandexDictionary.DictionaryResult;
import com.divanoapps.learnwords.YandexDictionary.YandexDictionaryService;
import com.divanoapps.learnwords.adapters.TranslationListAdapter;
import com.divanoapps.learnwords.data.api2.ApiCard;
import com.divanoapps.learnwords.data.api2.ApiError;
import com.divanoapps.learnwords.dialogs.MessageOkDialogFragment;
import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.TranslationOption;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FastAddActivity extends AppCompatActivity
    implements TranslationListAdapter.OnTranslationOptionSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.deck_name_view)
    TextView deckNameView;

    @BindView(R.id.word_edit)
    EditText wordEdit;

    @BindView(R.id.comment_edit_wrapper)
    TextInputLayout commentEditWrapper;

    @BindView(R.id.comment_edit)
    EditText commentEdit;

    @BindView(R.id.translation_edit)
    EditText translationEdit;

    @BindView(R.id.translation_options_view)
    RecyclerView translationOptionsView;

    private String deckName;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_add);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deckName = Application.getDefaultDeckName();
        deckNameView.setText(deckName);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText != null) {
                wordEdit.setText(sharedText);
                requestTranslations(sharedText);
            }
        }
//        else {
//            // Handle other intents, such as being started from the home screen
//        }

        wordEdit.requestFocus();
        wordEdit.clearFocus();

        translationOptionsView.setLayoutManager(new LinearLayoutManager(this));
        TranslationListAdapter translationListAdapter = new TranslationListAdapter();
        translationListAdapter.setOnTranslationOptionSelectedListener(this);
        translationOptionsView.setAdapter(translationListAdapter);

        RxTextView.textChanges(wordEdit)
            .subscribe(charSequence -> {
                requestTranslations(charSequence.toString());
                checkExistence();
            });

        RxTextView.textChanges(commentEdit).subscribe(charSequence -> checkExistence());

        googleApiClient = Application.getGoogleSignInApiClient(this, this);

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful
                GoogleSignInAccount account = task.getResult(ApiException.class);
                setAccount(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                e.printStackTrace();
                showErrorMessageToast(getString(R.string.failed_to_sign_in, e.getLocalizedMessage()));
                finish();
            }
        }
    }

    private void setAccount(GoogleSignInAccount account) {
        Application.setGoogleSignInAccount(account);
        Application.initializeApiFromGoogleSignInAccount(account);

        Application.getApi().registerUser()
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_card_add_activity_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: finish(); return true;// NavUtils.navigateUpFromSameTask(this); return true;
            case R.id.action_done: onDoneClicked(); return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    private ApiCard getCurrentStateAsApiCard() {
        ApiCard apiCard = new ApiCard();
        apiCard.setOwner(Application.getGoogleSignInAccount().getEmail());
        apiCard.setDeck(deckName);
        apiCard.setWord(wordEdit.getText().toString());
        apiCard.setComment(commentEdit.getText().toString());
        apiCard.setTranslation(translationEdit.getText().toString());
        apiCard.setDifficulty(Card.getDefaultDifficulty());
        apiCard.setHidden(false);
        return apiCard;
    }

    private void onDoneClicked() {
        ApiCard apiCard = getCurrentStateAsApiCard();
        Application.getApi().getCard(apiCard.getDeck(), apiCard.getWord(), apiCard.getComment())
            .doOnSuccess(unused -> showErrorMessage("Card already exists."))
            .doOnError(unused -> Application.getApi().saveCard(apiCard)
                .doOnComplete(() -> {
                    Toast.makeText(this, "Card saved", Toast.LENGTH_SHORT).show();

//                    LayoutInflater inflater = getLayoutInflater();
//                    View layout = inflater.inflate(R.layout.toast_card_saved,
//                        (ViewGroup) findViewById(R.id.custom_toast_container));
//
//                    TextView text = (TextView) layout.findViewById(R.id.text);
//                    text.setText("This is a custom toast");
//
//                    Toast toast = new Toast(getApplicationContext());
//                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                    toast.setDuration(Toast.LENGTH_LONG);
//                    toast.setView(layout);
//                    toast.show();

                    finish();
                })
                .doOnError(this::showErrorMessage)
                .subscribe())
            .subscribe();
    }

    private void requestTranslations(String text) {
        if (text.isEmpty()) {
            viewDictionaryResult(null);
            return;
        }
        Application.yandexDictionaryService.lookup(YandexDictionaryService.getApiKey(), Application.FAKE_DIRECTION, text)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(this::viewDictionaryResult)
            .doOnError(Throwable::printStackTrace)
            .subscribe();
    }

    private void viewDictionaryResult(DictionaryResult dictionaryResult) {
        if (translationOptionsView.getAdapter() == null)
            return;
        List<TranslationOption> translationOptions = YandexDictionaryService.convert(dictionaryResult);
        TranslationListAdapter adapter = (TranslationListAdapter) translationOptionsView.getAdapter();
        adapter.setTranslationOptions(translationOptions);
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

    private void checkExistence() {
        ApiCard apiCard = getCurrentStateAsApiCard();
        Application.getApi().getCard(apiCard.getDeck(), apiCard.getWord(), apiCard.getComment())
            .doOnSuccess(unused -> commentEditWrapper.setError("Already exists"))
            .doOnError(throwable -> {
                commentEditWrapper.setError(null);
                commentEditWrapper.setErrorEnabled(false);
            })
            .subscribe();
    }

    @Override
    public void onTranslationOptionSelected(TranslationOption option) {
        translationEdit.setText(option.getTranslation());
        translationEdit.requestFocus();
        translationEdit.setSelection(translationEdit.length());
    }

    @OnClick(R.id.select_deck_button)
    public void onSelectDeckButtonClicked() {
        Application.getApi().getUser()
            .doOnSuccess(apiUser -> {
                CharSequence[] items = new CharSequence[apiUser.getPersonalDecks().size()];
                for (int i = 0; i < apiUser.getPersonalDecks().size(); ++ i)
                    items[i] = apiUser.getPersonalDecks().get(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select deck")
                    .setItems(items, (dialog, which) -> {
                        deckName = apiUser.getPersonalDecks().get(which);
                        deckNameView.setText(deckName);
                    }).create().show();
            })
            .doOnError(this::showErrorMessage)
            .subscribe();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showErrorMessageToast(connectionResult.getErrorMessage());
        finish();
    }

    private void showErrorMessageToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
