package minions;

import fileio.CardInput;
import game.Minion;

public class Sentinel extends Minion {
    public Sentinel(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isFrontRow() {
        return false; // on the back row
    }

    @Override
    public void useAbility(Minion target) {
    } // no ability

    public boolean isTank() {
        return false;
    }
}