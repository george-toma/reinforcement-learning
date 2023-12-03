package com.learn.rlearning.form.game;

import lombok.Getter;

@Getter
public enum StateActionType {

    NONE(-1) {
        @Override
        public StateStorage.GameCoordinate newCoordinates(int x, int y) {
            return new StateStorage.GameCoordinate(x, y);
        }
    },
    LEFT(0) {
        @Override
        public StateStorage.GameCoordinate newCoordinates(int x, int y) {
            return new StateStorage.GameCoordinate(x, y - 1);
        }
    },

    RIGHT(1) {
        @Override
        public StateStorage.GameCoordinate newCoordinates(int x, int y) {
            return new StateStorage.GameCoordinate(x, y + 1);
        }
    },

    UP(2) {
        @Override
        public StateStorage.GameCoordinate newCoordinates(int x, int y) {
            return new StateStorage.GameCoordinate(x - 1, y);
        }
    },

    DOWN(3) {
        @Override
        public StateStorage.GameCoordinate newCoordinates(int x, int y) {
            return new StateStorage.GameCoordinate(x + 1, y);
        }
    };
    private final int action;

    public abstract StateStorage.GameCoordinate newCoordinates(int x, int y);

    StateActionType(int action) {
        this.action = action;
    }
}
