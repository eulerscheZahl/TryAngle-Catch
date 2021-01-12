package engine.task.debug;

import com.codingame.game.Player;
import engine.Board;
import engine.Node;
import engine.task.Task;
import view.BoardView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineTask extends DebugTask {
    public static final Pattern pattern = Pattern.compile("^\\s*(?<action>LINE)\\s+(?<x1>-?\\d+)\\s+(?<y1>-?\\d+)\\s+(?<x2>-?\\d+)\\s+(?<y2>-?\\d+)\\s*$", Pattern.CASE_INSENSITIVE);

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public LineTask(Player player, Board board, String command) {
        super(player, board);
        Matcher matcher = pattern.matcher(command);
        matcher.matches();
        x1 = Integer.parseInt(matcher.group("x1"));
        y1 = Integer.parseInt(matcher.group("y1"));
        x2 = Integer.parseInt(matcher.group("x2"));
        y2 = Integer.parseInt(matcher.group("y2"));
    }

    @Override
    public int getDebugType() {
        return 1;
    }

    @Override
    public String getSerializeKey() {
        return "L";
    }

    @Override
    public String serialize() {
        return x1 + " " + y1 + " " + x2 + " " + y2;
    }
}
