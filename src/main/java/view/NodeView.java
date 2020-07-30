package view;

import com.codingame.game.Player;
import engine.Node;
import view.modules.NodeModule;

public class NodeView {
    private Node node;
    private NodeModule nodeModule;
    private UnitView[] unitsViews = new UnitView[2];
    private Player owner;

    public NodeView(Node node, NodeModule nodeModule) {
        this.nodeModule = nodeModule;
        this.node = node;
        node.view = this;
        for (int i = 0; i < 2; i++) {
            unitsViews[i] = new UnitView(Player.getPlayer(i), node, 0, nodeModule);
        }
        endMove();
    }

    // for spawn events
    public void updateView(int playerId) {
        unitsViews[playerId].updateAmount(node.units[playerId], true);
        //endMove();
    }

    public void startMove() {
        for (int i = 0; i < 2; i++) {
            unitsViews[i].updateAmount(node.remainingUnits[i], true);
        }
    }

    public void endMove() {
        for (int i = 0; i < 2; i++) {
            unitsViews[i].updateAmount(node.units[i], false);
        }
       Player currentOwner = null;
        if (node.ownedBy(Player.getPlayer(0))) currentOwner = Player.getPlayer(0);
        if (node.ownedBy(Player.getPlayer(1))) currentOwner = Player.getPlayer(1);
        if (currentOwner != owner) nodeModule.updateOwner(node, currentOwner);
        owner = currentOwner;
    }
}
