package datamodel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class FamilyInfoManager
{
   public static String TABLE_NAME = "family_info";
   public static String COL_FAMILY_ID = "family_id";
   public static String COL_GUARDIAN1 = "guardian1";
   public static String COL_GUARDIAN2 = "guardian2";
   public static String COL_STREET = "street";
   public static String COL_APT = "apt";
   public static String COL_CITY = "city";
   public static String COL_STATE = "state";
   public static String COL_ZIP = "zip";
   public static String COL_AREA_CODE = "area_code";
   public static String COL_PHONE = "phone";
   public static String COL_EMAIL = "email";
   public static String COL_INSURE_CO = "insure_co";
   public static String COL_FAMILY_DOC = "family_doc";
   public static String COL_EMERGENCY1 = "emergency1";
   public static String COL_EMERGENCY2 = "emergency2";
   // for relation ID in table relative
   public static int GUARDIAN1_ID = 1;
   public static int GUARDIAN2_ID = 2;
   public static int FAMILY_DOC_ID = 3;
   public static int EMERGENCY1_ID = 4;
   public static int EMERGENCY2_ID = 5;
   public FamilyInfo getDataById(Connection con, long familyId)
         throws SQLException
   {
      FamilyInfo family = null;
      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
               + COL_FAMILY_ID + "=" + familyId;
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);
         if(rs.next())
         {
            family = getFamilyInfo(rs);
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return family;
   }
   public int insert(Connection con, FamilyInfo family) throws SQLException
   {
      int result = -1;
      try
      {
         if(family == null)
            return result;
         String query = "INSERT INTO " + TABLE_NAME + " (" + COL_FAMILY_ID
               + "," + COL_STREET + "," + COL_APT + "," + COL_CITY + ","
               + COL_STATE + "," + COL_ZIP + "," + COL_AREA_CODE + ","
               + COL_PHONE + "," + COL_EMAIL + "," + COL_INSURE_CO
               + ") VALUES (?,?,?,?,?,?,?,?,?,?)";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setLong(1, family.getFamilyId());
         pstmt.setString(2, family.getStreet());
         pstmt.setString(3, family.getApt());
         pstmt.setString(4, family.getCity());
         pstmt.setString(5, family.getState());
         pstmt.setString(6, family.getZip());
         pstmt.setString(7, family.getAreaCode());
         pstmt.setString(8, family.getPhone());
         pstmt.setString(9, family.getEmail());
         pstmt.setString(10, family.getInsureCo());
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   public int update(Connection con, FamilyInfo family) throws SQLException
   {
      int result = -1;
      try
      {
         if(family == null)
            return result;
         String query = "UPDATE " + TABLE_NAME + " SET " + COL_STREET + "=?,"
               + COL_APT + "=?," + COL_CITY + "=?," + COL_STATE + "=?,"
               + COL_ZIP + "=?," + COL_AREA_CODE + "=?," + COL_PHONE + "=?,"
               + COL_EMAIL + "=?," + COL_INSURE_CO + "=?" + " WHERE "
               + COL_FAMILY_ID + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setString(1, family.getStreet());
         pstmt.setString(2, family.getApt());
         pstmt.setString(3, family.getCity());
         pstmt.setString(4, family.getState());
         pstmt.setString(5, family.getZip());
         pstmt.setString(6, family.getAreaCode());
         pstmt.setString(7, family.getPhone());
         pstmt.setString(8, family.getEmail());
         pstmt.setString(9, family.getInsureCo());
         pstmt.setLong(10, family.getFamilyId());
         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   public int delete(Connection con, long familyId) throws SQLException
   {
      int result = -1;
      try
      {
         // check if current family ID is referenced by others
         String query = "SELECT " + StudentManager.COL_STUDENT_ID + " FROM "
               + StudentManager.TABLE_NAME + " WHERE "
               + StudentManager.COL_FAMILY_ID + "=? LIMIT 1";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setLong(1, familyId);
         ResultSet rs = pstmt.executeQuery();
         if(rs.next())
            throw new SQLException("Can not delete it. Family ID " + familyId
                  + " is referenced in table student.");
         // only family ID not referenced by other student can be deleted
         query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_FAMILY_ID + "=?";
         pstmt = con.prepareStatement(query);
         pstmt.setLong(1, familyId);
         result = pstmt.executeUpdate();
         // delete relatives 
         RelativeManager relativeMgr = new RelativeManager();
         relativeMgr.delete(con, familyId);
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }
      return result;
   }
   private FamilyInfo getFamilyInfo(ResultSet rs) throws SQLException
   {
      FamilyInfo family = new FamilyInfo();
      family.setFamilyId(rs.getLong(COL_FAMILY_ID));
      family.setGuardian1(rs.getInt(COL_GUARDIAN1));
      family.setGuardian2(rs.getInt(COL_GUARDIAN2));
      family.setStreet(rs.getString(COL_STREET));
      family.setApt(rs.getString(COL_APT));
      family.setCity(rs.getString(COL_CITY));
      family.setState(rs.getString(COL_STATE));
      family.setZip(rs.getString(COL_ZIP));
      family.setAreaCode(rs.getString(COL_AREA_CODE));
      family.setPhone(rs.getString(COL_PHONE));
      family.setEmail(rs.getString(COL_EMAIL));
      family.setInsureCo(rs.getString(COL_INSURE_CO));
      family.setFamilyDoc(rs.getInt(COL_FAMILY_DOC));
      family.setEmergency1(rs.getInt(COL_EMERGENCY1));
      family.setEmergency2(rs.getInt(COL_EMERGENCY2));
      return family;
   }
}
