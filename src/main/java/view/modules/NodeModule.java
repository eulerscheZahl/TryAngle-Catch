package view.modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.codingame.game.Player;
import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.Module;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import engine.Node;

@Singleton
public class NodeModule implements Module {

    private class UnitState {
        public Player player;
        public boolean turnStart;
        public Node node;
        public int amount;

        public UnitState(Player player, boolean turnStart, Node node, int amount) {
            this.player = player;
            this.turnStart = turnStart;
            this.node = node;
            this.amount = amount;
        }
    }

    private class NodeState {
        public Node node;
        public Player owner;

        public NodeState(Node node, Player owner) {
            this.node = node;
            this.owner = owner;
        }
    }

    GameManager<AbstractPlayer> gameManager;
    private List<Node> newNodeRegistration;
    private List<UnitState> unitStates;
    private List<NodeState> nodeStates;

    @Inject
    NodeModule(GameManager<AbstractPlayer> gameManager) {
        this.gameManager = gameManager;
        gameManager.registerModule(this);
        newNodeRegistration = new ArrayList<>();
        unitStates = new ArrayList<>();
        nodeStates = new ArrayList<>();
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
        for (Node node : newNodeRegistration) {
            s += node.getX() + "/" + node.getY() + " ";
        }

        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String p0Start = "00=";
        String p0End = "01=";
        String p1Start = "10=";
        String p1End = "11=";
        HashSet<String> processed = new HashSet<>();
        for (UnitState state : unitStates) {
            String hash = state.player + "_" + state.node + "_" + state.turnStart;
            if (processed.contains(hash)) continue; // avoid double commits
            processed.add(hash);
            String add = "" + alphabet.charAt(state.node.getId());
            if (state.amount != (state.turnStart ? 0 : 1)) add += state.amount;
            if (state.player.getIndex() == 0 && state.turnStart) p0Start += add;
            if (state.player.getIndex() == 0 && !state.turnStart) p0End += add;
            if (state.player.getIndex() == 1 && state.turnStart) p1Start += add;
            if (state.player.getIndex() == 1 && !state.turnStart) p1End += add;
        }
        if (p0Start.length() > 3) s += p0Start + " ";
        if (p0End.length() > 3) s += p0End + " ";
        if (p1Start.length() > 3) s += p1Start + " ";
        if (p1End.length() > 3) s += p1End + " ";

        String owners = "X";
        Player committed = null;
        Player[] playerOrder = new Player[]{null, Player.getPlayer(0), Player.getPlayer(1)};
        for (Player owner : playerOrder) {
            for (NodeState state : nodeStates) {
                if (state.owner == owner) {
                    if (committed != owner) {
                        committed = owner;
                        owners += owner.getIndex();
                    }
                    owners += alphabet.charAt(state.node.getId());
                }
            }
        }
        if (owners.length() > 1) s += owners;

        s = s.trim();
        if (s.length() > 0) gameManager.setViewData("nodes", s);

        newNodeRegistration.clear();
        unitStates.clear();
        nodeStates.clear();
    }

    public void registerNode(Node node) {
        newNodeRegistration.add(node);
    }

    public void updateUnits(Player player, boolean turnStart, Node node, int amount) {
        unitStates.add(0, new UnitState(player, turnStart, node, amount));
    }

    public void updateOwner(Node node, Player owner) {
        nodeStates.add(new NodeState(node, owner));
    }
}
