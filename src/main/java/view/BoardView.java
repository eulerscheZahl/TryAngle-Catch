package view;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import engine.Board;
import engine.Node;
import engine.Triangle;
import engine.task.*;
import engine.task.debug.DebugTask;
import view.modules.DebugModule;
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
    private DebugModule debugModule;
    private Text textTurn;

    public BoardView(Board board, GraphicEntityModule graphics, TooltipModule tooltips, TinyToggleModule toggleModule, NodeModule nodeModule, TaskModule taskModule, DebugModule debugModule) {
        this.graphics = graphics;
        this.toggleModule = toggleModule;
        this.taskModule = taskModule;
        this.debugModule = debugModule;
        this.board = board;
        if (graphics != null) {
            graphics.createRectangle().setZIndex(-9).setFillColor(0xffffff).setWidth(graphics.getWorld().getWidth()).setHeight(graphics.getWorld().getHeight());
            Sprite background = graphics.createSprite().setImage("background.png").setZIndex(-9).setAlpha(0.7);
            toggleModule.displayOnToggleState(background, "d", false);
        }
        for (Node node : board.nodes) {
            nodeModule.registerNode(node);
            nodeViews.add(new NodeView(node, nodeModule));
            for (Node n : node.neighbors) {
                connect(node, n);
            }
        }
        Player.getPlayer(0).initView(graphics, toggleModule);
        Player.getPlayer(1).initView(graphics, toggleModule);

        Sprite scoreBox = graphics.createSprite().setX(graphics.getWorld().getWidth() / 2).setY(-45).setAnchorX(0.5).setImage("decor_4.png").setScaleX(1.8).setScaleY(1.5).setZIndex(2);
        Rectangle rect = graphics.createRectangle().setWidth(graphics.getWorld().getWidth()).setHeight(200).setFillColor(0xFFFFFF).setZIndex(1);
        Sprite wall1 = graphics.createSprite().setImage("w.png").setScale(2).setZIndex(1).setAlpha(0.6);
        Sprite wall2 = graphics.createSprite().setX(682).setImage("w.png").setScale(2).setZIndex(1).setAlpha(0.6);
        Sprite wall3 = graphics.createSprite().setX(682 * 2).setImage("w.png").setScale(2).setZIndex(1).setAlpha(0.6);

        Group decor = graphics.createGroup(rect, wall1, wall2, wall3, scoreBox).setY(945);
        toggleModule.displayOnToggleState(decor, "d", false);
        textTurn = graphics.createText().setX(graphics.getWorld().getWidth() / 2 - 5).setY(990).setAnchorX(0.5).setFontSize(30).setText("Tryangle catch").setZIndex(2);
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

        public void drawEdge() {
            if (line != null) return;
            line = graphics.createLine().setX(n1.getX()).setY(n1.getY()).setX2(n2.getX()).setY2(n2.getY())
                    .setLineWidth(5).setFillColor(0).setZIndex(1).setAlpha(0);
            toggleModule.displayOnToggleState(line, "d", true);
            int length = 50 * (int) Math.round(n1.dist(n2) / 50);
            double dx = n2.getX() - n1.getX();
            double dy = n2.getY() - n1.getY();
            sprite = graphics.createSprite().setImage("p" + length + ".png").setAlpha(0);
            sprite.setX((n1.getX() + n2.getX()) / 2).setY((n1.getY() + n2.getY()) / 2).setAnchor(0.5)
                    .setRotation(Math.atan2(dy, dx)).setZIndex(1);
            toggleModule.displayOnToggleState(sprite, "d", false);
            graphics.commitEntityState(0, sprite, line);
            line.setAlpha(1);
            sprite.setAlpha(1);
        }

        public void hideLine() {
            line.setAlpha(0);
            sprite.setAlpha(0);
        }
    }

    private ArrayList<Connection> connections = new ArrayList<>();

    public void connect(Node n1, Node n2) {
        Connection connection = new Connection(n1, n2);
        if (!connections.contains(connection)) {
            connections.add(connection);
            connection.drawEdge();
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
        public int amount;

        public Move(Player player, Node from, Node to, int amount) {
            this.player = player;
            this.from = from;
            this.to = to;
            this.amount = amount;
        }
    }

    private ArrayList<Move> moves = new ArrayList<>();

    public void clearMoves() {
        moves.clear();
    }

    public void cacheMove(Player player, Node from, Node to, int amount) {
        for (Move move : moves) {
            if (move.player == player && move.from == from && move.to == to) {
                move.amount += amount;
                return;
            }
        }
        moves.add(new Move(player, from, to, amount));
    }

    public void animateMoves() {
        for (Move move : moves) {
            animateTask(new MoveTask(move.player, move.from, move.to, move.amount));
        }
    }

    public void animateTask(Task task) {
        taskModule.registerTask(task);
    }

    public void cacheDebug(DebugTask task) {
        debugModule.registerTask(task);
    }

    public void clearDebug() {
        debugModule.clear();
    }
}
