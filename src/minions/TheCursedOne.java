package minions;

import fileio.CardInput;
import game.Minion;

public class TheCursedOne extends Minion {
    public TheCursedOne(final CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public final boolean isFrontRow() {
        return false;
    }

    //TheCursedOne's ability is to swap its attack and health
    //if the health is less than or equal to 0, it will be set to 0
    @Override
    public final void useAbility(final Minion target) {
        int tempAttack = target.getAttackDamage();
        target.setAttackDamage(target.getHealth());
        target.setHealth(tempAttack);

        if (target.getHealth() <= 0) {
            target.setHealth(0);
        }
    }

    public final boolean isTank() {
        return false;
    }

    /***
     * Checks if the target is an ally or not
     * because only Disciple can target allies
     * @param target - the minion that the ability will be used on
     * @param isAlly - true if the target is an ally, done with the method is ally
     *               from gameboard
     * @return - error message if the target is an ally or null if the target is an enemy
     */
    @Override
    public final String canTarget(final Minion target, final boolean isAlly) {
        if (isAlly) {
            return "Attacked card does not belong to the enemy.";
        }
        return null;
    }
}


