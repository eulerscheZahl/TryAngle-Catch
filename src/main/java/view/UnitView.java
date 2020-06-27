package view;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Text;
import engine.Node;

public class UnitView {
    private Node node;
    private Player player;
    private int amount;

    private Group group;
    private Text text;

    public UnitView(Player player, Node node, int amount, GraphicEntityModule graphics) {
        this.node = node;
        this.player = player;
        this.amount = amount;

        group = graphics.createGroup().setX(node.getX() - 40 + 80 * player.getIndex()).setY(node.getY() - 40).setZIndex(9);
        Circle circle = graphics.createCircle().setRadius(25).setFillColor(player.getColor()).setLineWidth(1);
        group.add(circle);
        text = graphics.createText().setText(String.valueOf(amount)).setFillColor(0xffffff).setAnchor(0.5);
        group.add(text);
        graphics.commitEntityState(0, circle);
        if (amount == 0) group.setVisible(false);
    }

    public Player getPlayer() {
        return player;
    }

    public Node getNode() {
        return node;
    }

    public void updateAmount(int unit) {
        if (unit == amount) return;
        amount = unit;
        if (amount == 0) group.setVisible(false);
        else {
            group.setVisible(true);
            text.setText(String.valueOf(amount));
        }
    }

    public void commit(GraphicEntityModule graphics, double t) {
        graphics.commitEntityState(t, group, text);
    }

    public void moveTo(Node node) {
        if (this.node == node) return;
        this.node = node;
        group.setX(node.getX() - 40 + 80 * player.getIndex()).setY(node.getY() - 40);
    }

    public void show() {
        group.setVisible(true);
    }

    public void hide() {
        group.setVisible(false);
    }

    public void commitAmount(GraphicEntityModule graphics, int unit) {
        if (unit == amount) return;
        updateAmount(unit);
        commit(graphics, 0);
    }
}
