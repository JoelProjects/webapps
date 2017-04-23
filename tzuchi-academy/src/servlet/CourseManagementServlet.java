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
import datamodel.Course;
import datamodel.CourseManager;

public class CourseManagementServlet extends HttpServlet 
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
      con = db.getConnection(Constants.DS_REF_NAME);

      String courseIdStr = request.getParameter("courseId");

      CourseManager courseMgr = new CourseManager();
      
      String action = request.getParameter("action");      
      if(action.equals("update"))
      {
        String classIdStr = request.getParameter("classId");
        String primaryTeacherIdStr = request.getParameter("primaryTeacherId");
        String secondaryTeacherIdStr = request.getParameter("secondaryTeacherId");
        String classRoom = request.getParameter("classRoom");

        String schoolYear = request.getParameter("schoolYear");
        String schoolSemester = request.getParameter("schoolSemester");
        int year = Integer.parseInt(schoolYear);
               
        Course course = new Course();
        course.setYear(year);
        course.setSemester(schoolSemester);
        course.setClassId(Integer.parseInt(classIdStr));
        course.setPrimaryTeacherId(Long.parseLong(primaryTeacherIdStr));
        course.setSecondaryTeacherId(Long.parseLong(secondaryTeacherIdStr));
        course.setClassRoom(classRoom);
        
        if(courseIdStr == null)
        {
          // new course
          courseMgr.insert(con, course);
        }
        else
        {
          // update existing course
          int courseId = Integer.parseInt(courseIdStr);
          course.setCourseId(courseId);
          courseMgr.update(con, course);
        }
      }
      else
        if(action.equals("delete"))
        {
          int courseId = Integer.parseInt(courseIdStr);
          
          try
          {
            courseMgr.delete(con, courseId);
          }
          catch(SQLException se)
          {
            error = se.getMessage();
          }
        }

      String query = "";
      if(error != null)
        query = "?error=" + error;
      
      response.sendRedirect("webpages/admin/courseManagement.jsp" + query);
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
