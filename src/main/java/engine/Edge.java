package engine;

import java.util.ArrayList;

public class Edge {
    private Node n1;
    private Node n2;

    public Node getN1() {
        return n1;
    }

    public Node getN2() {
        return n2;
    }

    public Edge(Node n1, Node n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    public void makeNeighbors() {
        n1.addNeighbor(n2);
    }

    public void unmakeNeighbors() {
        n1.removeNeighbor(n2);
    }

    public static boolean ccw(Node a, Node b, Node c) {
        return (c.getY() - a.getY()) * (b.getX() - a.getX()) > (b.getY() - a.getY()) * (c.getX() - a.getX());
    }

    public boolean isCrossing(Edge edge) {
        if (this.n1 == this.n2) return false;
        // make sure that the edges don't "cross" by touching, i.e. sharing the same starting point
        if (this.n1 == edge.n1 || this.n2 == edge.n1 || this.n1 == edge.n2 || this.n2 == edge.n2) return false;

        // check for crossing edges
        return ccw(this.n1, edge.n1, edge.n2) != ccw(this.n2, edge.n1, edge.n2) && ccw(this.n1, this.n2, edge.n1) != ccw(this.n1, this.n2, edge.n2);
    }

    public double length() {
        return n1.dist(n2);
    }

    public boolean isBlocked(ArrayList<Edge> edges) {
        if (length() > Board.MAX_PATH_LENGTH) return true;
        if (edges.stream().anyMatch(l -> this.isCrossing(l))) return true;

        return false;
    }
}
