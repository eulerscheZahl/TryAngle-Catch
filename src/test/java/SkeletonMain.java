import com.codingame.gameengine.runner.MultiplayerGameRunner;

import java.util.Properties;

public class SkeletonMain {
    public static void main(String[] args) {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.setSeed(2728688809873374816L);
        gameRunner.setLeagueLevel(3);
        //Properties parameters = new Properties();
        //parameters.put("nodes", "500,500_500,700_700,600_900,600_1100,500_1100,700");
        //parameters.put("edges", "0,1_0,2_1,2_2,3_3,4_3,5_4,5");
        //parameters.put("units", "1,0_1,0_2,0_0,2_0,1_0,1");
        //gameRunner.setGameParameters(parameters);

        //gameRunner.addAgent(Agent1.class);
        gameRunner.addAgent("mono /home/eulerschezahl/Documents/Programming/challenges/CodinGame/TryangleCatch/bin/Debug/TryangleCatch.exe", "eulerscheZahl", "https://static.codingame.com/servlet/fileservlet?id=43135502422075&format=profile_avatar");
        gameRunner.addAgent("mono /home/eulerschezahl/Documents/Programming/challenges/CodinGame/TryangleCatch/bin/Debug/TryangleCatch.exe", "struct", "https://static.codingame.com/servlet/fileservlet?id=45097182899192&format=profile_avatar");
        gameRunner.start();
    }
}
