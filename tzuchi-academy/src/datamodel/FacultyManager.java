/* $Id: FacultyManager.java,v 1.1 2006/07/21 06:41:20 joelchou Exp $*/

package datamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class FacultyManager
{
  public static String TABLE_NAME = "faculty";
  public static String COL_TYPE = "type";
  public static String COL_FACULTY_ID = "faculty_id";  
  public static String COL_FIRST_NAME = "first_name";  
  public static String COL_LAST_NAME = "last_name";
  public static String COL_CHINESE_NAME = "chinese_name";  
  public static String COL_STREET = "street";  
  public static String COL_APT = "apt";  
  public static String COL_CITY = "city";
  public static String COL_STATE = "state";
  public static String COL_ZIP = "zip";
  public static String COL_AREA_CODE = "area_code";
  public static String COL_PHONE = "phone";
  public static String COL_EMAIL = "email";
  public static String COL_MOBILE_PHONE= "mobile_phone";
  
  //for faculty TYPE
  public static String TYPE_STAFF = "staff";
  public static String TYPE_TEACHER = "teacher";
  public static String TYPE_VOLUNTEER = "volunteer";

  /*
   * get faculty info by id
   */
  public Faculty getDataById(Connection con, long facultyId) 
    throws SQLException
  {
    Faculty faculty = null;

    try
      {
	String query = "select * from " + TABLE_NAME + " where " +
	  COL_FACULTY_ID + " =?";
	PreparedStatement pstmt = con.prepareStatement(query);
	pstmt.setLong(1, facultyId);
	ResultSet rs = pstmt.executeQuery();

	if (rs.next())
	  {
	    faculty=getFaculty(rs);
	  }
      } catch(SQLException e)
	{
	  throw new SQLException (e.toString());
	}
	    
    return faculty;
  }

  /**
   * Gets faculties by type.
   */
  public Vector getDataByType(Connection con, String type) 
    throws SQLException
  {
    Vector faculties = new Vector();
    
    try
    {
      String query = "SELECT * FROM " + TABLE_NAME + " WHERE " 
	+ COL_TYPE + "=?" + " ORDER BY " + COL_FIRST_NAME + "," 
	+ COL_LAST_NAME + "," + COL_CHINESE_NAME;
      
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setString(1, type);
      ResultSet rs = pstmt.executeQuery();
    
      while(rs.next())
      {
        faculties.add(getFaculty(rs));
      }
    }
    catch(SQLException e)
    {
      throw new SQLException(e.toString());
    }
    
    return faculties;  
  }

  /**
   * Gets all faculties.
   */
  public Vector getAllData(Connection con) 
    throws SQLException
  {
    Vector faculties = new Vector();
    
    try
    {
      String query = "SELECT * FROM " + TABLE_NAME +" ORDER BY " 
	+ COL_FIRST_NAME + "," + COL_LAST_NAME 
	+ "," + COL_CHINESE_NAME;
      
      PreparedStatement pstmt = con.prepareStatement(query);
      ResultSet rs = pstmt.executeQuery();
    
      while(rs.next())
      {
        faculties.add(getFaculty(rs));
      }
    }
    catch(SQLException e)
    {
      throw new SQLException(e.toString());
    }
    
    return faculties;  
  }
  
  /*
   *Update a record from database
   */
  public int update(Connection con, Faculty faculty) throws SQLException
  {
    int result = -1;

    try 
    {
      if(faculty == null)
        return result;
    
      String query = "update " + TABLE_NAME + " set " +
	             COL_TYPE + "=?," +
                     COL_FIRST_NAME + "=?," +
                     COL_LAST_NAME + "=?," +
                     COL_CHINESE_NAME + "=?," +	           
                     COL_STREET + "=?," +
                     COL_APT + "=?," +
                     COL_CITY + "=?," +
                     COL_STATE + "=?," +
                     COL_ZIP + "=?," +
                     COL_AREA_CODE + "=?," +
                     COL_PHONE + "=?," +
                     COL_EMAIL + "=?," +                     
                     COL_MOBILE_PHONE + "=?" +
                     " where " +
	             COL_FACULTY_ID + " =?";
    
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setString(1, faculty.getType());
      pstmt.setString(2, faculty.getFirstName());
      pstmt.setString(3, faculty.getLastName());
      pstmt.setString(4, faculty.getChineseName());
      pstmt.setString(5, faculty.getStreet());
      pstmt.setString(6, faculty.getApt());
      pstmt.setString(7, faculty.getCity());
      pstmt.setString(8, faculty.getState());
      pstmt.setString(9, faculty.getZip());
      pstmt.setString(10, faculty.getAreaCode());
      pstmt.setString(11, faculty.getPhone());
      pstmt.setString(12, faculty.getEmail());
      pstmt.setString(13, faculty.getMobilePhone());
      pstmt.setLong(14, faculty.getFacultyId());
     
       result = pstmt.executeUpdate();      
    }
    catch(SQLException e)
    {
      throw new SQLException(e.toString());  
    }

    return result;
  }

  /*
   *Insert a record to database
   */
   public int insert (Connection con, Faculty faculty) throws SQLException
  {
    int result = -1;

    try 
    {
      if(faculty == null)
        return result;
    
      String query = "INSERT INTO " + TABLE_NAME + " (" +
	             COL_FACULTY_ID + "," +
	             COL_TYPE + "," +
                     COL_FIRST_NAME + "," +
                     COL_LAST_NAME + "," +
                     COL_CHINESE_NAME + "," +
                     COL_STREET + "," +
                     COL_APT + "," +
                     COL_CITY + "," +
                     COL_STATE + "," +
                     COL_ZIP + "," +
                     COL_AREA_CODE + "," +
                     COL_PHONE + "," +
                     COL_EMAIL + "," +                     
                     COL_MOBILE_PHONE + 
                     ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setLong(1, faculty.getFacultyId());
      pstmt.setString(2, faculty.getType());
      pstmt.setString(3, faculty.getFirstName());
      pstmt.setString(4, faculty.getLastName());
      pstmt.setString(5, faculty.getChineseName());
      pstmt.setString(6, faculty.getStreet());
      pstmt.setString(7, faculty.getApt());
      pstmt.setString(8, faculty.getCity());
      pstmt.setString(9, faculty.getState());
      pstmt.setString(10, faculty.getZip());
      pstmt.setString(11, faculty.getAreaCode());
      pstmt.setString(12, faculty.getPhone());
      pstmt.setString(13, faculty.getEmail());
      pstmt.setString(14, faculty.getMobilePhone());
     
       result = pstmt.executeUpdate();      
    }
    catch(SQLException e)
    {
      throw new SQLException(e.toString());  
    }

    return result;
  }

  /*
   * delete a record from database
   */
  public int delete(Connection con, long facultyId) throws SQLException
  {
    int result = -1; 
    
   try 
    {
      //check if current facultyId is reference by other
      String query = "select " + CourseManager.COL_COURSE_ID
	+ " from " + CourseManager.TABLE_NAME + " where "
	+ CourseManager.COL_PRIMARY_TEACHER_ID + " =? || "
	+ CourseManager.COL_SECONDARY_TEACHER_ID 
	+ " =? LIMIT 1";
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setLong(1, facultyId);
      pstmt.setLong(2, facultyId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next())
	throw new SQLException ("Can not delete it. Faculty ID "
	  + facultyId + " is referenced in course table.");
     
      // only faculty id not referenced by other can be delete
      query = "DELETE FROM " + TABLE_NAME +
                     " WHERE " +
                     COL_FACULTY_ID + "=?";

      pstmt = con.prepareStatement(query);
      pstmt.setLong(1, facultyId); 
      
      result = pstmt.executeUpdate();
    }
    catch(SQLException e)
    {
      throw new SQLException(e.toString());  
    }

    return result;
  }
  
  /*
   *set the Faculty after query
   */
  private Faculty getFaculty(ResultSet rs) throws SQLException
  {
    Faculty f=new Faculty();
    f.setFacultyId(rs.getLong(COL_FACULTY_ID));
    f.setType(rs.getString(COL_TYPE));
    f.setFirstName(rs.getString(COL_FIRST_NAME));
    f.setLastName(rs.getString(COL_LAST_NAME));
    f.setChineseName(rs.getString(COL_CHINESE_NAME));
    f.setStreet(rs.getString(COL_STREET));
    f.setApt(rs.getString(COL_APT));
    f.setCity(rs.getString(COL_CITY));
    f.setState(rs.getString(COL_STATE));
    f.setZip(rs.getString(COL_ZIP));
    f.setAreaCode(rs.getString(COL_AREA_CODE));
    f.setPhone(rs.getString(COL_PHONE));
    f.setEmail(rs.getString(COL_EMAIL));
    f.setMobilePhone(rs.getString(COL_MOBILE_PHONE));

    return f;
  }
}
