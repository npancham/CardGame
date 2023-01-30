package com.example.cardgame.blackjack;

import com.example.cardgame.Card;
import com.example.cardgame.Deck;

import java.util.List;

public class Dealer extends Participant {
    public Dealer() {
        super("Dealer");
    }

    public void newHand(Deck deck) {
        List<Card> hand = getHand();
        hand.clear();
        Card card = deck.draw();
        hand.add(card);
    }

    public int resolvePlay(Deck deck) {
        while (evaluateHand() < 17) {
            getHand().add(deck.draw());
        }

        return evaluateHand();
    }
}
