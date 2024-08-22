package mypackage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Calculator3 implements ActionListener{

	JFrame frame;
	JTextField textField;
	JButton[] numberButtons = new JButton[10];
	JButton[] functionButtons = new JButton[9];
	JButton addButton, subButton, mulButton, divButton, decButton, equButton, delButton, clrButton, negButton;
	JPanel panel;
	
	String mathExpression = "";
	
	Font myFont = new Font("Ink free", Font.BOLD,25);
	
	double num1 = 0 , num2 = 0 , result = 0;
	char operator;
	 
	Calculator3(){
		frame = new JFrame("Calculator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(420,550);
		frame.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(50,25,300,50);
		textField.setFont(myFont);
		textField.setEditable(false);
		
		addButton = new JButton("+");
		subButton = new JButton("-");
		mulButton = new JButton("*");
		divButton = new JButton("/");
		decButton = new JButton(".");
		equButton = new JButton("=");
		delButton = new JButton("Delete");
		clrButton = new JButton("Clear");
		negButton = new JButton("(-)");
		
		functionButtons[0] = addButton;
		functionButtons[1] = subButton;
		functionButtons[2] = mulButton;
		functionButtons[3] = divButton;
		functionButtons[4] = decButton;
		functionButtons[5] = equButton;
		functionButtons[6] = delButton;
		functionButtons[7] = clrButton;
		functionButtons[8] = negButton;
		
		for(int i = 0; i < 9; i ++) {
			functionButtons[i].addActionListener(this);
			functionButtons[i].setFont(myFont);
			functionButtons[i].setFocusable(false);
		}
		
		for(int i = 0; i < 10; i ++) {
			numberButtons[i] = new JButton(String.valueOf(i));
			numberButtons[i].addActionListener(this);
			numberButtons[i].setFont(myFont);
			numberButtons[i].setFocusable(false);
		}
		
		negButton.setBounds(50,430,80,50);
		delButton.setBounds(130,430,110,50);
		clrButton.setBounds(240,430,110,50);
		
		panel = new JPanel();
		panel.setBounds(50,100,300,300);
		panel.setLayout(new GridLayout(4,4,10,10));
		
		panel.add(numberButtons[1]);
		panel.add(numberButtons[2]);	
		panel.add(numberButtons[3]);
		panel.add(addButton);
		panel.add(numberButtons[4]);
		panel.add(numberButtons[5]);	
		panel.add(numberButtons[6]);
		panel.add(subButton);	
		panel.add(numberButtons[7]);
		panel.add(numberButtons[8]);	
		panel.add(numberButtons[9]);
		panel.add(mulButton);		
		panel.add(decButton);
		panel.add(numberButtons[0]);
		panel.add(equButton);
		panel.add(divButton);
		
		frame.add(panel);
		frame.add(negButton);
		frame.add(delButton);
		frame.add(clrButton);
		frame.add(textField);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	
	
	public static void main(String[] args) {
		
		new Calculator3();
	
	}


	public double eval(String mathExpression) {
	    if (mathExpression.isEmpty()) {
	        throw new IllegalArgumentException("Input Required");
	    }
		return new Object() {	    
	        int pos = -1, ch;
	        
	        void nextChar() {
	            pos = pos + 1;
	            if (pos < mathExpression.length()) {
	            	ch = mathExpression.charAt(pos);
	            }
	            else {
	            	ch = -1;
	            }
	        }
	        
	        boolean eat(int charToEat) {
	            if (ch == charToEat) {
	                nextChar();
	                return true;
	            }
	            else
	            	return false;
	        }
	        
	        double parse() {
	            nextChar();
	            double result = parseAddSub();
	            if (pos < mathExpression.length()) throw new IllegalArgumentException("Invalid Char: " + (char) ch);
	            return result;
	        }
	        
	        double parseAddSub() {
	            double result = parseMulDiv();
	            for (;;) {
	                if (eat('+')) {
	                	if (ch == -1) throw new IllegalArgumentException("No Operand After +");
	                	result += parseMulDiv();
	                }
	                else if (eat('-')) {
	                	if (ch == -1) throw new IllegalArgumentException("No Operand After -");
	                	result -= parseMulDiv();
	                }
	                else 
	                	return result;
	            }
	        }
	        
	        double parseMulDiv() {
	            double result = parseNumber();
	            for (;;) {
	                if (eat('*')) { 
	                	if (ch == -1) throw new IllegalArgumentException("No Operand After *");
	                	result *= parseNumber();
	                }
	                else if (eat('/')) {
	                	if (ch == -1) throw new IllegalArgumentException("No Operand After /");
	                	double divisor = parseNumber();
	                	if (divisor == 0) throw new ArithmeticException("Division by Zero");
	                	result /= divisor;
	                }
	                else 
	                	return result;
	            }
	        }
	        
	        double parseNumber() {
	        	if (eat('-')) return -parseNumber(); 
	        	
	            double result;
	            int startPos = this.pos;
	            if ((ch >= '0' && ch <= '9') || ch == '.') { 
	                while ((ch >= '0' && ch <= '9') || ch == '.') {
	                	nextChar();
	                }
	                try {
	                result = Double.parseDouble(mathExpression.substring(startPos, this.pos));
	                } catch (NumberFormatException e) {
	                	throw new NumberFormatException("Extra Decimals");
	                }
	            } 
	            else {
	            	throw new IllegalArgumentException("Invalid Char: " + (char) ch);
	            }
	            return result;
	        }
	    }.parse();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		for(int j = 0; j < 10; j ++) {
			if(e.getSource() == numberButtons[j]) {
				textField.setText(textField.getText().concat(String.valueOf(j)));
			}
		}
		
		if(e.getSource()==decButton) {
			textField.setText(textField.getText().concat("."));
		}
		if(e.getSource()==addButton) {
			mathExpression += textField.getText() + "+";
			textField.setText("");
		}
		if(e.getSource()==subButton) {
			mathExpression += textField.getText() + "-";
			textField.setText("");
		}
		if(e.getSource()==mulButton) {
			mathExpression += textField.getText() + "*";
			textField.setText("");
		}
		if(e.getSource()==divButton) {
			mathExpression += textField.getText() + "/";
			textField.setText("");
		}
		if(e.getSource()==equButton) {
			mathExpression += textField.getText();
			try {
				result = this.eval(mathExpression);
				if(result == (int) result) {
					textField.setText(String.valueOf((int)result));
				}
				else {
					textField.setText(String.valueOf(result));
				}
			} catch (ArithmeticException ex) {
		        textField.setText("Error: " + ex.getMessage());
		    } catch (NumberFormatException ex) {
		        textField.setText("Error: " + ex.getMessage());
		    } catch (IllegalArgumentException ex) {
		        textField.setText("Error: " + ex.getMessage());
		    }
			
			mathExpression = "";
		}
		if(e.getSource()==clrButton) {
			textField.setText("");
		}
		if(e.getSource()==delButton) {
			String string = textField.getText();
			textField.setText("");
			for(int j = 0; j < string.length() - 1; j++) {
				textField.setText(textField.getText()+string.charAt(j));
			}
		}
		if(e.getSource()==negButton) {
			double temp = Double.parseDouble(textField.getText());
			temp *= -1;
			if(temp == (int) temp) {
				textField.setText(String.valueOf((int)temp));
			}
			else {
				textField.setText(String.valueOf(temp));
			}
		}
	}
}
