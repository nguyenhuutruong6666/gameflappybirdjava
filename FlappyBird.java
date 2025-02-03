import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    //images
    Image backgroundImg;
    Image backgroundDiem;
    Image bgScore;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    private JButton clickMenu;
    private JButton clickStore;

    //bird class
    int birdX = boardWidth/8;
    int birdY = boardWidth/2;
//    int birdWidth = 34;
//    int birdHeight = 24;
    int birdWidth = 34;
    int birdHeight = 24;

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

    //pipe class
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;  //scaled by 1/6
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

    //game logic
    Bird bird;
    int velocityX = -4; //move pipes to the left speed (simulates bird moving right)
    int velocityY = 0; //move bird up/down speed.
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver = false;
    double score = 0;

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        //load images
        backgroundImg = new ImageIcon(getClass().getResource("./picture/flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./picture/flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./picture/toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./picture/bottompipe.png")).getImage();
        backgroundDiem = new ImageIcon(getClass().getResource("./picture/flappybirdbg1.png")).getImage();
        bgScore = new ImageIcon(getClass().getResource("./picture/bgScore.png")).getImage();


        //bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        //place pipes timer
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              // Code to be executed
              // test
              placePipes();
            }
        });
        placePipeTimer.start();
        
		//game timer
		gameLoop = new Timer(1000/60, this); //how long it takes to start timer, milliseconds gone between frames 
        gameLoop.start();
	}
    
    void placePipes() {
        //(0-1) * pipeHeight/2.
        // 0 -> -128 (pipeHeight/4)
        // 1 -> -128 - 256 (pipeHeight/4 - pipeHeight/2) = -3/4 pipeHeight
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4;
    
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);
    
        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y  + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }
    
    
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
        //background
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);
    
        //bird
        g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);
    
        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
        
        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
    
        if (gameOver) {
            // String gameOverText = "Game Over: " + (int) score;
            String gameOverText = " " + (int) score + " ";
            FontMetrics metrics = g.getFontMetrics();
            int x = (boardWidth - metrics.stringWidth(gameOverText)) / 2;
            int y = (boardHeight / 2) + 22;
            g.drawImage(bgScore, 0, 0, this.boardWidth, this.boardHeight, null);
            g.drawString(gameOverText, x, y);

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
            //score
            g.drawImage(backgroundDiem, 0, 0, this.boardWidth, this.boardHeight, null);
            g.drawString(String.valueOf((int) score), 12, 34);
        }
    }

    public void move() {
        //bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0); //apply gravity to current bird.y, limit the bird.y to top of the canvas

        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5; //0.5 because there are 2 pipes! so 0.5*2 = 1, 1 for each set of pipes
                pipe.passed = true;
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
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
               a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) { //called every x milliseconds by gameLoop timer
        move();
        repaint();
        if (gameOver) {
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }  

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // System.out.println("JUMP!");
            velocityY = -9;

            if (gameOver) {
                //restart game by resetting conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placePipeTimer.start();

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

    //not needed
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
