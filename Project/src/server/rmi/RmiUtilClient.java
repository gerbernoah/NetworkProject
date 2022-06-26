package server.rmi;

import shared.rmi.ClientObs;
import server.UtilClient;

class RmiUtilClient extends UtilClient
{
    private final ClientObs client;
    private final String name;

    public RmiUtilClient(ClientObs clientObs, String name)
    {
        super();
        this.client = clientObs;
        this.name = name;
    }

    public ClientObs getClient()
    {
        return client;
    }

    public String getName()
    {
        return name;
    }

}
