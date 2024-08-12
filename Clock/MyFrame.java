package mypackage;

import java.awt.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

public class MyFrame extends JFrame {
 
	 SimpleDateFormat timeFormat;
	 SimpleDateFormat dayFormat;
	 SimpleDateFormat dateFormat;
	 JLabel timeLabel;
	 JLabel dayLabel;
	 JLabel dateLabel;
	 String time;
	 String day;
	 String date;
	
	 MyFrame() {
		  
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setTitle("Clock");
		 this.setLayout(new FlowLayout());
		 this.setSize(350,200);
		 this.setResizable(false);
		 this.setLocationRelativeTo(null);
		  
		 timeFormat = new SimpleDateFormat("hh:mm:ss a");
		 dayFormat = new SimpleDateFormat("EEEE");
		 dateFormat = new SimpleDateFormat("MMMMM dd, yyyy");
		  
		 timeLabel = new JLabel();
		 timeLabel.setFont(new Font("Verdana",Font.PLAIN,45));
		 timeLabel.setForeground(new Color(0x00FF00));
		 timeLabel.setBackground(Color.black);
		 timeLabel.setOpaque(true);
		  
		 dayLabel = new JLabel();
		 dayLabel.setFont(new Font("Ink Free",Font.PLAIN,40));
		  
		 dateLabel = new JLabel();
		 dateLabel.setFont(new Font("Ink Free",Font.PLAIN,35));
		  
		  
		 this.add(timeLabel);
		 this.add(dayLabel);
		 this.add(dateLabel);
		 this.setVisible(true);
		  
		 setTime();
	 }
	 
	 public void setTime() {
		  
		 while(true) {
			  time = timeFormat.format(Calendar.getInstance().getTime());
			  timeLabel.setText(time);
			  
			  day = dayFormat.format(Calendar.getInstance().getTime());
			  dayLabel.setText(day);
			  
			  date = dateFormat.format(Calendar.getInstance().getTime());
			  dateLabel.setText(date);
		  
			  try {
				  Thread.sleep(1000);
			  } 
			  catch (InterruptedException e) {
				  e.printStackTrace();
			  }
		  }
	 }
}
