package com.divanoapps.learnwords.data.requests;

import com.divanoapps.learnwords.data.IDB;
import com.divanoapps.learnwords.data.RequestError;
import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.CardId;

public class GetCardRequest extends Request<Card> {

    private CardId mId;

    public GetCardRequest(IDB db, CardId id) {
        super(db);
        mId = id;
    }

    @Override
    protected Card doInBackground(Void... params) {
        try {
            return getDb().getCard(mId);
        }
        catch (IDB.NotFoundException e)          { setError(new RequestError(e)); return null; }
        catch (IDB.ConnectionFailureException e) { setError(new RequestError(e)); return null; }
    }
}
