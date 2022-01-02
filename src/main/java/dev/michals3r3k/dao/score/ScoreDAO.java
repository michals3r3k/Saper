package dev.michals3r3k.dao.score;

import dev.michals3r3k.dao.AbstractDAO;
import dev.michals3r3k.json.reader.ScoreJSONReader;
import dev.michals3r3k.model.score.Score;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreDAO extends AbstractDAO<Score, Long>
{
    public ScoreDAO()
    {
        super(new ScoreJSONReader());
    }

    @Override
    public List<Score> getList()
    {
        return super.getList().stream()
            .sorted(Comparator.comparing(Score::getDate).reversed())
            .collect(Collectors.toList());
    }

}
