package engine.task;

import com.codingame.game.Player;
import engine.Board;
import engine.Node;
import engine.Triangle;
import view.BoardView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpawnTask extends Task {
    protected static final Pattern pattern = Pattern.compile("^\\s*(?<action>SPAWN)\\s+(?<spawn>\\d+)\\s+(?<node2>\\d+)\\s+(?<node3>\\d+)\\s*");

    private Node nodeSpawn;
    private Triangle triangle;

    public SpawnTask(Player player, Board board, String command) {
        super(player, board);
        Matcher matcher = pattern.matcher(command);
        matcher.matches();
        int spawnId = Integer.parseInt(matcher.group("spawn"));
        int node2Id = Integer.parseInt(matcher.group("node2"));
        int node3Id = Integer.parseInt(matcher.group("node3"));
        nodeSpawn = board.nodes.get(spawnId);
        Node node2 = board.nodes.get(node2Id);
        Node node3 = board.nodes.get(node3Id);
        for (Triangle t : board.triangles) {
            if (t.hasNode(nodeSpawn) && t.hasNode(node2) && t.hasNode(node3)) triangle = t;
        }
    }

    @Override
    public boolean allowMultiplePerFrame() {
        return true;
    }

    @Override
    public int getTaskPriority() {
        return 2;
    }

    @Override
    public int getRequiredLeague() {
        return 1;
    }

    @Override
    public boolean canApply(Board board) {
        return triangle != null && triangle.canUse(player, 0);
    }

    @Override
    public void apply(Board board) {
        triangle.use(0);
        nodeSpawn.units[player.getIndex()]++;
        nodeSpawn.updateView(player.getIndex());
    }

    @Override
    public void visualize(BoardView view) {

    }
}
