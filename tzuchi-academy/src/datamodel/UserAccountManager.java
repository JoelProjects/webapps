package datamodel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * This is the manager class for table representing user account in the database.
 */
public class UserAccountManager
{
   public static String TABLE_NAME = "user_account";
   public static String COL_USERNAME = "username";
   public static String COL_PASSWORD = "password";
   public static String COL_USER_ID = "user_id";
   public static String COL_ROLE = "role";
   /**
    * Gets user info by providing user name. An UserAccount object will be returned.
    *
    * @param con a database connection
    * @param name the user name
    * @return user account
    */
   public UserAccount getUserByName(Connection con, String name)
         throws SQLException
   {
      UserAccount user = null;
      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
               + COL_USERNAME + "='" + name + "'";
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         if(rs.next())
         {
            user = new UserAccount();
            user.setUserId(rs.getLong(COL_USER_ID));
            user.setUsername(rs.getString(COL_USERNAME));
            user.setRole(rs.getString(COL_ROLE));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return user;
   }
   /**
    * Gets user name by providing user ID.
    *
    * @param con a database connection
    * @param userId the user ID
    * @return user name
    */
   public String getNameById(Connection con, long userId) throws SQLException
   {
      String username = null;
      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_USER_ID
               + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setLong(1, userId);
         ResultSet rs = pstmt.executeQuery();
         if(rs.next())
         {
            username = rs.getString(COL_USERNAME);
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return username;
   }
   /**
    * Inserts a new record into the table by providing an UserAccount object.
    * 
    * @param con a database connection
    * @param user an User object to be inserted
    * @return the row count
    */
   public int insert(Connection con, UserAccount user) throws SQLException
   {
      int result = -1;
      try
      {
         if(user == null)
            return result;
         String query = "INSERT INTO " + TABLE_NAME + " (" + COL_USERNAME + ","
               + COL_PASSWORD + "," + COL_USER_ID + "," + COL_ROLE
               + ") VALUES (?,?,?,?)";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setString(1, user.getUsername());
         pstmt.setString(2, user.getPassword());
         pstmt.setLong(3, user.getUserId());
         pstmt.setString(4, user.getRole());
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   /**
    * Updates user password by providing user ID.
    *
    * @param con a database connection
    * @param userId user ID
    * @param password new password
    * @return the row count
    */
   public int updatePassword(Connection con, long userId, String password)
         throws SQLException
   {
      int result = -1;
      try
      {
         String query = "UPDATE " + TABLE_NAME + " SET " + COL_PASSWORD + "=?"
               + " WHERE " + COL_USER_ID + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setString(1, password);
         pstmt.setLong(2, userId);
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   /**
    * Checks if user account exists by providing user name.
    *
    * @param con a database connection
    * @param username the user name to be checked
    * @return true if user exists
    */
   public boolean exists(Connection con, String username) throws SQLException
   {
      if(getUserId(con, username) > -1)
         return true;
      else
         return false;
   }
   /**
    * Gets user ID.
    *
    * @param con a database connection
    * @param username the user name to be checked
    * @return true if user exists
    */
   public long getUserId(Connection con, String username) throws SQLException
   {
      long result = -1;
      try
      {
         String query = "SELECT " + COL_USER_ID + " FROM " + TABLE_NAME
               + " WHERE " + COL_USERNAME + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setString(1, username);
         ResultSet rs = pstmt.executeQuery();
         if(rs.next())
            result = rs.getLong(1);
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
}
