package server;

public class Field
{
    private Unit[] units;

    public Field()
    {
        units = new Unit[100];
        for (Unit unit : units)
        {
            unit = Unit.EMPTY;
        }
    }

    public void setUnit(int pos, Unit unit)
    {
        units[pos] = unit;
    }

    public Unit getUnit(int pos)
    {
        return units[pos];
    }
}
