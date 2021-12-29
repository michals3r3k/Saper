package dev.michals3r3k.model.save;

import java.util.Objects;

public class SaveId
{
    private Long saveId;
    private String username;

    public SaveId(Long saveId, String username)
    {
        this.saveId = saveId;
        this.username = username;
    }

    public Long getSaveId()
    {
        return saveId;
    }

    public String getUsername()
    {
        return username;
    }

    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final SaveId id = (SaveId) o;
        return Objects.equals(saveId, id.saveId)
            && Objects.equals(username, id.username);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(saveId, username);
    }

}
