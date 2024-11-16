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
}