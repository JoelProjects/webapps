package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Constants;
import util.DatabaseAccess;
import datamodel.Course;
import datamodel.CourseManager;
import datamodel.SchoolStatus;
import datamodel.SchoolStatusManager;
import datamodel.StudentPayment;
import datamodel.StudentPaymentMgr;
import datamodel.StudentReg;
import datamodel.StudentRegManager;

public class SchoolInfoManagementServlet extends HttpServlet 
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
      con = db.getConnection(Constants.DS_REF_NAME);

      String schoolYearStr = request.getParameter("schoolYear");
      String schoolSemester = request.getParameter("schoolSemester");
       
      con.setAutoCommit(false);
      
      // Currently, we only carry over data from Fall to Spring since
      // most of students register the whole year.
      if(schoolSemester.equalsIgnoreCase(CourseManager.SPRING))
      {
         // check if there is any record in the course table
         // if there is any, then don't need to carry over data from previous semester
         CourseManager courseMgr = new CourseManager();
         int currentYear = Integer.parseInt(schoolYearStr);
         Vector courses = courseMgr.getDataBySemester(con, currentYear, CourseManager.SPRING, -1);
         if(courses.size() == 0)
         {
            // get all courses from previous semester
            courses = 
               courseMgr.getDataBySemester(con, currentYear - 1, CourseManager.FALL, -1);
            // create course data for current semester
            Map courseIdMap = new HashMap();  // (old course ID, new course ID)
            StringBuffer inCondCourseIdsBuf = new StringBuffer();
            for(int i = 0; i < courses.size(); i++)
            {
               Course course = (Course)courses.get(i);
               int courseId = course.getCourseId();
               inCondCourseIdsBuf.append(courseId);
               if(i < courses.size() - 1)
                  inCondCourseIdsBuf.append(",");
               course.setYear(currentYear);
               course.setSemester(CourseManager.SPRING);
               int newCourseId = courseMgr.insert(con, course);
               
               // get mapping course ID between previous semester and current semester
               courseIdMap.put(new Integer(courseId), new Integer(newCourseId));
            }
            
            // get students already paid for this coming semester
            StudentPaymentMgr paymentMgr = new StudentPaymentMgr();
            Vector registeredSts = paymentMgr.getStudentIdsByTerm(con, currentYear, CourseManager.SPRING);
            StudentRegManager regMgr = new StudentRegManager();
            String inCondCourseIds = inCondCourseIdsBuf.toString();
            for(int st = 0; st < registeredSts.size(); st++)
            {
               Long studentIdObj = (Long)registeredSts.get(st);
               // get registered courses for this student in previous semester
               long studentId = studentIdObj.longValue();
               Vector courseIds = regMgr.getRegisteredCourseIds(con, studentId, inCondCourseIds);
               // insertion for new semester
               if(courseIds.size() == 0)
                  out.println("No registered courses for student ID: " + studentId);
               else
                  for(int idx = 0; idx < courseIds.size(); idx++)
                  {
                     Integer newCourseId = (Integer)courseIdMap.get(courseIds.get(idx));
                     StudentReg reg = new StudentReg();
                     reg.setStudentId(studentId);
                     reg.setCourseId(newCourseId.intValue());
                     regMgr.insert(con, reg);
                  }
            }
         }
         else
         {
            out.println("Data exist in " + schoolYearStr + " " + schoolSemester + 
                     ". Do not carry over data from previous semester.");
         }
      }
      
      // switch school calendar year and/or semester
      SchoolStatus status = new SchoolStatus();
      status.setYear(Integer.parseInt(schoolYearStr));
      status.setSemester(schoolSemester);

      SchoolStatusManager statusMgr = new SchoolStatusManager();
      
      if(statusMgr.getStatus(con) != null)
        statusMgr.update(con, status);
      else
        statusMgr.insert(con, status);
      
      con.commit();
      con.setAutoCommit(true);      
      
      response.sendRedirect("webpages/admin/courseManagement.jsp");
    }
    catch(Exception e)
    {
      out.println(e);
    }
    finally
    {
      if(con != null)
      {
         try
         {
            con.rollback();
            con.setAutoCommit(true);
            con.close();
         }
         catch(Exception e){}
      }
    }  
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    doGet(request, response);
  }
}
