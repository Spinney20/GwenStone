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

    @Override
    public void useAbility(Minion target) {
        int tempHealth = this.getHealth();
        this.setHealth(target.getHealth());
        target.setHealth(tempHealth);
    }

    public boolean isTank() {
        return false;
    }
}

