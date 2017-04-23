package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.Constants;
import util.DatabaseAccess;
import util.Utility;

/**
 * For updating user information.
 */
public class UpdateAccountServlet extends HttpServlet 
{
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  { 
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    
    Connection con = null;
    
    try 
    {
      // get user ID
      HttpSession session = request.getSession(false);
      Long objUserID = (Long)session.getAttribute("userID");
     
      // connect to the database
      DatabaseAccess db = new DatabaseAccess();
      con = db.getConnection(Constants.DS_REF_NAME);
      
      Statement stmt = con.createStatement();
    
      // get original password for current user
      ResultSet rs = 
        stmt.executeQuery("SELECT password FROM User WHERE userID=" + objUserID.toString());
  
      String oldPassword = null;
      
      if(rs.next())
      {
        oldPassword = rs.getString("password");
      }
      
      String inPassword = request.getParameter("passwd");
      // convert user input password into digested message
      inPassword = Utility.digestPassword(inPassword);  
      
      if(oldPassword != null && inPassword.equals(oldPassword))
      {
        // update new password
        String newPassword = request.getParameter("newPasswd");
        // convert user input password into digested message
        newPassword = Utility.digestPassword(newPassword);  

        String query = 
          "UPDATE User SET password='" + newPassword + "' WHERE userID=" + objUserID.toString();
        stmt.executeUpdate(query);

        response.sendRedirect("webpages/secure/studentMain.jsp");
      }
      else
      {
        response.sendRedirect("webpages/secure/updateAccount.jsp?error=1&selected=2");      
      }
    }
    catch(Exception e)
    {
      out.println(e);
    }
    finally
    {
      try{con.close();}
      catch(Exception e){}
    }
  }
  
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    doGet(request, response);
  }
}
