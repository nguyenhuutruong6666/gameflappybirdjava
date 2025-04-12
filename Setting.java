import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
// import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URL;

public class Setting extends JPanel {
    private JFrame frame;
    private Image backgroundImg;
    private Clip flyClip;
    private Clip endClip;
    private Boolean isMuted;

    private final String SETTING_FILE = "setting.txt";

    public Setting(JFrame frame) {
        this.frame = frame;
        this.setPreferredSize(new Dimension(360, 640));
        this.setLayout(null);

        backgroundImg = new ImageIcon(getClass().getResource("/picture/bgsound.png")).getImage();

        // Nút Back
        ImageIcon back = new ImageIcon(new ImageIcon(getClass().getResource("/picture/back.png"))
                .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        JButton backButton = new JButton(back);
        backButton.setBounds(2, 2, 50, 50);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setOpaque(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> backToMenu());
        add(backButton);

        // Nút bật/tắt âm thanh
        ImageIcon loa = new ImageIcon(new ImageIcon(getClass().getResource("/picture/loa.png"))
                .getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH));
        JButton muteButton = new JButton(loa);
        muteButton.setBounds(130, 340, 150, 100);
        muteButton.setContentAreaFilled(false);
        muteButton.setBorderPainted(false);
        muteButton.setOpaque(false);
        muteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(muteButton);

        // Đọc trạng thái từ file
        isMuted = loadMuteStateFromFile();
        muteButton.setText("" + (isMuted ? "ON" : "OFF"));

        muteButton.addActionListener(e -> {
            isMuted = !isMuted;
            updateMuteState();
            muteButton.setText("" + (isMuted ? "ON" : "OFF"));
            saveMuteStateToFile();
        });

        // Load âm thanh
        loadSound();
        updateMuteState();
    }

    private void updateMuteState() {
        if (flyClip != null && flyClip.isControlSupported(BooleanControl.Type.MUTE)) {
            BooleanControl muteControl = (BooleanControl) flyClip.getControl(BooleanControl.Type.MUTE);
            muteControl.setValue(isMuted);
        }
        if (endClip != null && endClip.isControlSupported(BooleanControl.Type.MUTE)) {
            BooleanControl muteControl = (BooleanControl) endClip.getControl(BooleanControl.Type.MUTE);
            muteControl.setValue(isMuted);
        }
    }

    private void loadSound() {
        try {
            URL flyURL = getClass().getResource("/picture/soundfly.wav");
            if (flyURL != null) {
                AudioInputStream flyStream = AudioSystem.getAudioInputStream(flyURL);
                flyClip = AudioSystem.getClip();
                flyClip.open(flyStream);
            } else {
                System.err.println("Không tìm thấy soundfly.wav");
            }

            URL endURL = getClass().getResource("/picture/soundend.wav");
            if (endURL != null) {
                AudioInputStream endStream = AudioSystem.getAudioInputStream(endURL);
                endClip = AudioSystem.getClip();
                endClip.open(endStream);
            } else {
                System.err.println("Không tìm thấy soundend.wav");
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void saveMuteStateToFile() {
        try (FileWriter writer = new FileWriter(SETTING_FILE)) {
            writer.write(isMuted.toString());
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu trạng thái âm thanh: " + e.getMessage());
        }
    }

    private boolean loadMuteStateFromFile() {
        File file = new File(SETTING_FILE);
        if (!file.exists()) {
            return false; // Mặc định là bật âm thanh
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            return Boolean.parseBoolean(line);
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc trạng thái âm thanh: " + e.getMessage());
            return false;
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
