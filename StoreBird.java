import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StoreBird extends JPanel {
    private JFrame frame;
    private int totalScore;
    private Image backgroundImg;

    public StoreBird(JFrame frame) {
        this.frame = frame;
        this.setPreferredSize(new Dimension(360, 640));
        this.setLayout(null);

        // Tải ảnh nền
        backgroundImg = new ImageIcon(getClass().getResource("/picture/flappybirdbg.png")).getImage();

        // Đọc tổng điểm từ file diem.txt
        totalScore = readScoreFromFile("diem.txt");

        // Hiển thị tổng điểm
        JLabel scoreLabel = new JLabel("Total score: " + totalScore, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setBounds(0, 30, 360, 30);
        scoreLabel.setForeground(Color.black);
        add(scoreLabel);

        // Hiển thị 6 hình ảnh với điểm số tương ứng
        String[] imageNames = {"anh1.png", "anh2.png", "anh3.png", "anh4.png", "anh5.png", "anh6.png"};
        int[] prices = {100, 200, 300, 400, 500, 600};

        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        gridPanel.setBounds(20, 80, 320, 250);
        gridPanel.setOpaque(false);
        
        for (int i = 0; i < 6; i++) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BorderLayout());
            itemPanel.setOpaque(false);
            
            JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/picture/" + imageNames[i]))
                                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            JLabel priceLabel = new JLabel(prices[i] + " point", SwingConstants.CENTER);
            priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
            priceLabel.setForeground(Color.WHITE);

            JButton selectButton = new JButton("Select");
            selectButton.setFont(new Font("Arial", Font.BOLD, 12));
            selectButton.setBackground(Color.ORANGE);
            selectButton.setForeground(Color.WHITE);
            
            itemPanel.add(imageLabel, BorderLayout.CENTER);
            itemPanel.add(priceLabel, BorderLayout.NORTH);
            itemPanel.add(selectButton, BorderLayout.SOUTH);

            gridPanel.add(itemPanel);
        }
        
        add(gridPanel);

        // Nút quay về Menu
        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 15));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(110, 500, 140, 40);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToMenu();
            }
        });
        add(backButton);
    }

    private int readScoreFromFile(String filename) {
        int score = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                score += Integer.parseInt(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return score;
    }

    private void returnToMenu() {
        frame.getContentPane().removeAll();
        MenuScreen menuScreen = new MenuScreen(frame);
        frame.add(menuScreen);
        frame.pack();
        frame.revalidate();
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
    }
}