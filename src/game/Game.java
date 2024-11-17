package game;

import java.util.ArrayList;

import fileio.CardInput;
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
                createHero(startGame.getPlayerOneHero()), // getting the hero from the input
                startGame.getShuffleSeed()
        );

        this.playerTwo = new Player(
                playerTwoDecks.getDecks().get(startGame.getPlayerTwoDeckIdx()), // getting the deck from the input
                createHero(startGame.getPlayerTwoHero()), // getting the hero from the input
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
                unfreezeCurrentPlayerCards(1);
            } else {
                //corrected this
                // mana shuld be incremented for the next round
                // after the current round ends (the second player's turn)
                playerOne.incrementMana(round + 1);
                playerTwo.incrementMana(round + 1);
                currentPlayer = 1;
                round++;
                resetAllMinions(); // reset all minions' attack states
                playerOne.getHero().setHasUsedAbility(false);
                playerTwo.getHero().setHasUsedAbility(false);
                playerOne.drawCard();
                playerTwo.drawCard();
                unfreezeCurrentPlayerCards(2);
            }
        } else if (startingPlayerId == 2) {
            if (currentPlayer == 2) {
                currentPlayer = 1;
                unfreezeCurrentPlayerCards(2);
            } else {
                playerTwo.incrementMana(round + 1);
                playerOne.incrementMana(round + 1);
                currentPlayer = 2;
                round++;
                resetAllMinions();// reset all minions' attack states
                playerOne.getHero().setHasUsedAbility(false);
                playerTwo.getHero().setHasUsedAbility(false);
                playerOne.drawCard();
                playerTwo.drawCard();
                unfreezeCurrentPlayerCards(1);
            }
        }
    }

    // Reset all minions' attack states
    //iterating thru the gameboard and setting the hasAttackedThisTurn
    // to false for all minions -> new round
    //todo I think i will need this for unfroxing
    // I COULDNT USE THIS METHOD TO UNFREEZE BECEAUSE
    // THE UNFREEZING IS DONE IN THE END OF THE TURN NOT THE ROUND
    // BAD LUCK
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
    // so i had to create a new method
    private void unfreezeCurrentPlayerCards(int currentPlayer) {
        int startRow, endRow;

        if (currentPlayer == 1) {
            startRow = 2;
            endRow = 3;
        } else {
            startRow = 0;
            endRow = 1;
        }

        for (int row = startRow; row <= endRow; row++) {
            for (int col = 0; col < 5; col++) {
                Minion minion = gameboard.getCardAtPosition(row, col);
                if (minion != null && minion.isFrozen()) {
                    minion.setUnfrozen();
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
    // I tried to make this inspired by the attackCard method
    // used in gameboard
    // I put attackHero in the game class because
    // its not an operation done on the gameboard
    public String attackHero(int attackerRow, int attackerCol, int currentPlayer) {
        Minion attacker = gameboard.getCardAtPosition(attackerRow, attackerCol);
        Hero enemyHero;
        int enemyFrontRow;

        if (currentPlayer == 1) {
            enemyHero = playerTwo.getHero();
            enemyFrontRow = 1;
        } else {
            enemyHero = playerOne.getHero();
            enemyFrontRow = 2;
        }

        if (attacker == null || attacker.isFrozen()) {
            return "Attacker card is frozen.";
        }

        if (attacker.hasAttacked()) {
            return "Attacker card has already attacked this turn.";
        }

        //Hero can only be attacked
        // if there are no tanks in the front row
        boolean hasTank = false;
        for (int i = 0; i < 5; i++) {
            Minion enemyCard = gameboard.getCardAtPosition(enemyFrontRow, i);
            if (enemyCard != null && enemyCard.isTank()) {
                hasTank = true;
                break;
            }
        }

        if (hasTank) {
            return "Attacked card is not of type 'Tank'.";
        }

        // Decreasing its health by the attacker's attack damage
        enemyHero.setHealth(enemyHero.getHealth() - attacker.getAttackDamage());
        attacker.setHasAttackedThisTurn(true);

        // the hreo being dead is checked in the main class

        return null;
    }

    // on the same skel as before, it returns the error
    public String useHeroAbility(int row, int currentPlayer) {
        Hero currentHero;

        if (currentPlayer == 1) {
            currentHero = playerOne.getHero();
        } else {
            currentHero = playerTwo.getHero();
        }

        if (currentHero.getMana() > getPlayerMana(currentPlayer)) {
            return "Not enough mana to use hero's ability.";
        }

        if (currentHero.hasUsedAbility) {
            return "Hero has already attacked this turn.";
        }

        if (currentHero instanceof LordRoyce || currentHero instanceof EmpressThorina) {
            if (!isEnemyRow(currentPlayer, row)) {
                return "Selected row does not belong to the enemy.";
            }
        } else if (currentHero instanceof GeneralKocioraw || currentHero instanceof KingMudface) {
            if (!isAllyRow(currentPlayer, row)) {
                return "Selected row does not belong to the current player.";
            }
        }
        // using the hero ability
        currentHero.useHeroAbility(row, gameboard);
        currentHero.setHasUsedAbility(true);
        // decrementing the mana for the hero ability
        if(currentPlayer == 1) {
            playerOne.decrementMana(currentHero.getMana());
        } else {
            playerTwo.decrementMana(currentHero.getMana());
        }

        return null;
    }

    private boolean isEnemyRow(int currentPlayer, int row) {
        if (currentPlayer == 1) {
            if (row == 0 || row == 1) {
                return true;
            } else {
                return false;
            }
        } else if (currentPlayer == 2) {
            if (row == 2 || row == 3) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isAllyRow(int currentPlayer, int row) {
        if (currentPlayer == 1) {
            if (row == 2 || row == 3) {
                return true;
            } else {
                return false;
            }
        } else if (currentPlayer == 2) {
            if (row == 0 || row == 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    // checking if the enemy hero is dead
    // this is done in the main class
    public boolean isEnemyHeroDead(int currentPlayer) {
        Hero enemyHero;
        if (currentPlayer == 1) {
            enemyHero = playerTwo.getHero();
        } else {
            enemyHero = playerOne.getHero();
        }

        if (enemyHero.getHealth() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    private Hero createHero(CardInput heroInput) {
        switch (heroInput.getName()) {
            case "Lord Royce":
                return new LordRoyce(heroInput);
            case "Empress Thorina":
                return new EmpressThorina(heroInput);
            case "General Kocioraw":
                return new GeneralKocioraw(heroInput);
            case "King Mudface":
                return new KingMudface(heroInput);
        }
        return null;
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

    public ArrayList<Minion> getFrozenCardsOnTable() {
        ArrayList<Minion> frozenCards = new ArrayList<>();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                Minion card = gameboard.getCardAtPosition(row, col);
                if (card != null && card.isFrozen()) {
                    frozenCards.add(card);
                }
            }
        }
        return frozenCards;
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
