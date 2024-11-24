package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import game.Game;
import game.Hero;
import game.Minion;

import java.util.ArrayList;

public final class Commands {

    //private constructor to prevent instantiation
    private Commands() {
    }

    /***
     * Handles the getPlayerDeck command.
     * uses createCardNode to create a card node for each card in the deck.
     * @param action - the action input
     * @param game - the game object
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handleGetPlayerDeck(final ActionsInput action, final Game game,
                                           final ArrayNode output,
                                           final ObjectMapper objectMapper) {
        ArrayNode deckOutput = objectMapper.createArrayNode();
        ArrayList<Minion> deck;

        if (action.getPlayerIdx() == 1) {
            deck = game.getPlayerOneDeck();
        } else {
            deck = game.getPlayerTwoDeck();
        }

        for (Minion card : deck) {
            deckOutput.add(createCardNode(card, objectMapper));
        }

        ObjectNode gameOutput = objectMapper.createObjectNode();
        gameOutput.put("command", "getPlayerDeck");
        gameOutput.put("playerIdx", action.getPlayerIdx());
        gameOutput.set("output", deckOutput);
        output.add(gameOutput);
    }

    /***
     * Creates a card node for a given card.
     * Used in multiple commands to create a card node for a given card.
     * @param card - the card to create a node for
     * @param objectMapper - the object mapper
     * @return - the card node
     */
    private static ObjectNode createCardNode(final Minion card, final ObjectMapper objectMapper) {
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
        return cardNode;
    }

    /***
     * Handles the getPlayerHero command.
     * gives the hero of a given player.
     * uses createHeroNode to create a hero node for the hero.
     * @param action - the action input
     * @param game - the game object
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handleGetPlayerHero(final ActionsInput action, final Game game,
                                           final ArrayNode output,
                                           final ObjectMapper objectMapper) {
        Hero hero;

        if (action.getPlayerIdx() == 1) {
            hero = game.getPlayerOneHero();
        } else {
            hero = game.getPlayerTwoHero();
        }

        ObjectNode gameOutput = objectMapper.createObjectNode();
        if (hero != null) {
            gameOutput.put("command", "getPlayerHero");
            gameOutput.put("playerIdx", action.getPlayerIdx());
            gameOutput.set("output", createHeroNode(hero, objectMapper));
        } else {
            gameOutput.put("command", "getPlayerHero");
            gameOutput.put("playerIdx", action.getPlayerIdx());
            gameOutput.put("output", "Hero not found.");
        }
        output.add(gameOutput);
    }

    private static ObjectNode createHeroNode(final Hero hero, final ObjectMapper objectMapper) {
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
        return heroNode;
    }

    /***
     * Handles the getPlayerTurn command.
     * gives the current player that is playing.
     * @param game - the game object
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handleGetPlayerTurn(final Game game, final ArrayNode output,
                                           final ObjectMapper objectMapper) {
        ObjectNode gameOutput = objectMapper.createObjectNode();
        gameOutput.put("command", "getPlayerTurn");
        gameOutput.put("output", game.getCurrentPlayer());
        output.add(gameOutput);
    }

    /***
     * Handles the endTurn command.
     * ends the turn of the current player.
     * @param action - the action input
     * @param game - the game object
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handlePlaceCard(final ActionsInput action, final Game game,
                                       final ArrayNode output, final ObjectMapper objectMapper) {
        ObjectNode gameOutput = objectMapper.createObjectNode();
        int playerIdx = game.getCurrentPlayer();
        int handIdx = action.getHandIdx();

        boolean cardPlaced = game.placeCard(playerIdx, handIdx);
        if (!cardPlaced) {
            gameOutput.put("command", "placeCard");
            gameOutput.put("error", game.getErrorMessage());
            gameOutput.put("handIdx", handIdx);
            output.add(gameOutput);
        }
    }

    /***
     * Handles the getCardsInHand command.
     * gives the cards in the hand of a given player.
     * uses createCardNode to create a card node for each card in the hand.
     * @param action - the action input
     * @param game - the game object
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handleGetCardsInHand(final ActionsInput action, final Game game,
                                            final ArrayNode output,
                                            final ObjectMapper objectMapper) {
        ArrayNode handOutput = objectMapper.createArrayNode();

        ArrayList<Minion> hand;
        if (action.getPlayerIdx() == 1) {
            hand = game.getPlayerOne().getHand();
        } else {
            hand = game.getPlayerTwo().getHand();
        }

        for (Minion card : hand) {
            handOutput.add(createCardNode(card, objectMapper));
        }

        ObjectNode gameOutput = objectMapper.createObjectNode();
        gameOutput.put("command", "getCardsInHand");
        gameOutput.put("playerIdx", action.getPlayerIdx());
        gameOutput.set("output", handOutput);
        output.add(gameOutput);
    }

    /***
     * Handles the getPlayerMana command.
     * gives the mana of a given player.
     * @param action - the action input
     * @param game - the game object
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handleGetPlayerMana(final ActionsInput action, final Game game,
                                           final ArrayNode output,
                                           final ObjectMapper objectMapper) {
        int mana = game.getPlayerMana(action.getPlayerIdx());

        ObjectNode gameOutput = objectMapper.createObjectNode();
        gameOutput.put("command", "getPlayerMana");
        gameOutput.put("playerIdx", action.getPlayerIdx());
        gameOutput.put("output", mana);

        output.add(gameOutput);
    }

    /***
     * Handles the getCardsOnTable command.
     * gives the cards on the table.
     * uses createCardNode to create a card node for each card on the table.
     * @param game - the game object
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handleGetCardsOnTable(final Game game, final ArrayNode output,
                                             final ObjectMapper objectMapper) {
        ArrayNode tableOutput = objectMapper.createArrayNode();
        ArrayList<ArrayList<Minion>> cardsOnTable = game.getCardsOnTable();

        for (ArrayList<Minion> rowCards : cardsOnTable) {
            ArrayNode rowArray = objectMapper.createArrayNode();

            for (Minion card : rowCards) {
                rowArray.add(createCardNode(card, objectMapper));
            }

            tableOutput.add(rowArray);
        }

        ObjectNode gameOutput = objectMapper.createObjectNode();
        gameOutput.put("command", "getCardsOnTable");
        gameOutput.set("output", tableOutput);
        output.add(gameOutput);
    }

    /***
     * Handles the cardUsesAttack command.
     * attacks a card with another card.
     * @param action - the action input
     * @param game - the game object
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handleCardUsesAttack(final ActionsInput action, final Game game,
                                            final ArrayNode output,
                                            final ObjectMapper objectMapper) {
        ObjectNode gameOutput = objectMapper.createObjectNode();
        int attackerRow = action.getCardAttacker().getX();
        int attackerCol = action.getCardAttacker().getY();
        int targetRow = action.getCardAttacked().getX();
        int targetCol = action.getCardAttacked().getY();

        String attackResult = game.attackCard(attackerRow, attackerCol, targetRow, targetCol);

        if (attackResult != null) {
            gameOutput.put("command", "cardUsesAttack");
            gameOutput.put("error", attackResult);

            ObjectNode cardAttackerNode = objectMapper.createObjectNode();
            cardAttackerNode.put("x", attackerRow);
            cardAttackerNode.put("y", attackerCol);
            gameOutput.set("cardAttacker", cardAttackerNode);

            ObjectNode cardAttackedNode = objectMapper.createObjectNode();
            cardAttackedNode.put("x", targetRow);
            cardAttackedNode.put("y", targetCol);
            gameOutput.set("cardAttacked", cardAttackedNode);

            output.add(gameOutput);
        }
    }

    /***
     * Handles the getCardAtPosition command.
     * gives the card at a given position.
     * uses createCardNode to create a card node for the card.
     * @param action - the action input
     * @param game - the game object
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handleGetCardAtPosition(final ActionsInput action, final Game game,
                                               final ArrayNode output,
                                               final ObjectMapper objectMapper) {
        int x = action.getX();
        int y = action.getY();

        Minion card = game.getGameboard().getCardAtPosition(x, y);
        ObjectNode cardOutput = objectMapper.createObjectNode();
        cardOutput.put("command", "getCardAtPosition");
        cardOutput.put("x", x);
        cardOutput.put("y", y);

        if (card != null) {
            cardOutput.set("output", createCardNode(card, objectMapper));
        } else {
            cardOutput.put("output", "No card available at that position.");
        }

        output.add(cardOutput);
    }

    /***
     * Handles the cardUsesAbility command.
     * uses the ability of a card on another card.
     * @param action - the action input
     * @param game - the game object
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handleCardUsesAbility(final ActionsInput action, final Game game,
                                             final ArrayNode output,
                                             final ObjectMapper objectMapper) {
        ObjectNode gameOutput = objectMapper.createObjectNode();
        int userRow = action.getCardAttacker().getX();
        int userCol = action.getCardAttacker().getY();
        int targetRow = action.getCardAttacked().getX();
        int targetCol = action.getCardAttacked().getY();

        String abilityResult = game.getGameboard().
                useAbility(userRow, userCol, targetRow, targetCol, game.getCurrentPlayer());

        if (abilityResult != null) {
            gameOutput.put("command", "cardUsesAbility");
            gameOutput.put("error", abilityResult);

            ObjectNode cardAttackerNode = objectMapper.createObjectNode();
            cardAttackerNode.put("x", userRow);
            cardAttackerNode.put("y", userCol);
            gameOutput.set("cardAttacker", cardAttackerNode);

            ObjectNode cardAttackedNode = objectMapper.createObjectNode();
            cardAttackedNode.put("x", targetRow);
            cardAttackedNode.put("y", targetCol);
            gameOutput.set("cardAttacked", cardAttackedNode);

            output.add(gameOutput);
        }
    }

    /***
     * Handles the useAttackHero command.
     * attacks the enemy hero with a card.
     * @param action - the action input
     * @param game - the game object
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handleUseAttackHero(final ActionsInput action, final Game game,
                                           final ArrayNode output,
                                           final ObjectMapper objectMapper) {
        ObjectNode gameOutput = objectMapper.createObjectNode();
        int heroAttackerRow = action.getCardAttacker().getX();
        int heroAttackerCol = action.getCardAttacker().getY();

        String heroAttackResult = game.
                attackHero(heroAttackerRow, heroAttackerCol, game.getCurrentPlayer());

        if (heroAttackResult != null) {
            gameOutput.put("command", "useAttackHero");
            gameOutput.put("error", heroAttackResult);

            ObjectNode cardAttackerNode = objectMapper.createObjectNode();
            cardAttackerNode.put("x", heroAttackerRow);
            cardAttackerNode.put("y", heroAttackerCol);
            gameOutput.set("cardAttacker", cardAttackerNode);

            output.add(gameOutput);
        }

        // Check if the enemy hero is dead
        if (game.isEnemyHeroDead(game.getCurrentPlayer())) {
            ObjectNode gameEndOutput = objectMapper.createObjectNode();
            if (game.getCurrentPlayer() == 1) {
                gameEndOutput.put("gameEnded", "Player one killed the enemy hero.");
            } else {
                gameEndOutput.put("gameEnded", "Player two killed the enemy hero.");
            }
            output.add(gameEndOutput);
        }
    }

    /***
     * Handles the useHeroAbility command.
     * uses the ability of the hero on a row.
     * @param action - the action input
     * @param game - the game object
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handleUseHeroAbility(final ActionsInput action, final Game game,
                                            final ArrayNode output,
                                            final ObjectMapper objectMapper) {
        ObjectNode gameOutput = objectMapper.createObjectNode();
        int affectedRow = action.getAffectedRow();

        String heroAbilityResult = game.useHeroAbility(affectedRow, game.getCurrentPlayer());

        if (heroAbilityResult != null) {
            gameOutput.put("command", "useHeroAbility");
            gameOutput.put("error", heroAbilityResult);
            gameOutput.put("affectedRow", affectedRow);
            output.add(gameOutput);
        }
    }

    /***
     * Handles the getFrozenCardsOnTable command.
     * gives the frozen cards on the table.
     * uses createCardNode to create a card node for each frozen card.
     * @param game - the game object
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handleGetFrozenCardsOnTable(final Game game, final ArrayNode output,
                                                   final ObjectMapper objectMapper) {
        ArrayList<Minion> frozenCards = game.getFrozenCardsOnTable();
        ArrayNode frozenCardsOutput = objectMapper.createArrayNode();

        for (Minion frozenCard : frozenCards) {
            frozenCardsOutput.add(createCardNode(frozenCard, objectMapper));
        }

        ObjectNode gameOutput = objectMapper.createObjectNode();
        gameOutput.put("command", "getFrozenCardsOnTable");
        gameOutput.set("output", frozenCardsOutput);
        output.add(gameOutput);
    }

    /***
     * Handles the GetPlayerOneWins command.
     * gives the number of wins player one has.
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handleGetPlayerOneWins(final ArrayNode output,
                                              final ObjectMapper objectMapper) {
        ObjectNode gameOutput = objectMapper.createObjectNode();
        gameOutput.put("command", "getPlayerOneWins");
        gameOutput.put("output", Game.getPlayerOneWins());
        output.add(gameOutput);
    }

    /***
     * Handles the GetPlayerTwoWins command.
     * gives the number of wins player two has.
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handleGetPlayerTwoWins(final ArrayNode output,
                                              final ObjectMapper objectMapper) {
        ObjectNode gameOutput = objectMapper.createObjectNode();
        gameOutput.put("command", "getPlayerTwoWins");
        gameOutput.put("output", Game.getPlayerTwoWins());
        output.add(gameOutput);
    }

    /***
     * Handles the GetTotalGamesPlayed command.
     * gives the total number of games played.
     * @param output - the output array
     * @param objectMapper - the object mapper
     */
    public static void handleGetTotalGamesPlayed(final ArrayNode output,
                                                 final ObjectMapper objectMapper) {
        ObjectNode gameOutput = objectMapper.createObjectNode();
        gameOutput.put("command", "getTotalGamesPlayed");
        gameOutput.put("output", Game.getTotalGamesPlayed());
        output.add(gameOutput);
    }
}
