package com.divanoapps.learnwords.data.api2;

import com.google.gson.annotations.SerializedName;

public class ApiCardReference {
    @SerializedName("word")
    private String word;

    @SerializedName("comment")
    private String comment;

    public ApiCardReference() {
    }

    public ApiCardReference(String word, String comment) {
        this.word = word;
        this.comment = comment;
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
}
