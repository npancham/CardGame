package com.example.cardgame.blackjack;

import com.example.cardgame.Card;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BlackjackRules {
    public enum Outcome {
        PLAYER_BLACKJACK,
        DEALER_BUST,
        PLAYER_NORMAL_WIN,
        DEALER_BLACKJACK,
        PLAYER_BUST,
        DEALER_NORMAL_WIN,
        PUSH
    }

    public static Map<Card.Rank, Integer> valueMap;
    public static Scanner scanner;

    static {
        valueMap = new HashMap<>();
        valueMap.put(Card.Rank.TWO, 2);
        valueMap.put(Card.Rank.THREE, 3);
        valueMap.put(Card.Rank.FOUR, 4);
        valueMap.put(Card.Rank.FIVE, 5);
        valueMap.put(Card.Rank.SIX, 6);
        valueMap.put(Card.Rank.SEVEN, 7);
        valueMap.put(Card.Rank.EIGHT, 8);
        valueMap.put(Card.Rank.NINE, 9);
        valueMap.put(Card.Rank.TEN, 10);
        valueMap.put(Card.Rank.JACK, 10);
        valueMap.put(Card.Rank.QUEEN, 10);
        valueMap.put(Card.Rank.KING, 10);

        scanner = new Scanner(System.in);
    }

//    public static void playGame() {
//        Deck deck = new Deck();
//        deck.shuffle();
//
//        Dealer dealer =  new Dealer();
//        Player player = new Player();
//
//        playRound(deck, dealer, player);
//    }

//    public static void showHand(Participant participant) {
//        String name = participant.getName();
//        List<Card> hand = participant.getHand();
//
//        System.out.println(name + ":");
//
//        for (Card card: hand) {
//            System.out.println("\t" + card.getCardInfo());
//        }
//
//        System.out.println("\t" + "Total value: " + participant.evaluateHand());
//    }

    public static Outcome evaluateOutcome(Dealer dealer, Player player) {
        if (player.hasBlackjack() && dealer.hasBlackjack()) {
            return Outcome.PUSH;
        }

        if (player.hasBlackjack()) {
            return Outcome.PLAYER_BLACKJACK;
        }

        if (dealer.hasBlackjack()) {
            return Outcome.DEALER_BLACKJACK;
        }

        if (player.hasBusted()) {
            return Outcome.PLAYER_BUST;
        }

        if (dealer.hasBusted()) {
            return Outcome.DEALER_BUST;
        }

        if (player.evaluateHand() > dealer.evaluateHand()) {
            return Outcome.PLAYER_NORMAL_WIN;
        }

        if (player.evaluateHand() < dealer.evaluateHand()) {
            return Outcome.DEALER_NORMAL_WIN;
        }

        return Outcome.PUSH;
    }

    public static Outcome evaluateSplitOutcome(Dealer dealer, Player player) {
        if (dealer.hasBlackjack()) {
            return Outcome.DEALER_BLACKJACK;
        }

        if (player.hasBusted()) {
            return Outcome.PLAYER_BUST;
        }

        if (dealer.hasBusted()) {
            return Outcome.DEALER_BUST;
        }

        if (player.evaluateHand() > dealer.evaluateHand()) {
            return Outcome.PLAYER_NORMAL_WIN;
        }

        if (player.evaluateHand() < dealer.evaluateHand()) {
            return Outcome.DEALER_NORMAL_WIN;
        }

        return Outcome.PUSH;
    }

//    public static void printOutcome(Outcome outcome) {
//        if (outcome == Outcome.PLAYER_BLACKJACK) {
//            System.out.println("Blackjack! You win!");
//        } else if (outcome == Outcome.DEALER_BUST) {
//            System.out.println("Dealer exceeded 21. You win!");
//        } else if (outcome == Outcome.PLAYER_NORMAL_WIN) {
//            System.out.println("You win!");
//        } else if (outcome == Outcome.DEALER_BLACKJACK) {
//            System.out.println("Dealer Blackjack! You lose...");
//        } else if (outcome == Outcome.PLAYER_BUST) {
//            System.out.println("You exceeded 21. You lose...");
//        } else if (outcome == Outcome.DEALER_NORMAL_WIN) {
//            System.out.println("You lose...");
//        } else if (outcome == Outcome.PUSH) {
//            System.out.println("It's a tie.");
//        }
//    }

//    public static void playRound(Deck deck, Dealer dealer, Player player) {
//        dealer.newHand(deck);
//        player.newHand(deck);
//
//        if (player.hasBlackjack()) {
//            concludeRound(deck, dealer, player);
//
//            return;
//        }
//
//        int turn = 1;
//        String playerInput;
//
//        while (true) {
//            System.out.println("-------Turn " + turn + ":-------");
//            showHand(dealer);
//            showHand(player);
//
//            System.out.println("[S]tand or [H]it?");
//
//            do {
//                playerInput = scanner.next();
//            } while (!playerInput.equals("S") && !playerInput.equals("s") && !playerInput.equals("H") && !playerInput.equals("h"));
//
//            if (playerInput.equals("S") || playerInput.equals("s")) {
//                player.stand();
//                concludeRound(deck, dealer, player);
//
//                return;
//            } else if (playerInput.equals("H") || playerInput.equals("h")) {
//                player.hit(deck);
//
//                if (player.hasTwentyOne() || player.hasBusted()) {
//                    concludeRound(deck, dealer, player);
//
//                    return;
//                }
//            }
//
//            turn++;
//        }
//    }

//    public static void concludeRound(Deck deck, Dealer dealer, Player player) {
//        dealer.resolvePlay(deck);
//
//        System.out.println("-------Result:-------");
//        showHand(dealer);
//        showHand(player);
//
//        Outcome outcome = evaluateOutcome(dealer, player);
//        printOutcome(outcome);
//    }
}
