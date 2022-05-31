package client;

import server.field.Ship;

import java.awt.*;

public interface Client
{
    boolean placeShips(Point[] ships);
    int shoot(int pos);
    public void sendMessage(String message);
    boolean isPlayerOnTurn();
    void setShips(Ship[] ships);

}
