import java.util.Calendar;

import javax.swing.JOptionPane;

/**
 * 
 * @author aalea
 * 
 * This is a JFrame that replicates task creator almost entirely, 
 * except it fills in the description with the description of the 
 * current task being edited.
 *
 */
public class TaskEditor extends TaskCreator {
	
	/**
	 * sets default serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * this is the task being edited
	 */
	private Task task;
	
	/**
	 * constructor which calls the super and fills in the components with
	 * data from the task
	 * 
	 * @param task being edited
	 */
	public TaskEditor(Task task) {
		
		//1. Calls the super class to set everything up
		super();
		//sets the task
		this.task = task;
		
		textFieldTaskName.setText(task.getDescription()); //set the description to the task's description
		comboBoxMonth.setSelectedIndex(task.getDate().getMonth()); //set the month to the task's month
		textFieldDay.setText(Integer.toString(task.getDate().getDate())); //set the day to the task's day
		comboBoxYear.setSelectedIndex(deriveYearIndex(task.getDate().getYear())); //set the year to the task's year
		comboBoxHour.setSelectedIndex(task.getDate().getHours()); //set the hour to the task's hour
		comboBoxMinute.setSelectedIndex(task.getDate().getMinutes()); //set the minute to the task's hour
		
		comboBoxMonthReminder.setSelectedIndex(task.getReminder().getMonth()); //set the month reminder to the task's reminder month
		textFieldDayReminder.setText(Integer.toString(task.getReminder().getDate())); //set the reminder day to the task's reminder day
		comboBoxYearReminder.setSelectedIndex(deriveYearIndex(task.getReminder().getYear())); //set the year reminder to the task's reminder year
		comboBoxHourReminder.setSelectedIndex(task.getReminder().getHours()); //set the hour reminder to the task's reminder hour
		comboBoxMinuteReminder.setSelectedIndex(task.getReminder().getMinutes()); //set the minute to the task's reminder minute
		
	}
	
	/**
	 * this method interprets the index of the year on the combo boxes
	 * @param year
	 * @return index
	 */
	public int deriveYearIndex(int index) {
		
		int newYear = index + 1900; //setup the current year
		
		for (int i = 0; i < yearArray.length; i++) { //iterate through years
			
			if (Integer.parseInt(yearArray[i])==newYear) { //if the year matches
				
				index = i; //grab the index
				break; //exit the loop
				
			}
				
		}
		
		return index; //return the index
	}
	
	/**
	 * this method, within this class, must edit the task instead a replicating itself
	 */
	@Override
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
			//call the method that handles the editing of tasks
			editTask();
		
	}
	
	/**
	 * this method is called when a user is finished in editing the task.
	 * It adds the new task info to the saved data file.
	 */
	private void editTask() {

		Test.mainScreen.getTasksPanel().taskChecked(task.getIndex()); //gets rid of task

		compileTask(); //creates it again with the updated values
		
	}
	
	

}
