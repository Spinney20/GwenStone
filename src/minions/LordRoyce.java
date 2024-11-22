package minions;

import fileio.CardInput;
import game.Gameboard;
import game.Hero;
import game.Minion;

public class LordRoyce extends Hero {
    public LordRoyce(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public void useHeroAbility(int row, Gameboard gameboard) {
        for(int i = 0; i < 5; i++) {
            Minion minion = gameboard.getCardAtPosition(row, i);
            if(minion != null) {
                minion.setFrozen();
            }
        }
    }
}
