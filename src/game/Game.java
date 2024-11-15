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

    public boolean placeCard(int playerIndex, int handIndex) {
        Player currentPlayer;
        if (playerIndex == 1) {
            currentPlayer = playerOne;
        } else {
            currentPlayer = playerTwo;
        }

        Minion cardToPlace = currentPlayer.getHand().get(handIndex);

        if (!currentPlayer.decrementMana(cardToPlace.getMana())) {
            return false;
        }

        boolean placedSuccessfully = gameboard.addCard(cardToPlace, playerIndex);
        if (placedSuccessfully) {
            currentPlayer.getHand().remove(handIndex);
            return true;
        } else {
            currentPlayer.incrementMana(cardToPlace.getMana());
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
                playerOne.incrementMana(round + 1);
                currentPlayer = 2;
            } else {
                playerTwo.incrementMana(round + 1);
                currentPlayer = 1;
                round++;
                playerOne.drawCard();
                playerTwo.drawCard();
            }
        } else if (startingPlayerId == 2) {
            if (currentPlayer == 2) {
                playerTwo.incrementMana(round + 1);
                currentPlayer = 1;
            } else {
                playerOne.incrementMana(round + 1);
                currentPlayer = 2;
                round++;
                playerOne.drawCard();
                playerTwo.drawCard();
            }
        }
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
}
