package com.learn.rlearning.form.game;

public enum AgentStateVisitType {
    VISITED(1), NOT_VISITED(0);
    private int value;

    AgentStateVisitType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
