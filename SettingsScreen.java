import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Container;

import javax.swing.ImageIcon;

/**
 * 
 * @author aalea
 * 
 * This is a JPanel that holds all components for the settings screen.
 *
 */
public class SettingsScreen extends JPanel implements MouseListener {
	
	/**
	 * this integer represents which time format the user wishes to use
	 * 1 - AM/PM
	 * 2 - 24 Hour
	 */
	private int timeFormat;
	
	/**
	 * this toggle button signifies if the user wants the time format in AM/PM
	 */
	private JToggleButton tglbtnAMPMTime;
	
	/**
	 * this toggle button signifies if the user wants the time format in 24 hours
	 */
	private JToggleButton tglbtnHour;
	
	/**
	 * this buttons triggers the resetting of all tasks
	 */
	private JButton btnResetTasks;
	
	/**
	 * this button exits the settings screen
	 */
	private JButton btnBack;
	
	/**
	 * constructor method which initializes the time format and adds everything to the screen
	 */
	public SettingsScreen() {
		
		setBackground(new Color(204, 153, 255)); //set the background colour
		System.out.println("settings screen up");
		setSize(654, 464); //set the size of the panel
		
		this.timeFormat = 1; //set default time format to AM/PM
		
		//1. Enable the layout to have absolute positioning
		setLayout(null);
		
		//2. Add a label with text "Time Format"
		JLabel lblTimeFormat = new JLabel("Time Format");
		//3. Set the font of the label
		lblTimeFormat.setFont(new Font("Raleway", Font.PLAIN, 25));
		//4. Set the bounds of the label
		lblTimeFormat.setBounds(63, 71, 174, 93);
		//5. Add the label to the screen
		add(lblTimeFormat);
		
		//6. Add a toggle button with text "AM/PM"
		tglbtnAMPMTime = new JToggleButton("AM/PM");
		//7. Set the colour to salmon
		tglbtnAMPMTime.setBackground(new Color(255, 153, 51));
		//8. Set the font
		tglbtnAMPMTime.setFont(new Font("Raleway", Font.PLAIN, 25));
		//9. Set the bounds
		tglbtnAMPMTime.setBounds(247, 81, 137, 75);
		//10. Add the toggle button to the screen
		add(tglbtnAMPMTime);
		
		//11. Add a toggle button with text "24 Hour"
		tglbtnHour = new JToggleButton("24 Hour");
		//12. Set the colour to pale green
		tglbtnHour.setBackground(new Color(153, 255, 153));
		//13. Set the font
		tglbtnHour.setFont(new Font("Raleway", Font.PLAIN, 25));
		//14. Set the bounds
		tglbtnHour.setBounds(394, 81, 137, 75);
		//15. Add the toggle button to the screen
		add(tglbtnHour);
		
		//16. Add a reset button with text, "Reset Tasks?"
		btnResetTasks = new JButton("Reset Tasks?");
		//17. Set the colour to red
		btnResetTasks.setBackground(new Color(255, 204, 153));
		//18. Set the font
		btnResetTasks.setFont(new Font("Raleway", Font.PLAIN, 28));
		//19. Set the bounds
		btnResetTasks.setBounds(172, 251, 240, 61);
		//20. Add the button to the screen
		add(btnResetTasks);
		
		//21. Add a 'Back' button
		btnBack = new JButton("Back");
		//22. Set the bounds
		btnBack.setBounds(28, 382, 89, 23);
		//23. Add the button to the screen
		add(btnBack);
		
		//29. Add mouse listener to the AM/PM toggle button
		tglbtnAMPMTime.addMouseListener(this);
		//30. Add mouse listener to the 24 Hour toggle button
		tglbtnHour.addMouseListener(this);
		//31. Add mouse listener to the Reset button
		btnResetTasks.addMouseListener(this);
		//add a mouse listener to the back button
		btnBack.addMouseListener(this);
		
		setVisible(true); //make the screen visible
		
	}

	/**
	 * controls the events when the mouse is clicked
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		
		//make the tasks panel invisible, just in case any components show up
		Test.mainScreen.getTasksPanel().setVisible(false);
		
		//1. If AM/PM button was clicked
		if (e.getSource()==tglbtnAMPMTime) {
			//set the 24 hour button to unclicked
			tglbtnHour.setSelected(false);
			//1.1. Set the time format to 1
			timeFormat = 1;
		}
		//2. If the 24 hour button was clicked
		else if (e.getSource()==tglbtnHour) {
			//set the AM/PM button to unclicked
			tglbtnAMPMTime.setSelected(false);
			//2.1. Set the time format to 2
			timeFormat = 2;
		}
		//3. If the reset button was clicked
		else if (e.getSource()==btnResetTasks) {

			File file = new File("tasks.txt"); //create file that will be placed in directory
			
			//try to delete and create a new task data file
			try {
				
				file.delete(); //delete file

				file.createNewFile(); //add new file to directory

			} catch (IOException err) { //if there is any error
				
				System.out.println(err);
				
			}

			Test.mainScreen.getTasksPanel().updateTasks(); //update the tasks on the screen
			//make the tasks panel invisible, just in case any components show up
			Test.mainScreen.getTasksPanel().setVisible(false); //doesn't entirely work, a section still shows up
		}
		else if (e.getSource()==btnBack) { //if the back button was clicked
			
			this.setVisible(false); //make the settings screen invisible
			
			Test.mainScreen.getRidSettings(timeFormat); //send the updated time format
			
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
