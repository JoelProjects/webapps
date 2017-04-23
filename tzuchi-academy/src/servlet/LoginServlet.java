package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.Constants;
import util.DatabaseAccess;
import util.UserInfoSession;
public class LoginServlet extends HttpServlet
{
   public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws IOException, ServletException
   {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      Connection con = null;
      try
      {
         String username = request.getRemoteUser();
         // connect to the database
         DatabaseAccess db = new DatabaseAccess();
         con = db.getConnection(Constants.DS_REF_NAME);
         int status = UserInfoSession.createUserInfoSession(request, username);
         if(status == 0)
         {
            if(request.isUserInRole("student"))
            {
               // String encodeURL = response.encodeRedirectURL(urlStr);
               response.sendRedirect("webpages/secure/studentMain.jsp");
            }
            else if(request.isUserInRole("teacher"))
            {
               response.sendRedirect("webpages/secure/teacherMain.jsp");
            }
            else if(request.isUserInRole("schooladmin"))
            {
               response.sendRedirect("webpages/admin/adminMain.jsp");
            }
         }
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
