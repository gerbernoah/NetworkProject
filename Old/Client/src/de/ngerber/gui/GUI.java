package de.ngerber.gui;

import de.ngerber.game.Game;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GUI extends JFrame
{
    private Game game;
    private final Screen menuScreen;
    private Screen gameScreen;
    private int squareSize;
    private KeyListener keyListener;

    public GUI(Game game)
    {
        this.game = game;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(0,0,800, 800);
        setResizable(false);

        menuScreen = new MenuScreen(this);
        setContentPane(menuScreen);

        setLayout(null);
        setVisible(true);

        keyListener = new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                switch (e.getKeyCode())
                {
                    case KeyEvent.VK_ESCAPE:
                    setContentPane(((Screen) getContentPane()).getEscapeScreen());
                    break;
                    default:
                        System.out.println("key pressed");
                }
            }
        };
    }

    public Game getGame()
    {
        return game;
    }

    public Screen getGameScreen()
    {
        return gameScreen;
    }

    public Screen getMenuScreen()
    {
        return menuScreen;
    }
}
