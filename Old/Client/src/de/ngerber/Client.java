package de.ngerber;

import de.ngerber.game.Game;
import de.ngerber.game.Player;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Client
{
    private final Socket server;
    private final InetSocketAddress socketAddress;
    private final Game game;
    private Player player;
    private boolean connected;


    public Client(String host, int port)
    {
        server = new Socket();
        socketAddress = new InetSocketAddress(host, port);
        game = new Game(this);
    }

    private void run()
    {
        try
        {
            InputStream inputStream = server.getInputStream();

            //field setup
            addLenAndSendMessage(new byte[]{0x06});
            byte[] message = getMessage(inputStream);
            assert message[0] == 0x06;
            game.setFieldSize(message[1]);

            while (true)
            {
                if (inputStream.available() > 0)
                {
                    message = getMessage(inputStream);

                    switch (message[0])
                    {
                        case 0x00:
                            //update Player
                            ByteArrayInputStream bis = new ByteArrayInputStream(message, 1, message.length - 1);
                            ObjectInputStream ois = new ObjectInputStream(bis);
                            try
                            {
                                Player updatePlayer = (Player) ois.readObject();
                                game.updateOrAddPlayerBy(updatePlayer);
                                if (player == null)
                                {
                                    player = updatePlayer;
                                    addLenAndSendMessage(new byte[]{0x07});
                                }
                            } catch (ClassNotFoundException e)
                            {
                                e.printStackTrace();
                                System.out.println("Player could not be deserialized");
                            }
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

    public void disconnect()
    {
        addLenAndSendMessage(new byte[]{0x04});
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
}
