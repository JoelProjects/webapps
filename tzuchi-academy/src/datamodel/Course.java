package datamodel;

/**
 * This is for courses in a specific semester.
 */
public class Course
{
  private int courseId;
  private long primaryTeacherId = -1;
  private int year;
  private String semester;
  private int classId;
  private String classRoom = "";
  private long secondaryTeacherId = -1;
  
  public int getCourseId()
  {
    return courseId;
  }
  
  public void setCourseId(int courseId)
  {
    this.courseId = courseId;
  }
  
  public long getPrimaryTeacherId()
  {
    return primaryTeacherId;
  }
  
  public void setPrimaryTeacherId(long primaryTeacherId)
  {
    this.primaryTeacherId = primaryTeacherId;
  }

  public int getYear()
  {
    return year;
  }
  
  public void setYear(int year)
  {
    this.year = year;
  }

  public String getSemester()
  {
    return semester;
  }
  
  public void setSemester(String semester)
  {
    this.semester = semester;
  }

  public int getClassId()
  {
    return classId;
  }
  
  public void setClassId(int classId)
  {
    this.classId = classId;
  }  

  public String getClassRoom()
  {
    return classRoom;
  }
  
  public void setClassRoom(String classRoom)
  {
    this.classRoom = classRoom;
  }

  public long getSecondaryTeacherId()
  {
    return secondaryTeacherId;
  }
  
  public void setSecondaryTeacherId(long secondaryTeacherId)
  {
    this.secondaryTeacherId = secondaryTeacherId;
  }
}
