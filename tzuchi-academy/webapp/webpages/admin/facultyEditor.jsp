<Html>
<head>
<link rel="stylesheet" href="../tzuchi.css" type="text/css">
</head>
<title>Faculty Management</title>

<SCRIPT LANGUAGE="javascript" SRC="../utility.js">
</SCRIPT>

<script language="javascript">

function goBack()
{
  document.myForm.action="facultyManagement.jsp";
  document.myForm.submit();
}

function validateInput() 
{
  /* check required fields */
  var mesg = "";
  if(trim(document.myForm.chineseName.value) == '' &&
      (trim(document.myForm.firstName.value) == ''||
      trim(document.myForm.lastName.value) == ''))
    mesg =  mesg + "Either Chinsee Name or English Name is required\n";

  if(trim(document.myForm.phone.value) == '' &&
      trim(document.myForm.mobilePhone.value) == '')
      mesg =  mesg + "Either telephone or mobile phone is required\n";

  if(document.myForm.type.value == -1)
    mesg =  mesg + "Type of Faculty is required\n";

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
<jsp:useBean id="faculty" scope="page" class="jsp.FacultyManagementBean" />
<!-- construct and initialized faculty Manager Bean by ID -->
<%
  Connection conn= connection.getConnection();

   String facultyIdStr = request.getParameter("facultyId"); 

  if (facultyIdStr !=null)
  {
	faculty.getDataById(conn, Long.parseLong(facultyIdStr));
  }

%>

<body>

<jsp:include page="adminHeader.jsp?activeTab=school"/>

<form name="myForm" method="post" action="../../AdminFacultyManagementServlet?action=update" onSubmit="return validateInput()">

<%
  if (facultyIdStr != null)
  {
%>
<input type="hidden" name="facultyId" value="<%=facultyIdStr %>">
<%
  }
%>

<a href="javascript:goBack()"><< Back</a>

<h3>Faculty Information</h3>
<br>
<SPAN class="asterisk">*</SPAN>:<b> Required Field</b>
<hr>
<br>
First Name:&nbsp;
<input Type="text" name="firstName" size="15" value="<jsp:getProperty name="faculty" property="firstName" />">
<SPAN class="asterisk">*</SPAN>&nbsp;&nbsp;&nbsp;
Last Name:&nbsp;
<input Type="text" name="lastName" size="15" value="<jsp:getProperty name="faculty" property="lastName" />">
<SPAN class="asterisk">*</SPAN>&nbsp;&nbsp;&nbsp;
<br><br>
Chinese Name:&nbsp;
<input Type="text" name="chineseName" size="10" value="<jsp:getProperty name="faculty" property="chineseName" />">
<br><br>
Street:&nbsp;
<input Type="text" name="street" size="30" value="<jsp:getProperty name="faculty" property="street" />">
city:&nbsp;
<input Type="text" name="city" size="15" value="<jsp:getProperty name="faculty" property="city" />">
<br><br>
State:&nbsp;
<SELECT size="1" name="state">
<jsp:getProperty name="faculty" property="state"/>
Zip Code:&nbsp;
<INPUT type="text" name="zip" size="10" value="<jsp:getProperty name="faculty" property="zip"/>">
<br><br>
Telephone:&nbsp;
<INPUT type="text" name="areaCode" size="3" value="<jsp:getProperty name="faculty" property="areaCode"/>">
-<INPUT type="text" name="phone" size="8" value="<jsp:getProperty name="faculty" property="phone"/>">
<SPAN class="asterisk">*</SPAN>&nbsp;(i.e.781-929-3919)
<br><br>
Mobil Phone:
<INPUT type="text" name="mobilePhone" size="12" value="<jsp:getProperty name="faculty" property="mobilePhone"/>">&nbsp;(i.e. 781-929-3919)
<br><br>
Email:&nbsp
<INPUT type="text" name="email" size="40" value="<jsp:getProperty name="faculty" property="email"/>">
<br><br>
Type of Faculty:&nbsp;
<SELECT size="1" name="type">
<jsp:getProperty name="faculty" property="type"/>
</SELECT><SPAN class="asterisk">*</SPAN>
<hr>
<input type="submit"><input type="reset">
</form>
</body>
</html>
