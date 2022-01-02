package dev.michals3r3k.dao.save;

import dev.michals3r3k.dao.AbstractDAO;
import dev.michals3r3k.json.reader.SaveJSONReader;
import dev.michals3r3k.model.save.Save;
import dev.michals3r3k.model.save.SaveId;

import java.util.List;
import java.util.stream.Collectors;

public class SaveDAO extends AbstractDAO<Save, SaveId>
{
    public SaveDAO()
    {
        super(new SaveJSONReader());
    }

    public List<Save> getSavesByUsername(String username)
    {
        return getList().stream()
            .filter(save -> save.getId().getUsername().equals(username))
            .collect(Collectors.toList());
    }

}
