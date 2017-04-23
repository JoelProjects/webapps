package jsp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import util.Utility;
import datamodel.ClassManager;
import datamodel.Course;
import datamodel.CourseManager;
import datamodel.Faculty;
import datamodel.FacultyManager;
public class CourseManagementBean
{
   private int classId = -1;
   private long primaryTeacherId = -1;
   private long secondaryTeacherId = -1;
   private String classRoom = "";
   public String getChineseCourseList(Connection con, int year, String semester)
   {
      return getCourseList(con, year, semester, ClassManager.CHINESE);
   }
   public String getActivityCourseList(Connection con, int year, String semester)
   {
      return getCourseList(con, year, semester, ClassManager.ACTIVITY);
   }
   public String getCourseList(Connection con, int year, String semester,
         int category)
   {
      CourseManager courseMgr = new CourseManager();
      Vector vect = new Vector();
      try
      {
         vect = courseMgr.getDataBySemester(con, year, semester, category);
      }
      catch(SQLException e)
      {
         System.out.println(e);
      }
      FacultyManager facultyMgr = new FacultyManager();
      StringBuffer html = new StringBuffer();
      if(vect.size() != 0)
      {
         ClassManager classMgr = new ClassManager();
         for(int i = 0; i < vect.size(); i++)
         {
            Course course = (Course)vect.get(i);
            int courseId = course.getCourseId();
            int classId = course.getClassId();
            long primaryTeacherId = course.getPrimaryTeacherId();
            long secondaryTeacherId = course.getSecondaryTeacherId();
            String className = "&nbsp;";
            try
            {
               datamodel.Class cl = classMgr.getDataById(con, classId);
               className = cl.getName();
            }
            catch(SQLException e)
            {
               System.out.println(e);
            }
            String teacherName = "&nbsp;";
            String taName = "&nbsp;";
            try
            {
               Faculty teacher = facultyMgr.getDataById(con, primaryTeacherId);
               if(teacher != null)
               {
                  teacherName = "";
                  if(!Utility.isEmpty(teacher.getChineseName()))
                     teacherName = teacher.getChineseName() + " ";
                  teacherName = teacherName + teacher.getFirstName() + " "
                        + teacher.getLastName();
               }
            }
            catch(SQLException e)
            {
               System.out.println(e);
            }
            try
            {
               Faculty ta = facultyMgr.getDataById(con, secondaryTeacherId);
               if(ta != null)
               {
                  taName = "";
                  if(!Utility.isEmpty(ta.getChineseName()))
                     taName = ta.getChineseName() + " ";
                  taName = taName + ta.getFirstName() + " " + ta.getLastName();
               }
            }
            catch(SQLException e)
            {
               System.out.println(e);
            }
            html.append("<tr align='center'>");
            html.append("<td><a href='javascript:viewStudents(" + courseId + ")'>").append(className).append("</a></td>");
            html.append("<td>").append(teacherName).append("</td>");
            html.append("<td>").append(taName).append("</td>");
            html.append("<td>").append(
                  "<input type='button' value='Edit' onClick=\"editCourse(")
                  .append(category + ",").append(courseId).append(")\"></td>");
            html
                  .append("<td>")
                  .append(
                        "<input type='button' value='Delete' onClick=\"deleteCourse(")
                  .append(courseId).append(")\"></td>");
            html.append("</tr>\n");
         }
      }
      else
      {
         html
               .append("<tr><td colspan='5' align='center'>No Course Available</td></tr>");
      }
      return html.toString();
   }
   public void getDataById(Connection con, int courseId)
   {
      CourseManager courseMgr = new CourseManager();
      try
      {
         Course course = courseMgr.getDataById(con, courseId);
         if(course != null)
         {
            classId = course.getClassId();
            primaryTeacherId = course.getPrimaryTeacherId();
            secondaryTeacherId = course.getSecondaryTeacherId();
            classRoom = course.getClassRoom();
         }
      }
      catch(Exception e)
      {
         System.out.println(e.toString());
      }
   }
   public String getCourse(Connection con, int categoryId)
   {
      ClassManager classMgr = new ClassManager();
      // get all available classes in a category
      Vector classes = new Vector();
      try
      {
         classes = classMgr.getDataByCategory(con, categoryId);
      }
      catch(SQLException e)
      {
         System.out.println(e);
      }
      StringBuffer html = new StringBuffer();
      html.append("<option value='-1'></option>");
      for(int i = 0; i < classes.size(); i++)
      {
         datamodel.Class cl = (datamodel.Class)classes.get(i);
         int id = cl.getClassId();
         String className = cl.getName();
         html.append("<option value='" + id + "'");
         if(id == classId)
            html.append(" selected");
         html.append(" >").append(className).append("</option>\n");
      }
      return html.toString();
   }
   public String getPrimaryTeacher(Connection con)
   {
      return getTeacherOptions(con, primaryTeacherId);
   }
   public String getSecondaryTeacher(Connection con)
   {
      return getTeacherOptions(con, secondaryTeacherId);
   }
   private String getTeacherOptions(Connection con, long teacherId)
   {
      FacultyManager facultyMgr = new FacultyManager();
      // get all teachers
      Vector teachers = new Vector();
      try
      {
         teachers = facultyMgr.getDataByType(con, FacultyManager.TYPE_TEACHER);
      }
      catch(SQLException e)
      {
         System.out.println(e);
      }
      StringBuffer html = new StringBuffer();
      html.append("<option value='-1'></option>");
      for(int i = 0; i < teachers.size(); i++)
      {
         Faculty teacher = (Faculty)teachers.get(i);
         long id = teacher.getFacultyId();
         String teacherName = "";
         if(!Utility.isEmpty(teacher.getChineseName()))
            teacherName = teacherName + teacher.getChineseName() + " ";
         teacherName = teacherName + teacher.getFirstName() + " "
               + teacher.getLastName();
         html.append("<option value='" + id + "'");
         if(id == teacherId)
            html.append(" selected");
         html.append(" >").append(teacherName).append("</option>\n");
      }
      return html.toString();
   }
   public String getClassRoom()
   {
      return classRoom;
   }
}
