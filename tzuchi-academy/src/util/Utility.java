package util;

import java.util.*;
import java.text.SimpleDateFormat;
import java.security.*;

/**
 * This class contains common methods will be used.
 */
public class Utility
{
  /**
   * Checks if a string is empty (null or of size 0 after removing white space
   * from both ends).
   *
   * @param str the string to be checked
   * @return true if it's empty
   */
  public static boolean isEmpty(String str)
  {
    if(str == null || str.trim().length() == 0)
      return true;
    else
      return false;
  }

  /**
   * Trims a string. If it's empty, empty string will be returned.
   *
   * @param str the string to be trimmed
   * @return trimmed string
   */
  public static String trim(String str)
  {
    if(isEmpty(str))
      return "";
    else
      return str.trim();
  }

  static public String getDateTimeString(Date d)
  {
    SimpleDateFormat formatter = new SimpleDateFormat();
    formatter.applyPattern("MM/dd/yyyy HH:mm:ss");
    return formatter.format(d);
  }

  static public String getDateString(Date d)
  {
    SimpleDateFormat formatter = new SimpleDateFormat();
    formatter.applyPattern("MM/dd/yyyy");
    return formatter.format(d);
  }

  /**
   * Returns a string with current date and time information with format
   * like yyyyMMddHHmmssSSS.
   *
   * @return a date time string
   */
  public static String getLongDateTimeStr()
  {
    return dateFormatter("yyyyMMddHHmmssSSS");
  }

  /**
   * Returns a string with current date information with a specified format.
   *
   * @param pattern a pattern to the date format
   * @return a date string
   */
  public static String dateFormatter(String pattern)
  {
    SimpleDateFormat formatter = new SimpleDateFormat(pattern);
    Date date = new Date();
    return formatter.format(date);
  }
  
  public static String big5ToUnicode(String big5)
  {
    String unicode = "";
    
    if(!isEmpty(big5))
    {
      try
      {
        unicode = new String(big5.getBytes("ISO-8859-1"), "BIG5");
      }
      catch(java.io.UnsupportedEncodingException e)
      {
        System.out.println(e);
      }
    }
    
    return unicode;
  }

  public static String digestPassword(String password)
  {
    String digest = null;

    try
    {
      // use message digest to encrypt password and the algorithm is MD5
      MessageDigest md = (MessageDigest)MessageDigest.getInstance("MD5").clone();
      md.update(password.getBytes());
      byte[] byteDigest = md.digest();
      StringBuffer strBuffer = new StringBuffer(byteDigest.length * 2);
      // convert to hex
      for(int i = 0; i < byteDigest.length; i++)
      {
        strBuffer.append(convertDigit(byteDigest[i] >> 4));
        strBuffer.append(convertDigit(byteDigest[i] & 0xf));
      }

      digest = strBuffer.toString();
    }
    catch(Exception e)
    {
      System.out.println(e);
    }

    return digest;
  }

  private static char convertDigit(int i)
  {
    i &= 0xf;
    if(i >= 10)
      return (char)((i - 10) + 97);
    else
      return (char)(i + 48);
  }
  
  /**
   * Map the Semester string to array index on for Constants.SCHOOL_TERM
   */
  public static int strToIndex(String semester)
  {
      for(int i = 0; i < Constants.SCHOOL_TERMS.length ; i++)
      {
          if (Constants.SCHOOL_TERMS[i].compareToIgnoreCase(semester) == 0)
              return i;
      }
      
      //no matched;
      return -1;         
  }
}
