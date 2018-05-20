package com.divanoapps.learnwords.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.divanoapps.learnwords.Application;
import com.divanoapps.learnwords.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * LauncherActivity. First screen with sign in button.
 */
public class LauncherActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // Constants

    private static final int RC_SIGN_IN = 9001;

    // UI

    @BindView(R.id.status_view)
    TextView statusView;

    // Other fields.

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        ButterKnife.bind(this);

        // Expand activity to make transparent notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        googleApiClient = Application.getGoogleSignInApiClient(this, this);

        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (lastSignedInAccount != null)
            setAccount(lastSignedInAccount);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showErrorMessage(connectionResult.getErrorMessage());
    }

    @OnClick(R.id.google_sign_in_button)
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

//    static class TestTask extends AsyncTask<Context, Void, Void> {
//        @Override
//        protected Void doInBackground(Context... contexts) {
//            StorageUserRepository storageUserRepository = new StorageUserRepository(contexts[0]);
//            StorageDeckRepository storageDeckRepository = new StorageDeckRepository(contexts[0]);
//            StorageCardRepository storageCardRepository = new StorageCardRepository(contexts[0]);
//
//            List<StorageUser> allUsers = storageUserRepository.getAllUsers();
//            List<StorageDeck> allDecks = storageDeckRepository.getAllDecks();
//            List<StorageCard> allCards = storageCardRepository.getAllCards();
//            System.out.println(allUsers);
//            System.out.println(allDecks);
//            System.out.println(allCards);
//
//            StorageUser storageUser = new StorageUser();
//            storageUser.setEmail("test1@example.com");
//            storageUser.setTimestamp(TimestampFactory.getTimestamp());
//            storageUserRepository.insert(storageUser);
//
//            storageUser = storageUserRepository.getByEmail("test1@example.com");
//            System.out.println(storageUser.getId());
//
//            StorageDeck storageDeck = new StorageDeck();
//            storageDeck.setOwner(storageUser.getId());
//            storageDeck.setName("Deck 1");
//            storageUser.setTimestamp(TimestampFactory.getTimestamp());
//            storageDeck.setFromLanguage("English");
//            storageDeck.setToLanguage("Russian");
//            storageDeckRepository.insert(storageDeck);
//
//            allUsers = storageUserRepository.getAllUsers();
//            allDecks = storageDeckRepository.getAllDecks();
//            allCards = storageCardRepository.getAllCards();
//            System.out.println(allUsers);
//            System.out.println(allDecks);
//            System.out.println(allCards);
//
//            return null;
//        }
//    }

    @OnClick(R.id.sign_out_button)
    public void signOut() {
//        new TestTask().execute(getApplicationContext());
//        Completable.fromAction(() -> {
//            StorageUserRepository storageUserRepository = new StorageUserRepository(getApplicationContext());
//            {
//                StorageUser storageUser = new StorageUser();
//                storageUser.setEmail("test1@example.com");
//                storageUser.setTimestamp(TimestampFactory.getTimestamp());
//                storageUserRepository.insert(storageUser);
//            }
//            {
//                StorageUser storageUser = storageUserRepository.getByEmail("test1@example.com");
//                System.out.println(storageUser.getId());
//            }
//        })
//            .observeOn(Schedulers.newThread())
//            .subscribeOn(AndroidSchedulers.mainThread())
//            .subscribe();


        Auth.GoogleSignInApi.signOut(googleApiClient)
            .setResultCallback(status -> statusView.setText("Signed out"));
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
                statusView.setText(getString(R.string.failed_to_sign_in, e.getLocalizedMessage()));
            }
        }
    }

    private void setAccount(GoogleSignInAccount account) {
        statusView.setText(getString(R.string.hello_message_prefix, account.getDisplayName()));

        Application.setGoogleSignInAccount(account);
        Application.initializeApiFromGoogleSignInAccount(account);

        Application.getApi().registerUser()
            .doOnError(this::showErrorMessage)
            .subscribe();

        startActivity(new Intent(this, DeckListActivity.class));
        finish();
    }

    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage(Throwable throwable) {
        showErrorMessage(throwable.getLocalizedMessage());
    }
}
