package com.learn.rlearning.form.game.physic;

import com.learn.rlearning.form.bot.Bot;
import com.learn.rlearning.form.game.StateActionType;
import com.learn.rlearning.form.game.StateOutcomeType;
import com.learn.rlearning.form.game.StateStorage;
import com.learn.rlearning.form.game.physic.condition.ConditionalFeedbackParameter;
import com.learn.rlearning.form.game.stage.StateExecutionDetails;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StateActionKeepGoingOutcomeEvaluator extends StateOutcomeEvaluator<InEvaluatorParameter> {

    public static final int HIGHEST_REWARD = 1000;

    @Override
    public StateOutcomeType executeEvaluate(InEvaluatorParameter inEvaluatorParameter, ConditionalFeedbackParameter feedbackParameter) {
        Bot bot = inEvaluatorParameter.bot();
        final StateExecutionDetails stateExecutionDetails = inEvaluatorParameter.stateExecutionDetails();

        stateExecutionDetails.setGameStateOutcome(StateOutcomeType.OK_KEEP_GOING);
        stateExecutionDetails.setReward(feedbackParameter.reward());
        bot.updateMove(inEvaluatorParameter.gameCoordinate(), feedbackParameter.reward());
        return StateOutcomeType.OK_KEEP_GOING;
    }

    @Override
    public ConditionalFeedbackParameter condition(InEvaluatorParameter inEvaluatorParameter) {
        StateStorage.GameCoordinate gameCoordinate = inEvaluatorParameter.gameCoordinate();

        final int rowLocation = Math.abs((gameCoordinate.getRow() + 1) - inEvaluatorParameter.rows());
        final int columnLocation = Math.abs((gameCoordinate.getColumn() + 1) - inEvaluatorParameter.columns());

        if ((rowLocation == 0 || rowLocation == inEvaluatorParameter.rows() - 1) ||
                (columnLocation == 0 || columnLocation == inEvaluatorParameter.columns() - 1)) {
            return new ConditionalFeedbackParameter(true, HIGHEST_REWARD);

        } else {
            final int reward = (int) (HIGHEST_REWARD - ((Math.multiplyExact(columnLocation, 10) / 100.0D) * HIGHEST_REWARD));
            return new ConditionalFeedbackParameter(true, reward);
        }
    }

    @Override
    public int order() {
        return 2;
    }
}
