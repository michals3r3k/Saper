package dev.michals3r3k.dao.save;

import dev.michals3r3k.Logger;
import dev.michals3r3k.context.Context;
import dev.michals3r3k.context.UserContext;
import dev.michals3r3k.dao.JSONRepository;
import dev.michals3r3k.json.converter.SaveJSONConverter;
import dev.michals3r3k.json.reader.SaveJSONReader;
import dev.michals3r3k.model.save.Save;
import dev.michals3r3k.model.save.SaveId;
import java.util.Objects;

public class SaveREPO extends JSONRepository<Save, SaveId>
{
    public SaveREPO()
    {
        super(new SaveJSONReader(), new SaveJSONConverter(), new Logger());
    }

    @Override
    protected SaveId getNextId()
    {
        Context context = Context.getContext();
        UserContext userContext = UserContext.getUserContext(context);
        return new SaveId(getFirstFreeSaveId(), userContext.getCurrentUserName());
    }

    private long getFirstFreeSaveId()
    {
        return jsonReader.read().stream()
            .map(Save::getId)
            .mapToLong(SaveId::getSaveId)
            .filter(Objects::nonNull)
            .max()
            .orElse(0L) + 1;
    }

}
