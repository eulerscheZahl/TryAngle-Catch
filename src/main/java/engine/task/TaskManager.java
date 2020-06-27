package engine.task;

import com.codingame.game.Player;
import engine.Board;
import io.undertow.server.handlers.accesslog.JBossLoggingAccessLogReceiver;

import java.util.ArrayList;
import java.util.Comparator;

public class TaskManager {
    private ArrayList<Task> tasks = new ArrayList<>();
    private Board board;

    public void parseTasks(Player player, Board board, String command) {
        this.board = board;
        for (String comm : command.split(";")) {
            Task task = Task.parseTask(player, board, comm);
            if (task != null) tasks.add(task);
        }
    }

    public boolean hasTasks() {
        return peekTasks().size() > 0;
    }

    public ArrayList<Task> popTasks() {
        ArrayList<Task> result = peekTasks();
        for (Task task : result) tasks.remove(task);
        return result;
    }

    private ArrayList<Task> peekTasks() {
        for (int i = tasks.size() -1; i >= 0; i--) {
            if (!tasks.get(i).canApply(board)) tasks.remove(i);
        }

        ArrayList<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (result.size() > 0 && result.get(0).getTaskPriority() > task.getTaskPriority()) result.clear();
            if ((result.size() == 0 || task.getTaskPriority() == result.get(0).getTaskPriority()) &&
                    (task.allowMultiplePerFrame() || !result.stream().anyMatch(t -> t.player == task.player)))
                result.add(task);
        }
        result.sort(Comparator.comparingInt(Task::getTaskCost));

        return result;
    }
}
