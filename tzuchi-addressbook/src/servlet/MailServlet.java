package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.Constants;
import util.DatabaseAccess;
import util.Logger;
import util.MailAccess;
import util.Utility;
import datamodel.PersonInfo;
import datamodel.PersonInfoManager;
public class MailServlet extends HttpServlet
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
         String action = request.getParameter("action");
         if(action.equals("send"))
         {
            String[] groupIds = request.getParameterValues("selectedGroupIds");
            String subject = request.getParameter("subject").trim();
            String content = request.getParameter("content").trim();
            // get emails for selected groups
            PersonInfoManager personMgr = new PersonInfoManager();
            HashMap emailMap = new HashMap(); // use e-mail as key to avoid duplicate person
            for(int i = 0; i < groupIds.length; i++)
            {
               Vector persons = personMgr.getDataByGroupId(con, Integer
                     .parseInt(groupIds[i]));
               for(int j = 0; j < persons.size(); j++)
               {
                  PersonInfo p = (PersonInfo)persons.get(j);
                  String email = p.getEmail();
                  if(!Utility.isEmpty(email))
                     emailMap.put(email, ""); // might store name as value in the future
               }
            }
            MailAccess mail = new MailAccess(Constants.MAIL_REF_NAME);
            StringBuffer error = new StringBuffer();
            StringBuffer recipient = new StringBuffer();
            if(emailMap.size() == 0)
               error.append("There is no e-mail in the mailing list.");
            else
            {
               emailMap.put(Constants.MAIL_SENDER, ""); // one copy to self
               for(Iterator it = emailMap.entrySet().iterator(); it.hasNext();)
               {
                  Map.Entry en = (Map.Entry)it.next();
                  String to = (String)en.getKey();
                  try
                  {
                     // using Verizon (SMTP authentication is required)
                     mail.send(Constants.MAIL_SENDER, to, subject, content,
                     Constants.SMTP_SERVER, Constants.MAIL_AUTH_USER,
                           Constants.MAIL_AUTH_PASSWORD);
                     recipient.append(to + "\n");
                  }
                  catch(Exception mailE)
                  {
                     Logger
                           .log(request, "To " + to + ": " + mailE.getMessage());
                     error
                           .append("To " + to + ": " + mailE.getMessage()
                                 + "\n");
                  }
               }
            }
            RequestDispatcher dispatcher =
            getServletContext().getRequestDispatcher(
                  "/webpages/secure/sentResult.jsp?type=tool&selected=2");
            request.setAttribute("error", Utility.escape(error.toString()));
            request.setAttribute("recipient", recipient.toString());
            dispatcher.forward(request, response);
            //response.sendRedirect("webpages/secure/sentResult.jsp?type=tool&selected=2&error=" + 
            //  java.net.URLEncoder.encode(Utility.escape(error.toString()), "UTF-8"));
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
