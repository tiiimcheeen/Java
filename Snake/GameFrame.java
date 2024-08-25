import javax.swing.*;

public class GameFrame extends JFrame{

	private static final long serialVersionUID = 1L; // To avoid warnings
    private GamePanel gamePanel; // Store the current game panel

    GameFrame() {       
        startNewGame(); // Initialize and start the game
    }

    private void startNewGame() {
        // Prompt the user to select the game mode before initializing the game panel
        Object[] options = {"Single Player", "Two Players"};
        int response = JOptionPane.showOptionDialog(
            this,
            "Choose Game Mode",
            "Snake Game",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );

        // Check if the user closed the dialog
        if (response == JOptionPane.CLOSED_OPTION) {
            System.exit(0);  // Exit the program if the dialog was closed
        }

        boolean twoPlayerMode = (response == 1); // Set game mode based on selection

        // If there is an existing game panel, remove it
        if (gamePanel != null) {
            this.remove(gamePanel);
        }

        // Create a new game panel with the selected mode and add it to the frame
        gamePanel = new GamePanel(twoPlayerMode, this);
        this.add(gamePanel);
        
        // Frame setup after adding the game panel to avoid flickering
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack(); // Resize frame to fit the new panel
        this.setLocationRelativeTo(null); // Center the frame on the screen
        this.setVisible(true); // Make the frame visible
    }

    // Method to be called from GamePanel when restarting the game
    public void restartGame() {
        startNewGame(); // Restart the game by starting a new game
    }
}
