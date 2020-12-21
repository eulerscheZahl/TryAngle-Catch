package view;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Polygon;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import engine.Triangle;

public class TriangleView {
    private Triangle triangle;
    private GraphicEntityModule graphicEntityModule;
    Player previousOwner = null;

    public TriangleView(Triangle triangle, GraphicEntityModule graphicEntityModule, TooltipModule tooltipModule) {
        this.triangle = triangle;
        this.graphicEntityModule = graphicEntityModule;
    }

    Polygon region = null;

    public void update() {
        if (graphicEntityModule == null) return;
        if (region == null) {
            region = graphicEntityModule.createPolygon().setZIndex(-1).setAlpha(0)
                    .addPoint(triangle.getNode1().getX(), triangle.getNode1().getY())
                    .addPoint(triangle.getNode2().getX(), triangle.getNode2().getY())
                    .addPoint(triangle.getNode3().getX(), triangle.getNode3().getY());
            graphicEntityModule.commitEntityState(0, region);
        }
        if (triangle.getOwner() == null) {
            if (previousOwner == null || triangle.canCapture(previousOwner)) {
                region.setAlpha(0).setFillColor(0xFFFFFF);
            } else region.setAlpha(0.3);
        } else {
            region.setAlpha(1).setFillColor(triangle.getOwner().getColor());
            previousOwner = triangle.getOwner();
        }
    }

    public void hide() {
        region.setVisible(false);
    }

    public void use() {
        triangle.getNode1().updateView(triangle.getOwner().getIndex(), true);
        triangle.getNode2().updateView(triangle.getOwner().getIndex(), true);
        triangle.getNode3().updateView(triangle.getOwner().getIndex(), true);
    }
}
