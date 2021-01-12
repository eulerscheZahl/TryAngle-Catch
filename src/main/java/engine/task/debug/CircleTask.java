package engine.task.debug;

import com.codingame.game.Player;
import engine.Board;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CircleTask extends DebugTask {
    public static final Pattern pattern = Pattern.compile("^\\s*(?<action>CIRCLE)\\s+(?<x>-?\\d+)\\s+(?<y>-?\\d+)\\s+(?<r>-?\\d+)\\s*$", Pattern.CASE_INSENSITIVE);

    private int x;
    private int y;
    private int r;

    public CircleTask(Player player, Board board, String command) {
        super(player, board);
        Matcher matcher = pattern.matcher(command);
        matcher.matches();
        x = Integer.parseInt(matcher.group("x"));
        y = Integer.parseInt(matcher.group("y"));
        r = Math.abs(Integer.parseInt(matcher.group("r")));
    }

    @Override
    public int getDebugType() {
        return 3;
    }

    @Override
    public String getSerializeKey() {
        return "C";
    }

    @Override
    public String serialize() {
        return x + " " + y + " " + r;
    }
}
