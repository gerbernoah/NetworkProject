package de.ngerber.game;

import de.ngerber.Client;
import de.ngerber.gui.GUI;

import java.util.ArrayList;

public class Game
{
    private int fieldSize;
    private final Client client;
    private final ArrayList<Player> players;

    public Game(Client client)
    {
        this.client = client;
        players = new ArrayList<>();
        GUI gui = new GUI(this);
    }

    public Player getPlayerById(int id)
    {
        for (Player player : players)
        {
            if (player.getId() == id)
                return player;
        }
        return null;
    }

    public void updateOrAddPlayerBy(Player player)
    {
        for (int i = 0; i < players.size(); i++)
        {
            if (players.get(i).getId() == player.getId())
            {
                players.set(i, player);
                return;
            }
        }
        players.add(player);
    }

    public Client getClient()
    {
        return client;
    }

    public void setFieldSize(int fieldSize)
    {
        this.fieldSize = fieldSize;
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public int getFieldSize()
    {
        return fieldSize;
    }
}
