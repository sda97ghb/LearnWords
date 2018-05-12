package com.divanoapps.learnwords.data.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by dmitry on 29.04.18.
 */

@Entity(primaryKeys = {"deckName", "word", "comment"},
        foreignKeys = @ForeignKey(entity = Deck.class,
                                  parentColumns = "name",
                                  childColumns = "deckName",
                                  onDelete = ForeignKey.CASCADE))
public class Card implements Parcelable {

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

    @Ignore
    public Card(Long timestamp, @NonNull String deckName, @NonNull String word, @NonNull String comment, String translation, Integer difficulty, Boolean hidden) {
        this.sync = Sync.ADD;
        this.timestamp = timestamp;
        this.deckName = deckName;
        this.word = word;
        this.comment = comment;
        this.translation = translation;
        this.difficulty = difficulty;
        this.hidden = hidden;
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

    @Ignore
    protected Card(Parcel in) {
        if (in.readByte() == 0) {
            sync = null;
        } else {
            sync = in.readInt();
        }
        if (in.readByte() == 0) {
            timestamp = null;
        } else {
            timestamp = in.readLong();
        }
        deckName = in.readString();
        word = in.readString();
        comment = in.readString();
        translation = in.readString();
        if (in.readByte() == 0) {
            difficulty = null;
        } else {
            difficulty = in.readInt();
        }
        byte tmpHidden = in.readByte();
        hidden = tmpHidden == 0 ? null : tmpHidden == 1;
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (sync == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sync);
        }
        if (timestamp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(timestamp);
        }
        dest.writeString(deckName);
        dest.writeString(word);
        dest.writeString(comment);
        dest.writeString(translation);
        if (difficulty == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(difficulty);
        }
        dest.writeByte((byte) (hidden == null ? 0 : hidden ? 1 : 2));
    }
}
