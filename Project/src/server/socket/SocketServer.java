package server.socket;

import server.Controller;
import server.Server;
import server.UtilClient;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocketServer implements Server
{
    private final ArrayList<ClientHandler> clientHandlers;
    private final Controller controller;

    private boolean gameStarted = false;
    public SocketServer(int port)
    {
        controller = new Controller(this);
        clientHandlers = new ArrayList<>();
        acceptConnections(port);
    }

    private void acceptConnections(int port)
    {
        try
        {
            System.out.println("server " + InetAddress.getLocalHost() + " running on port " + port);
            ServerSocket serverSocket = new ServerSocket(port);
            for (int i = 0; i < 2; i++)
            {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(this, socket);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler::run).start();
                System.out.println("Socket " + socket.getInetAddress() + " successfully connected");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void shot(UtilClient pClient, int pos, int onTurn)   //called from controller
    {
        for (ClientHandler client : clientHandlers)
        {
            if (client == pClient)
                continue;
            client.shot(pos, onTurn);
        }
    }

    public int shoot(ClientHandler pClient, int pos)   //called from client
    {
        ClientHandler dstClient = null;
        for (ClientHandler client : clientHandlers)
        {
            if (client == pClient)
                continue;
            else
                dstClient = client;
            break;
        }

        for (ClientHandler client : clientHandlers)
        {
            if (client == pClient)
                return controller.shoot(client, pos, dstClient);  //returns if shot was a hit
        }
        System.out.println("client not found");
        return -1;
    }

    public boolean placeShips(ClientHandler pClient, Point[] ships)    //called from client
    {
        for (ClientHandler client : clientHandlers)
        {
            if (client == pClient)
                return controller.placeShips(client, ships); //returns if placement was valid
        }
        System.out.println("client not found");
        return false;
    }

    public void clientReady(ClientHandler pClient) //called from client
    {
        if (gameStarted)
            return;

        boolean allReady = true;
        for (ClientHandler client : clientHandlers)
        {
            if (client == pClient)
                client.setReady(true);
            if (!client.isReady())
                allReady = false;
        }

        if (allReady)
        {
            clientHandlers.forEach(ClientHandler::gameStart);
            gameStarted = true;
        }
    }

    public void messageReceived(ClientHandler pClient, String message) //called from client
    {
        for (ClientHandler client : clientHandlers)
        {
            if (client == pClient)
                continue;
            else
                client.msgToClient(message);
            break;
        }
    }
    public static void main(String[] args)
    {
        new SocketServer(1099);
    }
}
