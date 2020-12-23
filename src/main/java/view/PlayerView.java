package view;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.*;
import view.modules.TinyToggleModule;

public class PlayerView {
    private Player player;
    private Text scoreText;
    private Text messageText;
    private Text troopsText;
    private Text trianglesText;
    private GraphicEntityModule graphicEntityModule;

    public PlayerView(Player player, GraphicEntityModule graphicEntityModule, TinyToggleModule toggleModule) {
        this.player = player;
        this.graphicEntityModule = graphicEntityModule;
        Group group = graphicEntityModule.createGroup().setX(player.getIndex() * (graphicEntityModule.getWorld().getWidth() / 2 + 150) + 30).setY(960);

        Text nickname = graphicEntityModule.createText().setText(player.getNicknameToken()).setX(150).setFillColor(player.getColor()).setFontSize(50).setStrokeThickness(2).setMaxWidth(450);
        Sprite avatar = graphicEntityModule.createSprite().setImage(player.getAvatarToken()).setBaseHeight(110).setBaseWidth(110);
        messageText = graphicEntityModule.createText("").setX(150).setY(80).setFillColor(player.getColor()).setFontSize(30).setStrokeThickness(2).setMaxWidth(300);
        scoreText = graphicEntityModule.createText("0").setX(750).setFillColor(player.getColor()).setFontSize(50).setText("0").setAnchorX(1).setStrokeThickness(2);

        Sprite troopsSprite = graphicEntityModule.createSprite().setX(490).setY(70).setImage("rb".charAt(player.getIndex()) + "5.png").setScale(0.05).setTint(player.getColor());
        troopsText = graphicEntityModule.createText().setText("3").setX(560).setY(70).setFillColor(player.getColor()).setFontSize(30).setStrokeThickness(2);
        Polygon triangle = graphicEntityModule.createPolygon().setX(660).setY(75).addPoint(10, 0).addPoint(0,20).addPoint(30,30).addPoint(10,0).setFillColor(player.getColor()).setLineWidth(2);
        trianglesText = graphicEntityModule.createText().setText("0").setX(750).setY(70).setAnchorX(1).setFillColor(player.getColor()).setFontSize(30).setStrokeThickness(2);
        group.add(nickname, avatar, messageText, scoreText, troopsSprite, troopsText, triangle, trianglesText);
        group.setZIndex(2);
    }

    private void updateText(Text text, String content) {
        if (!text.getText().equals(content)) {
            text.setText(content);
            graphicEntityModule.commitEntityState(0, text);
        }
    }

    public void update() {
        updateText(messageText, player.getMessage());
        updateText(scoreText, String.valueOf(player.getScore()));
        updateText(troopsText, String.valueOf(player.getUnits()));
        updateText(trianglesText, String.valueOf(player.getTriangles()));
    }
}
