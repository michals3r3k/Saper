package dev.michals3r3k.dao;

import dev.michals3r3k.Logger;
import dev.michals3r3k.json.reader.SaveJSONReader;
import dev.michals3r3k.model.save.Save;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class SaveDAO
{
    private static final String SAVE_FILENAME = "saves.json";
    private static final DateTimeFormatter DTF =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Logger logger = new Logger();
    private final SaveJSONReader reader = new SaveJSONReader();

    public List<Save> getSavesByUsername(String username)
    {
        return getSaves().stream()
            .filter(save -> save.getId().getUsername().equals(username))
            .collect(Collectors.toList());
    }

    public List<Save> getSaves()
    {
        return reader.read();
    }

    JSONArray getJSONSaves()
    {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = null;
        try(FileReader fr = new FileReader(SAVE_FILENAME))
        {
            jsonArray = (JSONArray) parser.parse(fr);
        } catch(ParseException e)
        {
            logger.warn(SAVE_FILENAME + " is empty");
        } catch(IOException e)
        {
            e.printStackTrace();
        }
        return jsonArray == null ? new JSONArray() : jsonArray;
    }

}
