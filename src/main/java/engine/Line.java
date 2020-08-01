package engine;

import java.util.ArrayList;

public class Line {
    private Node n1;
    private Node n2;

    public Node getN1() {
        return n1;
    }

    public Node getN2() {
        return n2;
    }

    public Line(Node n1, Node n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    public void makeNeighbors() {
        n1.neighbors.add(n2);
        n2.neighbors.add(n1);
    }

    public void unmakeNeighbors() {
        n1.neighbors.remove(n2);
        n2.neighbors.remove(n1);
    }

    public static boolean ccw(Node a, Node b, Node c) {
        return (c.getY() - a.getY()) * (b.getX() - a.getX()) > (b.getY() - a.getY()) * (c.getX() - a.getX());
    }

    public boolean isCrossing(Line line) {
        if (this.n1 == this.n2) return false;
        // make sure that the lines don't "cross" by touching, i.e. sharing the same starting point
        if (this.n1 == line.n1 || this.n2 == line.n1 || this.n1 == line.n2 || this.n2 == line.n2) return false;

        // check for crossing lines
        return ccw(this.n1, line.n1, line.n2) != ccw(this.n2, line.n1, line.n2) && ccw(this.n1, this.n2, line.n1) != ccw(this.n1, this.n2, line.n2);
    }

    public double length() {
        return n1.dist(n2);
    }

    public boolean isBlocked(ArrayList<Line> lines) {
        if (length() > Board.MAX_PATH_LENGTH) return true;
        if (lines.stream().anyMatch(l -> this.isCrossing(l))) return true;

        return false;
    }
}
