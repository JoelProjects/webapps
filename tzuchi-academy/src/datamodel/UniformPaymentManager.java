package datamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author Cheng-Hung Chou
 * @since 5/8/2005
 */
public class UniformPaymentManager
{
   public static String TABLE_NAME = "uniform_payment";
   public static String COL_STUDENT_ID = "student_id";
   public static String COL_YEAR = "year";
   public static String COL_SEMESTER = "semester";
   public static String COL_AMOUNT = "amount";
   public static String COL_CHECK_NO = "check_no";
   public static String COL_PAYMENT_TYPE_ID = "payment_type_id";
   public static String COL_WINTER_SIZE = "winter_size";
   public static String COL_SUMMER_SIZE = "summer_size";

   /**
    * Gets uniform payment for a student. An UniformPayment object will be returned.
    *
    * @param con a database connection
    * @param studnetId student ID
    * @return uniform payment
    */
   public UniformPayment getPayment(Connection con, long studentId, int year, String semester)
         throws SQLException
   {
      UniformPayment payment = null;

      try
      {
         String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
               + COL_STUDENT_ID + "=? AND " + COL_YEAR + "=? AND " + COL_SEMESTER + "=?";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setLong(1, studentId);
         pstmt.setInt(2, year);
         pstmt.setString(3, semester);         
         ResultSet rs = pstmt.executeQuery();

         if(rs.next())
         {
            payment = getUniformPayment(rs);
         }
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }

      return payment;
   } 
   
   /**
    * Inserts a new record into the table by providing an UniformPayment object.
    * 
    * @param con a database connection
    * @param user an UniformPayment object to be inserted
    * @return the row count
    */
   public int insert(Connection con, UniformPayment payment) throws SQLException
   {
      int result = -1;

      try
      {
         if(payment == null)
            return result;

         String query = "INSERT INTO " + TABLE_NAME + " (" + COL_AMOUNT + ","
               + COL_CHECK_NO + "," + COL_PAYMENT_TYPE_ID + "," + COL_SEMESTER
               + "," + COL_STUDENT_ID + "," + COL_SUMMER_SIZE + "," + COL_WINTER_SIZE
               + "," + COL_YEAR
               + ") VALUES (?,?,?,?,?,?,?,?)";

         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setDouble(1, payment.getAmount());
         pstmt.setString(2, payment.getCheckNo());
         pstmt.setInt(3, payment.getPaymentTypeId());
         pstmt.setString(4, payment.getSemester());
         pstmt.setLong(5, payment.getStudentId());         
         pstmt.setString(6, payment.getSummerSize());
         pstmt.setString(7, payment.getWinterSize());
         pstmt.setInt(8, payment.getYear());     

         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }

      return result;
   }   
   
   /**
    * Updates uniform payment.
    * 
    * @param con a database connection
    * @param user an UniformPayment object to be inserted
    * @return the row count
    */
   public int update(Connection con, UniformPayment payment) throws SQLException
   {
      int result = -1;

      try
      {
         if(payment == null)
            return result;

         String query = "UPDATE " + TABLE_NAME + " SET " + COL_AMOUNT + "=?,"
               + COL_CHECK_NO + "=?," + COL_PAYMENT_TYPE_ID + "=?," 
               + COL_SUMMER_SIZE + "=?," + COL_WINTER_SIZE + "=?"
               + " WHERE " + COL_STUDENT_ID + "=?"
               + " AND " + COL_YEAR + "=? AND " + COL_SEMESTER + "=?";

         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setDouble(1, payment.getAmount());
         pstmt.setString(2, payment.getCheckNo());
         pstmt.setInt(3, payment.getPaymentTypeId());
         pstmt.setString(4, payment.getSummerSize());
         pstmt.setString(5, payment.getWinterSize());
         pstmt.setLong(6, payment.getStudentId());
         pstmt.setInt(7, payment.getYear());     
         pstmt.setString(8, payment.getSemester());

         result = pstmt.executeUpdate();
      }
      catch(SQLException e)
      {
         throw new SQLException(e.toString());
      }

      return result;
   }      
   
   public UniformPayment getUniformPayment(ResultSet rs) throws SQLException
   {
      UniformPayment payment = new UniformPayment();
      payment.setAmount(rs.getDouble(COL_AMOUNT));
      payment.setCheckNo(rs.getString(COL_CHECK_NO));
      payment.setPaymentTypeId(rs.getInt(COL_PAYMENT_TYPE_ID));
      payment.setSemester(rs.getString(COL_SEMESTER));
      payment.setSummerSize(rs.getString(COL_SUMMER_SIZE));
      payment.setWinterSize(rs.getString(COL_WINTER_SIZE));
      payment.setYear(rs.getInt(COL_YEAR));     
      payment.setStudentId(rs.getLong(COL_STUDENT_ID));
      
      return payment;
   }
}
