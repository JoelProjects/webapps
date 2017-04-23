<HTML>
<HEAD>
<TITLE>Student Editor</TITLE>
<LINK rel="stylesheet" href="../tzuchi.css" type="text/css">
</HEAD>
<SCRIPT LANGUAGE="javascript" SRC="../utility.js">
</SCRIPT>
<script language="javascript">
function goBack(id)
{
  document.myForm.action="studentList.jsp?familyId=" + id;
  document.myForm.submit();
}
function validateInput() 
{
  /* check required fields */
  var mesg = "";
  if(trim(document.myForm.chineseName.value) == '' &&
     (trim(document.myForm.lastName.value) == '' ||
     trim(document.myForm.firstName.value) == '')) 
    mesg =  mesg + "Either Chinese or English Name should not be empty.\n";
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
<%@ page import="datamodel.*" %>
<%@ page contentType="text/html; charset=Big5" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="student" scope="page" class="jsp.StudentManagementBean" />
<%
  Connection con = connection.getConnection();
  String familyIdStr = request.getParameter("familyId");  
  String studentIdStr = request.getParameter("studentId");
  String isMale = "";
  String isFemale = "";
  if(studentIdStr != null)
  {
    student.getDataById(con, Long.parseLong(studentIdStr));
    if(student.getGender() != null)
    {
      if(student.getGender().equals(StudentManager.MALE))
        isMale = "checked";
      if(student.getGender().equals(StudentManager.FEMALE))
        isFemale = "checked";
    }
  }
%>
<BODY>
<jsp:include page="adminHeader.jsp?activeTab=student"/>
<FORM name="myForm" method="post" action="../../AdminStudentManagementServlet?action=update" onSubmit="return validateInput()">
<input type="hidden" name="familyId" value="<%= familyIdStr %>">
<%
  if(studentIdStr != null)
  {
%>
<input type="hidden" name="studentId" value="<%= studentIdStr %>">
<%
  }
%>
<a href="javascript:goBack('<%= familyIdStr %>')"><< Back</a>
<h3>慈濟中文學校&nbsp;Tzu-Chi Academy<BR>
學生註冊表&nbsp;Student Registration Form</h3>
<BR>
學生資料&nbsp;Student Information (<SPAN class="asterisk">*</SPAN>: Required Field)
<HR>
中文姓名:&nbsp;
<INPUT type="text" name="chineseName" size="10" value="<jsp:getProperty name="student" property="chineseName"/>">
<br>
English First Name:&nbsp; 
<INPUT type="text" name="firstName" size="20" value="<jsp:getProperty name="student" property="firstName"/>">
<SPAN class="asterisk">*</SPAN>&nbsp;&nbsp;&nbsp; 
English Last Name:&nbsp;
<INPUT type="text" name="lastName" size="20" value="<jsp:getProperty name="student" property="lastName"/>">
<SPAN class="asterisk">*</SPAN><br>
生日&nbsp;Date of Birth:&nbsp;
Month
<INPUT type="text" name="birthMonth" size="5" value="<jsp:getProperty name="student" property="birthMonth"/>">
Day
<INPUT type="text" name="birthDay" size="5" value="<jsp:getProperty name="student" property="birthDay"/>">
Year
<INPUT type="text" name="birthYear" size="5" value="<jsp:getProperty name="student" property="birthYear"/>">
<br>
性別&nbsp;Sex:&nbsp;
<INPUT type="radio" name="gender" value="M" <%= isMale %>>男&nbsp;Male
<INPUT type="radio" name="gender" value="F" <%= isFemale %>>女&nbsp;Female
<br>
學校&nbsp;School:&nbsp;
<INPUT type="text" name="dayTimeSchool" size="30" value="<jsp:getProperty name="student" property="dayTimeSchool"/>">
<br>
健康問題&nbsp;Health Problem?&nbsp;
<INPUT type="text" name="healthProblem" size="70" value="<jsp:getProperty name="student" property="healthProblem"/>"><br>
<HR>
<INPUT type="submit"><INPUT type="reset">
<br><br>
The school students of any race to all the rights, privileges, programs, and 
activities generally accorded or made available to students at that school and 
that the school does not discriminate on the basis of race in administration of 
its educational policies, admission policies, scholarship and load programs, and 
athletic and other school-administered programs.
</FORM>
</BODY>
</HTML>
