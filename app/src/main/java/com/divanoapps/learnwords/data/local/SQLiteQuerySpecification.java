package com.divanoapps.learnwords.data.local;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteProgram;
import android.arch.persistence.db.SupportSQLiteQuery;

import com.divanoapps.learnwords.data.Specification;

/**
 * Created by dmitry on 12.05.18.
 */

public class SQLiteQuerySpecification implements Specification, SupportSQLiteQuery {
    private SimpleSQLiteQuery simpleSQLiteQuery;

    SQLiteQuerySpecification(String query, Object... args) {
        simpleSQLiteQuery = new SimpleSQLiteQuery(query, args);
    }

    @Override
    public String getSql() {
        return simpleSQLiteQuery.getSql();
    }

    @Override
    public void bindTo(SupportSQLiteProgram statement) {
        simpleSQLiteQuery.bindTo(statement);
    }

    @Override
    public int getArgCount() {
        return simpleSQLiteQuery.getArgCount();
    }
}
