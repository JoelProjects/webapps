package jsp;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Vector;

import datamodel.Group;
import datamodel.GroupManager;

public class GroupManagementBean
{
   public String getGroupObjects(Connection con)
   {
      Vector groups = new Vector();

      try
      {
         GroupManager groupMgr = new GroupManager();
         groups = groupMgr.getAllData(con);
      }
      catch(SQLException e)
      {
         System.out.println(e);
      }

      Object[] args;
      String pattern = "groupList[{0}][{1}] = new Group({2},\"{3}\");";
      StringBuffer groupList = new StringBuffer();
      for(int i = 0; i < groups.size(); i++)
      {
         Group group = (Group)groups.get(i);
         args = new Object[]
         {new Integer(group.getLevelId()), new Integer(group.getGroupId()),
               new Integer(group.getParentId()), group.getGroupName()};

         groupList.append(MessageFormat.format(pattern, args)).append("\n");
      }

      return groupList.toString();
   }

   public String getGroupOptions(Connection con, int selectedGroupId)
   {
      return getGroupOptions(con, new int[]
      {selectedGroupId});
   }

   public String getGroupOptions(Connection con, int[] selectedGroupIds)
   {
      // get all available groups
      Vector groups = new Vector();

      try
      {
         GroupManager groupMgr = new GroupManager();
         groups = groupMgr.getLeveledData(con);
      }
      catch(SQLException e)
      {
         System.out.println(e);
      }

      StringBuffer groupOptions = new StringBuffer();
      for(int i = 0; i < groups.size(); i++)
      {
         Group group = (Group)groups.get(i);
         String name = group.getGroupName();
         int groupId = group.getGroupId();
         groupOptions.append("<option value='").append(groupId).append("'");

         if(selectedGroupIds != null)
         {
            for(int g = 0; g < selectedGroupIds.length; g++)
            {
               if(groupId == selectedGroupIds[g])
               {
                  groupOptions.append(" selected");
                  break;
               }
            }
         }

         groupOptions.append(">");
         groupOptions.append(name).append("</option>").append("\n");
      }

      return groupOptions.toString();
   }
}
