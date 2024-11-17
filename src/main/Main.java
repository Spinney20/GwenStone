package main;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import fileio.DecksInput;
import fileio.StartGameInput;
import fileio.ActionsInput;
import game.Game;
import checker.Checker;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
import fileio.Input;
import game.Gameboard;
import game.Hero;
import game.Minion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1, final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1), Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        StartGameInput startGame = inputData.getGames().get(0).getStartGame();
        DecksInput playerOneDecks = inputData.getPlayerOneDecks();
        DecksInput playerTwoDecks = inputData.getPlayerTwoDecks();
        Game game = new Game(startGame, playerOneDecks, playerTwoDecks);
        Gameboard gameboard = game.getGameboard();

        for (ActionsInput action : inputData.getGames().get(0).getActions()) {
            ObjectNode gameOutput = objectMapper.createObjectNode();

            switch (action.getCommand()) {
                case "getPlayerDeck":
                    ArrayNode deckOutput = objectMapper.createArrayNode();
                    ArrayList<Minion> deck;

                    if (action.getPlayerIdx() == 1) {
                        deck = game.getPlayerOneDeck();
                    } else {
                        deck = game.getPlayerTwoDeck();
                    }

                    for (Minion card : deck) {
                        ObjectNode cardNode = objectMapper.createObjectNode();
                        cardNode.put("mana", card.getMana());
                        cardNode.put("attackDamage", card.getAttackDamage());
                        cardNode.put("health", card.getHealth());
                        cardNode.put("description", card.getDescription());
                        ArrayNode colorsNode = objectMapper.createArrayNode();
                        for (String color : card.getColors()) {
                            colorsNode.add(color);
                        }
                        cardNode.set("colors", colorsNode);
                        cardNode.put("name", card.getName());
                        deckOutput.add(cardNode);
                    }

                    gameOutput.put("command", "getPlayerDeck");
                    gameOutput.put("playerIdx", action.getPlayerIdx());
                    gameOutput.set("output", deckOutput);
                    output.add(gameOutput);
                    break;

                case "getPlayerHero":
                    Hero hero = action.getPlayerIdx() == 1 ? game.getPlayerOneHero() : game.getPlayerTwoHero();
                    if (hero != null) {
                        ObjectNode heroNode = objectMapper.createObjectNode();
                        heroNode.put("mana", hero.getMana());
                        heroNode.put("description", hero.getDescription());
                        ArrayNode colorsNode = objectMapper.createArrayNode();
                        for (String color : hero.getColors()) {
                            colorsNode.add(color);
                        }
                        heroNode.set("colors", colorsNode);
                        heroNode.put("name", hero.getName());
                        heroNode.put("health", hero.getHealth());

                        gameOutput.put("command", "getPlayerHero");
                        gameOutput.put("playerIdx", action.getPlayerIdx());
                        gameOutput.set("output", heroNode);
                    }
                    output.add(gameOutput);
                    break;

                case "getPlayerTurn":
                    gameOutput.put("command", "getPlayerTurn");
                    gameOutput.put("output", game.getCurrentPlayer());
                    output.add(gameOutput);
                    break;

                case "placeCard":
                    int playerIdx = game.getCurrentPlayer();
                    int handIdx = action.getHandIdx();

                    boolean cardPlaced = game.placeCard(playerIdx, handIdx);
                    if(!cardPlaced) {
                        gameOutput.put("command", "placeCard");
                        gameOutput.put("error", game.getErrorMessage());
                        gameOutput.put("handIdx", handIdx);
                        output.add(gameOutput);
                    }
                    break;

                case "endPlayerTurn":
                    game.endPlayerTurn();
                    break;

                case "getCardsInHand":
                    ArrayNode handOutput = objectMapper.createArrayNode();

                    // What player hand you want I give you :))
                    ArrayList<Minion> hand;
                    if (action.getPlayerIdx() == 1) {
                        hand = game.getPlayerOne().getHand();
                    } else {
                        hand = game.getPlayerTwo().getHand();
                    }

                    // Adding each card with a for in the hand
                    for (Minion card : hand) {
                        ObjectNode cardNode = objectMapper.createObjectNode();
                        cardNode.put("mana", card.getMana());
                        cardNode.put("attackDamage", card.getAttackDamage());
                        cardNode.put("health", card.getHealth());
                        cardNode.put("description", card.getDescription());

                        ArrayNode colorsNode = objectMapper.createArrayNode();
                        for (String color : card.getColors()) {
                            colorsNode.add(color);
                        }
                        cardNode.set("colors", colorsNode);

                        cardNode.put("name", card.getName());
                        handOutput.add(cardNode);
                    }

                    // this is the output for the action
                    gameOutput.put("command", "getCardsInHand");
                    gameOutput.put("playerIdx", action.getPlayerIdx());
                    gameOutput.set("output", handOutput);
                    output.add(gameOutput);
                    break;

                case "getPlayerMana":
                    int mana = game.getPlayerMana(action.getPlayerIdx());
                    gameOutput.put("command", "getPlayerMana");
                    gameOutput.put("playerIdx", action.getPlayerIdx());
                    gameOutput.put("output", mana);
                    output.add(gameOutput);
                    break;

                case "getCardsOnTable":
                    ArrayNode tableOutput = objectMapper.createArrayNode();
                    ArrayList<ArrayList<Minion>> cardsOnTable = game.getCardsOnTable();

                    for (ArrayList<Minion> rowCards : cardsOnTable) {
                        ArrayNode rowArray = objectMapper.createArrayNode();

                        for (Minion card : rowCards) {
                            ObjectNode cardNode = objectMapper.createObjectNode();
                            cardNode.put("mana", card.getMana());
                            cardNode.put("attackDamage", card.getAttackDamage());
                            cardNode.put("health", card.getHealth());
                            cardNode.put("description", card.getDescription());

                            ArrayNode colorsNode = objectMapper.createArrayNode();
                            for (String color : card.getColors()) {
                                colorsNode.add(color);
                            }
                            cardNode.set("colors", colorsNode);
                            cardNode.put("name", card.getName());

                            rowArray.add(cardNode);
                        }

                        tableOutput.add(rowArray);
                    }

                    gameOutput.put("command", "getCardsOnTable");
                    gameOutput.set("output", tableOutput);
                    output.add(gameOutput);
                    break;
                case "cardUsesAttack":
                    int attackerRow = action.getCardAttacker().getX();
                    int attackerCol = action.getCardAttacker().getY();
                    int targetRow = action.getCardAttacked().getX();
                    int targetCol = action.getCardAttacked().getY();

                    String attackResult = game.attackCard(attackerRow, attackerCol, targetRow, targetCol);

                    if (attackResult != null) {
                        gameOutput.put("command", "cardUsesAttack");
                        gameOutput.put("error", attackResult);
                        gameOutput.set("cardAttacker", objectMapper.createObjectNode()
                                .put("x", attackerRow)
                                .put("y", attackerCol));
                        gameOutput.set("cardAttacked", objectMapper.createObjectNode()
                                .put("x", targetRow)
                                .put("y", targetCol));
                        output.add(gameOutput);
                    }
                    break;
                case "getCardAtPosition":
                    int x = action.getX();
                    int y = action.getY();

                    Minion card = game.getGameboard().getCardAtPosition(x, y);
                    ObjectNode cardOutput = objectMapper.createObjectNode();
                    cardOutput.put("command", "getCardAtPosition");
                    cardOutput.put("x", x);
                    cardOutput.put("y", y);

                    if (card != null) {
                        ObjectNode cardDetails = objectMapper.createObjectNode();
                        cardDetails.put("mana", card.getMana());
                        cardDetails.put("attackDamage", card.getAttackDamage());
                        cardDetails.put("health", card.getHealth());
                        cardDetails.put("description", card.getDescription());
                        ArrayNode colorsArray = objectMapper.createArrayNode();
                        for (String color : card.getColors()) {
                            colorsArray.add(color);
                        }
                        cardDetails.set("colors", colorsArray);
                        cardDetails.put("name", card.getName());
                        cardOutput.set("output", cardDetails);
                    } else {
                        cardOutput.put("output", "No card available at that position.");
                    }

                    output.add(cardOutput);
                    break;
                case "cardUsesAbility":
                    int userRow = action.getCardAttacker().getX();
                    int userCol = action.getCardAttacker().getY();
                    int targetRowPrim = action.getCardAttacked().getX();
                    int targetColPrim = action.getCardAttacked().getY();

                    String abilityResult = gameboard.useAbility(userRow, userCol, targetRowPrim, targetColPrim, game.getCurrentPlayer());

                    if (abilityResult != null) {
                        gameOutput.put("command", "cardUsesAbility");
                        gameOutput.put("error", abilityResult);
                        gameOutput.set("cardAttacker", objectMapper.createObjectNode()
                                .put("x", userRow)
                                .put("y", userCol));
                        gameOutput.set("cardAttacked", objectMapper.createObjectNode()
                                .put("x", targetRowPrim)
                                .put("y", targetColPrim));
                        output.add(gameOutput);
                    }
                    break;


                default:
                    System.out.println("error " + action.getCommand());
                    break;
            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }

}