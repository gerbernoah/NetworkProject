package server;

import registry.ClientObs;
import registry.ServerObs;

import java.awt.*;
import java.rmi.AlreadyBoundException;
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

            System.out.println("running on " + Registry.REGISTRY_PORT);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param pClient the client from which the shot is from
     * @param pos the position shot
     * @param onTurn if the client is on Turn after shot
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
        UtilClient dstClient = null;
        for (UtilClient client : clients)
        {
            if (client.getName().equals(name))
                continue;
            else
                dstClient = client;
            break;
        }

        for (UtilClient client : clients)
        {
            if (Objects.equals(client.getName(), name))
                return controller.shoot(client, pos, dstClient);  //returns if shot was a hit
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
    public boolean adClient(String name, ClientObs clientObs) throws RemoteException
    {
        try
        {
            registry.bind(name, clientObs);
            clients.add( new UtilClient(clientObs, name));
        } catch (AlreadyBoundException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void clientReady(String name) throws RemoteException
    {
        boolean allReady = true;
        for (UtilClient client : clients)
        {
            if (client.getName().equals(name))
                client.setReady(true);
            if (!client.isReady())
                allReady = false;
        }

            if (allReady)
                clients.forEach(client ->
                {
                    try
                    {
                        client.getClient().gameStart();
                    } catch (RemoteException e)
                    {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void messageReceived(String name, String message) throws RemoteException
    {
        for (UtilClient client : clients)
        {
            if (client.getName().equals(name))
                continue;
            else
                client.getClient().messageReceived(message);
            break;
        }
    }

    public static void main(String[] args)
    {
        new Server();
    }
}

