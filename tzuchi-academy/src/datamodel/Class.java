package datamodel;

/**
 * This is for available classes.
 */
public class Class
{
  private int classId;
  private String name;
  private int category;
  private String description = "";
  private int maxStudents = -1;
  
  public int getClassId()
  {
    return classId;
  }
  
  public void setClassId(int classId)
  {
    this.classId = classId;
  }
  
  public String getName()
  {
    return name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }

  public int getCategory()
  {
    return category;
  }
  
  public void setCategory(int category)
  {
    this.category = category;
  }

  public String getDescription()
  {
    return description;
  }
  
  public void setDescription(String description)
  {
    this.description = description;
  }

  public int getMaxStudents()
  {
    return maxStudents;
  }
  
  public void setMaxStudents(int maxStudents)
  {
    this.maxStudents = maxStudents;
  }  
}
