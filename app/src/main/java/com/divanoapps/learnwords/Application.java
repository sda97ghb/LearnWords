package com.divanoapps.learnwords;

import android.support.v7.app.AppCompatActivity;

import com.divanoapps.learnwords.YandexDictionary.YandexDictionaryService;
import com.divanoapps.learnwords.data.api2.Api;
import com.divanoapps.learnwords.data.api2.ServiceExecutor;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Application {

    // Random

    public static String getDefaultDeckName() {
        return "Unsorted";
    }

    // Yandex Dictionary

    public static final Retrofit yandexRetrofit = new Retrofit.Builder()
        .baseUrl(YandexDictionaryService.getApiUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();

    public static final YandexDictionaryService yandexDictionaryService = yandexRetrofit.create(YandexDictionaryService.class);

    public static final String FAKE_DIRECTION = "en-ru";

    // Learn Words Api

    private static ServiceExecutor<Api> apiServiceExecutor = new ServiceExecutor<>(Api.class);

    public static void initializeApiFromGoogleSignInAccount(GoogleSignInAccount googleSignInAccount) {
        apiServiceExecutor = new ServiceExecutor<>(Api.class, googleSignInAccount.getIdToken());
    }

//    public static Api getApi() {
//        return apiServiceExecutor.getService();
//    }

    // Google Sign In

    private static String getOAuth2WebClientId() {
        return "267917494384-oh291kv8mg1d8iov2epiojfg3apolbl8.apps.googleusercontent.com";
    }

    public static GoogleApiClient getGoogleSignInApiClient(AppCompatActivity activity,
                           GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        GoogleSignInOptions googleSignInOptions =
            new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getOAuth2WebClientId())
                .requestEmail()
                .build();
        return new GoogleApiClient.Builder(activity)
            .enableAutoManage(activity, onConnectionFailedListener)
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
            .build();
    }

    private static GoogleSignInAccount googleSignInAccount;

    public static void setGoogleSignInAccount(GoogleSignInAccount googleSignInAccount) {
        Application.googleSignInAccount = googleSignInAccount;
    }

    public static GoogleSignInAccount getGoogleSignInAccount() {
        return googleSignInAccount;
    }
}
