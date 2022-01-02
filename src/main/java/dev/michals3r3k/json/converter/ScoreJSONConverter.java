package dev.michals3r3k.json.converter;

import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.model.save.GameTime;
import dev.michals3r3k.model.score.Score;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScoreJSONConverter extends JSONConverter<Score, Long>
{
    private static final DateTimeFormatter DTF =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public JSONObject convert(final Score score)
    {
        JSONObject scoreDetails = getJsonScoreDetails(score);

        JSONObject jsonScore = new JSONObject();
        jsonScore.put("score", scoreDetails);
        return jsonScore;
    }

    @Override
    public JSONArray convert(final List<Score> scores)
    {
        JSONArray jsonArray = new JSONArray();
        scores.stream()
            .map(this::convert)
            .forEach(jsonArray::add);
        return jsonArray;
    }

    @Override
    public JSONObject getById(final JSONArray list, final Long id)
    {
        return null;
    }

    private JSONObject getJsonScoreDetails(final Score score)
    {
        JSONObject scoreDetails = new JSONObject();
        scoreDetails.put("gameTime", getJsonGameTime(score.getGameTime()));
        scoreDetails.put("board", getJsonBoard(score.getBoard()));
        scoreDetails.put("date", DTF.format(score.getDate()));
        scoreDetails.put("username", score.getUsername());
        scoreDetails.put("id", score.getId());
        return scoreDetails;
    }

    private JSONObject getJsonGameTime(final GameTime time)
    {
        JSONObject jsonTime = new JSONObject();
        jsonTime.put("minutes", time.getMinutes());
        jsonTime.put("seconds", time.getSeconds());
        jsonTime.put("elapsedTime", time.getElapsedTime());
        return jsonTime;
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
