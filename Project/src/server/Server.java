package server;

import client.Client;
import registry.ServerObs;
import server.field.Field;

import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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

    /**
     * @param pClient the client from which the shot is from
     * @param pos the position shot
     */
    public void shot(UtilClient pClient, int pos, boolean onTurn)
    {
        for (UtilClient client : clients)
        {
            if (client == pClient)
                continue;
            try
            {
                client.getClient().shot(pos, onTurn);
            } catch (RemoteException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int shoot(String name, int pos) throws RemoteException
    {
        for (UtilClient client : clients)
        {
            if (Objects.equals(client.getName(), name))
                return controller.shoot(client, pos);  //returns if shot was a hit
        }
        System.out.println("client not found");
        return -1;
    }

    @Override
    public boolean placeShips(String name, Point[] ships) throws RemoteException
    {
        for (UtilClient client : clients)
        {
            if (client.getName().equals(name))
                return controller.placeShips(client, ships); //returns if placement was valid
        }
        System.out.println("client not found");
        return false;
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
    private final Client client;
    private final int id;
    private final String name;
    private final Field field;

    public UtilClient(Client client, String name)
    {
        id = nextID;
        nextID++;

        this.client = client;
        this.name = name;
        field = new Field();
    }

    public Client getClient()
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

