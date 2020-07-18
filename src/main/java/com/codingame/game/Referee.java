package com.codingame.game;
import java.util.List;
import java.util.Random;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.toggle.ToggleModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;
import engine.Board;
import engine.task.TaskManager;

public class Referee extends AbstractReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
    @Inject private TooltipModule tooltipModule;
    @Inject private EndScreenModule endScreenModule;
    @Inject private ToggleModule toggleModule;

    private Board board;
    private TaskManager taskManager;
    @Override
    public void init() {
        Random random = new Random(gameManager.getSeed());
        Player.registerPlayer(gameManager.getPlayer(0));
        Player.registerPlayer(gameManager.getPlayer(1));

        board = new Board(random, graphicEntityModule, tooltipModule, toggleModule);
        taskManager = new TaskManager();
        gameManager.setMaxTurns(50);
    }

    @Override
    public void gameTurn(int turn) {
        if (!taskManager.hasTasks()) {
            if (board.finalizeTurn()){
                gameManager.setMaxTurns(gameManager.getMaxTurns() + 1);
                return;
            }
            for (Player player : gameManager.getActivePlayers()) {
                player.sendInputLine(board.getInput(turn == 1, player));
                player.execute();
            }

            for (Player player : gameManager.getActivePlayers()) {
                try {
                    List<String> outputs = player.getOutputs();
                    taskManager.parseTasks(player, board, outputs.get(0), gameManager.getLeagueLevel());
                } catch (TimeoutException e) {
                    player.deactivate(String.format("$%d timeout!", player.getIndex()));
                    player.setScore(-1);
                    gameManager.endGame();
                }
            }
        } else {
            gameManager.setMaxTurns(gameManager.getMaxTurns() + 1);
        }

        board.applyActions(taskManager);
    }

    @Override
    public void onEnd() {
        int[] scores = gameManager.getPlayers().stream().mapToInt(p -> p.getScore()).toArray();
        String[] texts = { String.valueOf(scores[0]), String.valueOf(scores[1]) };
        endScreenModule.setScores(scores, texts);
    }
}
