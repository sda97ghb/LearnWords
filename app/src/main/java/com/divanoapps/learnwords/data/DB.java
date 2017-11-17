package com.divanoapps.learnwords.data;

import android.os.AsyncTask;

import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.CardId;
import com.divanoapps.learnwords.entities.Deck;
import com.divanoapps.learnwords.entities.DeckId;
import com.divanoapps.learnwords.entities.DeckShort;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

/**
 * Created by dmitry on 08.10.17.
 */

public class DB {
    //////////////////////
    // Singleton interface

    private static volatile DB instance;

    public static DB getInstance() {
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

    private IDB mDb = new LocalDB();

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

    public static ModifyCardRequest modifyCard(CardId id, Map<String, Object> properties) {
        return new ModifyCardRequest(getDb(), id, properties);
    }

    public static UpdateCardRequest updateCard(CardId id, Card newCard) {
        return new UpdateCardRequest(getDb(), id, newCard);
    }

    public static DeleteCardRequest deleteCard(CardId id) {
        return new DeleteCardRequest(getDb(), id);
    }

    public static class Error {
        enum Type {
            Undefined, ///< Something bad happened but we don't know what
            ConnectionFailure,
            NotFound,
            Forbidden
        }

        private Type mType;
        private String mMessage;

        Error(Type type, String message) {
            mType = type;
            mMessage = message;
        }

        public Type getType() {
            return mType;
        }

        public String getMessage() {
            return mMessage;
        }
    }

    /**
     * Abstract asynchronous request.
     * Call onDone listener when done and onError if error happened.
     * @param <T> Type of requested value.
     */
    public static abstract class Request<T> extends AsyncTask<Void, Void, T> {

        private IDB mDb;

        private Error mError = null;

        Request(IDB db) {
            mDb = db;
        }

        public IDB getDb() {
            return mDb;
        }

        public Error getError() {
            return mError;
        }

        public void setError(Error error) {
            mError = error;
        }

        public interface OnDoneListener<T> {
            void onDone(T result);
        }

        public interface OnErrorListener {
            void onError(Error error);
        }

        private OnDoneListener<T> mOnDoneListener = result -> {};
        private OnErrorListener mOnErrorListener = error -> {};

        public Request<T> setOnDoneListener(OnDoneListener<T> listener) {
            mOnDoneListener = listener;
            return this;
        }

        public Request<T> setOnErrorListener(OnErrorListener listener) {
            mOnErrorListener = listener;
            return this;
        }

        private void notifyDone(T result) {
            mOnDoneListener.onDone(result);
        }

        private void notifyError(Error error) {
            mOnErrorListener.onError(error);
        }

        @Override
        protected void onPostExecute(T result) {
            if (getError() == null)
                notifyDone(result);
            else
                notifyError(getError());
        }
    }

    public static class InitializationRequest extends Request<Void> {

        InitializationRequest(IDB db) {
            super(db);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                IDB db = getDb();
                if (db instanceof LocalDB)
                    ((LocalDB) db).initialize();
                return null;
            } catch (LocalDB.UnableToReadFileException | JSONException e) {
                e.printStackTrace();
                String message = "Unable to load from local storage: " + e.getMessage();
                setError(new Error(Error.Type.Undefined, message));
                return null;
            }
        }
    }

    public static class DeckListRequest extends Request<List<DeckShort>> {

        DeckListRequest(IDB db) {
            super(db);
        }

        @Override
        protected List<DeckShort> doInBackground(Void... params) {
            try {
                return getDb().getDecks();
            } catch (Exception e) {
                e.printStackTrace();
                setError(new Error(Error.Type.Undefined, e.getMessage()));
                return null;
            }
        }
    }

    public static class GetDeckRequest extends Request<Deck> {

        private DeckId mId;

        GetDeckRequest(IDB db, DeckId id) {
            super(db);
            mId = id;
        }

        @Override
        protected Deck doInBackground(Void... params) {
            try {
                IDB db = getDb();
                return db.getDeck(mId);
            }
            catch (IDB.NotFoundException e) {
                e.printStackTrace();
                setError(new Error(Error.Type.NotFound, e.getMessage()));
                return null;
            }
        }
    }

    public static class SaveDeckRequest extends Request<Void> {

        private Deck mDeck;

        SaveDeckRequest(IDB db, Deck deck) {
            super(db);
            mDeck = deck;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                getDb().saveDeck(mDeck);
                return null;
            }
            catch (IDB.ForbiddenException e) {
                e.printStackTrace();
                setError(new Error(Error.Type.Forbidden, e.getMessage()));
                return null;
            }
        }
    }

    public static class ModifyDeckRequest extends Request<Void> {

        private DeckId mId;
        private Map<String, Object> mProperties;

        ModifyDeckRequest(IDB db, DeckId id, Map<String, Object> properties) {
            super(db);
            mId = id;
            mProperties = properties;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                getDb().modifyDeck(mId, mProperties);
                return null;
            }
            catch (IDB.ForbiddenException e) {
                e.printStackTrace();
                setError(new Error(Error.Type.Forbidden, e.getMessage()));
                return null;
            } catch (IDB.NotFoundException e) {
                e.printStackTrace();
                setError(new Error(Error.Type.NotFound, e.getMessage()));
                return null;
            }
        }
    }

    public static class DeleteDeckRequest extends Request<Void> {

        private DeckId mId;

        DeleteDeckRequest(IDB db, DeckId id) {
            super(db);
            mId = id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                getDb().deleteDeck(mId);
                return null;
            }
            catch (IDB.ForbiddenException e) {
                e.printStackTrace();
                setError(new Error(Error.Type.Forbidden, e.getMessage()));
                return null;
            } catch (IDB.NotFoundException e) {
                e.printStackTrace();
                setError(new Error(Error.Type.NotFound, e.getMessage()));
                return null;
            }
        }
    }

    public static class GetCardRequest extends Request<Card> {

        private CardId mId;

        GetCardRequest(IDB db, CardId id) {
            super(db);
            mId = id;
        }

        @Override
        protected Card doInBackground(Void... params) {
            try {
                return getDb().getCard(mId);
            }
            catch (IDB.NotFoundException e) {
                e.printStackTrace();
                setError(new Error(Error.Type.NotFound, e.getMessage()));
                return null;
            }
        }
    }

    public static class SaveCardRequest extends Request<Void> {

        private Card mCard;

        SaveCardRequest(IDB db, Card card) {
            super(db);
            mCard = card;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                getDb().saveCard(mCard);
                return null;
            }
            catch (IDB.ForbiddenException e) {
                e.printStackTrace();
                setError(new Error(Error.Type.Forbidden, e.getMessage()));
                return null;
            }
        }
    }

    public static class ModifyCardRequest extends Request<Void> {

        private CardId mId;
        private Map<String, Object> mProperties;

        ModifyCardRequest(IDB db, CardId id, Map<String, Object> properties) {
            super(db);
            mId = id;
            mProperties = properties;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                getDb().modifyCard(mId, mProperties);
                return null;
            }
            catch (IDB.ForbiddenException | IDB.NotFoundException e) {
                e.printStackTrace();
                setError(new Error(Error.Type.Forbidden, e.getMessage()));
                return null;
            }
        }
    }

    public static class UpdateCardRequest extends Request<Void> {

        private CardId mId;
        private Card mNewCard;

        UpdateCardRequest(IDB db, CardId id, Card newCard) {
            super(db);
            mId = id;
            mNewCard = newCard;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                getDb().updateCard(mId, mNewCard);
                return null;
            }
            catch (IDB.ForbiddenException | IDB.NotFoundException e) {
                e.printStackTrace();
                setError(new Error(Error.Type.Forbidden, e.getMessage()));
                return null;
            }
        }
    }

    public static class DeleteCardRequest extends Request<Void> {

        private CardId mId;

        DeleteCardRequest(IDB db, CardId id) {
            super(db);
            mId = id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                getDb().deleteCard(mId);
                return null;
            }
            catch (IDB.ForbiddenException e) {
                e.printStackTrace();
                setError(new Error(Error.Type.Forbidden, e.getMessage()));
                return null;
            } catch (IDB.NotFoundException e) {
                e.printStackTrace();
                setError(new Error(Error.Type.NotFound, e.getMessage()));
                return null;
            }
        }
    }
}
