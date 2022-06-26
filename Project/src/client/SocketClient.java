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
import java.util.Arrays;

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
        connect();
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
                    byte[] msg = getMessage();
                    switch (msg[0])
                    {
                        case CommandValues.SHUTDOWN:
                            shutdown();
                            break;

                        case CommandValues.SHOT:
                            shot(Byte.valueOf(msg[1]).intValue(), Byte.valueOf(msg[2]).intValue());
                            break;

                        case CommandValues.GAMESTART:
                            gameStart();
                            break;

                        case CommandValues.MSG:
                            messageReceived(String.valueOf(msg[1]));
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

    /**
     * @param pos pos of shot
     * @param onTurn: 0 = no hit; 1 = hit; 2 = hit + ship destroyed; 3 = hit + all ships destroyed;
     */
    private void shot(int pos, int onTurn)    //called from server
    {
        if (onTurn == 3)
            gameField.setGameEnd(false);
        setPlayerOnTurn(onTurn == 0);
        gameField.setGameComponentAsShot(0, pos);
    }

    public void gameStart()      //called from server
    {
        this.gameField = new GameField(jFrame.getContentPane(),this);
        this.gameField.setupShips(ships);
        setPlayerOnTurn(playerOnTurn);
    }

    public void messageReceived(String message)      //called from server
    {
        gameField.messageReceived(message);
    }

    public boolean connect()
    {
        if (connected)
            return false;
        try
        {
            server.connect(socketAddress);
            connected = true;
            addLenAndSendMessage(new byte[]{CommandValues.REGCLIENT});
            playerOnTurn = getMessage()[2] == 1;
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

    public void addLenAndSendMessage(byte[]... messages)
    {
        try
        {
            int size = 0;
            for (byte[] message : messages)
                size += message.length;
            byte[] len = ByteBuffer.allocate(4).putInt(size).array();
            ByteBuffer outBuffer = ByteBuffer.allocate(len.length + size).put(len);
            for (byte[] message : messages)
                outBuffer.put(message);
            byte[] out = outBuffer.array();

            server.getOutputStream().write(out);
        } catch (IOException e)
        {
            System.out.println("Could not send Message to " + server.getInetAddress().getHostName());
        }
    }

    @Override
    public boolean placeShips(Point[] ships)        //called from game field
    {
        byte[] byteShips = new byte[ships.length * 2];
        for (int i = 0; i < byteShips.length; i+=2)
        {
            byteShips[i] = Double.valueOf(ships[i/2].getX()).byteValue();
            byteShips[i+1] = Double.valueOf(ships[i/2].getY()).byteValue();
        }
        addLenAndSendMessage(new byte[]{CommandValues.CLREADY});
        addLenAndSendMessage(new byte[]{CommandValues.PLACESHIPS}, byteShips);

        return true;        //todo check return of PlaceShips
    }

    /**
     * @param pos destination to shoot
     * @return if shot was hit; 0 = no hit; 1 = hit; 2 = hit + ship destroyed; 3 = hit + all ships destroyed;
     */
    @Override
    public int shoot(int pos)       //called from game field
    {
        try
        {
            addLenAndSendMessage(new byte[]{CommandValues.SHOOT, Integer.valueOf(pos).byteValue()});
            int hit = getMessage()[2];
            setPlayerOnTurn(hit > 0);
            return hit;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void sendMessage(String message)     //called from game field
    {
        addLenAndSendMessage(new byte[]{CommandValues.MSG}, message.getBytes());
    }

    @Override
    public boolean isPlayerOnTurn()
    {
        return playerOnTurn;
    }

    @Override
    public void setShips(Ship[] ships)
    {
        this.ships = ships;
    }

    private void setPlayerOnTurn(boolean onTurn)
    {
        gameField.colorBorder(onTurn);
        playerOnTurn = onTurn;
    }
}
