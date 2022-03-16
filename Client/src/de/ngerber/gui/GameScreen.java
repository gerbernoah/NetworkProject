package de.ngerber.gui;

import de.ngerber.game.Game;
import de.ngerber.game.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameScreen extends JLabel
{
    private final GUI gui;
    private final Game game;
    private final int squareSize;

    public GameScreen(GUI gui)
    {
        this.gui = gui;
        this.game = gui.getGame();
        squareSize = this.getWidth() / game.getFieldSize();

        this.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                
            }
        });
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Player player : game.getPlayers())
        {
            Point pos = player.getPos();
            g2d.fillRect(
                    pos.x * squareSize,
                    pos.y * squareSize,
                    squareSize,
                    squareSize);
        }
    }
}
