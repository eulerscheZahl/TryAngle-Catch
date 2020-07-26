package view;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.toggle.ToggleModule;
import engine.Node;

public class UnitView {
    private Node node;
    private Player player;
    private int amount;

    private Group group;
    private Text text;
    private Sprite sprite;

    public UnitView(Player player, Node node, int amount, GraphicEntityModule graphics, ToggleModule toggleModule) {
        this.node = node;
        this.player = player;
        this.amount = amount;

        group = graphics.createGroup().setX(node.getX() - 40 + 80 * player.getIndex()).setY(node.getY() - 40).setZIndex(9);
        Circle circle = graphics.createCircle().setRadius(25).setFillColor(player.getColor()).setLineWidth(1);
        toggleModule.displayOnToggleState(circle, "debug", true);
        sprite = graphics.createSprite().setScale(0.1).setAnchor(0.5).setTint(player.getColor());
        toggleModule.displayOnToggleState(sprite, "debug", false);
        selectSpriteImage();

        text = graphics.createText().setText(String.valueOf(amount)).setFillColor(0xffffff).setStrokeThickness(4).setAnchor(0.5);
        group.add(circle, text, sprite);
        graphics.commitEntityState(0, circle, sprite);
        if (amount == 0) group.setVisible(false);
    }

    private void selectSpriteImage() {
        if (amount == 0) return;
        String image = player.getIndex() == 0?"r":"b";
        image += Math.min(amount, 5) + ".png";
        if (image.equals(sprite.getImage())) return;
        sprite.setImage(image);
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
            selectSpriteImage();
        }
    }

    public void commit(GraphicEntityModule graphics, double t) {
        graphics.commitEntityState(t, group, text, sprite);
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
