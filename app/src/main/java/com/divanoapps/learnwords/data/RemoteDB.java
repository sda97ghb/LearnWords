package com.divanoapps.learnwords.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.CardId;
import com.divanoapps.learnwords.entities.Deck;
import com.divanoapps.learnwords.entities.DeckId;
import com.divanoapps.learnwords.entities.DeckShort;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RemoteDB implements IDB {

    static class ApiError {
        private int mCode;
        private String mDescription;

        ApiError(int code, String description) {
            mCode = code;
            mDescription = description;
        }

        int getCode() {
            return mCode;
        }

        String getDescription() {
            return mDescription;
        }
    }

    //Private

    private static String getServerAddress() {
        return "10.97.128.63:8080";
    }

    private static String getApiVersion() {
        return "0.1";
    }

    private List<DeckShort> parseDeckList(String json) throws JSONException {
        JSONArray array = new JSONArray(json);
        List<DeckShort> decks = new LinkedList<>();

        for (int i = 0; i < array.length(); ++ i)
            decks.add(DeckShort.fromJson(array.getJSONObject(i)));

        return decks;
    }

    private String request(@NonNull String requestJson) throws ConnectionFailureException {
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL("http://" + getServerAddress() + "/learnwordsapi/" + getApiVersion());

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(false);
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            byte[] bodyBytes = requestJson.getBytes(StandardCharsets.UTF_8);

            connection.setRequestProperty("Content-Length", Integer.toString(bodyBytes.length));
            connection.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(bodyBytes);
            }
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new ConnectionFailureException();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();
            if (line == null)
                throw new ConnectionFailureException();
            reader.close();

            return line;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new ConnectionFailureException();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    private String requestString(@NonNull String entity, @NonNull String method, @Nullable String selector, @Nullable String body) throws ConnectionFailureException {
        URL url;
        HttpURLConnection connection = null;
        try {
            if (selector == null)
                url = new URL("http://" + getServerAddress() + "/" + entity);
            else
                url = new URL("http://" + getServerAddress() + "/" + entity + "?" + selector);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-HTTP-Method-Override", method);
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(false);
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            if (body != null) {
                byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

                connection.setRequestProperty("Content-Length", Integer.toString(bodyBytes.length));
                connection.setDoOutput(true);
                try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                    wr.write(bodyBytes);
                }
                connection.connect();
            }

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new ConnectionFailureException();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();
            if (line == null)
                throw new ConnectionFailureException();
            reader.close();

            return line;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new ConnectionFailureException();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    // Public

    @Override
    public List<DeckShort> getDecks() throws ConnectionFailureException {
        try {
            JSONObject requestJson = new JSONObject() {{
                put("entity", "decks");
                put("method", "get");
            }};

            String entireResponseString = request(requestJson.toString());
            JSONObject entireResponseJson = new JSONObject(entireResponseString);

            if (entireResponseJson.has("error")) {
                JSONObject errorJson = entireResponseJson.getJSONObject("error");
                ApiError error = new ApiError(errorJson.getInt("code"),
                                           errorJson.getString("description"));
                throw new ConnectionFailureException();
            }
            else if (entireResponseJson.has("response")) {
                JSONObject responseJson = entireResponseJson.getJSONObject("response");
                JSONArray array = responseJson.getJSONArray("decks");
                List<DeckShort> decks = new LinkedList<>();

                for (int i = 0; i < array.length(); ++ i)
                    decks.add(DeckShort.fromJson(array.getJSONObject(i)));

                return decks;
            }
            else {
                throw new ConnectionFailureException();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ConnectionFailureException();
        }
    }

    @Override
    public Deck getDeck(DeckId id) throws NotFoundException, ConnectionFailureException {
        try {
            JSONObject requestJson = new JSONObject() {{
                put("entity", "deck");
                put("method", "get");
                put("id", id.toJson());
            }};

            String entireResponseString = request(requestJson.toString());
            JSONObject entireResponseJson = new JSONObject(entireResponseString);

            if (entireResponseJson.has("error")) {
                JSONObject errorJson = entireResponseJson.getJSONObject("error");
                ApiError error = new ApiError(errorJson.getInt("code"),
                        errorJson.getString("description"));
                throw new ConnectionFailureException();
            }
            else if (entireResponseJson.has("response")) {
                JSONObject responseJson = entireResponseJson.getJSONObject("response");
                JSONObject deckJson = responseJson.getJSONObject("deck");
                return Deck.fromJson(deckJson);
            }
            else {
                throw new ConnectionFailureException();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ConnectionFailureException();
        }
    }

    @Override
    public void saveDeck(Deck deck) throws ConnectionFailureException, ForbiddenException {
        try {
            JSONObject requestJson = new JSONObject() {{
                put("entity", "deck");
                put("method", "save");
                put("deck", deck.toJson());
            }};

            String entireResponseString = request(requestJson.toString());
            JSONObject entireResponseJson = new JSONObject(entireResponseString);

            if (entireResponseJson.has("error")) {
                JSONObject errorJson = entireResponseJson.getJSONObject("error");
                ApiError error = new ApiError(errorJson.getInt("code"),
                        errorJson.getString("description"));
                throw new ConnectionFailureException();
            }
            else if (entireResponseJson.has("response")) {
                JSONObject responseJson = entireResponseJson.getJSONObject("response");
                return;
            }
            else {
                throw new ConnectionFailureException();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ConnectionFailureException();
        }
    }

    @Override
    public void updateDeck(DeckId id, Deck newDeck) throws NotFoundException, AlreadyExistsException, ForbiddenException {

    }

    @Override
    public void modifyDeck(DeckId id, Map<String, Object> properties) throws NotFoundException, PropertyNotExistsException, AlreadyExistsException, ForbiddenException {

    }

    @Override
    public void deleteDeck(DeckId id) throws NotFoundException, ForbiddenException, ConnectionFailureException {
        try {
            JSONObject requestJson = new JSONObject() {{
                put("entity", "deck");
                put("method", "delete");
                put("id", id.toJson());
            }};

            String entireResponseString = request(requestJson.toString());
            JSONObject entireResponseJson = new JSONObject(entireResponseString);

            if (entireResponseJson.has("error")) {
                JSONObject errorJson = entireResponseJson.getJSONObject("error");
                ApiError error = new ApiError(errorJson.getInt("code"),
                        errorJson.getString("description"));
                throw new ConnectionFailureException();
            }
            else if (entireResponseJson.has("response")) {
                JSONObject responseJson = entireResponseJson.getJSONObject("response");
                return;
            }
            else {
                throw new ConnectionFailureException();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ConnectionFailureException();
        }
    }

    @Override
    public Card getCard(CardId id) throws NotFoundException, ConnectionFailureException {
        try {
            JSONObject requestJson = new JSONObject() {{
                put("entity", "card");
                put("method", "get");
                put("id", id.toJson());
            }};

            String entireResponseString = request(requestJson.toString());
            JSONObject entireResponseJson = new JSONObject(entireResponseString);

            if (entireResponseJson.has("error")) {
                JSONObject errorJson = entireResponseJson.getJSONObject("error");
                ApiError error = new ApiError(errorJson.getInt("code"),
                        errorJson.getString("description"));
                throw new ConnectionFailureException();
            }
            else if (entireResponseJson.has("response")) {
                JSONObject responseJson = entireResponseJson.getJSONObject("response");
                JSONObject cardJson = responseJson.getJSONObject("card");
                String deckName = cardJson.getString("deck");
                return Card.fromJson(deckName, cardJson);
            }
            else {
                throw new ConnectionFailureException();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ConnectionFailureException();
        }
    }

    @Override
    public void saveCard(Card card) throws ConnectionFailureException, NotFoundException, ForbiddenException {
        requestString("card", "save", null, card.toJson().toString());
    }

    @Override
    public void updateCard(CardId id, Card newCard) throws NotFoundException, AlreadyExistsException, ForbiddenException {

    }

    @Override
    public void modifyCard(CardId id, Map<String, Object> properties) throws ForbiddenException, NotFoundException, AlreadyExistsException, PropertyNotExistsException {

    }

    @Override
    public void deleteCard(CardId id) throws ForbiddenException, NotFoundException {

    }
}
