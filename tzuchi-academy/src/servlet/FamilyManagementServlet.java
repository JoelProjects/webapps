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
import datamodel.FamilyInfo;
import datamodel.FamilyInfoManager;
import datamodel.Relative;
import datamodel.RelativeManager;

public class FamilyManagementServlet extends HttpServlet 
{
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
  
    String error = null;
    Connection con = null;
    long familyId = 0;
    try
    {
      // connect to the database
      DatabaseAccess db = new DatabaseAccess();
      con = db.getConnection(Constants.DS_REF_NAME);

      FamilyInfoManager familyMgr = new FamilyInfoManager();

      String action = request.getParameter("action");      
      if(action.equals("update"))
      {
        // need to put some checking to see if this family exists
        
        RelativeManager relativeMgr = new RelativeManager();
        String familyIdStr = request.getParameter("familyId");
        if(familyIdStr == null)
        {
          String strId = Utility.getLongDateTimeStr();  // generate new ID
          familyId = Long.parseLong(strId);
        }
        else
          familyId = Long.parseLong(familyIdStr);  
        
        // general family info
        FamilyInfo family = new FamilyInfo();
        family.setFamilyId(familyId);
        family.setStreet(request.getParameter("street"));
        family.setApt(request.getParameter("apt"));
        family.setCity(request.getParameter("city"));
        family.setState(request.getParameter("state"));      
        family.setZip(request.getParameter("zip"));
        family.setAreaCode(request.getParameter("areaCode"));
        family.setPhone(request.getParameter("phone"));
        family.setEmail(request.getParameter("email")); 
        family.setInsureCo(request.getParameter("insureCo"));      
        
        // guardian1 info
        Relative gd1 = new Relative();
        gd1.setFamilyId(familyId);
        gd1.setRelationId(FamilyInfoManager.GUARDIAN1_ID);
        gd1.setRelationName(Utility.big5ToUnicode(request.getParameter("gd1Relation")));
        gd1.setFirstName(request.getParameter("gd1FirstName"));
        gd1.setLastName(request.getParameter("gd1LastName"));        
        gd1.setChineseName(Utility.big5ToUnicode(request.getParameter("gd1ChineseName")));
        gd1.setWorkPhone(request.getParameter("gd1WorkPhone"));
        gd1.setMobilePhone(request.getParameter("gd1MobilePhone"));
        
        // mother info
        Relative gd2 = new Relative();
        gd2.setFamilyId(familyId);
        gd2.setRelationId(FamilyInfoManager.GUARDIAN2_ID);
        gd2.setRelationName(Utility.big5ToUnicode(request.getParameter("gd2Relation")));
        gd2.setFirstName(request.getParameter("gd2FirstName"));
        gd2.setLastName(request.getParameter("gd2LastName"));  
        gd2.setChineseName(Utility.big5ToUnicode(request.getParameter("gd2ChineseName")));     
        gd2.setWorkPhone(request.getParameter("gd2WorkPhone"));
        gd2.setMobilePhone(request.getParameter("gd2MobilePhone"));
        
        // family doctor info
        Relative doctor = new Relative();      
        doctor.setFamilyId(familyId);
        doctor.setRelationId(FamilyInfoManager.FAMILY_DOC_ID);
        doctor.setLastName(Utility.big5ToUnicode(request.getParameter("doctorName")));
        doctor.setWorkPhone(request.getParameter("doctorPhone"));

        // emergency 1
        Relative er1 = new Relative();
        er1.setFamilyId(familyId);
        er1.setRelationId(FamilyInfoManager.EMERGENCY1_ID);
        er1.setLastName(Utility.big5ToUnicode(request.getParameter("er1Name")));
        er1.setWorkPhone(request.getParameter("er1Phone"));

        // emergency 2
        Relative er2 = new Relative();
        er2.setFamilyId(familyId);
        er2.setRelationId(FamilyInfoManager.EMERGENCY2_ID);
        er2.setLastName(Utility.big5ToUnicode(request.getParameter("er2Name")));
        er2.setWorkPhone(request.getParameter("er2Phone"));
                
        if(familyIdStr == null)
        {
          // new data
          familyMgr.insert(con, family);
          relativeMgr.insert(con, gd1);
          relativeMgr.insert(con, gd2);
          relativeMgr.insert(con, doctor);
          relativeMgr.insert(con, er1);
          relativeMgr.insert(con, er2);
        }
        else
        {
          // update data
          familyMgr.update(con, family);
          relativeMgr.update(con, gd1);
          relativeMgr.update(con, gd2);
          relativeMgr.update(con, doctor);
          relativeMgr.update(con, er1);
          relativeMgr.update(con, er2);
        }
      }
      else
        if(action.equals("delete"))
        {
          String familyIdStr = request.getParameter("familyId");
          
          try
          {
            familyMgr.delete(con, Long.parseLong(familyIdStr));
          }
          catch(SQLException se)
          {
            error = se.getMessage();
          }
        }
            
      if(action.equals("update"))
        response.sendRedirect("webpages/admin/studentList.jsp?familyId=" + familyId);
      else
        if(action.equals("delete"))
        {
          String searchBy = request.getParameter("searchBy");
          String searchStr = request.getParameter("searchStr");

          String query = "?searchBy=" + searchBy + "&searchStr=" + searchStr;
          if(error != null)
            query = query + "&error=" + error;

          response.sendRedirect("webpages/admin/searchResults.jsp" + query);
        }
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
