package game;

import fileio.CardInput;

public class Warden extends Minion {
    public Warden(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isFrontRow() {
        return true; // must be in the front row
    }

    @Override
    public void useAbility(Minion target) {
    } // no ability

    public boolean isTank() {
        return true;
    }
}