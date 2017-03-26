package jsp;
import java.sql.Connection;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import util.Constants;
import util.DatabaseAccess;
// to keep database connection alive until the bean is removed from the session
public class ConnectionBean implements HttpSessionBindingListener
{
   private Connection con;
   public ConnectionBean()
   {
      try
      {
         createConnection();
      }
      catch(Exception e)
      {
         System.out.println(e);
      }
   }
   public Connection getConnection()
   {
      return con;
   }
   public void valueBound(HttpSessionBindingEvent event)
   {
      System.out.println("Create database connection: " + event.getName());
      try
      {
         if(con == null || con.isClosed())
         {
            createConnection();
         }
      }
      catch(Exception e)
      {
         con = null;
         System.out.println(e);
      }
   }
   public void valueUnbound(HttpSessionBindingEvent event)
   {
      System.out.println("Close database connection: " + event.getName());
      try
      {
         con.close();
      }
      catch(Exception e)
      {
         con = null;
      }
   }
   private void createConnection() throws Exception
   {
      // connect to the database
      DatabaseAccess db = new DatabaseAccess();
      con = db.getConnection("config", Constants.DATA_SOURCE);
   }
}
