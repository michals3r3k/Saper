package dev.michals3r3k;

import dev.michals3r3k.context.Context;
import dev.michals3r3k.frame.LoginFrame;

public class SaperRunner implements Runnable
{
    private static SaperRunner INSTANCE;

    private SaperRunner()
    {
        // intentionally empty constructor for disallowing objects creation
    }

    @Override
    public void run()
    {
        new LoginFrame();
//        new MenuFrame();
    }

    public static SaperRunner getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new SaperRunner();
        }
        return INSTANCE;
    }

}
