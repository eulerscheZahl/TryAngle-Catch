package view;

import com.codingame.game.Player;
import engine.Node;
import view.modules.NodeModule;

public class UnitView {
    private Node node;
    private Player player;
    private int amount;
    private NodeModule nodeModule;

    public UnitView(Player player, Node node, int amount, NodeModule nodeModule) {
        this.node = node;
        this.player = player;
        this.amount = amount;
        this.nodeModule = nodeModule;
    }

    public Player getPlayer() {
        return player;
    }

    public Node getNode() {
        return node;
    }

    public void updateAmount(int unit, boolean startTurn) {
        if (unit == amount) return;
        amount = unit;
        nodeModule.updateUnits(player, startTurn, node, amount);
    }
}
