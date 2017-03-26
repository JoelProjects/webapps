package datamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;
import java.util.Vector;

public class GroupManager
{
   public static String TABLE_NAME = "tzuchi_group";
   public static String COL_GROUP_ID = "group_id";
   public static String COL_GROUP_NAME = "group_name";
   public static String COL_PARENT_ID = "parent_id";
   public static String COL_LEVEL_ID = "level_id";

   public Vector getAllData(Connection con) throws SQLException
   {
      Vector groupList = new Vector();

      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY "
               + COL_LEVEL_ID + "," + COL_GROUP_NAME;
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query);

         while(rs.next())
         {
            groupList.add(getGroup(rs));
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }

      return groupList;
   }

   // gets groups with hierarchical names 
   public Vector getLeveledData(Connection con) throws SQLException
   {
      Vector vect = new Vector(); // to store results

      try
      {
         Statement stmt = con.createStatement();

         // get top level goups
         String query = "SELECT " + COL_GROUP_ID + "," + COL_GROUP_NAME
               + " FROM " + TABLE_NAME + " WHERE " + COL_LEVEL_ID + "=0"
               + " ORDER BY " + COL_GROUP_NAME + " DESC";
         ResultSet rs = stmt.executeQuery(query);

         Stack stack = new Stack(); // to store groups to be processed
         while(rs.next())
         {
            Group group = new Group();
            group.setGroupId(rs.getInt(COL_GROUP_ID));
            group.setGroupName(rs.getString(COL_GROUP_NAME));
            stack.push(group);
         }
         rs.close();
         stmt.close();

         // go through items in the stack
         query = "SELECT " + COL_GROUP_ID + "," + COL_GROUP_NAME + " FROM "
               + TABLE_NAME + " WHERE " + COL_PARENT_ID + "=? ORDER BY "
               + COL_GROUP_NAME + " DESC";
         PreparedStatement pstmt = con.prepareStatement(query);
         String name;
         while(!stack.empty())
         {
            Group group = (Group)stack.pop();
            vect.add(group);

            // get sub-groups
            name = group.getGroupName();
            pstmt.setInt(1, group.getGroupId());
            rs = pstmt.executeQuery();

            while(rs.next())
            {
               group = new Group();
               group.setGroupId(rs.getInt(COL_GROUP_ID));
               // merge names
               group.setGroupName(name + " > " + rs.getString(COL_GROUP_NAME));
               stack.push(group);
            }
         }
      }
      catch(Exception e)
      {
         throw new SQLException(e.toString());
      }

      return vect;
   }

   public int insert(Connection con, Group group) throws SQLException
   {
      int result = -1;

      try
      {
         if(group == null)
            return result;

         String query = "INSERT INTO " + TABLE_NAME + " (" + COL_GROUP_NAME
               + "," + COL_PARENT_ID + "," + COL_LEVEL_ID + ") VALUES (?,?,?)";

         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setString(1, group.getGroupName());
         pstmt.setInt(2, group.getParentId());
         pstmt.setInt(3, group.getLevelId());

         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }

      return result;
   }

   public int update(Connection con, Group group) throws SQLException
   {
      int result = -1;

      try
      {
         if(group == null)
            return result;

         String query = "UPDATE " + TABLE_NAME + " SET " + COL_GROUP_NAME
               + "=?" + " WHERE " + COL_GROUP_ID + "=?";

         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setString(1, group.getGroupName());
         pstmt.setInt(2, group.getGroupId());

         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }

      return result;
   }

   public int delete(Connection con, int groupId) throws SQLException
   {
      int result = -1;

      try
      {
         // check if current group ID is referenced by others
         String query = "SELECT " + PersonGroupManager.COL_PERSON_ID + " FROM "
               + PersonGroupManager.TABLE_NAME + " WHERE "
               + PersonGroupManager.COL_GROUP_ID + "=? LIMIT 1";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setInt(1, groupId);
         ResultSet rs = pstmt.executeQuery();

         if(rs.next())
            throw new SQLException("Can not delete it. This group (ID="
                  + groupId + ") has group member(s).");

         // only group ID not referenced by other course can be deleted
         query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_GROUP_ID + "=?";

         pstmt = con.prepareStatement(query);
         pstmt.setInt(1, groupId);

         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }

      return result;
   }

   private Group getGroup(ResultSet rs) throws SQLException
   {
      Group group = new Group();
      group.setGroupId(rs.getInt(COL_GROUP_ID));
      group.setGroupName(rs.getString(COL_GROUP_NAME));
      group.setParentId(rs.getInt(COL_PARENT_ID));
      group.setLevelId(rs.getInt(COL_LEVEL_ID));

      return group;
   }
}
