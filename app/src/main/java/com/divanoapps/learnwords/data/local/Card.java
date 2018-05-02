package com.divanoapps.learnwords.data.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

/**
 * Created by dmitry on 29.04.18.
 */

@Entity(primaryKeys = {"deckName", "word", "comment"},
        foreignKeys = @ForeignKey(entity = Deck.class,
                                  parentColumns = "name",
                                  childColumns = "deckName",
                                  onDelete = ForeignKey.CASCADE))
public class Card {
    public static Integer getMaxDifficulty() {
        return 30;
    }

    public static Integer getMinDifficulty() {
        return 0;
    }

    public static Integer getDefaultDifficulty() {
        return 10;
    }

    private Long timestamp;

    @NonNull
    private String deckName;

    @NonNull
    private String word;

    @NonNull
    private String comment;

    private String translation;
    private Integer difficulty;
    private Boolean hidden;

    public Card() {
    }

    public Card(Long timestamp, String deckName, String word, String comment,
                String translation, Integer difficulty, Boolean hidden) {
        this.timestamp = timestamp;
        this.deckName = deckName;
        this.word = word;
        this.comment = comment;
        this.translation = translation;
        this.difficulty = difficulty;
        this.hidden = hidden;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Boolean isHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }
}
