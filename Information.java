import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Information extends JPanel {
    private JFrame frame;
    private Image backgroundImg;

    public Information(JFrame frame) {
        this.frame = frame;
        this.setPreferredSize(new Dimension(360, 640));

        backgroundImg = new ImageIcon(getClass().getResource("/picture/bginfor.png")).getImage();
        // Tạo nút Back
        ImageIcon back = new ImageIcon(new ImageIcon(getClass().getResource("/picture/back.png"))
                                    .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        JButton backButton = new JButton(back);
        backButton.setBounds(2, 2, 50, 50);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setOpaque(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToMenu();
            }
        });

        setLayout(null);
        add(backButton);
    }

    private void backToMenu() {
        frame.getContentPane().removeAll();
        MenuScreen menu = new MenuScreen(frame);
        frame.add(menu);
        frame.pack();
        frame.revalidate();
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        g.setFont(new Font("Arial", Font.ROMAN_BASELINE, 10));
        g.setColor(Color.BLACK);
        String textvs = "Version 2.0";
        String text = "Flappy Bird là một trò chơi điện tử nổi tiếng với lối chơi đơn giản nhưng đầy thách thức, " +
                  "yêu cầu người chơi điều khiển chú chim bay qua các chướng ngại vật bằng cách chạm vào màn hình để giữ thăng bằng. " +
                  "Trò chơi từng tạo nên cơn sốt trên toàn cầu nhờ vào độ khó gây nghiện và thiết kế đồ họa pixel đơn giản nhưng cuốn hút.\n\n" +
                  "Phiên bản này đã được phát triển thêm bởi Công Thực, Mạnh Trí, Hữu Trường, mang đến nhiều cải tiến và tính năng mới. " +
                  "Người chơi có thể tùy chỉnh skin cho nhân vật, sử dụng điểm thưởng để mua các vật phẩm đặc biệt và trải nghiệm những thử thách mới đầy hấp dẫn. " +
                  "Với những nâng cấp này, trò chơi hứa hẹn sẽ mang lại nhiều giờ giải trí thú vị và gay cấn hơn bao giờ hết!";

        int textX = 42; // Lề trái
        int textY = 319; // Vị trí bắt đầu
        int maxWidth = getWidth() - 75; // Giới hạn chiều rộng văn bản
        drawWrappedText(g, text, textX, textY, maxWidth);
        g.drawString(textvs, 153, 505);
    }

    public void drawWrappedText(Graphics g, String text, int x, int y, int maxWidth) {
        FontMetrics fm = g.getFontMetrics();
        int lineHeight = fm.getHeight();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        
        for (String word : words) {
            if (fm.stringWidth(line + word) < maxWidth) {
                line.append(word).append(" ");
            } else {
                g.drawString(line.toString(), x, y);
                y += lineHeight;
                line = new StringBuilder(word + " ");
            }
        }
        g.drawString(line.toString(), x, y); // Vẽ dòng cuối
    }
    
}
