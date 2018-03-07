package com.divanoapps.learnwords.data;

import com.divanoapps.learnwords.data.requests.Request;

import org.json.JSONException;

public class InitializationRequest extends Request<Void> {

    InitializationRequest(IDB db) {
        super(db);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            IDB db = getDb();
            if (db instanceof LocalDB)
                ((LocalDB) db).initialize();
            if (db instanceof RemoteDB)
                ((RemoteDB) db).initialize();
            return null;
        } catch (LocalDB.UnableToReadFileException | JSONException e) {
            e.printStackTrace();
            String message = "Unable to load from local storage: " + e.getMessage();
            setError(new RequestError(RequestError.Type.Undefined, message));
            return null;
        } catch (IDB.ConnectionFailureException e) {
            e.printStackTrace();
            String message = "Authorization failure";
            setError(new RequestError(RequestError.Type.Undefined, message));
            return null;
        }
    }
}
