package datamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class PersonGroupManager
{
   public static String TABLE_NAME = "person_group";
   public static String COL_PERSON_ID = "person_id";
   public static String COL_GROUP_ID = "group_id";
   public static String COL_PERSON_TYPE = "person_type";

   public static int GROUP_MEMBER = 0;
   public static int GROUP_LEADER = 1;

   public Vector getDataByPersonId(Connection con, int personId)
         throws SQLException
   {
      Vector groupList = new Vector();

      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
               + COL_PERSON_ID + "=" + personId;
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);

         while(rs.next())
         {
            groupList.add(getPersonGroup(rs));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }

      return groupList;
   }

   public boolean isGroupLeader(Connection con, int personId, int groupId)
         throws SQLException
   {
      boolean isGroupLeader = false;

      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
               + COL_PERSON_ID + "=? AND " + COL_GROUP_ID + "=? AND "
               + COL_PERSON_TYPE + "=? LIMIT 1";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setInt(1, personId);
         pstmt.setInt(2, groupId);
         pstmt.setInt(3, GROUP_LEADER);
         ResultSet rs = pstmt.executeQuery();

         if(rs.next())
         {
            isGroupLeader = true;
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }

      return isGroupLeader;
   }

   public int insert(Connection con, PersonGroup group) throws SQLException
   {
      int result = -1;

      try
      {
         if(group == null)
            return result;

         String query = "INSERT INTO " + TABLE_NAME + " (" + COL_PERSON_ID
               + "," + COL_GROUP_ID + "," + COL_PERSON_TYPE
               + ") VALUES (?,?,?)";

         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setInt(1, group.getPersonId());
         pstmt.setInt(2, group.getGroupId());
         pstmt.setInt(3, group.getPersonType());

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
         String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_PERSON_ID
               + "=?";

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

   private PersonGroup getPersonGroup(ResultSet rs) throws SQLException
   {
      PersonGroup group = new PersonGroup();
      group.setPersonId(rs.getInt(COL_PERSON_ID));
      group.setGroupId(rs.getInt(COL_GROUP_ID));
      group.setPersonType(rs.getInt(COL_PERSON_TYPE));

      return group;
   }
}
