package server.socket;

import server.Server;
import server.UtilClient;
import shared.socket.CommandValues;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocketServer
{
    private final ArrayList<ClientHandler> clientHandlers;
    public SocketServer(int port)
    {
        clientHandlers = new ArrayList<>();
        acceptConnections(port);
    }

    private void acceptConnections(int port)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(port);
            for (int i = 0; i < 2; i++)
            {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(this, socket);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler::run).start();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
