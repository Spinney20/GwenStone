package minions;

import fileio.CardInput;
import game.Minion;

public class Goliath extends Minion {
    public Goliath(final CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public final boolean isFrontRow() {
        return true; // Goliath is tank -> first row
    }

    @Override
    public void useAbility(final Minion target) {
    }

    public final boolean isTank() {
        return true;
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
