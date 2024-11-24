package game;

import java.util.ArrayList;

import fileio.CardInput;
import fileio.DecksInput;
import fileio.StartGameInput;
import heroes.EmpressThorina;
import heroes.GeneralKocioraw;
import heroes.KingMudface;
import heroes.LordRoyce;

import static main.Constants.ROWS;
import static main.Constants.COLUMNS;
import static main.Constants.BACK_ROW_PLAYER1;
import static main.Constants.FRONT_ROW_PLAYER2;
import static main.Constants.FRONT_ROW_PLAYER1;
import static main.Constants.BACK_ROW_PLAYER2;


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
    private static int playerOneWins;
    private static int playerTwoWins;
    private static int totalGamesPlayed;

    public Game(final StartGameInput startGame, final DecksInput playerOneDecks,
                final DecksInput playerTwoDecks) {
        initializeGame(startGame, playerOneDecks, playerTwoDecks);
    }

    // initializing the game
    // this is the first thing that is done
    // when the game is created
    // the players are created, the gameboard is created
    private void initializeGame(final StartGameInput startGame, final DecksInput playerOneDecks,
                                final DecksInput playerTwoDecks) {
        this.playerOne = new Player(
                playerOneDecks.getDecks().
                        get(startGame.getPlayerOneDeckIdx()), // getting the deck from the input
                createHero(startGame.getPlayerOneHero()), // getting the hero from the input
                startGame.getShuffleSeed()
        );

        this.playerTwo = new Player(
                playerTwoDecks.getDecks().
                        get(startGame.getPlayerTwoDeckIdx()), // getting the deck from the input
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

    /**
     * basically with this method I only try to get the card I want to place on the table
     * then ,,pass" it to the addCard method from the gameboard which ACTUALLY places the card
     * on the table
     * Also Im verifying if the player has enough mana to place it and if the placing
     * on the table was done correctly +
     * @param playerIndex is the index of the player who is supposed to place the card
     * @param handIndex I need the index to know which card from the hand Im placing on the table
     * @return just returning true if I placed the card, false if it failed or it was impossible
     */
    public boolean placeCard(final int playerIndex, final int handIndex) {
        Player placingPlayer;
        // stabilizing the current player
        if (playerIndex == 1) {
            placingPlayer = playerOne;
        } else {
            placingPlayer = playerTwo;
        }
        // getting the card to place
        Minion cardToPlace = placingPlayer.getHand().get(handIndex);
        // error message if the card cannot be placed
        // because of the mana
        if (!placingPlayer.decrementMana(cardToPlace.getMana())) {
            errorMessage = "Not enough mana to place card on table.";
            return false;
        }

        boolean placedSuccessfully = gameboard.addCard(cardToPlace, playerIndex);
        if (placedSuccessfully) {
            placingPlayer.getHand().remove(handIndex);
            return true; // card was placed successfully
        } else {
            // getting the mana back if placing didn't take place
            placingPlayer.incrementMana(cardToPlace.getMana());
            return false;
        }
    }

    /**
     * Function for ending a player's turn
     * Ok so basically we have to main cases
     * one is starting player is 1 and
     * the 2nd one is the starting player is 2
     * to avoid drawing cards when I shouldn't
     * I will check if the current player is the starting player
     * and I will draw cards only if the current player is not the starting player
     * which means that the starting player has already ended his turn
     * I ALSO TREAT ENDING THE ROUND HERE
     * CHECKING WHO IS THE STARTING PLAYER HELPS ME A LOT HERE
     * So if the NOT STARTING PLAYER is ending his turn
     * it means that the round is ending, and Im doing
     * what im supossed to do when ending a round
     */
    public void endPlayerTurn() {
        if (startingPlayerId == 1) {
            if (currentPlayer == 1) {
                currentPlayer = 2;
                unfreezeCurrentPlayerCards(1);
            } else { // ENDING THE ROUND
                //corrected this
                // mana shuld be incremented for the next round
                // after the current round ends (the second player's turn)
                playerOne.incrementMana(round + 1);
                playerTwo.incrementMana(round + 1);
                currentPlayer = 1; //going back to the starting player
                round++; //ending the round means that we go to the next round
                resetAllMinions(); // reset all minions' attack states
                playerOne.getHero().setHasUsedAbility(false);
                playerTwo.getHero().setHasUsedAbility(false);
                playerOne.drawCard(); // after ending the round, a new round starts
                playerTwo.drawCard(); // so every player draws a new card
                unfreezeCurrentPlayerCards(2);
            }
        } else if (startingPlayerId == 2) {
            if (currentPlayer == 2) {
                currentPlayer = 1;
                unfreezeCurrentPlayerCards(2);
            } else { //treating ending the round stuff like I said
                playerTwo.incrementMana(round + 1);
                playerOne.incrementMana(round + 1);
                currentPlayer = 2;
                round++;
                resetAllMinions(); // reset all minions' attack states
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
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Minion minion = gameboard.getBoard()[row][col];
                if (minion != null) {
                    minion.setHasAttackedThisTurn(false);
                }
            }
        }
    }
    // so i had to create a new method
    private void unfreezeCurrentPlayerCards(final int activePlayer) {
        int startRow, endRow;

        if (activePlayer == 1) {
            startRow = FRONT_ROW_PLAYER1; // ROW 2
            endRow = BACK_ROW_PLAYER1; // ROW 3
        } else {
            startRow = BACK_ROW_PLAYER2; // ROW 0
            endRow = FRONT_ROW_PLAYER2; // ROW 1
        }

        for (int row = startRow; row <= endRow; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Minion minion = gameboard.getCardAtPosition(row, col);
                if (minion != null && minion.isFrozen()) {
                    minion.setUnfrozen();
                }
            }
        }
    }

    /***
     * This is just a method to kind of call the attackCard method from the gameboard
     * and put the result in main, attacking the card is done in the gameboard
     * so the method for that is in the gameboard class
     * I just pass the next parameters to the gameboard's attackCard method
     * @param attackerRow - the row of the attacker this is the row of the card that attacks
     * @param attackerCol - the col of the attacker this is the col of the card that attacks4
     * @param targetRow - the row of the target this is the row of the card that is attacked
     * @param targetCol - the col of the target this is the col of the card that is attacked
     * @returning the result of the attackCard method from the gameboard
     */
    public String attackCard(final int attackerRow, final int attackerCol,
                             final int targetRow, final int targetCol) {
        // As I said in the Gameboard class attackCard method
        // returns the error if its the case or null if not
        String result = gameboard.attackCard(attackerRow, attackerCol,
                                             targetRow, targetCol, currentPlayer);
        return result; // result will be used in main
    }

    /***
     * The logic is inspired from the attackCard method
     * but now I have to attack the hero, that is not on the gameboard so i have to
     * implement it here in the game class
     * The method returns the error if its the case or null if everything is ok
     * Im giving the following parameters
     * @param attackerRow - the row of the attacker this is the row of the card that attacks
     * @param attackerCol - the col of the attacker this is the col of the card that attacks
     * @param attackingPlayer - the player that is attacking
     * First im getting the attacker card from the gameboard
     * then im getting the enemy hero and after i start checking for the usual errors
     * if the attacker is frozen, if the attacker has already attacked etc
     * then I check if the enemy hero can be attacked - search for enemy tanks
     * because the hero can only be attacked if there are no tanks
     * then I decrease the health of the enemy hero by the attacker's attack damage
     * and set the attacker to have attacked this turn because attacking the hero
     * also counts as an attack
     * @return
     */
    public String attackHero(final int attackerRow, final int attackerCol,
                             final int attackingPlayer) {
        Minion attacker = gameboard.getCardAtPosition(attackerRow, attackerCol);
        Hero enemyHero;
        int enemyFrontRow;

        if (attackingPlayer == 1) {
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
        for (int i = 0; i < COLUMNS; i++) {
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

    /***
     * On the same skel that I used before, I return the error that I should, or null
     * if everything is ok
     * Here I have to use the hero's ability, on some rowns meaning that I have to
     * apply it to the gameboard
     * First im getting the hero and then i do the basic checks
     * if it has mana and id it has alr attacked
     * But then as some heroes affect own minions and some enemy minions, depending
     * on the hero I have to check if the row is valid
     * if everything is ok, I apply the method useHeroAbility from the gameboard
     * that will apply the hero's ability to the gameboard
     * and then just setting the hero to have used the ability and decrementing the mana
     * @param row - the row affected by heroes ability
     * @param activePlayer - the player that is using the hero's ability
     * @return
     */
    public String useHeroAbility(final int row, final int activePlayer) {
        Hero currentHero;

        if (activePlayer == 1) {
            currentHero = playerOne.getHero();
        } else {
            currentHero = playerTwo.getHero();
        }

        if (currentHero.getMana() > getPlayerMana(activePlayer)) {
            return "Not enough mana to use hero's ability.";
        }

        if (currentHero.hasUsedAbility()) {
            return "Hero has already attacked this turn.";
        }

        String potentialError = currentHero.canTargetRow(isAllyRow(activePlayer, row));
        if (potentialError != null) {
            return potentialError;
        }
        // using the hero ability
        currentHero.useHeroAbility(row, gameboard);
        currentHero.setHasUsedAbility(true);
        // decrementing the mana for the hero ability
        if (activePlayer == 1) {
            playerOne.decrementMana(currentHero.getMana());
        } else {
            playerTwo.decrementMana(currentHero.getMana());
        }

        return null;
    }

    private boolean isEnemyRow(final int activePlayer, final int row) {
        if (activePlayer == 1) {
            if (row == BACK_ROW_PLAYER2 || row == FRONT_ROW_PLAYER2) {
                return true;
            }
            return false;
        } else if (activePlayer == 2) {
            if (row == FRONT_ROW_PLAYER1 || row == BACK_ROW_PLAYER1) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean isAllyRow(final int activePlayer, final int row) {
        if (activePlayer == 1) {
            if (row == FRONT_ROW_PLAYER1 || row == BACK_ROW_PLAYER1) {
                return true;
            }
                return false;
        } else if (activePlayer == 2) {
            if (row == BACK_ROW_PLAYER2 || row == FRONT_ROW_PLAYER2) {
                return true;
            }
                return false;
        } else {
            return false;
        }
    }

    /***
     * just checking if the enemy hero is dead
     * used for also treating the wins and game ending
     * so here is where im incrementing the wins and the total games played
     * @param activePlayer
     * @return
     */
    public boolean isEnemyHeroDead(final int activePlayer) {
        Hero enemyHero;
        if (activePlayer == 1) {
            enemyHero = playerTwo.getHero();
        } else {
            enemyHero = playerOne.getHero();
        }

        if (enemyHero.getHealth() <= 0) {
            // Added the win to the player
            // basically if the hero has under 0 health
            // and the curr player is 1, player 1 wins
            // if the curr player is 2, player 2 wins
            if (activePlayer == 1) {
                playerOneWins++;
            } else {
                playerTwoWins++;
            }
            // either of the players wins
            // the total games played is incremented
            totalGamesPlayed++;
            return true;
        } else {
            return false;
        }
    }


    private Hero createHero(final CardInput heroInput) {
        switch (heroInput.getName()) {
            case "Lord Royce":
                return new LordRoyce(heroInput);
            case "Empress Thorina":
                return new EmpressThorina(heroInput);
            case "General Kocioraw":
                return new GeneralKocioraw(heroInput);
            case "King Mudface":
                return new KingMudface(heroInput);
            default: // I must have a default case (checkstyle)
                return null;
        }
    }


    public final ArrayList<ArrayList<Minion>> getCardsOnTable() {
        return gameboard.getCardsOnTable();
    }

    public final ArrayList<Minion> getPlayerOneDeck() {
        return playerOne.getDeck();
    }

    public final ArrayList<Minion> getPlayerTwoDeck() {
        return playerTwo.getDeck();
    }

    public final Hero getPlayerOneHero() {
        return playerOne.getHero();
    }

    public final Hero getPlayerTwoHero() {
        return playerTwo.getHero();
    }

    /***
     * getting the player's mana and returning it
     * this is like a getter of a getter because I need the mana
     * from the player to get it in the main class
     * @param playerIndex - i only need the index of the player
     * @return
     */
    public int getPlayerMana(final int playerIndex) {
        if (playerIndex == 1) {
            return playerOne.getMana();
        } else if (playerIndex == 2) {
            return playerTwo.getMana();
        }
        return -1;
    }

    /***
     * iterating thru the gameboard and getting the frozen cards
     * i need this because I have a command that needs them all
     * @return
     */
    public ArrayList<Minion> getFrozenCardsOnTable() {
        ArrayList<Minion> frozenCards = new ArrayList<>();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
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
    public final Player getPlayerOne() {
        return playerOne;
    }

    public final Player getPlayerTwo() {
        return playerTwo;
    }

    public final int getCurrentPlayer() {
        return currentPlayer;
    }

    public final Gameboard getGameboard() {
        return gameboard;
    }

    public final String getErrorMessage() {
        return errorMessage;
    }

    public static int getPlayerOneWins() {
        return playerOneWins;
    }

    public static int getPlayerTwoWins() {
        return playerTwoWins;
    }

    public static int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    /***
     * I have to set this in 0 in main before starting the GAMES
     */
    public static void setPlayerOneWins() {
        playerOneWins = 0;
    }

    /***
     * I have to set this in 0 in main before starting the GAMES
     */
    public static void setPlayerTwoWins() {
        playerTwoWins = 0;
    }

    /***
     * I have to set this in 0 in main before starting the GAMES
     */
    public static void setTotalGamesPlayed() {
        totalGamesPlayed = 0;
    }


}
