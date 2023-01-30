package com.example.cardgame.blackjack;

import com.example.cardgame.Card;
import com.example.cardgame.Deck;

import java.util.ArrayList;
import java.util.List;

public abstract class Participant {
    private final String name;
    private List<Card> hand;

    public Participant(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public abstract void newHand(Deck deck);

    public int evaluateHand() {
        // Since aces can count as either 11 or 1, we need to check whether counting the ace as 11 makes the total value
        // larger than 21. If so, the ace will count as 1 instead. Otherwise, it will count as 11.

        int totalValue = 0;
        int nAces = 0;

        for (Card card: hand) {
            if (!card.getRank().equals(Card.Rank.ACE)) {
                totalValue += BlackjackRules.valueMap.get(card.getRank());
            } else {
                nAces++;
            }
        }

        for (int i = 0; i < nAces; i++) {
            if (totalValue + 11 > 21) {
                totalValue += 1;
            } else {
                totalValue += 11;
            }
        }

        return totalValue;
    }

    public boolean hasBlackjack() {
        return evaluateHand() == 21 && hand.size() == 2;
    }

    public boolean hasTwentyOne() {
        return evaluateHand() == 21;
    }

    public boolean hasBusted() {
        return evaluateHand() > 21;
    }
}
