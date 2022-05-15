package server;

import registry.ClientObs;
import server.field.Field;

class UtilClient
{
    private static int nextID = 0;
    private boolean ready;
    private final ClientObs client;
    private final int id;
    private final String name;
    private final Field field;

    public UtilClient(ClientObs clientObs, String name)
    {
        id = nextID;
        nextID++;

        ready = false;
        this.client = clientObs;
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

    public ClientObs getClient()
    {
        return client;
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
