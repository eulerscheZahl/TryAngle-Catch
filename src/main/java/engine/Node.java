package engine;

import com.codingame.game.Player;
import com.google.inject.internal.cglib.proxy.$UndeclaredThrowableException;
import view.NodeView;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.PatternSyntaxException;

public class Node {
    private int id;
    private int x;
    private int y;
    public ArrayList<Node> neighbors = new ArrayList<>();
    public int[] units = new int[2];
    public int[] remainingUnits = new int[2];
    public NodeView view;

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Node(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Node mirror() {
        return new Node(id + 1, Board.WIDTH + 2 * Board.FRAME_SIZE - 1 - x, Board.HEIGHT + 2 * Board.FRAME_SIZE - 1 - y);
    }

    public double dist(Node node) {
        int dx = this.x - node.x;
        int dy = this.y - node.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void finalizeTurn() {
        remainingUnits[0] = units[0];
        remainingUnits[1] = units[1];
    }

    private int[] bfs() {
        int[] dist = new int[Board.NODE_COUNT];
        for (int i = 0; i < dist.length; i++) dist[i] = dist.length;
        dist[this.id] = 0;
        Queue<Node> queue = new ConcurrentLinkedDeque<>();
        queue.add(this);
        while (queue.size() > 0) {
            Node current = queue.poll();
            for (Node next : current.neighbors) {
                if (dist[next.id] < dist.length) continue;
                dist[next.id] = 1 + dist[current.id];
                queue.add(next);
            }
        }

        return dist;
    }

    public void updateView(int playerId) {
        view.updateView(playerId);
    }

    public boolean canMoveTo(int id, Node target) {
        if (this == target) return false;
        if (remainingUnits[id] <= 0) return false; // TODO warning
        int[] dist = target.bfs();
        return dist[this.id] > 0;
    }

    public boolean moveTo(int id, Node target) {
        if (this == target) return false;
        if (remainingUnits[id] <= 0) return false; // TODO warning
        int[] dist = target.bfs();

        for (Node next : this.neighbors) {
            if (dist[next.id] < dist[this.id]) {
                this.units[id]--;
                this.remainingUnits[id]--;
                next.units[id]++;
                return true;
            }
        }

        // TODO warning, no valid move
        return false;
    }

    public Player getOwner() {
        // surrounded?
        if (neighbors.stream().allMatch(n -> n.units[0] > n.units[1])) return Player.getPlayer(0);
        if (neighbors.stream().allMatch(n -> n.units[1] > n.units[0])) return Player.getPlayer(1);

        // more units?
        if (units[0] > units[1]) return Player.getPlayer(0);
        if (units[1] > units[0]) return Player.getPlayer(1);

        return null;
    }

    public boolean ownedBy(Player player) {
        return getOwner() == player;
    }
}
