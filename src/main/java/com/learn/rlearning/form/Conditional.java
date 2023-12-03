package com.learn.rlearning.form;

public interface Conditional<T,S> {
    S condition(T t);
}
