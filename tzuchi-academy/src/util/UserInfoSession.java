package util;
import java.sql.Connection;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import datamodel.UserAccount;
import datamodel.UserAccountManager;
/**
 * This class will be used to create a session object name userInfo.
 */
public class UserInfoSession
{
   public static String SESSION_NAME = "userInfo";
   public static String USER_ID = "userId";
   public static String USERNAME = "username";
   public static int createUserInfoSession(HttpServletRequest request,
         String username) throws Exception
   {
      Connection con = null;
      try
      {
         // connect to the database
         DatabaseAccess db = new DatabaseAccess();
         con = db.getConnection(Constants.DS_REF_NAME);
         UserAccountManager userMgr = new UserAccountManager();
         UserAccount user = userMgr.getUserByName(con, username);
         if(user != null)
         {
            // create user info session
            HashMap userInfo = new HashMap();
            HttpSession session = request.getSession(true);
            long userId = user.getUserId();
            userInfo.put(USER_ID, new Long(userId)); // user ID
            userInfo.put(USERNAME, user.getUsername()); // username
            session.setAttribute(SESSION_NAME, userInfo);
         }
         else
         {
            // if can not find user ID
            throw new Exception(
                  "Error in creating Session userInfo: user ID does not exist.");
         }
      }
      catch(Exception e)
      {
         throw new Exception("Error in creating Session userInfo\n" + e);
      }
      finally
      {
         try
         {
            con.close();
         }
         catch(Exception e)
         {
         }
      }
      return 0;
   }
}
