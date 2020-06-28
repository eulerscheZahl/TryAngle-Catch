package engine;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import engine.task.MoveTask;
import engine.task.Task;
import engine.task.TaskManager;
import view.BoardView;

import java.util.*;

public class Board {
    public static final int FRAME_SIZE = 80;
    public static final int WIDTH = 1920 - 2 * FRAME_SIZE;
    public static final int HEIGHT = 1080 - 2 * FRAME_SIZE;
    public static final int NODE_COUNT = 50;
    public static final int NODE_MIN_DIST = 140;

    public ArrayList<Node> nodes = new ArrayList<>();
    public ArrayList<Triangle> triangles = new ArrayList<>();

    private BoardView view;
    private GraphicEntityModule graphicEntityModule;
    private TooltipModule tooltipModule;

    public Board(Random random, GraphicEntityModule graphicEntityModule, TooltipModule tooltipModule) {
        this.graphicEntityModule = graphicEntityModule;
        this.tooltipModule = tooltipModule;

        int tries = 0;
        HashSet<Integer> nodeDistances = new HashSet<>();
        while (nodes.size() < NODE_COUNT && tries++ < 10000) {
            Node n1 = new Node(nodes.size(), FRAME_SIZE + random.nextInt(WIDTH - 2 * FRAME_SIZE), FRAME_SIZE + random.nextInt(HEIGHT - 2 * FRAME_SIZE));
            Node n2 = n1.mirror();
            if (nodes.stream().anyMatch(n -> n.dist(n1) < NODE_MIN_DIST) ||
                    n1.dist(n2) < NODE_MIN_DIST ||
                    nodeDistances.contains(n1.dist2(n2)) ||
                    nodes.stream().anyMatch(n -> nodeDistances.contains(n.dist2(n1)))) continue;
            nodeDistances.add(n1.dist2(n2));
            for (Node n : nodes) nodeDistances.add(n.dist2(n1));
            nodes.add(n1);
            nodes.add(n2);
        }
        ArrayList<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                Node n1 = nodes.get(i);
                Node n2 = nodes.get(j);
                lines.add(new Line(n1, n2));
            }
        }
        Collections.sort(lines, (o1, o2) -> (int) Math.signum(o1.length() - o2.length()));
        ArrayList<Line> exsitingLines = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).length() > 500 || lines.get(i).isBlocked(exsitingLines)) {
                lines.remove(i);
                i--;
            } else {
                lines.get(i).makeNeighbors();
                exsitingLines.add(lines.get(i));
            }
        }

        ArrayList<Node> nodesByX = new ArrayList<Node>(nodes);
        Collections.sort(nodesByX, Comparator.comparingInt(Node::getX));
        for (int i = 0; i < 3; i++) {
            Node node = nodesByX.get(i);
            node.units[0] = 1;
            getMirror(node).units[1] = 1;
        }
        updateTriangles();

        view = new BoardView(this, graphicEntityModule, tooltipModule);
        finalizeTurn();
    }

    public boolean canConnect(Node from, Node to) {
        ArrayList<Line> exsitingLines = new ArrayList<>();
        for (Node n1 : nodes) {
            for (Node n2 : n1.neighbors) {
                if (n2.getId() > n1.getId()) exsitingLines.add(new Line(n1, n2));
            }
        }
        Line connect = new Line(from, to);
        return !connect.isBlocked(exsitingLines);
    }

    private Node getMirror(Node node) {
        if (node.getId() % 2 == 0) return nodes.get(node.getId() + 1);
        return nodes.get(node.getId() - 1);
    }

    public void updateTriangles() {
        ArrayList<Triangle> oldTriangles = triangles;
        ArrayList<Triangle> newTriangles = findTriangles();
        triangles = new ArrayList<>();
        for (Triangle t : newTriangles) {
            Triangle old = null;
            for (Triangle t2 : oldTriangles) {
                if (t.equals(t2)) old = t2;
            }
            if (old == null) triangles.add(t);
            else triangles.add(old); // keep triangle stats like the owner
        }
        for (Triangle old : oldTriangles) {
            if (!triangles.contains(old)) old.delete();
        }
    }

    private ArrayList<Triangle> findTriangles() {
        ArrayList<Triangle> result = new ArrayList<>();
        for (Node node1 : nodes) {
            for (Node node2 : node1.neighbors) {
                for (Node node3 : node2.neighbors) {
                    if (node1.getId() < node2.getId() && node2.getId() < node3.getId() && node1.neighbors.contains(node3))
                        result.add(new Triangle(node1, node2, node3, graphicEntityModule, tooltipModule));
                }
            }
        }
        return result;
    }

    public void applyActions(TaskManager taskManager) {
        boolean move = false;
        for (Task task : taskManager.popTasks()) {
            task.apply(this);
            task.visualize(view);
            move |= task instanceof MoveTask;
        }

        if (move) {
            view.startMove();
            view.animateMoves();
            view.endMove();
        }
    }

    public boolean finalizeTurn() {
        for (Node node : nodes) node.finalizeTurn();
        view.clearMoves();

        for (Triangle triangle : triangles) {
            triangle.updateAllowedCaptures();
        }
        if (surroundNodes() || captureTriangles())
            return true;

        for (Triangle triangle : triangles) {
            if (triangle.getOwner() != null) triangle.getOwner().increaseScore();
        }
        return false;
    }

    private boolean surroundNodes() {
        ArrayList<Node>[] playerAdvantage = new ArrayList[2];
        for (int i = 0; i < 2; i++) {
            playerAdvantage[i] = new ArrayList<>();
            for (Node node : nodes) {
                if (node.units[i] > node.units[(i + 1) % 2]) playerAdvantage[i].add(node);
            }
        }

        boolean surrounded = false;
        for (int i = 0; i < 2; i++) {
            for (Node node : nodes) {
                int opponentIndex = (i + 1) % 2;
                if (node.units[i] > 0 && node.neighbors.stream().allMatch(n -> playerAdvantage[opponentIndex].contains(n))) {
                    node.units[i] = 0;
                    node.updateView(i);
                    surrounded = true;
                }
            }
        }

        return surrounded;
    }

    private boolean captureTriangles() {
        boolean result = false;
        for (Triangle triangle : triangles) {
            result |= triangle.capture();
        }
        return result;
    }

    public String getInput(boolean initial, Player player) {
        StringBuilder sb = new StringBuilder();
        if (initial) {
            sb.append(nodes.size() + "\n");
            for (Node node : nodes) {
                sb.append(node.getId() + " " + node.getX() + " " + node.getY() + "\n");
            }
        }
        int[][] unitsPerPlayer = new int[2][nodes.size()];
        for (Node node : nodes) {
            unitsPerPlayer[0][node.getId()] = node.units[player.getIndex()];
            unitsPerPlayer[1][node.getId()] = node.units[(player.getIndex() + 1) % 2];
        }
        for (int[] units : unitsPerPlayer) {
            sb.append(units[0]);
            for (int i = 1; i < units.length; i++) sb.append(" " + units[i]);
            sb.append("\n");
        }
        ArrayList<String> links = new ArrayList<>();
        for (Node n1 : nodes) {
            for (Node n2 : n1.neighbors) {
                if (n2.getId() <= n1.getId()) continue;
                links.add(n1.getId() + " " + n2.getId());
            }
        }
        sb.append(links.size() + "\n");
        for (String link : links) sb.append(link + "\n");
        sb.append(triangles.size() + "\n");
        for (Triangle triangle : triangles) sb.append(triangle.getInput(player) + "\n");

        //System.err.println(sb.toString());
        return sb.toString();
    }

    public void connect(Node from, Node to) {
        from.neighbors.add(to);
        to.neighbors.add(from);
        updateTriangles();
        view.connect(from, to);
    }

    public void disconnect(Node from, Node to) {
        from.neighbors.remove(to);
        to.neighbors.remove(from);
        updateTriangles();
        view.disconnect(from, to);
    }
}
