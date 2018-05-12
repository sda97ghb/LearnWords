package com.divanoapps.learnwords.use_case;

import com.divanoapps.learnwords.data.local.CardRepository;
import com.divanoapps.learnwords.data.local.Deck;
import com.divanoapps.learnwords.data.local.DeckRepository;
import com.divanoapps.learnwords.data.local.Sync;
import com.divanoapps.learnwords.data.local.TimestampFactory;

/**
 * Created by dmitry on 12.05.18.
 */

public class RenameDeck {
    public static void execute(Deck oldDeck, String newName, DeckRepository deckRepository, CardRepository cardRepository) {
        Deck newDeck = new Deck();

        newDeck.setSync(Sync.ADD);
        newDeck.setTimestamp(TimestampFactory.getTimestamp());

        newDeck.setName(newName);
        newDeck.setFromLanguage(oldDeck.getFromLanguage());
        newDeck.setToLanguage(oldDeck.getToLanguage());

        deckRepository.insert(newDeck);

        MoveCardsFromOneDeckToAnother.execute(oldDeck.getName(), newName, cardRepository);

        deckRepository.delete(oldDeck);
    }
}
