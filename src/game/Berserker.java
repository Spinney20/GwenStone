package game;

import fileio.CardInput;

public class Berserker extends Minion {
    public Berserker(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isFrontRow() {
        return false; // on the back row
    }

    @Override
    public void useAbility(Minion target) {
    }
    // moved is tank logic in each class
    public boolean isTank() {
        return false;
    }
}