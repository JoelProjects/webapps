/* $Id: FacultyManagementServlet.java,v 1.1 2006/07/21 06:41:21 joelchou Exp $*/
/*
 * FacultyManagementServer
 * udate data to database and delete a record from data base
 */


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
import datamodel.Faculty;
import datamodel.FacultyManager;


public class FacultyManagementServlet extends HttpServlet
{
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {

    String error = null; // error message to be displayed if there is any
    Connection conn =null;
    long facultyId = 0;
        
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    try
      {
	DatabaseAccess db = new DatabaseAccess();
	conn =  db.getConnection(Constants.DS_REF_NAME);
	
	FacultyManager facultyMgr = new FacultyManager();
	
	String action = request.getParameter("action");

	if (action.equals("update"))
	  {
	    
	    String facultyIdStr = request.getParameter("facultyId");

	    // need to put some checking to see if this faculty exists
	    if (facultyIdStr == null)
	      {
		//  new facultygenerate new ID
		String strId = Utility.getLongDateTimeStr();  
		facultyId = Long.parseLong(strId);
	      } else
		facultyId = Long.parseLong(facultyIdStr);

	    //get Faculty info from html
	    Faculty faculty = new Faculty();
	    faculty.setFacultyId(facultyId);
	    faculty.setType(request.getParameter("type"));
	    faculty.setFirstName(request.getParameter("firstName"));
	    faculty.setLastName(request.getParameter("lastName"));
	    faculty.setChineseName(Utility.big5ToUnicode(request.getParameter("chineseName")));
	    faculty.setStreet(request.getParameter("street"));
	    faculty.setApt(request.getParameter("apt"));
	    faculty.setCity(request.getParameter("city"));
	    faculty.setState(request.getParameter("state"));      
	    faculty.setZip(request.getParameter("zip"));
	    faculty.setAreaCode(request.getParameter("areaCode"));
	    faculty.setPhone(request.getParameter("phone"));
	    faculty.setEmail(request.getParameter("email")); 
	    faculty.setMobilePhone(request.getParameter("mobilePhone"));

	    //access data base here
	    if(facultyIdStr == null)
	      // new data
	      facultyMgr.insert(conn, faculty);
	    else
	      facultyMgr.update(conn, faculty);
	    
	  }else if(action.equals("delete"))
	    {
	      String facultyIdStr = request.getParameter("facultyId");
	      try
		{
		  facultyMgr.delete(conn, Long.parseLong(facultyIdStr));
		} catch (SQLException e)
		  {
		    error = e.getMessage();
		  }
	    }

	
	String query = "";
      if(error != null)
        query = "?error=" + error;
      
      //post action handling
      response.sendRedirect("webpages/admin/facultyManagement.jsp" + query);
      
	
      } catch (Exception e)
	{
	  out.println( e.toString());
	}
    finally
      {
	try{conn.close();}
	catch(Exception e){}
      } 
  } /* doGet */

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    doGet(request, response);
  } /* doPost */
} /* Class FacultyManagementServlet */
