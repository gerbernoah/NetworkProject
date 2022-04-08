package client.gui.components;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GameLabel extends JLabel {

    private int id;
    private boolean ship = false;
    private boolean hit = false;
    private boolean placeable = true;

    public GameLabel(int id) {
        this.setOpaque(true);
        this.setBorder(new LineBorder(Color.BLACK));
        this.id = id;
        reloadColor();
    }

    public boolean setShip(boolean ship) {
        if(ship && !placeable) return false;
        this.ship = ship;
        this.placeable = !ship;
        reloadColor();
        return true;
    }

    public void reloadColor() {
        if (ship) {
            this.setBackground(new Color(167,177,176));
        } else if (!ship && !placeable) {
            this.setBackground(Color.pink);
        } else {
            this.setBackground(Color.decode("#038cfc"));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isShip() {
        return ship;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public boolean isPlaceable() {
        return placeable;
    }

    public void setPlaceable(boolean placeable) {
        this.placeable = placeable;
    }
}
