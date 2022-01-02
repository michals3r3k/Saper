package dev.michals3r3k.dao;

import dev.michals3r3k.Logger;
import dev.michals3r3k.frame.game.Score;
import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.model.save.GameTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ScoreREPO
{
    private static final DateTimeFormatter DTF =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Logger logger = new Logger();
    private final ScoreDAO scoreDAO = new ScoreDAO();

    public void save(final Score score)
    {
        JSONArray scores = scoreDAO.getJSONScores();
        scores.add(convertToJson(score));
        write(scores);
        logger.info("New score has been saved");
    }

    private JSONObject convertToJson(final Score score)
    {
        JSONObject scoreDetails = new JSONObject();
        scoreDetails.put("gameTime", getJsonGameTime(score.getGameTime()));
        scoreDetails.put("board", getJsonBoard(score.getBoard()));
        scoreDetails.put("date", DTF.format(score.getDate()));
        scoreDetails.put("username", score.getUsername());

        JSONObject jsonScore = new JSONObject();
        jsonScore.put("score", scoreDetails);
        return jsonScore;
    }

    private JSONObject getJsonGameTime(final GameTime time)
    {
        JSONObject jsonTime = new JSONObject();
        jsonTime.put("minutes", time.getMinutes());
        jsonTime.put("seconds", time.getSeconds());
        jsonTime.put("elapsedTime", time.getElapsedTime());
        return jsonTime;
    }

    private void write(JSONArray jsonArray)
    {
        try(FileWriter fw = new FileWriter("scores.json"))
        {
            fw.write(jsonArray.toJSONString());
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private JSONObject getJsonBoard(Board board)
    {
        JSONObject boardDetails = new JSONObject();
        boardDetails.put("bombQuantity", board.getBombQuantity());
        boardDetails.put("width", board.getWidth());
        boardDetails.put("height", board.getHeight());
        return boardDetails;
    }

}
