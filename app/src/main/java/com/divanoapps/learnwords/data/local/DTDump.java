//package com.divanoapps.learnwords.data.local;
//
///**
// * Created by dmitry on 29.04.18.
// */
//
//public class DTDump {
////    private long dt;
////
////    @SerializedName("decks")
////    private List<DeckDTDump> deckDTDumpList;
////
////    public DTDump(Context context, String email) {
////        StorageUser user = new StorageUserRepository(context).getByEmail(email);
////        dt = user.getTimestamp();
////        List<DeckDTDump> deckDTDumps = new ArrayList<>(user.getPersonalDecks().size());
////        for (long deckId : user.getPersonalDecks())
////            deckDTDumps.add(new DeckDTDump(context, deckId));
////        deckDTDumpList = deckDTDumps;
////    }
////
////    public long getDt() {
////        return dt;
////    }
////
////    public List<DeckDTDump> getDeckDTDumpList() {
////        return deckDTDumpList;
////    }
////
////    public static class DeckDTDump {
////        String name;
////        long dt;
////
////        @SerializedName("cards")
////        List<CardDTDump> cardDTDumpList;
////
////        DeckDTDump(Context context, long id) {
////            StorageDeck deck = new StorageDeckRepository(context).getById(id);
////            name = deck.getName();
////            dt = deck.getTimestamp();
////            List<CardDTDump> cardDTDumps = new ArrayList<>(deck.getCards().size());
////            for (long cardId : deck.getCards())
////                cardDTDumps.add(new CardDTDump(context, cardId));
////            cardDTDumpList = cardDTDumps;
////        }
////
////        public String getName() {
////            return name;
////        }
////
////        public long getDt() {
////            return dt;
////        }
////
////        public List<CardDTDump> getCardDTDumpList() {
////            return cardDTDumpList;
////        }
////    }
////
////    public static class CardDTDump {
////        String word;
////        String comment;
////        long dt;
////
////        CardDTDump(Context context, long id) {
////            StorageCard card = new StorageCardRepository(context).getById(id);
////            word = card.getWord();
////            comment = card.getComment();
////            dt = card.getTimestamp();
////        }
////
////        public String getWord() {
////            return word;
////        }
////
////        public String getComment() {
////            return comment;
////        }
////
////        public long getDt() {
////            return dt;
////        }
////    }
//}
