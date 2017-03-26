package jsp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import util.Utility;
import datamodel.PersonGroupManager;
import datamodel.PersonInfo;
import datamodel.PersonInfoManager;
public class PersonManagementBean
{
   private PersonInfo person;
   public String getPersonList(Connection con, int groupId, boolean isAdmin)
   {
      PersonInfoManager personMgr = new PersonInfoManager();
      Vector vect = new Vector();
      try
      {
         vect = personMgr.getDataByGroupId(con, groupId);
      }
      catch(SQLException e)
      {
         System.out.println(e);
      }
      StringBuffer html = new StringBuffer();
      if(vect.size() != 0)
      {
         PersonGroupManager groupMgr = new PersonGroupManager();
         for(int i = 0; i < vect.size(); i++)
         {
            PersonInfo person = (PersonInfo)vect.get(i);
            int personId = person.getPersonId();
            boolean isGroupLeader = false;
            try
            {
               isGroupLeader = groupMgr.isGroupLeader(con, personId, groupId);
            }
            catch(SQLException e)
            {
               System.out.println(e);
            }
            if(isGroupLeader)
               html.append("<tr align='center' bgcolor='#CCFFFF'>");
            else
               html.append("<tr align='center'>");
            String chineseName = person.getChineseName();
            if(Utility.isEmpty(chineseName))
               chineseName = "&nbsp;";
            String lastName = person.getLastName();
            if(Utility.isEmpty(lastName))
               lastName = "&nbsp;";
            String firstName = person.getFirstName();
            if(Utility.isEmpty(firstName))
               firstName = "&nbsp;";
            String homePhone = person.getHomePhone();
            if(Utility.isEmpty(homePhone))
               homePhone = "&nbsp;";
            String cellPhone = person.getCellPhone();
            if(Utility.isEmpty(cellPhone))
               cellPhone = "&nbsp;";
            String email = person.getEmail();
            if(Utility.isEmpty(email))
               email = "&nbsp;";
            html.append("<td>").append(chineseName).append("</td>");
            html.append("<td>").append(lastName).append("</td>");
            html.append("<td>").append(firstName).append("</td>");
            html.append("<td>").append(homePhone).append("</td>");
            html.append("<td>").append(cellPhone).append("</td>");
            html.append("<td>").append(email).append("</td>");
            html.append("<td>").append(
                  "<input type='button' value='View' onClick=\"viewPerson('")
                  .append(personId).append("')\"></td>");
            // only admin can access
            if(isAdmin)
            {
               html
                     .append("<td>")
                     .append(
                           "<input type='button' value='Edit' onClick=\"editPerson('")
                     .append(personId).append("')\">");
               html
                     .append(
                           "&nbsp;<input type='button' value='Delete' onClick=\"deletePerson('")
                     .append(personId).append("')\"></td>");
            }
            html.append("</tr>\n");
         }
      }
      else
      {
         html
               .append("<tr><td colspan='8' align='center'>No Person Available</td></tr>");
      }
      return html.toString();
   }
   public void getDataById(Connection con, int personId)
   {
      PersonInfoManager personMgr = new PersonInfoManager();
      try
      {
         person = personMgr.getDataById(con, personId);
      }
      catch(SQLException e)
      {
         System.out.println(e);
      }
   }
   public String getChineseName()
   {
      String chineseName = "";
      if(person != null)
         chineseName = person.getChineseName();
      return chineseName;
   }
   public String getFirstName()
   {
      String firstName = "";
      if(person != null)
         firstName = person.getFirstName();
      return firstName;
   }
   public String getLastName()
   {
      String lastName = "";
      if(person != null)
         lastName = person.getLastName();
      return lastName;
   }
   public String getStreet()
   {
      String street = "";
      if(person != null)
         street = person.getStreet();
      return street;
   }
   public String getCity()
   {
      String city = "";
      if(person != null)
         city = person.getCity();
      return city;
   }
   public String getState()
   {
      String state = "";
      if(person != null)
         state = person.getState();
      return state;
   }
   public String getZip()
   {
      String zip = "";
      if(person != null)
         zip = person.getZip();
      return zip;
   }
   public String getEmail()
   {
      String email = "";
      if(person != null)
         email = person.getEmail();
      return email;
   }
   public String getHomePhone()
   {
      String phone = "";
      if(person != null)
         phone = person.getHomePhone();
      return phone;
   }
   public String getWorkPhone()
   {
      String phone = "";
      if(person != null)
         phone = person.getWorkPhone();
      return phone;
   }
   public String getCellPhone()
   {
      String phone = "";
      if(person != null)
         phone = person.getCellPhone();
      return phone;
   }
}
