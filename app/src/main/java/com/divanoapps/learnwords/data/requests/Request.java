package com.divanoapps.learnwords.data.requests;

import android.os.AsyncTask;

import com.divanoapps.learnwords.data.IDB;
import com.divanoapps.learnwords.data.RequestError;

/**
 * Abstract asynchronous request.
 * Call onDone listener when done and onError if error happened.
 * @param <T> Type of requested value or Void.
 */
public abstract class Request<T> extends AsyncTask<Void, Void, T> {

    private IDB mDb;
    private RequestError mError = null;

    public Request(IDB db) {
        mDb = db;
    }

    protected IDB getDb() {
        return mDb;
    }

    private RequestError getError() {
        return mError;
    }

    protected void setError(RequestError error) {
        mError = error;
    }

    // Listeners

    public interface OnDoneListener<T> {
        void onDone(T result);
    }

    public interface NoArgsOnDoneListener {
        void onDone();
    }

    public interface OnErrorListener {
        void onError(RequestError error);
    }

    public interface NoArgsOnErrorListener {
        void onError();
    }

    private OnDoneListener<T> mOnDoneListener = result -> {};
    private OnErrorListener mOnErrorListener = error -> {};

    public Request<T> setOnDoneListener(OnDoneListener<T> listener) {
        mOnDoneListener = listener;
        return this;
    }

    public Request<T> setOnDoneListener(NoArgsOnDoneListener listener) {
        mOnDoneListener = result -> listener.onDone();
        return this;
    }

    public Request<T> setOnErrorListener(OnErrorListener listener) {
        mOnErrorListener = listener;
        return this;
    }

    public Request<T> setOnErrorListener(NoArgsOnErrorListener listener) {
        mOnErrorListener = result -> listener.onError();
        return this;
    }

    private void notifyDone(T result) {
        mOnDoneListener.onDone(result);
    }

    private void notifyError(RequestError error) {
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