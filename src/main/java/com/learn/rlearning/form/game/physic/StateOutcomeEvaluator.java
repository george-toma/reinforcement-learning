package com.learn.rlearning.form.game.physic;

import com.learn.rlearning.form.Conditional;
import com.learn.rlearning.form.game.StateOutcomeType;
import com.learn.rlearning.form.game.physic.condition.ConditionalFeedbackParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class StateOutcomeEvaluator<T> implements Conditional<T, ConditionalFeedbackParameter>, EvaluatorOrder {

    protected abstract StateOutcomeType executeEvaluate(T t, ConditionalFeedbackParameter feedbackParameter);

    public StateOutcomeType doEvaluate(T t) {
        final ConditionalFeedbackParameter feedbackParameter = condition(t);
        if (feedbackParameter.shouldEvaluate()) {
            log.info("Running evaluator [{}, feedbackParameter: {}, " +
                    "inputParam: {}]", getClass().getSimpleName(), feedbackParameter, t);
            return executeEvaluate(t, feedbackParameter);
        }
        return StateOutcomeType.IGNORE;
    }
}
