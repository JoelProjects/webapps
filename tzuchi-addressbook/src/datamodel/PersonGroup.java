package datamodel;

public class PersonGroup
{
   private int personId = 0;
   private int groupId = 0;
   private int personType = PersonGroupManager.GROUP_MEMBER;

   public int getPersonId()
   {
      return personId;
   }

   public void setPersonId(int personId)
   {
      this.personId = personId;
   }

   public int getGroupId()
   {
      return groupId;
   }

   public void setGroupId(int groupId)
   {
      this.groupId = groupId;
   }

   public int getPersonType()
   {
      return personType;
   }

   public void setPersonType(int personType)
   {
      this.personType = personType;
   }
}
