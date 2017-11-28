package com.divanoapps.learnwords.data.requests;

import com.divanoapps.learnwords.data.IDB;
import com.divanoapps.learnwords.data.RequestError;
import com.divanoapps.learnwords.entities.Card;

public class SaveCardRequest extends Request<Void> {

    private Card mCard;

    public SaveCardRequest(IDB db, Card card) {
        super(db);
        mCard = card;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            getDb().saveCard(mCard);
            return null;
        }
        catch (IDB.ForbiddenException e) { setError(new RequestError(e)); return null; }
        catch (IDB.NotFoundException e)  { setError(new RequestError(e)); return null; }
    }
}
