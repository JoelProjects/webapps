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
import datamodel.ClassManager;

public class ClassManagementServlet extends HttpServlet 
{   
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    
    String error = null;  // error message to be displayed if there is any
    Connection con = null;
    try
    { 
      // connect to the database
      DatabaseAccess db = new DatabaseAccess();
      con = db.getConnection(Constants.DS_REF_NAME);

      String classIdStr = request.getParameter("classId");

      ClassManager classMgr = new ClassManager();
      
      String action = request.getParameter("action");      
      if(action.equals("update"))
      {
        String name = request.getParameter("name");
        String categoryStr = request.getParameter("category");
        String description = request.getParameter("description");
        String maxStudentsStr = request.getParameter("maxStudents");
        
        if(!Utility.isEmpty(name))
          name = name.trim();

        int maxStudents = -1;
        if(!Utility.isEmpty(maxStudentsStr))
          maxStudents = Integer.parseInt(maxStudentsStr);
       
        datamodel.Class cl = new datamodel.Class();
        cl.setName(Utility.big5ToUnicode(name));
        cl.setCategory(Integer.parseInt(categoryStr));
        cl.setMaxStudents(maxStudents);
        cl.setDescription(description);
        
        if(classIdStr == null)
        {
          // new class
          classMgr.insert(con, cl);
        }
        else
        {
          // update existing class
          int classId = Integer.parseInt(classIdStr);
          cl.setClassId(classId);
          classMgr.update(con, cl);
        }
      }
      else
        if(action.equals("delete"))
        {
          int classId = Integer.parseInt(classIdStr);
          
          try
          { 
            classMgr.delete(con, classId);
          }
          catch(SQLException se)
          {
            error = se.getMessage();
          }
        }
      
      String query = "";
      if(error != null)
        query = "?error=" + error;
        
      response.sendRedirect("webpages/admin/classManagement.jsp" + query);
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
