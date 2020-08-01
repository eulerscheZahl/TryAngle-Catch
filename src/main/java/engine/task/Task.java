package engine.task;

import com.codingame.game.Player;
import engine.Board;
import view.BoardView;

public abstract class Task {
    protected Player player;
    protected Board board;
    protected String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    protected Task(Player player, Board board) {
        this.player = player;
        this.board = board;
    }

    public Player getPlayer() {
        return player;
    }

    public static Task parseTask(Player player, Board board, String command, int league) {
        if (command.trim().equals("")) return null;
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
            player.addError(new InputError("Unknown command: " + command, true));
            return null;
        }
        if (task == null) {
            player.addError(new InputError("Unknown command: " + command, true));
            return null;
        }
        if (task.getRequiredLeague() > league) {
            player.addError(new InputError("Command not available in your league: " + command, false));
            return null;
        }
        if (!task.canApply(board)) player.addError(new InputError("Task can't be applied: " + command, false));
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

    public void visualize(BoardView view) {
        view.animateTask(this);
    }

    public abstract String getName();

    public abstract String getSerializeKey();

    public abstract String serialize();
}
