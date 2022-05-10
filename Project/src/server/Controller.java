package server;

import server.field.Field;

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

    public boolean shoot(UtilClient client, int pos)
    {
        if (player0OnTurn == (client.getId() == 1))
            return false;
        Field clField = client.getField();
        boolean ship = clField.shipContains(pos);

        clField.setUnit(pos, true);
        server.shot(client, pos);
        if (ship)
            client.getField().setUnit(pos, false);
        return ship;
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
