package datamodel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class SchoolStatusManager
{
   public static String TABLE_NAME = "school_status";
   public static String COL_YEAR = "year";
   public static String COL_SEMESTER = "semester";
   public SchoolStatus getStatus(Connection con) throws SQLException
   {
      SchoolStatus status = null;
      try
      {
         String query = "SELECT * FROM " + TABLE_NAME;
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         if(rs.next())
         {
            status = getSchoolStatus(rs);
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return status;
   }
   public int insert(Connection con, SchoolStatus status) throws SQLException
   {
      int result = -1;
      try
      {
         if(status == null)
            return result;
         String query = "INSERT INTO " + TABLE_NAME + " (" + COL_YEAR + ","
               + COL_SEMESTER + ") VALUES (?,?)";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setInt(1, status.getYear());
         pstmt.setString(2, status.getSemester());
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   public int update(Connection con, SchoolStatus status) throws SQLException
   {
      int result = -1;
      try
      {
         if(status == null)
            return result;
         String query = "UPDATE " + TABLE_NAME + " SET " + COL_YEAR + "=?,"
               + COL_SEMESTER + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setInt(1, status.getYear());
         pstmt.setString(2, status.getSemester());
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   private SchoolStatus getSchoolStatus(ResultSet rs) throws SQLException
   {
      SchoolStatus status = new SchoolStatus();
      status.setYear(rs.getInt(COL_YEAR));
      status.setSemester(rs.getString(COL_SEMESTER));
      return status;
   }
}
