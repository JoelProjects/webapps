package datamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import util.Utility;

public class RelativeManager
{
  public static String TABLE_NAME = "relative";
  public static String COL_FAMILY_ID = "family_id";
  public static String COL_FIRST_NAME = "first_name";
  public static String COL_LAST_NAME = "last_name";
  public static String COL_CHINESE_NAME = "chinese_name";
  public static String COL_WORK_PHONE = "work_phone";
  public static String COL_RELATION_ID = "relation_id";
  public static String COL_RELATION_NAME = "relation_name";
  public static String COL_MOBILE_PHONE = "mobile_phone";

  public Relative getDataById(Connection con, long familyId, int relationId)
    throws SQLException
  {
    Relative rel = null;

    try
    {
      String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_FAMILY_ID + "=" + familyId +
                     " AND " + COL_RELATION_ID + "=" + relationId;
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(query);

      if(rs.next())
      {
        rel = getRelative(rs);
      }
    }
    catch(SQLException e)
    {
      throw new SQLException(e.toString());
    }

    return rel;
  }

  public Vector searchByGuardian(Connection con, String condition)
    throws SQLException
  {
    Vector relatives = new Vector();

    try
    {
      String query = "SELECT * FROM " + TABLE_NAME +
                     " WHERE (" + COL_RELATION_ID + "=" + FamilyInfoManager.GUARDIAN1_ID +
                     " OR " + COL_RELATION_ID + "=" + FamilyInfoManager.GUARDIAN2_ID + ")";

      if(!Utility.isEmpty(condition))
         query = query + " AND " + condition;

      query = query + " ORDER BY " + COL_FIRST_NAME + "," + COL_LAST_NAME +
        "," + COL_CHINESE_NAME;

      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(query);

      while(rs.next())
      {
        relatives.add(getRelative(rs));
      }
    }
    catch(SQLException e)
    {
      throw new SQLException(e.toString());
    }

    return relatives;
  }

  public int insert(Connection con, Relative rel) throws SQLException
  {
    int result = -1;

    try
    {
      if(rel == null)
        return result;

      String query = "INSERT INTO " + TABLE_NAME + " (" +
                     COL_FAMILY_ID + "," +
                     COL_FIRST_NAME + "," +
                     COL_LAST_NAME + "," +
                     COL_CHINESE_NAME + "," +
                     COL_WORK_PHONE + "," +
                     COL_RELATION_ID + "," +
                     COL_RELATION_NAME + "," +
                     COL_MOBILE_PHONE +
                     ") VALUES (?,?,?,?,?,?,?,?)";

      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setLong(1, rel.getFamilyId());
      pstmt.setString(2, rel.getFirstName());
      pstmt.setString(3, rel.getLastName());
      pstmt.setString(4, rel.getChineseName());
      pstmt.setString(5, rel.getWorkPhone());
      pstmt.setInt(6, rel.getRelationId());
      pstmt.setString(7, rel.getRelationName());
      pstmt.setString(8, rel.getMobilePhone());

      result = pstmt.executeUpdate();
    }
    catch(SQLException e)
    {
      throw new SQLException(e.toString());
    }

    return result;
  }

  public int update(Connection con, Relative rel) throws SQLException
  {
    int result = -1;

    try
    {
      if(rel == null)
        return result;

      String query = "UPDATE " + TABLE_NAME + " SET " +
                     COL_FIRST_NAME + "=?," +
                     COL_LAST_NAME + "=?," +
                     COL_CHINESE_NAME + "=?," +
                     COL_WORK_PHONE + "=?," +
                     COL_MOBILE_PHONE + "=?," +
                     COL_RELATION_NAME + "=?" +
                     " WHERE " +
                     COL_FAMILY_ID + "=? AND " + COL_RELATION_ID + "=?";

      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setString(1, rel.getFirstName());
      pstmt.setString(2, rel.getLastName());
      pstmt.setString(3, rel.getChineseName());
      pstmt.setString(4, rel.getWorkPhone());
      pstmt.setString(5, rel.getMobilePhone());
      pstmt.setString(6, rel.getRelationName());
      pstmt.setLong(7, rel.getFamilyId());
      pstmt.setInt(8, rel.getRelationId());

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
      String query = "DELETE FROM " + TABLE_NAME +
                     " WHERE " +
                     COL_FAMILY_ID + "=?";

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

  private Relative getRelative(ResultSet rs) throws SQLException
  {
    Relative rel = new Relative();
    rel.setFamilyId(rs.getLong(COL_FAMILY_ID));
    rel.setFirstName(rs.getString(COL_FIRST_NAME));
    rel.setLastName(rs.getString(COL_LAST_NAME));
    rel.setChineseName(rs.getString(COL_CHINESE_NAME));
    rel.setWorkPhone(rs.getString(COL_WORK_PHONE));
    rel.setRelationId(rs.getInt(COL_RELATION_ID));
    rel.setRelationName(rs.getString(COL_RELATION_NAME));
    rel.setMobilePhone(rs.getString(COL_MOBILE_PHONE));

    return rel;
  }
}
