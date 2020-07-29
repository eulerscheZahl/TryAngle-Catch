package engine;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import view.TriangleView;

public class Triangle {
    private Node node1;
    private Node node2;
    private Node node3;
    private Player owner;
    private boolean[] canCapture = new boolean[]{true, true};
    private TriangleView view;

    public Triangle(Node node1, Node node2, Node node3, GraphicEntityModule graphicEntityModule, TooltipModule tooltipModule) {
        this.node1 = node1;
        this.node2 = node2;
        this.node3 = node3;
        view = new TriangleView(this, graphicEntityModule, tooltipModule);
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    public Node getNode3() {
        return node3;
    }

    public Player getOwner() {
        return owner;
    }

    @Override
    public int hashCode() {
        return 1000000 * node1.getId() + 1000 * node2.getId() + node3.getId();
    }

    @Override
    public boolean equals(Object other) {
        Triangle triangle = (Triangle) other;
        return this.node1 == triangle.node1 &&
                this.node2 == triangle.node2 &&
                this.node3 == triangle.node3;
    }

    public boolean canCapture(Player player) {
        return canCapture[player.getIndex()];
    }

    public boolean capture() {
        Player oldOwner = owner;
        if (canCapture[0] && node1.ownedBy(Player.getPlayer(0)) && node2.ownedBy(Player.getPlayer(0)) && node3.ownedBy(Player.getPlayer(0)))
            doCapture(0);
        if (canCapture[1] && node1.ownedBy(Player.getPlayer(1)) && node2.ownedBy(Player.getPlayer(1)) && node3.ownedBy(Player.getPlayer(1)))
            doCapture(1);
        return owner != oldOwner;
    }

    public void use(int units) {
        node1.units[owner.getIndex()] -= units;
        node2.units[owner.getIndex()] -= units;
        node3.units[owner.getIndex()] -= units;
        node1.remainingUnits[owner.getIndex()] -= units;
        node2.remainingUnits[owner.getIndex()] -= units;
        node3.remainingUnits[owner.getIndex()] -= units;
        canCapture[owner.getIndex()] = false;
        owner = null;
        //node1.updateView(owner.getIndex());
        //node2.updateView(owner.getIndex());
        //node3.updateView(owner.getIndex());
        view.update();
    }

    public boolean canUse(Player player, int units) {
        return owner == player &&
                node1.remainingUnits[player.getIndex()] >= units &&
                node2.remainingUnits[player.getIndex()] >= units &&
                node3.remainingUnits[player.getIndex()] >= units;
    }

    private void doCapture(int id) {
        this.owner = Player.getPlayer(id);
        view.update();
    }

    public String getInput(Player player) {
        int ownerId = -1;
        if (owner == player) ownerId = 0;
        else if (owner != null) ownerId = 1;
        return node1.getId() + " " + node2.getId() + " " + node3.getId() + " " + ownerId + " " +
                (canCapture[player.getIndex()] ? 1 : 0) + " " + (canCapture[(player.getIndex() + 1) % 2] ? 1 : 0);
    }

    public boolean hasNode(Node node) {
        return node1 == node || node2 == node || node3 == node;
    }

    public void updateAllowedCaptures() {
        for (int i = 0; i < 2; i++) {
            if (owner != null) canCapture[i] = owner.getIndex() != i;
            else canCapture[i] |= node1.units[i] == 0 && node2.units[i] == 0 && node3.units[i] == 0;
        }
    }

    public boolean hasNeighbor(Node node) {
        return node1.neighbors.contains(node) ||
                node2.neighbors.contains(node) ||
                node3.neighbors.contains(node);
    }

    public void delete() {
        view.hide();
    }

    public void updateOwnerView() {
        view.update();
    }
}
