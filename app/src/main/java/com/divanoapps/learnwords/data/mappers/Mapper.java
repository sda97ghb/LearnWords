package com.divanoapps.learnwords.data.mappers;

/**
 * Created by dmitry on 29.04.18.
 */

public interface Mapper<TStorage, TApi> {
    TApi mapStorageToApi(TStorage storageObject);
    TStorage mapApiToStorage(TApi apiObject);
}
