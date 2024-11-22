package heroes;

import fileio.CardInput;
import game.Minion;

public class Disciple extends Minion {
    public Disciple(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isFrontRow() {
        return false;
    }

    //disciple ability is to heal 2 health
    @Override
    public void useAbility(Minion target) {
        target.setHealth(target.getHealth() + 2);
    }

    public boolean isTank() {
        return false;
    }
}

