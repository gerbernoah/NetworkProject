package de.ngerber;

import de.ngerber.game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Server
{
    private final ArrayList<ClientHandler> clientHandlers;
    private final Stack<Integer> clientIds;
    private final Game game;

    public Server(int port, int maxConnections)
    {
        clientIds = new Stack<>();
        clientHandlers = new ArrayList<>();
        game = new Game(clientHandlers);

        acceptConnections(port, maxConnections);
    }

    private void acceptConnections(int port, int maxConnections)
    {
        try
        {
            clientIds.addAll(IntStream.range(0, maxConnections).boxed().collect(Collectors.toList()));
            ServerSocket serverSocket = new ServerSocket(port);
            while (true)
            {
                if (!clientIds.isEmpty())
                {
                    Socket socket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(this, socket, clientIds.pop());
                    clientHandlers.add(clientHandler);
                    new Thread(clientHandler::run).start();
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onDisconnect(ClientHandler clientHandler)
    {
        clientIds.push(clientHandler.getId());
        clientHandlers.remove(clientHandler);
    }

    public Game getGame()
    {
        return game;
    }
}
