package dev.michals3r3k.frame.menu;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameParams
{
    public static final int TILE_SIZE = 25;
    public static final Map<Integer, Color> TILE_VALUE_COLOR;
    static {
        TILE_VALUE_COLOR = new HashMap<>();
        TILE_VALUE_COLOR.put(1, new Color(51,51,255));
        TILE_VALUE_COLOR.put(2, new Color(0,204,0));
        TILE_VALUE_COLOR.put(3, new Color(255,0,0));
        TILE_VALUE_COLOR.put(4, new Color(204,0,204));
        TILE_VALUE_COLOR.put(5, new Color(255,51,153));
        TILE_VALUE_COLOR.put(6, new Color(102,0,51));
        TILE_VALUE_COLOR.put(7, new Color(178,102,255));
        TILE_VALUE_COLOR.put(8, new Color(0,51,102));
    }

    public static final String APP_TITLE = "Saper by Michal Serek";
    public static final String EASY = "Easy";
    public static final String MEDIUM = "Medium";
    public static final String HARD = "Hard";
    public static final String[] DIFFICULTIES = new String[]{EASY, MEDIUM, HARD};
    public static final Map<String, Integer> DIFFICULTY_MAP;
    static {
        DIFFICULTY_MAP = new HashMap<>();
        DIFFICULTY_MAP.put(EASY, 12);
        DIFFICULTY_MAP.put(MEDIUM, 25);
        DIFFICULTY_MAP.put(HARD, 50);
    }

    private GameParams(){}
}
