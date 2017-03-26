package util;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class DatabaseAccess
{
   private String configDir; // directory for configuration file
   private String dsDriver = null;
   private String dsProtocol = null;
   private String dsSubprotocol = null;
   private String dsName = null;
   private String dsUsername = null;
   private String dsPassword = null;
   // source: data source name 
   // configFile: data source configuration file name
   public Connection getConnection(String source, String configFile)
   throws SQLException, Exception
   {
      Connection con;
      try
      {
         InputStream inFile =
            this.getClass().getResourceAsStream(configFile);
         Properties prop = new Properties();
         prop.load(inFile);
         if(prop != null)
         {
            dsDriver = prop.getProperty(source + ".driver");
            dsProtocol = prop.getProperty(source + ".protocol");
            dsSubprotocol = prop.getProperty(source + ".subprotocol");
            if(dsName == null)
               dsName = prop.getProperty(source + ".dsname");
            if(dsUsername == null)
               dsUsername = prop.getProperty(source + ".username");
            if(dsPassword == null)
               dsPassword = prop.getProperty(source + ".password");
            // load driver class
            Class.forName(dsDriver);
            // connect to data source
            String url = dsProtocol + ":" + dsSubprotocol + ":" + dsName;
            con = DriverManager.getConnection(url, dsUsername, dsPassword);
            inFile.close();
         }
         else
            throw new Exception("* Can not find property file");
         return con;
      }
      catch(ClassNotFoundException e)
      {
         throw new Exception("* Can not find driver class '" + dsDriver + "'!");
      }
   }
}
