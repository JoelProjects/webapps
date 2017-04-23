package jsp;

import java.util.*;
import java.sql.*;

import datamodel.*;

public class RegistrationManagementBean
{
  public String getChineseOptions(Connection con, int year, String semester)
  {
    return getChineseOptions(con, year, semester, -1);
  }

  public String getChineseOptions(Connection con, int year, String semester, int selectedCourse)
  {
    return getCourseOptions(con, year, semester, ClassManager.CHINESE, selectedCourse);
  }

  public String getActivityOptions(Connection con, int year, String semester)
  {
    return getActivityOptions(con, year, semester, -1);
  }

  public String getActivityOptions(Connection con, int year, String semester, int selectedCourse)
  {
    return getCourseOptions(con, year, semester, ClassManager.ACTIVITY, selectedCourse);
  }

  public String getAllCoursesOptions(Connection con, int year, String semester)
  {
    StringBuffer html = new StringBuffer();
    html.append("<option value='-1'></option>");
    html.append(getCourseOptions1(con, year, semester, ClassManager.CHINESE, -1));
    html.append(getCourseOptions1(con, year, semester, ClassManager.ACTIVITY, -1));
    
    return html.toString();
  }

  public String getCourseOptions(Connection con, int year, String semester, int category, 
    int selectedCourse)
  {    
    StringBuffer html = new StringBuffer();
    html.append("<option value='-1'></option>");
    html.append(getCourseOptions1(con, year, semester, category, selectedCourse));
    
    return html.toString();
  }  

  public int getRegisteredCourse(Connection con, long studentId, int year, String semester, 
    int category)
  {
    int selectedCourse = -1;
    
    // get all courses for a specific year and semester in a category
    CourseManager courseMgr = new CourseManager();
    
    Vector courses = new Vector();
    try
    {
      courses = courseMgr.getDataBySemester(con, year, semester, category);
    }
    catch(SQLException e)
    {
      System.out.println(e);
    }
    
    int size = courses.size();
    if(size > 0)
    {
      int[] ids = new int[size];
      
      for(int i = 0; i < size; i++)
      {
        Course course = (Course)courses.get(i);
        ids[i] = course.getCourseId();
      }
      
      StudentRegManager regMgr = new StudentRegManager();
      
      try
      {
        Vector selectedIds = regMgr.getCourses(con, studentId, ids);
        // suppose there is only at most one selected course for each category
        if(selectedIds.size() > 0)
        {
          StudentReg reg = (StudentReg)selectedIds.get(0);
          selectedCourse = reg.getCourseId();
        }
      }
      catch(SQLException e)
      {
        System.out.println(e);
      }
    }
    
    return selectedCourse;
  }

  // HTML options without empty option
  private StringBuffer getCourseOptions1(Connection con, int year, String semester, int category, 
    int selectedCourse)
  {
    CourseManager courseMgr = new CourseManager();
    ClassManager classMgr = new ClassManager();

    // get all courses for a specific year and semester in a category    
    Vector courses = new Vector();
    try
    {
      courses = courseMgr.getDataBySemester(con, year, semester, category);
    }
    catch(SQLException e)
    {
      System.out.println(e);
    }
    
    StringBuffer html = new StringBuffer();
    for(int i = 0; i < courses.size(); i++)
    {
      try
      {
        Course course = (Course)courses.get(i);
        int courseId = course.getCourseId();
        int classId = course.getClassId();
      
        datamodel.Class cl = classMgr.getDataById(con, classId);
        String name = cl.getName();
      
        html.append("<option value='" + courseId + "'");
        if(courseId == selectedCourse)
          html.append(" selected");
        
        html.append(" >").append(name).append("</option>\n");
      }
      catch(SQLException e)
      {
        System.out.println(e);
      }
    }
    
    return html;
  } 
}
