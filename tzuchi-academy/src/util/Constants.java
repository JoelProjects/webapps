package util;

public interface Constants
{
  /**
   * Defines location of data source configuration.
   */
  public static String DATA_SOURCE = "../datasource.config";
  
  /**
   * Defines resource reference name for data source
   */
  public static final String DS_REF_NAME = "jdbc/tcads";  
  
  /**
   * static semester string define 
   */ 
  public static String FALL = "Fall";
  public static String SPRING = "Spring";
  public static String FULL = "FULL";
  
  /**
   * Defines the School Term
   */ 
  public static String [] SCHOOL_TERMS = {"Fall", "Spring"};
  
  /**
   * Defines payment types
   */ 
  public static String[] PAYMENT_TYPES = {"Check", "Cash", "Check+Cash"};  
  
  /**
   * Defines of the Payment Term for Tuition
   */
  public static String [] PAYMENT_TERMS = {FALL, SPRING, FULL};
  
  /**
   * Define of all of school years.
   */
  public static int [] SCHOOL_YEARS = {2007, 2006, 2005, 2004, 2003};  
}
