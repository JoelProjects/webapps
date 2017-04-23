package jsp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import datamodel.ClassManager;
public class ClassManagementBean
{
   private String name = "";
   private int category = -1;
   private String description = "";
   private int maxStudents = -1;
   public String getChineseClassList(Connection con)
   {
      return getClassList(con, ClassManager.CHINESE);
   }
   public String getActivityClassList(Connection con)
   {
      return getClassList(con, ClassManager.ACTIVITY);
   }
   public String getClassList(Connection con, int category)
   {
      ClassManager classMgr = new ClassManager();
      Vector vect = new Vector();
      try
      {
         vect = classMgr.getDataByCategory(con, category);
      }
      catch(SQLException e)
      {
         System.out.println(e);
      }
      StringBuffer html = new StringBuffer();
      if(vect.size() != 0)
      {
         for(int i = 0; i < vect.size(); i++)
         {
            datamodel.Class cl = (datamodel.Class)vect.get(i);
            int classId = cl.getClassId();
            String className = cl.getName();
            html.append("<tr align='center'>");
            html.append("<td>").append(className).append("</td>");
            html.append("<td>").append(
                  "<input type='button' value='Edit' onClick=\"editClass(")
                  .append(classId).append(")\"></td>");
            html.append("<td>").append(
                  "<input type='button' value='Delete' onClick=\"deleteClass(")
                  .append(classId).append(")\"></td>");
            html.append("</tr>\n");
         }
      }
      else
      {
         html
               .append("<tr><td colspan='5' align='center'>No Class Available</td></tr>");
      }
      return html.toString();
   }
   public void getDataById(Connection con, int classId)
   {
      ClassManager classMgr = new ClassManager();
      try
      {
         datamodel.Class cl = classMgr.getDataById(con, classId);
         if(cl != null)
         {
            name = cl.getName();
            category = cl.getCategory();
            description = cl.getDescription();
            maxStudents = cl.getMaxStudents();
         }
      }
      catch(Exception e)
      {
         System.out.println(e.toString());
      }
   }
   public String getName()
   {
      return name;
   }
   public String getCategory()
   {
      String[] categories =
      {"Chinese", "Activity"};
      int[] ids = new int[]
      {ClassManager.CHINESE, ClassManager.ACTIVITY};
      StringBuffer optionStr = new StringBuffer();
      // form OPTION part of HTML SELECT
      for(int i = 0; i < categories.length; i++)
      {
         optionStr.append("<option value='" + ids[i] + "'");
         if(ids[i] == category) // if it's selected one
            optionStr.append(" selected");
         optionStr.append(">").append(categories[i]).append("</option>\n");
      }
      return optionStr.toString();
   }
   public String getDescription()
   {
      return description;
   }
   public String getMaxStudents()
   {
      if(maxStudents > 0)
         return Integer.toString(maxStudents);
      else
         return "";
   }
}
