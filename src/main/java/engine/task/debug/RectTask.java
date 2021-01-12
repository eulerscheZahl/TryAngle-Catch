package engine.task.debug;

import com.codingame.game.Player;
import engine.Board;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RectTask extends DebugTask {
    public static final Pattern pattern = Pattern.compile("^\\s*(?<action>RECT)\\s+(?<x>-?\\d+)\\s+(?<y>-?\\d+)\\s+(?<w>-?\\d+)\\s+(?<h>-?\\d+)\\s*$", Pattern.CASE_INSENSITIVE);

    private int x;
    private int y;
    private int w;
    private int h;

    public RectTask(Player player, Board board, String command) {
        super(player, board);
        Matcher matcher = pattern.matcher(command);
        matcher.matches();
        x = Integer.parseInt(matcher.group("x"));
        y = Integer.parseInt(matcher.group("y"));
        w = Integer.parseInt(matcher.group("w"));
        h = Integer.parseInt(matcher.group("h"));
    }

    @Override
    public int getDebugType() {
        return 2;
    }

    @Override
    public String getSerializeKey() {
        return "R";
    }

    @Override
    public String serialize() {
        return x + " " + y + " " + w + " " + h;
    }
}
