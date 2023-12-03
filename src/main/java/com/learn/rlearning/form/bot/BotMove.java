package com.learn.rlearning.form.bot;

import com.learn.rlearning.form.game.StateActionType;
import com.learn.rlearning.form.game.StateStorage;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BotMove {
    public int oldState;
    public StateActionType action;
    public int newState;
    @Setter
    public double reward;
    public StateStorage.GameCoordinate gameCoordinate;

    public BotMove(int oldState, StateActionType action, int newState, double reward, StateStorage.GameCoordinate gameCoordinate) {
        this.oldState = oldState;
        this.action = action;
        this.newState = newState;
        this.reward = reward;
        this.gameCoordinate = gameCoordinate;
    }
}
