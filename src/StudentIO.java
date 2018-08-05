import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

/** A utility class used for input and output of student objects, as well as checking and editing student information
 * @version 1.0.0
 * @author Tayef
 */
public class StudentIO {

	/**
	 * List of all the student objects
	 */
	private static ArrayList<Student> students;
	
	private static FileReader input;
	private static BufferedReader reader;
	
	private static String sparefileOne = "spare file day 1.csv";
	private static String sparefileTwo = "spare list day 2.csv";
	private static String logFile = "studentlog.csv";
	
	/**
	 * List of all the files being used by the program
	 */
	private static String[] files = new String[]{logFile,sparefileOne,sparefileTwo};
	
	private static FileWriter fileWriter;
	
	/**
	 * Reads in all students who have a spare according to the day and period 
	 * @param day The current school day number
	 * @param period The current period number
	 * @return An arrayList of all student objects with a spare during the period
	 */
	public static ArrayList<Student> read(int day, int period){
		try {
			int spare;
			String firstName;
			String lastName;
			int studentNo;
			
			//checks day
			if(day == 1 || day == 3){
				input = new FileReader(files[1]);
				if(day == 3){
					switch(period){
					case 3: period = 4;
					break;
					case 4: period = 3;
					break;
					default:;
					}
				}
			}
			else if(day == 2 || day == 4){
				input = new FileReader(files[2]);
				if(day == 4){
					switch(period){
					case 3: period = 4;
					break;
					case 4: period = 3;
					break;
					default:;
					}
				}
			}

			reader = new BufferedReader(input);
			
			String line;		
			line = reader.readLine();
			line = reader.readLine();
			StringTokenizer tokenizer = new StringTokenizer(line, ",");

			//reads in students
			students = new ArrayList<Student>();
				while(line!= null)
				{	 
					tokenizer = new StringTokenizer(line, ",");
					try{
	
						spare = Integer.parseInt(tokenizer.nextToken());
						if(spare == period)
						{
						firstName = tokenizer.nextToken();
						lastName = tokenizer.nextToken();
						studentNo = Integer.parseInt(tokenizer.nextToken());
						Student temp = new Student(spare,firstName,lastName,studentNo);
						students.add(temp);
						}
						
						line = reader.readLine();
					}catch(EOFException e){JOptionPane.showMessageDialog(null, "Could not search students, please make sure all files are present");}
				}
				reader.close();
				input.close();
				
		}catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not load students, please make sure all files are present");
		}catch(IndexOutOfBoundsException e){
			 e.printStackTrace();
			 JOptionPane.showMessageDialog(null, "Incorrect number of files being used at one, please use only one log, and two data files");
		}catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Data file error");
		}
		return students;
	}	
	/**
	 * Writes student information and check in time to the last line of log file
	 * @param student The student being checked in
	 */
	public static void checkIn(Student student){
		try {
			fileWriter = new FileWriter(files[0],true);			
			File file = new File(files[0]);
			//Writes in log time if log file exists
			if(file.isFile())
			{
				String line = student.toFullString();
				line+=(",");
				line+=(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a").format(new Date()));
				fileWriter.append(line);
				fileWriter.append("\n");
				fileWriter.close();
			}
		} 
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "File is currenly in use. Please close it to check in the student.");
			e.printStackTrace();
		}
	}
	/**
	 * Searches log file for last student check in and returns check in time
	 * @param number The student number of the student being checked in the log
	 * @return A string of the last time the student checked in
	 */
	public static String checkLog(int number)
	{
		String values[];
		String logTime = "";
		try {
			input = new FileReader(files[0]);
			reader = new BufferedReader(input);
			
			//Looks last time student sign in, by checking last instance of student number in log file
			String line = reader.readLine();
			while(line!=null)
			{
				values = line.split(",");
				if(Integer.toString(number).equals(values[3]))
				{
					logTime = values[4];
				}
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "File not found");
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not process file");

		}
		return logTime;
	}
	/**
	 * Gets all the spares a student has
	 * @param student The student whose spare periods are being searched
	 * @return An array of booleans for where each true index corresponds to each spare period a student has
	 */
	public static boolean[] getSpares(Student student)
	{
		String referenceNo = Integer.toString(student.getNum());
		String values[];
		boolean[] spares = new boolean[8];
		try {
			input = new FileReader(files[1]);
			reader = new BufferedReader(input);
			String line = reader.readLine();
			//Looks for spares on day 1
			while(line != null)
			{
				values = line.split(",");
				if(referenceNo.equals(values[3]))
				{
					spares[Integer.valueOf(values[0]) - 1] = true;
				}
				line = reader.readLine();
			}
			input = new FileReader(files[2]);
			reader = new BufferedReader(input);
			line = reader.readLine();
			//Looks for sppare in day 2
			while(line != null)
			{
				values = line.split(",");
				if(referenceNo.equals(values[3]))
				{
					spares[Integer.valueOf(values[0]) + 3] = true;
				}
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "File not found");
			
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not process file");
		}
		
		return spares;
	}
	/**
	 * Finds file path being used and replaces with a new file path
	 * @param file The file path being replaced
	 * @param path The new file path
	 */
	public static void setPath(String file, String path)
	{
		for(int i = 0; i < files.length; i++){
			if(files[i].equals(file)){
				files[i] = path;
			}
		}
	}
	/**
	 * Gets the string of the path of the log file being used
	 * @return Log file path
	 */
	public static String getLogPath()
	{	
		return files[0];
	}
	/**
	 * Finds the string being used as the path for the day-one file
	 * @return File path of day-one spare data file
	 */
	public static String getOnePath()
	{
		return files[1];
	}
	/**
	 * Finds the string being used as the path for the day-two file
	 * @return File path of day-two spare data file
	 */
	public static String getTwoPath()
	{
		return files[2];
	}
	/**
	 * Adds student to spare data files
	 * @param day Day of student's spare
	 * @param period Period of student's spare
	 * @param first First name of student being added
	 * @param last Last name of student being added
	 * @param num Student number of student
	 */
	public static void addStudent(int day, int period, String first, String last, int num) {
		try{
			//Determines spare period
			if(day == 1 || day == 3){
					fileWriter = new FileWriter(files[1], true);
				if(day == 3){
					switch(period){
					case 3: period = 4;
					break;
					case 4: period = 3;
					break;
					default:;
					}
				}
			}
			else if(day == 2 || day == 4){
				fileWriter = new FileWriter(files[2],true);
				if(day == 4){
					switch(period){
					case 3: period = 4;
					break;
					case 4: period = 3;
					break;
					default:;
					}
				}
			}
			//Creates students and writes it into data file
			Student temp = new Student(period,first,last,num);
			fileWriter.append(temp.toFullString());
			fileWriter.append("\n");
			
			fileWriter.close();
			JOptionPane.showMessageDialog(null, "Student has successfully been added");	
			}
		catch(FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Student could not be added, because the data file could not be found");	
		}
		catch(IOException exc)
		{
			JOptionPane.showMessageDialog(null, "Student could not be added, because of file process error");	
		}		
	}
	public static void updateNames(ArrayList<Student> mainList)
	{
		//Writes all names back to temp file and rename it to old file
	}
}
