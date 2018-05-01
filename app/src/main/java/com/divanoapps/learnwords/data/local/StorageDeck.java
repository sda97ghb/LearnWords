//package com.divanoapps.learnwords.data.local;
//
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;
//
///**
// * Created by dmitry on 29.04.18.
// */
//
//@Entity()
//public class StorageDeck {
//    private Long timestamp;
//
//    @PrimaryKey
//    private String name;
//
//    private String fromLanguage;
//    private String toLanguage;
//
//    public StorageDeck() {
//    }
//
//    public StorageDeck(Long timestamp, String name, String fromLanguage, String toLanguage) {
//        this.timestamp = timestamp;
//        this.name = name;
//        this.fromLanguage = fromLanguage;
//        this.toLanguage = toLanguage;
//    }
//
//    public Long getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(Long timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getFromLanguage() {
//        return fromLanguage;
//    }
//
//    public void setFromLanguage(String fromLanguage) {
//        this.fromLanguage = fromLanguage;
//    }
//
//    public String getToLanguage() {
//        return toLanguage;
//    }
//
//    public void setToLanguage(String toLanguage) {
//        this.toLanguage = toLanguage;
//    }
//}
