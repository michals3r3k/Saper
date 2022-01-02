package dev.michals3r3k.dao;

import dev.michals3r3k.Logger;
import dev.michals3r3k.json.converter.JSONConverter;
import dev.michals3r3k.json.reader.JSONReader;
import dev.michals3r3k.model.Saveable;
import dev.michals3r3k.model.User;
import dev.michals3r3k.model.save.Save;
import dev.michals3r3k.model.score.Score;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class JSONRepository<T extends Saveable<E>, E> implements Repository<T, E>
{
    private static final Map<Class<?>, String> NAME_MAP = new HashMap<>();
    static
    {
        NAME_MAP.put(Save.class, "save");
        NAME_MAP.put(User.class, "user");
        NAME_MAP.put(Score.class, "score");
    }
    protected final JSONReader<T> jsonReader;
    private final JSONConverter<T, E> jsonConverter;
    private final Logger logger;

    protected JSONRepository(
        final JSONReader<T> jsonReader,
        final JSONConverter<T, E> jsonConverter,
        final Logger logger)
    {
        this.jsonReader = jsonReader;
        this.jsonConverter = jsonConverter;
        this.logger = logger;
    }

    @Override
    public void saveOrUpdate(final T obj)
    {
        if(obj.getId() == null)
        {
            save(obj);
            return;
        }
        update(obj);
    }

    @Override
    public void save(final T toSave)
    {
        if(checkId() && toSave.getId() != null)
        {
            throw new IllegalArgumentException("id should be null");
        }
        if(checkId() && toSave.getId() == null)
        {
            toSave.setId(getNextId());
        }
        JSONArray jsonArray = jsonReader.getJSONArray();
        jsonArray.add(jsonConverter.convert(toSave));
        write(jsonArray);
        logger.info("new " + NAME_MAP.get(toSave.getClass()) + " has been saved");
    }

    protected boolean checkId()
    {
        return true;
    }

    protected E getNextId()
    {
        return null;
    }

    @Override
    public void update(final T toUpdate)
    {
        JSONArray jsonArray = getJsonArrayAfterRemove(toUpdate.getId());
        jsonArray.add(jsonConverter.convert(toUpdate));
        write(jsonArray);
        logger.info(NAME_MAP.get(toUpdate.getClass()) + " has been updated");
    }


    @Override
    public void remove(final T obj)
    {
        removeById(obj.getId());
    }

    @Override
    public void removeById(final E id)
    {
        JSONArray jsonSaves = getJsonArrayAfterRemove(id);
        write(jsonSaves);
        logger.info("object has been removed");
    }

    private JSONArray getJsonArrayAfterRemove(final E id)
    {
        JSONArray jsonArray = jsonReader.getJSONArray();
        JSONObject saveJson = jsonConverter.getById(jsonArray, id);
        jsonArray.remove(saveJson);
        return jsonArray;
    }

    private void write(JSONArray jsonArray)
    {
        try(FileWriter fw = new FileWriter(getSourceFileName()))
        {
            fw.write(jsonArray.toJSONString());
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    protected abstract String getSourceFileName();

}
