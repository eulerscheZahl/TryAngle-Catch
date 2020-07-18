package engine.task;

import com.codingame.game.Player;
import engine.Board;
import view.BoardView;

import java.util.regex.Pattern;

public abstract class Task {
    protected Player player;
    protected Board board;

    protected Task(Player player, Board board) {
        this.player = player;
        this.board = board;
    }

    public Player getPlayer() {
        return player;
    }

    public static Task parseTask(Player player, Board board, String command, int league) {
        Task task = null;
        try {
            if (MoveTask.pattern.matcher(command).matches())
                task = new MoveTask(player, board, command);
            if (SpawnTask.pattern.matcher(command).matches())
                task = new SpawnTask(player, board, command);
            if (AttackTask.pattern.matcher(command).matches())
                task = new AttackTask(player, board, command);
            if (AddEdgeTask.pattern.matcher(command).matches())
                task = new AddEdgeTask(player, board, command);
            if (RemoveEdgeTask.pattern.matcher(command).matches())
                task = new RemoveEdgeTask(player, board, command);
        } catch (Exception ex) {
            return null;
        }
        if (task != null && task.getRequiredLeague() > league) return null;
        return task;
    }

    public abstract boolean allowMultiplePerFrame();

    public abstract int getTaskPriority();

    public abstract int getRequiredLeague();

    public int getTaskCost() {
        return 0;
    }

    public abstract boolean canApply(Board board);

    public abstract void apply(Board board);

    public abstract void visualize(BoardView view);
}
