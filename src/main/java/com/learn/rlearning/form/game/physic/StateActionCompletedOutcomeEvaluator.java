package com.learn.rlearning.form.game.physic;

import com.learn.rlearning.form.game.StateOutcomeType;
import com.learn.rlearning.form.game.StateStorage;
import com.learn.rlearning.form.game.physic.condition.ConditionalFeedbackParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StateActionCompletedOutcomeEvaluator extends StateOutcomeEvaluator<InEvaluatorParameter> {
    @Override
    public StateOutcomeType executeEvaluate(InEvaluatorParameter inEvaluatorParameter, ConditionalFeedbackParameter feedbackParameter) {
        log.info("Form achieved, hooray");
        return StateOutcomeType.COMPLETED_WITH_SUCCESS;
    }

    @Override
    public ConditionalFeedbackParameter condition(InEvaluatorParameter inEvaluatorParameter) {
        StateStorage stateStorage = inEvaluatorParameter.stateStorage();
        return stateStorage.isLineCompleteOnFirstOrLastRow() ? new ConditionalFeedbackParameter(true, 0) :
                new ConditionalFeedbackParameter(false, 0);
    }

    @Override
    public int order() {
        return 3;
    }
}
