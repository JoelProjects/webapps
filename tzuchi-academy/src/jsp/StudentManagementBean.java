package jsp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;
import util.HtmlUtility;
import util.Utility;
import datamodel.ClassManager;
import datamodel.Course;
import datamodel.CourseManager;
import datamodel.SchoolStatus;
import datamodel.SchoolStatusManager;
import datamodel.Student;
import datamodel.StudentManager;
public class StudentManagementBean
{
   private String chineseName = "";
   private String lastName = "";
   private String firstName = "";
   private String gender = "";
   private String dayTimeSchool = "";
   private String birthMonth = "";
   private String birthDay = "";
   private String birthYear = "";
   private int gradeOfSchool = 0;
   private String summerUniformSize = "";
   private boolean summerUniformReceived = false;
   private String winterUniformSize = "";
   private boolean winterUniformReceived = false;
   private String healthProblem = "";
   public String getStudentList(Connection con, long familyId)
   {
      StudentManager stMgr = new StudentManager();
      Vector vect = new Vector();
      try
      {
         vect = stMgr.getDataByFamily(con, familyId);
      }
      catch(SQLException e)
      {
         System.out.println(e);
      }
      // get current school year and semester
      SchoolStatusManager statusMgr = new SchoolStatusManager();
      SchoolStatus schoolStatus = null;
      int year = -1;
      String semester = "";
      try
      {
         schoolStatus = statusMgr.getStatus(con);
         if(schoolStatus != null)
         {
            year = schoolStatus.getYear();
            semester = schoolStatus.getSemester();
         }
      }
      catch(SQLException e)
      {
         System.out.println(e);
      }
      // get student info
      RegistrationManagementBean regBean = new RegistrationManagementBean();
      ClassManager classMgr = new ClassManager();
      StringBuffer html = new StringBuffer();
      if(vect.size() != 0)
      {
         for(int i = 0; i < vect.size(); i++)
         {
            Student st = (Student)vect.get(i);
            long stId = st.getStudentId();
            String englishName = st.getFirstName() + "&nbsp;"
                  + st.getLastName();
            String chineseName = st.getChineseName();
            if(Utility.isEmpty(chineseName))
               chineseName = "&nbsp;";
            // registered course
            String chineseCourse = null;
            String activityCourse = null;
            if(schoolStatus != null)
            {
               int chineseId = regBean.getRegisteredCourse(con, stId, year,
                     semester, ClassManager.CHINESE);
               int activityId = regBean.getRegisteredCourse(con, stId, year,
                     semester, ClassManager.ACTIVITY);
               try
               {
                  chineseCourse = classMgr.getNameByCourseId(con, chineseId);
               }
               catch(SQLException e)
               {
                  System.out.println(e);
               }
               try
               {
                  activityCourse = classMgr.getNameByCourseId(con, activityId);
               }
               catch(SQLException e)
               {
                  System.out.println(e);
               }
            }
            if(chineseCourse == null)
               chineseCourse = "None";
            if(activityCourse == null)
               activityCourse = "None";
            
            html.append("<tr align='center'>");
            html.append("<td>").append(englishName).append("</td>");
            html.append("<td>").append(chineseName).append("</td>");
            html.append("<td>").append("Chinese: " + chineseCourse).append(
                  "&nbsp;&nbsp;Activity: " + activityCourse).append(
                  "<br><a href=\"javascript:viewHistory('" + stId
                        + "')\">history</a>").append("</td>");
            html.append("<td>").append(
                  "<input type='button' value='Edit' onClick=\"editStudent('")
                  .append(stId).append("')\"></td>");
            html
                  .append("<td>")
                  .append(
                        "<input type='button' value='Register' onClick=\"registerStudent('")
                  .append(stId).append("')\"></td>");
            html
                  .append("<td>")
                  .append(
                        "<input type='button' value='Delete' onClick=\"deleteStudent('")
                  .append(stId).append("')\"></td>");
            html.append("</tr>\n");
         }
      }
      else
      {
         html
               .append("<tr><td colspan='5' align='center'>No Student Available</td></tr>");
      }
      return html.toString();
   }
   // get all registered courses for this student
   public String getHistory(Connection con, long studentId)
   {
      CourseManager courseMgr = new CourseManager();
      Vector courses = new Vector();
      try
      {
         courses = courseMgr.getDataByStudent(con, studentId);
      }
      catch(SQLException e)
      {
         System.out.println(e);
      }
      int year = 0;
      StringBuffer html = new StringBuffer();
      if(courses.size() != 0)
      {
         ClassManager classMgr = new ClassManager();
         String courseData = "";
         for(int c = 0; c < courses.size(); c++)
         {
            Course course = (Course)courses.get(c);
            String courseName = "";
            try
            {
               courseName = course.getSemester() + ": "
                     + classMgr.getNameByCourseId(con, course.getCourseId());
            }
            catch(SQLException e)
            {
               System.out.println(e);
            }
            // courses are sorted by year
            if(year != course.getYear())
            {
               if(c != 0)
                  html.append("<td>").append(courseData).append("</td></tr>");
               courseData = "";
               year = course.getYear();
               html.append("<tr align='center'><td>").append(year).append(
                     "</td>");
               courseData = courseData + courseName;
            }
            else
            {
               courseData = courseData + "<br>" + courseName;
            }
         }
         html.append("<td>").append(courseData).append("</td></tr>");
      }
      else
      {
         html
               .append("<tr><td colspan='3' align='center'>No Data Available</td></tr>");
      }
      return html.toString();
   }
   public void getDataById(Connection con, long studentId)
   {
      StudentManager stMgr = new StudentManager();
      try
      {
         Student student = stMgr.getDataById(con, studentId);
         if(student != null)
         {
            chineseName = student.getChineseName();
            lastName = student.getLastName();
            firstName = student.getFirstName();
            gender = student.getGender();
            String birthDateStr = student.getBirthDate().toString();
            dayTimeSchool = student.getDayTimeSchool();
            gradeOfSchool = student.getGradeOfSchool();
            summerUniformSize = student.getSummerUniformSize();
            summerUniformReceived = student.isSummerUniformReceived();
            winterUniformSize = student.getWinterUniformSize();
            winterUniformReceived = student.isWinterUniformReceived();
            healthProblem = student.getHealthProblem();
            StringTokenizer st = new StringTokenizer(birthDateStr, "-");
            birthYear = st.nextToken();
            birthMonth = st.nextToken();
            birthDay = st.nextToken();
         }
      }
      catch(Exception e)
      {
         System.out.println(e.toString());
      }
   }
   public String getChineseName()
   {
      return chineseName;
   }
   public String getLastName()
   {
      return lastName;
   }
   public String getFirstName()
   {
      return firstName;
   }
   public String getGender()
   {
      return gender;
   }
   public String getBirthYear()
   {
      if(birthDay == null || birthYear.equals("1900"))
         birthYear = "";
      return birthYear;
   }
   public String getBirthMonth()
   {
      if(birthMonth == null || birthYear.equals("1900"))
         birthMonth = "";
      return birthMonth;
   }
   public String getBirthDay()
   {
      if(birthDay == null || birthYear.equals("1900"))
         birthDay = "";
      return birthDay;
   }
   public String getGradeOfSchool()
   {
      if(gradeOfSchool > 0)
         return Integer.toString(gradeOfSchool);
      else
         return "";
   }
   public String getDayTimeSchool()
   {
      return dayTimeSchool;
   }
   public String getSummerUniformSizeOptions()
   {
      return HtmlUtility.getUniformSizeOptions(summerUniformSize);
   }
   public String getSummerUniformReceived()
   {
      String checked = "";
      if(summerUniformReceived)
         checked = "checked";
      return checked;
   }
   public String getWinterUniformSizeOptions()
   {
      return HtmlUtility.getUniformSizeOptions(winterUniformSize);
   }
   public String getWinterUniformReceived()
   {
      String checked = "";
      if(winterUniformReceived)
         checked = "checked";
      return checked;
   }
   public String getHealthProblem()
   {
      return healthProblem;
   }
}
