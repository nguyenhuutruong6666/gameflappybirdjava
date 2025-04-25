import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*; 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MenuScreen extends JPanel {
    //JPanel là một thành phần giao diện (GUI component) dùng để chứa các nút, ảnh, hoặc các thành phần khác.
    private JFrame frame;
    private Image bgStart;
    private boolean isSoundOn = true; // Biến theo dõi trạng thái âm thanh

    public void playButtonClickSound() {
        if (!isSoundOn) return; // Nếu âm thanh tắt, không làm gì

        try {
            // Đảm bảo rằng bạn có tệp âm thanh "buttonClick.wav" trong thư mục "sound"
            File soundFile = new File(getClass().getResource("/sound/buttonClick.wav").toURI());
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream); //mở luồng âm thanh.
            clip.start(); //bắt đầu phát.

            // Đảm bảo âm thanh phát xong
            while (clip.isRunning()) {
                Thread.sleep(10); // Đợi cho đến khi âm thanh phát xong
            }
        } catch (Exception e) {
            e.printStackTrace(); // Nếu có lỗi khi phát âm thanh
        }
    }


    public MenuScreen(JFrame frame) {
        this.frame = frame;
        this.setPreferredSize(new Dimension(360, 640));

        // Tải ảnh nền
        bgStart = new ImageIcon(getClass().getResource("/picture/bgStart.png")).getImage();

        // Tạo nút "Chơi Game"
        ImageIcon play = new ImageIcon(new ImageIcon(getClass().getResource("/picture/buttonPlay.png"))
                                    .getImage().getScaledInstance(180, 60, Image.SCALE_SMOOTH));
        JButton playButton = new JButton(play);
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        playButton.setFocusPainted(false); //Tắt hiệu ứng viền nét đứt (focus border) khi nút được chọn hoặc tab đến.
        playButton.setBounds(90, 200, 180, 60);
        playButton.setContentAreaFilled(false); //Không vẽ nền (background) mặc định của nút.
        playButton.setBorderPainted(false); //Tắt vẽ viền (border) quanh nút.
        playButton.setOpaque(false); //Cho phép phần nền phía sau (background) hiển thị xuyên qua nút.
        playButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); //Khi người dùng đưa chuột lên nút, con trỏ chuyển thành hình bàn tay 🤚.

        // Tạo nút "Store Bird"
        ImageIcon store = new ImageIcon(new ImageIcon(getClass().getResource("/picture/buttonStore.png"))
                                    .getImage().getScaledInstance(180, 60, Image.SCALE_SMOOTH));
        JButton btstore = new JButton(store);
        btstore.setFont(new Font("Arial", Font.BOLD, 20));
        btstore.setFocusPainted(false);
        btstore.setBounds(90, 275, 180, 60);
        btstore.setContentAreaFilled(false);
        btstore.setBorderPainted(false);
        btstore.setOpaque(false);
        btstore.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Tạo nút "Cài đặt"
        ImageIcon setting = new ImageIcon(new ImageIcon(getClass().getResource("/picture/buttonSetting.png"))
                                    .getImage().getScaledInstance(180, 60, Image.SCALE_SMOOTH));
        JButton btsetting = new JButton(setting);
        btsetting.setFont(new Font("Arial", Font.BOLD, 20));
        btsetting.setFocusPainted(false);
        btsetting.setBounds(90, 350, 180, 60);
        btsetting.setContentAreaFilled(false);
        btsetting.setBorderPainted(false);
        btsetting.setOpaque(false);
        btsetting.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Tạo nút "Thông Tin"
        ImageIcon about = new ImageIcon(new ImageIcon(getClass().getResource("/picture/buttonAbout.png"))
                                    .getImage().getScaledInstance(180, 60, Image.SCALE_SMOOTH));
        JButton btabout = new JButton(about);
        btabout.setFont(new Font("Arial", Font.BOLD, 20));
        btabout.setFocusPainted(false);
        btabout.setBounds(90, 425, 180, 60);
        btabout.setContentAreaFilled(false);
        btabout.setBorderPainted(false);
        btabout.setOpaque(false);
        btabout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Thêm ActionListener vào các nút và gọi playButtonClickSound()
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButtonClickSound(); // Phát âm thanh khi nhấn nút
                startGame(); // Chuyển sang màn hình chơi game
            }
        });

        btstore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButtonClickSound(); // Phát âm thanh khi nhấn nút
                openStore(); // Chuyển sang màn hình cửa hàng
            }
        });

        btsetting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButtonClickSound(); // Phát âm thanh khi nhấn nút
                openSetting(); // Chuyển sang màn hình cài đặt
            }
        });

        btabout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButtonClickSound(); // Phát âm thanh khi nhấn nút
                openInformation(); // Chuyển sang màn hình thông tin
            }
        });

        setLayout(null); //tự kiểm soát vị trí và kích thước từng thành phần
        add(playButton);
        add(btstore);
        add(btsetting);
        add(btabout);
    }

    private void startGame() {
        frame.getContentPane().removeAll();
        FlappyBird game = new FlappyBird();
        frame.add(game);
        frame.pack();
        game.requestFocus(); //Yêu cầu FlappyBird nhận focus bàn phím, để người dùng có thể điều khiển game bằng phím
        frame.revalidate(); //Yêu cầu Swing cập nhật lại layout của JFrame sau khi có thay đổi (thêm hoặc xóa thành phần).
        frame.repaint(); //Yêu cầu vẽ lại toàn bộ giao diện (repaint() tất cả thành phần trong frame).
    }

    private void openStore() {
        frame.getContentPane().removeAll();
        StoreBird storeBird = new StoreBird(frame);
        frame.add(storeBird);
        frame.pack();
        frame.revalidate();
        frame.repaint();
    }

    private void openSetting() {
        frame.getContentPane().removeAll();
        Setting setting = new Setting(frame);
        frame.add(setting);
        frame.pack();
        frame.revalidate();
        frame.repaint();
    }

    private void openInformation() {
        frame.getContentPane().removeAll();
        Information information = new Information(frame);
        frame.add(information);
        frame.pack();
        frame.revalidate();
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgStart, 0, 0, getWidth(), getHeight(), this);
    }
}
