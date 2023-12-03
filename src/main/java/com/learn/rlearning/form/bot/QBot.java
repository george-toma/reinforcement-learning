package com.learn.rlearning.form.bot;

import com.github.chen0040.rl.learning.qlearn.QLearner;
import com.learn.rlearning.form.game.Game;
import com.learn.rlearning.form.game.AgentStateVisitType;
import com.learn.rlearning.form.game.StateActionType;
import com.learn.rlearning.form.game.StateStorage;
import com.learn.rlearning.form.game.stage.StateExecutionDetails;

import java.util.Map;

public class QBot extends Bot {

    private final QLearner agent;

    public QBot(Game game, QLearner agent) {
        super(game);
        this.agent = agent;
    }

    @Override
    protected StateActionType selectAction(int state) {
        double qLeft = agent.getModel().getQ(state, StateActionType.LEFT.getAction());
        double qRight = agent.getModel().getQ(state, StateActionType.RIGHT.getAction());
        double qDown = agent.getModel().getQ(state, StateActionType.DOWN.getAction());
        double qUp = agent.getModel().getQ(state, StateActionType.UP.getAction());

        final double max = max(qLeft, qRight, qDown, qUp);
        if (max == qLeft) {
            return StateActionType.LEFT;
        }
        if (max == qRight) {
            return StateActionType.RIGHT;
        }
        if (max == qDown) {
            return StateActionType.DOWN;
        }
        if (max == qUp) {
            return StateActionType.UP;
        }
        return StateActionType.LEFT;
    }

    @Override
    public void updateStrategy() {
        for (Map.Entry<StateStorage.GameCoordinate, BotMove> entry : moves.entrySet()) {
            BotMove move = entry.getValue();
            StateExecutionDetails executionDetails = game.executionDetails(move.getGameCoordinate());
            if (executionDetails == null || executionDetails.getVisitedByAgent() == AgentStateVisitType.NOT_VISITED) {
                agent.update(move.oldState, move.action.getAction(), move.newState, -1000);
            } else {
                agent.update(move.oldState, move.action.getAction(), move.newState, move.getReward());
            }
        }
        reset();
    }

    private double max(final double... qValues) {
        double max = Double.MIN_VALUE;
        for (double qValue : qValues) {
            max = Math.max(max, qValue);
        }
        return max;
    }
}
