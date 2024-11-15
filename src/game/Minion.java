package game;

import fileio.CardInput;
import java.util.List;

//this class is gestioning the minions
// the minions are the most important card, they are taken from the deck
// placed in hand and then pottentially placed on the gameboard
public class Minion {
    private int mana;
    private int health;
    private int attackDamage;
    private String description;
    private List<String> colors;
    private String name;

    public Minion(CardInput cardInput) {
        // just getting the values of the minion
        this.mana = cardInput.getMana();
        this.health = cardInput.getHealth();
        this.attackDamage = cardInput.getAttackDamage();
        this.description = cardInput.getDescription();
        this.colors = cardInput.getColors();
        this.name = cardInput.getName();
    }
    // getter for the health
    public int getHealth() {
        return health;
    }

    // setter for the health
    public void setHealth(int health) {
        this.health = health;
    }

    // getter for the attack damage
    public int getAttackDamage() {
        return attackDamage;
    }

    // getter for the mana
    public int getMana() {
        return mana;
    }

    // getter for the description
    public String getDescription() {
        return description;
    }

    // getter for the colors
    public List<String> getColors() {
        return colors;
    }

    // getter for the name
    public String getName() {
        return name;
    }
}
