package jsp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import util.Utility;
import datamodel.Relative;
import datamodel.RelativeManager;
import datamodel.Student;
import datamodel.StudentManager;
public class SearchResultsBean
{
   /**
    * Arranged by students.
    */
   public String getStudentList(Connection con, String searchType,
         String searchStr)
   {
      StudentManager stMgr = new StudentManager();
      String condition = "";
      if(!Utility.isEmpty(searchType) && !Utility.isEmpty(searchStr))
      {
         // full name search for either Chinese or English name (case-insensitive)
         condition = "concat(" + StudentManager.COL_FIRST_NAME + ",' ',"
               + StudentManager.COL_LAST_NAME + ") LIKE '%" + searchStr
               + "%' OR " + StudentManager.COL_CHINESE_NAME + " LIKE '%"
               + Utility.big5ToUnicode(searchStr) + "%'";
      }
      Vector vect = new Vector();
      try
      {
         vect = stMgr.search(con, condition);
      }
      catch(SQLException e)
      {
         System.out.println(e);
      }
      
      return getStudentsHtml(vect);
   }
   /**
    * Arranged by students for a course.
    */
   public String getStudentList(Connection con, int courseId, int year, String semester)
   {
      StudentManager stMgr = new StudentManager();
      Vector vect = new Vector();
      try
      {
         vect = stMgr.search(con, courseId, year, semester);
      }
      catch(SQLException e)
      {
         System.out.println(e);
      }
      
      return getStudentsHtml(vect);      
   }   
   
   public String getStudentsHtml(List vect)
   {
      StringBuffer html = new StringBuffer();
      if(vect.size() != 0)
      {
         for(int i = 0; i < vect.size(); i++)
         {
            Student st = (Student)vect.get(i);
            long stId = st.getStudentId();
            long familyId = st.getFamilyId();
            String englishName = st.getFirstName() + "&nbsp;"
                  + st.getLastName();
            String chineseName = st.getChineseName();
            if(Utility.isEmpty(chineseName))
               chineseName = "&nbsp;";
            html.append("<tr align='center'>");
            html.append("<td>").append(englishName).append("</td>");
            html.append("<td>").append(chineseName).append("</td>");
            html.append("<td>").append(
                  "<a href=\"javascript:viewHistory('" + stId
                        + "')\">history</a>").append("</td>");
            html
                  .append("<td>")
                  .append(
                        "<input type='button' value='View Whole Family' onClick=\"manageFamily('")
                  .append(familyId).append("')\"></td>");
            html.append("</tr>\n");
         }
      }
      else
      {
         html
               .append("<tr><td colspan='6' align='center'>No Matched Data</td></tr>");
      }
      return html.toString();
   }
   /**
    * Arranged by families.
    */
   public String getFamilyList(Connection con, String searchType,
         String searchStr)
   {
      RelativeManager relativeMgr = new RelativeManager();
      String condition = "";
      if(!Utility.isEmpty(searchType) && !Utility.isEmpty(searchStr))
      {
         condition = "concat(" + RelativeManager.COL_FIRST_NAME + ",' ',"
               + RelativeManager.COL_LAST_NAME + ") LIKE '%" + searchStr
               + "%' OR " + RelativeManager.COL_CHINESE_NAME + " LIKE '%"
               + Utility.big5ToUnicode(searchStr) + "%'";
      }
      Vector vect = new Vector();
      try
      {
         vect = relativeMgr.searchByGuardian(con, condition);
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
            Relative rel = (Relative)vect.get(i);
            long familyId = rel.getFamilyId();
            if(Utility.isEmpty(rel.getFirstName())
                  && Utility.isEmpty(rel.getLastName())
                  && Utility.isEmpty(rel.getChineseName()))
               continue;
            String englishName = rel.getFirstName() + "&nbsp;"
                  + rel.getLastName();
            String chineseName = rel.getChineseName();
            String relationName = rel.getRelationName();
            if(Utility.isEmpty(rel.getFirstName())
                  && Utility.isEmpty(rel.getLastName()))
               englishName = "&nbsp;";
            if(Utility.isEmpty(chineseName))
               chineseName = "&nbsp;";
            html.append("<tr align='center'>");
            html.append("<td>").append(englishName).append("</td>");
            html.append("<td>").append(chineseName).append("</td>");
            html.append("<td>").append(relationName).append("</td>");
            html
                  .append("<td>")
                  .append(
                        "<input type='button' value='View Whole Family' onClick=\"manageFamily('")
                  .append(familyId).append("')\"></td>");
            html
                  .append("<td>")
                  .append(
                        "<input type='button' value='Delete' onClick=\"deleteFamily('")
                  .append(familyId).append("')\"></td>");
            html.append("</tr>\n");
         }
      }
      else
      {
         html
               .append("<tr><td colspan='6' align='center'>No Matched Data</td></tr>");
      }
      return html.toString();
   }
}
