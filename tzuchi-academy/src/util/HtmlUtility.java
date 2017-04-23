package util;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 5/14/2005
 */
public class HtmlUtility
{
   public static String getUniformSizeOptions(String uniformSize)
   {
      String[] sizes =
         {"", "S", "M", "L", "XL"};
      StringBuffer optionStr = new StringBuffer();

      // form OPTION part of HTML SELECT
      for(int i = 0; i < sizes.length; i++)
      {
         optionStr.append("<option");
         if(sizes[i].equals(uniformSize)) // if it's selected one
            optionStr.append(" selected");

         optionStr.append(">").append(sizes[i]).append("</option>\n");
      }

      return optionStr.toString();
   }   
   
   public static String getPaymentTypeOptions(int typeId)
   {
      StringBuffer optionStr = new StringBuffer();
      optionStr = optionStr.append("<option value=-1></option>\n");
      // form OPTION part of HTML SELECT
      for(int i = 0; i < Constants.PAYMENT_TYPES.length; i++)
      {
         int value = i + 1;
         optionStr.append("<option value=").append(value);
         if(value == typeId) // if it's selected one
            optionStr.append(" selected");

         optionStr.append(">").append(Constants.PAYMENT_TYPES[i]).append("</option>\n");
      }

      return optionStr.toString();
   }      
}
