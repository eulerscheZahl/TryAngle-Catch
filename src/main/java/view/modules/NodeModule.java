package view.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.Module;
import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import engine.Node;

@Singleton
public class NodeModule implements Module {

    GameManager<AbstractPlayer> gameManager;
    @Inject
    GraphicEntityModule entityModule;
    List<Node> registered, newRegistration;

    @Inject
    NodeModule(GameManager<AbstractPlayer> gameManager) {
        this.gameManager = gameManager;
        gameManager.registerModule(this);
        registered = new ArrayList<>();
        newRegistration = new ArrayList<>();
    }

    @Override
    public void onGameInit() {
        sendFrameData();
    }

    @Override
    public void onAfterGameTurn() {
        sendFrameData();
    }

    @Override
    public void onAfterOnEnd() {
    }

    private void sendFrameData() {
        String s = "";
        for (Node node : newRegistration) {
            s += node.getX()+"/"+node.getY() + " ";
        }
        if (newRegistration.size() > 0) gameManager.setViewData("nodes", s.trim());

        newRegistration.clear();
    }

    public void registerNode(Node node) {
        newRegistration.add(node);
    }
}
