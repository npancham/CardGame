package com.example.cardgame.blackjack;

import com.example.cardgame.Card;
import com.example.cardgame.Deck;

import java.util.List;

public class Player extends Participant {
    public Player() {
        this("Player");
    }

    public Player(String name) {
        super(name);
    }

    public void newHand(Deck deck) {
        List<Card> hand = getHand();
        hand.clear();
        Card card;

        for (int i = 0; i < 2; i++) {
            card = deck.draw();
            hand.add(card);
        }
    }

    public void newHand(Card splitCard, Deck deck) {
        List<Card> hand = getHand();
        hand.clear();
        hand.add(splitCard);
        Card card = deck.draw();
        hand.add(card);
    }

    public int stand() {
        return evaluateHand();
    }

    public int hit(Deck deck) {
        Card card = deck.draw();
        getHand().add(card);
        return evaluateHand();
    }
}
