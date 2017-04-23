package jsp;

import java.sql.Connection;
import java.sql.SQLException;

import util.HtmlUtility;

import datamodel.UniformPayment;
import datamodel.UniformPaymentManager;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 5/14/2005
 */
public class UniformPaymentBean
{
   private String summerUniformSize = "";
   private String winterUniformSize = "";
   private int paymentTypeId = -1;
   
   public UniformPayment getUniformPayment(Connection con, long studentId,
         int year, String semester)
   {
      UniformPayment uniform = null;
      try
      {
         UniformPaymentManager mgr = new UniformPaymentManager();
         uniform = mgr.getPayment(con, studentId, year, semester);
         
         if(uniform != null)
         {
            summerUniformSize = uniform.getSummerSize();
            winterUniformSize = uniform.getWinterSize(); 
            paymentTypeId = uniform.getPaymentTypeId();
         }
      }
      catch(SQLException e)
      {
         System.out.println(e.toString());
      }
      
      return uniform;
   }
   
   public String getSummerUniformSizeOptions()
   {
      return HtmlUtility.getUniformSizeOptions(summerUniformSize);
   }

   public String getWinterUniformSizeOptions()
   {
      return HtmlUtility.getUniformSizeOptions(winterUniformSize);
   }
   
   public String getPaymentTypeOptions()
   {
      return HtmlUtility.getPaymentTypeOptions(paymentTypeId);
   }   
}
