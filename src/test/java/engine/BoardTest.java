package engine;

import com.codingame.game.Player;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.runner.MultiplayerGameRunner;
import engine.task.TaskManager;
import org.junit.jupiter.api.BeforeEach;
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
        board = new Board(parameters, new Random(), null, null, null, null, null);
    }

    private void createMap() {
        populateMap("500,500_500,700_700,600_900,600_1100,500_1100,700", "0,1_0,2_1,2_2,3_3,4_3,5_4,5", "1,0_1,0_2,0_0,2_0,1_0,1");
    }

    private void createMap2() {
        populateMap( "700,400_700,650_950,400_950,650_950,500_700,550", "0,5_0,2_1,3_2,4_4,3_0,4_3,5_1,5", "1,0_0,1_1,0_0,1_1,0_0,1");
    }

    private void playTurn(String player0, String player1) {
        taskManager.parseTasks(Player.getPlayer(0), board, player0, 5);
        taskManager.parseTasks(Player.getPlayer(1), board, player1, 5);
        while (taskManager.hasTasks()) board.applyActions(taskManager);
        while (board.finalizeTurn()) ;
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
        playTurn("ADD_EDGE 4 2 0 5", "");
        assertTrue(board.nodes.get(4).neighbors.contains(board.nodes.get(5)));
        assertCanRecapture(board.triangles.get(0), Player.getPlayer(0));
        assertEquals(4, board.triangles.size(), "created new triangles");
    }

    @Test
    public void testComflictingAddEdge() {
        createMap2();
        playTurn("ADD_EDGE 4 2 0 5", "ADD_EDGE 3 1 5 0");
        assertTrue(board.nodes.get(4).neighbors.contains(board.nodes.get(5)), "shorter edge gets created");
        assertFalse(board.nodes.get(0).neighbors.contains(board.nodes.get(3)), "longer, crossing edge gets discarded");
        assertCanRecapture(board.triangles.get(0), Player.getPlayer(0));
        assertEquals(Player.getPlayer(1), board.triangles.get(2).getOwner(), "triangle not used");

        createMap2();
        playTurn("ADD_EDGE 0 2 4 3", "ADD_EDGE 5 1 3 4");
        assertTrue(board.nodes.get(4).neighbors.contains(board.nodes.get(5)));
        assertFalse(board.nodes.get(0).neighbors.contains(board.nodes.get(3)));
        assertCanRecapture(board.triangles.get(1), Player.getPlayer(1));
        assertEquals(Player.getPlayer(0), board.triangles.get(0).getOwner(), "triangle not used");
    }

    @Test
    public void surroundNode() {
        populateMap( "100,100_200,100_100,200", "0,1_0,2_1,2", "1,0_1,0_0,1");
        assertEquals(Player.getPlayer(0), board.triangles.get(0).getOwner(), "triangle captured");
        assertEquals(0, board.nodes.get(2).units[1], "opponent unit got surrounded and died");
    }
}