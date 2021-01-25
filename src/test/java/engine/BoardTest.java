package engine;

import com.codingame.game.Player;
import com.codingame.gameengine.core.MultiplayerGameManager;
import engine.task.TaskManager;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;
    private TaskManager taskManager = new TaskManager();

    private void populateMap(String nodes, String edges, String units) {
        Player.registerPlayer(new Player(0));
        Player.registerPlayer(new Player(1));
        MultiplayerGameManager gameManager = new MultiplayerGameManager<>();
        Properties parameters = new Properties();
        parameters.put("nodes", nodes);
        parameters.put("edges", edges);
        parameters.put("units", units);
        board = new Board(parameters, new Random(), null, null, null, null, null, null);
    }

    private void createMap() {
        populateMap("500,500_500,700_700,600_900,600_1100,500_1100,700", "0,1_0,2_1,2_2,3_3,4_3,5_4,5", "1,0_1,0_2,0_0,2_0,1_0,1");
    }

    private void createMap2() {
        populateMap("700,400_700,650_950,400_950,650_950,500_700,550", "0,5_0,2_1,3_2,4_4,3_0,4_3,5_1,5", "1,0_0,1_1,0_0,1_1,0_0,1");
    }

    private void createMap3() {
        populateMap("500,500_600,400_600,600_700,500_800,300_800,500", "0,1_0,2_0,3_1,3_2,3_3,4_3,5_4,5", "2,0_2,0_2,0_2,0_0,3_0,3");
    }

    private void playTurn(String player0, String player1) {
        taskManager.parseTasks(Player.getPlayer(0), board, player0, 5);
        taskManager.parseTasks(Player.getPlayer(1), board, player1, 5);
        while (taskManager.hasTasks())
            board.applyActions(taskManager);
        while (board.finalizeTurn(null)) ;
    }

    private void assertWasUsed(Triangle triangle, Player player) {
        assertEquals(null, triangle.getOwner(), "action loses ownership");
        assertEquals(false, triangle.canCapture(player), "can't recapture instantly");
    }

    private void assertCanRecapture(Triangle triangle, Player player) {
        assertEquals(null, triangle.getOwner(), "action loses ownership");
        assertEquals(true, triangle.canCapture(player), "can recapture instantly");
    }

    @Test
    public void testFindTriangles() {
        populateMap("200,200_400,400_0,400_200,300", "0,1_0,2_0,3_1,2_1,3_2,3", "0,0_0,0_0,0_0,0");
        assertEquals(3, board.triangles.size(), "triangle can't have node inside");
    }

    @Test
    public void testMove() {
        createMap();
        playTurn("MOVE 0 4 1;MOVE 1 4 1;MOVE 2 4 1", "");
        assertEquals(3, board.nodes.get(2).units[0], "one leaves, one stays, two come");
        assertEquals(1, board.nodes.get(3).units[0], "pathfinding its way");
        assertEquals(Player.getPlayer(0), board.triangles.get(0).getOwner(), "owner unchanged");
    }

    @Test
    public void testSpawn() {
        createMap();
        playTurn("SPAWN 2 1 0", "");
        assertEquals(3, board.nodes.get(2).units[0], "spawn creates new unit");
        assertWasUsed(board.triangles.get(0), Player.getPlayer(0));
    }

    @Test
    public void testNoSpawn() {
        createMap();
        playTurn("", "SPAWN 2 1 0");
        assertEquals(2, board.nodes.get(2).units[0], "opponent can't spawn with your triangle");
        assertEquals(0, board.nodes.get(2).units[1], "opponent can't spawn with your triangle");
        assertEquals(Player.getPlayer(0), board.triangles.get(0).getOwner(), "owner unchanged");
    }

    @Test
    public void testAttack() {
        createMap();
        board.nodes.get(3).units[0] = 1;
        playTurn("ATTACK 2 1 0 3", "");
        assertEquals(1, board.nodes.get(2).units[0], "attack costs unit");
        assertEquals(1, board.nodes.get(3).units[0], "attack spares own units");
        assertEquals(0, board.nodes.get(3).units[1], "attack kills opponents");
        assertWasUsed(board.triangles.get(0), Player.getPlayer(0));
    }

    @Test
    public void testNoAttack() {
        createMap();
        board.nodes.get(3).units[0] = 1;
        playTurn("ATTACK 2 1 0 4", "");
        assertEquals(Player.getPlayer(0), board.triangles.get(0).getOwner(), "owner unchanged, attack target out of range");
    }

    @Test
    public void testParallelAttack() {
        createMap();
        playTurn("ATTACK 2 1 0 3", "ATTACK 3 4 5 2");
        assertEquals(0, board.nodes.get(3).units[1], "attack killed units");
        assertEquals(0, board.nodes.get(2).units[0], "attack killed units");
        assertCanRecapture(board.triangles.get(0), Player.getPlayer(0));
        assertCanRecapture(board.triangles.get(1), Player.getPlayer(1));
    }

    @Test
    public void testAddEdge() {
        createMap2();
        playTurn("ADD_PATH 4 2 0 5", "");
        assertTrue(board.nodes.get(4).neighbors.contains(board.nodes.get(5)));
        assertCanRecapture(board.triangles.get(0), Player.getPlayer(0));
        assertEquals(4, board.triangles.size(), "created new triangles");
    }

    @Test
    public void testComflictingAddEdge() {
        createMap2();
        playTurn("ADD_PATH 4 2 0 5", "ADD_PATH 3 1 5 0");
        assertTrue(board.nodes.get(4).neighbors.contains(board.nodes.get(5)), "shorter edge gets created");
        assertFalse(board.nodes.get(0).neighbors.contains(board.nodes.get(3)), "longer, crossing edge gets discarded");
        assertCanRecapture(board.triangles.get(0), Player.getPlayer(0));
        assertEquals(Player.getPlayer(1), board.triangles.get(2).getOwner(), "triangle not used");
        assertEquals(4, board.triangles.size(), "created new triangles");

        createMap2();
        playTurn("ADD_PATH 0 2 4 3", "ADD_PATH 5 1 3 4");
        assertTrue(board.nodes.get(4).neighbors.contains(board.nodes.get(5)));
        assertFalse(board.nodes.get(0).neighbors.contains(board.nodes.get(3)));
        assertCanRecapture(board.triangles.get(1), Player.getPlayer(1));
        assertEquals(Player.getPlayer(0), board.triangles.get(0).getOwner(), "triangle not used");
    }

    @Test
    public void testSurroundNode() {
        populateMap("100,100_200,100_100,200", "0,1_0,2_1,2", "1,0_1,0_0,1");
        assertEquals(Player.getPlayer(0), board.triangles.get(0).getOwner(), "triangle captured");
        assertEquals(0, board.nodes.get(2).units[1], "opponent unit got surrounded and died");
    }

    @Test
    public void testIsolatedNode() {
        populateMap("100,100", "", "1,0");
        playTurn("", "");
        assertEquals(1, board.nodes.get(0).units[0], "single nodes don't die because of being surrounded");
    }

    @Test
    public void testDoubleSurrounded() {
        populateMap("100,100_200,200", "0,1", "1,0_0,1");
        playTurn("", "");
        assertEquals(0, board.nodes.get(0).units[0], "units surround each other");
        assertEquals(0, board.nodes.get(1).units[1], "units surround each other");
    }

    @Test
    public void testRemoveEdge() {
        createMap();
        board.nodes.get(3).units[0] = 1;
        playTurn("REMOVE_PATH 2 1 0 3", "");
        assertWasUsed(board.triangles.get(0), Player.getPlayer(0));
        assertTrue(!board.nodes.get(2).neighbors.contains(board.nodes.get(3)), "edge removed");
    }

    @Test
    public void testConcurrentRemoveEdge() {
        createMap();
        board.nodes.get(3).units[0] = 1;
        playTurn("REMOVE_PATH 2 1 0 3", "REMOVE_PATH 3 4 5 2");
        assertWasUsed(board.triangles.get(0), Player.getPlayer(0));
        assertWasUsed(board.triangles.get(1), Player.getPlayer(1));
        assertTrue(!board.nodes.get(2).neighbors.contains(board.nodes.get(3)), "edge removed");
    }

    @Test
    public void testCantUseAfterMove() {
        createMap();
        board.nodes.get(3).units[0] = 1;
        playTurn("MOVE 1 0 1;MOVE 0 1 1;REMOVE_PATH 2 1 0 3", "");
        assertEquals(Player.getPlayer(0), board.triangles.get(0).getOwner(), "can't use if units moved before");
        assertTrue(board.nodes.get(2).neighbors.contains(board.nodes.get(3)), "edge still exists");
    }

    @Test
    public void testDoubleSpending() {
        createMap2();
        playTurn("ATTACK 0 2 4 3;ATTACK 0 2 4 5", "");
        assertCanRecapture(board.triangles.get(0), Player.getPlayer(0));
        assertEquals(0, board.nodes.get(3).units[1], "first attack succeeded");
        assertEquals(1, board.nodes.get(5).units[1], "second attack failed");
    }

    @Test
    public void testSequentialMove() {
        createMap3();
        playTurn("MOVE 3 4 1;MOVE 1 3 2;MOVE 3 5 2", "");
        assertEquals(0, board.nodes.get(1).units[0]);
        assertEquals(2, board.nodes.get(3).units[0]);
        assertEquals(1, board.nodes.get(4).units[0]);
        assertEquals(1, board.nodes.get(5).units[0], "only 1 unit for second MOVE remaining");
    }

    @Test
    public void testSequentialPlan() {
        createMap3();
        playTurn("ATTACK 0 1 3 4;ATTACK 0 2 3 5", "");
        assertEquals(0, board.nodes.get(0).units[0]);
        assertEquals(1, board.nodes.get(1).units[0]);
        assertEquals(0, board.nodes.get(4).units[1]);
        assertEquals(0, board.nodes.get(5).units[1]);
        assertWasUsed(board.triangles.get(0), Player.getPlayer(0));
        assertWasUsed(board.triangles.get(1), Player.getPlayer(0));
    }

    @Test
    public void testFailedPlan() {
        createMap3();
        board.nodes.get(4).units[1] = 6;
        assertEquals(null, board.triangles.get(2).getOwner(), "triangle is neutral");
        playTurn("", "MOVE 4 3 3");
        assertEquals(Player.getPlayer(1), board.triangles.get(2).getOwner(), "triangle was captured");
        playTurn("ATTACK 0 1 3 4;ATTACK 0 2 3 5", "ATTACK 3 4 5 3");
        assertEquals(1, board.nodes.get(1).units[0]);
        assertEquals(2, board.nodes.get(2).units[0]);
        assertEquals(0, board.nodes.get(3).units[0]);
        assertEquals(0, board.nodes.get(4).units[1]);
        assertEquals(2, board.nodes.get(5).units[1]);
        assertWasUsed(board.triangles.get(0), Player.getPlayer(0));
        assertEquals(Player.getPlayer(0), board.triangles.get(1).getOwner(), "triangle could not be used");
    }

    @Test
    public void testIgnoreInvalid() {
        createMap3();
        board.nodes.get(3).units[1] = 3;
        playTurn("", "");
        playTurn("ATTACK 0 1 3 4;ATTACK 0 2 3 5", "ATTACK 3 4 0 3;ATTACK 3 4 5 3"); // second attack of player1 performed at the same time as first attack of player0
        assertEquals(1, board.nodes.get(1).units[0]);
        assertEquals(2, board.nodes.get(2).units[0]);
        assertEquals(0, board.nodes.get(3).units[0]);
        assertEquals(0, board.nodes.get(4).units[1]);
        assertEquals(2, board.nodes.get(5).units[1]);
        assertWasUsed(board.triangles.get(0), Player.getPlayer(0));
        assertEquals(Player.getPlayer(0), board.triangles.get(1).getOwner(), "triangle could not be used");
    }

    @Test
    public void testAddAndRemoveEdge() {
        createMap2();
        playTurn("ADD_PATH 4 2 0 5", "REMOVE_PATH 5 1 3 4");
        assertTrue(board.nodes.get(4).neighbors.contains(board.nodes.get(5)), "edge was added and not removed again");
    }

    @Test
    public void testAlternativeAction() {
        populateMap("500,500_400,600_600,600_400,700_600,700_400,800_600,800", "0,1_0,2_1,2_1,3_1,4_2,4_3,4_3,5_3,6_4,6", "1,0_1,0_1,0_0,3_0,3_0,3_0,3");
        playTurn("REMOVE_PATH 0 1 2 4", "REMOVE_PATH 3 5 6 1;REMOVE_PATH 4 3 6 2;REMOVE_PATH 3 4 6 1");
        assertFalse(board.nodes.get(2).neighbors.contains(board.nodes.get(4)), "player0, action 1");
        assertFalse(board.nodes.get(6).neighbors.contains(board.nodes.get(1)), "player1, action 1");
        assertFalse(board.nodes.get(6).neighbors.contains(board.nodes.get(1)), "player1, action 3 (2 was skipped as done by player0)");
    }
}
