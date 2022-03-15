package de.ngerber;

import de.ngerber.game.Game;
import de.ngerber.game.Player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
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


    public void updatePlayer(Player player)
    {
        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(player);
            oos.flush();
            byte[] bytePlayerData = bos.toByteArray();

            for (ClientHandler clientHandler : clientHandlers)
            {
                if (clientHandler.isRequestingAllPlayers())
                {
                    byte[] bytePlayer = ByteBuffer.allocate(bytePlayerData.length + 1)
                            .put(new byte[]{0x00})
                            .put(bytePlayerData)
                            .array();
                    clientHandler.addLenAndSendMessage(bytePlayer);
                }
                else if (clientHandler.getPlayer() == player)
                {
                    byte[] bytePlayer = ByteBuffer.allocate(bytePlayerData.length + 1)
                            .put(new byte[]{0x00})
                            .put(bytePlayerData)
                            .array();
                    clientHandler.addLenAndSendMessage(bytePlayer);
                }
            }

        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Could not serialize Player");
        }
    }

    public void onDisconnect(ClientHandler clientHandler)
    {
        clientIds.push(clientHandler.getPlayer().getId());
        clientHandlers.remove(clientHandler);
    }

    public Game getGame()
    {
        return game;
    }
}
