package dev.michals3r3k.dao;

import dev.michals3r3k.model.board.Board;
import dev.michals3r3k.model.board.components.Field;
import dev.michals3r3k.model.board.components.FieldType;
import dev.michals3r3k.model.board.components.RegularField;
import dev.michals3r3k.context.Context;
import dev.michals3r3k.context.UserContext;
import dev.michals3r3k.model.save.Save;
import dev.michals3r3k.model.save.SaveId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SaveREPO implements Saveable
{
    private static DateTimeFormatter DTF =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final SaveDAO saveDAO = new SaveDAO();

    @Override
    public void save(final Save save)
    {
        if(save.getId() == null)
        {
            Context context = Context.getContext();
            UserContext userContext = UserContext.getUserContext(context);
            save.setId(new SaveId(getFirstFreeSaveId(), userContext.getCurrentUserName()));
        }
        JSONObject saveDetails = new JSONObject();
        saveDetails.put("minutes", save.getMinutes());
        saveDetails.put("seconds", save.getSeconds());
        saveDetails.put("board", getJsonBoard(save.getBoard()));
        saveDetails.put("id", getJsonId(save.getId()));
        saveDetails.put("date", DTF.format(save.getSaveTime()));

        JSONObject jsonSave = new JSONObject();
        jsonSave.put("save", saveDetails);

        JSONArray saves = saveDAO.getJSONSaves();
        System.out.println(saves.toJSONString());
        saves.add(jsonSave);
        write(saves);
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


    @Override
    public void update(final Save save)
    {

    }

    @Override
    public void saveOrUpdate(final Save save)
    {

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

}
