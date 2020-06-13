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

    public static Task parseTask(Player player, Board board, String command) {
        try {
            if (MoveTask.pattern.matcher(command).matches())
                return new MoveTask(player, board, command);
            if (SpawnTask.pattern.matcher(command).matches())
                return new SpawnTask(player, board, command);
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    public abstract boolean allowMultiplePerFrame();

    public abstract int getTaskPriority();

    public abstract boolean canApply(Board board);

    public abstract void apply(Board board);

    public abstract void visualize(BoardView view);
}
