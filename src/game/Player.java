package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import fileio.CardInput;
import minions.Disciple;
import minions.Miraj;
import minions.TheCursedOne;
import minions.TheRipper;
import minions.Berserker;
import minions.Goliath;
import minions.Sentinel;
import minions.Warden;

import static main.Constants.MAX_MANA;

// Class player is managing the player
// the player has a deck, a hero, a hand and a mana
public class Player {
    private ArrayList<Minion> deck;
    private Hero hero;
    private ArrayList<Minion> hand;
    private int mana = 1;

    public Player(final ArrayList<CardInput> deck, final Hero hero, final int shuffleSeed) {
        this.deck = new ArrayList<>();
        for (CardInput cardInput : deck) {
            Minion minion = createMinionFromCardInput(cardInput);
            this.deck.add(minion);
        }
        Collections.shuffle(this.deck, new Random(shuffleSeed));
        this.hand = new ArrayList<>();
        this.hero = hero;
    }

    // creating a minion from the card input
    private Minion createMinionFromCardInput(final CardInput cardInput) {
        // i do this by the name
        String name = cardInput.getName();
        switch (name) {
            case "Goliath":
                return new Goliath(cardInput);
            case "Warden":
                return new Warden(cardInput);
            case "Sentinel":
                return new Sentinel(cardInput);
            case "Berserker":
                return new Berserker(cardInput);
            case "The Ripper":
                return new TheRipper(cardInput);
            case "Miraj":
                return new Miraj(cardInput);
            case "The Cursed One":
                return new TheCursedOne(cardInput);
            case "Disciple":
                return new Disciple(cardInput);
            default:
                return null;
        }
    }

    /***
     * Drawing a card from the deck and adding it to the hand
     *
     */
    public void drawCard() {
        // if the deck is not empty, then we can draw a card
        if (!deck.isEmpty()) {
            // always draw the first card from the deck
            Minion drawnCard = deck.remove(0);
            // and we add it to the hand
            hand.add(drawnCard);
        }
    }

    /***
     * Incrementing the mana
     * Will be used after each round
     * max mana to be incremented is 10
     * @param additionalMana - mana to be added
     */
    public void incrementMana(final int additionalMana) {
        // after the 10th round the mana will be incremented by 10
        // 10 is the max mana to be incremented
        int manaToAdd = additionalMana;
        if (manaToAdd > MAX_MANA) {
            manaToAdd = MAX_MANA;
        }
        this.mana += manaToAdd;
    }

    /***
     * decrementing the mana
     * this is used when a card is placed on the board
     * or when an ability is used
     * or when a hero ability is used
     * @param cost - mana cost of the card
     * @return - true if the mana is decremented, false otherwise
     */
    public boolean decrementMana(final int cost) {
        // making sure we have enough mana to place the card
        // i think its a double check, but its good to have it
        if (this.mana >= cost) {
            this.mana -= cost;
            return true;
        } else {
            return false;
        }
    }

    // getter for mana
    public final int getMana() {
        return mana;
    }

    public final ArrayList<Minion> getHand() {
        return hand;
    }

    public final ArrayList<Minion> getDeck() {
        return deck;
    }

    public final Hero getHero() {
        return hero;
    }
}
