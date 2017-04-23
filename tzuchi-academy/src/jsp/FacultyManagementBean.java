/* $Id: FacultyManagementBean.java,v 1.1 2006/07/21 06:41:21 joelchou Exp $*/

package jsp;

/*standard package*/
import java.sql.*;
import java.util.*;

/* custom package*/
import datamodel.*;

/*
 * Bean for jsp use
 */
public class FacultyManagementBean
{
  private String type = "";
  private String firstName = "";
  private String lastName = "";
  private String chineseName = "";
  private String street = "";
  private String apt = "";
  private String city = "";
  private String state = "";
  private String zip = "";
  private String areaCode = "";  
  private String phone = "";
  private String email = "";
  private String mobilePhone = "";

    
  public String getFacultyList (Connection con)
  {
    StringBuffer html = new StringBuffer();
    FacultyManager facultyMgr = new FacultyManager();
    Vector facultyList = new Vector();
    Faculty faculty = new Faculty();
    String chineseName="";
    String englishName="";
    String typeStr="";
    long facultyId=0;
 
  
    /*************
     * get Teacher
     *************/
    typeStr= FacultyManager.TYPE_TEACHER;

    try
      {
	facultyList = facultyMgr.getDataByType(con, typeStr);
      } catch (SQLException e)
	{
	  System.out.println(e);
	}

    //remember size for each type of faculty
    int sizeOfFaculty=facultyList.size(); 
              
    //if there data from resource
    if (facultyList.size() != 0)
      {
	for (int i=0; i<facultyList.size(); i++)
	  {
	    faculty = (Faculty) facultyList.get(i);
	    englishName=faculty.getFirstName()+" "+faculty.getLastName();
	    chineseName=faculty.getChineseName();
	    if (chineseName==null)
		chineseName="";
	    facultyId=faculty.getFacultyId();


	    html.append("<tr align='center'>");
	    html.append("<td>").append(englishName).append("</td>");
	    html.append("<td>").append(chineseName).append("</td>");        
	    html.append("<td>").append(typeStr).append("</td>");
html.append("<td>").append("<input type='button' value='Assign' onClick=\"assignTeacher('").append(facultyId).append("')\"></td>"); 
	    html.append("<td>").append("<input type='button' value='Edit' onClick=\"editFaculty('").append(facultyId).append("')\"></td>"); 
	    	    html.append("<td>").append("<input type='button' value='Delete' onClick=\"deleteFaculty('").append(facultyId).append("')\"></td>"); 
	    html.append("</tr>\n");        
	  }
      }
	/****************
	 * get Staff type
	 *****************/
	typeStr= FacultyManager.TYPE_STAFF;

    try
      {
	facultyList = facultyMgr.getDataByType(con, typeStr);
      } catch (SQLException e)
	{
	  System.out.println(e);
	}

    //remember size for each type of faculty
    sizeOfFaculty += facultyList.size(); 

    //if there data from resource
    if (facultyList.size() != 0)
      {
	for (int i=0; i<facultyList.size(); i++)
	  {
	    faculty = (Faculty) facultyList.get(i);
	    englishName=faculty.getFirstName()+"&nbsp;"+faculty.getLastName();
	    chineseName=faculty.getChineseName();
	    if (chineseName==null)
		chineseName="";
	    facultyId=faculty.getFacultyId();

	    html.append("<tr align='center'>");
	    html.append("<td>").append(englishName).append("</td>");
	    html.append("<td>").append(chineseName).append("</td>");        
	    html.append("<td>").append(typeStr).append("</td>");
	    html.append("<td>").append("</td>"); 
	    html.append("<td>").append("<input type='button' value='Edit' onClick=\"editFaculty('").append(facultyId).append("')\"></td>"); 
	    html.append("<td>").append("<input type='button' value='Delete' onClick=\"deleteFaculty('").append(facultyId).append("')\"></td>"); 
	    html.append("</tr>\n");        
	  }	

      }

	
	/****************
	 * get Volunteer type
	 ****************/
	typeStr= FacultyManager.TYPE_VOLUNTEER;

    
    try
      {
	facultyList = facultyMgr.getDataByType(con, typeStr);
      } catch (SQLException e)
	{
	  System.out.println(e);
	}

    //remember size for each type of faculty
    sizeOfFaculty+=facultyList.size(); 

    //check the size of the Faculty for all types
    //if there is not faculty, print no data availe
    if (sizeOfFaculty == 0)
      {
	html.append("<tr align='center'>");
	html.append("<td> No Data Available<td>");
	html.append("</tr>\n"); 
	return html.toString();
      }

    //if there data from resource
    if (facultyList.size() != 0)
      {
	for (int i=0; i<facultyList.size(); i++)
	  {
	    faculty = (Faculty) facultyList.get(i);
	    englishName=faculty.getFirstName()+" "+faculty.getLastName();
	    chineseName=faculty.getChineseName();
	    if (chineseName==null)
		chineseName="";
	    facultyId=faculty.getFacultyId();

	    html.append("<tr align='center'>");
	    html.append("<td>").append(englishName).append("</td>");
	    html.append("<td>").append(chineseName).append("</td>");
	    html.append("<td>").append(typeStr).append("</td>");
	    html.append("<td>").append("</td>"); 
	    html.append("<td>").append("<input type='button' value='Edit' onClick=\"editFaculty('").append(facultyId).append("')\"></td>"); 
	    html.append("<td>").append("<input type='button' value='Delete' onClick=\"deleteFaculty('").append(facultyId).append("')\"></td>"); 
	    html.append("</tr>\n");        
	  }	

      }

    return html.toString();
  }

  public void getDataById(Connection conn, long id)
  {
    FacultyManager facultyMgr = new FacultyManager();

    Faculty faculty = null;

    try 
      {
	faculty = facultyMgr.getDataById(conn,id);
      }
    catch (SQLException e)
      {
	System.out.println(":Error in getting Faculty's info");
      }
    if (faculty==null)
      return;
	
    //get data from database and private data member
    type = faculty.getType();
    firstName = faculty.getFirstName();
    lastName = faculty.getLastName();
    chineseName = faculty.getChineseName();
    street = faculty.getStreet();
    apt = faculty.getApt();
    city = faculty.getCity();
    state = faculty.getState();
    zip = faculty.getZip();
    areaCode = faculty.getAreaCode();
    phone = faculty.getPhone();
    email = faculty.getEmail();
    mobilePhone = faculty.getMobilePhone();
    
  }
      
  public String getType()
  {
    String[] types = {FacultyManager.TYPE_TEACHER,FacultyManager.TYPE_STAFF,
    FacultyManager.TYPE_VOLUNTEER};
    StringBuffer optionStr = new StringBuffer();
    
     optionStr.append("<option value='-1'>select type</option>");
    // form OPTION part of HTML SELECT
    for(int i = 0; i < types.length; i++)
    {
      optionStr.append("<OPTION vaule =' "+ i + "'");
      if(types[i].equals(type))  // if it's selected state
        optionStr.append(" selected");
      
      optionStr.append(">").append(types[i]).append("</OPTION>\n");
    }
    
    return optionStr.toString();
  }
  
  public String getFirstName()
  {
    return firstName;
  }
  
  public String getLastName()
  {
    return lastName;
  }
  
  public String getChineseName()
  {
    return chineseName;
  }
  
  public String getStreet()
  {
    return street;
  }
  
  public String getApt()
  {
    return apt;
  }
  
  public String getCity()
  {
    return city;
  }
  
  public String getState()
  {
    String[] states = {"MA", "NH", "RI"};
    StringBuffer optionStr = new StringBuffer();
    
    optionStr.append("<option value='-1'></option>");
    // form OPTION part of HTML SELECT
    for(int i = 0; i < states.length; i++)
    {
      optionStr.append("<OPTION");
      if(states[i].equals(state))  // if it's selected state
        optionStr.append(" selected");
      
      optionStr.append(">").append(states[i]).append("</OPTION>\n");
    }
    
    return optionStr.toString();
  }
  
  public String getZip()
  {
    return zip;
  }
  
  public String getAreaCode()
  {
    return areaCode;
  }
  
  public String getPhone()
  {
    return phone;
  }
  
  public String getEmail()
  {
    return email;
  }
  
  public String getMobilePhone()
  {
    return mobilePhone;
  }
  

  

}
