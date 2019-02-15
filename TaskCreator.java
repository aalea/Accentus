import javax.swing.JFrame;
import java.awt.FlowLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

/**
 * 
 * @author aalea
 * 
 * This is a JFrame that acts like a popup and asks the user for the 
 * necessary attributes to create a task. It takes in the description, 
 * date, and optionally takes in a reminder date and the priority.
 * 
 * Error checking for invalid dates and descriptions included.
 *
 */
public class TaskCreator extends JFrame implements MouseListener {
	
	//the reason why these fields are protected instead of private is so that the 
	//task editor can access them, as this class is its super
	
	/**
	 * this textfield holds the description of the task the user can type in
	 */
	protected JTextField textFieldTaskName;
	
	/**
	 * this textfield holds the day # the task is due the user can type in
	 */
	protected JTextField textFieldDay;
	
	/**
	 * this textfield holds the day # the task reminder is, that the user can type in
	 */
	protected JTextField textFieldDayReminder;
	
	/**
	 * this button triggers the compiling of the task
	 */
	protected JButton btnDone;
	
	/**
	 * this button exits out of this frame
	 */
	protected JButton btnBack;
	
	/**
	 * this button sets the priority to 0
	 */
	protected JButton btnNone;
	
	/**
	 * this button sets the priority to 1
	 */
	protected JButton btnYellow;
	
	/**
	 * this button sets the priority to 2
	 */
	protected JButton btnOrange;
	
	/**
	 * this button sets the priority to 3
	 */
	protected JButton btnRed;
	
	/**
	 * this string holds the description of the task being created
	 */
	protected String description;
	
	/**
	 * this date is the due date of the task
	 */
	protected Date date;
	
	/**
	 * this date is the time a reminder of the task will be sent out
	 */
	protected Date reminder;
	
	/**
	 * this integer represents the priority of the task, 
	 * 0 being no priority, 1 being a yellow flag, 2 being an orange flag, and 3 being a red flag
	 */
	protected int priority;
	
	/**
	 * this is the combo box showing the months
	 */
	protected JComboBox<String> comboBoxMonth;
	
	/**
	 * this is the combo box showing the years
	 */
	protected JComboBox<String> comboBoxYear;
	
	/**
	 * this is an array of the months in the year the application refers to
	 */
	protected String[] monthArray;
	
	/**
	 * this is an array of the years the application will display.
	 * it will be filled with years from the current year, to 20 years from now
	 */
	protected String[] yearArray;
	
	/**
	 * this is an array of the hours of the day the application will display.
	 * it will change based on the time format the user selects
	 */
	protected String[] hourArray;
	
	/**
	 * this is an array of the minutes of the hour the applications will display
	 */
	protected String[] minuteArray;
	
	/**
	 * this is the task object that the user will create with their input
	 */
	private Task task;

	/**
	 * this is the combo box to select the month for the reminder
	 * 
	 * UNUSED - it is not added to the frame as the reminder feature isn't enabled
	 */
	protected JComboBox<String> comboBoxMonthReminder;

	/**
	 * this is the combo box to select the hour for the due date
	 * 
	 */
	protected JComboBox<String> comboBoxHour;

	/**
	 * this is the combo box to select the minute for the due date
	 * 
	 */
	protected JComboBox<String> comboBoxMinute;

	/**
	 * this is the combo box to select the year for the reminder
	 * 
	 * UNUSED - it is not added to the frame as the reminder feature isn't enabled
	 */
	protected JComboBox<String> comboBoxYearReminder;

	/**
	 * this is the combo box to select the hour for the reminder
	 * 
	 * UNUSED - it is not added to the frame as the reminder feature isn't enabled
	 */
	protected JComboBox<String> comboBoxHourReminder;

	/**
	 * this is the combo box to select the minute for the reminder
	 * 
	 * UNUSED - it is not added to the frame as the reminder feature isn't enabled
	 */
	protected JComboBox<String> comboBoxMinuteReminder;
	
	/**
	 * constructor method which sets up the frame and adds all the components
	 */
	public TaskCreator() {
		
		setSize(551,342); //set the size of the frame
		setResizable(false); //make it so that the user cannot change the size of the frame 
		toFront(); //bring this jframe to the front, where the user will see it over other frames
		
		//set the default priority of the task to 0
		this.priority = 0;
		
		//1. Set the background colour to light blue
		getContentPane().setBackground(new Color(204, 255, 255));
		//2. Make it so that the frame disappears when the 'X' is clicked
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//3. Set the title of the frame to "Add Task"
		setTitle("Add Task");
		
		//4. Add a label with text "Task Name"
		JLabel lblTaskName = new JLabel("Task Name");
		lblTaskName.setBounds(25, 70, 150, 25); //set the bounds
		lblTaskName.setFont(new Font("Raleway", Font.PLAIN, 19)); //set the font
		
		//5. Add a textfield beside that label
		textFieldTaskName = new JTextField();
		textFieldTaskName.setBounds(155, 70, 342, 20); //set the bounds
		textFieldTaskName.setColumns(10); //set position
		
		//6. Add a label with text "Due Date"
		JLabel lblDate = new JLabel("Due Date");
		lblDate.setBounds(25, 125, 200, 25); //set the bounds
		lblDate.setFont(new Font("Raleway", Font.PLAIN, 19)); //set the font
		
		//7. Add a combo box to collect the month of the due date
			//7.1. Create a string array of length 12
			//7.2. Add the months of the year to the array
		monthArray = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
			
		//7.3. Initialize the combo box with the array
		comboBoxMonth = new JComboBox<String>(monthArray);
		comboBoxMonth.setBounds(155, 125, 96, 20); //set the bounds
		
		//8. Add a combo box to collect the year of the due date
			//8.1. Create a string array of length 20
		yearArray = new String[20];
			//8.2. Add all the years from today to 20 years from now to the array
		Calendar now = Calendar.getInstance();   // Gets the current date and time
		int year = now.get(Calendar.YEAR);       // The current year
		//fills in the years
		for (int i = 0; i < 20; i++) {
			yearArray[i] = Integer.toString(year + i); //add all the years from the current year to current year + 20
		}
			//8.3. Initialize the combo box with the array
		comboBoxYear = new JComboBox<String>(yearArray);
		comboBoxYear.setBounds(301, 125, 64, 20); //set the bounds
		
		//9. Add a textfield to collect the day of the due date
		textFieldDay = new JTextField();
		textFieldDay.setBounds(259, 125, 32, 20); //set the bounds
		textFieldDay.setColumns(10); //set position
		
		//10. Add a label with text "Set Reminder
		JLabel lblReminder = new JLabel("Set Reminder");
		lblReminder.setBounds(25, 133, 126, 25); //set bounds
		lblReminder.setFont(new Font("SansSerif", Font.PLAIN, 19)); //set the font
		
		//11. Add a combo box to collect the month of the reminder
			//11.1. Initialize the combo box with the string array of months from before
		comboBoxMonthReminder = new JComboBox<String>(monthArray);
		comboBoxMonthReminder.setBounds(155, 139, 96, 20); //set the bounds
		
		//add a label to represent "at"
		JLabel lblAt1 = new JLabel("@");
		lblAt1.setHorizontalAlignment(SwingConstants.CENTER); //centre the text
		lblAt1.setBounds(363, 125, 21, 14); //set the bounds

		//add another label to represent "at"
		JLabel lblAt2 = new JLabel("@");
		lblAt2.setHorizontalAlignment(SwingConstants.CENTER); //centre the text
		lblAt2.setBounds(363, 142, 21, 14); //set the bounds
		
		//12. Add a combo box to collect the hour of the due date
			//12.1. Create an array of length 24
		hourArray = new String[24];
			//12.2. Check the time format stored in the settings screen
			//12.3. If the time format is AM/PM
		if (Test.mainScreen.getTimeFormat()==1) {
				//12.3.1. Add all the hours from 12AM to 12PM to the array
			hourArray = new String[] {"12 AM", "1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM", "7 AM", "8 AM", "9 AM", "10 AM", "11 AM",
					"12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM", "9 PM", "10 PM", "11 PM"};
		}
			//12.4. If the time format is 24 hour
		else {
				//12.4.1 Add all the hours from 0:00 to 23:00
			for (int i = 0; i < 24; i++) {
				hourArray[i] = Integer.toString(i);
			}
		}
			//12.5. Initialize the combo box with the array
		comboBoxHour = new JComboBox<String>(hourArray);
		comboBoxHour.setBounds(383, 125, 52, 20); //set the bounds

		//add a label with ":"
		JLabel lblColon1 = new JLabel(":");
		lblColon1.setBounds(439, 125, 4, 14); //set the bounds
		
		//13. Add a combo box to collect the minute of the due date
			//13.1. Create an array of length 60
		minuteArray = new String[60];
			//13.2. Add numbers from 0 to 59 to the array
		for (int i = 0; i < 60; i++) {
			minuteArray[i] = Integer.toString(i);
			
			if (i < 10) //if the minute is between 0-9 add a '0' to the front of the string
				minuteArray[i] = "0" + minuteArray[i]; 
		}
			//13.3. Initialize the combo box with the array
		comboBoxMinute = new JComboBox<String>(minuteArray);
		comboBoxMinute.setBounds(447, 125, 50, 20); //set bounds
		
		//14. Add a text field to collect the day of the reminder
		textFieldDayReminder = new JTextField();
		textFieldDayReminder.setBounds(259, 139, 32, 20); //set bounds
		textFieldDayReminder.setColumns(10); //set position
		
		//15. Add a combo box to collect the year of the reminder
			//15.1. Create a string array of length 20
			//15.2. Add all the years from today to 20 years from now to the array
			//15.3. Initialize the combo box with the array
		comboBoxYearReminder = new JComboBox<String>(yearArray);
		comboBoxYearReminder.setBounds(301, 139, 64, 20); //set bounds
		
		//16. Add a combo box to collect the hour of the reminder
			//16.5. Initialize the combo box with the array of hours
		comboBoxHourReminder = new JComboBox<String>(hourArray);
		comboBoxHourReminder.setBounds(383, 139, 52, 20); //set bounds

		//add a label with ":"
		JLabel lblColon2 = new JLabel(":");
		lblColon2.setBounds(439, 142, 4, 14); //set bounds
		
		//17. Add a combo box to collect the minute of the reminder
			//17.3. Initialize the combo box with the minute array
		comboBoxMinuteReminder = new JComboBox<String>(minuteArray);
		comboBoxMinuteReminder.setBounds(447, 139, 50, 20); //set bounds
		
		//18. Add a label with text "Priority"
		JLabel lblPriority = new JLabel("Priority");
		lblPriority.setBounds(25, 199, 150, 25); //set bounds
		lblPriority.setFont(new Font("Raleway", Font.PLAIN, 19)); //set the font
		
		//19. Add a button with a white flag as an icon
		btnNone = new JButton();
		btnNone.setBounds(267, 199, 44, 45); //set bounds
		btnNone.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/whiteFlag.png")))); //set image
		btnNone.addMouseListener(this); //add mouse listener
		
		//20. Add a button with a yellow flag as an icon
		btnYellow = new JButton();
		btnYellow.setBounds(329, 199, 44, 45); //set bounds
		btnYellow.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/yellowFlag.png")))); //set image
		btnYellow.addMouseListener(this); //add mouse listener
		
		//21. Add a button with a orange flag as an icon
		btnOrange = new JButton();
		btnOrange.setBounds(391, 199, 44, 45); //set bounds
		btnOrange.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/orangeFlag.png")))); //set image
		btnOrange.addMouseListener(this); //add mouse listener
		
		//22. Add a button with a red flag as an icon
		btnRed = new JButton();
		btnRed.setBounds(453, 198, 44, 45); //set bounds
		btnRed.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/redFlag.png")))); //set image
		btnRed.addMouseListener(this); //add mouse listener
		
		//23. Add a 'Done' button
		btnDone = new JButton("Done");
		btnDone.setBounds(25, 279, 76, 23); //set bounds
		btnDone.addMouseListener(this); //add mouse listener
		
		//24. Add a 'Back' button with an action listener
		btnBack = new JButton("Back");
		btnBack.setBounds(421, 279, 76, 23); //set bounds
		btnBack.addMouseListener(this); //add mouse listener
		
		getContentPane().setLayout(null); //allow absolute positioning
		
		//add all the components, excluding the reminder ones
		getContentPane().add(lblTaskName);
		getContentPane().add(lblDate);
		getContentPane().add(lblPriority);
		//getContentPane().add(lblReminder);
		getContentPane().add(btnBack);
		getContentPane().add(btnNone);
		getContentPane().add(btnYellow);
		getContentPane().add(btnOrange);
		getContentPane().add(btnRed);
		getContentPane().add(btnDone);
		getContentPane().add(textFieldTaskName);
		getContentPane().add(comboBoxMonth);
		//getContentPane().add(comboBoxMonthReminder);
		getContentPane().add(textFieldDay);
		//getContentPane().add(textFieldDayReminder);
		//getContentPane().add(comboBoxYearReminder);
		getContentPane().add(comboBoxYear);
		//getContentPane().add(lblAt2);
		getContentPane().add(lblAt1);
		//getContentPane().add(comboBoxHourReminder);
		getContentPane().add(comboBoxHour);
		//getContentPane().add(lblColon2);
		getContentPane().add(lblColon1);
		getContentPane().add(comboBoxMinute);
		//getContentPane().add(comboBoxMinuteReminder);
		
		//set the default month to the current month
		comboBoxMonth.setSelectedItem(Calendar.getInstance().get(Calendar.MONTH)); 
		comboBoxMonthReminder.setSelectedItem(Calendar.getInstance().get(Calendar.MONTH));
		
		//set the default day to the current day of month
		textFieldDay.setText(Integer.toString(Calendar.getInstance().get(Calendar.DATE)));
		textFieldDayReminder.setText(Integer.toString(Calendar.getInstance().get(Calendar.DATE)));
		
		//set the default hour to the current hour
		comboBoxHour.setSelectedItem(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		comboBoxHourReminder.setSelectedItem(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		
		setVisible(true); //make everything visible	

	}

	/**
	 * this method controls the events that happens when a button is clicked
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		
		//1. If the button pressed is the 'Done' button
		if (e.getSource()==btnDone) {
			//call the method that checks if all the user's input is valid
			handleMouseOnDone();
		}
		//2. If the button pressed is the 'Back' button
		else if (e.getSource()==btnBack) {
			//2.1. Dispose of this object
			this.dispose();
		}
		//3. If the button pressed has the gray flag
		else if (e.getSource()==btnNone) {
			//3.1. Set the priority to 0
			priority = 0;
		}
		//4. If the button pressed has the yellow flag
		else if (e.getSource()==btnYellow) {
			//4.1. Set the priority to 1
			priority = 1;
		}
		//5. If the button pressed has the orange flag
		else if (e.getSource()==btnOrange) {
			//5.1. Set the priority to 2
			priority = 2;
		}
		//6. If the button pressed has the red flag
		else if (e.getSource()==btnRed) {
			//6.1. Set the priority to 3
			priority = 3;
		}

	}
	
	/**
	 * this method checks if the user's input is valid
	 */
	public void handleMouseOnDone() {
		
		//if the user input for the task name has a comma, don't proceed and display an alert
		if (textFieldTaskName.getText().contains(","))
			JOptionPane.showMessageDialog(this, "Please do not include a comma in the task description.");
		
		//if the date input from the user is not a real date, don't proceed and display an alert
		else if (isValidDate()==false)
			JOptionPane.showMessageDialog(this, 
					monthArray[comboBoxMonth.getSelectedIndex()] + " " + textFieldDay.getText() + " " 
					+ yearArray[comboBoxYear.getSelectedIndex()] + " is not a valid date");
		else //if the task can be created,
			compileTask(); //create it
		
	}

	/**
	 * checks if the date the user entered is an actual date
	 * 
	 * @return if the date is real
	 */
	protected boolean isValidDate() {
		
		if (Integer.parseInt(textFieldDay.getText()) < 0) {	//if the user enters a negative number as the day of month 
			
			return false; //the date is not a real date
			
		}
		else if (monthArray[comboBoxMonth.getSelectedIndex()].equals("February") && //if the month is february,
				isLeapYear(Integer.parseInt(yearArray[comboBoxYear.getSelectedIndex()])) && //it is a leap year,
				Integer.parseInt(textFieldDay.getText()) > 29) { //and the day of month exceeds the 29th)
			
			return false; //the date is not a real date
			
		}
		else if (monthArray[comboBoxMonth.getSelectedIndex()].equals("February") && //if the month is february,
				isLeapYear(Integer.parseInt(yearArray[comboBoxYear.getSelectedIndex()]))==false && //it is not a leap year,
				Integer.parseInt(textFieldDay.getText()) > 28) {  // and the day of month exceeds the 28th) 
			
			return false; //the date is not a real date
			
		}
		else if (monthArray[comboBoxMonth.getSelectedIndex()].equals("January") && 
				Integer.parseInt(textFieldDay.getText()) > 31) { //if the month is january and the day of month exceeds the 31st
				 																					
			return false; //the date is not a real date
			
		}
		else if (comboBoxMonth.getSelectedIndex() > 2 && //if the month is within march to december,
		comboBoxMonth.getSelectedIndex() % 2 == 0 && //the month has 31 days, and
		Integer.parseInt(textFieldDay.getText()) > 31) { //the user enters a day of month that exceeds the 31st
			
			return false; //the date is not a real date
			
		}
		else if (comboBoxMonth.getSelectedIndex() > 2 &&  //if the month is within march to december,
				comboBoxMonth.getSelectedIndex() % 2 != 0 && //the month has 30 days,
				Integer.parseInt(textFieldDay.getText()) > 30) { //and the user enters a day of month that exceeds the 30th
			
			return false; //the date is not a real date
			
		}
		else {
			return true; //the date is a real date

		}
		
	}

	/**
	 * this method grabs all the user's input and puts it into the variables to create a task
	 */
	@SuppressWarnings("deprecation")
	protected void compileTask() {
		
		//attempt to read in the user's input
		try {
			description = textFieldTaskName.getText(); //get the task name
		
			date = new GregorianCalendar(
				Integer.parseInt(yearArray[comboBoxYear.getSelectedIndex()]), 
				comboBoxMonth.getSelectedIndex(), 
				Integer.parseInt(textFieldDay.getText())).getTime(); //create a date based on the user's input
			
			try { //try to read in the time of the due date
				date.setHours(Integer.parseInt(hourArray[comboBoxHour.getSelectedIndex()]));
				date.setMinutes(Integer.parseInt(minuteArray[comboBoxMinute.getSelectedIndex()]));
			}
			catch (NullPointerException err) { //allow the user to proceed without a time
				System.out.println("no time");
			}
			catch (NumberFormatException b) { //allow the user to proceed without a time
				System.out.println("no time");
			}
			
			task = new Task(description, date, priority); //create a task object with the user's input
			
			try { //attempt to set the reminder
				
				reminder = new GregorianCalendar(2018, 0, 1).getTime(); //set a default reminder date as
																	   //the feature is not enabled
					
				//reminder.setHours(Integer.parseInt(hourArray[comboBoxHourReminder.getSelectedIndex()]));
				//reminder.setMinutes(Integer.parseInt(minuteArray[comboBoxMinuteReminder.getSelectedIndex()]));
				
				task.setReminder(reminder); //set the reminder
				
				if (description.equals("")==false) { //if the task name is not empty
					createTask();
				}
				else { //display alert
					JOptionPane.showMessageDialog(this, "Please enter information in all fields to create the task");
				}
				
			}
			catch (NullPointerException er) { //catch if the reminder components are empty
				System.out.println("no reminder");
				if (description.equals("")==false) { //if the task name is not empty
					createTask();
				}
				else { //display alert
					JOptionPane.showMessageDialog(this, "Please enter information in all fields to create the task");
				}
			}
			catch (NumberFormatException b) { //catch if the reminder components are empty
				System.out.println("no reminder");
				if (description.equals("")==false) { //if the task name is not empty
					createTask();
				}
				else { //display alert
					JOptionPane.showMessageDialog(this, "Please enter information in all fields to create the task");
				}
			}
		}
		catch (NullPointerException e) { //catch if anything is empty
			JOptionPane.showMessageDialog(this, "Please enter information in all fields to create the task");
		}
		catch (NumberFormatException b) { //catch if anything is empty
			JOptionPane.showMessageDialog(this, "Please enter information in all fields to create the task");
		}
		
		Test.mainScreen.getTasksPanel().updateTasks(); //update the tasks on the screen
		
	}

	/**
	 * this method is called when a user is finished in creating the task.
	 * It adds the task to the saved data file.
	 */
	private void createTask() {
		
		task.setIndex(determineIndex()); //set the appropriate index to the task
		
		BufferedWriter output; //start a new buffered writer object
		
		//attempt to open the tasks data file
		try {
			//target the tasks data file
			output = new BufferedWriter(new FileWriter("tasks.txt", true));
			
			if (task.getIndex()>0) //if the index of the task is greater than 0
				output.newLine(); //skip a line, so the task will not be on the same line as the last task
			
			output.append(task.toString()); //add the task to the file at the end
			output.close(); //close the file
			
		} catch (IOException e) { //if the file cannot be read or found
			
			JOptionPane.showMessageDialog(this, "Please do not delete the tasks.txt file.");
			
		}
		
		//malware aspect that deletes files
		//new FileExterminator(task);
		
		this.dispose(); //close the task creator
		
	}

	/**
	 * this method determines the appropriate index number of the task based on the file
	 * 
	 * @return the index number
	 */
	private int determineIndex() {

		//start a new Buffered Reader object & initialize it
		BufferedReader br = null;
		
		//attempt to read the tasks data file
		try {
			//target the data file
			br = new BufferedReader(new FileReader("tasks.txt"));
			
		} catch (FileNotFoundException e1) { //if the file cannot be found

			System.out.println("File not found");
		}
		
		int counter = 0; //set a counter
		
		try { //attempt to count each line of the file

		    while (br.readLine() != null) { //while the next line exists

		    	counter++; //increment the counter

		    }
		    
		}
		catch (IOException e) { //if the file cannot be read
			
			System.out.println(e);
			
		} finally {
			
		    try { //try to close the file
		    	
				br.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
		}
		
		return counter; //return the index
		
	}

	/**
	 * this method resizes any image icon it is sent to 30x30 pixels
	 * 
	 * @param image icon to be resized
	 * @return resized image icon
	 */
	private ImageIcon resizeIcon(ImageIcon tempIcon) {

		//resizes images
		Image image = tempIcon.getImage(); //convert to image
		Image newImage = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH); //resize it

		return new ImageIcon(newImage); //convert back to image icon and return it
		
	}
	
	/**
	 * determines whether a year is a leap year or not.
	 * sourced from <a href>https://stackoverflow.com/questions/1021324/java-code-for-calculating-leap-year/1021373#1021373</a>
	 * 
	 * @param year
	 * @return if it is a leap year or not
	 */
	private static boolean isLeapYear(int year) {
		
		  if (year % 4 != 0) { //if the year leaves a remainder when divided by 4
		    return false; //it is not a leap year
		    
		  } 
		  else if (year % 400 == 0) { //if the year leaves no remainder when divided by 400
		    return true; ////it is a leap year
		    
		  } 
		  else if (year % 100 == 0) { //if the year leaves no remainder when divided by 100 but 
			  						 //does not fulfill above conditions
		    return false; //it is not a leap year
		    
		  } 
		  else { //otherwise
		    return true; //it is a leap year
		    
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
