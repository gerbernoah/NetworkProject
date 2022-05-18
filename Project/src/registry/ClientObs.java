package registry;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientObs extends Remote
{
    void shot(int pos, int onTurn) throws RemoteException;

    void gameStart() throws RemoteException;

    void messageReceived(String message) throws RemoteException;
}
