package de.ngerber.gui;

import de.ngerber.game.Game;
import de.ngerber.game.Player;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame
{
    private final JButton connect;
    private final Game game;
    private int squareSize;

    public GUI(Game game)
    {
        this.game = game;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(0,0,800, 800);
        setLayout(null);

        connect = new JButton();
        connect.setBounds(100,100,400,100);
        connect.addActionListener(e -> game.getClient().connect());
        this.add(connect);

        setVisible(true);
    }

    public JButton getConnect()
    {
        return connect;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Player player : game.getPlayers())
        {
            squareSize = this.getWidth()/game.getFieldSize(); //todo: split contentpane and init in game content screen constructur
            Point pos = player.getPos();
            g2d.fillRect(
                    pos.x * squareSize,
                    pos.y * squareSize,
                    squareSize,
                    squareSize);
        }
    }
}
