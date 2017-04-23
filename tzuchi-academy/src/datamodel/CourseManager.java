package datamodel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
public class CourseManager
{
  public static String TABLE_NAME = "course";
  public static String COL_COURSE_ID = "course_id"; 
  public static String COL_PRIMARY_TEACHER_ID = "primary_teacher_id";
  public static String COL_YEAR = "year";
  public static String COL_SEMESTER = "semester";
  public static String COL_CLASS_ID = "class_id";
  public static String COL_CLASS_ROOM = "class_room";
  public static String COL_SECONDARY_TEACHER_ID = "secondary_teacher_id";
  // semester
  public static String FULL = "FULL";
  public static String FALL = "Fall";
  public static String SPRING = "Spring";
  public Course getDataById(Connection con, int courseId) 
    throws SQLException
  {
    Course course = null;
    try
    {
      String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_COURSE_ID 
                     + "=" + courseId;
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      if(rs.next())
      {
        course = getCourse(rs);
      }
    }
    catch(SQLException e)
    {
      throw new SQLException(e.toString());
    }
    return course;  
  }
  /**
   * Gets courses in a semester for a given course category (-1 for all).
   */
  public Vector getDataBySemester(Connection con, int year, String semester, int category) 
    throws SQLException
  {
    Vector courses = new Vector();
    try
    {
      String query = "";
      if(category != -1)
      {
        // MySQL 4.0 (4.1 will) does not support subquery, use inner join instead
        query = "SELECT " + TABLE_NAME + ".* FROM " + TABLE_NAME + "," + ClassManager.TABLE_NAME + 
                " WHERE " + COL_YEAR + "=? AND " + COL_SEMESTER + "=?" +
                " AND " + TABLE_NAME + "." + COL_CLASS_ID + "=" +
                ClassManager.TABLE_NAME + "." + ClassManager.COL_CLASS_ID + " AND " +
                ClassManager.TABLE_NAME + "." + ClassManager.COL_CATEGORY + "=" + category; 
      }
      else
      {
        query = "SELECT * FROM " + TABLE_NAME + 
                " WHERE " + COL_YEAR + "=? AND " + COL_SEMESTER + "=?";
      }                     
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setInt(1, year);
      pstmt.setString(2, semester);
      ResultSet rs = pstmt.executeQuery();
      while(rs.next())
      {
        courses.add(getCourse(rs));
      }
    }
    catch(SQLException e)
    {
      throw new SQLException(e.toString());
    }
    return courses;  
  }
  /**
   * Gets registered courses for a student.
   */
  public Vector getDataByStudent(Connection con, long studentId) 
    throws SQLException
  {
    Vector courses = new Vector();
    try
    {
      String query =
        "SELECT " + TABLE_NAME + ".* FROM " + TABLE_NAME + "," + StudentRegManager.TABLE_NAME + 
        " WHERE " + TABLE_NAME + "." + COL_COURSE_ID + "=" +
        StudentRegManager.TABLE_NAME + "." + StudentRegManager.COL_COURSE_ID + " AND " +
        StudentRegManager.TABLE_NAME + "." + StudentRegManager.COL_STUDENT_ID + "=?" +
        " ORDER BY " + TABLE_NAME + "." + COL_YEAR + " DESC, " + TABLE_NAME + "." + COL_SEMESTER; 
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setLong(1, studentId);
      ResultSet rs = pstmt.executeQuery();
      while(rs.next())
      {
        courses.add(getCourse(rs));
      }
    }
    catch(SQLException e)
    {
      throw new SQLException(e.toString());
    }
    return courses;  
  }
  /**
   * Gets course category. -1 will be returned if there is no such course.
   */
  public int getCourseCategory(Connection con, int courseId) 
    throws SQLException
  {
    int category = -1;
    try
    {
      // MySQL 4.0 (4.1 will) does not support subquery, use inner join instead
      String query = "SELECT " + ClassManager.TABLE_NAME + "." + ClassManager.COL_CATEGORY + 
                     " FROM " + TABLE_NAME + "," + ClassManager.TABLE_NAME + 
                     " WHERE " + TABLE_NAME + "." + COL_COURSE_ID + "=? AND " +
                     TABLE_NAME + "." + COL_CLASS_ID + "=" +
                     ClassManager.TABLE_NAME + "." + ClassManager.COL_CLASS_ID;                     
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setInt(1, courseId);
      ResultSet rs = pstmt.executeQuery();
      if(rs.next())
      {
        category = rs.getInt(1);
      }
    }
    catch(SQLException e)
    {
      throw new SQLException(e.toString());
    }
    return category;  
  }
  public int insert(Connection con, Course course) throws SQLException
  {
    int result = -1;
    try 
    {
      if(course == null)
        return result;
      String query = "INSERT INTO " + TABLE_NAME + " (" +
                     COL_PRIMARY_TEACHER_ID + "," +
                     COL_YEAR + "," +
                     COL_SEMESTER + "," +
                     COL_CLASS_ID + "," + 
                     COL_CLASS_ROOM + "," + 
                     COL_SECONDARY_TEACHER_ID +
                     ") VALUES (?,?,?,?,?,?)";
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setLong(1, course.getPrimaryTeacherId());
      pstmt.setInt(2, course.getYear());
      pstmt.setString(3, course.getSemester());
      pstmt.setInt(4, course.getClassId());
      pstmt.setString(5, course.getClassRoom());
      pstmt.setLong(6, course.getSecondaryTeacherId());
      result = pstmt.executeUpdate();      
      
      // get last inserted course ID (this is MySQL specific function)
      pstmt = con.prepareStatement("SELECT LAST_INSERT_ID()");
      ResultSet rs = pstmt.executeQuery();
      if(rs.next())
      {
        result = rs.getInt(1);
      }            
    }
    catch(SQLException e)
    {
      throw new SQLException(e.toString());  
    }
    return result;
  } 
  public int update(Connection con, Course course) throws SQLException
  {
    int result = -1;
    try 
    {
      if(course == null)
        return result;
      String query = "UPDATE " + TABLE_NAME + " SET " +
                     COL_PRIMARY_TEACHER_ID + "=?," +
                     COL_YEAR + "=?," +
                     COL_SEMESTER + "=?," +
                     COL_CLASS_ID + "=?," + 
                     COL_CLASS_ROOM + "=?," + 
                     COL_SECONDARY_TEACHER_ID + "=?" +
                     " WHERE " +
                     COL_COURSE_ID + "=?";                     
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setLong(1, course.getPrimaryTeacherId());
      pstmt.setInt(2, course.getYear());
      pstmt.setString(3, course.getSemester());
      pstmt.setInt(4, course.getClassId());
      pstmt.setString(5, course.getClassRoom());
      pstmt.setLong(6, course.getSecondaryTeacherId());
      pstmt.setInt(7, course.getCourseId());
      result = pstmt.executeUpdate();      
    }
    catch(SQLException e)
    {
      throw new SQLException(e.toString());  
    }
    return result;
  } 
  
  public int delete(Connection con, int courseId) throws SQLException
  { 
    int result = -1;
    try 
    {
      // check if current course ID is referenced by others
      String query = "SELECT " + StudentRegManager.COL_STUDENT_ID + " FROM " + StudentRegManager.TABLE_NAME +
                     " WHERE " + StudentRegManager.COL_COURSE_ID + "=? LIMIT 1";
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setInt(1, courseId); 
      ResultSet rs = pstmt.executeQuery();
      if(rs.next())
        throw new SQLException("Can not delete it. Course ID " + courseId + " is referenced in table student_reg.");
      // only course ID not referenced by others can be deleted
      query = "DELETE FROM " + TABLE_NAME +
              " WHERE " +
              COL_COURSE_ID + "=?";
      pstmt = con.prepareStatement(query);
      pstmt.setInt(1, courseId); 
      result = pstmt.executeUpdate();
    }
    catch(SQLException e)
    {
      throw new SQLException(e.toString());  
    }
    return result;
  }  
  private Course getCourse(ResultSet rs) throws SQLException
  {
    Course course = new Course();
    course.setCourseId(rs.getInt(COL_COURSE_ID));
    course.setPrimaryTeacherId(rs.getLong(COL_PRIMARY_TEACHER_ID));
    course.setYear(rs.getInt(COL_YEAR));
    course.setSemester(rs.getString(COL_SEMESTER));
    course.setClassId(rs.getInt(COL_CLASS_ID));
    course.setClassRoom(rs.getString(COL_CLASS_ROOM));
    course.setSecondaryTeacherId(rs.getLong(COL_SECONDARY_TEACHER_ID));
    return course;
  }  
}
