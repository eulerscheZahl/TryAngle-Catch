import java.util.Scanner;

public class Agent1 {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int nodeCount = in.nextInt();
        System.err.println(nodeCount);
        for (int i = 0; i < nodeCount; i++) {
            int nodeId = in.nextInt();
            int nodeX = in.nextInt();
            int nodeY = in.nextInt();
            System.err.println(nodeId + " " + nodeX + " " + nodeY);
        }

        // game loop
        while (true) {
            for (int i = 0; i < nodeCount; i++) {
                int myUnits = in.nextInt();
                System.err.print(myUnits + " ");
            }
            System.err.println();
            for (int i = 0; i < nodeCount; i++) {
                int opponentUnits = in.nextInt();
                System.err.print(opponentUnits);
            }
            System.err.println();
            int linkCount = in.nextInt();
            System.err.println(linkCount);
            for (int i = 0; i < linkCount; i++) {
                int node1 = in.nextInt();
                int node2 = in.nextInt();
                //System.err.println(node1 + " " + node2);
            }
            int triangleCount = in.nextInt();
            System.err.println((triangleCount));
            for (int i = 0; i < triangleCount; i++) {
                int node1 = in.nextInt();
                int node2 = in.nextInt();
                int node3 = in.nextInt();
                int owner = in.nextInt();
                boolean meCanCapture = in.nextInt() != 0;
                boolean opponentCanCapture = in.nextInt() != 0;
                //System.err.println(node1 + " " + node2 + " " + node3 + " " + owner + " " + meCanCapture + " " + opponentCanCapture);
            }
            System.out.println("WAIT");
        }
    }
}
