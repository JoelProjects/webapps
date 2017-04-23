<HTML>
<HEAD>
<TITLE>Family Editor</TITLE>
<LINK rel="stylesheet" href="../tzuchi.css" type="text/css">
</HEAD>
<SCRIPT LANGUAGE="javascript" SRC="../utility.js">
</SCRIPT>
<script language="javascript">
function goBack(id)
{
  if(id == '0')
    document.myForm.action="studentManagement.jsp";  
  else
    document.myForm.action="studentList.jsp?familyId=" + id;
  document.myForm.submit();
}
function validateInput() 
{
  /* check required fields */
  var mesg = "";
  if(trim(document.myForm.street.value) == '')
    mesg =  mesg + "Street is required\n";
    
  if(trim(document.myForm.city.value) == '')
    mesg =  mesg + "City is required\n";
  if(document.myForm.state.selectedIndex == -1)
    mesg =  mesg + "State is required\n";
  if(trim(document.myForm.zip.value) == '')
    mesg =  mesg + "Zip Code is required\n";
  if(trim(document.myForm.areaCode.value) == '')
    mesg =  mesg + "Area Code is required\n";
  if(trim(document.myForm.phone.value) == '')
    mesg =  mesg + "Phone is required\n";
  if(trim(document.myForm.gd1Relation.value) == '')
    mesg =  mesg + "Relationship for Guardian 1 is required\n";
  if(trim(document.myForm.gd1ChineseName.value) == '' &&
     (trim(document.myForm.gd1LastName.value) == '' ||
     trim(document.myForm.gd1FirstName.value) == '')) 
    mesg =  mesg + "Either Chinese or English Name for Guardian 1 should not be empty.\n";
  if(mesg != '')
  {
    alert(mesg);
    return false;
  }
  else
  {
    if(confirm("Are you sure to update this form?"))
      return true;
    else
      return false;
  }
}
</script>
<%@ page import="java.sql.*" %>
<%@ page contentType="text/html; charset=Big5" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="family" scope="page" class="jsp.FamilyManagementBean" />
<%
  Connection con = connection.getConnection();
  String familyIdStr = request.getParameter("familyId");
  if(familyIdStr != null)    
    family.getDataById(con, Long.parseLong(familyIdStr));
%>
<BODY>
<jsp:include page="adminHeader.jsp?activeTab=student"/>
<FORM name="myForm" method="post" action="../../AdminFamilyManagementServlet?action=update" onSubmit="return validateInput()">
<%
  if(familyIdStr != null)
  {
%>
<input type="hidden" name="familyId" value=<%= familyIdStr %>>
<a href="javascript:goBack('<%= familyIdStr %>')"><< Back</a>
<%
  }
  else
  {
%>
<a href="javascript:goBack('0')"><< Back</a>
<%
  }
%>
<h3>慈濟中文學校&nbsp;Tzu-Chi Academy<BR>
學生註冊表&nbsp;Student Registration Form
</h3>
<BR>
家庭資料&nbsp;Family Information (<SPAN class="asterisk">*</SPAN>: Required Field)
<HR>
家庭住址&nbsp;Street Address:&nbsp;
<INPUT type="text" name="street" size="30" value="<jsp:getProperty name="family" property="street"/>">
<SPAN class="asterisk">*</SPAN>&nbsp;&nbsp;&nbsp;Apt/Suit:&nbsp;
<INPUT type="text" name="apt" size="10" value="<jsp:getProperty name="family" property="apt"/>">
<br>
City:&nbsp;
<INPUT type="text" name="city" size="10" value="<jsp:getProperty name="family" property="city"/>"><SPAN class="asterisk">*</SPAN>
&nbsp;&nbsp;State:&nbsp;
<SELECT size="1" name="state">
<jsp:getProperty name="family" property="state"/>
</SELECT><SPAN class="asterisk">*</SPAN>
&nbsp;&nbsp;&nbsp;
Zip Code:&nbsp;<INPUT type="text" name="zip" size="10" value="<jsp:getProperty name="family" property="zip"/>"><SPAN class="asterisk">*</SPAN>
<br>
家庭電話&nbsp;Area Code:&nbsp;<INPUT type="text" name="areaCode" size="5" value="<jsp:getProperty name="family" property="areaCode"/>">
<SPAN class="asterisk">*</SPAN>&nbsp;&nbsp;&nbsp;Phone:&nbsp;<INPUT type="text" name="phone" size="15" value="<jsp:getProperty name="family" property="phone"/>">
<SPAN class="asterisk">*</SPAN><br>
E-Mail:&nbsp;<INPUT type="text" name="email" size="20" value="<jsp:getProperty name="family" property="email"/>">
<br>
<HR>
監護人&nbsp;Guardian<br>
關係&nbsp;Relationship:&nbsp;<INPUT type="text" name="gd1Relation" size="10" value="<jsp:getProperty name="family" property="gd1Relation"/>"><SPAN class="asterisk">*</SPAN><br>
First Name:&nbsp;<INPUT type="text" name="gd1FirstName" size="10" value="<jsp:getProperty name="family" property="gd1FirstName"/>">
&nbsp;&nbsp;&nbsp;Last Name:&nbsp;<INPUT type="text" name="gd1LastName" size="10" value="<jsp:getProperty name="family" property="gd1LastName"/>">
<br>
中文姓名:&nbsp;<INPUT type="text" name="gd1ChineseName" size="10" value="<jsp:getProperty name="family" property="gd1ChineseName"/>">
<br>
<!-- <P>職業&nbsp;Occupation:&nbsp;<INPUT type="text" name="fatherOccupation" size="20"></p> -->
工作電話&nbsp;Work Phone:&nbsp;<INPUT type="text" name="gd1WorkPhone" size="20" value="<jsp:getProperty name="family" property="gd1WorkPhone"/>">
&nbsp;&nbsp;&nbsp;
行動電話&nbsp;Mobile Phone:&nbsp;<INPUT type="text" name="gd1MobilePhone" size="20" value="<jsp:getProperty name="family" property="gd1MobilePhone"/>">
<br><br>
監護人&nbsp;Guardian<br>
關係&nbsp;Relationship:&nbsp;<INPUT type="text" name="gd2Relation" size="10" value="<jsp:getProperty name="family" property="gd2Relation"/>"><br>
First Name:&nbsp;<INPUT type="text" name="gd2FirstName" size="10" value="<jsp:getProperty name="family" property="gd2FirstName"/>">
&nbsp;&nbsp;&nbsp;Last Name:&nbsp;<INPUT type="text" name="gd2LastName" size="10" value="<jsp:getProperty name="family" property="gd2LastName"/>">
<br>
中文姓名:&nbsp;<INPUT type="text" name="gd2ChineseName" size="10" value="<jsp:getProperty name="family" property="gd2ChineseName"/>">
<br>
<!-- <P>職業&nbsp;Occupation:&nbsp;<INPUT type="text" name="fatherOccupation" size="20"></p> -->
工作電話&nbsp;Work Phone:&nbsp;<INPUT type="text" name="gd2WorkPhone" size="20" value="<jsp:getProperty name="family" property="gd2WorkPhone"/>">
&nbsp;&nbsp;&nbsp;
行動電話&nbsp;Mobile Phone:&nbsp;<INPUT type="text" name="gd2MobilePhone" size="20" value="<jsp:getProperty name="family" property="gd2MobilePhone"/>">
<br>
<HR>
假如發生意外事故並且人文學校聯絡不到您,請將兩位可以代您關照而且可以替您的小孩負責就醫的親戚或朋友的名字寫出來.
<br>
Should your child be hurt in an accident and we are unable to contact you, 
please list the names of two individuals who will take responsibility in seeking 
medical attention.
<br><br>
緊急聯絡&nbsp;Emergency Contacts<br>
姓名&nbsp;Name:&nbsp;<INPUT type="text" name="er1Name" size="40" value="<jsp:getProperty name="family" property="er1Name"/>">
&nbsp;&nbsp;&nbsp; 
電話&nbsp;Phone:&nbsp;<INPUT type="text" name="er1Phone" size="20" value="<jsp:getProperty name="family" property="er1Phone"/>"><br>
姓名&nbsp;Name:&nbsp;<INPUT type="text" name="er2Name" size="40" value="<jsp:getProperty name="family" property="er2Name"/>">
&nbsp;&nbsp;&nbsp; 
電話&nbsp;Phone:&nbsp;<INPUT type="text" name="er2Phone" size="20" value="<jsp:getProperty name="family" property="er2Phone"/>"><br>
<HR>
醫生和保險資料Doctor &amp; Insurance<br>
姓名&nbsp;Name:&nbsp; 
<INPUT type="text" name="doctorName" size="40" value="<jsp:getProperty name="family" property="doctorName"/>">
&nbsp;&nbsp;&nbsp; 
電話&nbsp;Phone:&nbsp; 
<INPUT type="text" name="doctorPhone" size="20" value="<jsp:getProperty name="family" property="doctorPhone"/>"><br>
保險資料&nbsp;Insurance Information:&nbsp;
<INPUT type="text" name="insureCo" size="60" value="<jsp:getProperty name="family" property="insureCo"/>"><br>
<HR>
<INPUT type="submit"><INPUT type="reset">
<P>The school students of any race to all the rights, privileges, programs, and 
activities generally accorded or made available to students at that school and 
that the school does not discriminate on the basis of race in administration of 
its educational policies, admission policies, scholarship and load programs, and 
athletic and other school-administered programs.
</P>
</FORM>
</BODY>
</HTML>
