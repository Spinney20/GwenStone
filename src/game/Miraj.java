package game;

import fileio.CardInput;

public class Miraj extends Minion {
    public Miraj(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isFrontRow() {
        return true;
    }
}

