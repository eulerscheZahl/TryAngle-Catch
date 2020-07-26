package view;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import engine.Node;
import view.modules.TinyToggleModule;

public class NodeView {
    private Node node;
    private GraphicEntityModule graphics;
    private UnitView[] unitsViews = new UnitView[2];
    private Circle mainCircle;
    private Sprite roof;

    public NodeView(Node node, GraphicEntityModule graphics, TooltipModule tooltips, TinyToggleModule toggleModule) {
        this.graphics = graphics;
        this.node = node;
        node.view = this;
        mainCircle = graphics.createCircle().setX(node.getX()).setY(node.getY()).setRadius(35).setFillColor(0).setLineWidth(10).setLineColor(0).setZIndex(8);
        toggleModule.displayOnToggleState(mainCircle, "d", true);
        Group group = graphics.createGroup().setX(node.getX() - 30).setY(node.getY() - 40).setScale(0.1).setZIndex(8);
        Sprite house = graphics.createSprite().setImage("s1.png");
        roof = graphics.createSprite().setImage("s2.png");
        group.add(house, roof);
        graphics.createText().setX(node.getX()).setY(node.getY()).setAnchor(0.5).setText(String.valueOf(node.getId()))
                .setFillColor(0xffffff).setStrokeThickness(4).setZIndex(8);
        toggleModule.displayOnToggleState(group, "d", false);
        tooltips.setTooltipText(group, "x=" + node.getX() + "\ny=" + node.getY());
        tooltips.setTooltipText(mainCircle, "x=" + node.getX() + "\ny=" + node.getY());

        for (int i = 0; i < 2; i++) {
            unitsViews[i] = new UnitView(Player.getPlayer(i), node, 0, graphics, toggleModule);
        }
        endMove();
    }

    public void updateView(int playerId) {
        unitsViews[playerId].updateAmount(node.units[playerId]);
        endMove();
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
        if (lineColor == 0) roof.setTint(0xffffff);
        else roof.setTint(lineColor);
    }
}
