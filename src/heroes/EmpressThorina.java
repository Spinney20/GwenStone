package heroes;

import fileio.CardInput;
import game.Gameboard;
import game.Hero;
import game.Minion;

import static main.Constants.COLUMNS;

public class EmpressThorina extends Hero {
    public EmpressThorina(final CardInput cardInput) {
        super(cardInput);
    }

    // Empress Thorina's ability is to destroy the minion with the highest health
    @Override
    public final void useHeroAbility(final int row, final Gameboard gameboard) {
        Minion strongest = null; // this is the minion with the highest health
        int strongestColumn = 0; // i also need the column to remove the card from the board
        for (int i = 0; i < COLUMNS; i++) {
            Minion minion =
                    gameboard.getCardAtPosition(row, i);
            // if minion null = pointer exception
            if (minion != null && (strongest == null || minion.getHealth()
                    > strongest.getHealth())) {
                strongest = minion;
                strongestColumn = i;
            }
        }
        // also i have to remove the card from the board
        if (strongest != null) {
            gameboard.removeCardAndShiftLeft(row, strongestColumn);
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
