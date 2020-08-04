package engine.task;

import com.codingame.game.Player;
import engine.Board;
import engine.Node;
import engine.Triangle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttackTask extends Task {
    protected static final Pattern pattern = Pattern.compile("^\\s*(?<action>ATTACK)\\s+(?<node1>\\d+)\\s+(?<node2>\\d+)\\s+(?<node3>\\d+)\\s+(?<target>\\d+)\\s*$", Pattern.CASE_INSENSITIVE);

    private Node target;
    private Triangle triangle;
    private int amount;

    public AttackTask(Player player, Board board, String command) {
        super(player, board);
        Matcher matcher = pattern.matcher(command);
        matcher.matches();
        Node node1 = getNode(board, matcher.group("node1"));
        Node node2 = getNode(board, matcher.group("node2"));
        Node node3 = getNode(board, matcher.group("node3"));
        this.target = getNode(board, matcher.group("target"));
        this.triangle = getTriangle(board, node1, node2, node3);
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
    public int getRequiredLeague() {
        return 2;
    }

    @Override
    public boolean canApply(Board board) {
        return triangle != null && triangle.canUse(player, 1) && triangle.hasNeighbor(target);
    }

    @Override
    public void apply(Board board) {
        triangle.use(1);
        amount = target.units[player.getOpponent().getIndex()];
        target.units[player.getOpponent().getIndex()] = 0;
        target.remainingUnits[player.getOpponent().getIndex()] = 0;
        target.updateView(player.getOpponent().getIndex(), true);
    }

    @Override
    public String getName() {
        return "ATTACK";
    }

    @Override
    public String getSerializeKey() {
        return "A";
    }

    @Override
    public String serialize() {
        String result = "";
        result += alphabet.charAt(triangle.getNode1().getId());
        result += alphabet.charAt(triangle.getNode2().getId());
        result += alphabet.charAt(triangle.getNode3().getId());
        result += alphabet.charAt(target.getId());
        if (amount != 1) result += amount;
        return result;
    }
}