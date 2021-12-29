package dev.michals3r3k.factory;

import dev.michals3r3k.board.Board;
import dev.michals3r3k.board.components.*;
import dev.michals3r3k.user.Save;
import dev.michals3r3k.user.SaveId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SaveDAO
{
    private static DateTimeFormatter DTF =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Save getSaveById(SaveId id)
    {
        return getSaves().stream()
            .filter(s -> id.equals(s.getId()))
            .findAny()
            .orElse(null);
    }

    public List<Save> getSaves()
    {
        JSONArray jsonSaves = getJSONSaves();
        if(jsonSaves == null)
        {
            return Collections.emptyList();
        }
        return ((List<Object>) jsonSaves).stream()
            .map(this::getSave)
            .collect(Collectors.toList());
    }

    JSONArray getJSONSaves()
    {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = null;
        try(FileReader fr = new FileReader("saves.json"))
        {
            jsonArray = (JSONArray) parser.parse(fr);
        } catch(ParseException e)
        {
            e.printStackTrace();
        } catch(IOException e)
        {
            e.printStackTrace();
        }
        return jsonArray == null ? new JSONArray() : jsonArray;
    }

    private Save getSave(Object object)
    {
        JSONObject jsonSave = (JSONObject) ((JSONObject) object).get("save");
        LocalDateTime saveTime = LocalDateTime.parse(
            (String) jsonSave.get("date"), DTF);
        int seconds = ((Long) jsonSave.get("seconds")).intValue();
        int minutes = ((Long) jsonSave.get("minutes")).intValue();
        Board board = getBoard(jsonSave);
        SaveId id = getSaveId(jsonSave);
        return new Save(id, board, minutes, seconds, saveTime);
    }

    private SaveId getSaveId(final JSONObject jsonSave)
    {
        JSONObject jsonId = (JSONObject) jsonSave.get("id");
        long saveId = (Long) jsonId.get("saveId");
        String username = (String) jsonId.get("username");
        return new SaveId(saveId, username);
    }

    private Board getBoard(final JSONObject jsonSave)
    {
        JSONObject jsonBoard = (JSONObject) jsonSave.get("board");
        int bombQuantity = ((Long) jsonBoard.get("bombQuantity")).intValue();
        int width = ((Long) jsonBoard.get("width")).intValue();
        int height = ((Long) jsonBoard.get("height")).intValue();
        Field[][] fields = getFields(jsonBoard, width, height);
        return new Board(fields, bombQuantity);
    }

    private Field[][] getFields(final JSONObject jsonBoard, final int width, final int height)
    {
        Field[][] fields = new Field[width][height];
        for(Object rows : (JSONArray) jsonBoard.get("fields"))
        {
            for(Object field : (JSONArray) rows)
            {
                JSONObject jsonField = (JSONObject) field;
                int x = ((Long) jsonField.get("x")).intValue();
                int y = ((Long) jsonField.get("y")).intValue();
                FieldType type = getFieldType((String) jsonField.get("type"));
                FieldStatus status = getFieldStatus((String) jsonField.get("status"));
                if(type == FieldType.BOMB)
                {
                    fields[x][y] = new BombField(x, y, status);
                } else if(type == FieldType.REGULAR)
                {
                    int value = ((Long) jsonField.get("value")).intValue();
                    fields[x][y] = new RegularField(x, y, status, value);
                } else
                {
                    fields[x][y] = new EmptyField(x, y, status);
                }
            }
        }
        return fields;
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

}
