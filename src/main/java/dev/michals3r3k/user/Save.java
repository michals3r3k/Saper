package dev.michals3r3k.user;

import dev.michals3r3k.board.Board;
import dev.michals3r3k.board.components.*;
import dev.michals3r3k.context.Context;
import dev.michals3r3k.context.UserContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Save implements Saveable
{
    private SaveId id;
    private Board board;
    private int minutes;
    private int seconds;

    public Save(
        final SaveId id,
        final Board board,
        final int minutes,
        final int seconds)
    {
        this.id = id;
        this.board = board;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public SaveId getId()
    {
        if(id == null || id.getSaveId() == null || id.getUsername() == null)
        {
            return null;
        }
        return id;
    }

    public Board getBoard()
    {
        return board;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public int getSeconds()
    {
        return seconds;
    }

    @Override
    public void save()
    {
        JSONObject saveDetails = new JSONObject();
        saveDetails.put("minutes", minutes);
        saveDetails.put("seconds", seconds);
        saveDetails.put("board", getJsonBoard());

        JSONObject save = new JSONObject();
        save.put("save", saveDetails);
        JSONArray saves = new JSONArray();

        save.put("id", getJsonId());

        saves.add(save);
        System.out.println(saves.toJSONString());
    }


    private JSONObject getJsonBoard()
    {
        JSONObject boardDetails = new JSONObject();
        boardDetails.put("calculated", board.isCalculated());
        boardDetails.put("bombQuantity", board.getBombQuantity());
        boardDetails.put("width", board.getWidth());
        boardDetails.put("height", board.getHeight());
        JSONArray fields = new JSONArray();
        for(Field[] row : board.getFields())
        {
            JSONArray fieldRow = new JSONArray();
            for(final Field field : row)
            {
                JSONObject fieldDetails = new JSONObject();
                fieldDetails.put("x", field.getRowPosition());
                fieldDetails.put("y", field.getColPosition());
                fieldDetails.put("status", field.getStatus());
                fieldDetails.put("type", field.getFieldType());
                if(field.getFieldType() == FieldType.REGULAR)
                {
                    fieldDetails.put("value", ((RegularField) field).getValue());
                }
                fieldRow.add(fieldDetails);
            }
            fields.add(fieldRow);
        }
        boardDetails.put("fields", fields);
        return boardDetails;
    }

    private JSONObject getJsonId()
    {
        JSONObject jsonId = new JSONObject();
        if(getId() == null)
        {
            jsonId.put("saveId", getFirstFreeSaveId());
            Context context = Context.getContext();
            UserContext userContext = UserContext.getUserContext(context);
            jsonId.put("username", userContext.getCurrentUserName());
        } else
        {
            jsonId.put("saveId", getId().getSaveId());
            jsonId.put("username", getId().getUsername());
        }
        return jsonId;
    }

    private long getFirstFreeSaveId()
    {
        return getSaves().stream()
            .map(Save::getId)
            .mapToLong(SaveId::getSaveId)
            .filter(Objects::nonNull)
            .max()
            .orElse(0L) + 1;
    }

    @Override
    public Save load()
    {
        return null;
    }

    private List<Save> getSaves()
    {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = null;
        try(FileReader fr = new FileReader("saves.json"))
        {
            jsonArray = (JSONArray) parser.parse(fr);
        } catch(ParseException | IOException e)
        {
            //nothing to do
        }
        if(jsonArray == null)
        {
            return Collections.emptyList();
        }
        return ((List<Object>)jsonArray).stream()
            .map(this::getSave)
            .collect(Collectors.toList());
    }

    private Save getSave(Object object)
    {
        JSONObject jsonSave = (JSONObject) ((JSONObject) object).get("save");
        int seconds = Integer.parseInt((String) jsonSave.get("seconds"));
        int minutes = Integer.parseInt((String) jsonSave.get("minutes"));

        JSONObject jsonBoard = (JSONObject) jsonSave.get("board");
        int bombQuantity = Integer.parseInt((String) jsonBoard.get("bombQuantity"));
        int width = Integer.parseInt((String) jsonBoard.get("width"));
        int height = Integer.parseInt((String) jsonBoard.get("height"));
        Field[][] fields = new Field[width][height];
        for(Object rows : (JSONArray) jsonBoard.get("fields"))
        {
            for(Object field : (JSONArray) rows)
            {
                JSONObject jsonField = (JSONObject) field;
                int x = Integer.parseInt((String) jsonField.get("x"));
                int y = Integer.parseInt((String) jsonField.get("y"));
                FieldType type = (FieldType) jsonField.get("type");
                FieldStatus status = (FieldStatus) jsonField.get("status");
                if(type == FieldType.BOMB)
                {
                    fields[x][y] = new BombField(x, y, status);
                } else if(type == FieldType.REGULAR)
                {
                    int value = Integer.parseInt((String) jsonField.get("value"));
                    fields[x][y] = new RegularField(x, y, status, value);
                } else
                {
                    fields[x][y] = new EmptyField(x, y, status);
                }
            }
        }
        Board board = new Board(fields, bombQuantity);

        JSONObject jsonId = (JSONObject) jsonSave.get("id");
        long saveId = Long.parseLong((String) jsonId.get("saveId"));
        String username = (String) jsonId.get("username");
        SaveId id = new SaveId(saveId, username);
        return new Save(id, board, minutes, seconds);
    }

}
