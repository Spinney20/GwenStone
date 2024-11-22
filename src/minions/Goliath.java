package minions;

import fileio.CardInput;
import game.Minion;

public class Goliath extends Minion {
    public Goliath(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isFrontRow() {
        return true; // Goliath is tank -> first row
    }

    @Override
    public void useAbility(Minion target) {
    }

    public boolean isTank() {
        return true;
    }
}