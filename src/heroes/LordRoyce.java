package heroes;

import fileio.CardInput;
import game.Gameboard;
import game.Hero;
import game.Minion;

import static main.Constants.COLUMNS;

public class LordRoyce extends Hero {
    public LordRoyce(final CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public final void useHeroAbility(final int row, final Gameboard gameboard) {
        for (int i = 0; i < COLUMNS; i++) {
            Minion minion = gameboard.getCardAtPosition(row, i);
            if (minion != null) {
                minion.setFrozen();
            }
        }
    }

    @Override
    public final String canTargetRow(final boolean isAllyRow) {
        if (isAllyRow) {
            return "Selected row does not belong to the enemy.";
        }
        return null;
    }
}
