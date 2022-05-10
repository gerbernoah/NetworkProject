package server.field;

import java.util.ArrayList;

public class Field
{
    private final ArrayList<Ship> ships;
    private final Boolean[] field;

    public Field()
    {
        ships = new ArrayList<>();
        field = new Boolean[100];
    }

    public boolean shipContains(int pos)
    {
        for (Ship ship : ships)
        {
            if (ship.contains(pos))
                return true;
        }
        return false;
    }

    /**
     * @param startPos of the ship
     * @param endPos of the ship
     * @return if ship destination is valid
     */
    public boolean addShip(int startPos, int endPos)
    {
        Ship ship = new Ship(startPos, endPos);
        for (Integer position : ship.getPositions())
        {
            if (getShipPositions().contains(position))
                return false;
        }
        ships.add(ship);
        return true;
    }

    private ArrayList<Integer> getShipPositions()
    {
        ArrayList<Integer> positions = new ArrayList<>();
        for (Ship ship : ships)
        {
            for (int position : ship.getPositions())
            {
                positions.add(position);
            }
        }
        return positions;
    }

    public void clearShips()
    {
        ships.clear();
    }

    public boolean getUnit(int pos)
    {
        return field[pos];
    }

    public void setUnit(int pos, boolean shot)
    {
        field[pos] = shot;
    }
}
