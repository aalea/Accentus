import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;

import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.ScrollPane;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author aalea
 * 
 * Contains the prompts to view tasks for today, 
 * next 7 days, and all, open the settings screen, 
 * and add a task. Uses mouse listeners for button clicks
 * and an action listener for the animation
 *
 */
public class MainScreen extends JFrame implements MouseListener, ActionListener {
	
	/**
	 * this holds the background colour of the main screen
	 */
	private Color backgroundColour;
	
	/**
	 * this is the label that holds the text of the today button
	 */
	private JLabel lblTodayText;
	
	/**
	 * this is the label that holds the bubble image for the today button
	 */
	private JLabel lblToday;
	
	/**
	 * this is the label that holds the text of the next 7 days button
	 */
	private JLabel lblNext7DaysText;
	
	/**
	 * this is the label that holds the bubble image for the next 7 days button
	 */
	private JLabel lblNext7Days;
	
	/**
	 * this is the label that holds the text of the all button
	 */
	private JLabel lblAllText;
	
	/**
	 * this is the label that holds the bubble image for the all button
	 */
	private JLabel lblAll;
	
	/**
	 * this is the label that holds the text of the settings button
	 */
	private JLabel lblSettingsText;
	
	/**
	 * this is the label that holds the bubble image for the settings button
	 */
	private JLabel lblSettings;
	
	/**
	 * this is the label that holds the text of the add task button
	 */
	private JLabel lblAddTaskText;
	
	/**
	 * this is the label that holds the bubble image for the add task button
	 */
	private JLabel lblAddTask;
	
	/**
	 * the panel that holds the tasks, it will be switched out as the user
	 * clicks the today, next 7 days, and all buttons
	 */
	private TaskList tasksPanel;
	
	/**
	 * the formatting of the time which is used in task creator and changed in settings screen
	 */
	private int timeFormat;
	
	/**
	 * the settings screen
	 */
	private SettingsScreen settings;
	
	/**
	 * timer to control animations of bubbles
	 */
	private Timer animation;
	
	/**
	 * determines whether the animation should happen for the bubble or not.
	 * if set to "bubble", the bubble will move.
	 * if set to "splash", the bubble will not move.
	 * 
	 * index representations
	 * 0 - lblToday
	   1 - lblNext7Days
	   2 - lblAll
	   3 - lblAddTask
	   4 - lblSettings
	 */
	private String[] animationStatus;
	
	/**
	 * determines whether each bubble should move up or down.
	 * if greater than or equal to 0 or less than or equal to 3,
	 * it will move up.
	 * if greater than or equal to 4 or less than or equal to 7,
	 * it will move down.
	 * if it equals 8, the value will be reset to 0.
	 * 
	 * index representations
	 * 0 - lblToday
	   1 - lblNext7Days
	   2 - lblAll
	   3 - lblSettings
	   4 - lblAddTask
	 */
	private int[] animationStep;
	
	/**
	 * an arraylist of the button images on the screen, used for the animation
	 */
	private ArrayList<JLabel> bubbles;
	
	/**
	 * an arraylist of the button text on the screen, used for the animation
	 */
	private ArrayList<JLabel> bubbleText;
	
	/**
	 * the scroll pane which contains the tasks panel. if the number of tasks on the panel
	 * are too much for the screen, a scroll bar will appear
	 */
	private JScrollPane sp;
	
	/**
	 * sets up the gui.
	 * 
	 * font configuration credit to <a href>https://stackoverflow.com/questions/5652344/how-can-i-use-a-custom-font-in-java</a>
	 * scroll pane configuration to <a href>http://esus.com/adding-jpanel-null-layout-jscrollpane/</a>
	 */
	public MainScreen() {

		this.timeFormat = 1; //initialize the time format
		
		setSize(654, 464); //set appropriate size for jframe, do not change
		setResizable(false); //makes it so that end user cannot change size
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes it so that when the user 
													   //clicks 'X', the program will stop
		
		bubbleText = new ArrayList<JLabel>(); //initializes the arraylist of text labels for the buttons
		bubbles = new ArrayList<JLabel>(); //initializes the arraylist of image labels for the buttons
		
		//6.1. Call the settings screen panel
		settings = new SettingsScreen();
		settings.setVisible(false); //the settings screen panel will intially be invisible
								   //it will only be visible once the user clicks on the button
			//6.1.1. Just add it to the panels
		getContentPane().add(settings);
		
			//1.1. Set the background colour of the content pane to a light blue
		this.backgroundColour = new Color(204, 255, 255); //create colour
		getContentPane().setBackground(backgroundColour); //set the colour
		
			//1.2. Set the icon displayed on the top left of the frame to a checkmark logo
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainScreen.class.getResource("/images/checkmark.png")));
			//1.3. Set the title of the frame
		setTitle("Accentus - Your Tasks");
			//1.4. Set the content pane to an absolute layout
		getContentPane().setLayout(null); //so that components can be manually positioned
			//1.5. Setup the 'Today' button
				//1.5.1. Create the text aspect
					//1.5.1.1. Create a new label and set the text to "Today"
		lblTodayText = new JLabel("Today");
					//1.5.1.2. Center the text
		lblTodayText.setHorizontalAlignment(SwingConstants.CENTER);
					//1.5.1.4. Set the bounds of the label on the screen
		lblTodayText.setBounds(72, 50, 103, 78);
					//1.5.1.5. Add a mouse listener to this label
		lblTodayText.addMouseListener(this);
					//1.5.1.6. Add the label to the screen
		getContentPane().add(lblTodayText);
		
		bubbleText.add(lblTodayText); //add to array of text labels for bubble buttons
		
				//1.5.2. Create the shape aspect
					//1.5.2.1. Create a new label
		lblToday = new JLabel();
					//1.5.2.2. Set the icon of the label to an image of a bubble
						//1.5.2.2.1. Call a method that resizes the image
						//1.5.2.2.2. Set the icon to the resized image
		lblToday.setIcon(resizeIcon(new ImageIcon(MainScreen.class.getResource("/images/bubble.png"))));
					//1.5.2.3. Set the bounds of the shape
		lblToday.setBounds(72, 37, 103, 103);
					//1.5.2.4. Add the shape to the screen
		getContentPane().add(lblToday);
		
		bubbles.add(lblToday); //add to array of image labels for bubble buttons
		
			//1.6. Setup the 'Next 7 Days' button
				//1.6.1. Create the text aspect
					//1.6.1.1. Create a new label and set the text to "Next 7 Days"
		lblNext7DaysText = new JLabel("Next 7 Days");
					//1.6.1.2. Center the text
		lblNext7DaysText.setHorizontalAlignment(SwingConstants.CENTER);
					//1.6.1.4. Set the bounds of the label on the screen
		lblNext7DaysText.setBounds(61, 164, 103, 103);
					//1.6.1.5. Add a mouse listener to this label
		lblNext7DaysText.addMouseListener(this);
					//1.6.1.6. Add the label to the screen
		getContentPane().add(lblNext7DaysText);
		
		bubbleText.add(lblNext7DaysText); //add to array of text labels for bubble buttons
		
				//1.6.2. Create the shape aspect
					//1.6.2.1. Create a new label
		lblNext7Days = new JLabel();
					//1.6.2.2. Set the icon of the label to an image of a bubble
						//1.6.2.2.1. Call a method that resizes the image
						//1.6.2.2.2. Set the icon to the resized image
		lblNext7Days.setIcon(resizeIcon(new ImageIcon(MainScreen.class.getResource("/images/bubble.png"))));
					//1.6.2.3. Set the bounds of the shape
		lblNext7Days.setBounds(61, 164, 103, 103);
					//1.6.2.4. Add the shape to the screen
		getContentPane().add(lblNext7Days);
		
		bubbles.add(lblNext7Days); //add to array of image labels for bubble buttons
		
		//1.7. Setup the 'All' button
			//1.7.1. Create the text aspect
				//1.7.1.1. Create a new label and set the text to "All"		
		lblAllText = new JLabel("All");
				//1.7.1.2. Center the text
		lblAllText.setHorizontalAlignment(SwingConstants.CENTER);
				//1.7.1.4. Set the bounds of the label on the screen
		lblAllText.setBounds(71, 302, 103, 103);
				//1.7.1.5. Add a mouse listener to this label
		lblAllText.addMouseListener(this);
				//1.7.1.6. Add the label to the screen
		getContentPane().add(lblAllText);
		
		bubbleText.add(lblAllText); //add to array of text for bubbles
		
			//1.7.2. Create the shape aspect
				//1.7.2.1. Create a new label
		lblAll = new JLabel();
				//1.7.2.2. Set the icon of the label to an image of a bubble
					//1.7.2.2.1. Call a method that resizes the image
					//1.7.2.2.2. Set the icon to the resized image
		lblAll.setIcon(resizeIcon(new ImageIcon(MainScreen.class.getResource("/images/bubble.png"))));
				//1.7.2.3. Set the bounds of the shape
		lblAll.setBounds(71, 302, 103, 103);
				//1.7.2.4. Add the shape to the screen
		getContentPane().add(lblAll);
		
		bubbles.add(lblAll); ///add to array of image labels for bubble buttons
		
		//1.8. Setup the 'Settings' button
			//1.8.1. Create the text aspect
				//1.8.1.1. Create a new JLabel and set the text to "Settings"
		lblSettingsText = new JLabel("Settings");
				//1.8.1.2. Center the text
		lblSettingsText.setHorizontalAlignment(SwingConstants.CENTER);
				//1.8.1.4. Set the bounds of the label on the screen
		lblSettingsText.setBounds(542, 331, 84, 78);
				//1.8.1.5. Add a mouse listener to this label
		lblSettingsText.addMouseListener(this);
				//1.8.1.6. Add the label to the screen
		getContentPane().add(lblSettingsText);
		
		bubbleText.add(lblSettingsText); //add to array of text labels for bubble buttons
		
			//1.8.2. Create the shape aspect
				//1.8.2.1. Create a new label
		lblSettings = new JLabel();
				//1.8.2.2. Set the icon of the label to an image of a bubble
					//1.8.2.2.1. Call a method that resizes the image
					//1.8.2.2.2. Set the icon to the resized image
		lblSettings.setIcon(resizeIcon(new ImageIcon(MainScreen.class.getResource("/images/bubble.png"))));
				//1.8.2.3. Set the bounds of the shape
		lblSettings.setBounds(530, 321, 103, 103);
				//1.8.2.4. Add the shape to the screen
		getContentPane().add(lblSettings);
		
		bubbles.add(lblSettings); //add to array of image labels for bubble buttons
		
		//1.9. Setup the 'Add Task' button
			//1.9.1. Create the text aspect
				//1.9.1.1. Create a new label and set the text to "Add Task"	
		lblAddTaskText = new JLabel("Add Task");
				//1.9.1.2. Center the text
		lblAddTaskText.setHorizontalAlignment(SwingConstants.CENTER);
				//1.9.1.4. Set the bounds of the label on the screen
		lblAddTaskText.setBounds(542, 76, 84, 78);
				//1.9.1.5. Add a mouse listener to this label
		lblAddTaskText.addMouseListener(this);
				//1.9.1.6. Add the label to the screen
		getContentPane().add(lblAddTaskText);
		
		bubbleText.add(lblAddTaskText); //add to array of text labels for bubble buttons
		
			//1.9.2. Create the shape aspect
				//1.9.2.1. Create a new label
		lblAddTask = new JLabel();
				//1.9.2.2. Set the icon of the label to an image of a bubble
					//1.9.2.2.1. Call a method that resizes the image
					//1.9.2.2.2. Set the icon to the resized image
		lblAddTask.setIcon(resizeIcon(new ImageIcon(MainScreen.class.getResource("/images/bubble.png"))));
				//1.9.2.3. Set the bounds of the shape
		lblAddTask.setBounds(530, 66, 103, 103);
				//1.9.2.4. Add the shape to the screen
		getContentPane().add(lblAddTask);
		
		bubbles.add(lblAddTask); //add to array of image labels for bubble buttons
		
		//1.10. Add a panel for the tasks
			//1.10.1. Make all tasks appear initially
		tasksPanel = new TaskList(new Date(0),new Date(Long.MAX_VALUE));
		
		//call method that configures the scroll pane
	    refreshScrollPane();
	    
	    //attempt to read in external font
		try {
			//install font on local graphics environment
		     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		     //resources are read in as a url so it has to be converted to a string that will be parameters for a file
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, 
		    		 new File(MainScreen.class.getResource("Raleway-Regular.ttf").toString().replace("%20", " ").substring(6))));
		     
		     
		} catch (IOException e) { //if file cannot be read in

			System.out.println(e);
			
		} catch (FontFormatException er) { //if font cannot be configured
			
			System.out.println(er);

		}
		
		setFonts(); //call method that sets the fonts in all the text labels
		
		this.setVisible(true); //make the frame visible
		
		//initialize the animation timer
		animation = new Timer(100, this);
		//sets the status of each button to the default "bubble", as all buttons are bubbles at the moment
		animationStatus = new String[] {"bubble","bubble","bubble","bubble","bubble"};
		//initializes the step of the animation for each button
		animationStep = new int[] {0,0,0,0,0};
		//start the animation timer
		animation.start();
		
	}

	/**
	 * this method is called whenever the scroll pane is updated.
	 * creating a new instance of the scroll pane will ensure that 
	 * there is only one view in the viewport in the scroll pane
	 */
	private void refreshScrollPane() {
		//create a new scroll pane with the tasks panel as the view, 
		//displays a vertical scroll bar if the number of tasks on the tasks panel go pass the length of the frame,
		//and never displays a horizontal scroll bar
		sp = new JScrollPane(tasksPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    sp.setBounds(177, 11, 343, 404); //set the bounds of the scroll bar
	    sp.setBorder(null); //get rid of the scroll pane's border
	    getContentPane().add(sp, BorderLayout.CENTER); //add the scroll pane to the frame
		
	}

	/**
	 * this method sets the fonts in all of the text labels to Raleway
	 */
	private void setFonts () {
		//set the font in the today label
		lblTodayText.setFont(new Font("Raleway", Font.PLAIN, 18));
		//set the font in the next 7 days label
		lblNext7DaysText.setFont(new Font("Raleway", Font.PLAIN, 18));
		//set the font in the all label
		lblAllText.setFont(new Font("Raleway", Font.PLAIN, 18));
		//set the font in the settings label
		lblSettingsText.setFont(new Font("Raleway", Font.PLAIN, 18));
		//set the font in the add task label
		lblAddTaskText.setFont(new Font("Raleway", Font.PLAIN, 18));
	}

	public TaskList getTasksPanel() {
		return tasksPanel;
	}

	public void setTasksPanel(TaskList tasksPanel) {
		this.tasksPanel = tasksPanel;
	}

	public int getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(int newTimeFormat) {
		timeFormat = newTimeFormat;
	}

	/**
	 * this method resizes any image icon it is sent
	 * @param the image icon that needs to be resized
	 * @return the resized image icon
	 */
	private Icon resizeIcon(ImageIcon tempIcon) {
		
		Image image = tempIcon.getImage(); //convert to image
		Image newImage = image.getScaledInstance(103, 103,  java.awt.Image.SCALE_SMOOTH); //resize it

		return new ImageIcon(newImage); //convert back to imageicon and sends it

	}

	public Color getBackgroundColour() {
		return backgroundColour;
	}

	public void setBackgroundColour(Color backgroundColour) {
		this.backgroundColour = backgroundColour;
		getContentPane().setBackground(backgroundColour);
	}

	/**
	 * this method controls the actions that occur when a button is clicked
	 * 
	 * @param the mouse event
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		//1. Detect the component the mouse clicked on
		//2. If the component is the 'Add Task' label
		if (e.getSource()==lblAddTaskText) {
			//make the button image turn into a splash
			lblAddTask.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/splash.png"))));
			animationStatus[4] = "splash"; //set the status of the button
			//2.1. Start a new instance of the add task class
			new TaskCreator();
		}
		//3. If the component is the 'All' label
		else if (e.getSource()==lblAllText) {
			//ensure that all buttons are set to the correct images and statuses
			//set add task button to a bubble
			lblAddTask.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/bubble.png")))); 
			animationStatus[4] = "bubble"; //update the status
			//set next 7 days button to a bubble
			lblNext7Days.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/bubble.png"))));
			animationStatus[1] = "bubble"; //update the status
			//set today button to a bubble
			lblToday.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/bubble.png"))));
			animationStatus[0] = "bubble"; //update the status
			
			//set all button to a splash, as it was the button clicked
			lblAll.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/splash.png"))));
			animationStatus[2] = "splash"; //update the status

			getContentPane().remove(sp); //remove the scroll pane, which contains the tasks panel
			
			//3.1. Call TaskList
				//3.1.1. Send a date value representing Jan 1 1970 as the start date as a parameter
				//3.1.2. Send a date value representing a large date as the end date as a parameter
			tasksPanel = new TaskList(new Date(0),new Date(Long.MAX_VALUE));
			
			refreshScrollPane(); //add back the scroll pane, which will now display the updated tasks panel

			//refresh the frame
			revalidate();
			repaint();
		}
		//4. If the component is the 'Next 7 Days' label
		else if (e.getSource()==lblNext7DaysText) {
			
			//ensure that all buttons are set to the correct images and statuses
			//set add task button to a bubble
			lblAddTask.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/bubble.png"))));
			animationStatus[4] = "bubble"; //update the status
			//set all button to a bubble
			lblAll.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/bubble.png"))));
			animationStatus[2] = "bubble"; //update the status
			//set today button to a bubble
			lblToday.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/bubble.png"))));
			animationStatus[0] = "bubble"; //update the status
			
			//set next 7 days button to a splash
			lblNext7Days.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/splash.png"))));
			animationStatus[1] = "splash"; //update the status
			
			//create a variable that represents today at 12AM
			Calendar startDate = Calendar.getInstance(); //get the current date and time
			startDate.set(Calendar.HOUR_OF_DAY, 0); //set the hour to 12AM
			startDate.set(Calendar.MINUTE, 0); //set the minute to 0
			startDate.set(Calendar.SECOND, 0); //set the second to 0
			startDate.set(Calendar.MILLISECOND, 0); //set the millisecond to 0
			
			//create a variable that represents 7 days from now at 11:59PM
			Calendar endDate = Calendar.getInstance(); //get the current date and time
			endDate.add(Calendar.DAY_OF_MONTH, 7); //add 7 days to it
			endDate.set(Calendar.HOUR_OF_DAY, 23); //set the time to 11:00PM
			endDate.set(Calendar.MINUTE, 59); //set the minute to 59
			endDate.set(Calendar.SECOND, 59); //set the second to 59
			endDate.set(Calendar.MILLISECOND, 999); //set the millisecond to 999

			getContentPane().remove(sp); //remove the scroll pane, which contains the tasks panel
			
			//4.1. Call TaskList
				//4.1.1. Send a date value representing today as the start date as a parameter
				//4.1.2. Send a date value representing 7 days from now as the end date as a parameter
			tasksPanel = new TaskList(startDate.getTime(), endDate.getTime());
			
			refreshScrollPane(); //add back the scroll pane

			//refresh the frame
			revalidate();
			repaint();
			
		}
		//5. If the component is the 'Today' label
		else if (e.getSource()==lblTodayText) {
			
			//ensure that all buttons are set to the correct images and statuses
			//set add task button to a bubble
			lblAddTask.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/bubble.png"))));
			animationStatus[4] = "bubble"; //update the status
			//set next 7 days button to a bubble
			lblNext7Days.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/bubble.png"))));
			animationStatus[1] = "bubble"; //update the status
			//set all button to a bubble
			lblAll.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/bubble.png"))));
			animationStatus[2] = "bubble"; //update the status
			
			//set today button to a splash
			lblToday.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/splash.png"))));
			animationStatus[0] = "splash"; //update the status
			
			//create a variable that represents today at 12AM
			Calendar startDate = Calendar.getInstance();
			startDate.set(Calendar.HOUR_OF_DAY, 0); //set the hour to 12AM
			startDate.set(Calendar.MINUTE, 0); //set the minute to 0
			startDate.set(Calendar.SECOND, 0); //set the second to 0
			startDate.set(Calendar.MILLISECOND, 0); //set the millisecond
			
			//create a variable that represents today at 11:59PM
			Calendar endDate = Calendar.getInstance();
			endDate.set(Calendar.HOUR_OF_DAY, 23); //set the hour to 11PM
			endDate.set(Calendar.MINUTE, 59); //set the minute to 59
			endDate.set(Calendar.SECOND, 59); //set the second to 59
			endDate.set(Calendar.MILLISECOND, 999); //set the millisecond to 999
			
			getContentPane().remove(sp); //remove the scroll pane, which contains the tasks panel
			
			//5.1. Call TaskList
				//5.1.1. Send a date value representing today as the start date as a parameter
				//5.1.2. Send a date value representing today as the end date as a parameter
			tasksPanel = new TaskList(startDate.getTime(), endDate.getTime());
			
			refreshScrollPane(); //add back the scroll pane
			
			//refresh the frame
			revalidate();
			repaint();
			
		}
		//6. If the component is the 'Settings' label
		else if (e.getSource()==lblSettingsText) {
			
			//set the add task button to a bubble
			lblAddTask.setIcon(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/bubble.png"))));
			animationStatus[4] = "bubble"; //update the status
			
			//ensures that the edit task buttons on the tasks panel does not show up
			for (int i = 0; i < tasksPanel.getEditTaskBtn().size(); i++) {
				tasksPanel.getEditTaskBtn().get(i).setVisible(false); //makes each button invisible
			}
			//ensures that the checkbox buttons on the tasks panel does not show up
			for (int i = 0; i < tasksPanel.getTaskCheckBoxes().size(); i++) {
				tasksPanel.getTaskCheckBoxes().get(i).setVisible(false); //makes each button invisible
			}
			
			//makes the settings screen visible
			settings.setVisible(true);
			
			//refresh the frame
			revalidate();
			repaint();
			
		}
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * this method controls the actions when a user exits the settings screen
	 * 
	 * @param the format which the time will be displayed in
	 */
	public void getRidSettings(int timeFormat) {
		
		settings.setVisible(false); //makes the settings screen invisible
		this.timeFormat = timeFormat; //sets the time format

		//allows the edit task buttons on tasks panel to be visible again
		for (int i = 0; i < tasksPanel.getEditTaskBtn().size(); i++) {
			tasksPanel.getEditTaskBtn().get(i).setVisible(true); //makes each button visible
		}
		//allows the checkbox buttons on tasks panel to be visible again
		for (int i = 0; i < tasksPanel.getTaskCheckBoxes().size(); i++) {
			tasksPanel.getTaskCheckBoxes().get(i).setVisible(true); //makes each button visible
		}
		
		//refresh the frame
		revalidate();
		repaint();
		
	}

	/**
	 * this method is only utilized for the animation functions
	 * @param the event in which the animation timer goes off
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource()==animation) { //if the action event is the timer
			
			//iterate through all of the bubbles
			for (int i = 0; i < bubbles.size(); i++) {
				
				//if the current component is a bubble (not a splash)
				if (animationStatus[i].equals("bubble")) {
					//if the bubble needs to rise
					if (animationStep[i] >= 0 && animationStep[i] <= 3) {
						//increment the position counter
						animationStep[i]++;
						//move the bubble up
						bubbles.get(i).setBounds(bubbles.get(i).getX(), bubbles.get(i).getY() + 1, 103, 103);
						//move the bubble's text up
						bubbleText.get(i).setBounds(bubbleText.get(i).getX(), bubbleText.get(i).getY() + 1, 
								bubbleText.get(i).getWidth(), bubbleText.get(i).getHeight());

					}
					//if the bubble needs to descend
					else if (animationStep[i] > 3 && animationStep[i] <= 7) {
						//increment the position counter
						animationStep[i]++;
						////move the bubble down
						bubbles.get(i).setBounds(bubbles.get(i).getX(), bubbles.get(i).getY() - 1, 103, 103);
						//move the bubble's text down
						bubbleText.get(i).setBounds(bubbleText.get(i).getX(), bubbleText.get(i).getY() - 1, 
								bubbleText.get(i).getWidth(), bubbleText.get(i).getHeight());

					}
					//if the position counter needs to reset
					else if (animationStep[i]==8) {
						animationStep[i] = 0; //reset it
					}
					
				}
			}		
		}
	}
}
