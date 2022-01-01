package dev.michals3r3k.context;

import java.util.HashMap;
import java.util.Map;

public class Context
{
    private static Context context;

    public static Context getContext()
    {
        if(context == null)
        {
            context = new Context();
        }
        return context;
    }

    private final Map<Class<?>, Object> parameterMap;

    private Context()
    {
        parameterMap = new HashMap<>();
    }

    public Object getParameter(Class<?> clazz)
    {
        return parameterMap.get(clazz);
    }

    public void setParameter(Object object)
    {
        parameterMap.put(object.getClass(), object);
    }

}
