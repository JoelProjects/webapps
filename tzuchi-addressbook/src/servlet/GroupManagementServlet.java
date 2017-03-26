package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Constants;
import util.DatabaseAccess;
import datamodel.Group;
import datamodel.GroupManager;

public class GroupManagementServlet extends HttpServlet
{
   public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws IOException, ServletException
   {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();

      String error = null;
      Connection con = null;

      try
      {
         // connect to the database
         DatabaseAccess db = new DatabaseAccess();
         con = db.getConnection("config", Constants.DATA_SOURCE);

         String action = request.getParameter("action");
         GroupManager groupMgr = new GroupManager();
         if(action.equals("add"))
         {
            String levelIdStr = request.getParameter("levelId");
            String parentIdStr = request.getParameter("parentId");
            String groupName = request.getParameter("groupName");

            Group group = new Group();
            group.setGroupName(groupName);
            group.setParentId(Integer.parseInt(parentIdStr));
            group.setLevelId(Integer.parseInt(levelIdStr));

            groupMgr.insert(con, group);
         }
         else if(action.equals("delete"))
         {
            String groupIdStr = request.getParameter("groupId");

            try
            {
               groupMgr.delete(con, Integer.parseInt(groupIdStr));
            }
            catch(SQLException se)
            {
               error = se.getMessage();
            }
         }
         else if(action.equals("rename"))
         {
            String groupIdStr = request.getParameter("groupId");
            String groupName = request.getParameter("groupName");

            Group group = new Group();
            group.setGroupId(Integer.parseInt(groupIdStr));
            group.setGroupName(groupName);

            groupMgr.update(con, group);
         }

         String query = "";
         if(error != null)
            query = query + "&error=" + error;

         response
               .sendRedirect("webpages/secure/groupManagement.jsp?type=group&selected=1"
                     + query);
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
