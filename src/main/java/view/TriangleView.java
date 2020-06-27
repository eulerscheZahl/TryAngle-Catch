package view;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Polygon;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.internal.cglib.proxy.$Factory;
import engine.Triangle;

public class TriangleView {
    private Triangle triangle;
    private GraphicEntityModule graphicEntityModule;
    private TooltipModule tooltipModule;


    public TriangleView(Triangle triangle, GraphicEntityModule graphicEntityModule, TooltipModule tooltipModule) {
        this.triangle = triangle;
        this.graphicEntityModule = graphicEntityModule;
        this.tooltipModule = tooltipModule;
    }

    Polygon region = null;
    public void update() {
        if (region == null) {
            region = graphicEntityModule.createPolygon().setZIndex(-1)
                    .addPoint(triangle.getNode1().getX(), triangle.getNode1().getY())
                    .addPoint(triangle.getNode2().getX(), triangle.getNode2().getY())
                    .addPoint(triangle.getNode3().getX(), triangle.getNode3().getY());
            graphicEntityModule.commitEntityState(0, region);
        }
        if (triangle.getOwner() == null) region.setVisible(false);
        else region.setVisible(true).setFillColor(triangle.getOwner().getColor());
    }

    public void hide() {
        region.setVisible(false);
    }
}
