package datamodel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
public class StudentRegManager
{
   public static String TABLE_NAME = "student_reg";
   public static String COL_COURSE_ID = "course_id";
   public static String COL_STUDENT_ID = "student_id";
   /**
    * Gets all students in a course.
    */
   public Vector getStudents(Connection con, int courseId) throws SQLException
   {
      Vector ids = new Vector();
      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
               + COL_COURSE_ID + "=" + courseId;
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         while(rs.next())
         {
            ids.add(getStudentReg(rs));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return ids;
   }
   
   /**
    * Gets registered course IDs for a student from a list of possible courses.
    */
   public Vector getRegisteredCourseIds(Connection con, long studentId, String inCond) 
      throws SQLException
   {
      Vector ids = new Vector();
      try
      {
         String query = "SELECT " + COL_COURSE_ID + " FROM " + TABLE_NAME + " WHERE "
               + COL_STUDENT_ID + "=? AND " 
               + COL_COURSE_ID + " IN (" + inCond + ")";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setLong(1, studentId);         
         ResultSet rs = pstmt.executeQuery();
         while(rs.next())
         {
            ids.add(new Integer(rs.getInt(1)));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return ids;
   }   
   /**
    * Gets all courses for a student.
    */
   public Vector getCourses(Connection con, long studentId) throws SQLException
   {
      Vector ids = new Vector();
      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
               + COL_STUDENT_ID + "=" + studentId + " ORDER BY "
               + COL_COURSE_ID;
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         while(rs.next())
         {
            ids.add(getStudentReg(rs));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return ids;
   }
   /**
    * Gets course IDs for a student within a group of courses.
    */
   public Vector getCourses(Connection con, long studentId, int[] courseIds)
         throws SQLException
   {
      Vector ids = new Vector();
      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
               + COL_STUDENT_ID + "=" + studentId;
         if(courseIds.length > 0)
         {
            StringBuffer idCond = new StringBuffer();
            idCond.append(COL_COURSE_ID + "=" + courseIds[0]);
            for(int i = 1; i < courseIds.length; i++)
            {
               idCond.append(" OR " + COL_COURSE_ID + "=" + courseIds[i]);
            }
            query = query + " AND ( " + idCond.toString() + " )";
         }
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         while(rs.next())
         {
            ids.add(getStudentReg(rs));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return ids;
   }
   public int insert(Connection con, StudentReg reg) throws SQLException
   {
      int result = -1;
      try
      {
         if(reg == null)
            return result;
         String query = "INSERT INTO " + TABLE_NAME + " (" + COL_COURSE_ID
               + "," + COL_STUDENT_ID + ") VALUES (?,?)";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setInt(1, reg.getCourseId());
         pstmt.setLong(2, reg.getStudentId());
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   public int update(Connection con, int newCourseId, StudentReg reg)
         throws SQLException
   {
      int result = -1;
      try
      {
         if(reg == null)
            return result;
         String query = "UPDATE " + TABLE_NAME + " SET " + COL_COURSE_ID + "=?"
               + " WHERE " + COL_COURSE_ID + "=? AND " + COL_STUDENT_ID + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setInt(1, newCourseId);
         pstmt.setInt(2, reg.getCourseId());
         pstmt.setLong(3, reg.getStudentId());
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   public int delete(Connection con, int courseId, long studentId)
         throws SQLException
   {
      int result = -1;
      try
      {
         String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_COURSE_ID
               + "=? AND " + COL_STUDENT_ID + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setInt(1, courseId);
         pstmt.setLong(2, studentId);
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   private StudentReg getStudentReg(ResultSet rs) throws SQLException
   {
      StudentReg reg = new StudentReg();
      reg.setCourseId(rs.getInt(COL_COURSE_ID));
      reg.setStudentId(rs.getLong(COL_STUDENT_ID));
      return reg;
   }
}
