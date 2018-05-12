package com.divanoapps.learnwords.data;

import java.util.List;

/**
 * Created by dmitry on 12.05.18.
 */

public interface Repository<T> {
    void insert(T... items);
    void update(T item);
    void delete(T... items);
//    void delete(Specification specification);
    List<T> query(Specification specification);
}