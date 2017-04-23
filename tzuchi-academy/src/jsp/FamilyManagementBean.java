package jsp;
import java.sql.Connection;
import java.sql.SQLException;
import datamodel.FamilyInfo;
import datamodel.FamilyInfoManager;
import datamodel.Relative;
import datamodel.RelativeManager;
public class FamilyManagementBean
{
   private String street = "";
   private String apt = "";
   private String city = "";
   private String state = "";
   private String zip = "";
   private String areaCode = "";
   private String phone = "";
   private String email = "";
   private String gd1Relation = "";
   private String gd1FirstName = "";
   private String gd1LastName = "";
   private String gd1ChineseName = "";
   private String gd1WorkPhone = "";
   private String gd1MobilePhone = "";
   private String gd2Relation = "";
   private String gd2FirstName = "";
   private String gd2LastName = "";
   private String gd2ChineseName = "";
   private String gd2WorkPhone = "";
   private String gd2MobilePhone = "";
   private String insureCo = "";
   private String doctorName = "";
   private String doctorPhone = "";
   private String er1Name = "";
   private String er1Phone = "";
   private String er2Name = "";
   private String er2Phone = "";
   public void getDataById(Connection con, long familyId)
   {
      FamilyInfoManager familyMgr = new FamilyInfoManager();
      FamilyInfo family = null;
      try
      {
         family = familyMgr.getDataById(con, familyId);
      }
      catch(SQLException e)
      {
         System.out.println("Error in getting family's info");
      }
      if(family == null)
         return;
      // general family info
      street = family.getStreet();
      apt = family.getApt();
      city = family.getCity();
      state = family.getState();
      zip = family.getZip();
      areaCode = family.getAreaCode();
      phone = family.getPhone();
      email = family.getEmail();
      insureCo = family.getInsureCo();
      RelativeManager relativeMgr = new RelativeManager();
      // get guardian1's info
      try
      {
         Relative rel = relativeMgr.getDataById(con, familyId,
               FamilyInfoManager.GUARDIAN1_ID);
         gd1Relation = rel.getRelationName();
         gd1FirstName = rel.getFirstName();
         gd1LastName = rel.getLastName();
         gd1ChineseName = rel.getChineseName();
         gd1WorkPhone = rel.getWorkPhone();
         gd1MobilePhone = rel.getMobilePhone();
      }
      catch(Exception e)
      {
         System.out.println("Error in getting guardian1's info");
      }
      // get guardian2's info 
      try
      {
         Relative rel = relativeMgr.getDataById(con, familyId,
               FamilyInfoManager.GUARDIAN2_ID);
         gd2Relation = rel.getRelationName();
         gd2FirstName = rel.getFirstName();
         gd2LastName = rel.getLastName();
         gd2ChineseName = rel.getChineseName();
         gd2WorkPhone = rel.getWorkPhone();
         gd2MobilePhone = rel.getMobilePhone();
      }
      catch(Exception e)
      {
         System.out.println("Error in getting guardian2's info");
      }
      // get family doctor's info 
      try
      {
         Relative rel = relativeMgr.getDataById(con, familyId,
               FamilyInfoManager.FAMILY_DOC_ID);
         doctorName = rel.getLastName();
         doctorPhone = rel.getWorkPhone();
      }
      catch(Exception e)
      {
         System.out.println("Error in getting doctor's info");
      }
      // get emergency1's info 
      try
      {
         Relative rel = relativeMgr.getDataById(con, familyId,
               FamilyInfoManager.EMERGENCY1_ID);
         er1Name = rel.getLastName();
         er1Phone = rel.getWorkPhone();
      }
      catch(Exception e)
      {
         System.out.println("Error in getting emergency1's info");
      }
      // get emergency2's info 
      try
      {
         Relative rel = relativeMgr.getDataById(con, familyId,
               FamilyInfoManager.EMERGENCY2_ID);
         er2Name = rel.getLastName();
         er2Phone = rel.getWorkPhone();
      }
      catch(Exception e)
      {
         System.out.println("Error in getting emergency2's info");
      }
   }
   public String getStreet()
   {
      return street;
   }
   public String getApt()
   {
      return apt;
   }
   public String getCity()
   {
      return city;
   }
   public String getState()
   {
      String[] states =
      {"MA", "NH", "RI"};
      StringBuffer optionStr = new StringBuffer();
      // form OPTION part of HTML SELECT
      for(int i = 0; i < states.length; i++)
      {
         optionStr.append("<OPTION");
         if(states[i].equals(state)) // if it's selected state
            optionStr.append(" selected");
         optionStr.append(">").append(states[i]).append("</OPTION>\n");
      }
      return optionStr.toString();
   }
   public String getZip()
   {
      return zip;
   }
   public String getAreaCode()
   {
      return areaCode;
   }
   public String getPhone()
   {
      return phone;
   }
   public String getEmail()
   {
      return email;
   }
   public String getInsureCo()
   {
      return insureCo;
   }
   public String getGd1Relation()
   {
      return gd1Relation;
   }
   public String getGd1FirstName()
   {
      return gd1FirstName;
   }
   public String getGd1LastName()
   {
      return gd1LastName;
   }
   public String getGd1ChineseName()
   {
      return gd1ChineseName;
   }
   public String getGd1WorkPhone()
   {
      return gd1WorkPhone;
   }
   public String getGd1MobilePhone()
   {
      return gd1MobilePhone;
   }
   public String getGd2Relation()
   {
      return gd2Relation;
   }
   public String getGd2FirstName()
   {
      return gd2FirstName;
   }
   public String getGd2LastName()
   {
      return gd2LastName;
   }
   public String getGd2ChineseName()
   {
      return gd2ChineseName;
   }
   public String getGd2WorkPhone()
   {
      return gd2WorkPhone;
   }
   public String getGd2MobilePhone()
   {
      return gd2MobilePhone;
   }
   public String getDoctorName()
   {
      return doctorName;
   }
   public String getDoctorPhone()
   {
      return doctorPhone;
   }
   public String getEr1Name()
   {
      return er1Name;
   }
   public String getEr1Phone()
   {
      return er1Phone;
   }
   public String getEr2Name()
   {
      return er2Name;
   }
   public String getEr2Phone()
   {
      return er2Phone;
   }
}
