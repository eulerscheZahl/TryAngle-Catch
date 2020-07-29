package view.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.Module;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import engine.Node;
import view.BoardView.Move;

@Singleton
public class MoveModule implements Module {

    GameManager<AbstractPlayer> gameManager;
    List<Move> registered, newRegistration;

    class Toggle {
        public String name;
        public boolean state = true;

        public Toggle(String name, boolean state) {
            this.name = name;
            this.state = state;
        }

        public boolean equals(Toggle other) {
            return other != null && this.state == other.state && stringEquals(this.name, other.name);
        }

        private boolean stringEquals(String a, String b) {
            if (a == b) {
                return true;
            } else if (a != null && a.equals(b)) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Inject
    MoveModule(GameManager<AbstractPlayer> gameManager) {
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
    	HashMap<Integer, String> data = new HashMap<>();
    	data.put(0, "");
    	data.put(1, "");
    	String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	for (Move move : newRegistration) {
    		String s = "" + alphabet.charAt(move.from.getId()) + alphabet.charAt(move.to.getId());
    		if (move.count > 1) s += move.count;
    		data.put(move.player.getIndex(), data.get(move.player.getIndex()) + s);
    	}
        if (newRegistration.size() > 0) gameManager.setViewData("moves", data);
        newRegistration.clear();
    }

    public void registerMove(Move move) {
        newRegistration.add(move);
    }
}
