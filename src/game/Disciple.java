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
}

