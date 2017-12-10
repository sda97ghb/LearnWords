package com.divanoapps.learnwords.data.requests;

import com.divanoapps.learnwords.data.IDB;
import com.divanoapps.learnwords.data.RequestError;
import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.CardId;

public class UpdateCardRequest extends Request<Void> {

    private CardId mId;
    private Card mNewCard;

    public UpdateCardRequest(IDB db, CardId id, Card newCard) {
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
        catch (IDB.ForbiddenException e)     { setError(new RequestError(e)); return null; }
        catch (IDB.NotFoundException e)      { setError(new RequestError(e)); return null; }
        catch (IDB.AlreadyExistsException e) { setError(new RequestError(e)); return null; }
        catch (IDB.ConnectionFailureException e) { setError(new RequestError(e)); return null; }
    }
}
