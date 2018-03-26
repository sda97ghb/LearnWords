package com.divanoapps.learnwords.data.api2;

import java.util.Objects;

public class RequestId {
    private String entity;
    private String method;

    public RequestId(String entity, String method) {
        this.entity = entity;
        this.method = method;
    }

    public String getEntity() {
        return entity;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, method);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RequestId))
            return false;
        RequestId other = (RequestId) obj;
        return other.entity.equals(entity) &&
            other.method.equals(method);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("entity = ").append(entity).append(", method = ").append(method).toString();
    }
}
