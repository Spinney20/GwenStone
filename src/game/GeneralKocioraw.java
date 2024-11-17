package game;

import fileio.CardInput;

public class GeneralKocioraw extends Hero
{
    public GeneralKocioraw(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public void useHeroAbility(int row, Gameboard gameboard) {
        for(int i = 0; i < 5; i++) {
            Minion minion = gameboard.getCardAtPosition(row, i);
            if(minion != null) {
                minion.setAttackDamage(minion.getAttackDamage() + 1);
            }
        }
    }
}
