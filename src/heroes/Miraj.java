package heroes;

import fileio.CardInput;
import game.Minion;

public class Miraj extends Minion {
    public Miraj(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isFrontRow() {
        return true;
    }

    //Miraj's ability is to swap health with another minion
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

