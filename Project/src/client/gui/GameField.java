package client.gui;

import client.Client;
import client.gui.components.GameLabel;
import client.gui.components.HintTextField;
import server.field.Ship;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLOutput;
import java.text.DecimalFormat;

public class GameField {

    private final Container contentPane;
    private final Client client;

    private final JPanel[] gamePanel = new JPanel[2];
    private final GameLabel[][] gameComponents = new GameLabel[2][100];

    private final JTextArea chatArea = new JTextArea();
    private final JScrollPane scrollPane = new JScrollPane();
    private final JTextField chatField = new HintTextField("Nachricht eingeben..");
    private final JButton sendMessage = new JButton("Senden");

    private final JLabel headerYou = new JLabel("Eigene Schiffe: ");
    private final JLabel headerEnemy = new JLabel("Gegnerische Schiffe: ");

    private final JPanel statsYou = new JPanel(new GridLayout(3, 2));
    private final JPanel statsEnemy = new JPanel(new GridLayout(3, 2));

    private final JLabel[] headerStats = new JLabel[2];

    private final JLabel[] shotsYou = new JLabel[2];
    private final JLabel[] hitsYou = new JLabel[2];
    private final JLabel[] hitRateYou = new JLabel[2];

    private final JLabel[] shotsEnemy = new JLabel[2];
    private final JLabel[] hitsEnemy = new JLabel[2];
    private final JLabel[] hitRateEnemy = new JLabel[2];

    private int[] shots = new int[2];
    private int[] hits = new int[2];

    public GameField(Container contentPane, Client client) {
        this.client = client;
        this.contentPane = contentPane;
        contentPane.setLayout(null);
        contentPane.removeAll();
        contentPane.repaint();
        setup();
    }

    private void setup() {
        contentPane.add(headerYou);
        contentPane.add(headerEnemy);
        headerYou.setBounds(50, 280, 300, 40);
        headerYou.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
        headerEnemy.setBounds(654, 280, 300, 40);
        headerEnemy.setFont(new Font(Font.DIALOG, Font.BOLD, 25));
        setupGameFieldOnPanel(0);
        setupGameFieldOnPanel(1);
        setupStats();
        setupChat();
    }

    private void setupGameFieldOnPanel(int player) {
        gamePanel[player] = new JPanel(new GridLayout(10,10));
        gamePanel[player].setBorder(new LineBorder(Color.BLACK));
        gamePanel[player].setOpaque(true);
        gamePanel[player].setBackground(Color.BLACK);
        for(int i = 0; i < 100; i++) {
            gameComponents[player][i] = new GameLabel();
            gamePanel[player].add(gameComponents[player][i]);
            if(player == 1) {
                int finalI = i;
                gameComponents[player][i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(client.isPlayerOnTurn()) {
                            int shoot = client.shoot(finalI);
                            boolean hit = shoot != 0;
                            setGameComponentAsShot(player, finalI, hit);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        gameComponents[player][finalI].setBackground(Color.decode("#5db5fc"));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        gameComponents[player][finalI].reloadColor(false);
                    }
                });
            }
        }
        contentPane.add(gamePanel[player]);

        switch (player) {
            case 0: gamePanel[0].setBounds(50, 320, 320, 320); break;
            case 1: gamePanel[1].setBounds(654, 320, 320, 320); break;
        }
    }

    private void setupChat() {

        contentPane.add(chatField);
        contentPane.add(sendMessage);
        contentPane.add(scrollPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.add(chatArea);
        scrollPane.setBounds(50,50, 924, 150);
        scrollPane.setViewportView(chatArea);
        chatArea.setEditable(false);
        chatArea.setBorder(new LineBorder(Color.black));
        chatField.setBounds(50, 210, 774, 40);
        sendMessage.setBounds(834, 210, 140, 40);
        chatField.requestFocus();

        sendMessage.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                sendMessage();
            }
        });

        chatField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    sendMessage();
            }
        });
    }

    private void sendMessage()
    {
        String message = chatField.getText();
        client.sendMessage(message);
        chatField.setText("");
        chatArea.append("You: " + message + "\n");
    }

    public void messageReceived(String message)
    {
        chatArea.append("Opponent: " + message + "\n");
    }

    private void setupStats() {
        contentPane.add(statsYou);
        contentPane.add(statsEnemy);

        statsYou.setBounds(400, 360, 200, 110);
        statsEnemy.setBounds(400, 510, 200, 110);

        headerStats[0] = new JLabel("Deine Stats: ", SwingConstants.LEFT);
        headerStats[1] = new JLabel("Gegner Stats: ", SwingConstants.LEFT);
        contentPane.add(headerStats[0]);
        contentPane.add(headerStats[1]);
        headerStats[0].setBounds(400,320,160,40);
        headerStats[1].setBounds(400,470, 160,40);
        headerStats[0].setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        headerStats[1].setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        shotsYou[0] = new JLabel("Geschossen: ");
        shotsYou[1] = new JLabel("0", SwingConstants.CENTER);
        hitsYou[0] = new JLabel("Getroffen: ");
        hitsYou[1] = new JLabel("0", SwingConstants.CENTER);
        hitRateYou[0] = new JLabel("Trefferquote: ");
        hitRateYou[1] = new JLabel("100%", SwingConstants.CENTER);

        shotsEnemy[0] = new JLabel("Geschossen: ");
        shotsEnemy[1] = new JLabel("0", SwingConstants.CENTER);
        hitsEnemy[0] = new JLabel("Getroffen: ");
        hitsEnemy[1] = new JLabel("0", SwingConstants.CENTER);
        hitRateEnemy[0] = new JLabel("Trefferquote: ");
        hitRateEnemy[1] = new JLabel("100%", SwingConstants.CENTER);

        statsYou.add(shotsYou[0]);
        statsYou.add(shotsYou[1]);
        statsYou.add(hitsYou[0]);
        statsYou.add(hitsYou[1]);
        statsYou.add(hitRateYou[0]);
        statsYou.add(hitRateYou[1]);

        statsEnemy.add(shotsEnemy[0]);
        statsEnemy.add(shotsEnemy[1]);
        statsEnemy.add(hitsEnemy[0]);
        statsEnemy.add(hitsEnemy[1]);
        statsEnemy.add(hitRateEnemy[0]);
        statsEnemy.add(hitRateEnemy[1]);
    }

    public void setupShips(Ship[] ships) {
        for(Ship ship : ships) {
            for(int pos : ship.getPositions()) {
                gameComponents[0][pos].setShip(true);
            }
        }
        contentPane.repaint();
    }

    public void setGameComponentAsShot(int player, int id) {
        gameComponents[player][id].setHit(true);
        gameComponents[player][id].reloadColor(gameComponents[player][id].isShip());
        for (MouseListener act : gameComponents[player][id].getMouseListeners()) {
            gameComponents[player][id].removeMouseListener(act);
        }
        increaseShots(1, gameComponents[player][id].isShip() == gameComponents[player][id].isHit());
    }
    public void setGameComponentAsShot(int player, int id, boolean hit) {
        gameComponents[player][id].setHit(true);
        gameComponents[player][id].reloadColor(hit);
        increaseShots(0, hit);
        for (MouseListener act : gameComponents[player][id].getMouseListeners()) {
            gameComponents[player][id].removeMouseListener(act);
        }
    }

    public void colorBorder(boolean ownBorder)
    {
        if (ownBorder)
        {
            gamePanel[0].setBackground(Color.BLACK);
            gamePanel[1].setBackground(Color.RED);
        }
        else
        {
            gamePanel[0].setBackground(Color.RED);
            gamePanel[1].setBackground(Color.BLACK);
        }
    }

    private void updateStats() {
        hitsEnemy[1].setText(String.valueOf(hits[1]));
        shotsEnemy[1].setText(String.valueOf(shots[1]));

        hitsYou[1].setText(String.valueOf(hits[0]));
        shotsYou[1].setText(String.valueOf(shots[0]));
        double[] hitRate = new double[2];
        DecimalFormat df = new DecimalFormat("###.##");
        if(shots[1] != 0) {
            hitRate[1] = Double.valueOf(hits[1])/Double.valueOf(shots[1])*100;
            System.out.println("hits1  "+ hits[1]);
            System.out.println("shoots1  " + shots[1]);
            System.out.println(hitRate[1]);
            hitRateEnemy[1].setText(df.format(hitRate[1]) + "%");
        }
        if(shots[0] != 0) {
            hitRate[0] = Double.valueOf(hits[0])/Double.valueOf(shots[0])*100;
            System.out.println("hits0  "+ hits[0]);
            System.out.println("shoots0  " + shots[0]);
            System.out.println(hitRate[0]);
            hitRateYou[1].setText(df.format(hitRate[0]) + "%");
        }
    }

    private void increaseShots(int player, boolean hit) {
        shots[player]++;
        if(hit) hits[player]++;
        updateStats();
    }

    public void setGameEnd(String winMessage, Color color) {
        contentPane.removeAll();
        JLabel messageLabel = new JLabel(winMessage, SwingConstants.CENTER);
        messageLabel.setFont(new Font(messageLabel.getFont().getName(), messageLabel.getFont().getStyle(), 50));
        messageLabel.setForeground(color);
        messageLabel.setBounds(300, 220, 424, 120);
        contentPane.add(messageLabel);
    }
}
