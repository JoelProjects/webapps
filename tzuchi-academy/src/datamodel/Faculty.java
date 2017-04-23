/* $Id: Faculty.java,v 1.1 2006/07/21 06:41:20 joelchou Exp $*/

package datamodel;

/*
 *class for school Faculty's infomation
 */

public class Faculty
{
  private long facultyId = -1;
  private String type = "";
  private String firstName = "";
  private String lastName = "";
  private String chineseName = "";
  private String street = "";
  private String apt = "";
  private String city = "";
  private String state = "";
  private String zip = "";
  private String areaCode = "";
  private String phone = "";
  private String email = "";
  private String mobilePhone ="";
  
  
  /*
   * get private member
   */
  
  public long getFacultyId()
  {
    return facultyId;
  }
  
  public String getType()
  {
    return type;
  }
  
  public String getFirstName()
  {
    return firstName;
  }
  
  public String getLastName()
  {
    return lastName;
  }
  
  public String getChineseName()
  {
    return chineseName;
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
    return state;
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
  
  public String getMobilePhone()
  {
    return mobilePhone;
  }
  
  /*
   *setting private member
   */
  
  public void setFacultyId(long id)
  {
    facultyId=id;
  }
  
  public void setType(String Type)
  {
    type=Type;
  }
  
  public void setFirstName(String FirstName)
  {
    firstName=FirstName;
  }
  
  public void setLastName(String LastName)
  {
    lastName=LastName;
  }
  
  public void setChineseName(String chineseName)
  {
    this.chineseName=chineseName;
  }
  
  public void setStreet(String Street)
  {
    street=Street;
  }
  
  public void setApt(String Apt)
  {
    apt=Apt;
  }
  
  public void setCity(String City)
  {
    city=City;
  }
  
  public void setState(String State)
  {
    state=State;
  }
  
  public void setZip(String Zip)
  {
    zip=Zip;
  }
  
  public void setAreaCode(String AreaCode)
  {
    areaCode=AreaCode;
  }
  
  public void setPhone(String Phone)
  {
    phone=Phone;
  }
  
  public void setEmail(String Email)
  {
    email=Email;
  }
  
  public void setMobilePhone(String MobilePhone)
  {
    mobilePhone=MobilePhone;
  }
    
}


