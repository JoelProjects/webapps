package datamodel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
public class PersonInfoManager
{
   public static String TABLE_NAME = "person_info";
   public static String COL_PERSON_ID = "person_id";
   public static String COL_CHINESE_NAME = "chinese_name";
   public static String COL_FIRST_NAME = "first_name";
   public static String COL_LAST_NAME = "last_name";
   public static String COL_STREET = "street";
   public static String COL_CITY = "city";
   public static String COL_STATE = "state";
   public static String COL_ZIP = "zip";
   public static String COL_EMAIL = "email";
   public static String COL_HOME_PHONE = "home_phone";
   public static String COL_WORK_PHONE = "work_phone";
   public static String COL_CELL_PHONE = "cell_phone";
   public PersonInfo getDataById(Connection con, int personId)
         throws SQLException
   {
      PersonInfo person = null;
      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
               + COL_PERSON_ID + "=" + personId;
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         if(rs.next())
         {
            person = getPersonInfo(rs);
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return person;
   }
   public Vector getDataByGroupId(Connection con, int groupId)
         throws SQLException
   {
      Vector persons = new Vector();
      try
      {
         String query = "SELECT " + TABLE_NAME + ".* FROM " + TABLE_NAME + ","
               + PersonGroupManager.TABLE_NAME +
               " WHERE " + TABLE_NAME + "." + COL_PERSON_ID + "=" +
               PersonGroupManager.TABLE_NAME + "."
               + PersonGroupManager.COL_PERSON_ID + " AND " +
               PersonGroupManager.TABLE_NAME + "."
               + PersonGroupManager.COL_GROUP_ID + "=" + groupId +
               " ORDER BY " + PersonGroupManager.COL_PERSON_TYPE + " DESC, "
               + COL_CHINESE_NAME + "," + COL_LAST_NAME
               + "," + COL_FIRST_NAME;
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         while(rs.next())
         {
            persons.add(getPersonInfo(rs));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return persons;
   }
   public Vector getAllData(Connection con) throws SQLException
   {
      Vector persons = new Vector();
      try
      {
         String query = "SELECT * FROM " + TABLE_NAME +
         " ORDER BY " + COL_CHINESE_NAME + "," + COL_LAST_NAME + ","
               + COL_FIRST_NAME;
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         while(rs.next())
         {
            persons.add(getPersonInfo(rs));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return persons;
   }
   synchronized public int insert(Connection con, PersonInfo person)
         throws SQLException
   {
      int result = -1;
      try
      {
         if(person == null)
            return result;
         String query = "INSERT INTO " + TABLE_NAME + " (" +
         COL_CHINESE_NAME + "," +
         COL_FIRST_NAME + "," +
         COL_LAST_NAME + "," +
         COL_STREET + "," +
         COL_CITY + "," +
         COL_STATE + "," +
         COL_ZIP + "," +
         COL_EMAIL + "," +
         COL_HOME_PHONE + "," +
         COL_WORK_PHONE + "," +
         COL_CELL_PHONE +
         ") VALUES (?,?,?,?,?,?,?,?,?,?,?)";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setString(1, person.getChineseName());
         pstmt.setString(2, person.getFirstName());
         pstmt.setString(3, person.getLastName());
         pstmt.setString(4, person.getStreet());
         pstmt.setString(5, person.getCity());
         pstmt.setString(6, person.getState());
         pstmt.setString(7, person.getZip());
         pstmt.setString(8, person.getEmail());
         pstmt.setString(9, person.getHomePhone());
         pstmt.setString(10, person.getWorkPhone());
         pstmt.setString(11, person.getCellPhone());
         result = pstmt.executeUpdate();
         // the the most recent person ID
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery("SELECT @@IDENTITY");
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
   public int update(Connection con, PersonInfo person) throws SQLException
   {
      int result = -1;
      try
      {
         if(person == null)
            return result;
         String query = "UPDATE " + TABLE_NAME + " SET " +
         COL_CHINESE_NAME + "=?," +
         COL_FIRST_NAME + "=?," +
         COL_LAST_NAME + "=?," +
         COL_STREET + "=?," +
         COL_CITY + "=?," +
         COL_STATE + "=?," +
         COL_ZIP + "=?," +
         COL_EMAIL + "=?," +
         COL_HOME_PHONE + "=?," +
         COL_WORK_PHONE + "=?," +
         COL_CELL_PHONE + "=?" +
         " WHERE " +
         COL_PERSON_ID + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setString(1, person.getChineseName());
         pstmt.setString(2, person.getFirstName());
         pstmt.setString(3, person.getLastName());
         pstmt.setString(4, person.getStreet());
         pstmt.setString(5, person.getCity());
         pstmt.setString(6, person.getState());
         pstmt.setString(7, person.getZip());
         pstmt.setString(8, person.getEmail());
         pstmt.setString(9, person.getHomePhone());
         pstmt.setString(10, person.getWorkPhone());
         pstmt.setString(11, person.getCellPhone());
         pstmt.setInt(12, person.getPersonId());
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   public int delete(Connection con, int personId) throws SQLException
   {
      int result = -1;
      try
      {
         String query = "DELETE FROM " + TABLE_NAME +
         " WHERE " +
         COL_PERSON_ID + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setInt(1, personId);
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   private PersonInfo getPersonInfo(ResultSet rs) throws SQLException
   {
      PersonInfo person = new PersonInfo();
      person.setPersonId(rs.getInt(COL_PERSON_ID));
      person.setChineseName(rs.getString(COL_CHINESE_NAME));
      person.setFirstName(rs.getString(COL_FIRST_NAME));
      person.setLastName(rs.getString(COL_LAST_NAME));
      person.setStreet(rs.getString(COL_STREET));
      person.setCity(rs.getString(COL_CITY));
      person.setState(rs.getString(COL_STATE));
      person.setZip(rs.getString(COL_ZIP));
      person.setEmail(rs.getString(COL_EMAIL));
      person.setHomePhone(rs.getString(COL_HOME_PHONE));
      person.setWorkPhone(rs.getString(COL_WORK_PHONE));
      person.setCellPhone(rs.getString(COL_CELL_PHONE));
      return person;
   }
}
