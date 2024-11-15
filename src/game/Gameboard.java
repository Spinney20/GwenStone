package game;

import java.util.ArrayList;

// Class gameboard is managing the gameboard
// this class is the must
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
        // wrong logic
        // todo implement tanks first
        int rowToPlace;
        if (playerIndex == 1) {
            rowToPlace = 3;
        } else {
            rowToPlace = 0;
        }

        // adding the card to the first empty cell in the row
        // if there is no empty cell, then the card cannot be placed
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


}
