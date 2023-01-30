package com.example.cardgame;

import java.util.HashMap;
import java.util.Map;

public class CardToImageUrl {
    private static final String CARD_IMAGE_FOLDER = "src/main/resources/com/example/cardgame/images/cards";
    private static final String IMAGE_FILE_EXTENSION = ".png";
    private static final Map<Card.Suit, String> SUIT_TO_STRING;
    private static final Map<Card.Rank, String> RANK_TO_STRING;

    static {
        SUIT_TO_STRING = new HashMap<>();
        SUIT_TO_STRING.put(Card.Suit.SPADES, "spades");
        SUIT_TO_STRING.put(Card.Suit.HEARTS, "hearts");
        SUIT_TO_STRING.put(Card.Suit.DIAMONDS, "diamonds");
        SUIT_TO_STRING.put(Card.Suit.CLUBS, "Clubs");

        RANK_TO_STRING = new HashMap<>();
        RANK_TO_STRING.put(Card.Rank.ACE, "A");
        RANK_TO_STRING.put(Card.Rank.TWO, "2");
        RANK_TO_STRING.put(Card.Rank.THREE, "3");
        RANK_TO_STRING.put(Card.Rank.FOUR, "4");
        RANK_TO_STRING.put(Card.Rank.FIVE, "5");
        RANK_TO_STRING.put(Card.Rank.SIX, "6");
        RANK_TO_STRING.put(Card.Rank.SEVEN, "7");
        RANK_TO_STRING.put(Card.Rank.EIGHT, "8");
        RANK_TO_STRING.put(Card.Rank.NINE, "9");
        RANK_TO_STRING.put(Card.Rank.TEN, "10");
        RANK_TO_STRING.put(Card.Rank.JACK, "J");
        RANK_TO_STRING.put(Card.Rank.QUEEN, "Q");
        RANK_TO_STRING.put(Card.Rank.KING, "K");
    }

    public static String toUrl(Card card) {
        StringBuilder sb = new StringBuilder();
        sb.append("file:");
        sb.append(CARD_IMAGE_FOLDER);
        sb.append("/");
        sb.append(SUIT_TO_STRING.get(card.getSuit()));
        sb.append(RANK_TO_STRING.get(card.getRank()));
        sb.append(IMAGE_FILE_EXTENSION);
        return sb.toString();
    }
}
