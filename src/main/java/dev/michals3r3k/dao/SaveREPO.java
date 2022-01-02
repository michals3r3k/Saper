package dev.michals3r3k.dao;

import dev.michals3r3k.Logger;
import dev.michals3r3k.context.Context;
import dev.michals3r3k.context.UserContext;
import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.model.board.components.Field;
import dev.michals3r3k.model.board.components.FieldType;
import dev.michals3r3k.model.board.components.RegularField;
import dev.michals3r3k.model.save.Save;
import dev.michals3r3k.model.save.SaveId;
import dev.michals3r3k.model.save.GameTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SaveREPO implements Saveable
{
    private static final DateTimeFormatter DTF =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Logger logger = new Logger();
    private final SaveDAO saveDAO = new SaveDAO();

    @Override
    public void saveOrUpdate(final Save save)
    {
        if(save.getId() == null)
        {
            save(save);
            return;
        }
        update(save);
    }

    @Override
    public void save(final Save save)
    {
        if(save.getId() != null)
        {
            throw new IllegalArgumentException("id should be null");
        }
        save.setId(getNextSaveId());
        JSONArray saves = saveDAO.getJSONSaves();
        saves.add(convertToJson(save));
        write(saves);
        logger.info("New save has been saved");
    }

    private SaveId getNextSaveId()
    {
        Context context = Context.getContext();
        UserContext userContext = UserContext.getUserContext(context);
        return new SaveId(getFirstFreeSaveId(), userContext.getCurrentUserName());
    }

    @Override
    public void update(final Save save)
    {
        JSONArray jsonSaves = getJsonArrayAfterRemove(save.getId());
        jsonSaves.add(convertToJson(save));
        write(jsonSaves);
        logger.info("Save has been updated");
    }

    @Override
    public void remove(final Save save)
    {
        removeById(save.getId());
    }

    @Override
    public void removeById(final SaveId saveId)
    {
        JSONArray jsonSaves = getJsonArrayAfterRemove(saveId);
        write(jsonSaves);
        logger.info("Save has been removed");
    }

    private JSONObject getJsonSave(final JSONArray jsonSaves, SaveId givenSaveId)
    {
        for(final Object obj : jsonSaves)
        {
            JSONObject jsonSave = (JSONObject) obj;
            JSONObject jsonSaveDetails = (JSONObject) jsonSave.get("save");
            JSONObject jsonId = (JSONObject) jsonSaveDetails.get("id");
            long saveId = (Long) jsonId.get("saveId");
            String username = (String) jsonId.get("username");
            SaveId id = new SaveId(saveId, username);
            if(id.equals(givenSaveId))
            {
                return jsonSave;
            }
        }
        throw new IllegalStateException("Given save does not exists");
    }

    private JSONObject convertToJson(final Save save)
    {
        JSONObject saveDetails = new JSONObject();
        saveDetails.put("gameTime", getJsonGameTime(save.getGameTime()));
        saveDetails.put("board", getJsonBoard(save.getBoard()));
        saveDetails.put("id", getJsonId(save.getId()));
        saveDetails.put("date", DTF.format(save.getSaveTime()));
        saveDetails.put("flagQuantity", save.getFlagQuantity());

        JSONObject jsonSave = new JSONObject();
        jsonSave.put("save", saveDetails);
        return jsonSave;
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
        try(FileWriter fw = new FileWriter("saves.json"))
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

    private long getFirstFreeSaveId()
    {
        return saveDAO.getSaves().stream()
            .map(Save::getId)
            .mapToLong(SaveId::getSaveId)
            .filter(Objects::nonNull)
            .max()
            .orElse(0L) + 1;
    }

    private JSONArray getJsonArrayAfterRemove(final SaveId saveId)
    {
        JSONArray jsonSaves = saveDAO.getJSONSaves();
        JSONObject saveJson = getJsonSave(jsonSaves, saveId);
        jsonSaves.remove(saveJson);
        return jsonSaves;
    }

}

