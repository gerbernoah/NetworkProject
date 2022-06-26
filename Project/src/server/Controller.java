package server;

import server.field.Field;
import server.field.Ship;

import java.awt.*;

public class Controller
{
    private boolean player0OnTurn;
    private final Server server;

    public Controller(Server server)
    {
        player0OnTurn = true;
        this.server = server;
    }

    public int shoot(UtilClient client, int pos, UtilClient destClient) //called from server
    {
        if (player0OnTurn == (client.getId() == 0))
        {
            System.out.println("not your turn");
            return 0;
        }

        Field clField = destClient.getField();
        boolean anyShip = clField.getShipOn(pos) != null;

        if (!anyShip)
            player0OnTurn = !player0OnTurn;

        clField.setUnit(pos, true);

        int onTurn = 3;
        if (!anyShip)
            onTurn = 0;
        else if (clField.getAliveUnits(clField.getShipOn(pos)) > 0)
            onTurn = 1;
        else
        {
            for (Ship ship1 : clField.getShips())
                if (clField.getAliveUnits(ship1) > 0)
                {
                    onTurn = 2;
                    break;
                }
        }
        server.shot(client, pos, onTurn);
        return onTurn;
    }

    public boolean placeShips(UtilClient client, Point[] ships) //called from server
    {
        for (Point ship : ships)
        {
            if (!client.getField().addShip(ship.x, ship.y))
            {
                client.getField().clearShips();
                return false;
            }
        }
        return true;
    }
}
