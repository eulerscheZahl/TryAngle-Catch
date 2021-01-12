package engine.task.debug;

import com.codingame.game.Player;
import engine.Board;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextTask extends DebugTask {
    public static final Pattern pattern = Pattern.compile("^\\s*(?<action>TEXT)\\s+(?<x>-?\\d+)\\s+(?<y>-?\\d+)\\s+(?<text>.+)\\s*$", Pattern.CASE_INSENSITIVE);
    private static final int MAX_MESSAGE_LENGTH = 20;

    private int x;
    private int y;
    private String text;

    public TextTask(Player player, Board board, String command) {
        super(player, board);
        Matcher matcher = pattern.matcher(command);
        matcher.matches();
        x = Integer.parseInt(matcher.group("x"));
        y = Integer.parseInt(matcher.group("y"));
        text = matcher.group("text");
        if (text.length() > MAX_MESSAGE_LENGTH) text = text.substring(0, MAX_MESSAGE_LENGTH);
    }

    @Override
    public int getDebugType() {
        return 4;
    }

    @Override
    public String getSerializeKey() {
        return "T";
    }

    @Override
    public String serialize() {
        return x + " " + y + " " + text;
    }
}
