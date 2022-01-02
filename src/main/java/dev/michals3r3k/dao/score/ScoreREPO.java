package dev.michals3r3k.dao.score;

import dev.michals3r3k.Logger;
import dev.michals3r3k.dao.JSONRepository;
import dev.michals3r3k.json.converter.ScoreJSONConverter;
import dev.michals3r3k.json.reader.ScoreJSONReader;
import dev.michals3r3k.model.score.Score;

public class ScoreREPO extends JSONRepository<Score, Long>
{
    public ScoreREPO()
    {
        super(new ScoreJSONReader(), new ScoreJSONConverter(), new Logger());
    }

    @Override
    protected String getSourceFileName()
    {
        return "scores.json";
    }

    @Override
    protected Long getNextId()
    {
        return jsonReader.read().stream()
            .mapToLong(Score::getId)
            .max()
            .orElse(0L) + 1L;
    }

}
