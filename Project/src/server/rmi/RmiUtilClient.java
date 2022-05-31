package server.rmi;

import shared.rmi.ClientObs;
import server.UtilClient;

class RmiUtilClient extends UtilClient
{
    private final ClientObs client;

    public RmiUtilClient(ClientObs clientObs, String name)
    {
        super(name);
        this.client = clientObs;
    }

    public ClientObs getClient()
    {
        return client;
    }
}
