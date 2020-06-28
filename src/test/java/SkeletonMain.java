import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class SkeletonMain {
    public static void main(String[] args) {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.setSeed(2728688809873374816L);
        //gameRunner.addAgent(Agent1.class);
        //gameRunner.addAgent(Agent1.class);

        // Another way to add a player
        gameRunner.addAgent("mono /home/eulerschezahl/Documents/Programming/challenges/CodinGame/TryangleCatch/bin/Debug/TryangleCatch.exe");
        gameRunner.addAgent("mono /home/eulerschezahl/Documents/Programming/challenges/CodinGame/TryangleCatch/bin/Debug/TryangleCatch.exe");
        gameRunner.start();
    }
}
