package engine.task;

import com.codingame.game.Player;
import engine.Board;
import engine.Node;
import engine.Triangle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpawnTask extends Task {
    protected static final Pattern pattern = Pattern.compile("^\\s*(?<action>SPAWN)\\s+(?<spawn>\\d+)\\s+(?<node2>\\d+)\\s+(?<node3>\\d+)\\s*$", Pattern.CASE_INSENSITIVE);

    private Node nodeSpawn;
    private Triangle triangle;

    public SpawnTask(Player player, Board board, String command) {
        super(player, board);
        Matcher matcher = pattern.matcher(command);
        matcher.matches();
        this.nodeSpawn = getNode(board, matcher.group("spawn"));
        Node node2 = getNode(board, matcher.group("node2"));
        Node node3 = getNode(board, matcher.group("node3"));
        this.triangle = getTriangle(board, nodeSpawn, node2, node3);
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
        return !hasFailedParsing() && triangle.canUse(player, 0);
    }

    @Override
    public void apply(Board board) {
        triangle.use(0);
        nodeSpawn.units[player.getIndex()]++;
        nodeSpawn.updateView(player.getIndex(), false);
    }

    @Override
    public String getName() {
        return "SPAWN";
    }

    @Override
    public String getSerializeKey() {
        return "S";
    }

    @Override
    public String serialize() {
        String result = "" + alphabet.charAt(nodeSpawn.getId());
        if (triangle.getNode1() != nodeSpawn) result += alphabet.charAt(triangle.getNode1().getId());
        if (triangle.getNode2() != nodeSpawn) result += alphabet.charAt(triangle.getNode2().getId());
        if (triangle.getNode3() != nodeSpawn) result += alphabet.charAt(triangle.getNode3().getId());
        return result;
    }
}
