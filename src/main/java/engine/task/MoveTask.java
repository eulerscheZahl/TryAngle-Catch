package engine.task;

import com.codingame.game.Player;
import engine.Board;
import engine.Node;
import view.BoardView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoveTask extends Task {
    protected static final Pattern pattern = Pattern.compile("^\\s*(?<action>MOVE)\\s+(?<from>\\d+)\\s+(?<to>\\d+)\\s+(?<amount>\\d+)\\s*$");

    private Node nodeFrom;
    private Node nodeTo;
    private int amount;

    public MoveTask(Player player, Board board, String command) {
        super(player, board);
        Matcher matcher = pattern.matcher(command);
        matcher.matches();
        int fromId = Integer.parseInt(matcher.group("from"));
        int toId = Integer.parseInt(matcher.group("to"));
        amount = Integer.parseInt(matcher.group("amount"));
        nodeFrom = this.board.nodes.get(fromId);
        nodeTo = this.board.nodes.get(toId);
    }

    public MoveTask(Player player, Node from, Node to, int count) {
        super(player, null);
        this.nodeFrom = from;
        this.nodeTo = to;
        this.amount = count;
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
    public boolean canApply(Board board) {
        return nodeFrom.canMoveTo(player.getIndex(), nodeTo);
    }

    @Override
    public void apply(Board board) {
        nodeFrom.moveTo(player.getIndex(), nodeTo, amount);
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
        return "" + alphabet.charAt(nodeFrom.getId()) + alphabet.charAt(nodeTo.getId());
    }
}
