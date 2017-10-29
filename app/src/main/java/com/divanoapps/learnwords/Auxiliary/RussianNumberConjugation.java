package com.divanoapps.learnwords.Auxiliary;

public class RussianNumberConjugation {
    public static String getCards(int number) {
        if (number > 10 && number < 20)
            return "карточек";
        switch (number % 10) {
            case 0: return "карточек";
            case 1: return "карточка";
            case 2: return "карточки";
            case 3: return "карточки";
            case 4: return "карточки";
            case 5: return "карточек";
            case 6: return "карточек";
            case 7: return "карточек";
            case 8: return "карточек";
            case 9: return "карточек";
        }
        return "карточек";
    }

    public static String getHidden(int number) {
        if (number == 11)
            return "скрыты";
        if (number % 10 == 1)
            return "срыта";
        return "скрыты";
    }
}
