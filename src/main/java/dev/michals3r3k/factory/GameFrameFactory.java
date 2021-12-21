package dev.michals3r3k.factory;

import dev.michals3r3k.frame.game.GameFrame;

public class GameFrameFactory {
    private final BoardFactory boardFactory;

    public GameFrameFactory(final BoardFactory boardFactory)
    {
        this.boardFactory = boardFactory;
    }

    public GameFrame getGameFrame(){
        return null;
    }

}
