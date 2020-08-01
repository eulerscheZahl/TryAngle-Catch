package engine.task;

import com.codingame.game.Player;
import engine.Board;
import engine.Node;

public class SurroundTask extends Task {
    private int amount;
    private Node target;

    public SurroundTask(Node node, Player player, int amount) {
        super(player, null);
        this.target = node;
        this.amount = amount;
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
        return 0;
    }

    @Override
    public boolean canApply(Board board) {
        return false;
    }

    @Override
    public void apply(Board board) {
    }

    @Override
    public String getName() {
        return "SURROUND";
    }

    @Override
    public String getSerializeKey() {
        return "X";
    }

    @Override
    public String serialize() {
        String result = "" + alphabet.charAt(target.getId());
        if (amount != 1) result += amount;
        return result;
    }
}