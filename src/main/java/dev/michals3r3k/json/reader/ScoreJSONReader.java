package dev.michals3r3k.json.reader;

import dev.michals3r3k.Logger;
import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.model.save.GameTime;
import dev.michals3r3k.model.score.Score;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreJSONReader extends JSONReader<Score>
{
    private static final DateTimeFormatter DTF =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ScoreJSONReader()
    {
        super(new Logger());
    }

    @Override
    protected String getSourceFileName()
    {
        return "scores.json";
    }

    @Override
    public List<Score> read()
    {
        JSONArray jsonScores = getJSONArray();
        if(jsonScores == null)
        {
            return Collections.emptyList();
        }
        return ((List<Object>) jsonScores).stream()
            .map(this::getScore)
            .sorted(Comparator.comparing(Score::getDate).reversed())
            .collect(Collectors.toList());
    }

    private Score getScore(Object object)
    {
        JSONObject jsonScore = (JSONObject) ((JSONObject) object).get("score");
        LocalDateTime date = LocalDateTime.parse(
            (String) jsonScore.get("date"), DTF);
        Long id = (Long) jsonScore.get("id");
        String username = (String) jsonScore.get("username");
        GameTime gameTime = getGameTime(jsonScore);
        Board board = getBoard(jsonScore);
        return new Score(id, date, username, gameTime, board);
    }

    private GameTime getGameTime(final JSONObject jsonScore)
    {
        JSONObject gameTime = (JSONObject) jsonScore.get("gameTime");
        int minutes = ((Long) gameTime.get("minutes")).intValue();
        int seconds = ((Long) gameTime.get("seconds")).intValue();
        int elapsedTime = ((Long) gameTime.get("elapsedTime")).intValue();
        return new GameTime(seconds, minutes, elapsedTime);
    }

    private Board getBoard(final JSONObject jsonSave)
    {
        JSONObject jsonBoard = (JSONObject) jsonSave.get("board");
        int bombQuantity = ((Long) jsonBoard.get("bombQuantity")).intValue();
        int width = ((Long) jsonBoard.get("width")).intValue();
        int height = ((Long) jsonBoard.get("height")).intValue();
        return new Board(width, height, bombQuantity);
    }

}
