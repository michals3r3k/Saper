package dev.michals3r3k.json.reader;

import dev.michals3r3k.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public abstract class JSONReader<T> implements ListReader<T>
{
    private final Logger logger;

    protected abstract String getSourceFileName();

    protected JSONReader(Logger logger)
    {
        this.logger = logger;
    }

    public JSONArray getJSONArray()
    {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = null;
        String source = getSourceFileName();
        try(FileReader fr = new FileReader(source))
        {
            jsonArray = (JSONArray) parser.parse(fr);
        } catch(ParseException e)
        {
            logger.warn(source + " is empty");
        } catch(IOException e)
        {
            e.printStackTrace();
        }
        return jsonArray == null ? new JSONArray() : jsonArray;
    }

}
