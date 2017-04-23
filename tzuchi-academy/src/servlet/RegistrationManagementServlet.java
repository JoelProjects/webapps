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
import util.Utility;
import datamodel.StudentManager;
import datamodel.StudentPayment;
import datamodel.StudentPaymentMgr;
import datamodel.StudentReg;
import datamodel.StudentRegManager;
import datamodel.UniformPayment;
import datamodel.UniformPaymentManager;

// this is for registration
public class RegistrationManagementServlet extends HttpServlet 
{   
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    
    Connection con = null;
    String familyIdStr = request.getParameter("familyId");
    String studentIdStr = request.getParameter("studentId");
    long studentId = Long.parseLong(studentIdStr);
    long familyId = Long.parseLong(familyIdStr);

    try
    { 
      // connect to the database
      DatabaseAccess db = new DatabaseAccess();
      con = db.getConnection(Constants.DS_REF_NAME);

      StudentRegManager regMgr = new StudentRegManager();
      
      String action = request.getParameter("action");      
      if(action.equals("update"))
      {
        // original data
        String oldChineseIdStr = request.getParameter("oldChineseId");
        String oldActivityIdStr = request.getParameter("oldActivityId");
        int oldChineseId = Integer.parseInt(oldChineseIdStr);
        int oldActivityId = Integer.parseInt(oldActivityIdStr);
        // selected data
        String chineseIdStr = request.getParameter("chineseId");
        String activityIdStr = request.getParameter("activityId");
        int chineseId = Integer.parseInt(chineseIdStr);
        int activityId = Integer.parseInt(activityIdStr);

        // chinese
        if(oldChineseId == -1)
        {
          // if chinese ID is not -1, insert it
          if(chineseId != -1)
          {
            StudentReg reg = new StudentReg();
            reg.setCourseId(chineseId);
            reg.setStudentId(studentId);
            regMgr.insert(con, reg);
          }
        }
        else
        {
          // if chinese ID is -1, then delete it
          if(chineseId == -1)
          {
            regMgr.delete(con, oldChineseId, studentId);
          }
          else
          {
            // otherwise, update it
            StudentReg reg = new StudentReg();
            reg.setCourseId(oldChineseId);
            reg.setStudentId(studentId);
            regMgr.update(con, chineseId, reg);
          }
        }

        // activity
        if(oldActivityId == -1)
        {
          // if activity ID is not -1, insert it
          if(activityId != -1)
          {
            StudentReg reg = new StudentReg();
            reg.setCourseId(activityId);
            reg.setStudentId(studentId);
            regMgr.insert(con, reg);
          }
        }
        else
        {
          // if activity ID is -1, then delete it
          if(activityId == -1)
          {
            regMgr.delete(con, oldActivityId, studentId);
          }
          else
          {
            // otherwise, update it
            StudentReg reg = new StudentReg();
            reg.setCourseId(oldActivityId);
            reg.setStudentId(studentId);
            regMgr.update(con, activityId, reg);
          }
        }

        StudentManager stMgr = new StudentManager();        
        // uniform
        String summerUniformSize = request.getParameter("summerUniformSize");
        String summerUniformReceived = request.getParameter("summerUniformReceived");
        String winterUniformSize = request.getParameter("winterUniformSize");
        String winterUniformReceived = request.getParameter("winterUniformReceived");

        boolean isSummerReceived = false;
        if(summerUniformReceived != null)
          isSummerReceived = true;

        boolean isWinterReceived = false;
        if(winterUniformReceived != null)
          isWinterReceived = true;

        stMgr.updateUniform(con, studentId, summerUniformSize, isSummerReceived,
          winterUniformSize, isWinterReceived);
        
        // grade
        String gradeOfSchoolStr = request.getParameter("gradeOfSchool");
        int gradeOfSchool = -1;
        if(!Utility.isEmpty(gradeOfSchoolStr))
          gradeOfSchool = Integer.parseInt(gradeOfSchoolStr);
          
        stMgr.updateGradeOfSchool(con, studentId, gradeOfSchool);
        
        //update tuition table
        StudentPayment sp = new StudentPayment();
        StudentPaymentMgr spMgr = new StudentPaymentMgr();
        sp.setStudentId(studentId);
        int year = Integer.parseInt(request.getParameter("schoolYear"));
        String schoolSemester = request.getParameter("schoolSemester");  // registered semester
        String oldPayTerm = request.getParameter("oldPayTerm");
        String currentTerm = request.getParameter("term");  // pay term
        String oldCheckNo = request.getParameter("oldCheckNo");
        String checkNo = request.getParameter("checkNo");
        int tuitionPaymentTypeId = Integer.parseInt(request.getParameter("payment"));
        sp.setYear(year);
        sp.setSemester(currentTerm);
        sp.setCheckNo(checkNo);
        sp.setPaymentTypeId(tuitionPaymentTypeId);
        
        String amountStr = request.getParameter("amount");
        int amount = 0;
        if(!Utility.isEmpty(amountStr))
            amount = Integer.parseInt(amountStr);      
        String oldAmountStr = request.getParameter("oldAmount");
        int oldAmount = Integer.parseInt(oldAmountStr);
        
        if (oldAmount != 0)
        {
            //this record exist befor
            
            //detect the change on payment term
            if (oldPayTerm.compareToIgnoreCase(currentTerm) != 0)
            {
                sp.setTuition(oldAmount);   //old amount for search criteria
                spMgr.updateSemester(con, sp);
                //change from fall semester to full semester, add new record to next year's spring
                if (oldPayTerm.compareToIgnoreCase(Constants.FALL) == 0 && 
                        currentTerm.compareToIgnoreCase(Constants.FULL) == 0)
                {
                    sp.setYear(year + 1);
                    sp.setSemester(Constants.SPRING);
                    sp.setTuition(amount);
                    spMgr.insert(con, sp);
                } else if (oldPayTerm.compareToIgnoreCase(Constants.FULL) == 0 && 
                        currentTerm.compareToIgnoreCase(Constants.FALL) == 0)
//                  change from full semester to fall semester, delete new record to next year's spring    
                {
                    sp.setYear(year + 1);
                    sp.setSemester(Constants.SPRING);
                    spMgr.delete(con, sp);
                }
                
            }
            //detect changes on check No
            if (!oldCheckNo.equals(checkNo))
            {
                sp.setYear(year);
                sp.setTuition(amount);
                sp.setSemester(currentTerm);
                spMgr.updatePayment(con, sp);
                
                //if next year spring exist, update it as well
                if (spMgr.findStudentById(con, sp.getStudentId(), year +1, Constants.SPRING) != null)
                {
                    //update spring term
                    sp.setYear(year +1);
                    sp.setSemester(Constants.SPRING);
                    spMgr.updatePayment(con, sp);  //update payment infomation
                }
            }
            
            //detect changes on tuition
            if(amount != 0 && oldAmount != amount)
            //when amount is not zero and the old amount is not the same as current amount
            //execute the update
            {         
                sp.setTuition(amount);
                sp.setYear(year);
                sp.setSemester(currentTerm);
                spMgr.updatePayment(con, sp);  //update payment infomation
                
                //if next year spring exist, update it as well
                if (spMgr.findStudentById(con, sp.getStudentId(), year +1, Constants.SPRING) != null)
                {
                    //update spring term
                    sp.setYear(year +1);
                    sp.setSemester(Constants.SPRING);
                    spMgr.updatePayment(con, sp);  //update payment infomation
                }
                
            }else if (amount == 0)
            {
                //amount become 0, lets delete it
                sp.setYear(year);
                sp.setSemester(currentTerm);
                spMgr.delete(con, sp);
                
                //if next year spring exist delete as well
                if (spMgr.findStudentById(con, sp.getStudentId(), year +1, Constants.SPRING) != null)
                {
                    //update spring term
                    sp.setYear(year +1);
                    sp.setSemester(Constants.SPRING);
                    spMgr.delete(con, sp);
                }
                
            }         
        }else
        {
            //this record never exist
            if(amount != 0)
            {
                sp.setYear(year);
                sp.setSemester(currentTerm);
                sp.setTuition(amount);
                spMgr.insert(con, sp);  //new payment infomation
                if (currentTerm.compareToIgnoreCase(Constants.FULL) == 0)
                    //when student pay in full, add new record for next year's spring
                {
                    sp.setYear(year + 1);
                    sp.setSemester(Constants.SPRING);
                    spMgr.insert(con, sp);
                }
            }          
        }  
        
        // uniform payment
        String uniformAmount = request.getParameter("uniformAmount");
        if(!Utility.isEmpty(uniformAmount))
        {
           String uniformCheckNo = request.getParameter("uniformCheckNo");
           int uniformPaymentTypeId = Integer.parseInt(request.getParameter("uniformPayment"));
           String paySummerUniformSize = request.getParameter("paySummerUniformSize");
           String payWinterUniformSize = request.getParameter("payWinterUniformSize");
        
           UniformPayment uniformPay = new UniformPayment();
           uniformPay.setStudentId(studentId);
           uniformPay.setYear(year);
           uniformPay.setSemester(schoolSemester);
           uniformPay.setAmount(Double.parseDouble(uniformAmount));
           uniformPay.setPaymentTypeId(uniformPaymentTypeId);
           uniformPay.setCheckNo(uniformCheckNo);
           uniformPay.setSummerSize(paySummerUniformSize);
           uniformPay.setWinterSize(payWinterUniformSize);
        
           String newUniformPayment = request.getParameter("newUniformPayment");
           UniformPaymentManager uniformMgr = new UniformPaymentManager();        
           if(newUniformPayment.equalsIgnoreCase("yes"))
           {
              uniformMgr.insert(con, uniformPay);
           }
           else
           {
              uniformMgr.update(con, uniformPay);
           }
        }
      }/* action.equals("update")*/
      
      //396834 is magic number to go back to paymentSearch.jsp
      if (familyId == 396834)
      {
          String reqYear = request.getParameter("reqYear");
          String reqSemester = request.getParameter("reqSemester");
          response.sendRedirect("webpages/admin/paymentSearch.jsp?year=" + reqYear 
                  	+"&semester="+ reqSemester);
      } else
          // return to the student main page after updating data
          response.sendRedirect("webpages/admin/studentList.jsp?familyId=" + familyId);
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
