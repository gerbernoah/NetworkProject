package server;

import server.field.Field;

public abstract class UtilClient
{
    private static int nextID = 0;
    private boolean ready;
    private final int id;
    private final String name;
    private final Field field;

    public UtilClient(String name)
    {
        id = nextID;
        nextID++;
        ready = false;
        this.name = name;
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

    public String getName()
    {
        return name;
    }

    public Field getField()
    {
        return field;
    }
}
