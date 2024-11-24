package main;

import fileio.ActionsInput;
import fileio.DecksInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;
import game.Game;
import checker.Checker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;

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
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH
                + filePath1), Input.class);

        ArrayNode output = objectMapper.createArrayNode();
        Game.setPlayerOneWins();
        Game.setPlayerTwoWins();
        Game.setTotalGamesPlayed();

        for (GameInput gameInput : inputData.getGames()) {
            StartGameInput startGame = gameInput.getStartGame();
            DecksInput playerOneDecks = inputData.getPlayerOneDecks();
            DecksInput playerTwoDecks = inputData.getPlayerTwoDecks();
            Game game = new Game(startGame, playerOneDecks, playerTwoDecks);

            for (ActionsInput action : gameInput.getActions()) {

                switch (action.getCommand()) {
                    case "getPlayerDeck":
                        Commands.handleGetPlayerDeck(action, game, output, objectMapper);
                        break;

                    case "getPlayerHero":
                        Commands.handleGetPlayerHero(action, game, output, objectMapper);
                        break;

                    case "getPlayerTurn":
                        Commands.handleGetPlayerTurn(game, output, objectMapper);
                        break;

                    case "placeCard":
                        Commands.handlePlaceCard(action, game, output, objectMapper);
                        break;

                    case "endPlayerTurn":
                        game.endPlayerTurn();
                        break;

                    case "getCardsInHand":
                        Commands.handleGetCardsInHand(action, game, output, objectMapper);
                        break;

                    case "getPlayerMana":
                        Commands.handleGetPlayerMana(action, game, output, objectMapper);
                        break;

                    case "getCardsOnTable":
                        Commands.handleGetCardsOnTable(game, output, objectMapper);
                        break;
                    case "cardUsesAttack":
                        Commands.handleCardUsesAttack(action, game, output, objectMapper);
                        break;
                    case "getCardAtPosition":
                        Commands.handleGetCardAtPosition(action, game, output, objectMapper);
                        break;
                    case "cardUsesAbility":
                        Commands.handleCardUsesAbility(action, game, output, objectMapper);
                        break;
                    case "useAttackHero":
                        Commands.handleUseAttackHero(action, game, output, objectMapper);
                        break;
                    case "useHeroAbility":
                        Commands.handleUseHeroAbility(action, game, output, objectMapper);
                        break;
                    case "getFrozenCardsOnTable":
                        Commands.handleGetFrozenCardsOnTable(game, output, objectMapper);
                        break;
                    case "getPlayerOneWins":
                        Commands.handleGetPlayerOneWins(output, objectMapper);
                        break;

                    case "getPlayerTwoWins":
                        Commands.handleGetPlayerTwoWins(output, objectMapper);
                        break;

                    case "getTotalGamesPlayed":
                        Commands.handleGetTotalGamesPlayed(output, objectMapper);
                        break;
                    default:
                        System.out.println("error " + action.getCommand());
                        break;


                }

            }
        }
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }


}
