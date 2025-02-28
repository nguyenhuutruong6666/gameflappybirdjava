import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StoreBird extends JPanel {
    private JFrame frame;
    private int totalScore;
    private Image backgroundImg;
    private Set<String> ownedSkins;

    public StoreBird(JFrame frame) {
        this.frame = frame;
        this.setPreferredSize(new Dimension(360, 640));
        this.setLayout(null);

        // Tải ảnh nền
        backgroundImg = new ImageIcon(getClass().getResource("/picture/flappybirdbg.png")).getImage();

        // Đọc tổng điểm từ file diem.txt
        totalScore = readScoreFromFile("diem.txt");
        // luu skin da mua
        ownedSkins = readOwnedSkins("ownedSkins.txt");

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
        
        // Sửa phần vòng lặp for để thêm sự kiện khi chọn skin
        for (int i = 0; i < 6; i++) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BorderLayout());
            itemPanel.setOpaque(false);

            JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/picture/" + imageNames[i]))
                                    .getImage().getScaledInstance(80, 55, Image.SCALE_SMOOTH)));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel priceLabel = new JLabel(prices[i] + " point", SwingConstants.CENTER);
            priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
            priceLabel.setForeground(Color.WHITE);

            // JButton selectButton = new JButton(ownedSkins.contains(imageNames[i]) ? "Use" : "Select");
            JButton selectButton = new JButton(ownedSkins.contains(imageNames[i]) ? "Select" : "Buy");
            selectButton.setFont(new Font("Arial", Font.BOLD, 12));
            selectButton.setBackground(Color.ORANGE);
            selectButton.setForeground(Color.BLACK);
            
            final int index = i;
            selectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (ownedSkins.contains(imageNames[index])) {
                        saveSelectedSkin(imageNames[index]);
                        JOptionPane.showMessageDialog(frame, "Bạn đã chọn skin " + imageNames[index]);
                    } else {
                        buySkin(imageNames[index], prices[index], scoreLabel, selectButton);
                    }
                }
            });

            itemPanel.add(imageLabel, BorderLayout.CENTER);
            itemPanel.add(priceLabel, BorderLayout.NORTH);
            itemPanel.add(selectButton, BorderLayout.SOUTH);
            gridPanel.add(itemPanel);
        }

        // Nút dùng lại skin mặc định
        ImageIcon defaultIcon = new ImageIcon(new ImageIcon(getClass().getResource("/picture/test.png"))
                                    .getImage().getScaledInstance(80, 55, Image.SCALE_SMOOTH));

        JButton defaultButton = new JButton("Use Skin Default", defaultIcon);
        defaultButton.setFont(new Font("Arial", Font.BOLD, 12));
        defaultButton.setHorizontalTextPosition(SwingConstants.CENTER);
        defaultButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        defaultButton.setBackground(Color.GRAY);
        defaultButton.setForeground(Color.WHITE);
        defaultButton.setBounds(110, 350, 140, 120);
        defaultButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
                saveSelectedSkin("test.png");
                JOptionPane.showMessageDialog(frame, "Reset to default skin!");
            }
        });
        add(defaultButton);

        
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

    private void buySkin(String skinName, int price, JLabel scoreLabel, JButton button) {
        if (totalScore >= price) {
            totalScore -= price; // Trừ điểm
            saveScoreToFile("diem.txt", totalScore);    // Ghi lại điểm mới
            saveOwnedSkin(skinName);    // Lưu skin da mua
            saveSelectedSkin(skinName); // Lưu skin được chọn
            ownedSkins.add(skinName);
            scoreLabel.setText("Total score: " + totalScore);   // Cập nhật điểm trên giao diện
            // button.setText("Use");
            button.setText("Select");
            JOptionPane.showMessageDialog(this, "Bạn đã đổi thành công skin " + skinName);
        } else {
            JOptionPane.showMessageDialog(this, "Không đủ điểm để mua skin này!");
        }
    }

    private void saveScoreToFile(String filename, int newScore) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
        writer.write(String.valueOf(newScore));
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}

private void saveSelectedSkin(String skinName) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("SelectBird.txt"))) {
        writer.write(skinName);
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}

private void saveOwnedSkin(String skinName) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("ownedSkins.txt", true))) {
        writer.write(skinName);
        writer.newLine();
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}  

    private Set<String> readOwnedSkins(String filename) {
        Set<String> skins = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                skins.add(line.trim());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return skins;
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