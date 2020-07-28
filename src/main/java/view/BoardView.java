package view;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

import engine.Board;
import engine.Node;
import engine.Triangle;
import view.modules.MoveModule;
import view.modules.NodeModule;
import view.modules.TinyToggleModule;

import java.util.ArrayList;

public class BoardView {
    private ArrayList<NodeView> nodeViews = new ArrayList<>();
    private Board board;
    private GraphicEntityModule graphics;
    private MoveModule moveModule;
    private TinyToggleModule toggleModule;
    private Text textTurn;
    private Text textType;

    public BoardView(Board board, GraphicEntityModule graphics, TooltipModule tooltips, TinyToggleModule toggleModule, NodeModule boardModule, MoveModule moveModule) {
        this.graphics = graphics;
        this.moveModule = moveModule;
        this.toggleModule = toggleModule;
        this.board = board;
        graphics.createRectangle().setZIndex(-9).setFillColor(0xffffff).setWidth(graphics.getWorld().getWidth()).setHeight(graphics.getWorld().getHeight());
        Sprite background = graphics.createSprite().setImage("background.png").setZIndex(-9).setAlpha(0.7);
        toggleModule.displayOnToggleState(background, "d", false);
        for (Node node : board.nodes) {
            boardModule.registerNode(node);
            nodeViews.add(new NodeView(node, graphics, tooltips, toggleModule));
            for (Node n : node.neighbors) {
                connect(node, n);
            }
        }
        Player.getPlayer(0).initView(graphics);
        Player.getPlayer(1).initView(graphics);

        textTurn = graphics.createText().setX(graphics.getWorld().getWidth()/2).setY(30).setAnchorX(0.5).setFontSize(50);
        textType = graphics.createText().setX(graphics.getWorld().getWidth()/2).setY(90).setAnchorX(0.5).setFontSize(50);
    }

    public void updateTurn(int turn, String type) {
        String turnString = "Turn " + turn;
        if (!turnString.equals(textTurn.getText())) textTurn.setText("Turn " + turn);
        if (!type.equals(textType.getText())) textType.setText(type);
        graphics.commitEntityState(0, textTurn, textType);
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
            Connection conn = (Connection)other;
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

    public void startMove(){
        for (NodeView nodeView : nodeViews) nodeView.startMove();
    }

    public void endMove(){
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
            moveModule.registerMove(move);
        }
    }
}
