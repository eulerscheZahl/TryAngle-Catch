package engine.task;

import com.codingame.game.Player;
import engine.Board;
import engine.Node;
import engine.Triangle;
import view.BoardView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveEdgeTask extends Task {
    protected static final Pattern pattern = Pattern.compile("^\\s*(?<action>REMOVE_EDGE)\\s+(?<nodeFrom>\\d+)\\s+(?<node2>\\d+)\\s+(?<node3>\\d+)\\s+(?<nodeTo>\\d+)\\s*");

    private Node from;
    private Node to;
    private Triangle triangle;

    public RemoveEdgeTask(Player player, Board board, String command) {
        super(player, board);
        Matcher matcher = pattern.matcher(command);
        matcher.matches();
        int nodeFromId = Integer.parseInt(matcher.group("node1"));
        int node2Id = Integer.parseInt(matcher.group("node2"));
        int node3Id = Integer.parseInt(matcher.group("node3"));
        int nodeToId = Integer.parseInt(matcher.group("target"));
        from = board.nodes.get(nodeFromId);
        Node node2 = board.nodes.get(node2Id);
        Node node3 = board.nodes.get(node3Id);
        this.to = board.nodes.get(nodeToId);
        for (Triangle t : board.triangles) {
            if (t.hasNode(from) && t.hasNode(node2) && t.hasNode(node3)) this.triangle = t;
        }
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
    public boolean canApply(Board board) {
        return triangle != null && triangle.canUse(player, 1) && from.neighbors.contains(to) && !triangle.hasNode(to);
    }

    @Override
    public void apply(Board board) {
        triangle.use(1);
        board.disconnect(from, to);
    }

    @Override
    public void visualize(BoardView view) {

    }
}