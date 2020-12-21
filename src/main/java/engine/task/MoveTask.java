package engine.task;

import com.codingame.game.Player;
import engine.Board;
import engine.Node;
import view.BoardView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoveTask extends Task {
    protected static final Pattern pattern = Pattern.compile("^\\s*(?<action>MOVE)\\s+(?<from>\\d+)\\s+(?<to>\\d+)\\s+(?<amount>\\d+)\\s*$", Pattern.CASE_INSENSITIVE);

    private Node nodeFrom;
    private Node nodeTo;
    private int amount;

    public MoveTask(Player player, Board board, String command) {
        super(player, board);
        Matcher matcher = pattern.matcher(command);
        matcher.matches();
        nodeFrom = getNode(board, matcher.group("from"));
        nodeTo = getNode(board, matcher.group("to"));
        amount = Integer.parseInt(matcher.group("amount"));
        if (amount <= 0) addParsingError("Amount of units to move be positive", false);
    }

    public MoveTask(Player player, Node from, Node to, int amount) {
        super(player, null);
        this.nodeFrom = from;
        this.nodeTo = to;
        this.amount = amount;
    }

    @Override
    public boolean allowMultiplePerFrame() {
        return true;
    }

    @Override
    public int getTaskPriority() {
        return 1;
    }

    @Override
    public int getRequiredLeague() {
        return 1;
    }

    @Override
    public boolean canApply(Board board, boolean strict) {
        if (!strict) return true;
        return !hasFailedParsing() && nodeFrom.canMoveTo(player.getIndex(), nodeTo);
    }

    @Override
    public void apply(Board board) {
        amount = nodeFrom.getMoveAmount(player.getIndex(), amount);
        nodeTo = nodeFrom.moveTo(player.getIndex(), nodeTo, amount);
    }

    @Override
    public void visualize(BoardView view) {
        // use own serialization and don't call super.visualize()
        view.cacheMove(player, nodeFrom, nodeTo, amount);
    }

    @Override
    public String getName() {
        return "MOVE";
    }

    @Override
    public String getSerializeKey() {
        return "M";
    }

    @Override
    public String serialize() {
        String result = "" + alphabet.charAt(nodeFrom.getId()) + alphabet.charAt(nodeTo.getId());
        if (amount != 1) result += amount;
        return result;
    }
}
