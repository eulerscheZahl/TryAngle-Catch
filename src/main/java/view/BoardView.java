package view;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import engine.Board;
import engine.Node;

import java.util.ArrayList;
import java.util.Stack;

public class BoardView {
    private ArrayList<NodeView> nodeViews = new ArrayList<>();
    private GraphicEntityModule graphics;

    public BoardView(Board board, GraphicEntityModule graphics, TooltipModule tooltips) {
        this.graphics = graphics;
        graphics.createRectangle().setZIndex(-9).setFillColor(0xffffff).setWidth(graphics.getWorld().getWidth()).setHeight(graphics.getWorld().getHeight());
        for (Node node : board.nodes) {
            nodeViews.add(new NodeView(node, graphics, tooltips));
        }
    }

    public void startMove(){
        for (NodeView nodeView : nodeViews) nodeView.startMove();
    }

    public void endMove(){
        for (NodeView nodeView : nodeViews) nodeView.endMove();
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
