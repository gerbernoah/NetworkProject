package client.gui.components;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GameLabel extends JLabel {

    private boolean ship = false;
    private boolean hit = false;
    private boolean placeable = true;

    public GameLabel() {
        this.setOpaque(true);
        this.setBorder(new LineBorder(Color.BLACK));
        reloadColor(false);
    }

    public void setShip(boolean ship) {
        this.ship = ship;
        reloadColor(false);
    }

    public void reloadColor(boolean hit) {
        if(hit) {
            this.setBackground(Color.RED);
        } else if(this.hit) {
            this.setBackground(Color.GRAY);
        } else if (ship) {
            this.setBackground(new Color(167,177,176));
        } else {
            this.setBackground(Color.decode("#038cfc"));
        }
    }

    public boolean isShip() {
        return ship;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public boolean isPlaceable() {
        return placeable;
    }

    public void setPlaceable(boolean placeable) {
        this.placeable = placeable;
        reloadColor(false);
    }
}
