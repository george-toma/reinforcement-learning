package com.learn.rlearning.form.game.physic;

import com.learn.rlearning.form.bot.Bot;
import com.learn.rlearning.form.game.StateActionType;
import com.learn.rlearning.form.game.StateOutcomeType;
import com.learn.rlearning.form.game.StateStorage;
import com.learn.rlearning.form.game.physic.condition.ConditionalFeedbackParameter;
import com.learn.rlearning.form.game.stage.StateExecutionDetails;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StateActionCrashedOutcomeEvaluator extends StateOutcomeEvaluator<InEvaluatorParameter> {
    private static final ConditionalFeedbackParameter CRASHED_FEEDBACK = new ConditionalFeedbackParameter(true, -1000);
    private static final ConditionalFeedbackParameter NOT_CRASHED_FEEDBACK = new ConditionalFeedbackParameter(false, 0);

    @Override
    public StateOutcomeType executeEvaluate(InEvaluatorParameter inEvaluatorParameter, ConditionalFeedbackParameter feedbackParameter) {
        log.warn("Agent crashed [{}]", inEvaluatorParameter.gameCoordinate());
        Bot bot = inEvaluatorParameter.bot();
        final StateExecutionDetails stateExecutionDetails = inEvaluatorParameter.stateExecutionDetails();
        stateExecutionDetails.setGameStateOutcome(StateOutcomeType.CRASHED);
        stateExecutionDetails.setReward(feedbackParameter.reward());
        bot.updateMove(inEvaluatorParameter.gameCoordinate(), stateExecutionDetails.getReward());
        bot.updateStrategy();
        return StateOutcomeType.CRASHED;
    }

    @Override
    public ConditionalFeedbackParameter condition(InEvaluatorParameter inEvaluatorParameter) {
        final StateActionType gameStateAction = inEvaluatorParameter.stateExecutionDetails().getGameStateAction();
        StateStorage.GameCoordinate gameCoordinate = inEvaluatorParameter.gameCoordinate();
        StateStorage.GameCoordinate newAgentGameCoordinate = inEvaluatorParameter.newGameCoordinate();

        if (gameStateAction.equals(StateActionType.NONE)) {
            log.warn("Agent went outside of environment [{}] ", gameCoordinate);
            return CRASHED_FEEDBACK;
        } else if (gameCoordinate.isCoordinateOutsideOfEnvironment(inEvaluatorParameter.rows(),
                inEvaluatorParameter.columns())) {
            return CRASHED_FEEDBACK;
        }
        return NOT_CRASHED_FEEDBACK;
    }

    @Override
    public int order() {
        return 1;
    }
}
