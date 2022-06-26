package server.socket;

import server.Server;
import server.UtilClient;
import shared.socket.CommandValues;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ClientHandler implements Server
{
    private final SocketServer server;
    private final Socket socket;
    private boolean online;

    public ClientHandler(SocketServer server, Socket socket)
    {
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

                    switch (message[0])
                    {

                    }
                }
            }
        } catch (IOException | NullPointerException e)
        {
            e.printStackTrace();
            closeConnection();
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
            closeConnection();
        }
    }

    public void shutdown()
    {
        addLenAndSendMessage(new byte[]{CommandValues.SHUTDOWN});
        closeConnection();
    }

    private void closeConnection()
    {
        if (!online)
        {
            System.out.println("Already disconnected from " + socket.getInetAddress().getHostName());
            return;y
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

    @Override
    public void shot(UtilClient pClient, int pos, int onTurn)
    {

    }
}
