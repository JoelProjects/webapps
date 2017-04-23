package datamodel;
/**
 * This is a class representing data record of user account in the database.
 */
public class UserAccount
{
   private String username;
   private String password;
   private long userId;
   private String role;
   public String getUsername()
   {
      return username;
   }
   public void setUsername(String username)
   {
      this.username = username;
   }
   public String getPassword()
   {
      return password;
   }
   public void setPassword(String password)
   {
      this.password = password;
   }
   public long getUserId()
   {
      return userId;
   }
   public void setUserId(long userId)
   {
      this.userId = userId;
   }
   public String getRole()
   {
      return role;
   }
   public void setRole(String role)
   {
      this.role = role;
   }
}
