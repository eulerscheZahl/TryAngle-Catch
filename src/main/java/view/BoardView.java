package view;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import engine.Board;
import engine.Node;
import engine.Triangle;

import java.util.ArrayList;

public class BoardView {
    private ArrayList<NodeView> nodeViews = new ArrayList<>();
    private Board board;
    private GraphicEntityModule graphics;

    public BoardView(Board board, GraphicEntityModule graphics, TooltipModule tooltips) {
        this.graphics = graphics;
        this.board = board;
        graphics.createRectangle().setZIndex(-9).setFillColor(0xffffff).setWidth(graphics.getWorld().getWidth()).setHeight(graphics.getWorld().getHeight());
        for (Node node : board.nodes) {
            nodeViews.add(new NodeView(node, graphics, tooltips));
            for (Node n : node.neighbors) {
                connect(node, n);
            }
        }
        Player.getPlayer(0).initView(graphics);
        Player.getPlayer(1).initView(graphics);
    }

    class Connection {
        public Node n1;
        public Node n2;
        public Line line;

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
        }

        public void hideLine() {
            line.setVisible(false);
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

    private class Move {
        public Player player;
        public Node from;
        public Node to;
        public int count;

        public Move(Player player, Node from, Node to) {
            this.player = player;
            this.from = from;
            this.to = to;
            count = 1;
        }
    }

    private ArrayList<Move> moves = new ArrayList<>();

    public void clearMoves() {
        moves.clear();
    }

    public void cacheMove(Player player, Node from, Node to) {
        for (Move move : moves) {
            if (move.player == player && move.from == from && move.to == to) {
                move.count++;
                return;
            }
        }
        moves.add(new Move(player, from, to));
    }

    private ArrayList<UnitView> unitViews = new ArrayList<>();
    public void animateMoves() {
        ArrayList<UnitView> inUse = new ArrayList<>();
        for (Move move : moves) {
            UnitView current = null;
            for (UnitView view : unitViews) {
                if (view.getPlayer() == move.player && (view.getNode() == move.from || current == null)) current = view;
            }
            if (current == null) current = new UnitView(move.player, move.from, move.count, graphics);
            inUse.add(current);
            unitViews.remove(current);

            current.moveTo(move.from);
            current.updateAmount(move.count);
            current.show();
            current.commit(graphics, 0);
            current.moveTo(move.to);
            current.hide();
        }
        unitViews.addAll(inUse);
    }
}
