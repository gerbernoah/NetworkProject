package client;

import registry.ClientObs;
import server.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class Client implements ClientObs
{
    private Server server;
    String regName;

    public Client()
    {
        try {

            Registry registry = LocateRegistry.getRegistry("localhost", Registry.REGISTRY_PORT);
            server = (Server) registry.lookup("s");

            ClientObs clientObs = (ClientObs) UnicastRemoteObject.exportObject(this, 0);

            if (!Arrays.asList(registry.list()).contains("c1"))
                regName = "c1";
            else if (!Arrays.asList(registry.list()).contains("c2"))
                regName = "c2";
            else
            {
                System.out.println("server full");
                return;
            }

            registry.bind(regName, clientObs);
            server.clientAdded(regName);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean placeShip(int startPos, int endPos)
    {
        try
        {
            int err = server.placeShip(regName, startPos, endPos);
            if (err == -1)
                System.out.println("client name not in registry");
            return err == 1;
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param pos destination to shoot
     * @return if shot was valid
     */
    public boolean shoot(int pos)
    {
        try
        {
            int err = server.shoot(regName, pos);
            if (err == -1)
                System.out.println("client name not in registry");
            return err == 1;
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void shot(int pos) throws RemoteException
    {
        //Schiff an der Stelle pos wurde abgeschossen todo schiff löschen
    }
}
