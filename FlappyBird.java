import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.sound.sampled.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    
    private boolean isSoundOn = true; // Biến để kiểm tra trạng thái âm thanh, mặc định là bật
    public void toggleSound() {
        isSoundOn = !isSoundOn; // Đảo ngược trạng thái âm thanh
    
        if (isSoundOn) {
            System.out.println("Âm thanh bật");
        } else {
            System.out.println("Âm thanh tắt");
        }
    }

    public void playButtonClickSound() {
        if (!isSoundOn) return; // Nếu âm thanh tắt, không làm gì
    
        try {
            // Đảm bảo rằng bạn có một tệp âm thanh "buttonClick.wav" trong thư mục "sound"
            File soundFile = new File(getClass().getResource("/sound/buttonClick.wav").toURI());
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
    
            // Đảm bảo âm thanh phát xong
            while (clip.isRunning()) {
                Thread.sleep(10); // Đợi cho đến khi âm thanh phát xong
            }
        } catch (Exception e) {
            e.printStackTrace(); // Nếu có lỗi khi phát âm thanh
        }
    }    

    // Phát âm thanh khi chim bay lên
    public void playJumpSound() {
        if (!isSoundOn) return; // Nếu âm thanh tắt, không làm gì

        try {
            File soundFile = new File(getClass().getResource("/sound/jump.wav").toURI());
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

            // Đảm bảo âm thanh phát xong
            while (clip.isRunning()) {
                Thread.sleep(10); // Đợi cho đến khi âm thanh phát xong
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phát âm thanh khi trò chơi kết thúc
    public void playGameOverSound() {
        if (!isSoundOn) return; // Nếu âm thanh tắt, không làm gì

        try {
            File soundFile = new File(getClass().getResource("/sound/gameOver.wav").toURI());
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            clip.drain(); // Đảm bảo âm thanh phát xong
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phát âm thanh khi cộng điểm
    public void playPointSound() {
        if (!isSoundOn) return; // Nếu âm thanh tắt, không làm gì

        try {
            File soundFile = new File(getClass().getResource("/sound/point.wav").toURI());
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            clip.drain(); // Đảm bảo âm thanh phát xong
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int boardWidth = 360;
    int boardHeight = 640;

    // images
    Image backgroundImg;
    Image backgroundDiem;
    Image bgScore;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    private JButton clickMenu;
    private JButton clickStore;
    private JButton backToMenuButton;

    // bird class
    int birdX = boardWidth / 8;
    int birdY = boardWidth / 2;
    // int birdWidth = 34;
    // int birdHeight = 24;
    int birdWidth = 55;
    int birdHeight = 40;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    // pipe class
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64; // scaled by 1/6
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    // game logic
    Bird bird;
    int velocityX = -4; // move pipes to the left speed (simulates bird moving right)
    int velocityY = 0; // move bird up/down speed.
    int gravity = 1;

    // Biến điều khiển dao động của ống
    int pipeOffsetY = 0; // Độ lệch vị trí của ống theo trục Y
    int pipeDirection = -1; // Hướng di chuyển (1 = xuống, -1 = lên)
    int pipeMoveSpeed = 3; // Tốc độ di chuyển lên/xuống của ống
    int pipeStartMovingScore = 20; // Điểm bắt đầu dao động
    int pipeStopMovingScore = 32; // Điểm dừng dao động sau khi vượt thêm 15 điểm
    boolean pipeMoving = false; // Trạng thái ống đang dao động

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver = false;
    double score = 0;

    boolean gameStarted = false;
    boolean firstTime = true;

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        // load images
        backgroundImg = new ImageIcon(getClass().getResource("./picture/flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./picture/test.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./picture/toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./picture/bottompipe.png")).getImage();
        backgroundDiem = new ImageIcon(getClass().getResource("./picture/flappybirdbg1.png")).getImage();
        bgScore = new ImageIcon(getClass().getResource("./picture/bgScore.png")).getImage();

        // bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        // place pipes timer
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to be executed
                // test
                placePipes();
            }
        });
        placePipeTimer.start();

        // game timer
        gameLoop = new Timer(1000 / 60, this); // how long it takes to start timer, milliseconds gone between frames
        gameLoop.start();

        // Mặc định nếu không có file hoặc lỗi thì dùng skin mặc định
        String selectedSkin = "test.png";
        File skinFile = new File("SelectBird.txt");

        if (skinFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(skinFile))) {
                String skinName = reader.readLine();
                if (skinName != null && !skinName.isEmpty()) {
                    selectedSkin = skinName;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // Load skin đã chọn
        birdImg = new ImageIcon(getClass().getResource("/picture/" + selectedSkin)).getImage();
    }

    void placePipes() {
        // (0-1) * pipeHeight/2.
        // 0 -> -128 (pipeHeight/4)
        // 1 -> -128 - 256 (pipeHeight/4 - pipeHeight/2) = -3/4 pipeHeight
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 4;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // background
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);

        // bird
        g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);

        // pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
        // Space to play
        if (!gameStarted || firstTime) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            String message = "Press Space to start";
            FontMetrics metrics = g.getFontMetrics();
            int x = (boardWidth - metrics.stringWidth(message)) / 2;
            int y = boardHeight / 2;
            g.drawString(message, x, y);

            if (backToMenuButton == null) {
                ImageIcon back = new ImageIcon(new ImageIcon(getClass().getResource("/picture/back.png"))
                        .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                backToMenuButton = new JButton(back);
                backToMenuButton.setFont(new Font("Arial", Font.BOLD, 15));
                backToMenuButton.setFocusPainted(false);
                backToMenuButton.setBounds(2, 2, 50, 50);
                backToMenuButton.setContentAreaFilled(false);
                backToMenuButton.setBorderPainted(false);
                backToMenuButton.setOpaque(false);
                backToMenuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                backToMenuButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        returnToMenu();
                    }
                });
                this.setLayout(null);
                this.add(backToMenuButton);
            }
        }

        // score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 28));

        if (gameOver) {
            String gameOverText = " " + (int) score + " ";
            FontMetrics metrics = g.getFontMetrics();
            int x = (boardWidth - metrics.stringWidth(gameOverText)) / 2;
            int y = (boardHeight / 2) + 22;
            g.drawImage(bgScore, 0, 0, this.boardWidth, this.boardHeight, null);
            g.drawString(gameOverText, x, y);

            // Vẽ điểm cao nhất
            // Đọc highScore từ file và hiển thị
            int highScore = readHighScore();
            String highScoreText = " " + highScore + " ";
            int x2 = (boardWidth - metrics.stringWidth(highScoreText)) / 2;
            g.drawString(highScoreText, x2, 191);

            // Reset game khi thua
            bird.y = birdY;
            velocityY = 0;
            pipes.clear();
            gameOver = false;
            score = 0;

            // Reset trạng thái dao động của ống
            pipeStartMovingScore = 20;
            pipeMoving = false;

            if (clickMenu == null && clickStore == null) {
                // Thêm nút "Menu"
                clickMenu = new JButton("Menu");
                clickMenu.setFont(new Font("Arial", Font.BOLD, 15));
                clickMenu.setFocusPainted(false);
                clickMenu.setBounds(70, 430, 80, 40);
                clickMenu.setBackground(Color.ORANGE);
                clickMenu.setForeground(Color.WHITE);
                clickMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        returnToMenu();
                    }
                });

                // Thêm nút "Store"
                clickStore = new JButton("Store");
                clickStore.setFont(new Font("Arial", Font.BOLD, 15));
                clickStore.setFocusPainted(false);
                clickStore.setBounds(210, 430, 80, 40);
                clickStore.setBackground(Color.ORANGE);
                clickStore.setForeground(Color.WHITE);
                clickStore.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ToStore();
                    }
                });

                // Thêm nút vào JPanel
                this.setLayout(null);
                this.add(clickMenu);
                this.add(clickStore);
            }
        } else {
            // Ẩn hai nút khi chơi tiếp
            if (clickMenu != null && clickStore != null) {
                this.remove(clickMenu);
                this.remove(clickStore);
                clickMenu = null;
                clickStore = null;
                this.revalidate();
                this.repaint();
            }
            // score
            g.drawImage(backgroundDiem, 270, 0, this.boardWidth, this.boardHeight, null);
            g.drawString(String.valueOf((int) score), 282, 34);
        }
    }

    public void move() {
        if (!gameStarted)
            return; // Nếu chưa bắt đầu thì không di chuyển

        // bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0); // apply gravity to current bird.y, limit the bird.y to top of the canvas

        // pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            // Kích hoạt dao động khi đạt điểm `pipeStartMovingScore`
            if (score >= pipeStartMovingScore) {
                pipeMoving = true; // Bắt đầu dao động

                // Khi đã đạt thêm 15 điểm kể từ lúc ống bắt đầu dao động, dừng dao động
                if (score >= pipeStartMovingScore + 15) {
                    pipeMoving = false;
                    pipeStartMovingScore += 22; // Cập nhật mốc điểm tiếp theo để dao động
                }
            }

            // Nếu ống đang dao động thì điều chỉnh vị trí theo trục Y
            if (pipeMoving) {
                pipe.y += pipeDirection * pipeMoveSpeed;

                // Giới hạn dao động của ống trong khoảng ±20px
                if (pipeOffsetY >= 35 || pipeOffsetY <= -40) {
                    pipeDirection *= -1; // Đảo chiều
                }
                pipeOffsetY += pipeDirection * pipeMoveSpeed;
            }

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5; // 0.5 vì có 2 ống
                pipe.passed = true;
                playPointSound(); // Phát âm thanh khi cộng điểm
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }

    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameStarted)
            return; // Không làm gì nếu trò chơi chưa bắt đầu
        move();
        repaint();
        if (gameOver) {
            playGameOverSound(); // Phát âm thanh khi trò chơi kết thúc
            placePipeTimer.stop();
            gameLoop.stop();
            saveScore();
            gameStarted = false; // Đặt lại để người chơi nhấn Space mới chơi lại
        }
    }

    private void saveScore() {
        File file = new File("diem.txt");
        File highScoreFile = new File("HighScore.txt");
        int totalScore = 0;
        int highScore = 0;

        // Đọc điểm cũ
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                if (line != null) {
                    totalScore = Integer.parseInt(line.trim());
                }
            } catch (IOException | NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        // Đọc điểm cao nhất từ file HighScore.txt
        if (highScoreFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(highScoreFile))) {
                String line = reader.readLine();
                if (line != null) {
                    highScore = Integer.parseInt(line.trim());
                }
            } catch (IOException | NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        // Cộng điểm mới vào tổng điểm
        totalScore += (int) score;

        // Ghi điểm mới vào file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(String.valueOf(totalScore));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Kiểm tra và cập nhật điểm cao nhất
        if ((int) score > highScore) {
            highScore = (int) score;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(highScoreFile))) {
                writer.write(String.valueOf(highScore));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameStarted) {
                gameStarted = true;
                gameLoop.start();
                placePipeTimer.start();
                firstTime = false; // Đặt cờ để ẩn thông báo
                repaint(); // Cập nhật màn hình ngay lập tức để xóa thông báo
            }
            velocityY = -9;
            playJumpSound(); // Phát âm thanh khi chim bay lên

            if (gameOver) {
                // Reset game khi thua
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                // gameStarted = false; // Đợi nhấn Space mới chơi lại

                // Ẩn hai nút khi chơi tiếp
                if (clickMenu != null && clickStore != null) {
                    this.remove(clickMenu);
                    this.remove(clickStore);
                    clickMenu = null;
                    clickStore = null;
                    this.revalidate();
                    this.repaint();
                }
            }

            if (backToMenuButton != null) {
                this.remove(backToMenuButton);
                backToMenuButton = null;
                this.revalidate();
                this.repaint();
            }
        }
    }

    private void returnToMenu() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        MenuScreen menuScreen = new MenuScreen(frame);
        frame.add(menuScreen);
        frame.pack();
        frame.revalidate();
        frame.repaint();
    }

    private void ToStore() {
        JFrame frame1 = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame1.getContentPane().removeAll();
        StoreBird storeBird = new StoreBird(frame1);
        frame1.add(storeBird);
        frame1.pack();
        frame1.revalidate();
        frame1.repaint();
    }

    private int readHighScore() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("HighScore.txt"));
            String line = reader.readLine();
            reader.close();
            return line != null ? Integer.parseInt(line.trim()) : 0;
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    // not needed
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
