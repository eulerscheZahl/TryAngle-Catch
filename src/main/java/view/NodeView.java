package view;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import engine.Node;

public class NodeView {
    private Node node;
    private GraphicEntityModule graphics;
    private UnitView[] unitsViews = new UnitView[2];
    private Circle mainCircle;

    public NodeView(Node node, GraphicEntityModule graphics, TooltipModule tooltips) {
        this.graphics = graphics;
        this.node = node;
        node.view = this;
        for (Node n : node.neighbors) {
            if (n.getId() < node.getId()) continue;
            graphics.createLine().setX(n.getX()).setY(n.getY()).setX2(node.getX()).setY2(node.getY())
                    .setLineWidth(5).setFillColor(0);
        }
        mainCircle = graphics.createCircle().setX(node.getX()).setY(node.getY()).setRadius(35).setFillColor(0).setLineWidth(10).setLineColor(0);
        graphics.createText().setX(node.getX()).setY(node.getY()).setAnchor(0.5).setText(String.valueOf(node.getId()))
                .setFillColor(0xffffff);

        for (int i = 0; i < 2; i++) {
            unitsViews[i] = new UnitView(Player.getPlayer(i), node, 0, graphics);
        }
        endMove();
    }

    public void updateView(int id) {
        unitsViews[id].updateAmount(node.units[id]);
    }

    public void startMove() {
        for (int i = 0; i < 2; i++) {
            unitsViews[i].commitAmount(graphics, node.remainingUnits[i]);
        }
    }

    public void endMove() {
        for (int i = 0; i < 2; i++) {
            unitsViews[i].updateAmount(node.units[i]);
        }
        int lineColor = 0;
        if (node.ownedBy(Player.getPlayer(0))) lineColor = Player.getColor(0);
        if (node.ownedBy(Player.getPlayer(1))) lineColor = Player.getColor(1);
        mainCircle.setLineColor(lineColor);
    }
}
