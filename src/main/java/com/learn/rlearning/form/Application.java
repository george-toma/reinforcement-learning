package com.learn.rlearning.form;


import com.learn.rlearning.form.game.Game;

public class Application {
    public static void main(String[] args) throws Exception {
        Game game = new Game(4, 4);
        game.run();
    }
}
