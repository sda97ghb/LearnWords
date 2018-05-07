package com.divanoapps.learnwords.data.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by dmitry on 29.04.18.
 */

@Entity()
public class Deck {
    private Integer sync;

    private Long timestamp;

    @PrimaryKey
    @NonNull
    private String name = "";

    private String fromLanguage;
    private String toLanguage;

    @Ignore
    private List<Card> cards;

    @Ignore
    public Deck() {
    }

    public Deck(Integer sync, Long timestamp, @NonNull String name, String fromLanguage, String toLanguage) {
        this.sync = sync;
        this.timestamp = timestamp;
        this.name = name;
        this.fromLanguage = fromLanguage;
        this.toLanguage = toLanguage;
    }

    @Ignore
    public Deck(Integer sync, Long timestamp, @NonNull String name, String fromLanguage, String toLanguage, List<Card> cards) {
        this.sync = sync;
        this.timestamp = timestamp;
        this.name = name;
        this.fromLanguage = fromLanguage;
        this.toLanguage = toLanguage;
        this.cards = cards;
    }

    public Integer getSync() {
        return sync;
    }

    public void setSync(Integer sync) {
        this.sync = sync;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromLanguage() {
        return fromLanguage;
    }

    public void setFromLanguage(String fromLanguage) {
        this.fromLanguage = fromLanguage;
    }

    public String getToLanguage() {
        return toLanguage;
    }

    public void setToLanguage(String toLanguage) {
        this.toLanguage = toLanguage;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
