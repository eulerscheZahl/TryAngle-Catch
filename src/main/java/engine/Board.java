package engine;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import engine.task.SurroundTask;
import engine.task.MoveTask;
import engine.task.Task;
import engine.task.TaskManager;
import view.BoardView;
import view.modules.NodeModule;
import view.modules.TaskModule;
import view.modules.TinyToggleModule;

import java.util.*;

public class Board {
    public static final int TOP_BORDER = 70;
    public static final int BOTTOM_BORDER = 190;
    public static final int SIDE_BORDER = 80;
    public static final int WIDTH = 1920 - 2 * SIDE_BORDER;
    public static final int HEIGHT = 1080 - TOP_BORDER - BOTTOM_BORDER;
    public static final int NODE_COUNT = 50;
    public static final int NODE_MIN_DIST = 140;
    public static final double SPARSE_MIN = 0.1;
    public static final double SPARSE_MAX = 0.5;
    public static final int MIN_TRIANGLE_COUNT = 20;
    public static final int MAX_PATH_LENGTH = 400;
    public static final int MAX_ANGLE = 160;

    public ArrayList<Node> nodes = new ArrayList<>();
    public ArrayList<Triangle> triangles = new ArrayList<>();

    private BoardView view;
    private GraphicEntityModule graphicEntityModule;
    private TooltipModule tooltipModule;
    private TinyToggleModule toggleModule;

    public Board(Random random, GraphicEntityModule graphicEntityModule, TooltipModule tooltipModule, TinyToggleModule toggleModule, NodeModule nodeModule, TaskModule taskModule) {
        this.graphicEntityModule = graphicEntityModule;
        this.tooltipModule = tooltipModule;
        this.toggleModule = toggleModule;


        ArrayList<Edge> exsitingEdges;
        while (true) {
            // place nodes on map, ensure minimum distance and unique distances
            int tries = 0;
            nodes.clear();
            HashSet<Integer> nodeDistances = new HashSet<>();
            while (nodes.size() < NODE_COUNT && tries++ < 10000) {
                Node n1 = new Node(nodes.size(), SIDE_BORDER + random.nextInt(WIDTH), TOP_BORDER + random.nextInt(HEIGHT));
                Node n2 = n1.mirror();
                if (nodes.stream().anyMatch(n -> n.dist(n1) < NODE_MIN_DIST) ||
                        n1.dist(n2) < NODE_MIN_DIST ||
                        nodeDistances.contains(n1.dist2(n2)) ||
                        nodes.stream().anyMatch(n -> nodeDistances.contains(n.dist2(n1)))) continue;
                if (obtuseAngle(nodes, n1, n2)) continue;
                nodeDistances.add(n1.dist2(n2));
                for (Node n : nodes) nodeDistances.add(n.dist2(n1));
                nodes.add(n1);
                nodes.add(n2);
            }

            // add edges to create triangles
            ArrayList<Edge> edges = new ArrayList<Edge>();
            for (int i = 0; i < nodes.size(); i++) {
                for (int j = i + 1; j < nodes.size(); j++) {
                    Node n1 = nodes.get(i);
                    Node n2 = nodes.get(j);
                    edges.add(new Edge(n1, n2));
                }
            }
            Collections.sort(edges, (o1, o2) -> (int) Math.signum(o1.length() - o2.length()));
            exsitingEdges = new ArrayList<>();
            for (int i = 0; i < edges.size(); i++) {
                if (edges.get(i).isBlocked(exsitingEdges)) {
                    edges.remove(i);
                    i--;
                } else {
                    edges.get(i).makeNeighbors();
                    exsitingEdges.add(edges.get(i));
                }
            }

            updateTriangles();
            if (isStronglyConnected() && triangles.size() >= MIN_TRIANGLE_COUNT) break;
        }

        // randomly remove some edges again
        HashSet<Edge> testedEdges = new HashSet<>();
        int removeEdgeCount = (int) (exsitingEdges.size() * (random.nextDouble() * (SPARSE_MAX - SPARSE_MIN) + SPARSE_MIN));
        while (removeEdgeCount > 0) {
            removeEdgeCount -= 2;
            Edge toDisconnect = exsitingEdges.get(random.nextInt(exsitingEdges.size()));
            if (testedEdges.contains(toDisconnect)) continue;
            Node mirror1 = getMirror(toDisconnect.getN1());
            Node mirror2 = getMirror(toDisconnect.getN2());
            Edge partner = null;
            for (Edge edge : exsitingEdges) {
                if (edge.getN1() == mirror1 && edge.getN2() == mirror2 || edge.getN1() == mirror2 && edge.getN2() == mirror1)
                    partner = edge;
            }
            testedEdges.add(toDisconnect);
            testedEdges.add(partner);

            updateTriangles();
            toDisconnect.unmakeNeighbors();
            partner.unmakeNeighbors();
            updateTriangles();
            if (triangles.size() >= MIN_TRIANGLE_COUNT && isStronglyConnected()) {
                exsitingEdges.remove(toDisconnect);
                exsitingEdges.remove(partner);
            } else {
                toDisconnect.makeNeighbors();
                partner.makeNeighbors();
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

        view = new BoardView(this, graphicEntityModule, tooltipModule, toggleModule, nodeModule, taskModule);
        finalizeTurn();
    }

    private boolean isStronglyConnected() {
        for (Node blocked : nodes) {
            Node start = nodes.get(0);
            if (start == blocked) start = nodes.get(1);
            int[] dist = start.bfs(blocked);
            int unreachable = 0;
            for (int i = 0; i < nodes.size(); i++) {
                if (dist[i] == dist.length) unreachable++;
            }
            if (unreachable > 1) return false;
        }
        return true;
    }

    private boolean obtuseAngle(ArrayList<Node> nodes, Node n1, Node n2) {
        ArrayList<Node> list = new ArrayList<Node>(nodes);
        list.add(n1);
        list.add(n2);
        for (int i1 = 0; i1 < list.size(); i1++) {
            for (int i2 = i1 + 1; i2 < list.size(); i2++) {
                for (int i3 = i2 + 1; i3 < list.size(); i3++) {
                    double a = list.get(i1).dist(list.get(i2));
                    double b = list.get(i1).dist(list.get(i3));
                    double c = list.get(i2).dist(list.get(i3));
                    if (a > MAX_PATH_LENGTH || b > MAX_PATH_LENGTH || c > MAX_PATH_LENGTH) continue;
                    double alpha = Math.toDegrees(Math.acos((b * b + c * c - a * a) / (2 * b * c)));
                    double beta = Math.toDegrees(Math.acos((a * a + c * c - b * b) / (2 * a * c)));
                    double gamma = Math.toDegrees(Math.acos((a * a + b * b - c * c) / (2 * a * b)));
                    if (alpha > MAX_ANGLE || beta > MAX_ANGLE || gamma > MAX_ANGLE)
                        return true;
                }
            }
        }
        return false;
    }

    public boolean canConnect(Node from, Node to) {
        ArrayList<Edge> exsitingEdges = new ArrayList<>();
        for (Node n1 : nodes) {
            for (Node n2 : n1.neighbors) {
                if (n2.getId() > n1.getId()) exsitingEdges.add(new Edge(n1, n2));
            }
        }
        Edge connect = new Edge(from, to);
        return !connect.isBlocked(exsitingEdges);
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

        if (view != null) {
            for (Triangle old : oldTriangles) {
                if (!triangles.contains(old)) old.delete();
            }
        }
    }

    private ArrayList<Triangle> findTriangles() {
        ArrayList<Triangle> result = new ArrayList<>();
        for (Node node1 : nodes) {
            for (Node node2 : node1.neighbors) {
                for (Node node3 : node2.neighbors) {
                    if (node1.getId() < node2.getId() && node2.getId() < node3.getId() && node1.neighbors.contains(node3) &&
                            !nodes.stream().anyMatch(n -> isInside(n, node1, node2, node3)))
                        result.add(new Triangle(node1, node2, node3, graphicEntityModule, tooltipModule));
                }
            }
        }
        return result;
    }

    private boolean isInside(Node n, Node a, Node b, Node c) {
        if (n == a || n == b || n == c) return false;
        boolean ccw1 = Edge.ccw(n, a, b);
        boolean ccw2 = Edge.ccw(n, b, c);
        boolean ccw3 = Edge.ccw(n, c, a);

        boolean has_neg = !ccw1 || !ccw2 || !ccw3;
        boolean has_pos = ccw1 || ccw2 || ccw3;

        return !(has_neg && has_pos);
    }

    private boolean killedSurrounded = false;
    private int gameTurn = 0;

    public void applyActions(TaskManager taskManager) {
        killedSurrounded = false;
        Player.getPlayer(0).updateView();
        Player.getPlayer(1).updateView();

        boolean move = false;
        for (Task task : taskManager.popTasks()) {
            view.updateTurn(gameTurn, task.getName());
            if (!task.canApply(this)) continue; // double check because of multiple actions per turn
            task.apply(this);
            task.visualize(view);
            move |= task instanceof MoveTask;
        }

        if (move) {
            view.startMove();
            view.animateMoves();
        }
        makeStats();
        view.endMove();
    }

    private void makeStats() {
        for (int playerIndex = 0; playerIndex < 2; playerIndex++) {
            Player player = Player.getPlayer(playerIndex);
            player.setTriangles(0);
            player.setUnits(0);
            player.setNodes(0);
            for (Node node : nodes) {
                player.setUnits(player.getUnits() + node.units[playerIndex]);
                if (node.ownedBy(player)) player.setNodes(1 + player.getNodes());
            }
            for (Triangle triangle : triangles) {
                if (triangle.getOwner() == player) player.setTriangles(player.getTriangles() + 1);
            }
            player.updateView();
        }
    }

    public boolean finalizeTurn() {
        for (Node node : nodes) node.finalizeTurn();
        view.clearMoves();

        for (Triangle triangle : triangles) {
            triangle.updateAllowedCaptures();
        }
        if (surroundNodes() || captureTriangles()) {
            makeStats();
            return true;
        }

        for (Triangle triangle : triangles) {
            if (triangle.getOwner() != null) triangle.getOwner().increaseScore();
            triangle.finalizeTurn();
        }
        makeStats();
        return false;
    }

    private boolean surroundNodes() {
        if (killedSurrounded) return false;
        killedSurrounded = true;
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
                    view.animateTask(new SurroundTask(node, Player.getPlayer(i), node.units[i]));
                    node.units[i] = 0;
                    node.updateView(i, true);
                    surrounded = true;
                    view.updateTurn(gameTurn, "SURROUND");
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
        if (result) view.updateTurn(gameTurn, "CAPTURE");
        return result;
    }

    public String getInput(boolean initial, Player player) {
        if (player.getIndex() == 0) gameTurn++;

        StringBuilder sb = new StringBuilder();
        if (initial) {
            sb.append(nodes.size() + "\n");
            for (Node node : nodes) {
                sb.append(node.getId() + " " + node.getX() + " " + node.getY() + "\n");
            }
        }

        sb.append(player.getScore() + " " + player.getOpponent().getScore() + "\n");
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
