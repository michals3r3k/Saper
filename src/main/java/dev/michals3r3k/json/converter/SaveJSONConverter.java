package dev.michals3r3k.json.converter;

import dev.michals3r3k.json.reader.SaveJSONReader;
import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.model.board.components.Field;
import dev.michals3r3k.model.board.components.FieldType;
import dev.michals3r3k.model.board.components.RegularField;
import dev.michals3r3k.model.save.GameTime;
import dev.michals3r3k.model.save.Save;
import dev.michals3r3k.model.save.SaveId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class SaveJSONConverter extends JSONConverter<Save, SaveId>
{
    private static final DateTimeFormatter DTF =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final SaveJSONReader reader = new SaveJSONReader();

    @Override
    public JSONArray convert(final List<Save> saves)
    {
        JSONArray jsonArray = new JSONArray();
        saves.stream()
            .map(this::convert)
            .forEach(jsonArray::add);
        return jsonArray;
    }

    @Override
    public JSONObject convert(final Save save)
    {
        JSONObject jsonSave = new JSONObject();
        jsonSave.put("save", getJsonSaveDetails(save));
        return jsonSave;
    }

    @Override
    public JSONObject getById(final JSONArray list, final SaveId givenId)
    {
        for(final Object obj : list)
        {
            JSONObject jsonSave = (JSONObject) obj;
            JSONObject jsonSaveDetails = (JSONObject) jsonSave.get("save");
            JSONObject jsonId = (JSONObject) jsonSaveDetails.get("id");
            long saveId = (Long) jsonId.get("saveId");
            String username = (String) jsonId.get("username");
            SaveId id = new SaveId(saveId, username);
            if(id.equals(givenId))
            {
                return jsonSave;
            }
        }
        throw new IllegalStateException("Given save does not exists");
    }

    private JSONObject getJsonSaveDetails(final Save save)
    {
        JSONObject saveDetails = new JSONObject();
        saveDetails.put("gameTime", getJsonGameTime(save.getGameTime()));
        saveDetails.put("board", getJsonBoard(save.getBoard()));
        saveDetails.put("id", getJsonId(save.getId()));
        saveDetails.put("date", DTF.format(save.getSaveTime()));
        saveDetails.put("flagQuantity", save.getFlagQuantity());
        return saveDetails;
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
                fieldDetails.put("status", field.getStatus().name());
                fieldDetails.put("type", field.getFieldType().name());
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

    private JSONObject getJsonId(SaveId id)
    {
        JSONObject jsonId = new JSONObject();
        jsonId.put("saveId", id.getSaveId());
        jsonId.put("username", id.getUsername());
        return jsonId;
    }

}
