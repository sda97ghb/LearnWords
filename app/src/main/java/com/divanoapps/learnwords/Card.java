package com.divanoapps.learnwords;

// TODO: Card may not have translation.

/**
 * This class represents single card with original word, translation of this word,
 * picture existence flag, visibility flag and difficulty.
 */
public class Card {
    private String originalWord;
    private String translation;

    public static int DEFAULT_DIFFICULTY = 10;
    public static int MAX_DIFFICULTY = 30;

    private int difficulty = DEFAULT_DIFFICULTY;

    private boolean hasPicture = false;
    private boolean isVisible = true;

    public Card() {
        this.originalWord = "";
        this.translation = "";
    }

    public Card(String originalWord, String translation, boolean hasPicture, int difficulty, boolean isVisible)
    {
        this.originalWord = originalWord;
        this.translation = translation;
        this.hasPicture = hasPicture;
        this.difficulty = difficulty;
        this.isVisible = isVisible;
    }

    public String getOriginalWord() {
        return this.originalWord;
    }

    public String getTranslation() {
        return this.translation;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public boolean hasPicture() {
        return this.hasPicture;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void setOriginalWord(String originalWord) {
        this.originalWord = originalWord;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public void setDifficulty(int difficulty) {
        // TODO: add error when rating is invalid
        if (difficulty < 0 || difficulty > MAX_DIFFICULTY)
            return;
        this.difficulty = difficulty;
    }

    public void increaseDifficulty() {
        if (difficulty < MAX_DIFFICULTY)
            ++ difficulty;
    }

    public void decreaseDifficulty() {
        if (difficulty > 0)
            -- difficulty;
    }

    public void switchVisibility() {
        isVisible = !isVisible;
    }

    public boolean hasTranslation() {
        return !(getTranslation().trim().equals(""));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Card))
            return false;
        Card other = (Card) obj;
        return this.getOriginalWord().equals(other.getOriginalWord()) &&
                this.getTranslation().equals(other.getTranslation()) &&
                this.getDifficulty() == other.getDifficulty() &&
                this.hasPicture() == other.hasPicture() &&
                this.isVisible() == other.isVisible();
    }

    @Override
    public String toString() {
        return "Card {" +
                "originalWord=" + getOriginalWord() + ", " +
                "translation=" + getTranslation() + ", " +
                "difficulty=" + getDifficulty() + ", " +
                "hasPicture=" + hasPicture() + ", " +
                "isVisible=" + isVisible() + "}";
    }
}
