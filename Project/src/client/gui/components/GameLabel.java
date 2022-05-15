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
        reloadColor(false);
    }

    public boolean setShip(boolean ship) {
        if(this.ship || !placeable)
            return false;
        this.ship = ship;
        this.placeable = !ship;
        reloadColor(false);
        return true;
    }

    public void reloadColor(boolean hit) {
        if(hit) {
            this.setBackground(Color.RED);
        } else if(this.hit) {
            this.setBackground(Color.GRAY);
        } else if (ship) {
            this.setBackground(new Color(167,177,176));
        } else if (!placeable) {
            this.setBackground(new Color(165, 0, 24));
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
