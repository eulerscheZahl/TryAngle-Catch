package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

public class Player extends AbstractMultiplayerPlayer {

    private static int[] colors = {0xff4040, 0x4040ff};

    public static int getColor(int id) {
        return colors[id];
    }

    private static Player[] players = new Player[2];

    public static Player getPlayer(int id) {
        return players[id];
    }

    public static void registerPlayer(Player player) {
        players[player.getIndex()] = player;
    }

    public int getColor() {
        return getColor(this.getIndex());
    }

    @Override
    public int getExpectedOutputLines() {
        return 1;
    }

    public Player getOpponent() {
        return getPlayer((1 + this.getIndex()) % 2);
    }

    public void increaseScore() {
        setScore(getScore() + 1);
    }
}
