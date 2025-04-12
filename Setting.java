import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Setting extends JPanel {
    private JFrame frame;
    private Image backgroundImg;

    public Setting(JFrame frame) {
        this.frame = frame;
        this.setPreferredSize(new Dimension(360, 640));

        backgroundImg = new ImageIcon(getClass().getResource("/picture/flappybirdbg.png")).getImage();
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
    }
}
