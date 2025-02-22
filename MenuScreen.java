import javax.swing.*; 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuScreen extends JPanel {
    private JFrame frame;
    private Image bgStart;

    public MenuScreen(JFrame frame) {
        this.frame = frame;
        this.setPreferredSize(new Dimension(360, 640));

        // Tải ảnh nền
        bgStart = new ImageIcon(getClass().getResource("/picture/bgStart.png")).getImage();

        // Tạo nút "Chơi Game"
        JButton playButton = new JButton("Play Game");
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        playButton.setFocusPainted(false);
        playButton.setBounds(100, 200, 160, 50);
        playButton.setBackground(Color.ORANGE);
        playButton.setForeground(Color.WHITE);

        // Tạo nút "Store Bird"
        JButton playButton1 = new JButton("Store Bird");
        playButton1.setFont(new Font("Arial", Font.BOLD, 20));
        playButton1.setFocusPainted(false);
        playButton1.setBounds(100, 300, 160, 50);
        playButton1.setBackground(Color.ORANGE);
        playButton1.setForeground(Color.WHITE);

        // Tạo nút "Thông Tin"
        JButton playButton2 = new JButton("Information");
        playButton2.setFont(new Font("Arial", Font.BOLD, 20));
        playButton2.setFocusPainted(false);
        playButton2.setBounds(100, 400, 160, 50);
        playButton2.setBackground(Color.ORANGE);
        playButton2.setForeground(Color.WHITE);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        playButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openStore();
            }
        });

        playButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openInformation();
            }
        });

        setLayout(null);
        add(playButton);
        add(playButton1);
        add(playButton2);
    }

    private void startGame() {
        frame.getContentPane().removeAll();
        FlappyBird game = new FlappyBird();
        frame.add(game);
        frame.pack();
        game.requestFocus();
        frame.revalidate();
        frame.repaint();
    }

    private void openStore() {
        frame.getContentPane().removeAll();
        StoreBird storeBird = new StoreBird(frame);
        frame.add(storeBird);
        frame.pack();
        frame.revalidate();
        frame.repaint();
    }

    private void openInformation() {
        frame.getContentPane().removeAll();
        Information information = new Information(frame);
        frame.add(information);
        frame.pack();
        frame.revalidate();
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgStart, 0, 0, getWidth(), getHeight(), this);
    }
}
