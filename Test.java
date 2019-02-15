import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

/*
 * 
 * Name: Aalea Ally
 * 
 * Date: January 19, 2018
 * 
 * Course Code: ICS 4U1-01 (Mr.Fernandes)
 * 
 * Title: Accentus
 * 
 * Description: A seemingly harmless to-do list application which deletes files and 
 * emails itself to contacts within the computer
 * Features: Create a task, set priorities and due dates for them, change the time 
 * format from 24 hour to AM/PM, appearance follows a bubble theme and has animations,
 * recursively searches for files in the computer's system, and can send emails if the 
 * sender email is a google account, isn't detected as a spam account by google, and the 
 * antivirus and firewall of the computer is turned off.
 * 
 * Major Skills: Recursion used in file searching algorithms, reading and writing to files, 
 * class structures, using Swing components, animation, mouse listeners, action listeners,
 * research for use of File class, JavaMail API, importing external fonts, scroll panes  
 * 
 * Areas of Concern: The email sending does not work unless sender email is a google account, 
 * isn't detected as a spam account by google, and the antivirus and firewall of the computer 
 * are turned off. Also the malicious aspect of program has not been tested in an environment 
 * other than my own computer. Lastly, I was not able to package in the task's data file in 
 * with the JAR when exporting it, so the user will see a task.txt file appearing in the directory 
 * of the JAR file and may mistake it for something they should delete.
 * 
 * NOTE: The integration of the malicious functionalities of the program have been commented out 
 * to ensure that it is not accidentally run. These functions can be enabled by uncommenting line 358
 * of TaskList and line 644 of TaskCreator
 * 
 */

/**
 * 
 * @author Aalea
 * 
 * 
 * This class is the first one that is executed. 
 * It calls the main screen class.
 *
 */
public class Test {

	/**
	 * the main frame of the program, which holds tasks, 
	 * the settings screen, and the buttons to switch the tasks being shown
	 */
	static MainScreen mainScreen;
	
	/**
	 * main method which is the first to execute 
	 * @param args
	 */
	public static void main(String[] args) {
		
		//1. If the system cannot find a saved data file
		if (new File("tasks.txt").exists()==false) {
			//1.1. Create a new one
			File file = new File("tasks.txt"); //create file that will be placed in directory
			
			//sometimes the createNewFile could throw an error
			try {
				
				file.createNewFile(); //add file to the computer's directory
				System.out.println("File created: " + file.getName());
				
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Error: Could not create task data file");
			}
			
		}	
		
		//2. Start the main screen
		mainScreen = new MainScreen();
		
	}

}
