/* $Id: StudentPayment.java,v 1.1 2006/07/21 06:41:21 joelchou Exp $
 *
 * $Log: StudentPayment.java,v $
 * Revision 1.1  2006/07/21 06:41:21  joelchou
 * init
 *
 * Revision 1.1  2005/12/07 04:25:37  joelchou
 * Submitted for new structure.
 *
 * Revision 1.10  2005/05/15 06:08:36  joelchou
 * Added uniform payments.
 *
 * Revision 1.9  2004/11/20 11:26:24  jlin
 * use string to represent the semester data
 *
 * Revision 1.8  2004/11/16 13:17:02  jlin
 * concatnat the first name and last name
 *
 * Revision 1.7  2004/11/12 13:26:04  jlin
 * add the data fields for join table
 *
 * Revision 1.6  2004/11/03 12:43:13  jlin
 * store semester in datatype int
 *
 * Revision 1.5  2004/10/25 21:40:05  jlin
 * fix some Typo
 *
 * Revision 1.4  2004/10/25 12:06:16  jlin
 * use String for checkNo
 *
 * Revision 1.3  2004/10/22 12:32:56  jlin
 * add package which this class belongs to
 *
 * Revision 1.2  2004/10/21 12:52:30  jlin
 * fix the cvs Header
 *
 *
 */

package datamodel;

/**
 * @(#)StudentPayment
 * This class is used to represent the student's payment in data storage
 * 
 * @author Jeff Lin
 * @version $Date: 2006/07/21 06:41:21 $
 */

public class StudentPayment
{
	private long studentId;
	private int year;
	private String semester = "";
	private String checkNo = "";
	private int tuition;
   private int paymentTypeId;
	
	//data field for join table
	private String english_name;
	private String chinese_name;
	private String course_name;
	
		
	// getter
	public long getStudentId()
	{
		return studentId;
	}
	
	public int getYear()
	{
		return year;
	}
	
	public String getSemester()
	{
		return semester;
	}
	
	public String getCheckNo()
	{
		return checkNo;
	}
	
	public int getTuition()
	{
		return tuition;
	}
	
	public String getEngName()
	{
	    return english_name;
	}
	
	public String getChName()
	{
	    return chinese_name;
	}
	
	public String getCourseName()
	{
	    return course_name;
	}
	
	//setter
	public void setStudentId(long id)
	{
		studentId = id;
	}
	
	public void setYear(int myYear)
	{
		year = myYear;
	}
	
	public void setSemester(String mySemester)
	{
		semester = mySemester;
	}
	
	public void setCheckNo(String myCheck)
	{
		checkNo = myCheck;
	}
	
	public void setTuition(int myTuition)
	{
		tuition = myTuition;
	}
	
	public void setEngName(String first_name, String last_name)
	{
	    english_name = first_name + " " + last_name;
	}
	
	public void setChName(String name)
	{
	    chinese_name = name;
	}
	
	public void setCourseName(String name)
	{
	    course_name = name;
	}
   
   /**
    * @return Returns the paymentTypeId.
    */
   public int getPaymentTypeId()
   {
      return paymentTypeId;
   }
   
   /**
    * @param paymentType The paymentTypeId to set.
    */
   public void setPaymentTypeId(int paymentTypeId)
   {
      this.paymentTypeId = paymentTypeId;
   }   
}
