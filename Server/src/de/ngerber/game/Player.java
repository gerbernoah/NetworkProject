package de.ngerber.game;

import com.sun.javafx.scene.traversal.Direction;

import java.awt.*;
import java.util.ArrayList;

public class Player
{
    private int x;
    private int y;
    private int toGrow;
    private final int speed;
    private final ArrayList<Point> tail;
    private Direction direction;
    private final Color color;

    public Player(int x, int y, Color color)
    {
        this.x = x;
        this.y = y;;
        this.color = color;
        speed = 5;
        direction = Direction.values()[(int) (Math.random() * 4)];
        tail = new ArrayList<>();
    }

    public void moveForward()
    {
        switch (direction)
        {
            case UP:
                y += speed;
                break;
            case DOWN:
                y -= speed;
                break;
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
        }
        tail.add(new Point(x,y));
        if (toGrow > 0)
            toGrow--;
        else
            tail.remove(0);
    }

    public void grow(int toGrow)
    {
        this.toGrow += toGrow;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public ArrayList<Point> getTail()
    {
        return tail;
    }

    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

    public Color getColor()
    {
        return color;
    }
}
