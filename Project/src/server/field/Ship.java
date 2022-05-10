package server.field;

import java.util.ArrayList;

public class Ship
{
    private final int xStartPos, yStartPos, xEndPos, yEndPos;
    private final boolean horizontal;

    public Ship(int startPos, int endPos)
    {
        xStartPos = startPos % 10;
        yStartPos = startPos / 10;
        xEndPos = endPos % 10;
        yEndPos = endPos / 10;

        horizontal = yStartPos == yEndPos;
    }

    public boolean contains(int x, int y)
    {
        return xStartPos <= x && x <= xEndPos
                && yStartPos <= y && y <= yEndPos;
    }

    public boolean contains(int pos)
    {
        return contains(pos % 10, pos / 10);
    }

    public ArrayList<Integer> getPositions()
    {
        ArrayList<Integer> positions = new ArrayList<>();
        if (horizontal)
            for (int i = xStartPos; i <= xEndPos; i++)
                positions.add(i);
        else
            for (int i = yStartPos; i <= yEndPos; i++)
                positions.add(i);
        return positions;
    }

    public boolean isHorizontal()
    {
        return horizontal;
    }

    public int getxStartPos()
    {
        return xStartPos;
    }

    public int getyStartPos()
    {
        return yStartPos;
    }

    public int getxEndPos()
    {
        return xEndPos;
    }

    public int getyEndPos()
    {
        return yEndPos;
    }
}
