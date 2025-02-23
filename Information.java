import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Information extends JPanel {
    private JFrame frame;
    private Image backgroundImg;
    private Image qrImage;

    public Information(JFrame frame) {
        this.frame = frame;
        this.setPreferredSize(new Dimension(360, 640));

        backgroundImg = new ImageIcon(getClass().getResource("/picture/flappybirdbg.png")).getImage();
        qrImage = new ImageIcon(getClass().getResource("/picture/maqr.png")).getImage();

        // Tạo nút Back
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBounds(10, 10, 80, 30);
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);

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

        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.BLACK);
        String text = "Donate in here";
        FontMetrics metrics = g.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth(text)) / 2;
        int y = 200;
        g.drawString(text, x, y);

        int qrSize = 250;
        int qrX = (getWidth() - qrSize) / 2;
        int qrY = y + 30;
        g.drawImage(qrImage, qrX, qrY, qrSize, qrSize, this);
    }
}
