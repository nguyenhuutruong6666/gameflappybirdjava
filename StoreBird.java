import javax.swing.*;
import java.awt.*;
import java.io.*;

public class StoreBird extends JPanel {
    private int totalScore;

    public StoreBird(JFrame frame) {
        this.setPreferredSize(new Dimension(360, 640));
        loadScore();
        
        JButton backButton = new JButton("Quay lại");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setFocusPainted(false);
        backButton.setBounds(110, 500, 140, 50);
        backButton.setBackground(Color.ORANGE);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(new MenuScreen(frame));
            frame.pack();
            frame.revalidate();
            frame.repaint();
        });

        setLayout(null);
        add(backButton);
    }

    private void loadScore() {
        File file = new File("diem.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                if (line != null) {
                    totalScore = Integer.parseInt(line.trim());
                }
            } catch (IOException | NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.setColor(Color.BLACK);
        g.drawString("Điểm tích lũy: " + totalScore, 80, 300);
    }
}
