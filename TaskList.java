import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.AbstractBorder; 
import java.awt.*; 
import java.awt.geom.Area; 

/**
 * 
 * @author aalea
 * 
 * This is a JPanel that extracts all tasks 
 * between the date range in the parameters and 
 * stores them in an arraylist. Then, it formats 
 * itself to display these tasks.
 * All the buttons listen for mouse events.
 *
 */
public class TaskList extends JPanel implements MouseListener {

	/**
	 * this value holds the start of the date range for tasks the program will search for
	 */
	private Date startDate;
	
	/**
	 * this value holds the end of the date range for tasks the program will search for
	 */
	private Date endDate;
	
	/**
	 * this arraylist holds the tasks the panel will display
	 */
	private ArrayList<Task> tasks;
	
	/**
	 * this is the arraylist of all the buttons representing checkboxes for the tasks
	 */
	private ArrayList<JButton> taskCheckBoxes;
	
	/**
	 * this is the arraylist of all the labels representing descriptions for the tasks
	 */
	private ArrayList<JLabel> taskDescriptions;
	
	/**
	 * this is the arraylist of all the labels that will hold the priority flag icon for each task 
	 */
	private ArrayList<JLabel> taskPriorities;
	
	/**
	 * this is the arraylist of all the edit buttons for each task
	 */
	private ArrayList<JButton> editTaskBtn;
	
	/**
	 * this is an array of the image icons of the different priorities
	 */
	final ImageIcon[] PRIORITY_ICONS;

	/**
	 * constructor method which fills in the priority image icon array,
	 * sets the size and backup colour of the panel, reads in the font and 
	 * sets all the fields up
	 * 
	 * @param startDate the beginning of the date range of the tasks to search for
	 * @param endDate the end of the date range of the tasks to search for
	 */
	public TaskList(Date startDate, Date endDate) {
		
		//fills in the array with the different flag images
		PRIORITY_ICONS = new ImageIcon[] {
				resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/whiteFlag.png")), 20, 20),
				resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/yellowFlag.png")), 20, 20),
				resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/orangeFlag.png")), 20, 20),
				resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/redFlag.png")), 20, 20)};
		
		this.setBackground(new Color(204, 255, 255)); //set backup colour
		this.setBounds(177, 11, 343, 404); //set bounds
		
		//attempt to read in external font file
		try {
			//register font on local graphics environment
		     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, 
		    		 new File(MainScreen.class.getResource("Raleway-Regular.ttf").toString().replace("%20", " ").substring(6))));
		     
		     
		} catch (IOException e) { //if font file cannot be found or read in
			
			System.out.println("Can't read Raleway");
			
		} catch (FontFormatException e) { //if there is an issue with the font itself
			
			System.out.println("Can't read Raleway");
			
		}
		
		//1. Grab the start date from the parameters and store it in the object
		this.startDate = startDate;
		//2. Grab the end date from the parameters and store it in the object
		this.endDate = endDate;
		//3. Setup the panel
			//3.1. Set the border so it doesn't appear
		setBorder(null);
			//3.2. Set the layout to null, for absolute positioning
		setLayout(null);
			//3.3. Initialize the ArrayList of checkboxes for each task
		taskCheckBoxes = new ArrayList<JButton>();
			//3.4. Initialize the ArrayList of descriptions for each task
		taskDescriptions = new ArrayList<JLabel>();
			//3.5. Initialize the ArrayList of priority icons for each task
		taskPriorities = new ArrayList<JLabel>();
			//3.6. Initialize the ArrayList of edit buttons for each task
		editTaskBtn = new ArrayList<JButton>();
		//initialize array of task objects
		tasks = new ArrayList<Task>();
			//3.7. Calls a method that that searches and stores all the tasks within the date range
		grabTasks();
			//3.8. Call a method that does the more in-depth setup of the panel
		setupComponents();
		
	}
	
	/**
	 * this method searches for all tasks within the given date range
	 * and stores it in an arraylist
	 */
	void grabTasks() {

		//1. Open saved file of tasks
			//1.1. Read line
			//1.2. Divide line into sections using the commas
			//1.3. Save first, second and third section
			//1.4. If date in fourth section is greater than or equal to start date and less than or equal to end date
				//1.4.1. Create a new task
					//1.4.1.1. Pass off third section as parameter for task description
					//1.4.1.2. Pass off fourth section as parameter for task date
					//1.4.1.3. Save fifth section
					//1.4.1.4. Set task reminder as fifth section
					//1.4.1.5. Save sixth section
					//1.4.1.6. Set priority as sixth section
					//1.4.1.7. Set first section as index
				//1.4.2. Add this task to the arraylist
		
		int index = 0; //initialize index of task
		String description = ""; //initialize description of task
		Date date = new Date(0); //initialize due date of task
		Date reminder = new Date(0); //initialize reminder date of task
		int priority = 0; //initialize priority of task
		
		//empty of the tasks array if this method was called before
		for (int i = 0; i < tasks.size(); i++)
			tasks.remove(0); //remove each element in arraylist
		
		try { //attempt to read in tasks
			
			Scanner input = new Scanner (new File("tasks.txt")); //open a scanner object to read the tasks data file
			input.useDelimiter(","); //separate sections by commas

			//keep going until the end of the tasks data file
			while (input.hasNext()) {
				
				index = Integer.parseInt(input.next().trim()); //read index
				description = input.next(); //read description
				date = new Date(input.nextLong()); //read due date
				reminder = new Date(input.nextLong()); //read reminder date
				priority = input.nextInt(); //read priority
				
				//if the due date is within the date range of this task list
				if (date.getTime() >= startDate.getTime() && date.getTime() <= endDate.getTime()) {
					
					Task tempTask = new Task(description, date, priority); //create a task object
					tempTask.setIndex(index); //set the index
					tempTask.setReminder(reminder); //set the reminder
				
					tasks.add(tempTask); //add the task to the array of tasks
					
				}
				
			}
			input.close(); //close the file
		}
		catch (FileNotFoundException e) { //file is not in directory
			JOptionPane.showMessageDialog(null, "Please do not delete the tasks.txt file you may "
					+ "find in the same folder as this program, it saves all your tasks.");
		}
		
	}

	/**
	 * this method does a more in-depth setup of the GUI components on the panel
	 */
	private void setupComponents() {
		//for loop that iterates through the length of the tasks within the date range
		for (int i = 0; i < tasks.size(); i++) {
			//1. Create a temporary checkbox button
			JButton tempButton = new JButton();
				//1.1. Set the background to white
			tempButton.setBackground(Color.WHITE);
				//1.2. Set the bounds 
			tempButton.setBounds(10, 11 + (37*i), 26, 26);
				//1.4. Set the border colour
			tempButton.setBorder(new LineBorder(Color.BLACK));
				//1.5. Add an mouse listener to the button
			tempButton.addMouseListener(this);
			//2. Add the temporary checkbox button to an arraylist
			taskCheckBoxes.add(tempButton);
			//3. Add the checkbox button in the arraylist to the panel
			add(taskCheckBoxes.get(i));
			//4. Create a temporary label for the task description
				//4.1. Set the text as the description of the current task
			JLabel tempLblTask = new JLabel(tasks.get(i).getDescription());
			//make it so that the due date appears when the user hovers over task
			tempLblTask.setToolTipText("Due date: " + tasks.get(i).getDate().toString());
				//4.2. Set the bounds of the label
			tempLblTask.setBounds(50, 11 + (37*i), 217, 26);
			//set font of the label
			tempLblTask.setFont(new Font("Raleway", Font.PLAIN, 14));
			//5. Add the temporary label to an arraylist
			taskDescriptions.add(tempLblTask);
			//6. Add the label in the arraylist to the panel
			add(taskDescriptions.get(i));
			//7. Create a temporary label for the priority icon
				//7.1. Set the label's icon to the proper priority icon
			JLabel tempLblPriority = new JLabel(PRIORITY_ICONS[tasks.get(i).getPriority()]);
				//7.2. Set the bounds of the label
			tempLblPriority.setBounds(268, 11 + (37*i), 24, 26);
			//8. Add the temporary label to an arraylist
			taskPriorities.add(tempLblPriority);
			//9. Add the label in the arraylist to the panel
			add(taskPriorities.get(i));
			//10. Create a temporary edit button
				//10.1. Set the text to "..."
			JButton tempEditBtn = new JButton("...");
				//10.2. Set the bounds of the button
			tempEditBtn.setBounds(294, 11 + (37*i), 24, 26);
				//10.3. Add a mouse listener to the button
			tempEditBtn.addMouseListener(this);
			//11. Add the temporary button to an arraylist
			editTaskBtn.add(tempEditBtn);
			//12. Add the button in the arraylist to the panel
			add(editTaskBtn.get(i));
		}
		
		JLabel background; //the label which holds the background image
		
		if (taskCheckBoxes.size()*37 > 404) { //if the number of tasks on the screen is too large to 
											 //display them all, a scroll bar will appear. the height of the background image
											//will change dynamically as the number of tasks get larger
			//set background image to an image icon of appropriate size, based on the number of tasks on the screen
			background = new JLabel(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/tasksBackground.png")), 
					343, (taskCheckBoxes.size()*37) + 5));
			//set background image to appropriate bounds, based on the number of tasks on the screen
			background.setBounds(0, 0, 343, (taskCheckBoxes.size()*37) + 5);
		}
		else { //if the number of tasks on the panel does not exceed the height of the frame,
			  //the tasks panel will have a default size, so the image in the label will be set to this default size
			//set background image to an image icon of appropriate size
			background = new JLabel(resizeIcon(new ImageIcon(TaskCreator.class.getResource("/images/tasksBackground.png")), 343, 404));
			//set background image to appropriate bounds
			background.setBounds(0, 0, 343, 404);
		}
		
		add(background); //add the background image label to the panel
		
		this.setPreferredSize(new Dimension(343, (taskCheckBoxes.size()*37) + 5)); //set the preferred size to let the scroll
																				  //pane know if the scroll bar needs to appear or not
		//refresh the panel
		revalidate();
		repaint();
		
		//make the panel visible
		setVisible(true);
		
	}

	public ArrayList<JButton> getTaskCheckBoxes() {
		return taskCheckBoxes;
	}

	public void setTaskCheckBoxes(ArrayList<JButton> taskCheckBoxes) {
		this.taskCheckBoxes = taskCheckBoxes;
	}

	public ArrayList<JButton> getEditTaskBtn() {
		return editTaskBtn;
	}

	public void setEditTaskBtn(ArrayList<JButton> editTaskBtn) {
		this.editTaskBtn = editTaskBtn;
	}

	/**
	 * this method updates the tasks being displayed
	 */
	public void updateTasks() {
		
		//1. Remove all components from the panel
		
		int size = taskCheckBoxes.size(); //capture the number of tasks on the screen
		
		//iterate for each task
		for (int i = 0; i < size; i++) {
			//1.1. Clear the arraylist holding the checkbox buttons
			taskCheckBoxes.remove(0);
			//1.2. Clear the arraylist holding the task description labels
			taskDescriptions.remove(0);
			//1.3. Clear the arraylist holding the priority icon labels
			taskPriorities.remove(0);
			//1.4. Clear the arraylist holding the edit buttons
			editTaskBtn.remove(0);
		}
			//1.5. Finally, clear the panel
		this.removeAll(); //remove all components from the panel
		
		//refresh the panel
		this.repaint();
		this.revalidate();
		
		//2. Update the arraylist of tasks by calling the method that grabs the tasks
		grabTasks();
		//3. Call the method that does the in-depth setup of the components
		setupComponents();
	}

	/**
	 * this method handles the event in which a task is checked off
	 * 
	 * @param index of the task
	 */
	protected void taskChecked(int index) {
		
		//Search for matching task object
			//Find the task in the arraylist of tasks that has a location matching the index within the parameters
		for (int i = 0; i < tasks.size(); i++) {
			//2.2. Save that task
			if (tasks.get(i).getIndex()==index) {
		//3. Discard the task by calling the method within the task object
				//new Mailman(tasks.get(i));
				tasks.get(i).discard(); //tell the task to get rid of itself
				tasks.remove(i); //remove the task from the arraylist of tasks
				break; //exit the loop
			}
		}
		//4. Call the method that updates the panel's tasks
		updateTasks();
	}

	/**
	 * this method triggers the editing of a task, specified by the index parameter
	 * @param index the location of the task
	 */
	private void editTask(int index) {
		
		//1. Search for matching task object
			//1.1. Find the task in the arraylist of tasks that has a location matching the index within the parameters
		for (int i = 0; i < tasks.size(); i++) {
			
			if (tasks.get(i).getIndex()==index) { //if the index matches the index of the current task
		//2. Open an instance of task editor
			//2.1. Send task object as parameter
				new TaskEditor(tasks.get(i));
			}
			
		}
		//3. Call the method that updates the tasks being displayed
		updateTasks();
	}

	/**
	 * this method controls the events that happens when a button is clicked
	 * 
	 */
	@Override
	public void mousePressed(MouseEvent e) {
				//Detects which component was clicked
		
		if (editTaskBtn.contains(e.getSource())) { //if an edit task button was clicked
			//Get the index
			///Iterate through the arraylist holding the edit buttons
			for (int i = 0; i < editTaskBtn.size(); i++) {
				
				//If the component matches the current component in the loop
				if (e.getSource()==editTaskBtn.get(i)) {

					//Call the method that handles the editing of tasks
					//Send the index of the component as a parameter
					editTask(i);
					break; //exit the loop
				}	
			}
		}
				//If the component is a checkbox
		else if (taskCheckBoxes.contains(e.getSource())) {
			
			//Get the index
			//Iterate through the arraylist holding the checkbox buttons
			for (int i = 0; i < taskCheckBoxes.size(); i++) {
				
				//If the component matches the current component in the loop
				if (e.getSource()==taskCheckBoxes.get(i)) {

					//Call the method that handles the editing of tasks
					//Send the index of the component as a parameter
					taskChecked(i);
					break; //exit the loop
				}
			}
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
	 * this method resizes a given image icon
	 * 
	 * @param the image icon to be resized
	 * @param width of the resized image
	 * @param height of the resized image
	 * @return the resized image
	 */
	private ImageIcon resizeIcon(ImageIcon tempIcon, int width, int height) {

		Image image = tempIcon.getImage(); //convert to image
		Image newImage = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH); //resize it

		return new ImageIcon(newImage); //convert back to imageicon and send it
		
	}
}
