package de.ngerber.gui;

import de.ngerber.Client;

import javax.swing.*;

public class MenuScreen extends JLabel
{
    public MenuScreen(GUI gui)
    {
        JButton connect = new JButton();
        connect.setBounds(100,100,400,100);
        connect.addActionListener(e -> gui.getGame().getClient().connect());
        this.add(connect);
    }
}
