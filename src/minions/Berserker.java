package minions;

import fileio.CardInput;
import game.Minion;

public class Berserker extends Minion {
    public Berserker(final CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public final boolean isFrontRow() {
        return false; // on the back row
    }

    // it has no ability
    @Override
    public void useAbility(final Minion target) {
    }
    // moved is tank logic in each class
    // i know i could've used isfrontrow for this but i wanted
    // to do smth else with this method and it remained here
    // and when I use it in the for from the board class
    // to check if the minion is tank or not
    // is more intuitive lol
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
