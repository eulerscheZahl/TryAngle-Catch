package engine.task.debug;

import com.codingame.game.Player;
import engine.Board;
import engine.task.Task;
import view.BoardView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DebugTask extends Task {

    protected DebugTask(Player player, Board board) {
        super(player, board);
    }

    public abstract int getDebugType();

    @Override
    public boolean allowMultiplePerFrame() {
        return true;
    }

    @Override
    public int getTaskPriority() {
        return -1;
    }

    @Override
    public int getRequiredLeague() {
        return 1;
    }

    @Override
    public boolean canApply(Board board, boolean strict) {
        return true;
    }

    @Override
    public void apply(Board board) { }

    @Override
    public void visualize(BoardView view) {
        // use own serialization and don't call super.visualize()
        view.cacheDebug(this);
    }

    @Override
    public String getName() {
        return "DEBUG";
    }
 }
