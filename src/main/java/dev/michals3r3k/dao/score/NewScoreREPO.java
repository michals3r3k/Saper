package dev.michals3r3k.dao.score;

import dev.michals3r3k.Logger;
import dev.michals3r3k.dao.JSONRepository;
import dev.michals3r3k.json.converter.ScoreJSONConverter;
import dev.michals3r3k.json.reader.ScoreJSONReader;
import dev.michals3r3k.model.score.Score;

public class NewScoreREPO extends JSONRepository<Score, Long>
{
    protected NewScoreREPO()
    {
        super(new ScoreJSONReader(), new ScoreJSONConverter(), new Logger());
    }

    @Override
    protected String getSourceFileName()
    {
        return "scores.json";
    }

}
