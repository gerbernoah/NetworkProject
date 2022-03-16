package de.ngerber.gui;

import de.ngerber.game.Game;
import de.ngerber.game.Player;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame
{
    private Game game;
    private int squareSize;

    public GUI(Game game)
    {
        this.game = game;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(0,0,800, 800);
        setResizable(false);

        MenuScreen menuScreen = new MenuScreen(this);
        GameScreen gameScreen = new GameScreen(this);

        setLayout(null);
        setVisible(true);
    }

    public Game getGame()
    {
        return game;
    }
}
