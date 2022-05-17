package server.field;

import java.util.ArrayList;
import java.util.Arrays;

public class Field
{
    private final ArrayList<Ship> ships;
    private final Boolean[] isHit;

    public Field()
    {
        ships = new ArrayList<>();
        isHit = new Boolean[100];
        Arrays.fill(isHit, false);
    }

    /**
     * @param startPos of the ship
     * @param endPos of the ship
     * @return if ship destination is valid
     */
    public boolean addShip(int startPos, int endPos)
    {
        Ship ship = new Ship(Math.min(startPos, endPos), Math.max(startPos, endPos));

        int xStart = Math.max(0, ship.getxStartPos() - 1);
        int yStart = Math.max(0, ship.getyStartPos() - 1);
        int xEnd = Math.min(9, ship.getxEndPos() + 1);
        int yEnd = Math.min(9, ship.getyEndPos() + 1);

        for (int x = xStart; x <= xEnd; x++)
            for (int y = yStart; y <= yEnd; y++)
                for (Ship ship1 : ships)
                {
                    if (ship1.contains(x, y))
                        return false;
                }

        ships.add(ship);
        return true;
    }

    public int getAliveUnits(Ship ship)
    {
        int alive = 0;
        for (Integer position : ship.getPositions())
        {
            if (!isHit[position])
                alive++;
        }
        return alive;
    }

    public Ship getShipOn(int pos)
    {
        for (Ship ship : ships)
        {
            if (ship.contains(pos))
                return ship;
        }
        return null;
    }

    private ArrayList<Integer> getShipPositions()
    {
        ArrayList<Integer> positions = new ArrayList<>();
        for (Ship ship : ships)
            positions.addAll(ship.getPositions());
        return positions;
    }

    public void clearShips()
    {
        ships.clear();
    }

    public ArrayList<Ship> getShips()
    {
        return ships;
    }

    public void setUnit(int pos, boolean shot)
    {
        isHit[pos] = shot;
    }
}
