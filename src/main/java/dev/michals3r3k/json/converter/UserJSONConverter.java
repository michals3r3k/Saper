package dev.michals3r3k.json.converter;

import dev.michals3r3k.model.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class UserJSONConverter extends JSONConverter<User, String>
{
    @Override
    public JSONArray convert(final List<User> users)
    {
        JSONArray jsonArray = new JSONArray();
        users.stream()
            .map(this::convert)
            .forEach(jsonArray::add);
        return jsonArray;
    }

    @Override
    public JSONObject convert(final User user)
    {
        JSONObject userDetails = getJsonUserDetails(user);
        JSONObject jsonUser = new JSONObject();
        jsonUser.put("user", userDetails);
        return jsonUser;
    }

    private JSONObject getJsonUserDetails(final User users)
    {
        JSONObject userDetails = new JSONObject();
        userDetails.put("username", users.getUsername());
        userDetails.put("password", users.getPassword());
        return userDetails;
    }

    @Override
    public JSONObject getById(final JSONArray list, final String id)
    {
        return null;
    }

}
