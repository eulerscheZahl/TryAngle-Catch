package engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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

    public void makeNeighbors()
    {
        n1.neighbors.add(n2);
        n2.neighbors.add(n1);
    }

    private static boolean ccw(Node a, Node b, Node c)
    {
        return (c.getY() - a.getY()) * (b.getX() - a.getX()) > (b.getY() - a.getY()) * (c.getX() - a.getX());
    }

    public boolean isCrossing(Line line)
    {
        HashSet<Node> nodes = new HashSet<Node>();
        nodes.add(this.n1);
        nodes.add(this.n2);
        nodes.add(line.n1);
        nodes.add(line.n2);
        if (nodes.size() < 4) return false;
        return ccw(this.n1, line.n1, line.n2) != ccw(this.n2, line.n1, line.n2) && ccw(this.n1, this.n2, line.n1) != ccw(this.n1, this.n2, line.n2);
    }

    public double length()
    {
        return n1.dist(n2);
    }

    public boolean isBlocked(ArrayList<Line> lines)
    {
        if (lines.stream().anyMatch(l -> this.isCrossing(l))) return true;
        List<Node> commonNeighbors = n1.neighbors.stream().filter(n -> n2.neighbors.contains(n)).collect(Collectors.toList());
        for(Node n3 : commonNeighbors)
        {
            if (n1.dist(n3) + n3.dist(n2) < n1.dist(n2) * 1.05) return true;
        }
        return false;
    }
}
