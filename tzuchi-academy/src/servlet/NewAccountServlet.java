package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.Constants;
import util.DatabaseAccess;
import util.Utility;
/**
 * For login page to check if user exists, is authorized and get role
 * of current login user.
 */
public class NewAccountServlet extends HttpServlet
{
   public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws IOException, ServletException
   {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      Connection con = null;
      try
      {
         String username = request.getParameter("username");
         String password = request.getParameter("passwd");
         // connect to the database
         DatabaseAccess db = new DatabaseAccess();
         con = db.getConnection(Constants.DS_REF_NAME);
         Statement stmt = con.createStatement();
         // get password and role of current login name to check if user exists or not
         ResultSet rs = stmt
               .executeQuery("SELECT password FROM User WHERE userName='"
                     + username + "'");
         if(!rs.next())
         {
            password = Utility.digestPassword(password); // digested password
            String strID = Utility.getLongDateTimeStr(); // generate user ID
            long userID = Long.parseLong(strID);
            // use batch update to initialize a new account
            con.setAutoCommit(false);
            Statement batchStmt = con.createStatement();
            // insert a new user account
            String query = "INSERT INTO User VALUES ('" + username + "', '"
                  + password + "', 'student', " + userID + ")";
            batchStmt.addBatch(query);
            // insert a new family info
            batchStmt.addBatch("INSERT INTO FamilyInfo (familyID) VALUES ("
                  + userID + ")");
            // insert 5 rows into table Relative for related persons' info
            batchStmt
                  .addBatch("INSERT INTO Relative (familyID, relationID) VALUES ("
                        + userID + ", 1)"); // father
            batchStmt
                  .addBatch("INSERT INTO Relative (familyID, relationID) VALUES ("
                        + userID + ", 2)"); // mother
            batchStmt
                  .addBatch("INSERT INTO Relative (familyID, relationID) VALUES ("
                        + userID + ", 3)"); // family doctor
            batchStmt
                  .addBatch("INSERT INTO Relative (familyID, relationID) VALUES ("
                        + userID + ", 4)"); // emergency 1                     
            batchStmt
                  .addBatch("INSERT INTO Relative (familyID, relationID) VALUES ("
                        + userID + ", 5)"); // emergency 2        
            batchStmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
            out.println("<center><br>You are registered as '" + username
                  + "' successfully.<BR>");
            out
                  .println("<A HREF=\"javascript:window.top.location.replace('index.html')\">Click here to log in.</A></cetner>");
         }
         else
         {
            String queryString = "error=1&username=" + username;
            response.sendRedirect("../webpages/newAccount.html?" + queryString);
         }
      }
      catch(BatchUpdateException be)
      {
         out.println("Error in creating new account:<P>");
         try
         {
            con.rollback();
         }
         catch(Exception e)
         {
            out.println(e);
         }
         out.println(be);
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
