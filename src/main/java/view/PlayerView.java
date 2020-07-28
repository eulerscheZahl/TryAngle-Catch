package view;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;

public class PlayerView {
    private Player player;
    private Text scoreText;
    private Text messageText;
    private GraphicEntityModule graphicEntityModule;

    public PlayerView(Player player, GraphicEntityModule graphicEntityModule) {
        this.player = player;
        this.graphicEntityModule = graphicEntityModule;
        Group group = graphicEntityModule.createGroup().setX(player.getIndex() * (graphicEntityModule.getWorld().getWidth() / 2 + 200) + 30).setY(30);
        Text nickname = graphicEntityModule.createText().setText(player.getNicknameToken()).setX(150).setFillColor(player.getColor()).setFontSize(50).setStrokeThickness(2).setMaxWidth(400);
        Sprite avatar = graphicEntityModule.createSprite().setImage(player.getAvatarToken()).setBaseHeight(110).setBaseWidth(110);
        messageText = graphicEntityModule.createText("").setX(150).setY(80).setFillColor(player.getColor()).setFontSize(30).setStrokeThickness(2).setMaxWidth(400);
        scoreText = graphicEntityModule.createText("0").setX(700).setFillColor(player.getColor()).setFontSize(80).setText("0").setAnchorX(1).setStrokeThickness(2);
        group.add(nickname, avatar, messageText, scoreText);
    }

    public void updateMessage() {
        if (!messageText.getText().equals(player.getMessage())) {
            messageText.setText(player.getMessage());
            graphicEntityModule.commitEntityState(0, messageText);
        }
    }

    public void updateScore() {
        if (!scoreText.getText().equals(String.valueOf(player.getScore()))) {
            scoreText.setText(String.valueOf(player.getScore()));
            graphicEntityModule.commitEntityState(0, scoreText);
        }
    }
}
