package view.modules;

import java.util.ArrayList;
import java.util.List;

import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.Module;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import engine.task.*;

@Singleton
public class TaskModule implements Module {
    private GameManager<AbstractPlayer> gameManager;
    private List<Task> tasks;

    @Inject
    TaskModule(GameManager<AbstractPlayer> gameManager) {
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

        String[] playerTasks = {"", ""};

        for (Task task : tasks) {
            playerTasks[task.getPlayer().getIndex()] += task.serialize();
        }

        String data = tasks.get(0).getSerializeKey() + playerTasks[0] + ";" + playerTasks[1];
        gameManager.setViewData("tasks", data);
        tasks.clear();
    }

    public void registerTask(Task task) {
        tasks.add(task);
    }
}
