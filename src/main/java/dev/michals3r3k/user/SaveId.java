package dev.michals3r3k.user;

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

}
