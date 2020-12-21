package engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codingame.game.Player;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.runner.MultiplayerGameRunner;
import engine.task.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.Random;

public class BoardTest {
    private Board board;
    private TaskManager taskManager = new TaskManager();
    private void createMap() {
        Player.registerPlayer(new Player(0));
        Player.registerPlayer(new Player(1));
        MultiplayerGameManager gameManager = new MultiplayerGameManager<>();
        Properties parameters = new Properties();
        parameters.put("nodes", "500,500_500,700_700,600_900,600_1100,500_1100,700");
        parameters.put("edges", "0,1_0,2_1,2_2,3_3,4_3,5_4,5");
        parameters.put("units", "1,0_1,0_2,0_0,2_0,1_0,1");
        board = new Board(parameters, new Random(), null, null, null, null, null);
    }

    private void playTurn(String player0, String player1) {
        taskManager.parseTasks(Player.getPlayer(0), board, player0, 5);
        taskManager.parseTasks(Player.getPlayer(1), board, player1, 5);
        while (taskManager.hasTasks()) board.applyActions(taskManager);
        while (board.finalizeTurn()) ;
    }

    private void assertWasUsed(Triangle triangle, Player player) {
        assertEquals(null, triangle.getOwner(), "action loses ownership");
        assertEquals(false, triangle.canCapture(player), "can't re-capture instantly");
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
        board.nodes.get(3).units[0] = 1;
        playTurn("ATTACK 2 1 0 3", "ATTACK 3 4 5 2");
        assertEquals(0, board.nodes.get(3).units[1], "attack killed units");
        assertEquals(0, board.nodes.get(2).units[0], "attack killed units");
        assertWasUsed(board.triangles.get(0), Player.getPlayer(0));
        assertWasUsed(board.triangles.get(1), Player.getPlayer(1));
    }
}
