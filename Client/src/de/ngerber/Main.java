package de.ngerber;

import com.sun.security.ntlm.Server;

public class Main
{
    public static void main(String[] args)
    {
        new Client("localhost", 8080);
    }
}
