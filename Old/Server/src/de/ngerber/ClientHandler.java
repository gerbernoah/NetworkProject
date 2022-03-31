package de.ngerber;

import com.sun.javafx.scene.traversal.Direction;
import de.ngerber.game.Player;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ClientHandler
{
    private final Server server;
    private final Socket socket;
    private boolean online;
    private boolean toUpdate;
    private boolean isRequestingAllPlayers;

    private final Player player;

    public ClientHandler(Server server, Socket socket, int id)
    {
        this.server = server;
        this.socket = socket;
        online = true;

        Point pos = server.getGame().getAvailableStartPosition();
        player = new Player(id, pos, Color.BLACK);
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
                    byte[] message = getMessage(inputStream);

                    switch (message[0])
                    {
                        case 0x00:
                        case 0x01:
                        case 0x02:
                        case 0x03:
                            //update player direction
                            player.setDirection(Direction.values()[message[0]]);
                            toUpdate = true;
                        case 0x04:
                            //shutdown
                            shutdown();
                            return;
                        case 0x05:
                            //update Player color
                            if (message.length < 5)
                                break;
                            player.setColor(new Color(
                                    Byte.toUnsignedInt(message[1]),
                                    Byte.toUnsignedInt(message[2]),
                                    Byte.toUnsignedInt(message[3]),
                                    Byte.toUnsignedInt(message[4])
                            ));
                            toUpdate = true;
                            break;
                        case 0x06:
                            //field
                            byte[] field = BigInteger.valueOf(server.getGame().getFieldSize()).toByteArray();
                            byte[] out = ByteBuffer.allocate(field.length + 1)
                                    .put(message[0])
                                    .put(field)
                                    .array();
                            addLenAndSendMessage(out);
                            toUpdate = true;
                            break;
                        case 0x07:
                            //complete setup received
                            isRequestingAllPlayers = true;
                            break;
                    }
                    if (toUpdate)
                    {
                        server.updatePlayer(player);
                        toUpdate = false;
                    }
                }
            }
        } catch (IOException | NullPointerException e)
        {
            e.printStackTrace();
            shutdown();
        }
    }

    private byte[] getMessage(InputStream inputStream) throws IOException
    {
        byte[] len = new byte[4];
        if (!tryReadUntilBufferFull(inputStream, len))
            throw new IOException();
        byte[] message = new byte[len[0] << 24 | len[1] << 16 | len[2] << 8 | len[3]];
        if (!tryReadUntilBufferFull(inputStream, message))
            throw new IOException();

        return message;
    }

    private boolean tryReadUntilBufferFull(InputStream inputStream, byte[] buffer)
    {
        if (buffer.length == 0)
            return false;
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

    public void addLenAndSendMessage(byte[] message)
    {
        try
        {
            byte[] len = ByteBuffer.allocate(4).putInt(message.length).array();
            byte[] out = ByteBuffer.allocate(len.length + message.length).put(len).put(message).array();

            socket.getOutputStream().write(out);
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

    public boolean isRequestingAllPlayers()
    {
        return isRequestingAllPlayers;
    }

    public Player getPlayer()
    {
        return player;
    }
}
