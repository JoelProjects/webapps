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
import util.Utility;
import datamodel.Student;
import datamodel.StudentManager;

// this is for updating data to the database in the form of student information
public class StudentManagementServlet extends HttpServlet 
{   
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    
    String error = null;
    Connection con = null;
    String familyIdStr = request.getParameter("familyId");
    long familyId = Long.parseLong(familyIdStr);
    try
    { 
      // connect to the database
      DatabaseAccess db = new DatabaseAccess();
      con = db.getConnection(Constants.DS_REF_NAME);

      StudentManager stMgr = new StudentManager();
      
      String action = request.getParameter("action");      
      if(action.equals("update"))
      {
        String studentIdStr = request.getParameter("studentId");
        long studentId = 0;
        if(studentIdStr == null)
        {
          String strId = Utility.getLongDateTimeStr();  // generate new ID
          studentId = Long.parseLong(strId);
        }
        else
          studentId = Long.parseLong(studentIdStr);  
            
        // birth date
        String birthMonth = request.getParameter("birthMonth");
        String birthDay = request.getParameter("birthDay");
        String birthYear = request.getParameter("birthYear");
        if(birthMonth == null || birthMonth.trim().length() == 0)
          birthMonth = "1";
        if(birthDay == null || birthDay.trim().length() == 0)
          birthDay = "1";
        if(birthYear == null || birthYear.trim().length() == 0)
          birthYear = "1900";
        String birthDate = birthYear + "-" + birthMonth + "-" + birthDay;
              
        Student st = new Student();
        st.setStudentId(studentId);
        st.setFamilyId(familyId);
        st.setChineseName(Utility.big5ToUnicode(request.getParameter("chineseName")));
        st.setLastName(request.getParameter("lastName"));
        st.setFirstName(request.getParameter("firstName"));
        st.setGender(request.getParameter("gender"));
        st.setBirthDate(java.sql.Date.valueOf(birthDate));
        st.setDayTimeSchool(request.getParameter("dayTimeSchool"));
        st.setHealthProblem(Utility.big5ToUnicode(request.getParameter("healthProblem")));
        
        if(studentIdStr == null)
        {
          // new student
          stMgr.insert(con, st);
        }
        else
        {
          // update existing student
          stMgr.update(con, st);
        }
      }
      else
        if(action.equals("delete"))
        {
          String studentIdStr = request.getParameter("studentId");
          
          try
          {
            stMgr.delete(con, Long.parseLong(studentIdStr));
          }
          catch(SQLException se)
          {
            error = se.getMessage();
          }
        }
      
      // return to the student main page after updating data
      String query = "?familyId=" + familyId;
      if(error != null)
        query = query + "&error=" + error;

      response.sendRedirect("webpages/admin/studentList.jsp" + query);
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
