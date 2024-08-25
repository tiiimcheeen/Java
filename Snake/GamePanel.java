import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L; // To avoid warnings

    // Constants
    private static final int SCREEN_WIDTH = 750;
    private static final int SCREEN_HEIGHT = 750;
    private static final int UNIT_SIZE = 25;
    private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 75;
    
    // Record score file
    private static final String RECORD_SCORE_FILE = "RecordScore.txt";
    private int recordScore = 0; // Initialize record score

    // Game State Variables	
    private final int x1[] = new int[GAME_UNITS]; // Snake 1 (single-player or player 1)
    private final int y1[] = new int[GAME_UNITS];
    private final int x2[] = new int[GAME_UNITS]; // Snake 2 (player 2 in two-player mode)
    private final int y2[] = new int[GAME_UNITS];
    private int bodyParts1 = 6;
    private int bodyParts2 = 6;
    private int applesEaten;
    private int appleX;
    private int appleY;
    private char direction1 = 'R'; // Snake 1 direction
    private char direction2 = 'L'; // Snake 2 direction
    private boolean running = false;
    private Timer timer;
    private Random random;
    private boolean twoPlayerMode = false; // Flag to determine the game mode
    private boolean snake1Lost = false; // Flag to check if Snake 1 lost
    private boolean snake2Lost = false; // Flag to check if Snake 2 lost

    private GameFrame parentFrame; // Reference to the parent frame

    // Constructor with game mode parameter and parent frame reference
    public GamePanel(boolean twoPlayerMode, GameFrame parentFrame) {
        this.twoPlayerMode = twoPlayerMode; // Set the game mode
        this.parentFrame = parentFrame; // Store reference to the parent frame
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.setDoubleBuffered(true); // Enable double buffering explicitly
        setupKeyBindings();
        loadRecordScore(); // Load the record score when the game starts
        startGame();
    }

    // Public Methods
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
            repaint();
        }

    }
    
    // Load the record score from the file
    private void loadRecordScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RECORD_SCORE_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                recordScore = Integer.parseInt(line);
            }
        } catch (IOException e) {
            // File not found or cannot be read, default record score is 0
        	recordScore = 0;
        }
    }

    // Save the record score to the file
    private void saveRecordScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RECORD_SCORE_FILE))) {
            writer.write(String.valueOf(recordScore));
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception
        }
    }

    // Game Initialization
    private void setupKeyBindings() {
        // Snake 1 (WASD controls)
        bindKey("W", "moveUp1", e -> { if (direction1 != 'D') direction1 = 'U'; });
        bindKey("S", "moveDown1", e -> { if (direction1 != 'U') direction1 = 'D'; });
        bindKey("A", "moveLeft1", e -> { if (direction1 != 'R') direction1 = 'L'; });
        bindKey("D", "moveRight1", e -> { if (direction1 != 'L') direction1 = 'R'; });

        // Snake 2 (Arrow Key controls)
        bindKey("UP", "moveUp2", e -> { if (direction2 != 'D') direction2 = 'U'; });
        bindKey("DOWN", "moveDown2", e -> { if (direction2 != 'U') direction2 = 'D'; });
        bindKey("LEFT", "moveLeft2", e -> { if (direction2 != 'R') direction2 = 'L'; });
        bindKey("RIGHT", "moveRight2", e -> { if (direction2 != 'L') direction2 = 'R'; });
    }

    @SuppressWarnings("serial")
    private void bindKey(String keyStroke, String actionName, ActionListener action) {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyStroke), actionName);
        getActionMap().put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.actionPerformed(e);
            }
        });
    }
    
    private void startGame() {
        // Initialize starting position for Snake 1
        x1[0] = 0; // Start Snake 1 near the top left
        y1[0] = 0;

        if (twoPlayerMode) {
            // Initialize starting position for Snake 2
            x2[0] = SCREEN_WIDTH - UNIT_SIZE; // Start Snake 2 on the opposite side
            y2[0] = SCREEN_HEIGHT - UNIT_SIZE;
        }
        
        newApple(); // Generate the first apple
        running = true; // Start the game
        timer = new Timer(DELAY, this);
        timer.start();
    }

    // Game Logic
    private void draw(Graphics g) {
        if (running) {
            // Draw apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Draw Snake 1 (player 1)
            for (int i = 0; i < bodyParts1; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                } else {
                    // Generate a random green-ish color for Snake 1
                    int red = random.nextInt(150);
                    int green = 100 + random.nextInt(156);
                    int blue = random.nextInt(150);
                    g.setColor(new Color(red, green, blue));
                }
                g.fillRect(x1[i], y1[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Draw Snake 2 if in two-player mode
            if (twoPlayerMode) {
                for (int i = 0; i < bodyParts2; i++) {
                    if (i == 0) {
                        g.setColor(Color.blue); // Snake 2 head
                    } else {
                        // Generate a random blue-ish color for Snake 2
                        int red = random.nextInt(150);
                        int green = random.nextInt(150);
                        int blue = 100 + random.nextInt(156);
                        g.setColor(new Color(red, green, blue));
                    }
                    g.fillRect(x2[i], y2[i], UNIT_SIZE, UNIT_SIZE);
                }
            } else {
                // Display score only in single-player mode
                g.setColor(Color.red);
                g.setFont(new Font("Ink Free", Font.BOLD, 40));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
                
                g.setFont(new Font("Ink Free", Font.BOLD, 30));
                g.drawString("Record: " + recordScore, (SCREEN_WIDTH - metrics.stringWidth("Record: " + recordScore)) / 2, SCREEN_HEIGHT - g.getFont().getSize());
            }
        } else {
            gameOver();
        }
    }

    private void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    private void move() {
        // Move Snake 1
        for (int i = bodyParts1; i > 0; i--) {
            x1[i] = x1[i - 1];
            y1[i] = y1[i - 1];
        }

        switch (direction1) {
            case 'U':
                y1[0] -= UNIT_SIZE;
                break;
            case 'D':
                y1[0] += UNIT_SIZE;
                break;
            case 'L':
                x1[0] -= UNIT_SIZE;
                break;
            case 'R':
                x1[0] += UNIT_SIZE;
                break;
        }

        // Move Snake 2 if in two-player mode
        if (twoPlayerMode) {
            for (int i = bodyParts2; i > 0; i--) {
                x2[i] = x2[i - 1];
                y2[i] = y2[i - 1];
            }

            switch (direction2) {
                case 'U':
                    y2[0] -= UNIT_SIZE;
                    break;
                case 'D':
                    y2[0] += UNIT_SIZE;
                    break;
                case 'L':
                    x2[0] -= UNIT_SIZE;
                    break;
                case 'R':
                    x2[0] += UNIT_SIZE;
                    break;
            }
        }
    }

    private void checkApple() {
        // Check if Snake 1 eats the apple
        if ((x1[0] == appleX) && (y1[0] == appleY)) {
            bodyParts1++;
            applesEaten++;
            newApple();
        }

        // Check if Snake 2 eats the apple in two-player mode
        if (twoPlayerMode && (x2[0] == appleX) && (y2[0] == appleY)) {
            bodyParts2++;
            newApple();
        }
    }

    private void checkCollisions() {
        snake1Lost = false;
        snake2Lost = false;

        // Check Snake 1 collisions with itself
        for (int i = bodyParts1; i > 0; i--) {
            if ((x1[0] == x1[i]) && (y1[0] == y1[i])) {
                snake1Lost = true;
                running = false;
            }
        }

        // Check if Snake 1 hits the borders
        if (x1[0] < 0 || x1[0] >= SCREEN_WIDTH || y1[0] < 0 || y1[0] >= SCREEN_HEIGHT) {
            snake1Lost = true;
            running = false;
        }

        if (twoPlayerMode) {
            // Check Snake 2 collisions with itself
            for (int i = bodyParts2; i > 0; i--) {
                if ((x2[0] == x2[i]) && (y2[0] == y2[i])) {
                    snake2Lost = true;
                    running = false;
                }
            }

            // Check if Snake 2 hits the borders
            if (x2[0] < 0 || x2[0] >= SCREEN_WIDTH || y2[0] < 0 || y2[0] >= SCREEN_HEIGHT) {
                snake2Lost = true;
                running = false;
            }

            // Check if Snake 1 collides with Snake 2
            for (int i = 0; i < bodyParts2; i++) {
                if (x1[0] == x2[i] && y1[0] == y2[i]) {
                    snake1Lost = true;
                    running = false;
                }
            }

            // Check if Snake 2 collides with Snake 1
            for (int i = 0; i < bodyParts1; i++) {
                if (x2[0] == x1[i] && y2[0] == y1[i]) {
                    snake2Lost = true;
                    running = false;
                }
            }
        }

        if (!running) {
            timer.stop();
        }
    }

    private void gameOver() {
        // Determine the game over message based on the game mode
        String message = "";

        if (twoPlayerMode) {
            // Check if both snakes lost at the same time
            if (snake1Lost && snake2Lost) {
                // Determine the winner based on who ate more apples
                if (bodyParts1 > bodyParts2) {
                    message = "What a close match! Green Snake triumphs by eating more apples! \nReady for another challenge?";
                } else if (bodyParts2 > bodyParts1) {
                    message = "What a close match! Blue Snake triumphs by eating more apples! \nReady for another challenge?";
                } else {
                    message = "It's a tie! Both snakes gave it their all! \nCare for a rematch to settle the score?";
                }
            } else if (snake1Lost) {
                // If only Snake 1 lost
                message = "Victory for Blue Snake! Green Snake falls short! \nWant a rematch to turn the tables?";
            } else if (snake2Lost) {
                // If only Snake 2 lost
                message = "Victory for Green Snake! Blue Snake falls short! \nWant a rematch to turn the tables?";
            }
        } else {
            // Single player mode message
            message = "Game Over! You scored " + applesEaten + " apples! \nThink you can beat your score? Try again!";
            
            // Update and save record score if needed
            if (applesEaten > recordScore) {
            	recordScore = applesEaten;
                saveRecordScore(); // Save the new record score to the file
            }
        }

        // Create custom buttons
        Object[] options = {"Restart", "Exit"};

        // Show the pop-up dialog for restarting or exiting
        int response = JOptionPane.showOptionDialog(
            this,
            message,
            "Game Over",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]  // Set the initially focused button to "Restart"
        );

        if (response == 0) {  // "Restart" button clicked
            parentFrame.restartGame();
        } else {  // "Exit" button clicked or dialog closed
            System.exit(0);
        }
    }
}
