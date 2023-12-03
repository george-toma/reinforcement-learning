package com.learn.rlearning.form.game;

import com.github.chen0040.rl.learning.qlearn.QLearner;
import com.learn.rlearning.form.bot.Bot;
import com.learn.rlearning.form.bot.QBot;
import com.learn.rlearning.form.game.physic.*;
import com.learn.rlearning.form.game.stage.StageType;
import com.learn.rlearning.form.game.stage.StateExecutionDetails;
import lombok.extern.slf4j.Slf4j;


import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Game {
    public static final int ACTIONS_COUNT = StateActionType.values().length;

    private final QLearner learner;

    private Bot bot;
    private final StateStorage stateStorage;
    private StageType gameStage = StageType.PENDING;
    private int episode = 1;
    private final int rows, columns;
    private Random random = new Random();

    public Game(int rows, int columns) {
        stateStorage = new StateStorage(rows, columns);
        learner = new QLearner(stateStorage.count(), ACTIONS_COUNT - 1);
        learner.getModel().setAlpha(0.7);
        learner.getModel().setGamma(1.0);
        this.rows = rows;
        this.columns = columns;
    }

    public void run() throws Exception {
        start();
        List<StateOutcomeEvaluator> evaluators = getEvaluators();
        StateStorage.GameCoordinate gameCoordinates = createRandomAgentPosition();
        while (gameStage == StageType.IN_PROGRESS) {
            TimeUnit.SECONDS.sleep(2);
            log.info("Running episode [{}] for coordinate [{}]", +episode, gameCoordinates);
            StateExecutionDetails executionDetails = bot.act(gameCoordinates);
            stateStorage.visitState(gameCoordinates);
            StateActionType gameStateAction = executionDetails.getGameStateAction();
            StateStorage.GameCoordinate newAgentGameCoordinate = gameStateAction.newCoordinates(gameCoordinates.getRow(), gameCoordinates.getColumn());
            log.info("Agent took decision[{}] with new coordinates [{}]", gameStateAction, newAgentGameCoordinate);
            for (StateOutcomeEvaluator evaluator : evaluators) {
                StateOutcomeType stateOutcomeType = evaluator.doEvaluate(new InEvaluatorParameter(gameCoordinates,
                        executionDetails, rows, columns, newAgentGameCoordinate, bot, stateStorage));
                if (stateOutcomeType != StateOutcomeType.IGNORE) {
                    stateStorage.printEnvironment();
                    switch (stateOutcomeType) {
                        case COMPLETED_WITH_SUCCESS -> {
                            stop();
                            break;
                        }
                        case CRASHED -> {
                            episode++;
                            stateStorage.populateWithDefaultState(rows, columns);
                            gameCoordinates = createRandomAgentPosition();
                            break;
                        }
                        case OK_KEEP_GOING -> {
                            gameCoordinates = newAgentGameCoordinate;
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    private List<StateOutcomeEvaluator> getEvaluators() {
        List<StateOutcomeEvaluator> evaluators = List.of(new StateActionCrashedOutcomeEvaluator(),
                new StateActionCompletedOutcomeEvaluator(),
                new StateActionKeepGoingOutcomeEvaluator());
        return evaluators;
    }

    private StateStorage.GameCoordinate createRandomAgentPosition() {
        StateStorage.GameCoordinate gameCoordinates = new StateStorage.GameCoordinate(random.nextInt(rows),
                random.nextInt(columns));
        return gameCoordinates;
    }

    public void stop() {
        gameStage = StageType.FINISHED;
    }

    private void start() {
        bot = new QBot(this, learner);
        gameStage = StageType.IN_PROGRESS;
    }

    public StateExecutionDetails executionDetails(StateStorage.GameCoordinate gameCoordinate) {
        return stateStorage.getExecution(gameCoordinate);
    }

    public StateExecutionDetails mapState(StateStorage.GameCoordinate gameCoordinate) {
        return stateStorage.getExecution(gameCoordinate);
    }

}
