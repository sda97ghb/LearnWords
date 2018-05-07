package com.divanoapps.learnwords.data.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
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

    private Integer sync;

    private Long timestamp;

    @NonNull
    private String deckName = "";

    @NonNull
    private String word = "";

    @NonNull
    private String comment = "";

    private String translation;
    private Integer difficulty;
    private Boolean hidden;

    @Ignore
    public Card() {
    }

    public Card(Integer sync, Long timestamp, @NonNull String deckName, @NonNull String word,
                @NonNull String comment, String translation, Integer difficulty, Boolean hidden) {
        this.sync = sync;
        this.timestamp = timestamp;
        this.deckName = deckName;
        this.word = word;
        this.comment = comment;
        this.translation = translation;
        this.difficulty = difficulty;
        this.hidden = hidden;
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

    @NonNull
    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(@NonNull String deckName) {
        this.deckName = deckName;
    }

    @NonNull
    public String getWord() {
        return word;
    }

    public void setWord(@NonNull String word) {
        this.word = word;
    }

    @NonNull
    public String getComment() {
        return comment;
    }

    public void setComment(@NonNull String comment) {
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
