package datamodel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import util.Utility;
public class StudentManager
{
   public static String TABLE_NAME = "student";
   public static String COL_STUDENT_ID = "student_id";
   public static String COL_FAMILY_ID = "family_id";
   public static String COL_FIRST_NAME = "first_name";
   public static String COL_LAST_NAME = "last_name";
   public static String COL_CHINESE_NAME = "chinese_name";
   public static String COL_GENDER = "gender";
   public static String COL_BIRTH_DATE = "birth_date";
   public static String COL_DAY_TIME_SCHOOL = "day_time_school";
   public static String COL_GRADE_OF_SCHOOL = "grade_of_school";
   public static String COL_SUMMER_UNIFORM_SIZE = "summer_uniform_size";
   public static String COL_SUMMER_UNIFORM_RECEIVED = "summer_uniform_received";
   public static String COL_WINTER_UNIFORM_SIZE = "winter_uniform_size";
   public static String COL_WINTER_UNIFORM_RECEIVED = "winter_uniform_received";
   public static String COL_HEALTH_PROBLEM = "health_problem";
   public static String MALE = "M";
   public static String FEMALE = "F";
   public Student getDataById(Connection con, long studentId)
         throws SQLException
   {
      Student st = null;
      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
               + COL_STUDENT_ID + "=" + studentId;
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         if(rs.next())
         {
            st = getStudent(rs);
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return st;
   }
   /**
    * Gets all students in a family.
    */
   public Vector getDataByFamily(Connection con, long familyId)
         throws SQLException
   {
      Vector students = new Vector();
      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
               + COL_FAMILY_ID + "=" + familyId;
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         while(rs.next())
         {
            students.add(getStudent(rs));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return students;
   }
   public Vector search(Connection con, String condition) throws SQLException
   {
      Vector students = new Vector();
      try
      {
         String query = "SELECT * FROM " + TABLE_NAME;
         if(!Utility.isEmpty(condition))
            query = query + " WHERE " + condition;
         query = query + " ORDER BY " + COL_FIRST_NAME + "," + COL_LAST_NAME
               + "," + COL_CHINESE_NAME;
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         while(rs.next())
         {
            students.add(getStudent(rs));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return students;
   }
   public Vector search(Connection con, int courseId, int year, String semester) 
      throws SQLException
   {
      Vector students = new Vector();
      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_STUDENT_ID + " IN " 
            + "(SELECT " + StudentRegManager.COL_STUDENT_ID + " FROM " + StudentRegManager.TABLE_NAME
            + " WHERE " + StudentRegManager.COL_COURSE_ID + "=" + courseId + ")"
            + " ORDER BY " + COL_FIRST_NAME + "," + COL_LAST_NAME
            + "," + COL_CHINESE_NAME;
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         while(rs.next())
         {
            students.add(getStudent(rs));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return students;
   }   
   
   public int insert(Connection con, Student st) throws SQLException
   {
      int result = -1;
      try
      {
         if(st == null)
            return result;
         String query = "INSERT INTO " + TABLE_NAME + " (" + COL_STUDENT_ID
               + "," + COL_FAMILY_ID + "," + COL_FIRST_NAME + ","
               + COL_LAST_NAME + "," + COL_CHINESE_NAME + "," + COL_GENDER
               + "," + COL_BIRTH_DATE + "," + COL_DAY_TIME_SCHOOL + ","
               + COL_HEALTH_PROBLEM + ") VALUES (?,?,?,?,?,?,?,?,?)";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setLong(1, st.getStudentId());
         pstmt.setLong(2, st.getFamilyId());
         pstmt.setString(3, st.getFirstName());
         pstmt.setString(4, st.getLastName());
         pstmt.setString(5, st.getChineseName());
         pstmt.setString(6, st.getGender());
         pstmt.setDate(7, (java.sql.Date)st.getBirthDate());
         pstmt.setString(8, st.getDayTimeSchool());
         pstmt.setString(9, st.getHealthProblem());
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   public int update(Connection con, Student st) throws SQLException
   {
      int result = -1;
      try
      {
         if(st == null)
            return result;
         String query = "UPDATE " + TABLE_NAME + " SET " + COL_FIRST_NAME
               + "=?," + COL_LAST_NAME + "=?," + COL_CHINESE_NAME + "=?,"
               + COL_GENDER + "=?," + COL_BIRTH_DATE + "=?,"
               + COL_DAY_TIME_SCHOOL + "=?," + COL_HEALTH_PROBLEM + "=?"
               + " WHERE " + COL_STUDENT_ID + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setString(1, st.getFirstName());
         pstmt.setString(2, st.getLastName());
         pstmt.setString(3, st.getChineseName());
         pstmt.setString(4, st.getGender());
         pstmt.setDate(5, (java.sql.Date)st.getBirthDate());
         pstmt.setString(6, st.getDayTimeSchool());
         pstmt.setString(7, st.getHealthProblem());
         pstmt.setLong(8, st.getStudentId());
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   public int updateUniform(Connection con, long studentId, String summerSize,
         boolean isSummerReceived, String winterSize, boolean isWinterReceived)
         throws SQLException
   {
      int result = -1;
      int summerReceived = 0;
      if(isSummerReceived)
         summerReceived = 1;
      int winterReceived = 0;
      if(isWinterReceived)
         winterReceived = 1;
      try
      {
         String query = "UPDATE " + TABLE_NAME + " SET "
               + COL_SUMMER_UNIFORM_SIZE + "=?," + COL_SUMMER_UNIFORM_RECEIVED
               + "=?," + COL_WINTER_UNIFORM_SIZE + "=?,"
               + COL_WINTER_UNIFORM_RECEIVED + "=?" + " WHERE "
               + COL_STUDENT_ID + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setString(1, summerSize);
         pstmt.setInt(2, summerReceived);
         pstmt.setString(3, winterSize);
         pstmt.setInt(4, winterReceived);
         pstmt.setLong(5, studentId);
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   public int updateGradeOfSchool(Connection con, long studentId,
         int gradeOfSchool) throws SQLException
   {
      int result = -1;
      try
      {
         String query = "UPDATE " + TABLE_NAME + " SET " + COL_GRADE_OF_SCHOOL
               + "=?" + " WHERE " + COL_STUDENT_ID + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setInt(1, gradeOfSchool);
         pstmt.setLong(2, studentId);
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   public int delete(Connection con, long studentId) throws SQLException
   {
      int result = -1;
      try
      {
         // check if current student ID is referenced by others
         String query = "SELECT " + StudentRegManager.COL_COURSE_ID + " FROM "
               + StudentRegManager.TABLE_NAME + " WHERE "
               + StudentRegManager.COL_STUDENT_ID + "=? LIMIT 1";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setLong(1, studentId);
         ResultSet rs = pstmt.executeQuery();
         if(rs.next())
            throw new SQLException("Can not delete it. Student ID " + studentId
                  + " is referenced in table student_reg.");
         // only student ID not referenced by other course can be deleted
         query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_STUDENT_ID
               + "=?";
         pstmt = con.prepareStatement(query);
         pstmt.setLong(1, studentId);
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   public int deleteByFamily(Connection con, long familyId) throws SQLException
   {
      int result = -1;
      try
      {
         String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_FAMILY_ID
               + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setLong(1, familyId);
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   private Student getStudent(ResultSet rs) throws SQLException
   {
      Student st = new Student();
      st.setStudentId(rs.getLong(COL_STUDENT_ID));
      st.setFamilyId(rs.getLong(COL_FAMILY_ID));
      st.setFirstName(rs.getString(COL_FIRST_NAME));
      st.setLastName(rs.getString(COL_LAST_NAME));
      st.setChineseName(rs.getString(COL_CHINESE_NAME));
      st.setGender(rs.getString(COL_GENDER));
      st.setBirthDate(rs.getDate(COL_BIRTH_DATE));
      st.setDayTimeSchool(rs.getString(COL_DAY_TIME_SCHOOL));
      st.setGradeOfSchool(rs.getInt(COL_GRADE_OF_SCHOOL));
      st.setSummerUniformSize(rs.getString(COL_SUMMER_UNIFORM_SIZE));
      if(rs.getInt(COL_SUMMER_UNIFORM_RECEIVED) == 1)
         st.setSummerUniformReceived(true);
      else
         st.setSummerUniformReceived(false);
      st.setWinterUniformSize(rs.getString(COL_WINTER_UNIFORM_SIZE));
      if(rs.getInt(COL_WINTER_UNIFORM_RECEIVED) == 1)
         st.setWinterUniformReceived(true);
      else
         st.setWinterUniformReceived(false);
      st.setHealthProblem(rs.getString(COL_HEALTH_PROBLEM));
      return st;
   }
}
