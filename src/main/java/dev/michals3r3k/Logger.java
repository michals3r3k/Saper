package dev.michals3r3k;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Logger
{
    private static final DateTimeFormatter DTF =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Map<LogType, String> colorMap;
    static
    {
        colorMap = new HashMap<>();
        colorMap.put(LogType.INFO, "\u001B[34m");
        colorMap.put(LogType.WARN, "\u001B[33m");
        colorMap.put(LogType.ERR, "\u001B[31m");
    }

    public void warn(String message)
    {
        getLog(LogType.WARN, message);
    }

    public void info(String message)
    {
        getLog(LogType.INFO, message);
    }

    public void error(String message)
    {
        getLog(LogType.ERR, message);
    }

    private void getLog(LogType type, String message)
    {
        final String log = colorMap.get(type) + "[" + type.name() + "] " + "\u001B[0m" +
            message + " [" + DTF.format(LocalDateTime.now()) + "]";
        System.out.println(log);
    }

    private enum LogType
    {
        INFO,
        WARN,
        ERR;
    }
}
