package game;

import fileio.CardInput;

public class Sentinel extends Minion {
    public Sentinel(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isFrontRow() {
        return false; // on the back row
    }
}