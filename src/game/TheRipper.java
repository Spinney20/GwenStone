package game;

import fileio.CardInput;

public class TheRipper extends Minion {
    public TheRipper(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isFrontRow() {
        return true;
    }
}

