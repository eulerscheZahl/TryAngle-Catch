package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import engine.task.InputError;
import view.PlayerView;
import view.modules.TinyToggleModule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Player extends AbstractMultiplayerPlayer {

    private static int[] colors = {0xff4040, 0x4040ff}; // 0xFF1D5C, 0x22A1E4
    private String message = "";
    private int units;
    private int triangles;
    private int nodes;
    private PlayerView view;
    private ArrayList<InputError> errors = new ArrayList<>();

    public Player() { super(); }

    public Player(int index) {
        super();
        this.index = index;
    }

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
        if (this.message.length() > 50) this.message = this.message.substring(0, 50);
    }

    public void initView(GraphicEntityModule graphics, TinyToggleModule toggleModule) {
        view = new PlayerView(this, graphics, toggleModule);
    }

    public void updateView() {
        if (view != null) view.update();
    }

    public int getNodes() {
        return nodes;
    }

    public void setNodes(int nodes) {
        this.nodes = nodes;
    }

    public int getTriangles() {
        return triangles;
    }

    public void setTriangles(int triangles) {
        this.triangles = triangles;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public ArrayList<InputError> popErrors() {
        ArrayList<InputError> result = new ArrayList<>();
        while (errors.size() > 0) {
            InputError error = errors.get(0);
            ArrayList<InputError> errorsOfType = errors.stream().filter(e -> e.getErrorCode() == error.getErrorCode()).collect(Collectors.toCollection(ArrayList::new));
            errors.removeAll(errorsOfType);
            if (errorsOfType.size() <= 3) result.addAll(errorsOfType);
            else {
                result.add(errorsOfType.get(0));
                result.add(errorsOfType.get(1));
                result.add(new InputError((errorsOfType.size() - 2) + " more errors of that type", error.getErrorCode(), error.isCritical()));
            }
        }
        return result;
    }

    public void addError(InputError error) {
        errors.add(error);
    }
}
