package dev.michals3r3k.json.reader;

import dev.michals3r3k.Logger;
import dev.michals3r3k.model.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserJSONReader extends JSONReader<User>
{
    public UserJSONReader()
    {
        super(new Logger());
    }

    @Override
    protected String getSourceFileName()
    {
        return "users.json";
    }

    @Override
    public List<User> read()
    {
        JSONArray userList = getJSONArray();
        if(userList == null)
        {
            return Collections.emptyList();
        }
        return (List<User>) userList.stream()
            .map(this::getUser)
            .collect(Collectors.toList());
    }

    private User getUser(Object obj)
    {
        JSONObject jsonObj = (JSONObject) obj;
        JSONObject user = (JSONObject) jsonObj.get("user");
        String username = (String) user.get("username");
        String password = (String) user.get("password");
        return new User(username, password);
    }

}
