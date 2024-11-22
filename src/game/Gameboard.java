package game;

import heroes.Disciple;

import java.util.ArrayList;

// Class gameboard is managing the gameboard
// this class is a must
// its the place where the action is happening
public class Gameboard {
    private static final int ROWS = 4; // 4 rows, 2 for each player
    private static final int COLS = 5; // max 5 cards in a row
    private Minion[][] gameboard;

    public Gameboard() {
        this.gameboard = new Minion[ROWS][COLS];
    }

    // adding a card to the gameboard
    // PROBABLY THE HARDEST THING UNTIL NOW :(
    public boolean addCard(Minion card, int playerIndex) {
        int rowToPlace;

        if (playerIndex == 1) {
            if (card.isFrontRow()) {
                rowToPlace = 2; // Front row for player 1
            } else {
                rowToPlace = 3; // Back row for player 1
            }
        } else {
            if (card.isFrontRow()) {
                rowToPlace = 1; // Front row for player 2
            } else {
                rowToPlace = 0; // Back row for player 2
            }
        }
        //effectively placuinf the card on the gameboard
        for (int i = 0; i < COLS; i++) {
            if (gameboard[rowToPlace][i] == null) {
                gameboard[rowToPlace][i] = card;
                return true;
            }
        }
        return false;
    }

    // this is getting all the cards on the table
    // this is used to display the cards on the table
    // just 2 fors for iterating thru the matrix
    public ArrayList<ArrayList<Minion>> getCardsOnTable() {
        ArrayList<ArrayList<Minion>> cardsOnTable = new ArrayList<>();

        for (int row = 0; row < ROWS; row++) {
            ArrayList<Minion> rowCards = new ArrayList<>();
            for (int col = 0; col < COLS; col++) {
                if (gameboard[row][col] != null) {
                    rowCards.add(gameboard[row][col]);
                }
            }
            cardsOnTable.add(rowCards);
        }

        return cardsOnTable;
    }

    // As i should have done with place card
    // i will make this a public string and return the potential errors
    // to avoid complex error handling
    // i just return the error if its the case, if not i return null
    public String attackCard(int attackerRow, int attackerCol, int targetRow, int targetCol, int attackingPlayer) {
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
        for (int i = 0; i < COLS; i++) {
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
    // very similar to attackCard, used the same logic
    public String useAbility(int userRow, int userCol, int targetRow, int targetCol, int playerIdx) {
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
        if (user instanceof Disciple) {
            if (!isAlly(playerIdx, targetRow)) {
                return "Attacked card does not belong to the current player.";
            }
        } else {
            if (!isEnemy(playerIdx, targetRow)) {
                return "Attacked card does not belong to the enemy.";
            }

            //LOGIC COPIED FROM attackCard
            // Check for tanks on the opponent's front row
            boolean hasTank = false;
            int enemyFrontRow = (playerIdx == 1) ? 1 : 2;
            for (int i = 0; i < COLS; i++) {
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
        if(target.getHealth() == 0) {
            removeCardAndShiftLeft(targetRow, targetCol);
        }
        return null;
    }

    // logic for finding if a minion is an enemy or an ally
    private boolean isEnemy(int playerIdx, int row) {
        // we use the gameboard to determine if the card is an enemy or an ally
        if (playerIdx == 1 && row <= 1) {
            return true;
        } else if (playerIdx == 2 && row >= 2) {
            return true;
        } else {
            return false;
        }
    }
    // if its not an enemy, then its an ally
    private boolean isAlly(int playerIdx, int row) {
        return !isEnemy(playerIdx, row);
    }

    //main logic forr removing a card from the gameboard
    // i have to shift all the remainied cards to the left
    public void removeCardAndShiftLeft(int row, int col) {

        // shifting all cards to the left
        for (int i = col; i < COLS - 1; i++) {
            gameboard[row][i] = gameboard[row][i + 1];
        }

        // last pos to null
        gameboard[row][COLS - 1] = null;
    }

    public Minion[][] getBoard() {
        return gameboard;
    }

    public Minion getCardAtPosition(int x, int y) {
        if (x < 0 || x >= ROWS || y < 0 || y >= COLS) {
            return null;
        }
        return gameboard[x][y];
    }

}
