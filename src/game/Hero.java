package game;

import fileio.CardInput;
import java.util.List;

//this class is gestioning the hero
// every player has a hero
public class Hero {
    private int mana;
    private String description;
    private List<String> colors;
    private String name;
    private int health;

    public Hero(CardInput cardInput) {
        this.mana = cardInput.getMana();
        this.description = cardInput.getDescription();
        this.colors = cardInput.getColors();
        this.name = cardInput.getName();
        this.health = 30; // hero;s health is always 30
    }

    //Gtetter for the health
    public int getHealth() {
        return health;
    }

    public void useAbility() {
        // todo
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
}
