/* $Id: StudentPaymentMgr.java,v 1.1 2006/07/21 06:41:21 joelchou Exp $
*
* $Log: StudentPaymentMgr.java,v $
* Revision 1.1  2006/07/21 06:41:21  joelchou
* init
*
* Revision 1.2  2006/02/04 07:06:41  joelchou
* Updated for carrying over data from previous semester. Currently, we only carry over data from Fall to Spring since most of students register the whole year.
*
* Revision 1.1  2005/12/07 04:25:37  joelchou
* Submitted for new structure.
*
* Revision 1.13  2005/05/15 06:08:36  joelchou
* Added uniform payments.
*
* Revision 1.12  2004/12/07 13:21:20  jlin
* enable check no support
*
* Revision 1.11  2004/11/20 19:52:26  jlin
* fixed the query problem on wrong years and semesters.
*
* Revision 1.10  2004/11/20 11:26:24  jlin
* use string to represent the semester data
*
* Revision 1.9  2004/11/19 13:27:57  jlin
* prepar to fix the query problem on wrong years and semesters.
*
* Revision 1.8  2004/11/16 20:15:54  jlin
* Add student id in the query string
*
* Revision 1.7  2004/11/16 13:17:45  jlin
* implement findStudentsByTerm
*
* Revision 1.6  2004/11/03 12:45:59  jlin
* 1. change semester datatype to in
* 2. add new funcation updateSemester
*
* Revision 1.5  2004/11/02 13:13:18  jlin
* 1. create new delete function
* 2. fix update query statement
*
* Revision 1.4  2004/10/26 12:10:30  jlin
* 1. rename getRegisteredStudents to findStudentByTerm
* 2. add new function findStudentById
*
* Revision 1.3  2004/10/25 21:49:41  jlin
* finish the delete, update, insert functions
*
* Revision 1.2  2004/10/25 12:07:11  jlin
* Implementation continued
*
* Revision 1.1  2004/10/22 12:31:14  jlin
* initial draft
*
*
*/

package datamodel;

import java.util.*;
import java.sql.*;

import util.Constants;

/**
* @(#)StudentPaymentMgr
* This class is Manager class that query the database and get the result set 
* from database
* 
* @author Jeff Lin
* @version $Date: 2006/07/21 06:41:21 $
*/
public class StudentPaymentMgr
{
	  public static String TABLE_NAME = "tuition";
	  public static String COL_STUDENT_ID = "student_id";
	  public static String COL_YEAR = "year";
	  public static String COL_SEMESTER = "semester";
	  public static String COL_CHECK_NO = "check_no";
	  public static String COL_TUITION = "tuition";
     public static String COL_PAYMENT_TYPE_ID = "payment_type_id";
	  
	  /**
	   * find multiple student payment infomation by the term of the year.
	   * @param year - year to be query
	   * @param semester - school semester to be query not payment semester.	   
	   * @return the single or multiple records of query results
	   */ 
	  public Vector findStudentsByTerm(Connection con, int year, String semester)
	  throws SQLException
	  {
		  /*
		   * t1 - student_reg table
		   * t2 - course table
		   * t3 - tuition
		   * t4 - student
		   * t5 - class
		   */
	      
	      Vector result = new Vector();
	      try
	      {
	          /*
	           * the original query
	           * select t4.last_name, t4.first_name, t4.chinese_name, t5.name, t3.tuition, t3.semester from student_reg t1
	           * left join course t2 on t1.course_id=t2.course_id
	           * left join tuition t3 on t1.student_id = t3.student_id
	           * left join student t4 on t1.student_id = t4.student_id
	           * left join class t5 on t2.class_id = t5.class_id
	           * where t2.year = 2004 and t2.semester = "fall" and t5.category = 1
	           */
	          /* 
	           * This query fix the year and semester problem
	           * select t4.last_name, t4.first_name, t5.name, t3.tuition, t3.semester, t3.tuition from student_reg t1
	           * left join course t2 on t1.course_id=t2.course_id
	           * left join (select * from tuition where tuition.semester="spring" or tuition.semester = "FULL") t3
	           * on t1.student_id = t3.student_id and t2.year = t3.year
	           * left join student t4 on t1.student_id = t4.student_id
	           * left join class t5 on t2.class_id = t5.class_id
	           * where t2.year = 2004 and t2.semester = "spring" and t5.category = 1
	           */
	          String query = "SELECT t4." + StudentManager.COL_FIRST_NAME +", t4." +
	          	StudentManager.COL_LAST_NAME + ", t4." + StudentManager.COL_CHINESE_NAME +
	          	", t5." + ClassManager.COL_NAME + ", t3." + COL_TUITION +", t1."+StudentManager.COL_STUDENT_ID + ", t3." +
	          	COL_SEMESTER + ", t3." + COL_CHECK_NO + "\nFROM " + StudentRegManager.TABLE_NAME + " t1\nleft join " +
	          	CourseManager.TABLE_NAME +" t2 on t1." + StudentRegManager.COL_COURSE_ID +
	          	" = t2." + CourseManager.COL_COURSE_ID +"\nleft join (SELECT * FROM " +
	          	TABLE_NAME + " WHERE " + TABLE_NAME + "." + COL_SEMESTER + " = \"" + semester +"\" or " +
	          	TABLE_NAME + "." + COL_SEMESTER + "= \"" + Constants.FULL
	          	+"\") t3 on t1." + StudentRegManager.COL_STUDENT_ID + " = t3." + COL_STUDENT_ID 
	          	+ " AND t2." + CourseManager.COL_YEAR + " = t3." + COL_YEAR + "\nleft join " +
	          	StudentManager.TABLE_NAME + " t4 on t1." + StudentRegManager.COL_STUDENT_ID +
	          	" = t4." + StudentManager.COL_STUDENT_ID + "\nleft join " +
	          	ClassManager.TABLE_NAME + " t5 on t2." + CourseManager.COL_CLASS_ID +
	          	" = t5." + ClassManager.COL_CLASS_ID + "\nWHERE t2." + CourseManager.COL_YEAR +
	          	" = " + year + " AND t2." + CourseManager.COL_SEMESTER + " = \"" + semester +
	          	"\" AND t5." + ClassManager.COL_CATEGORY + " = " + ClassManager.CHINESE;	          
 /*         
	          String query = "SELECT t4." + StudentManager.COL_FIRST_NAME +", t4." +
	          	StudentManager.COL_LAST_NAME + ", t4." + StudentManager.COL_CHINESE_NAME +
	          	", t5." + ClassManager.COL_NAME + ", t3." + COL_TUITION +", t1."+StudentManager.COL_STUDENT_ID + ", t3." +
	          	COL_SEMESTER + "\nFROM " + StudentRegManager.TABLE_NAME + " t1\nleft join " +
	          	CourseManager.TABLE_NAME +" t2 on t1." + StudentRegManager.COL_COURSE_ID +
	          	" = t2." + CourseManager.COL_COURSE_ID +"\nleft join " +
	          	TABLE_NAME +" t3 on t1." + StudentRegManager.COL_STUDENT_ID +
	          	" = t3." + COL_STUDENT_ID + "\nleft join " +
	          	StudentManager.TABLE_NAME + " t4 on t1." + StudentRegManager.COL_STUDENT_ID +
	          	" = t4." + StudentManager.COL_STUDENT_ID + "\nleft join " +
	          	ClassManager.TABLE_NAME + " t5 on t2." + CourseManager.COL_CLASS_ID +
	          	" = t5." + ClassManager.COL_CLASS_ID + "\nWHERE t2." + CourseManager.COL_YEAR +
	          	" = " + year + " AND t2." + CourseManager.COL_SEMESTER + " = \"" + semester +
	          	"\" AND t5." + ClassManager.COL_CATEGORY + " = " + ClassManager.CHINESE;
*/	          	          
	          	          
		        Statement stmt = con.createStatement();
		        ResultSet rs = stmt.executeQuery(query);          	
	          	
		        while (rs.next())
		        {
		            result.add(getStudentPaymentByTerm(rs));
		        }
		 	      
		  	}catch(SQLException e)
		  	{
		  		throw new SQLException(e.toString());
		  	}
		  
		  	return result;
	  }
/*	  public Vector findStudentsByTerm(Connection con, int year, int semester)
	  throws SQLException
	  {
	  	Vector result = new Vector();
	  	
	  	try 
		{
	        String query = "SELECT * FROM " + TABLE_NAME + 
            " WHERE " + COL_YEAR + "=" + year +
			" AND " + COL_SEMESTER + "=" + semester + ";";
            
	        Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery(query);

	        while(rs.next())
	        {
	        	result.add(getStudentPayment(rs));
	        }
	        
	  	}catch(SQLException e)
		{
	  		throw new SQLException(e.toString());
		}
	  	
	  	return result;
	  }
*/	  
	  
	  /**
	   * get student's payment infomation by giving student id, year and semester
	   * @param studentId - student ID to be query
	   * @param year - year to be query
	   * @param semester - semester to be query
	   * @return single record of query results. null when there is no record found
	   */  
	  public StudentPayment findStudentById(Connection con ,long studentId,
	  		int year, String semester)throws SQLException
	  {
	  	StudentPayment sp = null;
	  	
	  	try 
		{
	        String query = "SELECT * FROM " + TABLE_NAME + 
            " WHERE " + COL_YEAR + "=?" +
			" AND " + COL_SEMESTER + "=?"  +
			" AND " + COL_STUDENT_ID + "=?;";

	  		PreparedStatement pstmt = con.prepareStatement(query);
	  		pstmt.setInt(1, year);
	  		pstmt.setString(2, semester);
	  		pstmt.setLong(3, studentId);
	  		
	  		ResultSet rs = pstmt.executeQuery();

	  		if (rs.next())
	  			sp = getStudentPayment(rs);
	  		
	  	}catch(SQLException e)
		{
	  		throw new SQLException(e.toString());
		}
	  		  	
	  	return sp;
	  }
	  
   /**
	 * Gets registered student IDs in a semester.
	 */
   public Vector getStudentIdsByTerm(Connection con, int year, String semester) 
      throws SQLException
   {
      Vector ids = new Vector();

      try
      {
         String query = "SELECT " + COL_STUDENT_ID + " FROM " + TABLE_NAME + " WHERE "
            + COL_YEAR + "=? AND " 
            + COL_SEMESTER + "=?";

         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setInt(1, year);      
         pstmt.setString(2, semester);
         ResultSet rs = pstmt.executeQuery();

         while(rs.next())
         {
            ids.add(new Long(rs.getLong(1)));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }

      return ids;
   }        
     
	  /**
	   * insert a StudentPayment recorder to database
	   * @param con Database connection object
	   * @param studentPay - the recorder want to be inserted
	   */
	  public int insert(Connection con, StudentPayment studentPay)
	  throws SQLException
	  {
	  	int result = -1;
	  	
	  	try
		{
	  		String query = "INSERT INTO " + TABLE_NAME + " (" +
        				COL_YEAR + "," +
						COL_SEMESTER + "," +
						COL_STUDENT_ID + "," +
						COL_CHECK_NO + "," +
						COL_TUITION + "," + 
                  COL_PAYMENT_TYPE_ID +
						") VALUES (?,?,?,?,?,?)";
	  	
	  		PreparedStatement pstmt = con.prepareStatement(query);
	  		pstmt.setInt(1, studentPay.getYear());
	  		pstmt.setString(2, studentPay.getSemester());
	  		pstmt.setLong(3, studentPay.getStudentId());
	  		pstmt.setString(4, studentPay.getCheckNo());
	  		pstmt.setInt(5, studentPay.getTuition());
         pstmt.setInt(6, studentPay.getPaymentTypeId());
	  		
	  		result = pstmt.executeUpdate();
		}catch (SQLException e)
		{
			throw new SQLException(e.toString()); 
		}
	  	
	  	
	  	return result;
	  }
	  
	  /**
	   * update a StudentPayment recorder to database
	   * @param con Database connection object
	   * @param studentPay - the recorder want to be inserted
	   */
	  public int updatePayment(Connection con, StudentPayment studentPay)
	  throws SQLException
	  {
	  	int result = -1;
	  	
	  	try
		{
	  		String query = "UPDATE " + TABLE_NAME + " SET " +
						COL_CHECK_NO + "=?," +
						COL_TUITION + "=?," +
                  COL_PAYMENT_TYPE_ID + "=?" +
						" WHERE " +
						COL_STUDENT_ID + "=?" +
						" AND " + COL_YEAR + "=?" +
						" AND " + COL_SEMESTER + "=?;";
	  		
	  		PreparedStatement pstmt = con.prepareStatement(query);
	  		
	  		pstmt.setInt(5, studentPay.getYear());
	  		pstmt.setString(6, studentPay.getSemester());
	  		pstmt.setLong(4, studentPay.getStudentId());
	  		pstmt.setString(1, studentPay.getCheckNo());
	  		pstmt.setInt(2, studentPay.getTuition());
         pstmt.setInt(3, studentPay.getPaymentTypeId());
         
	  		result = pstmt.executeUpdate();
		}catch (SQLException e)
		{
			throw new SQLException(e.toString()); 
		}
	  		  	
	  	return result;	  	
	  }
	  
	  /**
	   * update the semester field for a student payment record
	   * @param con Database connection object
	   * @param studentPay - the recorder want to be inserted
	   */
	  public int updateSemester(Connection con, StudentPayment studentPay)
	  throws SQLException
	  {
	  	int result = -1;
	  	
	  	try
		{
	  		String query = "UPDATE " + TABLE_NAME + " SET " +
	  					COL_SEMESTER + "=?" +
						" WHERE " +
						COL_STUDENT_ID + "=?" +
						" AND " + COL_YEAR + "=?" +
						" AND " + COL_TUITION + "=?;";
	  		
	  		PreparedStatement pstmt = con.prepareStatement(query);
	  		
	  		pstmt.setInt(3, studentPay.getYear());
	  		pstmt.setString(1, studentPay.getSemester());
	  		pstmt.setLong(2, studentPay.getStudentId());
	  		pstmt.setInt(4, studentPay.getTuition());
	  		
	  		result = pstmt.executeUpdate();
		}catch (SQLException e)
		{
			throw new SQLException(e.toString()); 
		}	  		  	
	  	return result;	  	
	  }
	  
	  
	  /**
	   * delete all recorders that belong to a student
	   * @param con Database connection object
	   * @param studentId - matched recorder to be deleted
	   * @return the result of the query -1 means failed
	   */
	  public int delete(Connection con, long studentId)
	  throws SQLException
	  {
	  	int result = -1;
	  	try 
		{  
	  		String query = "DELETE FROM " + TABLE_NAME +
					" WHERE " +
					COL_STUDENT_ID + "=?";
	      
	  		PreparedStatement pstmt = con.prepareStatement(query);
	  		pstmt.setLong(1, studentId); 
	  		result = pstmt.executeUpdate();
	  	}catch (SQLException e)
		{
			throw new SQLException(e.toString()); 
		}
	      
	  	return result;
	  }
	  /**
	   * delete a record for a student in a school year
	   * @param con Database connection object
	   * @param sp - the StudentPayment to be delete
	   * @return teh result of the query.  -1 means failed
	   */
	  public int delete(Connection con, StudentPayment sp)
	  throws SQLException
	  {
		  	int result = -1;
		  	try 
			{  
		  		String query = "DELETE FROM " + TABLE_NAME +
						" WHERE " +
						COL_STUDENT_ID + "=?" +
						" AND " + COL_YEAR + "=?" +
						" AND " + COL_SEMESTER + "=?;";	
		  		
		  		PreparedStatement pstmt = con.prepareStatement(query);
		  		pstmt.setLong(1, sp.getStudentId());
		  		pstmt.setInt(2, sp.getYear());
		  		pstmt.setString(3, sp.getSemester());
		  		result = pstmt.executeUpdate();
		  		
		  	}catch (SQLException e)
			{
				throw new SQLException(e.toString()); 
			}
		      
		  	return result;
	      
	  }
	  
	  /**
	   * translate database query recorder to object model
	   * @param rs - the result set of the query
	   * @return the object represent the one database recorder 
	   */
	  private StudentPayment getStudentPayment(ResultSet rs) throws SQLException
	  {
	  	StudentPayment sp = new StudentPayment();
	  	sp.setStudentId(rs.getLong(COL_STUDENT_ID));
	  	sp.setYear(rs.getInt(COL_YEAR));
	  	if(rs.getString(COL_SEMESTER).compareToIgnoreCase("null") != 0)
	  	    sp.setSemester(rs.getString(COL_SEMESTER));
	  	sp.setTuition(rs.getInt(COL_TUITION));
	  	if(rs.getString(COL_CHECK_NO).compareToIgnoreCase("null") != 0)
	  	    sp.setCheckNo(rs.getString(COL_CHECK_NO));
      sp.setPaymentTypeId(rs.getInt(COL_PAYMENT_TYPE_ID));
	  	
	  	return sp;
	  }
	  
	  /**
	   * translate database query recorder to object model
	   * @param rs - the result set of the query
	   * @return the object represent the one database recorder 
	   */
	  private StudentPayment getStudentPaymentByTerm(ResultSet rs) throws SQLException
	  {
	  	StudentPayment sp = new StudentPayment();
	  	sp.setStudentId(rs.getLong(StudentManager.COL_STUDENT_ID));
	  	sp.setSemester(rs.getString(COL_SEMESTER));
	  	sp.setTuition(rs.getInt(COL_TUITION));
	  	sp.setEngName(rs.getString(StudentManager.COL_FIRST_NAME), rs.getString(StudentManager.COL_LAST_NAME));
	  	sp.setCourseName(rs.getString(ClassManager.COL_NAME));
	  	sp.setChName(rs.getString(StudentManager.COL_CHINESE_NAME));
	  	sp.setCheckNo(rs.getString(COL_CHECK_NO));
      //sp.setPaymentTypeId(rs.getInt(COL_PAYMENT_TYPE_ID));
      
	  	return sp;
	  }
	  	    
	  		
}