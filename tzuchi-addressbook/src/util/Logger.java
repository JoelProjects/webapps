package util;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/**
 * This class provides methods to write logging messages into the logging file.
 */
public class Logger
{
   public static void log(HttpServletRequest request, String message)
   {
      getServletContext(request).log(message);
   }
   public static void log(HttpServletRequest request, String message,
         Throwable throwable)
   {
      getServletContext(request).log(message, throwable);
   }
   private static ServletContext getServletContext(HttpServletRequest request)
   {
      HttpSession session = request.getSession();
      return session.getServletContext();
   }
}
