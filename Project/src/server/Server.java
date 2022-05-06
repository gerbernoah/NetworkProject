package server;

import client.Client;
import com.sun.xml.internal.bind.v2.model.core.ID;
import registry.ServerObs;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Server implements ServerObs
{
    private final ArrayList<UtilClient> clients;
    private Registry registry;
    private final Controller controller;

    public Server()
    {
        controller = new Controller(this);
        clients = new ArrayList<>();

        try {
            System.setProperty("java.rmi.server.hostname", "localhost");
            registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

            ServerObs serverObs = (ServerObs) UnicastRemoteObject.exportObject(this, 0);
            registry.bind("s", serverObs);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void killShipUnit(int pos)
    {
        for (UtilClient client : clients)
        {
            try
            {
                client.getClient().shot(pos);
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int shoot(String name, int pos) throws RemoteException
    {
        for (UtilClient client : clients)
        {
            if (Objects.equals(client.getName(), name))
                return controller.shoot(client.getId(), pos) ? 1 : 0;   //returns 1 if shot was valid, 0 if invalid
        }
        return -1;  //returns -1 if name not exist
    }

    @Override
    public int placeShip(String name, int startPos, int endPos) throws RemoteException
    {
        for (UtilClient client : clients)
        {
            if (Objects.equals(client.getName(), name))
                return controller.placeShip(client.getId(), startPos, endPos) ? 1 : 0;   //returns 1 if shot was valid, 0 if invalid
        }
        return -1;  //returns -1 if name not exist
    }

    @Override
    public void clientAdded(String name) throws RemoteException
    {
        try
        {
            clients.add( new UtilClient((Client) registry.lookup(name), name));
        } catch (NotBoundException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        new Server();
    }
}

class UtilClient
{
    private static int nextID = 0;
    private Client client;
    private int id;
    private String name;

    public UtilClient(Client client, String name)
    {
        id = nextID;
        nextID++;

        this.client = client;
        this.name = name;
    }

    public Client getClient()
    {
        return client;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}

