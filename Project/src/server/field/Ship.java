package server.field;

import java.util.ArrayList;

public class Ship
{
    private int xStartPos, yStartPos, xEndPos, yEndPos;
    private boolean horizontal;

    public Ship(int startPos, int endPos)
    {
        xStartPos = startPos % 10;
        yStartPos = startPos / 10;
        xEndPos = endPos % 10;
        yEndPos = endPos / 10;

        horizontal = yStartPos == yEndPos;
    }

    public Ship() {

    }

    public boolean validate()
    {
        return xEndPos < 10 && yEndPos < 10 && xStartPos >= 0 && yStartPos >= 0
                && (xStartPos == xEndPos || yStartPos == yEndPos);
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
                positions.add(yStartPos * 10 + i);
        else
            for (int i = yStartPos; i <= yEndPos; i++)
                positions.add(i * 10 + xStartPos);
        return positions;
    }

    public boolean isHorizontal()
    {
        return horizontal;
    }

    public int getStartPos()
    {
        return yStartPos * 10 + xStartPos;
    }

    public int getEndPos()
    {
        return yEndPos * 10 + xEndPos;
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

    /**
     * changes ship position (nicht so hübsch gemacht :p)
     * @param startPos of the ship
     * @param endPos of the ship
     */
   public boolean setPos(int startPos, int endPos)
   {
       if ( new Ship(startPos, endPos).validate() )
       {
           xStartPos = startPos % 10;
           yStartPos = startPos / 10;
           xEndPos = endPos % 10;
           yEndPos = endPos / 10;

           horizontal = yStartPos == yEndPos;
           return true;
       }
       else
           return false;
   }

   public boolean placed() {
       return getStartPos() != getEndPos();
   }
}
