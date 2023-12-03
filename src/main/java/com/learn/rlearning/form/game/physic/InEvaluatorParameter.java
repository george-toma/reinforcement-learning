package com.learn.rlearning.form.game.physic;

import com.learn.rlearning.form.bot.Bot;
import com.learn.rlearning.form.game.StateStorage;
import com.learn.rlearning.form.game.stage.StateExecutionDetails;

public record InEvaluatorParameter(StateStorage.GameCoordinate gameCoordinate,
                                   StateExecutionDetails stateExecutionDetails,
                                   int rows, int columns, StateStorage.GameCoordinate newGameCoordinate,
                                   Bot bot, StateStorage stateStorage) {
}
