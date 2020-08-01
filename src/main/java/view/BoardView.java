package view;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import engine.Board;
import engine.Node;
import engine.Triangle;
import engine.task.*;
import view.modules.NodeModule;
import view.modules.TaskModule;
import view.modules.TinyToggleModule;

import java.util.ArrayList;

public class BoardView {
    private ArrayList<NodeView> nodeViews = new ArrayList<>();
    private Board board;
    private GraphicEntityModule graphics;
    private TinyToggleModule toggleModule;
    private TaskModule taskModule;
    private Text textTurn;

    public BoardView(Board board, GraphicEntityModule graphics, TooltipModule tooltips, TinyToggleModule toggleModule, NodeModule nodeModule, TaskModule taskModule) {
        this.graphics = graphics;
        this.toggleModule = toggleModule;
        this.taskModule = taskModule;
        this.board = board;
        graphics.createRectangle().setZIndex(-9).setFillColor(0xffffff).setWidth(graphics.getWorld().getWidth()).setHeight(graphics.getWorld().getHeight());
        Sprite background = graphics.createSprite().setImage("background.png").setZIndex(-9).setAlpha(0.7);
        toggleModule.displayOnToggleState(background, "d", false);
        for (Node node : board.nodes) {
            nodeModule.registerNode(node);
            nodeViews.add(new NodeView(node, nodeModule));
            for (Node n : node.neighbors) {
                connect(node, n);
            }
        }
        Player.getPlayer(0).initView(graphics, toggleModule);
        Player.getPlayer(1).initView(graphics, toggleModule);

        Sprite scoreBox = graphics.createSprite().setX(graphics.getWorld().getWidth() / 2).setAnchorX(0.5).setY(900).setImage("decor_4.png").setScaleX(1.8).setScaleY(1.5);
        toggleModule.displayOnToggleState(scoreBox, "d", false);
        textTurn = graphics.createText().setX(graphics.getWorld().getWidth() / 2 - 5).setY(990).setAnchorX(0.5).setFontSize(30).setText("Tryangle catch");
    }

    public void updateTurn(int turn, String type) {
        textTurn.setText(turn + ": " + type);
        graphics.commitEntityState(0, textTurn);
    }

    class Connection {
        public Node n1;
        public Node n2;
        public Line line;
        public Sprite sprite;

        public Connection(Node n1, Node n2) {
            this.n1 = n1;
            this.n2 = n2;
            if (n1.getId() > n2.getId()) {
                this.n1 = n2;
                this.n2 = n1;
            }
        }

        @Override
        public boolean equals(Object other) {
            Connection conn = (Connection) other;
            return this.n1 == conn.n1 && this.n2 == conn.n2;
        }

        public void drawLine() {
            if (line != null) return;
            line = graphics.createLine().setX(n1.getX()).setY(n1.getY()).setX2(n2.getX()).setY2(n2.getY())
                    .setLineWidth(5).setFillColor(0).setZIndex(1);
            toggleModule.displayOnToggleState(line, "d", true);
            int length = 50 * (int) Math.round(n1.dist(n2) / 50);
            double dx = n2.getX() - n1.getX();
            double dy = n2.getY() - n1.getY();
            sprite = graphics.createSprite().setImage("p" + length + ".png");
            sprite.setX((n1.getX() + n2.getX()) / 2).setY((n1.getY() + n2.getY()) / 2).setAnchor(0.5)
                    .setRotation(Math.atan2(dy, dx)).setZIndex(1);
            toggleModule.displayOnToggleState(sprite, "d", false);
        }

        public void hideLine() {
            line.setVisible(false);
            sprite.setVisible(false);
        }
    }

    private ArrayList<Connection> connections = new ArrayList<>();

    public void connect(Node n1, Node n2) {
        Connection connection = new Connection(n1, n2);
        if (!connections.contains(connection)) {
            connections.add(connection);
            connection.drawLine();
        }
    }

    public void disconnect(Node n1, Node n2) {
        Connection connection = new Connection(n1, n2);
        for (Connection conn : connections) {
            if (conn.equals(connection)) conn.hideLine();
        }
        connections.remove(connection);
    }

    public void startMove() {
        for (NodeView nodeView : nodeViews) nodeView.startMove();
    }

    public void endMove() {
        for (NodeView nodeView : nodeViews) nodeView.endMove();
        for (Triangle triangle : board.triangles) triangle.updateOwnerView();
    }

    public class Move {
        public Player player;
        public Node from;
        public Node to;
        public int count;

        public Move(Player player, Node from, Node to, int amount) {
            this.player = player;
            this.from = from;
            this.to = to;
            this.count = amount;
        }
    }

    private ArrayList<Move> moves = new ArrayList<>();

    public void clearMoves() {
        moves.clear();
    }

    public void cacheMove(Player player, Node from, Node to, int amount) {
        for (Move move : moves) {
            if (move.player == player && move.from == from && move.to == to) {
                move.count += amount;
                return;
            }
        }
        moves.add(new Move(player, from, to, amount));
    }

    public void animateMoves() {
        for (Move move : moves) {
            animateTask(new MoveTask(move.player, move.from, move.to, move.count));
        }
    }

    public void animateTask(Task task) {
        taskModule.registerTask(task);
    }
}
