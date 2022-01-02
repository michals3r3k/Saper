package dev.michals3r3k.dao;

import dev.michals3r3k.Logger;
import dev.michals3r3k.json.converter.JSONConverter;
import dev.michals3r3k.json.reader.JSONReader;
import dev.michals3r3k.model.Saveable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public abstract class JSONRepository<T extends Saveable<E>, E> implements Repository<T, E>
{
    protected final JSONReader<T> jsonReader;
    private final JSONConverter<T, E> jsonConverter;
    private final Logger logger;

    protected JSONRepository(
        JSONReader<T> jsonReader,
        JSONConverter<T, E> jsonConverter,
        Logger logger)
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
        if(toSave.getId() != null)
        {
            throw new IllegalArgumentException("id should be null");
        }
        toSave.setId(getNextId());
        JSONArray jsonArray = jsonReader.getJSONArray();
        jsonArray.add(jsonConverter.convert(toSave));
        write(jsonArray);
        logger.info("New save has been saved");
    }

    protected abstract E getNextId();

    @Override
    public void update(final T toUpdate)
    {
        JSONArray jsonArray = getJsonArrayAfterRemove(toUpdate.getId());
        jsonArray.add(jsonConverter.convert(toUpdate));
        write(jsonArray);
        logger.info("Save has been updated");
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
        logger.info("Save has been removed");
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
        try(FileWriter fw = new FileWriter("saves.json"))
        {
            fw.write(jsonArray.toJSONString());
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

}
