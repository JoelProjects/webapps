package datamodel;

public class Group
{
   private int groupId;
   private String groupName;
   private int parentId;
   private int levelId;

   public int getGroupId()
   {
      return groupId;
   }

   public void setGroupId(int groupId)
   {
      this.groupId = groupId;
   }

   public String getGroupName()
   {
      return groupName;
   }

   public void setGroupName(String groupName)
   {
      this.groupName = groupName;
   }

   public int getParentId()
   {
      return parentId;
   }

   public void setParentId(int parentId)
   {
      this.parentId = parentId;
   }

   public int getLevelId()
   {
      return levelId;
   }

   public void setLevelId(int levelId)
   {
      this.levelId = levelId;
   }
}
