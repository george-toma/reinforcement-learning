package com.learn.rlearning.form.bot;


import com.learn.rlearning.form.game.Game;
import com.learn.rlearning.form.game.AgentStateVisitType;
import com.learn.rlearning.form.game.StateActionType;
import com.learn.rlearning.form.game.StateStorage;
import com.learn.rlearning.form.game.stage.StateExecutionDetails;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class Bot {

    protected int lastState;
    protected StateActionType lastAction = StateActionType.NONE;
    protected Map<StateStorage.GameCoordinate, BotMove> moves = new HashMap<>();
    protected final Game game;

    public abstract void updateStrategy();

    public StateExecutionDetails act(StateStorage.GameCoordinate gameCoordinate) {
        StateExecutionDetails stateExecutionDetails = game.mapState(gameCoordinate);
        final int currentState = stateExecutionDetails.getStateIndex();
        StateActionType action = selectAction(currentState);
        moves.put(gameCoordinate, new BotMove(lastState, lastAction, currentState, stateExecutionDetails.getReward(), gameCoordinate));

        lastState = currentState;
        lastAction = action;
        stateExecutionDetails.setGameStateAction(action);
        stateExecutionDetails.setVisitedByAgent(AgentStateVisitType.VISITED);

        return stateExecutionDetails;
    }

    public void updateMove(StateStorage.GameCoordinate gameCoordinate, double reward) {
        BotMove move = moves.get(gameCoordinate);
        if (move == null) {
            return;
        }
        move.setReward(reward);
    }

    void reset() {
        lastState = 0;
        lastAction = StateActionType.NONE;
        moves.clear();
    }

    protected abstract StateActionType selectAction(int state);
}
