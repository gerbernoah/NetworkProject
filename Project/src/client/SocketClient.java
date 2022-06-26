package client;

import client.gui.GameField;
import server.field.Ship;
import shared.socket.CommandValues;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class SocketClient implements Client
{
    private final Socket server;
    private final InetSocketAddress socketAddress;
    private boolean connected;
    private boolean running;
    private boolean playerOnTurn;
    private GameField gameField;
    private final JFrame jFrame;
    private Ship[] ships;

    public SocketClient(JFrame jFrame, String host, int port)
    {
        this.jFrame = jFrame;
        server = new Socket();
        socketAddress = new InetSocketAddress(host, port);
    }

    private void run()
    {
        running = true;
        try
        {
            InputStream inputStream = server.getInputStream();
            while (running)
            {
                if (inputStream.available() > 0)
                {
                    switch (getMessage()[0])
                    {
                        case CommandValues.SHUTDOWN:
                            shutdown();
                            break;
                    }
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Server disconnected");
        }
    }

    public boolean connect()
    {
        if (connected)
            return false;
        try
        {
            server.connect(socketAddress);
            connected = true;
            new Thread(this::run).start();
        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Could not connect to server" + socketAddress);
            return false;
        }
        return true;
    }

    private void shutdown()
    {
        running = false;
        try
        {
            server.close();
            System.out.println("Successfully closed connection to " + socketAddress);
        } catch (IOException e)
        {
            System.out.println("Already closed connection to " + socketAddress);
            throw new RuntimeException(e);
        }
    }

    private byte[] getMessage() throws IOException
    {
        byte[] len = new byte[4];
        if (!tryReadUntilBufferFull(len))
            throw new IOException();
        byte[] message = new byte[len[0] << 24 | len[1] << 16 | len[2] << 8 | len[3]];
        if (!tryReadUntilBufferFull(message))
            throw new IOException();

        return message;
    }

    private boolean tryReadUntilBufferFull(byte[] buffer)
    {
        if (buffer.length == 0)
            return false;
        try
        {
            int read = 0;
            InputStream inputStream = server.getInputStream();
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

    public void addLenAndSendMessage(byte[] message)
    {
        try
        {
            byte[] len = ByteBuffer.allocate(4)
                    .putInt(message.length)
                    .array();
            byte[] out = ByteBuffer.allocate(len.length + message.length)
                    .put(len)
                    .put(message)
                    .array();

            server.getOutputStream().write(out);
        } catch (IOException e)
        {
            System.out.println("Could not send Message to " + server.getInetAddress().getHostName());
        }
    }

    @Override
    public boolean placeShips(Point[] ships)
    {
        return false;
    }

    @Override
    public int shoot(int pos)
    {
        return 0;
    }

    @Override
    public void sendMessage(String message)
    {

    }

    @Override
    public boolean isPlayerOnTurn()
    {
        return false;
    }

    @Override
    public void setShips(Ship[] ships)
    {

    }
}
