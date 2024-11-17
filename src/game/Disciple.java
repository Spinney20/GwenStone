package game;

import fileio.CardInput;

public class Disciple extends Minion {
    public Disciple(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isFrontRow() {
        return false;
    }

    @Override
    public void useAbility(Minion target) {
        target.setHealth(target.getHealth() + 2);
    }

    public boolean isTank() {
        return false;
    }
}

