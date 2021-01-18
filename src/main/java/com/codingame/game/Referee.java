package com.codingame.game;
import java.util.List;
import java.util.Random;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;
import engine.Board;
import engine.task.InputError;
import engine.task.TaskManager;
import view.modules.DebugModule;
import view.modules.NodeModule;
import view.modules.TaskModule;
import view.modules.TinyToggleModule;

public class Referee extends AbstractReferee {
    @Inject
    private MultiplayerGameManager<Player> gameManager;
    @Inject
    private GraphicEntityModule graphicEntityModule;
    @Inject
    private TooltipModule tooltipModule;
    @Inject
    private EndScreenModule endScreenModule;
    @Inject
    private TinyToggleModule toggleModule;
    @Inject
    private NodeModule nodeModule;
    @Inject
    private TaskModule taskModule;
    @Inject
    private DebugModule debugModule;


    private Board board;
    private TaskManager taskManager;

    @Override
    public void init() {
        Random random = new Random(gameManager.getSeed());
        Player.registerPlayer(gameManager.getPlayer(0));
        Player.registerPlayer(gameManager.getPlayer(1));

        board = new Board(gameManager.getGameParameters(), random, graphicEntityModule, tooltipModule, toggleModule, nodeModule, taskModule, debugModule);
        taskManager = new TaskManager();
        gameManager.setMaxTurns(200);
    }

    @Override
    public void gameTurn(int turn) {
        if (!taskManager.hasTasks()) {
            if (board.finalizeTurn()) {
                gameManager.setMaxTurns(gameManager.getMaxTurns() + 1);
                return;
            }
            for (Player player : gameManager.getActivePlayers()) {
                if (player.getUnits() == 0 && player.getTriangles() == 0) {
                    killPlayer(player, "no units or triangles left");
                    continue;
                }
                if (player.getOpponent().getTriangles() >= board.triangles.size() * 0.8) {
                    killPlayer(player, "is dominated");
                    continue;
                }
                player.sendInputLine(board.getInput(turn == 1, player, gameManager.getLeagueLevel() >= 3));
                player.execute();
            }

            for (Player player : gameManager.getActivePlayers()) {
                try {
                    List<String> outputs = player.getOutputs();
                    taskManager.parseTasks(player, board, outputs.get(0), gameManager.getLeagueLevel());
                } catch (TimeoutException e) {
                    killPlayer(player, "timeout");
                }

                for (InputError error : player.popErrors()) {
                    if (error.isCritical()) {
                        killPlayer(player, error.getMessage());
                    } else gameManager.addToGameSummary(player.getNicknameToken() + ": " + error.getMessage());
                }
            }
            board.applyDebug(taskManager);
        } else {
            gameManager.setMaxTurns(gameManager.getMaxTurns() + 1);
        }

        board.applyActions(taskManager);
    }

    private void killPlayer(Player player, String message) {
        if (!player.isActive()) return;
        player.deactivate(String.format("$%d %s!", player.getIndex(), message));
        player.setScore(-1);
        gameManager.endGame();
    }

    @Override
    public void onEnd() {
        int[] scores = gameManager.getPlayers().stream().mapToInt(p -> p.getScore()).toArray();
        String[] texts = {String.valueOf(scores[0]), String.valueOf(scores[1])};
        endScreenModule.setScores(scores, texts);
    }
}
