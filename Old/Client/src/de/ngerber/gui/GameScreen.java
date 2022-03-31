package de.ngerber.gui;

import de.ngerber.game.Game;
import de.ngerber.game.Player;

import java.awt.*;

public class GameScreen extends Screen
{
    private final GUI gui;
    private final Game game;
    private final int squareSize;

    public GameScreen(GUI gui)
    {
        this.gui = gui;
        this.game = gui.getGame();
        squareSize = this.getWidth() / game.getFieldSize();
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

    @Override
    public Screen getEscapeScreen()
    {
        return gui.getMenuScreen();
    }
}
