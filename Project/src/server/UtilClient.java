package server;

import server.field.Field;

public abstract class UtilClient
{
    private static int nextID = 0;
    private boolean ready;
    private final int id;
    private final Field field;

    public UtilClient()
    {
        id = nextID;
        nextID++;
        ready = false;
        field = new Field();
    }

    public void setReady(boolean ready)
    {
        this.ready = ready;
    }

    public boolean isReady()
    {
        return ready;
    }
    public int getId()
    {
        return id;
    }

    public Field getField()
    {
        return field;
    }
}
