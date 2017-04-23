/* $Id: CSVReportServlet.java,v 1.1 2006/07/21 06:41:21 joelchou Exp $
 * Created on 2004/11/22
 *
 * $Log: CSVReportServlet.java,v $
 * Revision 1.1  2006/07/21 06:41:21  joelchou
 * init
 *
 * Revision 1.2  2005/12/18 07:22:54  joelchou
 * Changed to get data source from JNDI name.
 *
 * Revision 1.1  2005/12/07 04:25:37  joelchou
 * Submitted for new structure.
 *
 * Revision 1.7  2005/09/10 06:15:10  joelchou
 * Added uniform payments.
 *
 * Revision 1.6  2005/08/28 18:54:59  joelchou
 * Changed in MIME type and document title.
 *
 * Revision 1.5  2004/12/07 18:45:39  jlin
 * fix table header and null strings
 *
 * Revision 1.4  2004/12/07 13:21:20  jlin
 * enable check no support
 *
 * Revision 1.3  2004/11/27 03:03:08  jlin
 * finish up implementation
 *
 * Revision 1.2  2004/11/24 12:56:11  jlin
 * continue implementation
 *
 * Revision 1.1  2004/11/22 13:51:25  jlin
 * Initial draft
 *
 *
 * TODO add Comment for CSVReportServlet.java
 */
package servlet;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Constants;
import util.DatabaseAccess;
import util.Utility;
import datamodel.SchoolStatus;
import datamodel.SchoolStatusManager;
import datamodel.StudentPayment;
import datamodel.StudentPaymentMgr;
import datamodel.UniformPayment;
import datamodel.UniformPaymentManager;

/**
 * This servlet generate the comma seperate value files format of student file
 *
 * @author Jeff Lin
 * @ version $Date: 2006/07/21 06:41:21 $
 */
public class CSVReportServlet extends HttpServlet
{
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException
   {
      Connection con = null;
      StringBuffer buffer = new StringBuffer();
      String docType = null;

      SchoolStatus status = null;

      response.setContentType("application/excel;charset=Unicode;");
      try
      {
         // connect to the database
         DatabaseAccess db = new DatabaseAccess();
         con = db.getConnection(Constants.DS_REF_NAME);
         docType = request.getParameter("type");

         //compose the data here
         if(docType.equals("tuition"))
         {
            //get request information
            SchoolStatusManager statusMgr = new SchoolStatusManager();
            String yearStr = request.getParameter("year");
            int schoolYear = Integer.parseInt(yearStr);
            String semesterStr = request.getParameter("semester");
            StudentPaymentMgr mgr = new StudentPaymentMgr();
            UniformPaymentManager uniformMgr = new UniformPaymentManager();
            Vector records = mgr.findStudentsByTerm(con, schoolYear,
                  semesterStr);
            buffer.append("Tzuchi Academy ").append(yearStr).append(" ")
                  .append(semesterStr);
            buffer.append(" Student Payment Details\n\n");
            //append header of the table
            buffer
                  .append("\"English Name\",\"Chinese Name\",\"Tuition $\",\"Check No.\",\"Uniform $\",\"Check No.\"\n");
            Iterator i = records.iterator();
            while(i.hasNext())
            {
               StudentPayment sp = (StudentPayment)i.next();
               long studentId = sp.getStudentId();
               buffer.append("\"").append(sp.getEngName()).append("\",\"")
                     .append(sp.getChName()).append("\",\"");
               buffer.append(sp.getTuition()).append("\",\"");
               if(!Utility.isEmpty(sp.getCheckNo()))
                  buffer.append(sp.getCheckNo());
               
               buffer.append("\",\"");
               
               // uniform
               try
               {
                  UniformPayment uniform = uniformMgr.getPayment(con, studentId, schoolYear, semesterStr);
                  if(uniform != null)
                  {
                     double uniformPay = uniform.getAmount();
                     buffer.append(uniformPay).append("\",\"");
                     if(!Utility.isEmpty(uniform.getCheckNo()))
                        buffer.append(uniform.getCheckNo()); 
                     
                     buffer.append("\"");
                  }
                  else
                  {
                     buffer.append("\",\"\"");
                  }
               }
               catch(SQLException e)
               {
                  System.out.println(e.toString());
               }
               
               buffer.append("\n");
            }

            //set File Name
            String fileName = "tuition-" + yearStr + semesterStr;
            response.setHeader("Content-Disposition", "attachment;filename="
                  + fileName + ".csv");
         }

         // output the writer as bytes to the response output
         DataOutput output = new DataOutputStream(response.getOutputStream());
         byte[] bytes = buffer.toString().getBytes("Unicode");
         response.setContentLength(bytes.length);
         for(int i = 0; i < bytes.length; i++)
         {
            output.writeByte(bytes[i]);
         }
      }
      catch(Exception e)
      {
         System.out.println(e.toString());
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

   protected void doPost(HttpServletRequest request,
         HttpServletResponse response) throws ServletException, IOException
   {
      doGet(request, response);
   }
}
