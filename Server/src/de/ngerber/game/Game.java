package de.ngerber.game;

import de.ngerber.ClientHandler;
import de.ngerber.util.BowyerWatson;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game
{
    private final int fieldSize = 50;
    private final ArrayList<ClientHandler> clientHandlers;
    private final BowyerWatson bw;

    public Game(ArrayList<ClientHandler> clientHandlers)
    {
        this.clientHandlers = clientHandlers;
        bw = new BowyerWatson(fieldSize, fieldSize);
    }

    public Point getAvailableStartPosition()
    {
        return null;
    }
}
