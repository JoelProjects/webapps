package datamodel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
public class ClassManager
{
   public static String TABLE_NAME = "class";
   public static String COL_CLASS_ID = "class_id";
   public static String COL_NAME = "name";
   public static String COL_CATEGORY = "category";
   public static String COL_DESCRIPTION = "description";
   public static String COL_MAX_STUDENTS = "max_student";
   // class category
   public static int CHINESE = 1;
   public static int ACTIVITY = 20;
   public Class getDataById(Connection con, int classId) throws SQLException
   {
      Class cl = null;
      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
               + COL_CLASS_ID
               + "=" + classId;
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         if(rs.next())
         {
            cl = getClass(rs);
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return cl;
   }
   /**
    * Gets all classes in a category.
    */
   public Vector getDataByCategory(Connection con, int category)
         throws SQLException
   {
      Vector classes = new Vector();
      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
               + COL_CATEGORY + "=" + category;
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         while(rs.next())
         {
            classes.add(getClass(rs));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return classes;
   }
   /**
    * Gets courses in a semester for a given course category (-1 for all).
    */
   public String getNameByCourseId(Connection con, int courseId)
         throws SQLException
   {
      String name = null;
      try
      {
         // MySQL 4.0 (4.1 will) does not support subquery, use inner join instead
         String query = "SELECT " + TABLE_NAME + "." + COL_NAME + " FROM "
               + TABLE_NAME + "," + CourseManager.TABLE_NAME + " WHERE "
               + TABLE_NAME + "." + COL_CLASS_ID + "="
               + CourseManager.TABLE_NAME + "." + CourseManager.COL_CLASS_ID
               + " AND " + CourseManager.TABLE_NAME + "."
               + CourseManager.COL_COURSE_ID + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setInt(1, courseId);
         ResultSet rs = pstmt.executeQuery();
         if(rs.next())
         {
            name = rs.getString(1);
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return name;
   }
   public int insert(Connection con, Class cl) throws SQLException
   {
      int result = -1;
      try
      {
         if(cl == null)
            return result;
         String query = "INSERT INTO " + TABLE_NAME + " (" + COL_NAME + ","
               + COL_CATEGORY + "," + COL_DESCRIPTION + "," + COL_MAX_STUDENTS
               + ") VALUES (?,?,?,?)";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setString(1, cl.getName());
         pstmt.setInt(2, cl.getCategory());
         pstmt.setString(3, cl.getDescription());
         pstmt.setInt(4, cl.getMaxStudents());
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   public int update(Connection con, Class cl) throws SQLException
   {
      int result = -1;
      try
      {
         if(cl == null)
            return result;
         String query = "UPDATE " + TABLE_NAME + " SET " + COL_NAME + "=?,"
               + COL_CATEGORY + "=?," + COL_DESCRIPTION + "=?,"
               + COL_MAX_STUDENTS + "=?" + " WHERE " + COL_CLASS_ID + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setString(1, cl.getName());
         pstmt.setInt(2, cl.getCategory());
         pstmt.setString(3, cl.getDescription());
         pstmt.setInt(4, cl.getMaxStudents());
         pstmt.setInt(5, cl.getClassId());
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   public int delete(Connection con, int classId) throws SQLException
   {
      int result = -1;
      try
      {
         // check if current class ID is referenced by others
         String query = "SELECT " + CourseManager.COL_COURSE_ID + " FROM "
               + CourseManager.TABLE_NAME + " WHERE "
               + CourseManager.COL_CLASS_ID + "=? LIMIT 1";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setInt(1, classId);
         ResultSet rs = pstmt.executeQuery();
         if(rs.next())
            throw new SQLException("Can not delete it. Class ID " + classId
                  + " is referenced in table course.");
         // only class ID not referenced by others can be deleted
         query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_CLASS_ID + "=?";
         pstmt = con.prepareStatement(query);
         pstmt.setInt(1, classId);
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   private Class getClass(ResultSet rs) throws SQLException
   {
      Class cl = new Class();
      cl.setClassId(rs.getInt(COL_CLASS_ID));
      cl.setName(rs.getString(COL_NAME));
      cl.setCategory(rs.getInt(COL_CATEGORY));
      cl.setDescription(rs.getString(COL_DESCRIPTION));
      cl.setMaxStudents(rs.getInt(COL_MAX_STUDENTS));
      return cl;
   }
}
