package dev.michals3r3k.context;

import dev.michals3r3k.user.User;

public class UserContext
{
    public static UserContext getUserContext(Context context)
    {
        Object parameter = context.getParameter(UserContext.class);
        if(parameter != null)
        {
            return (UserContext) parameter;
        }
        return new UserContext();
    }

    private User user;

    public UserContext()
    {
        this.user = null;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public User getUser()
    {
        return user;
    }



}

