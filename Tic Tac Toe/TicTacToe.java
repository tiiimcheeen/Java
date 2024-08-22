import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class TicTacToe implements ActionListener{
	
	Random random = new Random();
	JFrame frame = new JFrame();
	JPanel title_panel = new JPanel();
	JPanel button_panel = new JPanel();
	JLabel textfield = new JLabel();
	JButton[] buttons = new JButton[9];
	JButton resetButton = new JButton("Reset");
	boolean player1_turn;
	
	private static final Font TITLE_FONT = new Font("Ink Free", Font.BOLD, 60);
	private static final Font BUTTON_FONT = new Font("MV Boli", Font.BOLD, 100);
	private static final Font RESET_BUTTON_FONT = new Font("Verdana", Font.PLAIN, 36);
	private static final Color X_COLOR = new Color(220, 20, 60);
	private static final Color O_COLOR = new Color(30, 144, 255);
	
	public TicTacToe(){
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800,800);
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		textfield.setBackground(Color.DARK_GRAY);
		textfield.setForeground(Color.WHITE);
		textfield.setText("Tic Tac Toe");
		textfield.setFont(TITLE_FONT);
		textfield.setHorizontalAlignment(JLabel.CENTER);
		textfield.setOpaque(true);
		
		resetButton.setFont(RESET_BUTTON_FONT);
		resetButton.setFocusable(false);
		
		title_panel.setLayout(new BorderLayout());
		title_panel.setPreferredSize(new Dimension(800, 120));
		
		button_panel.setLayout(new GridLayout(3,3));
		
		for(int i=0;i<9;i++) {
			buttons[i] = new JButton();
			button_panel.add(buttons[i]);
			buttons[i].setFont(BUTTON_FONT);
			buttons[i].setFocusable(false);
			buttons[i].addActionListener(this);
		}
		
		resetButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        resetGame();
		    }
		});
		
		title_panel.add(textfield);
		frame.add(resetButton, BorderLayout.SOUTH);
		frame.add(title_panel,BorderLayout.NORTH);
		frame.add(button_panel);
		
		firstTurn(true);
	}
	
	private void firstTurn(boolean withDelay) {
		if (withDelay) {
		    for (int i = 0; i < 9; i++) {
		        buttons[i].setEnabled(false);
		    }
		    
		    Timer timer = new Timer(2000, new ActionListener() {
		        @Override
		        public void actionPerformed(ActionEvent e) {
	                setPlayerTurn();
	                setButtonsEnabled(true);
		        }
		    });
		    
		    timer.setRepeats(false);
		    timer.start();
		}
		else {
            setPlayerTurn();
		}
	}
	
	private void check() {
		int[][] winConditions = {
			    {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, 
			    {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, 
			    {0, 4, 8}, {2, 4, 6}  
			};

			for (int[] win : winConditions) {
			    if (buttons[win[0]].getText().equals("X") &&
			        buttons[win[1]].getText().equals("X") &&
			        buttons[win[2]].getText().equals("X")) {
			        showWin(win[0], win[1], win[2], "X");
			        return;
			    }
			    if (buttons[win[0]].getText().equals("O") &&
			        buttons[win[1]].getText().equals("O") &&
			        buttons[win[2]].getText().equals("O")) {
			        showWin(win[0], win[1], win[2], "O");
			        return;
			    }
			}
		
	    boolean isDraw = true;
	    for (int i = 0; i < 9; i++) {
	        if (buttons[i].getText().isEmpty()) {
	            isDraw = false;
	            break;
	        }
	    }
	    if (isDraw) {
	        textfield.setText("Draw");
	        setButtonsEnabled(false);
	    }
	}
	
	private void showWin(int a, int b, int c, String player) {
	    buttons[a].setBackground(Color.GREEN);
	    buttons[b].setBackground(Color.GREEN);
	    buttons[c].setBackground(Color.GREEN);
	    
	    setButtonsEnabled(false);
	    textfield.setText(player + " wins");
	}
	
 	public static void main(String[] args) {
		new TicTacToe();
	}
 	
 	private void resetGame() {
 	    for (int i = 0; i < 9; i++) {
 	        buttons[i].setText("");
 	        buttons[i].setEnabled(true);
 	        buttons[i].setBackground(null);
 	    }
 	    textfield.setText("Tic Tac Toe");
 	    firstTurn(false);
 	}
 	
 	private void setPlayerTurn() {
 	    if (random.nextInt(2) == 0) {
 	        player1_turn = true;
 	        textfield.setText("X turn");
 	    } else {
 	        player1_turn = false;
 	        textfield.setText("O turn");
 	    }
 	}
 	
 	private void setButtonsEnabled(boolean enabled) {
 	    for (JButton button : buttons) {
 	        button.setEnabled(enabled);
 	    }
 	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(int i=0;i<9;i++) {
			if(e.getSource()==buttons[i]) {
				if(player1_turn) {
					if(buttons[i].getText().isEmpty()) {
						buttons[i].setForeground(X_COLOR);
						buttons[i].setText("X");
						player1_turn=false;
						textfield.setText("O turn");
						check();
					}
				}
				else {
					if(buttons[i].getText().isEmpty()) {
						buttons[i].setForeground(O_COLOR);
						buttons[i].setText("O");
						player1_turn=true;
						textfield.setText("X turn");
						check();
					}
				}
			}			
		}
	}
}
