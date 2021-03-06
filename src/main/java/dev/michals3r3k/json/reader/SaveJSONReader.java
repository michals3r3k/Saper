package dev.michals3r3k.json.reader;

import dev.michals3r3k.Logger;
import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.model.board.components.*;
import dev.michals3r3k.model.save.GameTime;
import dev.michals3r3k.model.save.Save;
import dev.michals3r3k.model.save.SaveId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SaveJSONReader extends JSONReader<Save>
{
    private static final DateTimeFormatter DTF =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SaveJSONReader()
    {
        super(new Logger());
    }

    @Override
    protected String getSourceFileName()
    {
        return "saves.json";
    }

    @Override
    public List<Save> read()
    {
        JSONArray jsonSaves = getJSONArray();
        if(jsonSaves == null)
        {
            return Collections.emptyList();
        }
        return ((List<Object>) jsonSaves).stream()
            .map(this::getSave)
            .sorted(Comparator.comparing(Save::getSaveTime).reversed())
            .collect(Collectors.toList());
    }

    private Save getSave(Object object)
    {
        JSONObject jsonSave = (JSONObject) ((JSONObject) object).get("save");
        LocalDateTime saveTime = LocalDateTime.parse(
            (String) jsonSave.get("date"), DTF);
        return new Save(
            getSaveId(jsonSave),
            getFlagQuantity(jsonSave),
            getBoard(jsonSave),
            getGameTime(jsonSave),
            saveTime);
    }

    private SaveId getSaveId(final JSONObject jsonSave)
    {
        JSONObject jsonId = (JSONObject) jsonSave.get("id");
        return new SaveId(
            (Long) jsonId.get("saveId"),
            (String) jsonId.get("username"));
    }

    private Integer getFlagQuantity(JSONObject jsonSave)
    {
        Long flagQuantity = (Long) jsonSave.get("flagQuantity");
        if(flagQuantity == null)
        {
            return null;
        }
        return flagQuantity.intValue();
    }

    private Board getBoard(final JSONObject jsonSave)
    {
        JSONObject jsonBoard = (JSONObject) jsonSave.get("board");
        int bombQuantity = ((Long) jsonBoard.get("bombQuantity")).intValue();
        boolean calculated = (Boolean) jsonBoard.get("calculated");
        Field[][] fields = getFields(jsonBoard);
        return new Board(fields, bombQuantity, calculated);
    }

    private Field[][] getFields(final JSONObject jsonBoard)
    {
        int width = ((Long) jsonBoard.get("width")).intValue();
        int height = ((Long) jsonBoard.get("height")).intValue();
        Field[][] fields = new Field[height][width];
        for(Object rows : (JSONArray) jsonBoard.get("fields"))
        {
            for(Object field : (JSONArray) rows)
            {
                JSONObject jsonField = (JSONObject) field;
                int x = ((Long) jsonField.get("x")).intValue();
                int y = ((Long) jsonField.get("y")).intValue();
                fields[x][y] = getField(jsonField, x ,y);
            }
        }
        return fields;
    }

    private Field getField(final JSONObject jsonField, int x, int y)
    {
        FieldType type = getFieldType((String) jsonField.get("type"));
        FieldStatus status = getFieldStatus((String) jsonField.get("status"));
        if(type == FieldType.BOMB)
        {
            return new BombField(x, y, status);
        }
        if(type == FieldType.REGULAR)
        {
            int value = ((Long) jsonField.get("value")).intValue();
            return new RegularField(x, y, status, value);
        }
        return new EmptyField(x, y, status);
    }

    private FieldStatus getFieldStatus(final String status)
    {
        if(status.equals("COVERED"))
        {
            return FieldStatus.COVERED;
        }
        if(status.equals("UNCOVERED"))
        {
            return FieldStatus.UNCOVERED;
        }
        return FieldStatus.FLAG;
    }

    private FieldType getFieldType(final String type)
    {
        if(type.equals("BOMB"))
        {
            return FieldType.BOMB;
        }
        if(type.equals("EMPTY"))
        {
            return FieldType.EMPTY;
        }
        return FieldType.REGULAR;
    }

    private GameTime getGameTime(final JSONObject jsonSave)
    {
        JSONObject jsonGameTime = (JSONObject) jsonSave.get("gameTime");
        return new GameTime(
            ((Long) jsonGameTime.get("seconds")).intValue(),
            ((Long) jsonGameTime.get("minutes")).intValue(),
            ((Long) jsonGameTime.get("elapsedTime")).intValue());
    }

}
