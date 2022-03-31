package de.ngerber.game;

import com.sun.javafx.scene.traversal.Direction;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable
{
    private final int id;
    private Point pos;
    private int toGrow;
    private int speed;
    private final ArrayList<Point> tail;
    private Direction direction;
    private Color color;

    public Player(int id, Point pos, Color color)
    {
        this.id = id;
        this.pos = pos;
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
                pos.y += speed;
                break;
            case DOWN:
                pos.y -= speed;
                break;
            case LEFT:
                pos.x -= speed;
                break;
            case RIGHT:
                pos.x += speed;
                break;
        }
        tail.add(new Point(pos.x,pos.y));
        if (toGrow > 0)
            toGrow--;
        else
            tail.remove(0);
    }

    public int getId()
    {
        return id;
    }

    public void grow(int toGrow)
    {
        this.toGrow += toGrow;
    }

    public Point getPos()
    {
        return pos;
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

    public void setColor(Color color)
    {
        this.color = color;
    }
}
