import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StoreBird extends JPanel {
    private int totalScore;
    
    public StoreBird(JFrame frame) {
        setLayout(new BorderLayout());
        totalScore = getTotalScore();

        JLabel scoreLabel = new JLabel("Tổng điểm: " + totalScore, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(scoreLabel, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Quay lại Menu");
        backButton.addActionListener(e -> {
            frame.setContentPane(new MenuScreen(frame));
            frame.revalidate();
        });
        add(backButton, BorderLayout.SOUTH);
    }

    private int getTotalScore() {
        int total = 0;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:flappybird.db");
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS Scores (id INTEGER PRIMARY KEY AUTOINCREMENT, score INTEGER)");
            try (ResultSet rs = stmt.executeQuery("SELECT SUM(score) AS total FROM Scores")) {
                if (rs.next()) {
                    total = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}
