package com.divanoapps.learnwords.use_case;

import com.divanoapps.learnwords.data.local.Card;
import com.divanoapps.learnwords.data.local.CardRepository;
import com.divanoapps.learnwords.data.local.CardSpecificationsFactory;

import java.util.List;

/**
 * Created by dmitry on 12.05.18.
 */

public class MoveCardsFromOneDeckToAnother {
    public static void execute(String oldDeckName, String newDeckName, CardRepository cardRepository) {
        List<Card> cardList = cardRepository.query(CardSpecificationsFactory.cardsWithDeckName(oldDeckName));
        Card[] cards = cardList.toArray(new Card[cardList.size()]);
        cardRepository.delete(cards);
        for (Card card : cards)
            card.setDeckName(newDeckName);
        cardRepository.insert(cards);
    }
}
