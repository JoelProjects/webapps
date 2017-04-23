package util;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
public class DatabaseAccess
{
   private String configDir; // directory for configuration file
   private String dsDriver = null;
   private String dsProtocol = null;
   private String dsSubprotocol = null;
   private String dsName = null;
   private String dsUsername = null;
   private String dsPassword = null;
   private DataSource defaultDS = null;
   public DatabaseAccess()
   {
   }
   /**
    * @param name resource reference name for default data source
    */
   public DatabaseAccess(String name) throws Exception
   {
      defaultDS = getDataSource(name);
   }
   /**
    * Gets database connection through a data source configuration file. A Connection 
    * to this data source will be returned.
    * 
    * @param source data source name 
    * @param configFile data source configuration file name
    * @return a Connection object
    */
   public Connection getConnection(String source, String configFile)
            throws SQLException, Exception
   {
      Connection con = null;
      try
      {
         if(configFile != null)
         {
            //InputStream inFile = 
            //  this.getClass().getResourceAsStream(configFile);
            InputStream inFile = new File(configFile).toURL().openStream();
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
            {
               throw new Exception("* Can not find property file");
            }
         }
         return con;
      }
      catch(ClassNotFoundException e)
      {
         throw new Exception("* Can not find driver class '" + dsDriver + "'!");
      }
   }
   /**
    * Gets database connection through JNDI lookup. A Connection to this data source
    * will be returned.
    * 
    * @param name resource reference name for this data source
    * @return a Connection object
    */
   public Connection getConnection(String name) throws SQLException, Exception
   {
      Connection con = null;
      DataSource ds = getDataSource(name);
      if(ds != null)
         con = ds.getConnection();
      return con;
   }
   /**
    * Gets database connection by using the default data source. A Connection to 
    * this data source will be returned.
    * 
    * @return a Connection object
    */
   public Connection getConnection() throws SQLException
   {
      Connection con = null;
      if(defaultDS != null)
         con = defaultDS.getConnection();
      return con;
   }
   private DataSource getDataSource(String name) throws Exception
   {
      DataSource ds = null;
      try
      {
         Context ctx = new InitialContext();
         ctx = (Context)ctx.lookup("java:comp/env");
         ds = (DataSource)ctx.lookup(name);
      }
      catch(Exception e)
      {
         throw new Exception("Error while getting data source for JNDI name: "
                  + name);
      }
      return ds;
   }
}
