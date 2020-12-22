package engine.task;

import com.codingame.game.Player;
import engine.Board;

import java.util.ArrayList;
import java.util.Comparator;

public class TaskManager {
    private ArrayList<Task> tasks = new ArrayList<>();
    private Board board;

    public void parseTasks(Player player, Board board, String command, int league) {
        player.setMessage("");
        this.board = board;
        for (String comm : command.split(";")) {
            if (comm.toUpperCase().startsWith("MSG ")) {
                player.setMessage(comm.substring(4).trim());
                continue;
            }
            Task task = Task.parseTask(player, board, comm, league);
            if (task != null) tasks.add(task);
        }
    }

    public boolean hasTasks() {
        return peekTasks().size() > 0;
    }

    public ArrayList<ArrayList<Task>> popTasks() {
        ArrayList<Task> pop = peekTasks();
        for (Task task : pop) tasks.remove(task);
        ArrayList<ArrayList<Task>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        result.add(new ArrayList<>());
        for (Task task : pop) result.get(task.player.getIndex()).add(task);
        return result;
    }

    private ArrayList<Task> peekTasks() {
        for (int i = tasks.size() - 1; i >= 0; i--) {
            if (!tasks.get(i).canApply(board, true)) tasks.remove(i);
        }

        ArrayList<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (result.size() > 0 && result.get(0).getTaskPriority() > task.getTaskPriority()) result.clear();
            if ((result.size() == 0 || task.getTaskPriority() == result.get(0).getTaskPriority()))
                result.add(task);
        }
        result.sort(Comparator.comparingInt(Task::getTaskCost));

        return result;
    }

    public void returnTasks(ArrayList<ArrayList<Task>> tasks) {
        for (ArrayList<Task> playerTasks : tasks) this.tasks.addAll(playerTasks);
    }
}
