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

    //TheCursedOne's ability is to swap its attack and health
    //if the health is less than or equal to 0, it will be set to 0
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


