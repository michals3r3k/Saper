package dev.michals3r3k.context;

import dev.michals3r3k.model.save.Save;
import dev.michals3r3k.model.save.SaveId;

public class SaveContext
{
    public static SaveContext getSaveContext(Context context)
    {
        Object parameter = context.getParameter(SaveContext.class);
        if(parameter != null)
        {
            return (SaveContext) parameter;
        }
        return new SaveContext();
    }

    private Save save;

    public SaveContext()
    {
        this.save = null;
    }

    public Save getSave()
    {
        return save;
    }

    public void setSave(final Save save)
    {
        this.save = save;
    }

    public SaveId getSaveIdOrNull()
    {
        return save == null ? null : save.getId();
    }

}
