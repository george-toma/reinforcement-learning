package com.learn.rlearning.form.game;

import com.learn.rlearning.form.game.stage.StateExecutionDetails;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class StateStorage {
    private final int rows, columns;
    private Map<GameCoordinate, StateExecutionDetails> gameState = new LinkedHashMap<>();
    private AtomicInteger stateCounter = new AtomicInteger(0);
    private int visitState[][];

    public StateStorage(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        visitState = new int[rows][columns];
        populateWithDefaultState(rows, columns);
    }

    public void populateWithDefaultState(int rows, int columns) {
        gameState.clear();
        stateCounter.set(0);
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                visitState[i][j] = 0;
                gameState.put(new GameCoordinate(i, j),
                        new StateExecutionDetails(stateCounter.incrementAndGet()));
            }
        }
    }

    public int count() {
        return rows * columns;
    }

    public StateExecutionDetails getExecution(GameCoordinate gameCoordinate) {
        return gameState.getOrDefault(gameCoordinate, getOutEnvironmentState(gameCoordinate));
    }

    public void visitState(GameCoordinate gameCoordinate) {
        if (!gameCoordinate.isCoordinateOutsideOfEnvironment(rows, columns)) {
            visitState[gameCoordinate.getRow()][gameCoordinate.getColumn()] = 1;
        }
    }

    public boolean isLineCompleteOnFirstOrLastRow() {
        boolean isComplete = true;
        List<Integer> columnsToIterate = IntStream.rangeClosed(0, columns - 1).boxed().collect(Collectors.toList());
        for (int i : Arrays.asList(0, rows - 1)) {
            isComplete = true;
            for (int j : columnsToIterate) {
                if (visitState[i][j] != AgentStateVisitType.VISITED.getValue()) {
                    isComplete = false;
                }
            }
            if (isComplete) {
                break;
            }
        }
        return isComplete;
    }

    public void printEnvironment() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(" " + visitState[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    @EqualsAndHashCode
    @ToString
    public static class GameCoordinate {
        private int row, column;

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }


        public GameCoordinate(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public boolean isCoordinateOutsideOfEnvironment(int rows, int columns) {
            if (row < 0 || column < 0) {
                return true;
            }
            if (row >= rows || column >= columns) {
                return true;
            }
            return false;
        }
    }

    private StateExecutionDetails getOutEnvironmentState(GameCoordinate gameCoordinate) {
        StateExecutionDetails outsideGameExecution = new StateExecutionDetails(-1);
        gameState.put(gameCoordinate, outsideGameExecution);

        return outsideGameExecution;
    }
}
