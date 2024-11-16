package game;

import fileio.CardInput;

public class Goliath extends Minion {
    public Goliath(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isFrontRow() {
        return true; // Goliath is tank -> first row
    }
}
