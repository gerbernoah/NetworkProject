package client.gui;

import client.gui.components.GameLabel;
import client.gui.components.HintTextField;
import client.gui.components.Ship;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class GameField {

    private Container contentPane;

    private JPanel[] gamePanel = new JPanel[2];
    private GameLabel[][] gameComponents = new GameLabel[2][100];

    private JTextArea chatArea = new JTextArea();
    private JTextField chatField = new HintTextField("Nachricht eingeben..");
    private JButton sendMessage = new JButton("Senden");

    private JLabel headerYou = new JLabel("Eigene Schiffe: ");
    private JLabel headerEnemy = new JLabel("Gegnerische Schiffe: ");

    private JPanel statsYou = new JPanel(new GridLayout(3, 2));
    private JPanel statsEnemy = new JPanel(new GridLayout(3, 2));

    private JLabel[] headerStats = new JLabel[2];

    private JLabel[] shotsYou = new JLabel[2];
    private JLabel[] hitsYou = new JLabel[2];
    private JLabel[] hitRateYou = new JLabel[2];

    private JLabel[] shotsEnemy = new JLabel[2];
    private JLabel[] hitsEnemy = new JLabel[2];
    private JLabel[] hitRateEnemy = new JLabel[2];

    public GameField(Container contentPane) {
        this.contentPane = contentPane;
        contentPane.setLayout(null);
        contentPane.removeAll();
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
        setupChat();
        setupStats();
    }

    private void setupGameFieldOnPanel(int player) {
        gamePanel[player] = new JPanel(new GridLayout(10,10));
        gamePanel[player].setBorder(new LineBorder(Color.BLACK));
        gamePanel[player].setOpaque(true);
        gamePanel[player].setBackground(Color.BLACK);
        for(int i = 0; i < 100; i++) {
            gameComponents[player][i] = new GameLabel(i);
            gamePanel[player].add(gameComponents[player][i]);
        }
        contentPane.add(gamePanel[player]);

        switch (player) {
            case 0 : gamePanel[0].setBounds(50,320, 320, 320); break;
            case 1 : gamePanel[1].setBounds(654,320, 320, 320); break;
        }
    }

    private void setupChat() {
        contentPane.add(chatArea);
        contentPane.add(chatField);
        contentPane.add(sendMessage);
        chatArea.setBounds(50,50, 924, 150);
        chatArea.setEditable(false);
        chatField.setBounds(50, 210, 774, 40);
        sendMessage.setBounds(834, 210, 140, 40);
        chatArea.setBorder(new LineBorder(Color.black));
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

    public Container getContentPane() {
        return contentPane;
    }

    public void setContentPane(Container contentPane) {
        this.contentPane = contentPane;
    }

    public void setFieldListener(MouseAdapter act, int id, int player) {
        gameComponents[player][id].addMouseListener(act);
    }

    public void setupShips(Ship[] ships) {
        for(Ship ship : ships) {
            for(int pos : ship.getPosition()) {
                gameComponents[0][pos].setShip(true);
            }
        }
        contentPane.repaint();
    }

    public void setGameComponentAsHit(int player, int id) {
        gameComponents[player][id].setHit(true);
    }

}

class MainGameField {
    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setVisible(true);
        jf.setSize(1024,720);
        GameField gamefield = new GameField(jf.getContentPane());
        jf.setContentPane(gamefield.getContentPane());
    }
}