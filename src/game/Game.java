package game;

import java.util.ArrayList;
import fileio.DecksInput;
import fileio.StartGameInput;

// This is the most important class, in which
// all the logic is managed
public class Game {
    private Player playerOne;
    private Player playerTwo;
    private int currentPlayer;
    private int startingPlayerId;
    private int round;
    private Gameboard gameboard;
    private String errorMessage;

    public Game(StartGameInput startGame, DecksInput playerOneDecks, DecksInput playerTwoDecks) {
        initializeGame(startGame, playerOneDecks, playerTwoDecks);
    }

    //initializing the game
    // this is the first thing that is done
    // when the game is created
    // the players are created, the gameboard is created
    private void initializeGame(StartGameInput startGame, DecksInput playerOneDecks, DecksInput playerTwoDecks) {
        this.playerOne = new Player(
                playerOneDecks.getDecks().get(startGame.getPlayerOneDeckIdx()), // getting the deck from the input
                new Hero(startGame.getPlayerOneHero()), // getting the hero from the input
                startGame.getShuffleSeed()
        );
        this.playerTwo = new Player(
                playerTwoDecks.getDecks().get(startGame.getPlayerTwoDeckIdx()), // getting the deck from the input
                new Hero(startGame.getPlayerTwoHero()), // getting the hero from the input
                startGame.getShuffleSeed()
        );

        // drawing the first card for each player
        playerOne.drawCard();
        playerTwo.drawCard();

        this.currentPlayer = startGame.getStartingPlayer();
        this.startingPlayerId = startGame.getStartingPlayer();
        this.round = 1; // the game starts from round 1
        this.gameboard = new Gameboard();
    }
    // placing a card on the table
    public boolean placeCard(int playerIndex, int handIndex) {
        Player currentPlayer;
        // stabilizing the current player
        if (playerIndex == 1) {
            currentPlayer = playerOne;
        } else {
            currentPlayer = playerTwo;
        }
        // getting the card to place
        Minion cardToPlace = currentPlayer.getHand().get(handIndex);
        // error message if the card cannot be placed
        // because of the mana
        if (!currentPlayer.decrementMana(cardToPlace.getMana())) {
            errorMessage = "Not enough mana to place card on table.";
            return false;
        }

        boolean placedSuccessfully = gameboard.addCard(cardToPlace, playerIndex);
        if (placedSuccessfully) {
            currentPlayer.getHand().remove(handIndex);
            return true; // card was placed successfully
        } else {
            //error message if the card cannot be placed
            // because the row is full
            currentPlayer.incrementMana(cardToPlace.getMana());
            errorMessage = "Cannot place card on table since row is full.";
            return false;
        }
    }


    public void endPlayerTurn() {
        //Ok so basically we have to main cases
        // one is the starting player is 1 and
        // the 2nd one is the starting player is 2
        // to avoid drawing cards when I shouldn't
        // I will check if the current player is the starting player
        // and I will draw cards only if the current player is not the starting player
        // which means that the starting player has already ended his turn
        if (startingPlayerId == 1) {
            if (currentPlayer == 1) {
                currentPlayer = 2;
            } else {
                //corrected this
                // mana shuld be incremented for the next round
                // after the current round ends (the second player's turn)
                playerOne.incrementMana(round + 1);
                playerTwo.incrementMana(round + 1);
                currentPlayer = 1;
                round++;
                resetAllMinions(); // reset all minions' attack states
                playerOne.drawCard();
                playerTwo.drawCard();
            }
        } else if (startingPlayerId == 2) {
            if (currentPlayer == 2) {
                currentPlayer = 1;
            } else {
                playerTwo.incrementMana(round + 1);
                playerOne.incrementMana(round + 1);
                currentPlayer = 2;
                round++;
                resetAllMinions(); // reset all minions' attack states
                playerOne.drawCard();
                playerTwo.drawCard();
            }
        }
    }

    // Reset all minions' attack states
    //iterating thru the gameboard and setting the hasAttackedThisTurn
    // to false for all minions -> new round
    //todo I think i will need this for unfroxing
    private void resetAllMinions() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                Minion minion = gameboard.getBoard()[row][col];
                if (minion != null) {
                    minion.setHasAttackedThisTurn(false);
                }
            }
        }
    }

    public String attackCard(int attackerRow, int attackerCol, int targetRow, int targetCol) {
        // As I said in the Gameboard class attackCard method
        // returns the error if its the case or null if not
        String result = gameboard.attackCard(attackerRow, attackerCol, targetRow, targetCol, currentPlayer);
        return result; // result will be used in main
    }


    public ArrayList<ArrayList<Minion>> getCardsOnTable() {
        return gameboard.getCardsOnTable();
    }

    public ArrayList<Minion> getPlayerOneDeck() {
        return playerOne.getDeck();
    }

    public ArrayList<Minion> getPlayerTwoDeck() {
        return playerTwo.getDeck();
    }

    public Hero getPlayerOneHero() {
        return playerOne.getHero();
    }

    public Hero getPlayerTwoHero() {
        return playerTwo.getHero();
    }

    public int getPlayerMana(int playerIndex) {
        if (playerIndex == 1) {
            return playerOne.getMana();
        } else if (playerIndex == 2) {
            return playerTwo.getMana();
        }
        return -1;
    }

    //getter for each player because i need it
    // in the main class to get the player's hand
    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getRound() {
        return round;
    }

    public Gameboard getGameboard() {
        return gameboard;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
