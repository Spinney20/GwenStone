package game;

import fileio.CardInput;
import java.util.List;

//this class is gestioning the minions
// the minions are the most important card, they are taken from the deck
// placed in hand and then pottentially placed on the gameboard
public abstract class Minion {
    private int mana;
    private int health;
    private int attackDamage;
    private String description;
    private List<String> colors;
    private String name;
    private boolean isFrozen;
    private boolean hasAttackedThisTurn;
    private boolean isTank;

    public Minion(CardInput cardInput) {
        // just getting the values of the minion
        this.mana = cardInput.getMana();
        this.health = cardInput.getHealth();
        this.attackDamage = cardInput.getAttackDamage();
        this.description = cardInput.getDescription();
        this.colors = cardInput.getColors();
        this.name = cardInput.getName();
        this.isFrozen = false;
        this.hasAttackedThisTurn = false;

        // the tanks are Goliat and Warden
        // set the tank to true if the name is Goliat or Warden
        if (name.equals("Goliath") || name.equals("Warden")) {
            this.isTank = true;
        } else {
            this.isTank = false;
        }
    }
    // this makes the card receive the damage
    // given by the attacker
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }
    // if the card has under 0 health, then it is dead
    // we will use this to remove the card from the gameboard
    // if its dead
    public boolean isDead() {
        return this.health <= 0;
    }
    //todo use for ability
    public boolean isFrozen() {
        return isFrozen;
    }
    // veryfing if the card has attacked this round
    // basicallly a getter for the hasAttackedThisTurn
    public boolean hasAttacked() {
        return hasAttackedThisTurn;
    }
    // setter for hasAttackedThisTurn variable
    // used to set is as true after the card has attacked
    // and to false when the round ends
    public void setHasAttackedThisTurn(boolean hasAttackedThisTurn) {
        this.hasAttackedThisTurn = hasAttackedThisTurn;
    }

    public abstract boolean isTank();

    // getter for the health
    public int getHealth() {
        return health;
    }

    // setter for the health
    // used if some abilities are used on this minion
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

    public abstract boolean isFrontRow();

    //used for each minion that has an ability
    public abstract void useAbility(Minion target);

    // used for each minion that Rippers ability
    // is used on
    // the ability is to decrease the attack damage of the target
    // by 2
    public void decreaseAttackDamage() {
        this.attackDamage -= 2; // decrease the attack damage by 2
        if (this.attackDamage < 0) {
            this.attackDamage = 0;
        }
    }

    // used for each minion that TheCursedOne ability
    // is used on
    public void setAttackDamage(int number) {
        this.attackDamage = number;
    }

    public void setFrozen() {
        isFrozen = true;
    }

    // used to unfreeze after turn ending
    public void setUnfrozen() {
        isFrozen = false;
    }
}
