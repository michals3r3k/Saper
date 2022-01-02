package dev.michals3r3k.model;

public class User implements Saveable<String>
{
    private String username;
    private String password;

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    @Override
    public String getId()
    {
        return getUsername();
    }

    @Override
    public void setId(final String id)
    {
        this.username = id;
    }

}
