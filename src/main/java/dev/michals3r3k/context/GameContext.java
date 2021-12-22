package dev.michals3r3k.context;

import dev.michals3r3k.frame.game.BoardPanel;
import dev.michals3r3k.frame.game.TilePanel;

public class GameContext
{
    public GameContext getGameContext(Context context)
    {
        return (GameContext) context.getParameter(GameContext.class);
    }

    private BoardPanel boardPanel;
    private TilePanel tilePanel;

    public GameContext(BoardPanel boardPanel, TilePanel tilePanel)
    {
        this.boardPanel = boardPanel;
        this.tilePanel = tilePanel;
    }

    public BoardPanel getBoardPanel()
    {
        return boardPanel;
    }

    public TilePanel getTilePanel()
    {
        return tilePanel;
    }

}
