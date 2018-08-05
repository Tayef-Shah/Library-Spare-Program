import java.io.Serializable;
/**
 * @version 1.0.0
 * @author Tayef
 * Students objects hold the students information concerning his/her spare, name, and student number
 */

@SuppressWarnings("serial")
public class Student implements Serializable
{
	int spare; 
	String first;
	String last;
	int studentNo;
	
/**
 * Constructs student
 * @param sparePeriod The spare period number of a student on either day 1 or day 2
 * @param firstName The first name of a student
 * @param lastName The last name of a student
 * @param studentNumber The student number of a student
 */
	public Student(int sparePeriod, String firstName, String lastName, int studentNumber)
	{	
		spare = sparePeriod; 
		first = firstName;
		last = lastName;
		studentNo = studentNumber;
	}
	/**
	 * Returns spare period of student
	 * @return int
	 */
	public int getSpare(){
		return spare;
	}
	/**
	 * Returns first name of student
	 * @return String
	 */
	public String getFirst(){
		return first;
	}
	/**
	 * Returns last name of student
	 * @return String
	 */
	public String getLast(){
		return last;
	}
	/**
	 * Returns student number
	 * @return String
	 */
	public int getNum(){
		return studentNo;
	}
	/**
	 * Returns full name of student
	 * @return String
	 */
	public String toString(){
		String toString = first + " " + last;
		return toString;
	}
	/**
	 * Returns all student object information formatted like a .csv file line
	 * @return String
	 */
	public String toFullString(){
		String toString = spare + "," + first + "," + last + "," + studentNo;
		return toString;
	}
}
