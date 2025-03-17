import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Information extends JPanel {
    private JFrame frame;
    private Image backgroundImg;
    private Image Image, QRImage;

    public Information(JFrame frame) {
        this.frame = frame;
        this.setPreferredSize(new Dimension(360, 640));

        backgroundImg = new ImageIcon(getClass().getResource("/picture/flappybirdbg.png")).getImage();
        Image = new ImageIcon(getClass().getResource("/picture/actor.png")).getImage();
        QRImage = new ImageIcon(getClass().getResource("/picture/maqr.png")).getImage();

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
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.GRAY);
        String text = "Version 1.0";
        String dn = "Donate in here";
        FontMetrics metrics = g.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth(text)) / 2;
        int y = 622;

        int aX = (getWidth() - 350) / 2;
        g.drawImage(Image, aX, 50, 350, 600, this);
        g.drawString(dn, 115, 340);
        g.drawImage(QRImage, 80, 350, 200, 200, this);
        g.drawString(text, x, y);
    }
}
