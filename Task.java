import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * 
 * @author aalea
 * 
 * The literal task that application saves and shows 
 * the user. It stores the description and date. Optionally 
 * for the user, it stores a reminder date and priority.
 *
 */
public class Task {
	
	/**
	 * the description of the task
	 */
	private String description;
	
	/**
	 * the due date of the task
	 */
	private Date date;
	
	/**
	 * the priority of the task
	 * 
	 * 0 - no priority
	 * 1 - yellow flag
	 * 2 - orange flag
	 * 3 - red flag
	 */
	private int priority;
	
	/**
	 * the time the program will remind the user of this task
	 * (optional for user)
	 */
	private Date reminder;
	
	/**
	 * the index used to locate the task in the saved data file
	 */
	private int index;
	
	/**
	 * 
	 * @param description the description of the task
	 * @param date the due date of the task
	 * @param priority the priority level of the task
	 */
	public Task(String description, Date date, int priority) {
		//1. Set the description 
		this.description = description;
		//2. Set the due date
		this.date = date;
		//3. Set the priority level
		this.priority = priority;
		
	}
	
	/**
	 * this method removes this task from the saved data file
	 * @throws IOException 
	 */
	public void discard() {
		
		//created a buffered reader object
		BufferedReader br = null;
		
		//try to read the file
		try {
			//read in tasks data file
			br = new BufferedReader(new FileReader("tasks.txt"));
			
		} catch (FileNotFoundException e1) { //couldn't find the file

			System.out.println(e1);
			
		}
		
		ArrayList<String> taskFile = new ArrayList<String>(); //initialize an arraylist to hold the new tasks data file
		
		//try to collect each line
		try {
			
			//read the first line
		    String line = br.readLine();

		    //keep reading until no lines are left
		    while (line != null) {
		        
		        if (line.contains(index + "," + description)==false) //if the task is not the task being discarded
		        	taskFile.add(line); //add it to the array collecting the updated tasks
		        
		        line = br.readLine(); //read the line
		        
		    }
		    
		}
		catch (IOException e) { //if there is an error
			
			System.out.println(e);
			
		} finally {
			
		    try { //try to close the file
		    	
				br.close();
				
			} catch (IOException e) {
				
				System.out.println(e);
			}
		}
		
		//create a new printer writer object
		PrintWriter writer = null;
		
		try { //try to read in the tasks data file 
			
			writer = new PrintWriter("tasks.txt", "UTF-8"); //read in the file which is in UTF-8 format
			
		} catch (FileNotFoundException e) { //file not found

			System.out.println(e);
			
		} catch (UnsupportedEncodingException e) { //file cannot be read

			System.out.println(e);
			
		}
		for (int i = 0; i < taskFile.size()-1; i++) { //iterate through all the collected tasks
			
			writer.printf(reNumberIndex(taskFile.get(i), i) + "\n"); //write each task to the file and skip a line
			
		}
		if (taskFile.size()>0) //if only one task was collected
			writer.print(reNumberIndex(taskFile.get(taskFile.size()-1), taskFile.size()-1)); //write the task to the file
		
		writer.close(); //close the writer
		
	}
	
	/**
	 * this toString method formats the task so that it could easily be written directly to the data file
	 */
	@Override
	public String toString() {
		return index + "," + description + "," + date.getTime() + "," + reminder.getTime() + "," + priority + ",";

	}

	/**
	 * this method renumbers the index so it could properly be added to the data file
	 * 
	 * @param the task line
	 * @param the proper index of the task
	 */
	public String reNumberIndex(String line, int index) {
		
		//reformat the line so it has the proper index
		String newLine = (index + line.substring(Integer.toString(index).length()));
		
		return newLine; //return the line
	}

	public Date getReminder() {
		return reminder;
	}

	public void setReminder(Date reminder) {
		this.reminder = reminder;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
