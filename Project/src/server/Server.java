package server;

import registry.Product;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server
{
    public static void main(String[] args)
    {
        try {
            System.setProperty("java.rmi.server.hostname", "localhost");

            ProductImpl product1 = new ProductImpl("CapriSonne", 0.3);
            ProductImpl product2 = new ProductImpl("Pinguin", 50);

            Product stub1 = (Product) UnicastRemoteObject.exportObject(product1, 0);
            Product stub2 = (Product) UnicastRemoteObject.exportObject(product2, 0);

            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

            registry.rebind("c", stub1);
            registry.rebind("p", stub2);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
