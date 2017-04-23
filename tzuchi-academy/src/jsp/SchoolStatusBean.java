package jsp;

import java.sql.*;
import util.*;

import datamodel.*;

public class SchoolStatusBean
{
  private int year = 0;
  private String semester = "";
  
  public SchoolStatus getStatus(Connection con)
  {
    SchoolStatusManager statusMgr = new SchoolStatusManager();

    SchoolStatus status = null;    
    try
    {
      status = statusMgr.getStatus(con);
      
      if(status != null)
      {
        year = status.getYear();
        semester = status.getSemester();
      }
    }
    catch(SQLException e)
    {
      System.out.println(e);
    }
    
    return status;
  }
  
  public int getYear()
  {
    return year;
  }
  
  public String getSemester()
  {
    return semester;
  }
  
  public String getSchoolYearOption(Connection con)
  {
      StringBuffer out = new StringBuffer();

      if (year == 0)
          getStatus(con);
      
      for (int i = 0; i< Constants.SCHOOL_YEARS.length; i++)
      {
		out.append("<option value='" + Constants.SCHOOL_YEARS[i] +"' ");
		if (Constants.SCHOOL_YEARS[i]== year)
			out.append("selected ");
		out.append("> ").append(Constants.SCHOOL_YEARS[i]).append(" </option>\n");    
      }
         
      
      return out.toString();
  }
  
  public String getSchoolSemesterOption(Connection con)
  {
      StringBuffer out = new StringBuffer();
      
      if (Utility.isEmpty(semester))
          getStatus(con);
  
      for (int i = 0; i< Constants.SCHOOL_TERMS.length; i++)
      {
		out.append("<option value='" + Constants.SCHOOL_TERMS[i] +"' ");
		if (Constants.PAYMENT_TERMS[i].compareToIgnoreCase(semester) == 0)
			out.append("selected ");
		out.append("> ").append(Constants.SCHOOL_TERMS[i]).append(" </option>\n");
      }
      
      return out.toString();
  }
     
}
