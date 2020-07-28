package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import engine.task.InputError;
import view.PlayerView;

import java.util.ArrayList;

public class Player extends AbstractMultiplayerPlayer {

    private static int[] colors = {0xff4040, 0x4040ff}; // 0xFF1D5C, 0x22A1E4
    private String message;
    private PlayerView view;
    private ArrayList<InputError> errors = new ArrayList<>();

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        if (this.message.length() > 30) this.message = this.message.substring(0, 30);
    }

    public void initView(GraphicEntityModule graphics) {
        view = new PlayerView(this, graphics);
    }

    public void updateMessage() {
        view.updateMessage();
    }

    public void updateScore() {
        view.updateScore();
    }

    public ArrayList<InputError> popErrors() {
        ArrayList<InputError> result = errors;
        errors = new ArrayList<>();
        return result;
    }

    public void addError(InputError error) {
        errors.add(error);
    }
}
