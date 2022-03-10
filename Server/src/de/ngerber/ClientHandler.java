package de.ngerber;

import de.ngerber.game.Player;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Random;

public class ClientHandler
{
    private final Server server;
    private final Socket socket;
    private boolean online;
    private final int id;

    private final Player player;

    public ClientHandler(Server server, Socket socket, int id)
    {
        this.server = server;
        this.socket = socket;
        this.id = id;
        online = true;

        Point pos = server.getGame().getAvailableStartPosition();
        player = new Player(pos.x, pos.y, Color.BLACK);
    }

    public void run()
    {
        try
        {
            InputStream inputStream = socket.getInputStream();
            while (online)
            {
                if (inputStream.available() > 0)
                {
                    byte[] len = new byte[4];
                    if (!tryReadUntilBufferFull(inputStream, len))
                        shutdown();
                    byte[] message = new byte[len[0] << 24 | len[1] << 16 | len[2] << 8 | len[3]];
                    if (!tryReadUntilBufferFull(inputStream, message))
                        shutdown();

                    if (message[0] == 0x00)
                        //no player movement
                        switch (message[1])
                        {

                        }
                    else
                    {

                    }
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
            shutdown();
        }
    }

    private boolean tryReadUntilBufferFull(InputStream inputStream, byte[] buffer)
    {
        try
        {
            int read = 0;
            while (read < buffer.length)
            {
                int readBytes = inputStream.read(buffer, read, buffer.length - read);
                if (readBytes == -1)
                    return false;
                read += readBytes;
            }
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    public void sendMessage(byte[] message)
    {
        byte[] len = ByteBuffer.allocate(4).putInt(message.length).array();
        byte[] output = ByteBuffer.allocate(len.length + message.length).put(len).put(message).array();

        try
        {
            socket.getOutputStream().write(output);
        } catch (IOException e)
        {
            System.out.println("Could not send Message to " + socket.getInetAddress().getHostName());
            shutdown();
        }
    }

    public void shutdown()
    {
        online = false;
        server.onDisconnect(this);
        try
        {
            socket.close();
            System.out.println(socket.getInetAddress().getHostName() + " successfully closed");
        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Could not close " + socket.getInetAddress().getHostName());
        }
    }

    public int getId()
    {
        return id;
    }

    public Player getPlayer()
    {
        return player;
    }
}
