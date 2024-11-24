package minions;

import fileio.CardInput;
import game.Minion;

public class Miraj extends Minion {
    public Miraj(final CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public final boolean isFrontRow() {
        return true;
    }

    //Miraj's ability is to swap health with another minion
    @Override
    public final void useAbility(final Minion target) {
        int tempHealth = this.getHealth();
        this.setHealth(target.getHealth());
        target.setHealth(tempHealth);
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

