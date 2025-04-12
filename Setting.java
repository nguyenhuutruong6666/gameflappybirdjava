import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Setting extends JPanel {
    private JFrame frame;
    private Image backgroundImg;
    private JButton soundButton; // Nút âm thanh
    private boolean isSoundOn = true; // Biến theo dõi trạng thái âm thanh

    public Setting(JFrame frame) {
        this.frame = frame;
        this.setPreferredSize(new Dimension(360, 640));

        backgroundImg = new ImageIcon(getClass().getResource("/picture/flappybirdbg.png")).getImage();

        // Tạo nút quay lại
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

        // Tạo nút âm thanh
        updateSoundButton(); // Cập nhật biểu tượng của nút âm thanh

        setLayout(null);
        add(backButton);
        add(soundButton); // Thêm nút âm thanh vào giao diện
    }

    // Phương thức để cập nhật biểu tượng của nút âm thanh
    private void updateSoundButton() {
        ImageIcon soundIcon;

        if (isSoundOn) {
            soundIcon = new ImageIcon(new ImageIcon(getClass().getResource("/picture/sound_on.png"))
                    .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        } else {
            soundIcon = new ImageIcon(new ImageIcon(getClass().getResource("/picture/sound_off.png"))
                    .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        }

        soundButton = new JButton(soundIcon);
        soundButton.setBounds(150, 300, 40, 40); // Đặt vị trí nút
        soundButton.setContentAreaFilled(false);
        soundButton.setBorderPainted(false);
        soundButton.setOpaque(false);
        soundButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Lắng nghe sự kiện nhấn nút
        soundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleSound(); // Đảo ngược trạng thái âm thanh
                updateSoundButton(); // Cập nhật lại biểu tượng của nút âm thanh sau khi nhấn
            }
        });
    }

    // Phương thức để bật/tắt âm thanh
    private void toggleSound() {
        isSoundOn = !isSoundOn; // Đảo ngược trạng thái âm thanh
        if (isSoundOn) {
            System.out.println("Âm thanh bật");
        } else {
            System.out.println("Âm thanh tắt");
        }
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
