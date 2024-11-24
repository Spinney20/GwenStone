package game;

import fileio.CardInput;
import java.util.List;

import static main.Constants.HERO_HEALTH;

//this class is gestioning the hero
// every player has a hero
public abstract class Hero {
    private int mana;
    private String description;
    private List<String> colors;
    private String name;
    private int health;
    private boolean hasUsedAbility = false;

    public Hero(final CardInput cardInput) {
        this.mana = cardInput.getMana();
        this.description = cardInput.getDescription();
        this.colors = cardInput.getColors();
        this.name = cardInput.getName();
        this.health = HERO_HEALTH; // hero;s health is always 30
        this.hasUsedAbility = false;
    }

    //Gtetter for the health
    // final cause there is no modification after
    public final int getHealth() {
        return health;
    }

    //GETTERS
    // final cause there is no modification after
    public final int getMana() {
        return mana;
    }

    public final String getDescription() {
        return description;
    }

    public final List<String> getColors() {
        return colors;
    }

    public final String getName() {
        return name;
    }
    // setter for the health
    // will be used if the hero is attacked
    public final void setHealth(final int health) {
        this.health = health;
    }
    // similar to minion s attack method set attack
    public final void setHasUsedAbility(final boolean yesOrNo) {
        this.hasUsedAbility = yesOrNo;
    }

    /***
     * This will be overriden by the hero's ability
     * depending on the hero
     * @param row - the row where the hero will use the ability
     * @param gameboard - the gameboard where the logic takes place
     */
    public abstract void useHeroAbility(int row, Gameboard gameboard);

    /***
     * getter for the has used ability
     * i had to make the variable private (checkstyle)
     * so that's why i need a getter
      */
    public boolean hasUsedAbility() {
        return hasUsedAbility;
    }

    /***
     * this will be overriden in the heroes
     * and will be used when the heroes are using their abilities
     * @param isAllyRow - the result of a function i have in the gameboard
     * @return - erorr message if the hero can't target the row null otherwise
     */
    public abstract String canTargetRow(boolean isAllyRow);

}
