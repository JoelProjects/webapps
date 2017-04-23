/* $Id: StudentPaymentBean.java,v 1.1 2006/07/21 06:41:21 joelchou Exp $
 * Created on 2004/10/30
 * 
 * $Log: StudentPaymentBean.java,v $
 * Revision 1.1  2006/07/21 06:41:21  joelchou
 * init
 *
 * Revision 1.1  2005/12/07 04:25:37  joelchou
 * Submitted for new structure.
 *
 * Revision 1.18  2005/09/10 06:15:10  joelchou
 * Added uniform payments.
 *
 * Revision 1.17  2005/05/15 06:08:36  joelchou
 * Added uniform payments.
 *
 * Revision 1.16  2004/12/07 18:46:23  jlin
 * remove debug print out
 *
 * Revision 1.15  2004/12/07 13:21:20  jlin
 * enable check no support
 *
 * Revision 1.14  2004/11/25 15:33:14  jlin
 * fix the web page error and move "Generate CSV File" button to top of the tuition table
 *
 * Revision 1.13  2004/11/22 11:47:21  jlin
 * fix the school year and semester selection
 *
 * Revision 1.12  2004/11/20 19:54:09  jlin
 * only allow the current semester and Full semester shows on payment option
 *
 * Revision 1.11  2004/11/20 11:27:22  jlin
 * use string to represent the semester data
 *
 * Revision 1.10  2004/11/18 13:32:07  jlin
 * put search result table in the bean
 *
 * Revision 1.9  2004/11/18 13:06:22  jlin
 * fix the problem of studentId number is truncat on JavaScript.
 * solving this problem by make Student Id as string
 *
 * Revision 1.8  2004/11/17 13:07:39  jlin
 * change getTermOptions function
 *
 * Revision 1.7  2004/11/16 23:09:23  jlin
 * fix the update button
 *
 * Revision 1.6  2004/11/16 20:14:38  jlin
 * Add payment update button
 *
 * Revision 1.5  2004/11/16 13:18:57  jlin
 * put a query result from manager class to html string
 *
 * Revision 1.4  2004/11/11 13:11:11  jlin
 * add getTuitionByTerm function
 *
 * Revision 1.3  2004/11/03 12:47:58  jlin
 * 1. change semester datatype to in
 * 2. if we can't find student in currently semester.  look for full term
 *
 * Revision 1.2  2004/11/02 22:48:36  jlin
 * add getTermOptions function
 *
 * Revision 1.1  2004/11/02 13:02:32  jlin
 * initial draft
 *
 * 
 * This class will be call by jsp and interface between internal and external
 */
package jsp;

import java.sql.*;

import datamodel.*;

import java.util.*;
import util.*;

/**
 * StudentPaymentBean is used by JSP to talk to database layer
 *
 * @author Jeff Lin <br>
 * @ version $Date: 2006/07/21 06:41:21 $<br>
 */
public class StudentPaymentBean
{
   private long studentId;
   private int year;
   private String term;
   private String checkNo;
   private int tuition;
   private int paymentTypeId = -1;

   public StudentPayment getTuitionFor(Connection con, long studentId,
         int year, String semester)
   {
      StudentPayment sp = null;

      this.year = year;
      this.term = semester;
      this.studentId = studentId;

      try
      {
         StudentPaymentMgr mgr = new StudentPaymentMgr();
         sp = mgr.findStudentById(con, studentId, year, term);

         //if can't finad any student for that semester. try to look for full semester
         if(sp == null)
            sp = mgr.findStudentById(con, studentId, year, Constants.FULL);
      }
      catch(SQLException e)
      {
         System.out.println(e.toString());
      }

      if(sp == null)
      {
         //if there is no record exist. zero everything.
         sp = new StudentPayment();
      }
      else
      {
         checkNo = sp.getCheckNo();
         tuition = sp.getTuition();
         term = sp.getSemester();
         paymentTypeId = sp.getPaymentTypeId();
      }

      return sp;
   }

   /**
    * get Payment term option, only display currentTerm and Full semester
    */
   public String getTermOptions(String currentTerm)
   {
      StringBuffer out = new StringBuffer();

      if(!Utility.isEmpty(currentTerm))
         term = currentTerm;

      if(term.compareToIgnoreCase(Constants.FULL) == 0)
      {
         out.append("<option value = '" + Constants.FALL + "'>"
               + Constants.FALL + " </option>\n");
         out.append("<option selected value = '" + Constants.FULL + "'>"
               + Constants.FULL + " </option>\n");

      }
      if(term.compareToIgnoreCase(Constants.FALL) == 0)
      {
         out.append("<option selected value = '" + Constants.FALL + "'>"
               + Constants.FALL + " </option>\n");
         out.append("<option  value = '" + Constants.FULL + "'>"
               + Constants.FULL + " </option>\n");
      }
      else
      {
         out.append("<option value = '" + term + "'>" + term
               + " </option>\n");
      }

      /*   	for (int i=0; i<Constants.PAYMENT_TERMS.length; i++)
       {
       out.append("<option value='" + Constants.PAYMENT_TERMS[i] +"' ");
       if (Constants.PAYMENT_TERMS[i].compareToIgnoreCase(term) == 0)
       out.append("selected ");
       out.append("> ").append(Constants.PAYMENT_TERMS[i]).append(" </option>\n");
       //TODO it is not a good idea to show all Payment Term
       //We should only show the current term and Full term to avoid problem
       }
       */
      return out.toString();
   }

   public String getTuitionByTerm(Connection con, int year, String semester)
   {
      int payTerm = Utility.strToIndex(semester);
      StringBuffer html = new StringBuffer();
      StudentPaymentMgr mgr = new StudentPaymentMgr();
      UniformPaymentManager uniformMgr = new UniformPaymentManager();
      SchoolStatusBean status = new SchoolStatusBean();
      status.getStatus(con); //get current shcool status
      Vector records = null;
      html.append("<h3>Search Results for ").append(year).append(", ").append(
            semester).append("</h3><br>\n");

      try
      {
         records = mgr.findStudentsByTerm(con, year, semester);
      }
      catch(SQLException e)
      {
         System.out.println(e.toString());
      }

      if(records != null && records.size() > 0)
      {
         html.append("<b>Totoal students found: ").append(records.size());
         html
               .append(
                     "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"button\" value=\"Generate CSV File\" onClick=\"generateFile()\">")
               .append("</b>");
         html
               .append("<table border=\"1\">\n<tr>\n")
               .append(
                     "<th>English Name</th><th>Chinese Name</th><th>Registered Course</th><th>Tuition $</th><th>Pay Term</th><th>Check No.</th><th>Uniform $</th><th>Check No.</th><th>Edit</th>\n</tr>");

         Iterator i = records.iterator();
         while(i.hasNext())
         {
            StudentPayment sp = (StudentPayment)i.next();
            long studentId = sp.getStudentId();
            html.append("<tr><td>");
            if(Utility.isEmpty(sp.getEngName()))
               html.append("&nbsp;").append("</td><td>");
            else
               html.append(sp.getEngName()).append("</td><td>");

            if(Utility.isEmpty(sp.getChName()))
               html.append("&nbsp;").append("</td><td>");
            else
               html.append(sp.getChName()).append("</td><td>");

            html.append(sp.getCourseName()).append("</td><td>");
            
            // tuition
            html.append(sp.getTuition()).append("</td><td>");
            if(Utility.isEmpty(sp.getSemester()))
               html.append("&nbsp;").append("</td><td>");
            else
               html.append(sp.getSemester()).append("</td><td>");
            if(Utility.isEmpty(sp.getCheckNo()))
               html.append("&nbsp;").append("</td><td>");
            else
               html.append(sp.getCheckNo()).append("</td><td>");
            
            // uniform
            try
            {
               UniformPayment uniform = uniformMgr.getPayment(con, studentId, year, semester);
               if(uniform != null)
               {
                  double uniformPay = uniform.getAmount();
                  html.append(uniformPay).append("</td><td>");
                  if(Utility.isEmpty(uniform.getCheckNo()))
                     html.append("&nbsp;").append("</td><td>");
                  else
                     html.append(uniform.getCheckNo()).append("</td><td>");                  
               }
               else
               {
                  html.append("&nbsp;</td><td>&nbsp;</td><td>");
               }
            }
            catch(SQLException e)
            {
               System.out.println(e.toString());
            }               
            
            if(status.getYear() == year
                  && semester.compareToIgnoreCase(status.getSemester()) == 0)
            {
               html
                     .append("<input type=\"button\" value=\"Update\" onClick=\"updateInfo('");
               html.append(sp.getStudentId()).append("')\">");
            }
            else
               html.append("&nbsp;");

            html.append("</td></tr>\n");
         }
         
         html.append("</table>");
      }
      else
      {
         //there is nothing in the result
         html.append("<h3>No Students Found</h3>");
      }/* records != null*/

      return html.toString();
   }

   public String getPaymentTypeOptions()
   {
      return HtmlUtility.getPaymentTypeOptions(paymentTypeId);
   }
}
