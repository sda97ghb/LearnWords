package com.divanoapps.learnwords.data;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by dmitry on 12.05.18.
 */

public class RxRepository<T> {
    private Repository<T> repository;

    public RxRepository(Repository<T> repository) {
        this.repository = repository;
    }

    public Completable insert(T... items) {
        return Completable.fromAction(() -> repository.insert(items));
    }

    public Completable update(T item) {
        return Completable.fromAction(() -> repository.update(item));
    }

    public Completable delete(T... items) {
        return Completable.fromAction(() -> repository.delete(items));
    }

//    public Completable delete(Specification specification) {
//        return Completable.fromAction(() -> repository.delete(specification));
//    }

    public Single<List<T>> query(Specification specification) {
        return Single.fromCallable(() -> repository.query(specification));
    }
}