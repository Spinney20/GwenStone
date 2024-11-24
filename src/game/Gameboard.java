package game;

import java.util.ArrayList;

// importing the constants
import static main.Constants.FRONT_ROW_PLAYER1;
import static main.Constants.BACK_ROW_PLAYER1;
import static main.Constants.BACK_ROW_PLAYER2;
import static main.Constants.FRONT_ROW_PLAYER2;
import static main.Constants.ROWS;
import static main.Constants.COLUMNS;

// Class gameboard is managing the gameboard
// this class is a must
// its the place where the action is happening
public class Gameboard { // max 5 cards in a row
    private Minion[][] gameboard;

    public Gameboard() {
        this.gameboard = new Minion[ROWS][COLUMNS];
    }


    /***
     * This method is used to add a card to the gameboard
     * it takes the minion that has to be placed and the player index
     * the player index is used to determine where the card should be placed
     * if the player index is 1, the card will be placed on the first player's side
     * if the player index is 2, the card will be placed on the second player's side
     * the method returns true if the card was successfully placed on the gameboard
     * and false if the card could not be placed
     * possible reasons for not being able to place the card are:
     * - the row is full as it follows
     * - the card is not a front row card and the front row is full
     * - the card is not a back row card and the back row is full
     * @param card - the card that has to be placed
     * @param playerIndex - the index of the player that is placing the card
     * @return true if the card was successfully placed, false otherwise
     */
    public boolean addCard(final Minion card, final int playerIndex) {
        int rowToPlace;

        if (playerIndex == 1) {
            if (card.isFrontRow()) {
                rowToPlace = FRONT_ROW_PLAYER1;
            } else {
                rowToPlace = BACK_ROW_PLAYER1;
            }
        } else {
            if (card.isFrontRow()) {
                rowToPlace = FRONT_ROW_PLAYER2;
            } else {
                rowToPlace = BACK_ROW_PLAYER2;
            }
        }
        //effectively placuinf the card on the gameboard
        for (int i = 0; i < COLUMNS; i++) {
            if (gameboard[rowToPlace][i] == null) {
                gameboard[rowToPlace][i] = card;
                return true;
            }
        }
        return false;
    }

    /***
     * This is getting all the cards on the table
     * iterating through the gameboard and adding the cards to a list
     * @return - it returns a list of a list of minions (the minions on the table)
     */
    public ArrayList<ArrayList<Minion>> getCardsOnTable() {
        ArrayList<ArrayList<Minion>> cardsOnTable = new ArrayList<>();

        for (int row = 0; row < ROWS; row++) {
            ArrayList<Minion> rowCards = new ArrayList<>();
            for (int col = 0; col < COLUMNS; col++) {
                if (gameboard[row][col] != null) {
                    rowCards.add(gameboard[row][col]);
                }
            }
            cardsOnTable.add(rowCards);
        }

        return cardsOnTable;
    }

    /***
     * As I should have done with the placement of the cards
     * I am returning the error message instead of printing it
     * to avoid complex error handling
     * and null if the attack was successful
     * First im getting the attacker and the target
     * and then im proceeding to check if the attack is possible
     * if its possible i check if there are tanks on the front row
     * if the target is a tank its ok
     * but if there are tanks on the front row and the target is not a tank
     * the attack is not possible
     * if everything is ok, I proceed to attack the target
     * and apply the takeDamage method
     * and then I make the attacker unable to attack again this turn
     * by setting the hasAttackedThisTurn to true
     * ALSO if the target is dead, I remove it from the gameboard
     * @param attackerRow - the row where the attacker is
     * @param attackerCol - the column where the attacker is
     * @param targetRow - the row where the target is
     * @param targetCol - the column where the target is
     * @param attackingPlayer - the player that is attacking
     * @return -errors if the attack was not successful null otherwise
     */
    public String attackCard(final int attackerRow, final int attackerCol, final int targetRow,
                             final int targetCol, final int attackingPlayer) {
        Minion attacker = gameboard[attackerRow][attackerCol];
        Minion target = gameboard[targetRow][targetCol];

        // Check if target card belongs to the enemy
        if ((attackingPlayer == 1 && targetRow > 1) || (attackingPlayer == 2 && targetRow < 2)) {
            return "Attacked card does not belong to the enemy.";
        }

        // Check if attacker has already attacked this turn
        if (attacker.hasAttacked()) {
            return "Attacker card has already attacked this turn.";
        }

        // Check if attacker is frozen
        if (attacker.isFrozen()) {
            return "Attacker card is frozen.";
        }

        // Check for tanks on the opponent's front row
        boolean hasTank = false;
        int enemyFrontRow = (attackingPlayer == 1) ? 1 : 2;
        for (int i = 0; i < COLUMNS; i++) {
            Minion enemyCard = gameboard[enemyFrontRow][i];
            if (enemyCard != null && enemyCard.isTank()) {
                hasTank = true;
                break;
            }
        }

        // If there are tanks, ensure the target is a tank
        if (hasTank && (target == null || !target.isTank())) {
            return "Attacked card is not of type 'Tank'.";
        }



        // Perform the attack
        target.takeDamage(attacker.getAttackDamage());
        attacker.setHasAttackedThisTurn(true);

        // remove the target from the gameboard if it dies
        if (target.isDead()) {
            removeCardAndShiftLeft(targetRow, targetCol);
        }

        return null; // successful attack, no errors
    }

    /***
     * This method is used to use the ability of a card
     * I tried to have the same skel as the attackCard method
     * first im getting the user and the target
     * and then im proceeding to check if the ability is possible
     * if its possible i check if there are tanks on the front row
     * if the target is a tank its ok
     * but if there are tanks on the front row and the target is not a tank
     * the ability is not possible
     * if everything is ok, I proceed to use the ability on the target
     * just like in the attackCard method
     * and then I make the user unable to attack again this turn
     * by setting the hasAttackedThisTurn to true
     * ALSO if the target is dead, I remove it from the gameboard
     * @param userRow - the row where the user is
     * @param userCol - the column where the user is
     * @param targetRow - the row where the target is
     * @param targetCol - the column where the target is
     * @param playerIdx - the player that is using the ability
     * @return -errors if the ability was not successful null otherwise
     */
    public String useAbility(final int userRow, final int userCol, final int targetRow,
                             final int targetCol, final int playerIdx) {
        Minion user = gameboard[userRow][userCol];
        Minion target = gameboard[targetRow][targetCol];

        if (user == null) {
            return "No card at user position.";
        }

        if (user.isFrozen()) {
            return "Attacker card is frozen.";
        }

        if (user.hasAttacked()) {
            return "Attacker card has already attacked this turn.";
        }

        // the only diff from attack card, is that Disciple
        // can operate only on ally cards
        // and Goliath can operate only on enemy cards
        // instead of attacking we use the ability
        String potentialError = user.canTarget(target, isAlly(playerIdx, targetRow));
        if (potentialError != null) {
            return potentialError;
        }

        if (isEnemy(playerIdx, targetRow)) {
            //LOGIC COPIED FROM attackCard
            // Check for tanks on the opponent's front row
            boolean hasTank = false;
            int enemyFrontRow = (playerIdx == 1) ? 1 : 2;
            for (int i = 0; i < COLUMNS; i++) {
                Minion enemyCard = gameboard[enemyFrontRow][i];
                if (enemyCard != null && enemyCard.isTank()) {
                    hasTank = true;
                    break;
                }
            }

            // If there are tanks, ensure the target is a tank
            if (hasTank && (target == null || !target.isTank())) {
                return "Attacked card is not of type 'Tank'.";
            }
        }

        user.useAbility(target);
        user.setHasAttackedThisTurn(true);
        // after the ability is used, some cards may have the health 0
        // this means they are dead and I remove them
        if (target.getHealth() == 0) {
            removeCardAndShiftLeft(targetRow, targetCol);
        }
        return null;
    }

    // logic for finding if a minion is an enemy or an ally
    private boolean isEnemy(final int playerIdx, final int row) {
        // we use the gameboard to determine if the card is an enemy or an ally
        if (playerIdx == 1 && row <= 1) {
            return true;
        } else if (playerIdx == 2 && row >= 2) {
            return true;
        }
            return false;
    }
    // if its not an enemy, then its an ally
    private boolean isAlly(final int playerIdx, final int row) {
        return !isEnemy(playerIdx, row);
    }

    /***
     * main logic for removing a card from the gameboard
     * I remove it by shifting all the cards to the left
     * @param row - row to remove the card from
     * @param col - column to remove the card from
     */
    public void removeCardAndShiftLeft(final int row, final int col) {

        // shifting all cards to the left
        for (int i = col; i < COLUMNS - 1; i++) {
            gameboard[row][i] = gameboard[row][i + 1];
        }

        // last pos to null ofc
        gameboard[row][COLUMNS - 1] = null;
    }

    public final Minion[][] getBoard() {
        return gameboard;
    }

    /***
     * This method is used to get a card at a certain position
     * I return null if the position is invalid
     * @param x - the row
     * @param y - the column
     * @return
     */
    public Minion getCardAtPosition(final int x, final int y) {
        if (x < 0 || x >= ROWS || y < 0 || y >= COLUMNS) {
            return null;
        }
        return gameboard[x][y];
    }
}
