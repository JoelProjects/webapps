package datamodel;

/**
 * This is for class registration info.
 */
public class StudentReg
{
  private int courseId;
  private long studentId;
  
  public int getCourseId()
  {
    return courseId;
  }
  
  public void setCourseId(int courseId)
  {
    this.courseId = courseId;
  }
  
  public long getStudentId()
  {
    return studentId;
  }
  
  public void setStudentId(long studentId)
  {
    this.studentId = studentId;
  }
}
