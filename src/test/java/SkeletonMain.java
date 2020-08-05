import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class SkeletonMain {
    public static void main(String[] args) {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.setSeed(2728688809873374816L);
        gameRunner.setLeagueLevel(3);

        //gameRunner.addAgent(Agent1.class);
        gameRunner.addAgent("mono /home/eulerschezahl/Documents/Programming/challenges/CodinGame/TryangleCatch/bin/Debug/TryangleCatch.exe", "eulerscheZahl", "https://static.codingame.com/servlet/fileservlet?id=43135502422075&format=profile_avatar");
        gameRunner.addAgent("mono /home/eulerschezahl/Documents/Programming/challenges/CodinGame/TryangleCatch/bin/Debug/boss.exe", "struct", "https://static.codingame.com/servlet/fileservlet?id=45097182899192&format=profile_avatar");
        gameRunner.start();
    }
}
