package dev.michals3r3k.dao;

import dev.michals3r3k.Logger;
import dev.michals3r3k.context.Context;
import dev.michals3r3k.context.UserContext;
import dev.michals3r3k.json.converter.SaveJSONConverter;
import dev.michals3r3k.json.reader.SaveJSONReader;
import dev.michals3r3k.model.save.Save;
import dev.michals3r3k.model.save.SaveId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class SaveREPO implements Saveable
{
    private final Logger logger = new Logger();
    private final SaveJSONConverter converter = new SaveJSONConverter();
    private final SaveJSONReader reader = new SaveJSONReader();

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
        JSONArray saves = reader.getJSONArray();
        saves.add(converter.convert(save));
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
        jsonSaves.add(converter.convert(save));
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

    private long getFirstFreeSaveId()
    {
        return reader.read().stream()
            .map(Save::getId)
            .mapToLong(SaveId::getSaveId)
            .filter(Objects::nonNull)
            .max()
            .orElse(0L) + 1;
    }

    private JSONArray getJsonArrayAfterRemove(final SaveId saveId)
    {
        JSONArray jsonSaves = reader.getJSONArray();
        JSONObject saveJson = getJsonSave(jsonSaves, saveId);
        jsonSaves.remove(saveJson);
        return jsonSaves;
    }

}

