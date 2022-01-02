package dev.michals3r3k.dao.user;

import dev.michals3r3k.dao.AbstractDAO;
import dev.michals3r3k.json.reader.UserJSONReader;
import dev.michals3r3k.model.User;

public class UserDAO extends AbstractDAO<User, String>
{
    public UserDAO()
    {
        super(new UserJSONReader());
    }

    public User getUser(String username)
    {
        return getList().stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst()
            .orElse(null);
    }

}
