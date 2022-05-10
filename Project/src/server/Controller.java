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

    public int shoot(UtilClient client, int pos)
    {
        if (player0OnTurn == (client.getId() == 0))
            return 0;

        Field clField = client.getField();
        boolean anyShip = clField.getShipOn(pos) != null;

        if (!anyShip)
            player0OnTurn = !player0OnTurn;

        clField.setUnit(pos, true);
        server.shot(client, pos, anyShip);

        if (!anyShip)
            return 0;
        if (clField.getAliveUnits(clField.getShipOn(pos)) > 0)
            return 1;
        for (Ship ship1 : clField.getShips())
            if (clField.getAliveUnits(ship1) > 0)
                return 2;
        return 3;
    }

    public boolean placeShips(UtilClient client, Point[] ships)
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
