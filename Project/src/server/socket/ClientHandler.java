package server.socket;

import server.UtilClient;
import shared.socket.CommandValues;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ClientHandler extends UtilClient
{
    private final SocketServer server;
    private final Socket socket;
    private boolean online;

    public ClientHandler(SocketServer server, Socket socket)
    {
        super();
        this.server = server;
        this.socket = socket;
        online = true;
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
                    byte[] message = getMessage();
                    System.out.println(Arrays.toString(message));
                    switch (message[0])
                    {
                        case CommandValues.SHUTDOWN:
                            closeConnection();
                            break;

                        case CommandValues.REGCLIENT:
                            addLenAndSendMessage(new byte[]{CommandValues.RETURN, CommandValues.REGCLIENT, Integer.valueOf(getId()).byteValue()});
                            break;

                        case CommandValues.SHOOT:
                            int hit = server.shoot(this, Byte.valueOf(message[1]).intValue());
                            addLenAndSendMessage(new byte[]{CommandValues.RETURN, CommandValues.SHOOT, Integer.valueOf(hit).byteValue()});
                            break;

                        case CommandValues.PLACESHIPS:
                            placeShips(Arrays.copyOfRange(message, 1, message.length));
                            break;

                        case CommandValues.MSG:
                            server.messageReceived(this, Arrays.toString(message));
                            break;

                        case CommandValues.CLREADY:
                            //server.clientReady(this);
                            gameStart();
                            break;

                    }
                }
            }
        } catch (IOException | NullPointerException e)
        {
            e.printStackTrace();
            closeConnection();
        }
    }

    public void shot(int pos, int onTurn)   //called from server
    {
        addLenAndSendMessage(new byte[]{
                        CommandValues.SHOT,
                        Integer.valueOf(pos).byteValue(),
                        Integer.valueOf(onTurn).byteValue()
        });
    }

    private void placeShips(byte[] byteShips)    //called from client
    {
        Point[] ships = new Point[byteShips.length/2];
        for (int i = 0; i < byteShips.length - 1; i+=2)
        {
            ships[i / 2] = new Point(byteShips[i], byteShips[i + 1]);
        }
        boolean valid = server.placeShips(this, ships); //returns if placement was valid
        addLenAndSendMessage(new byte[]{CommandValues.RETURN, CommandValues.PLACESHIPS, Integer.valueOf(valid ? 1 : 0).byteValue()});
    }

    public void gameStart()      //called from server
    {
        addLenAndSendMessage(new byte[]{CommandValues.GAMESTART});
    }

    public void msgToClient(String message)      //called from server
    {
        addLenAndSendMessage(new byte[]{CommandValues.MSG}, message.getBytes());
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
            InputStream inputStream = socket.getInputStream();
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

            socket.getOutputStream().write(out);
        } catch (IOException e)
        {
            System.out.println("Could not send Message to " + socket.getInetAddress().getHostName());
        }
    }

    public void shutdown()  //shutdown the client
    {
        addLenAndSendMessage(new byte[]{CommandValues.SHUTDOWN});
        closeConnection();
    }

    private void closeConnection()
    {
        if (!online)
        {
            System.out.println("Already disconnected from " + socket.getInetAddress().getHostName());
            return;
        }
        online = false;
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
}
