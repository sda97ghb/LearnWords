package com.divanoapps.learnwords.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class RequestError {
    public enum Type {
        Undefined, ///< Something bad happened but we don't know what
        ConnectionFailure,
        NotFound,
        Forbidden,
        AlreadyExists,
        PropertyNotExists
    }

    private Type mType;
    private String mMessage;
    private Exception mCause;

    public RequestError(IDB.AlreadyExistsException e) {
        mType = Type.AlreadyExists;
        mMessage = e.getMessage();
        mCause = e;
    }

    public RequestError(IDB.ConnectionFailureException e) {
        mType = Type.ConnectionFailure;
        mMessage = e.getMessage();
        mCause = e;
    }

    public RequestError(IDB.PropertyNotExistsException e) {
        mType = Type.PropertyNotExists;
        mMessage = e.getMessage();
        mCause = e;
    }

    public RequestError(IDB.ForbiddenException e) {
        mType = Type.Forbidden;
        mMessage = e.getMessage();
        mCause = e;
    }

    public RequestError(IDB.NotFoundException e) {
        mType = Type.NotFound;
        mMessage = e.getMessage();
        mCause = e;
    }

    public RequestError(Type type, @NonNull String message) {
        mType = type;
        mMessage = message;
        mCause = null;
    }

    public RequestError(Type type, @NonNull String message, @Nullable Exception cause) {
        mType = type;
        mMessage = message;
        mCause = cause;
    }

    public Type getType() {
        return mType;
    }

    public String getMessage() {
        return mMessage;
    }

    public Exception getCause() {
        return mCause;
    }
}
