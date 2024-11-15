package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import fileio.CardInput;

// Class player is managing the player
// the player has a deck, a hero, a hand and a mana
public class Player {
    private ArrayList<Minion> deck;
    private Hero hero;
    private ArrayList<Minion> hand;
    private int mana = 1;

    public Player(ArrayList<CardInput> deck, Hero hero, int shuffleSeed) {

        // each player has a deck, so we should get the deck from the input
        this.deck = new ArrayList<>();
        for (CardInput cardInput : deck) {
            this.deck.add(new Minion(cardInput));
        }
        // i must shuffle the deck
        Collections.shuffle(this.deck, new Random(shuffleSeed));

        // initializing the hand to an empty array
        this.hand = new ArrayList<>();
        this.hero = hero; // assigning the hero
    }

    // drawing a card from the deck
    public void drawCard() {
        // if the deck is not empty, then we can draw a card
        if (!deck.isEmpty()) {
            // always draw the first card from the deck
            Minion drawnCard = deck.remove(0);
            // and we add it to the hand
            hand.add(drawnCard);
        }
    }

    // incrementing the mana, this will be used after each round
    public void incrementMana(int additionalMana) {
        // after the 10th round the mana will be incremented by 10
        // 10 is the max mana to be incremented
        if (additionalMana > 10) {
            additionalMana = 10;
        }
        this.mana += additionalMana;
    }

    // deceamenting the mana, this will be used when a card is placed on the gameboard
    public boolean decrementMana(int cost) {
        // making sure we have enough mana to place the card
        // i think its a double check, but its good to have it
        if (this.mana >= cost) {
            this.mana -= cost;
            return true;
        } else {
            return false;
        }
    }


    // I will maybe use when I will go for another game || multiple games
    public void resetMana() {
        mana = 0;
    }

    // getter for mana
    public int getMana() {
        return mana;
    }

    public ArrayList<Minion> getHand() {
        return hand;
    }

    public ArrayList<Minion> getDeck() {
        return deck;
    }

    public Hero getHero() {
        return hero;
    }
}
