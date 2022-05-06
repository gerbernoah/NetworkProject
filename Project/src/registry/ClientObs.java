package registry;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientObs extends Remote
{
    void shot(int pos) throws RemoteException;
}
