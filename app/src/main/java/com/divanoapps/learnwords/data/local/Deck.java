package com.divanoapps.learnwords.data.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by dmitry on 29.04.18.
 */

@Entity()
public class Deck implements Parcelable {
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

    @Ignore
    public Deck(Long timestamp, @NonNull String name, String fromLanguage, String toLanguage) {
        this.sync = Sync.ADD;
        this.timestamp = timestamp;
        this.name = name;
        this.fromLanguage = fromLanguage;
        this.toLanguage = toLanguage;
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

    protected Deck(Parcel in) {
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
        name = in.readString();
        fromLanguage = in.readString();
        toLanguage = in.readString();
        cards = in.createTypedArrayList(Card.CREATOR);
    }

    public static final Creator<Deck> CREATOR = new Creator<Deck>() {
        @Override
        public Deck createFromParcel(Parcel in) {
            return new Deck(in);
        }

        @Override
        public Deck[] newArray(int size) {
            return new Deck[size];
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
        dest.writeString(name);
        dest.writeString(fromLanguage);
        dest.writeString(toLanguage);
        dest.writeTypedList(cards);
    }
}
