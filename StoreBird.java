import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class StoreBird extends JPanel {
    private JFrame frame;
    private int totalScore;
    private Image backgroundImg;
    private Set<String> ownedSkins;
    private String selectedSkin;

    public StoreBird(JFrame frame) {
        this.frame = frame;
        this.setPreferredSize(new Dimension(360, 640));
        this.setLayout(null);

        backgroundImg = new ImageIcon(getClass().getResource("/picture/flappybirdbg.png")).getImage();
        totalScore = readScoreFromFile("diem.txt");
        ownedSkins = readOwnedSkins("ownedSkins.txt");
        selectedSkin = readSelectedSkin("SelectBird.txt");

        JLabel scoreLabel = new JLabel("Total score: " + totalScore, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setBounds(0, 30, 360, 30);
        scoreLabel.setForeground(Color.black);
        add(scoreLabel);

        String[] imageNames = {"anh1.png", "anh2.png", "anh3.png", "anh4.png", "anh5.png", "anh6.png"};
        int[] prices = {100, 200, 300, 400, 500, 600};

        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        gridPanel.setBounds(20, 80, 320, 250);
        gridPanel.setOpaque(false);
        
        for (int i = 0; i < 6; i++) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BorderLayout());
            itemPanel.setOpaque(true);

            JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/picture/" + imageNames[i]))
                                    .getImage().getScaledInstance(80, 55, Image.SCALE_SMOOTH)));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel priceLabel = new JLabel(prices[i] + " point", SwingConstants.CENTER);
            priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
            priceLabel.setForeground(Color.BLACK);

            JButton selectButton = new JButton(ownedSkins.contains(imageNames[i]) ? "Select" : "Buy");
            selectButton.setFont(new Font("Arial", Font.BOLD, 12));
            selectButton.setBackground(Color.ORANGE);
            selectButton.setForeground(Color.BLACK);
            
            if (imageNames[i].equals(selectedSkin)) {
                selectButton.setBackground(Color.GREEN);
                itemPanel.setBackground(new Color(173, 255, 47));
            }

            final int index = i;
            selectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (ownedSkins.contains(imageNames[index])) {
                        saveSelectedSkin(imageNames[index]);
                        JOptionPane.showMessageDialog(frame, "You have chosen a skin " + imageNames[index]);
                        refreshUI();
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

        JButton defaultButton = new JButton("Use Skin Default");
        defaultButton.setFont(new Font("Arial", Font.BOLD, 12));
        defaultButton.setBackground(selectedSkin.equals("test.png") ? Color.GREEN : Color.GRAY);
        defaultButton.setForeground(Color.WHITE);
        defaultButton.setBounds(110, 350, 140, 40);
        defaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSelectedSkin("test.png");
                JOptionPane.showMessageDialog(frame, "Reset to default skin!");
                refreshUI();
            }
        });
        add(defaultButton);

        add(gridPanel);
        
        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 15));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(110, 500, 140, 40);
        backButton.addActionListener(e -> returnToMenu());
        add(backButton);
    }

    private void refreshUI() {
        frame.getContentPane().removeAll();
        frame.add(new StoreBird(frame));
        frame.pack();
        frame.revalidate();
        frame.repaint();
    }

    private void buySkin(String skinName, int price, JLabel scoreLabel, JButton button) {
        if (totalScore >= price) {
            totalScore -= price;
            saveScoreToFile("diem.txt", totalScore);  // Lưu điểm mới vào file
            saveOwnedSkin(skinName);
            saveSelectedSkin(skinName);
            ownedSkins.add(skinName);
            scoreLabel.setText("Total score: " + totalScore);
            refreshUI();
        } else {
            JOptionPane.showMessageDialog(this, "Not enough points to purchase this skin!");
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
    
    private void saveScoreToFile(String filename, int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(String.valueOf(score));
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

    private String readSelectedSkin(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            return reader.readLine().trim();
        } catch (IOException ex) {
            return "test.png";
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
        frame.add(new MenuScreen(frame));
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
