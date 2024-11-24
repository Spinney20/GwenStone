package minions;

import fileio.CardInput;
import game.Minion;

public class Disciple extends Minion {
    public Disciple(final CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public final boolean isFrontRow() {
        return false;
    }

    //disciple ability is to heal 2 health
    @Override
    public final void useAbility(final Minion target) {
        target.setHealth(target.getHealth() + 2);
    }

    public final boolean isTank() {
        return false;
    }

    /***
     * Disciple can only target allies
     * @param target - the minion that the ability will be used on
     * @param isAlly - true if the target is an ally, done with the method is ally
     *               from gameboard
     * @return - error message if the target is NOT an ally or null if the target is an ally
     */
    @Override
    public final String canTarget(final Minion target, final boolean isAlly) {
        if (!isAlly) {
            return "Attacked card does not belong to the current player.";
        }
        return null;
    }


}

