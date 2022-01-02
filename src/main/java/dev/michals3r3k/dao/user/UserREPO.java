package dev.michals3r3k.dao.user;

import dev.michals3r3k.Logger;
import dev.michals3r3k.dao.JSONRepository;
import dev.michals3r3k.json.converter.UserJSONConverter;
import dev.michals3r3k.json.reader.UserJSONReader;
import dev.michals3r3k.model.User;

public class UserREPO extends JSONRepository<User, String>
{
    public UserREPO()
    {
        super(new UserJSONReader(), new UserJSONConverter(), new Logger());
    }

    @Override
    protected boolean checkId()
    {
        return false;
    }

    @Override
    protected String getSourceFileName()
    {
        return "users.json";
    }

}
