package dev.michals3r3k.model.save;

public class GameTime
{
    private int seconds;
    private int minutes;
    private int elapsedTime;

    public GameTime(final int seconds, final int minutes, final int elapsedTime)
    {
        this.seconds = seconds;
        this.minutes = minutes;
        this.elapsedTime = elapsedTime;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public void setSeconds(final int seconds)
    {
        this.seconds = seconds;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public void setMinutes(final int minutes)
    {
        this.minutes = minutes;
    }

    public int getElapsedTime()
    {
        return elapsedTime;
    }

    public void setElapsedTime(final int elapsedTime)
    {
        this.elapsedTime = elapsedTime;
    }

    public void addSecond()
    {
        this.elapsedTime++;
        this.seconds = elapsedTime % 60;
        this.minutes = (elapsedTime / 60) % 60;
    }

}
