package engine.task;

import com.codingame.game.Player;
import engine.Board;
import engine.Node;
import engine.Triangle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveEdgeTask extends Task {
    protected static final Pattern pattern = Pattern.compile("^\\s*(?<action>REMOVE_PATH)\\s+(?<nodeFrom>\\d+)\\s+(?<node2>\\d+)\\s+(?<node3>\\d+)\\s+(?<nodeTo>\\d+)\\s*$", Pattern.CASE_INSENSITIVE);

    private Node from;
    private Node to;
    private Triangle triangle;

    public RemoveEdgeTask(Player player, Board board, String command) {
        super(player, board);
        Matcher matcher = pattern.matcher(command);
        matcher.matches();
        this.from = getNode(board, matcher.group("nodeFrom"));
        Node node2 = getNode(board, matcher.group("node2"));
        Node node3 = getNode(board, matcher.group("node3"));
        this.to = getNode(board, matcher.group("nodeTo"));
        this.triangle = getTriangle(board, from, node2, node3);
        if (!hasFailedParsing() && !triangle.hasNeighbor(to)) addParsingError("House " + to.getId() + " isn't connected to given triangle", InputError.NOT_NEAR_TRIANGLE, false);
    }

    @Override
    public boolean allowMultiplePerFrame() {
        return false;
    }

    @Override
    public int getTaskPriority() {
        return 4;
    }

    @Override
    public int getRequiredLeague() {
        return 3;
    }

    @Override
    public boolean canApply(Board board, boolean strict) {
        if (!strict) return true;
        return !hasFailedParsing() && triangle.canUse(player, 1) && from.neighbors.contains(to) && !triangle.hasNode(to);
    }

    @Override
    public void apply(Board board) {
        triangle.use(1);
        board.disconnect(from, to);
    }

    @Override
    public String getName() {
        return "REMOVE_PATH";
    }

    @Override
    public String getSerializeKey() {
        return "R";
    }

    @Override
    public String serialize() {
        String result = "" + alphabet.charAt(from.getId());
        if (triangle.getNode1() != from) result += alphabet.charAt(triangle.getNode1().getId());
        if (triangle.getNode2() != from) result += alphabet.charAt(triangle.getNode2().getId());
        if (triangle.getNode3() != from) result += alphabet.charAt(triangle.getNode3().getId());
        result += alphabet.charAt(to.getId());
        return result;
    }
}