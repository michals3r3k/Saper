package dev.michals3r3k.dao.score;

import dev.michals3r3k.Logger;
import dev.michals3r3k.frame.game.Score;
import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.model.save.GameTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreDAO
{
    private static final String SCORE_FILENAME = "scores.json";
    private static final DateTimeFormatter DTF =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Logger logger = new Logger();

    public List<Score> getScores()
    {
        JSONArray jsonScores = getJSONScores();
        if(jsonScores == null)
        {
            return Collections.emptyList();
        }
        return ((List<Object>) jsonScores).stream()
            .map(this::getScore)
            .sorted(Comparator.comparing(Score::getDate).reversed())
            .collect(Collectors.toList());
    }

    JSONArray getJSONScores()
    {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = null;
        try(FileReader fr = new FileReader(SCORE_FILENAME))
        {
            jsonArray = (JSONArray) parser.parse(fr);
        } catch(ParseException e)
        {
            logger.warn(SCORE_FILENAME + " is empty");
        } catch(IOException e)
        {
            e.printStackTrace();
        }
        return jsonArray == null ? new JSONArray() : jsonArray;
    }

    private Score getScore(Object object)
    {
        JSONObject jsonScore = (JSONObject) ((JSONObject) object).get("score");
        LocalDateTime date = LocalDateTime.parse(
            (String) jsonScore.get("date"), DTF);
        String username = (String) jsonScore.get("username");
        GameTime gameTime = getGameTime(jsonScore);
        Board board = getBoard(jsonScore);

        return new Score(date, username, gameTime, board);
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
