package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.Constants;
import util.DatabaseAccess;
import datamodel.PersonGroup;
import datamodel.PersonGroupManager;
import datamodel.PersonInfo;
import datamodel.PersonInfoManager;
public class PersonManagementServlet extends HttpServlet
{
   public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws IOException, ServletException
   {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      Connection con = null;
      try
      {
         // connect to the database
         DatabaseAccess db = new DatabaseAccess();
         con = db.getConnection("config", Constants.DATA_SOURCE);
         PersonInfoManager personMgr = new PersonInfoManager();
         PersonGroupManager groupMgr = new PersonGroupManager();
         // group ID & name
         String groupIdStr = request.getParameter("groupId"); // current group
         String groupName = request.getParameter("groupName");
         int currentGroupId = Integer.parseInt(groupIdStr);
         String action = request.getParameter("action");
         if(action.equals("update"))
         {
            // person info
            PersonInfo person = new PersonInfo();
            person.setChineseName(request.getParameter("chineseName"));
            person.setFirstName(request.getParameter("firstName"));
            person.setLastName(request.getParameter("lastName"));
            person.setStreet(request.getParameter("street"));
            person.setCity(request.getParameter("city"));
            person.setState(request.getParameter("state"));
            person.setZip(request.getParameter("zip"));
            person.setEmail(request.getParameter("email"));
            person.setHomePhone(request.getParameter("homePhone"));
            person.setWorkPhone(request.getParameter("workPhone"));
            person.setCellPhone(request.getParameter("cellPhone"));
            String personIdStr = request.getParameter("personId");
            int personId;
            if(personIdStr == null)
            {
               // new person
               personId = personMgr.insert(con, person);
            }
            else
            {
               // update existing person
               personId = Integer.parseInt(personIdStr);
               person.setPersonId(personId);
               personMgr.update(con, person);
            }
            
            // selected group IDs (might be multiple)
            String[] selectedGroupIdsStr = request
                  .getParameterValues("selectedGroupIds");
            // original person group info in DB
            Vector orgPersonGroups = groupMgr.getDataByPersonId(con, personId);
            PersonGroup group = new PersonGroup();
            groupMgr.delete(con, personId);
            if(selectedGroupIdsStr != null)
            {
               // Group Leader checkbox 
               String personType = request.getParameter("type");
               group.setPersonId(personId);
               for(int i = 0; i < selectedGroupIdsStr.length; i++)
               {
                  boolean newGroup = true;
                  int id = Integer.parseInt(selectedGroupIdsStr[i]);
                  group.setGroupId(id);
                  for(int idx = 0; idx < orgPersonGroups.size(); idx++)
                  {
                     PersonGroup pg = (PersonGroup)orgPersonGroups.get(idx);
                     // existing person group
                     if(id == pg.getGroupId())
                     {
                        newGroup = false;
                        if(currentGroupId == id)
                        {
                           // if it's the current group, check if Group Leader is checked or not
                           if(personType != null)
                              group
                                    .setPersonType(PersonGroupManager.GROUP_LEADER);
                           else
                              group
                                    .setPersonType(PersonGroupManager.GROUP_MEMBER);
                        }
                        else
                        {
                           // if not, use original person type info
                           group.setPersonType(pg.getPersonType());
                        }
                        
                        break;
                     }
                  }
                  // new group for this person
                  if(newGroup)
                  {
                     if(currentGroupId == id)
                     {
                        // if it's the current group, check if Group Leader is checked or not
                        if(personType != null)
                           group.setPersonType(PersonGroupManager.GROUP_LEADER);
                        else
                           group.setPersonType(PersonGroupManager.GROUP_MEMBER);
                     }
                     else
                        group.setPersonType(PersonGroupManager.GROUP_MEMBER);
                  }
                  groupMgr.insert(con, group);
               }
            }
         }
         else if(action.equals("delete"))
         {
            String personIdStr = request.getParameter("personId");
            int personId = Integer.parseInt(personIdStr);
            personMgr.delete(con, personId);
            groupMgr.delete(con, personId);
         }
         String encodedName = java.net.URLEncoder.encode(groupName, "UTF-8"); // encode chinese characters
         String url = "webpages/secure/personManagement.jsp?type=address&selected=0&groupId="
               +
               groupIdStr + "&groupName=" + encodedName;
         response.sendRedirect(url);
      }
      catch(Exception e)
      {
         out.println(e);
      }
      finally
      {
         try
         {
            con.close();
         }
         catch(Exception e)
         {
         }
      }
   }
   public void doPost(HttpServletRequest request, HttpServletResponse response)
   throws IOException, ServletException
   {
      doGet(request, response);
   }
}
