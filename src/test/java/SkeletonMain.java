import com.codingame.gameengine.runner.MultiplayerGameRunner;

import java.util.Properties;

public class SkeletonMain {
    public static void main(String[] args) {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.setSeed(2728688809873374816L);
        gameRunner.setLeagueLevel(3);
        Properties parameters = new Properties();
        //parameters.put("nodes", "700,400_700,650_950,400_950,650_950,500_700,550");
        //parameters.put("edges", "0,5_0,2_1,3_2,4_4,3_0,4_3,5_1,5");
        //parameters.put("units", "1,0_0,1_1,0_0,1_1,0_0,1");
        //gameRunner.setGameParameters(parameters);

        //gameRunner.addAgent(Agent1.class);
        gameRunner.addAgent("mono /home/eulerschezahl/Documents/Programming/challenges/CodinGame/TryangleCatch/bin/Debug/TryangleCatch.exe", "eulerscheZahl", "https://static.codingame.com/servlet/fileservlet?id=43135502422075&format=profile_avatar");
        gameRunner.addAgent("mono /home/eulerschezahl/Documents/Programming/challenges/CodinGame/TryangleCatch/bin/Debug/TryangleCatch.exe", "struct", "https://static.codingame.com/servlet/fileservlet?id=61418101984360&format=profile_avatar");
        gameRunner.start();
    }
}
