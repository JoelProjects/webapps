package datamodel;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 5/8/2005
 */
public class UniformPayment
{
   private long studentId;
   private int year;
   private String semester;
   private double amount;
   private String checkNo;
   private int paymentTypeId;
   private String winterSize;
   private String summerSize;
   
   /**
    * @return Returns the amount.
    */
   public double getAmount()
   {
      return amount;
   }
   
   /**
    * @param amount The amount to set.
    */
   public void setAmount(double amount)
   {
      this.amount = amount;
   }
   
   /**
    * @return Returns the checkNo.
    */
   public String getCheckNo()
   {
      return checkNo;
   }
   
   /**
    * @param checkNo The checkNo to set.
    */
   public void setCheckNo(String checkNo)
   {
      this.checkNo = checkNo;
   }
   
   /**
    * @return Returns the paymentTypeId.
    */
   public int getPaymentTypeId()
   {
      return paymentTypeId;
   }
   
   /**
    * @param paymentType The paymentTypeId to set.
    */
   public void setPaymentTypeId(int paymentTypeId)
   {
      this.paymentTypeId = paymentTypeId;
   }
   
   /**
    * @return Returns the semester.
    */
   public String getSemester()
   {
      return semester;
   }
   
   /**
    * @param semester The semester to set.
    */
   public void setSemester(String semester)
   {
      this.semester = semester;
   }
   
   /**
    * @return Returns the year.
    */
   public int getYear()
   {
      return year;
   }
   
   /**
    * @param year The year to set.
    */
   public void setYear(int year)
   {
      this.year = year;
   }
   
   /**
    * @return Returns the studentId.
    */
   public long getStudentId()
   {
      return studentId;
   }
   
   /**
    * @param studentId The studentId to set.
    */
   public void setStudentId(long studentId)
   {
      this.studentId = studentId;
   }
   
   /**
    * @return Returns the summerSize.
    */
   public String getSummerSize()
   {
      return summerSize;
   }
   
   /**
    * @param summerSize The summerSize to set.
    */
   public void setSummerSize(String summerSize)
   {
      this.summerSize = summerSize;
   }
   
   /**
    * @return Returns the winterSize.
    */
   public String getWinterSize()
   {
      return winterSize;
   }
   
   /**
    * @param winterSize The winterSize to set.
    */
   public void setWinterSize(String winterSize)
   {
      this.winterSize = winterSize;
   }
}
