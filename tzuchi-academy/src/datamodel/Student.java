package datamodel;
import java.sql.Date;
public class Student
{
   private long studentId = -1;
   private long familyId = -1;
   private String firstName = "";
   private String lastName = "";
   private String chineseName = "";
   private String gender = "";
   private java.util.Date birthDate = new Date(0);
   private String dayTimeSchool = "";
   private int gradeOfSchool = -1; // for day time school
   private String summerUniformSize = "";
   private boolean summerUniformReceived = false;
   private String winterUniformSize = "";
   private boolean winterUniformReceived = false;
   private String healthProblem = "";
   public long getStudentId()
   {
      return studentId;
   }
   public void setStudentId(long studentId)
   {
      this.studentId = studentId;
   }
   public long getFamilyId()
   {
      return familyId;
   }
   public void setFamilyId(long familyId)
   {
      this.familyId = familyId;
   }
   public String getFirstName()
   {
      return firstName;
   }
   public void setFirstName(String firstName)
   {
      this.firstName = firstName;
   }
   public String getLastName()
   {
      return lastName;
   }
   public void setLastName(String lastName)
   {
      this.lastName = lastName;
   }
   public String getChineseName()
   {
      return chineseName;
   }
   public void setChineseName(String chineseName)
   {
      this.chineseName = chineseName;
   }
   public String getGender()
   {
      return gender;
   }
   public void setGender(String gender)
   {
      this.gender = gender;
   }
   public java.util.Date getBirthDate()
   {
      return birthDate;
   }
   public void setBirthDate(java.util.Date birthDate)
   {
      this.birthDate = birthDate;
   }
   public String getDayTimeSchool()
   {
      return dayTimeSchool;
   }
   public void setDayTimeSchool(String dayTimeSchool)
   {
      this.dayTimeSchool = dayTimeSchool;
   }
   public int getGradeOfSchool()
   {
      return gradeOfSchool;
   }
   public void setGradeOfSchool(int gradeOfSchool)
   {
      this.gradeOfSchool = gradeOfSchool;
   }
   public String getSummerUniformSize()
   {
      return summerUniformSize;
   }
   public void setSummerUniformSize(String summerUniformSize)
   {
      this.summerUniformSize = summerUniformSize;
   }
   public boolean isSummerUniformReceived()
   {
      return summerUniformReceived;
   }
   public void setSummerUniformReceived(boolean summerUniformReceived)
   {
      this.summerUniformReceived = summerUniformReceived;
   }
   public String getWinterUniformSize()
   {
      return winterUniformSize;
   }
   public void setWinterUniformSize(String winterUniformSize)
   {
      this.winterUniformSize = winterUniformSize;
   }
   public boolean isWinterUniformReceived()
   {
      return winterUniformReceived;
   }
   public void setWinterUniformReceived(boolean winterUniformReceived)
   {
      this.winterUniformReceived = winterUniformReceived;
   }
   public String getHealthProblem()
   {
      return healthProblem;
   }
   public void setHealthProblem(String healthProblem)
   {
      this.healthProblem = healthProblem;
   }
}
