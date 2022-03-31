package de.ngerber;

public class Main
{
    public static void main(String[] args)
    {
        new Server(8080, 10); //maxPlayer < 30 because of BeyerWatson else increase "FieldSize" in Game
    }
}
