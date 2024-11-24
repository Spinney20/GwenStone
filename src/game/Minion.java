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

    public Minion(final CardInput cardInput) {
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

    /***
     * this makes the minion receive damage
     * given by the attacker
     * @param damage - the damage that the minion will receive
     */
    public void takeDamage(final int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }
    // if the card has under 0 health, then it is dead
    // we will use this to remove the card from the gameboard
    // if its dead
    public final boolean isDead() {
        return this.health <= 0;
    }
    //todo use for ability
    public final boolean isFrozen() {
        return isFrozen;
    }

    /***
     * veryifying if the card has attacked this turn
     * basically a getter for the hasAttackedThisTurn variable
     * @return - true if the card has attacked this turn
     */
    public final boolean hasAttacked() {
        return hasAttackedThisTurn;
    }

    /***
     * setter for hasAttackedThisTurn variable
     * used to set is as true after the card has attacked
     * and to false when the round ends
     * @param hasAttackedThisTurn - set to true if the card has attacked
     */
    public void setHasAttackedThisTurn(final boolean hasAttackedThisTurn) {
        this.hasAttackedThisTurn = hasAttackedThisTurn;
    }

    /***
     * this will be overriden in the minions
     * some are tanks and should be placed in the front row
     * others are not tanks and should be placed in the back row
     * @return
     */
    public abstract boolean isTank();

    // getter for the health
    public final int getHealth() {
        return health;
    }

    // setter for the health
    // used if some abilities are used on this minion
    public final void setHealth(final int health) {
        this.health = health;
    }

    // getter for the attack damage
    public final int getAttackDamage() {
        return attackDamage;
    }

    // getter for the mana
    public final int getMana() {
        return mana;
    }

    // getter for the description
    public final String getDescription() {
        return description;
    }

    // getter for the colors
    public final List<String> getColors() {
        return colors;
    }

    // getter for the name
    public final String getName() {
        return name;
    }

    /***
     * this will be overriden in the minions
     * to know if the minion is in the front row or not
     * basically tank or not tank
     * @return
     */
    public abstract boolean isFrontRow();


    /***
     * used for each minion that has an ability
     * @param target - the minion that the ability will be used on
     */
    public abstract void useAbility(Minion target);

    /***
     * used for each minion that Rippers ability
     * is used on
     * the ability is to decrease the attack damage of the target
     * by 2
     */
    public void decreaseAttackDamage() {
        this.attackDamage -= 2; // decrease the attack damage by 2
        if (this.attackDamage < 0) {
            this.attackDamage = 0;
        }
    }

    // used for each minion that TheCursedOne ability
    // is used on
    public final void setAttackDamage(final int number) {
        this.attackDamage = number;
    }

    /***
     * used for each minion that should be frozen
     */
    public void setFrozen() {
        isFrozen = true;
    }

    /***
     * used to unfreeze after the round ends
     */
    public void setUnfrozen() {
        isFrozen = false;
    }

    /***
     * used for disciple vs goliath when applying the ability
     * disciple can only target allies
     * goliat can only target enemies
     * @param target - the minion that the ability will be used on
     * @param isAlly - true if the target is an ally, done with the method is ally
     *               from gameboard
      */
    public abstract String canTarget(Minion target, boolean isAlly);

}
