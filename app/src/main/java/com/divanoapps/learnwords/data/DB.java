package com.divanoapps.learnwords.data;

import com.divanoapps.learnwords.data.requests.DeckListRequest;
import com.divanoapps.learnwords.data.requests.DeleteCardRequest;
import com.divanoapps.learnwords.data.requests.DeleteDeckRequest;
import com.divanoapps.learnwords.data.requests.GetCardRequest;
import com.divanoapps.learnwords.data.requests.GetDeckRequest;
import com.divanoapps.learnwords.data.requests.ModifyCardRequest;
import com.divanoapps.learnwords.data.requests.ModifyDeckRequest;
import com.divanoapps.learnwords.data.requests.SaveCardRequest;
import com.divanoapps.learnwords.data.requests.SaveDeckRequest;
import com.divanoapps.learnwords.data.requests.UpdateCardRequest;
import com.divanoapps.learnwords.data.requests.UpdateDeckRequest;
import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.CardId;
import com.divanoapps.learnwords.entities.Deck;
import com.divanoapps.learnwords.entities.DeckId;

import java.util.Map;

/**
 * DB singleton that contains instance of DB and provides system of requests to interact with it.
 */
public class DB {
    //////////////////////
    // Singleton interface

    private static volatile DB instance;

    private static DB getInstance() {
        DB localInstance = instance;
        if (localInstance == null) {
            synchronized (DB.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DB();
                }
            }
        }
        return localInstance;
    }

    //////////////////////
    // DB primary business

    // Private

    private IDB mDb = new RemoteDB(); // new LocalDB();

    // Public

    public static IDB getDb() {
        return getInstance().mDb;
    }

    public static InitializationRequest initialize() {
        return new InitializationRequest(getDb());
    }

    public static DeckListRequest getDecks() {
        return new DeckListRequest(getDb());
    }

    public static GetDeckRequest getDeck(DeckId id) {
        return new GetDeckRequest(getDb(), id);
    }

    public static SaveDeckRequest saveDeck(Deck deck) {
        return new SaveDeckRequest(getDb(), deck);
    }

    public static UpdateDeckRequest updateDeck(DeckId id, Deck newDeck) {
        return new UpdateDeckRequest(getDb(), id, newDeck);
    }

    public static ModifyDeckRequest modifyDeck(DeckId id, Map<String, Object> properties) {
        return new ModifyDeckRequest(getDb(), id, properties);
    }

    public static DeleteDeckRequest deleteDeck(DeckId id) {
        return new DeleteDeckRequest(getDb(), id);
    }

    public static GetCardRequest getCard(CardId id) {
        return new GetCardRequest(getDb(), id);
    }

    public static SaveCardRequest saveCard(Card card) {
        return new SaveCardRequest(getDb(), card);
    }

    public static UpdateCardRequest updateCard(CardId id, Card newCard) {
        return new UpdateCardRequest(getDb(), id, newCard);
    }

    public static ModifyCardRequest modifyCard(CardId id, Map<String, Object> properties) {
        return new ModifyCardRequest(getDb(), id, properties);
    }

    public static DeleteCardRequest deleteCard(CardId id) {
        return new DeleteCardRequest(getDb(), id);
    }
}
