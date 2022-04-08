package client.gui;

import client.gui.components.GameLabel;

import javax.swing.*;
import java.awt.*;

public class PlaceField {

    private Container contentPane;

    private JPanel gamePanel = new JPanel(new GridLayout(10,10));
    private GameLabel[] gameComponents = new GameLabel[100];

    public PlaceField(Container container) {
        contentPane = container;
        contentPane.removeAll();
        setup();
    }

    private void setup() {

    }

    public Container getContentPane() {
        return contentPane;
    }

}

class MainPlaceField {
    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setVisible(true);
        jf.setSize(1024,720);
        PlaceField placefield = new PlaceField(jf.getContentPane());
        jf.setContentPane(placefield.getContentPane());
    }
}
