package datamodel;
public class FamilyInfo
{
   private long familyId = -1;
   private int guardian1 = FamilyInfoManager.GUARDIAN1_ID;
   private int guardian2 = FamilyInfoManager.GUARDIAN2_ID;
   private String street = "";
   private String apt = "";
   private String city = "";
   private String state = "";
   private String zip = "";
   private String areaCode = "";
   private String phone = "";
   private String email = "";
   private String insureCo = "";
   private int familyDoc = FamilyInfoManager.FAMILY_DOC_ID;
   private int emergency1 = FamilyInfoManager.EMERGENCY1_ID;
   private int emergency2 = FamilyInfoManager.EMERGENCY2_ID;
   public long getFamilyId()
   {
      return familyId;
   }
   public void setFamilyId(long familyId)
   {
      this.familyId = familyId;
   }
   public int getGuardian1()
   {
      return guardian1;
   }
   public void setGuardian1(int guardian1)
   {
      this.guardian1 = guardian1;
   }
   public int getGuardian2()
   {
      return guardian2;
   }
   public void setGuardian2(int guardian2)
   {
      this.guardian2 = guardian2;
   }
   public String getStreet()
   {
      return street;
   }
   public void setStreet(String street)
   {
      this.street = street;
   }
   public String getApt()
   {
      return apt;
   }
   public void setApt(String apt)
   {
      this.apt = apt;
   }
   public String getCity()
   {
      return city;
   }
   public void setCity(String city)
   {
      this.city = city;
   }
   public String getState()
   {
      return state;
   }
   public void setState(String state)
   {
      this.state = state;
   }
   public String getZip()
   {
      return zip;
   }
   public void setZip(String zip)
   {
      this.zip = zip;
   }
   public String getAreaCode()
   {
      return areaCode;
   }
   public void setAreaCode(String areaCode)
   {
      this.areaCode = areaCode;
   }
   public String getPhone()
   {
      return phone;
   }
   public void setPhone(String phone)
   {
      this.phone = phone;
   }
   public String getEmail()
   {
      return email;
   }
   public void setEmail(String email)
   {
      this.email = email;
   }
   public String getInsureCo()
   {
      return insureCo;
   }
   public void setInsureCo(String insureCo)
   {
      this.insureCo = insureCo;
   }
   public int getFamilyDoc()
   {
      return familyDoc;
   }
   public void setFamilyDoc(int familyDoc)
   {
      this.familyDoc = familyDoc;
   }
   public int getEmergency1()
   {
      return emergency1;
   }
   public void setEmergency1(int emergency1)
   {
      this.emergency1 = emergency1;
   }
   public int getEmergency2()
   {
      return emergency2;
   }
   public void setEmergency2(int emergency2)
   {
      this.emergency2 = emergency2;
   }
}