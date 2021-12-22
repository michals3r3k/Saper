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
            return new Context();
        }
        return context;
    }

    private Map<Class<?>, Object> parameterMap;

    private Context()
    {
        this.parameterMap = new HashMap<>();
    }

    public Object getParameter(Class<?> clazz)
    {
        return parameterMap.get(clazz);
    }

    public void setParameter(Class<?> clazz, Object parameter)
    {
        this.parameterMap.put(clazz, parameter);
    }

}
