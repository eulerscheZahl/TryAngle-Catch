using System;
using System.Linq;
using System.Collections.Generic;

class Node
{
    public int ID;
    public int X, Y;
    public List<Node> Neighbors = new List<Node>();
    public int MyUnits;
    public int OpponentUnits;
    public int[] Dist = new int[50];

    public Node(int id, int x, int y)
    {
        this.X = x;
        this.Y = y;
        this.ID = id;
    }

    public void Clear()
    {
        Neighbors.Clear();
        MyUnits = 0;
        OpponentUnits = 0;
        for (int i = 0; i < Dist.Length; i++) Dist[i] = 1000;
    }

    public void ComputeDist()
    {
        Queue<Node> queue = new Queue<Node>();
        queue.Enqueue(this);
        Dist[this.ID] = 0;
        while (queue.Count > 0)
        {
            Node current = queue.Dequeue();
            foreach (Node next in current.Neighbors)
            {
                if (Dist[next.ID] != 1000) continue;
                Dist[next.ID] = 1 + Dist[current.ID];
                queue.Enqueue(next);
            }
        }
    }

    public string Move(Node target)
    {
        if (this == target) return "";
        return "MOVE " + this.ID + " " + Neighbors.OrderBy(n => n.Dist[target.ID]).First().ID + " 1";
    }
}

class Triangle
{
    public Node Node1;
    public Node Node2;
    public Node Node3;
    public int Owner;
    public bool MeCanCapture;
    public bool OppCanCapture;

    public Triangle(Node node1, Node node2, Node node3, int owner, bool meCanCapture, bool oppCanCapture)
    {
        this.Node1 = node1;
        this.Node2 = node2;
        this.Node3 = node3;
        this.Owner = owner;
        this.MeCanCapture = meCanCapture;
        this.OppCanCapture = oppCanCapture;
    }

    public List<Node> Corners => new List<Node> { Node1, Node2, Node3 };
    public List<Node> Neighbors => Corners.SelectMany(c => c.Neighbors).ToList();

    public IEnumerable<List<Node>> CornerPermutations()
    {
        yield return new List<Node> { Node1, Node2, Node3 };
        //yield return new List<Node> { Node1, Node3, Node2 };
        //yield return new List<Node> { Node2, Node1, Node3 };
        //yield return new List<Node> { Node2, Node3, Node1 };
        //yield return new List<Node> { Node3, Node1, Node2 };
        //yield return new List<Node> { Node3, Node2, Node1 };
    }

    public override string ToString()
    {
        return Node1.ID + " " + Node2.ID + " " + Node3.ID;
    }
}

class Solution
{
    static string ReadLine()
    {
        string line = Console.ReadLine();
        //Console.Error.WriteLine(line);
        return line;
    }

    static void Main(string[] args)
    {
        Random random = new Random(0);
        string[] inputs;
        int nodeCount = int.Parse(ReadLine());
        List<Node> nodes = new List<Node>();
        for (int i = 0; i < nodeCount; i++)
        {
            inputs = ReadLine().Split(' ');
            int nodeId = int.Parse(inputs[0]);
            int nodeX = int.Parse(inputs[1]);
            int nodeY = int.Parse(inputs[2]);
            nodes.Add(new Node(nodeId, nodeX, nodeY));
        }

        // game loop
        while (true)
        {
            int[] scores = ReadLine().Split().Select(int.Parse).ToArray();
            nodes.ForEach(n => n.Clear());
            for (int i = 0; i < nodeCount; i++)
            {
                inputs = ReadLine().Split(' ');
                int[] nums = inputs.Select(int.Parse).ToArray();
                nodes[nums[0]].MyUnits = nums[1];
                nodes[nums[0]].OpponentUnits = nums[2];
            }
            int linkCount = int.Parse(ReadLine());
            for (int i = 0; i < linkCount; i++)
            {
                inputs = ReadLine().Split(' ');
                int node1 = int.Parse(inputs[0]);
                int node2 = int.Parse(inputs[1]);
                nodes[node1].Neighbors.Add(nodes[node2]);
                nodes[node2].Neighbors.Add(nodes[node1]);
            }
            int triangleCount = int.Parse(ReadLine());
            List<Triangle> triangles = new List<Triangle>();
            for (int i = 0; i < triangleCount; i++)
            {
                inputs = ReadLine().Split(' ');
                int node1 = int.Parse(inputs[0]);
                int node2 = int.Parse(inputs[1]);
                int node3 = int.Parse(inputs[2]);
                int owner = int.Parse(inputs[3]);
                bool meCanCapture = inputs[4] != "0";
                bool opponentCanCapture = inputs[5] != "0";
                triangles.Add(new Triangle(nodes[node1], nodes[node2], nodes[node3], owner, meCanCapture, opponentCanCapture));
            }
            int linkableCount = int.Parse(ReadLine());
            for (int i = 0; i < linkableCount; i++) ReadLine();

            nodes.ForEach(n => n.ComputeDist());

            List<Node> myUnitCells = new List<Node>();
            foreach (Node node in nodes)
            {
                for (int i = 0; i < node.MyUnits; i++) myUnitCells.Add(node);
            }

            int bestCost = 1000;
            string solution = "WAIT";

            if (myUnitCells.Count > 10)
            {
                List<Triangle> myTriangles = triangles.Where(t => t.Owner == 0 && t.Corners.All(c => c.MyUnits > 0)).ToList();
                if (myTriangles.Count > 0)
                {
                    Triangle attackSource = myTriangles.OrderByDescending(t => t.Neighbors.Max(n => n.OpponentUnits)).First();
                    Node attack = attackSource.Neighbors.OrderByDescending(n => n.OpponentUnits).First();
                    if (attack.OpponentUnits > 1)
                    {
                        solution = "ATTACK " + attackSource + " " + attack.ID + ";MSG ATTACK!;";
                    }
                }
            }

            List<Node> moving = new List<Node>();
            if (myUnitCells.Count >= 3) {
                foreach (Triangle triangle in triangles.Where(t => t.Owner != 0 && t.MeCanCapture))
                {
                    foreach (List<Node> corners in triangle.CornerPermutations())
                    {
                        List<Node> sources = new List<Node>();
                        List<Node> free = myUnitCells.ToList();
                        int currentScore = 0;
                        foreach (Node target in corners)
                        {
                            int index = 0;
                            for (int i = 1; i < free.Count; i++)
                            {
                                if (target.Dist[free[i].ID] < target.Dist[free[index].ID]) index = i;
                            }
                            sources.Add(free[index]);
                            free.RemoveAt(index);
                            currentScore = Math.Max(currentScore, sources.Last().Dist[target.ID]);
                        }
                        if (currentScore < bestCost)
                        {
                            bestCost = currentScore;
                            solution = string.Join(";", Enumerable.Range(0, 3).Select(i => sources[i].Move(corners[i])));
                            foreach (Node node in sources) solution += ";CIRCLE " + node.X + " " + node.Y + " 45";
                            for (int i = 0; i < corners.Count; i++) solution += ";LINE " + corners[i].X + " " + corners[i].Y + " " + corners[(i+1)%corners.Count].X + " " + corners[(i+1)%corners.Count].Y;
                            moving = sources;
                        }
                    }
                }
            }

            if (myUnitCells.Count < nodes.Count) {
                foreach (Triangle triangle in triangles.Where(t => t.Owner == 0))
                {
                    solution += ";SPAWN " + triangle.Node1.ID + " " + triangle.Node2.ID + " " + triangle.Node3.ID;
                    break;
                }
            }
            foreach (Node node in moving) myUnitCells.Remove(node);
            foreach (Node unit in myUnitCells) solution += ";MOVE " + unit.ID + " " + unit.Neighbors[random.Next(unit.Neighbors.Count)].ID + " 1";

            Console.Error.WriteLine("cost: " + bestCost);
            Console.WriteLine(solution);
        }
    }
}