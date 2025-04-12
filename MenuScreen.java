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
        ImageIcon play = new ImageIcon(new ImageIcon(getClass().getResource("/picture/buttonPlay.png"))
                                    .getImage().getScaledInstance(180, 60, Image.SCALE_SMOOTH));
        JButton playButton = new JButton(play);
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        playButton.setFocusPainted(false);
        playButton.setBounds(90, 200, 180, 60);
        playButton.setContentAreaFilled(false); // Làm trong suốt nền và viền
        playButton.setBorderPainted(false);
        playButton.setOpaque(false);
        playButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Tùy chọn: hiệu ứng con trỏ tay khi di chuột
        

        // Tạo nút "Store Bird"
        ImageIcon store = new ImageIcon(new ImageIcon(getClass().getResource("/picture/buttonStore.png"))
                                    .getImage().getScaledInstance(180, 60, Image.SCALE_SMOOTH));
        JButton btstore = new JButton(store);
        btstore.setFont(new Font("Arial", Font.BOLD, 20));
        btstore.setFocusPainted(false);
        btstore.setBounds(90, 275, 180, 60);
        btstore.setContentAreaFilled(false); // Làm trong suốt nền và viền
        btstore.setBorderPainted(false);
        btstore.setOpaque(false);
        btstore.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Tùy chọn: hiệu ứng con trỏ tay khi di chuột

        // Tạo nút "cài đặt"
        ImageIcon setting = new ImageIcon(new ImageIcon(getClass().getResource("/picture/buttonSetting.png"))
                                    .getImage().getScaledInstance(180, 60, Image.SCALE_SMOOTH));
        JButton btsetting = new JButton(setting);
        btsetting.setFont(new Font("Arial", Font.BOLD, 20));
        btsetting.setFocusPainted(false);
        btsetting.setBounds(90, 350, 180, 60);
        btsetting.setContentAreaFilled(false); // Làm trong suốt nền và viền
        btsetting.setBorderPainted(false);
        btsetting.setOpaque(false);
        btsetting.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Tùy chọn: hiệu ứng con trỏ tay khi di chuột


        // Tạo nút "Thông Tin"
        ImageIcon about = new ImageIcon(new ImageIcon(getClass().getResource("/picture/buttonAbout.png"))
                                    .getImage().getScaledInstance(180, 60, Image.SCALE_SMOOTH));
        JButton btabout = new JButton(about);
        btabout.setFont(new Font("Arial", Font.BOLD, 20));
        btabout.setFocusPainted(false);
        btabout.setBounds(90, 425, 180, 60);
        btabout.setContentAreaFilled(false); // Làm trong suốt nền và viền
        btabout.setBorderPainted(false);
        btabout.setOpaque(false);
        btabout.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Tùy chọn: hiệu ứng con trỏ tay khi di chuột

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        btstore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openStore();
            }
        });

        btabout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openInformation();
            }
        });

        setLayout(null);
        add(playButton);
        add(btstore);
        add(btsetting);
        add(btabout);
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
