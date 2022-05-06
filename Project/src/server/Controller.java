package server;

import java.util.ArrayList;

public class Controller
{
    private final Field field;
    private final Server server;

    public Controller(Server server)
    {
        field = new Field();
        this.server = server;
    }

    private void killShipUnit(int pos)
    {
        field.setUnit(pos, Unit.SHOT);
        server.killShipUnit(pos);
    }

    public boolean shoot(int clientID, int pos)
    {
        switch (field.getUnit(pos))
        {
            case EMPTY: return true;
            case SHOT: return false;
            case SHIP0:
                if (clientID == 0)  //shooting own ship
                    return false;
                killShipUnit(pos);
                return true;
            case SHIP1:
                if (clientID == 1)  //shooting own ship
                    return false;
                killShipUnit(pos);
                return true;
        }
        return false;   //something went wrong
    }

    public boolean placeShip(int clientID, int startPos, int endPos)
    {
        ArrayList<Integer> positions;
        int xStartPos = startPos % 10;
        int yStartPos = startPos / 10;
        int xEndPos = endPos % 10;
        int yEndPos = endPos / 10;

        if (xStartPos == xEndPos)
        {

        }
        else if (yStartPos == yEndPos)
        {

        }
        else
            return false;   //something went wrong
    }
}
