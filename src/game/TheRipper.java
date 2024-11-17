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

    //TheRipper's ability is to decrease enemy minions' attack by 2
    @Override
    public void useAbility(Minion target) {
        target.decreaseAttackDamage();
    }

    public boolean isTank() {
        return false;
    }
}

