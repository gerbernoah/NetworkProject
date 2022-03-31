package client;

import registry.Product;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client
{
    public static void main(String[] args)
    {
        try {

            Registry registry = LocateRegistry.getRegistry("localhost", Registry.REGISTRY_PORT);

            Product pinguin = (Product) registry.lookup("p");
            Product capriSonne = (Product) registry.lookup("c");

            System.out.println(pinguin.getName() + " trinkt " + capriSonne.getName());
            System.out.println(pinguin.getName() + " muss " + capriSonne.getPrice() + " zahlen :(");

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
