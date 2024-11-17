package game;

import fileio.CardInput;

public class TheCursedOne extends Minion {
    public TheCursedOne(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isFrontRow() {
        return false;
    }

    @Override
    public void useAbility(Minion target) {
        int tempAttack = target.getAttackDamage();
        target.setAttackDamage(target.getHealth());
        target.setHealth(tempAttack);

        if (target.getHealth() <= 0) {
            target.setHealth(0);
        }
    }

    public boolean isTank() {
        return false;
    }
}


