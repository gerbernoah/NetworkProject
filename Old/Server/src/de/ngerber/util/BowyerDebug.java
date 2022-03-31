package de.ngerber.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BowyerDebug extends JFrame
{
    private BowyerWatson bw;

    public BowyerDebug()
    {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(0,0,800, 800);
        bw = new BowyerWatson(getWidth(), getHeight());
        setVisible(true);

        this.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                repaint();
            }
        });
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        bw.drawDebug(g2d);
    }
}
