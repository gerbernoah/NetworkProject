package server;

import registry.Product;

import java.rmi.RemoteException;

public class ProductImpl implements Product
{
    private String name;
    private double price;

    public ProductImpl(String name, double price) throws RemoteException
    {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() throws RemoteException
    {
        return this.name;
    }

    @Override
    public double getPrice() throws RemoteException
    {
        return this.price;
    }
}
