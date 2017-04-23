<html>
<head>
<link rel="stylesheet" href="../tzuchi.css" type="text/css">
</head>
<title>School Info Editor</title>

<SCRIPT LANGUAGE="javascript" SRC="../utility.js">
</SCRIPT>

<script language="javascript">

function goBack(id)
{
  document.myForm.action="courseManagement.jsp";
  document.myForm.submit();
}

function validateInput()
{
  /* check required fields */
  var mesg = "";
  if(trim(document.myForm.schoolYear.value) == '')
    mesg =  mesg + "School Calendar Year should not be empty.\n";
  if(document.myForm.schoolSemester.value == -1)
    mesg =  mesg + "School Semester should not be empty.\n";

  // number
  if(!isInteger(trim(document.myForm.schoolYear.value)))
    mesg =  mesg + "School Calendar Year should be an integer.\n";

  if(mesg != '')
  {
    alert(mesg);
    return false;
  }
  else
  {
    if(confirm("Are you sure to change semester?"))
      return true;
    else
      return false;
  }
}

</script>

<%@ page contentType="text/html; charset=Big5" %>

<body>
<jsp:include page="adminHeader.jsp?activeTab=school" />

<form name="myForm" method="post" action="../../AdminSchoolInfoManagementServlet" onSubmit="return validateInput()">

<h3>School Information Editor</h3>
<table>
  <tr>
    <td>School Calendar Year</td><td><input type="text" name="schoolYear" size="8"></td>
  </tr>
  <tr>
    <td>School Semester</td>
    <td>
      <select name="schoolSemester">
        <option value="-1"></option>
        <option>Fall</option>
        <option>Spring</option>
      </select>
    </td>
  </tr>
</table>
<br>
<input type="submit" value="Change">&nbsp;
<input type="button" value="Cancel" onClick="javascript:history.go(-1)">

</form>
</body>
</html>
