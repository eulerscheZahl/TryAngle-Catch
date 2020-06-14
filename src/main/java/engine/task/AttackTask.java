package engine.task;

import com.codingame.game.Player;
import engine.Board;
import engine.Node;
import engine.Triangle;
import view.BoardView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttackTask extends Task {
    protected static final Pattern pattern = Pattern.compile("^\\s*(?<action>ATTACK)\\s+(?<node1>\\d+)\\s+(?<node2>\\d+)\\s+(?<node3>\\d+)\\s+(?<target>\\d+)\\s*");

    private Node target;
    private Triangle triangle;

    public AttackTask(Player player, Board board, String command) {
        super(player, board);
        Matcher matcher = pattern.matcher(command);
        matcher.matches();
        int node1Id = Integer.parseInt(matcher.group("node1"));
        int node2Id = Integer.parseInt(matcher.group("node2"));
        int node3Id = Integer.parseInt(matcher.group("node3"));
        int targetId = Integer.parseInt(matcher.group("target"));
        Node node1 = board.nodes.get(node1Id);
        Node node2 = board.nodes.get(node2Id);
        Node node3 = board.nodes.get(node3Id);
        this.target = board.nodes.get(targetId);
        for (Triangle t : board.triangles) {
            if (t.hasNode(node1) && t.hasNode(node2) && t.hasNode(node3)) this.triangle = t;
        }
    }

    @Override
    public boolean allowMultiplePerFrame() {
        return false;
    }

    @Override
    public int getTaskPriority() {
        return 0;
    }

    @Override
    public boolean canApply(Board board) {
        return triangle != null && triangle.canUse(player, 1) && triangle.hasNeighbor(target);
    }

    @Override
    public void apply(Board board) {
        triangle.use(1);
        target.units[player.getOpponent().getIndex()] = 0;
        target.updateView(player.getOpponent().getIndex());
    }

    @Override
    public void visualize(BoardView view) {

    }
}