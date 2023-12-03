package com.learn.rlearning.form.game.stage;


import com.learn.rlearning.form.game.AgentStateVisitType;
import com.learn.rlearning.form.game.StateActionType;
import com.learn.rlearning.form.game.StateOutcomeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class StateExecutionDetails {
    /**
     * agent state index mapping a 2D space
     */
    private final int stateIndex;
    StateActionType gameStateAction;
    StateOutcomeType gameStateOutcome;
    int reward;
    AgentStateVisitType visitedByAgent = AgentStateVisitType.NOT_VISITED;
}
