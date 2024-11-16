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
}