package dev.michals3r3k.frame.game;

import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.model.board.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TilePanel extends JPanel
{
    Tile[][] tilePanels;
    Board board;
    boolean canPlay;

    TilePanel(
        Board board,
        GameFrame gameFrame)
    {
        this.canPlay = true;
        int rows = board.getHeight();
        int cols = board.getWidth();
        this.tilePanels = new Tile[rows][cols];
        this.setLayout(new GridLayout(rows, cols));
        this.setOpaque(false);
        this.board = board;
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                tilePanels[i][j] = new Tile(this.board, new Position(i, j),
                    this, gameFrame);
                this.add(tilePanels[i][j]);
            }
        }
    }

    void uncoverBombs(Position position)
    {
        int currX = position.getX();
        int currY = position.getY();
        Field[][] fields = board.getFields();
        for(int i = 0; i < fields.length; i++)
        {
            for(int j = 0; j < fields[i].length; j++)
            {
                Tile tile = getTile(i, j);
                if(tile.getFieldType() == FieldType.BOMB && !tile.isFlag())
                {
                    if(currX != i || currY != j)
                    {
                        tile.setStatus(FieldStatus.UNCOVERED);
                        tile.setBackground(Color.DARK_GRAY);
                    }
                    tile.setText("B");
                }
            }
        }
    }

    public Tile getTile(int x, int y)
    {
        return tilePanels[x][y];
    }

    public void uncoverEmptyNeighbours(int x, int y)
    {
        List<Tile> neighbourTiles = getNeighbourTiles(x, y);
        neighbourTiles.forEach(t -> {
            t.setBackground(Color.DARK_GRAY);
            t.setStatus(FieldStatus.UNCOVERED);
            if(t.getFieldType() == FieldType.REGULAR)
            {
                t.setValue(((RegularField) board.getField(t.getPosX(), t.getPosY())).getValue());
            }
        });
    }

    private List<Tile> getNeighbourTiles(int x, int y)
    {
        Tile tile = getTile(x, y);
        List<Tile> tiles = new ArrayList<>();
        tiles.add(tile);
        return getNeighbourTiles(tiles, 0);
    }

    private List<Tile> getNeighbourTiles(List<Tile> result, int index)
    {
        if(index == result.size())
        {
            return result;
        }
        Tile tile = result.get(index);
        int x = tile.getPosX();
        int y = tile.getPosY();
        if(isRegularField(x, y))
        {
            return getNeighbourTiles(result, ++index);
        }
        if(x - 1 >= 0 && y - 1 >= 0 && isNotBomb(x - 1, y - 1)) //left-top
        {
            Tile leftTop = getTile(x - 1, y - 1);
            if(!result.contains(leftTop))
            {
                result.add(leftTop);
            }
        }
        if(x - 1 >= 0 && isNotBomb(x - 1, y)) //top
        {
            Tile top = getTile(x - 1, y);
            if(!result.contains(top))
            {
                result.add(top);
            }
        }
        Field[][] fields = board.getFields();
        int colSize = fields[x].length;
        if(x - 1 >= 0 && y + 1 < colSize && isNotBomb(x - 1, y + 1)) //top-right
        {
            Tile rightTop = getTile(x - 1, y + 1);
            if(!result.contains(rightTop))
            {
                result.add(rightTop);
            }
        }
        if(y - 1 >= 0 && isNotBomb(x, y - 1)) //left
        {
            Tile left = getTile(x, y - 1);
            if(!result.contains(left))
            {
                result.add(left);
            }
        }
        if(y + 1 < colSize && isNotBomb(x, y + 1)) //right
        {
            Tile right = getTile(x, y + 1);
            if(!result.contains(right))
            {
                result.add(right);
            }
        }
        int rowSize = fields.length;
        if(x + 1 < rowSize && y - 1 >= 0 && isNotBomb(x + 1, y - 1)) //left-bottom
        {
            Tile leftBottom = getTile(x + 1, y - 1);
            if(!result.contains(leftBottom))
            {
                result.add(leftBottom);
            }
        }
        if(x + 1 < rowSize && isNotBomb(x + 1, y)) //bottom
        {
            Tile bottom = getTile(x + 1, y);
            if(!result.contains(bottom))
            {
                result.add(bottom);
            }
        }
        if(x + 1 < rowSize && y + 1 < colSize && isNotBomb(x + 1, y + 1)) //bottom right
        {
            Tile rightBottom = getTile(x + 1, y + 1);
            if(!result.contains(rightBottom))
            {
                result.add(rightBottom);
            }
        }
        return getNeighbourTiles(result, ++index);
    }

    private boolean isRegularField(int x, int y)
    {
        return board.getField(x, y).getFieldType() == FieldType.REGULAR;
    }

    public boolean isCanPlay()
    {
        return canPlay;
    }

    public void setCanPlay(boolean canPlay)
    {
        this.canPlay = canPlay;
    }

    public boolean isAllNonBombsUncovered()
    {
        return !Arrays.asList(this.tilePanels)
            .stream()
            .map(Arrays::asList)
            .flatMap(Collection::stream)
            .filter(this::isNotBomb)
            .anyMatch(Tile::isVisible);
    }

    private boolean isNotBomb(Tile tile)
    {
        return isNotBomb(tile.getPosX(), tile.getPosY());
    }

    private boolean isNotBomb(int x, int y)
    {
        FieldType fieldType = board.getField(x, y).getFieldType();
        return  fieldType != FieldType.BOMB;
    }

}
