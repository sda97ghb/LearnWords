package com.divanoapps.learnwords.data.requests;

import com.divanoapps.learnwords.data.IDB;
import com.divanoapps.learnwords.data.RequestError;
import com.divanoapps.learnwords.entities.CardId;

import java.util.Map;

public class ModifyCardRequest extends Request<Void> {

    private CardId mId;
    private Map<String, Object> mProperties;

    public ModifyCardRequest(IDB db, CardId id, Map<String, Object> properties) {
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
        catch (IDB.ForbiddenException e)         { setError(new RequestError(e)); return null; }
        catch (IDB.NotFoundException e)          { setError(new RequestError(e)); return null; }
        catch (IDB.AlreadyExistsException e)     { setError(new RequestError(e)); return null; }
        catch (IDB.PropertyNotExistsException e) { setError(new RequestError(e)); return null; }
        catch (IDB.ConnectionFailureException e) { setError(new RequestError(e)); return null; }
    }
}
