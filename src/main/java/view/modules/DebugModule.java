package view.modules;

import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.Module;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import engine.task.Task;
import engine.task.debug.DebugTask;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class DebugModule implements Module {
    private GameManager<AbstractPlayer> gameManager;
    private List<DebugTask> tasks;
    private int[][] createdEntities = new int[2][5];
    private static final int MAX_ENTITIES_PER_PLAYER = 50;

    @Inject
    DebugModule(GameManager<AbstractPlayer> gameManager) {
        this.gameManager = gameManager;
        gameManager.registerModule(this);
        tasks = new ArrayList<>();
    }

    @Override
    public void onGameInit() {
        sendFrameData();
    }

    @Override
    public void onAfterGameTurn() {
        sendFrameData();
    }

    @Override
    public void onAfterOnEnd() {
    }

    private void sendFrameData() {
        if (tasks.size() == 0) return;

        int[][] free = new int[2][];
        free[0] = createdEntities[0].clone();
        free[1] = createdEntities[1].clone();

        String[] playerTasks = {"", ""};
        for (DebugTask task : tasks) {
            if (free[task.getPlayer().getIndex()][task.getDebugType()] > 0) {
                free[task.getPlayer().getIndex()][task.getDebugType()]--;
            } else if (createdEntities[task.getPlayer().getIndex()][0] < MAX_ENTITIES_PER_PLAYER) {
                createdEntities[task.getPlayer().getIndex()][0]++;
                createdEntities[task.getPlayer().getIndex()][task.getDebugType()]++;
            } else { // player can't create more entities
                continue;
            }
            playerTasks[task.getPlayer().getIndex()] += ";" + task.getSerializeKey() + task.serialize();
        }

        if (playerTasks[0].length() == 0) playerTasks[0] = ";";
        if (playerTasks[1].length() == 0) playerTasks[1] = ";";
        String data = playerTasks[0].substring(1) + ";;" + playerTasks[1].substring(1);
        gameManager.setViewData("debug", data);
        tasks.clear();
    }

    public void clear() {
        gameManager.setViewData("debug", ";;");
    }

    public void registerTask(DebugTask task) {
        tasks.add(task);
    }
}
