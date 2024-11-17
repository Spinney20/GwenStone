package game;

import fileio.CardInput;
import java.util.List;

//this class is gestioning the hero
// every player has a hero
public abstract class Hero {
    private int mana;
    private String description;
    private List<String> colors;
    private String name;
    private int health;
    public boolean hasUsedAbility = false;

    public Hero(CardInput cardInput) {
        this.mana = cardInput.getMana();
        this.description = cardInput.getDescription();
        this.colors = cardInput.getColors();
        this.name = cardInput.getName();
        this.health = 30; // hero;s health is always 30
        this.hasUsedAbility = false;
    }

    //Gtetter for the health
    public int getHealth() {
        return health;
    }

    //GETTERS
    public int getMana() {
        return mana;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getColors() {
        return colors;
    }

    public String getName() {
        return name;
    }
    // setter for the health
    // will be used if the hero is attacked
    public void setHealth(int health) {
        this.health = health;
    }
    // similar to minion s attack method set attack
    public void setHasUsedAbility(boolean hasUsedAbility) {
        this.hasUsedAbility = hasUsedAbility;
    }

    public abstract void useHeroAbility(int row, Gameboard gameboard);
}
