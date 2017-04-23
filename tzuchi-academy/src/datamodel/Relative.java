package datamodel;
public class Relative
{
   private long familyId = -1;
   private String firstName = "";
   private String lastName = "";
   private String chineseName = "";
   private String workPhone = "";
   private int relationId = -1;
   private String relationName = "";
   private String mobilePhone = "";
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
   public String getWorkPhone()
   {
      return workPhone;
   }
   public void setWorkPhone(String workPhone)
   {
      this.workPhone = workPhone;
   }
   public int getRelationId()
   {
      return relationId;
   }
   public void setRelationId(int relationId)
   {
      this.relationId = relationId;
   }
   public String getRelationName()
   {
      return relationName;
   }
   public void setRelationName(String relationName)
   {
      this.relationName = relationName;
   }
   public String getMobilePhone()
   {
      return mobilePhone;
   }
   public void setMobilePhone(String mobilePhone)
   {
      this.mobilePhone = mobilePhone;
   }
}
