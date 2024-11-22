package minions;

import fileio.CardInput;
import game.Minion;

public class Berserker extends Minion {
    public Berserker(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isFrontRow() {
        return false; // on the back row
    }

    // it has no ability
    @Override
    public void useAbility(Minion target) {
    }
    // moved is tank logic in each class
    // i know i could've used isfrontrow for this but i wanted
    // to do smth else with this method and it remained here
    // and when I use it in the for from the board class
    // to check if the minion is tank or not
    // is more intuitive lol
    public boolean isTank() {
        return false;
    }
}