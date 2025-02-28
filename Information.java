import javax.swing.*;
import java.awt.*;

public class Information extends JPanel {
    private JFrame frame;
    private Image backgroundImg;
    private Image qrImage;

    public Information(JFrame frame) {
        this.frame = frame;
        this.setPreferredSize(new Dimension(360, 640));

        backgroundImg = new ImageIcon(getClass().getResource("/picture/flappybirdbg.png")).getImage();
        qrImage = new ImageIcon(getClass().getResource("/picture/maqr.png")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
         
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.black);
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
