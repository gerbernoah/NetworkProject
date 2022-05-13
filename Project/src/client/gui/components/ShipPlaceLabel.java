package client.gui.components;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ShipPlaceLabel extends JLabel {

    private String name;
    private int length;

    public ShipPlaceLabel(String name, int length) {
        this.name = name;
        this.setText(name);
        this.length = length;
        this.setOpaque(true);
        this.setBackground(Color.lightGray);
        this.setBorder(new LineBorder(Color.BLACK));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
    }

    public int getLength() {
        return length;
    }

}
