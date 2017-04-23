<html>
<head>
<link rel="stylesheet" href="../tzuchi.css" type="text/css">
</head>
<title>School Management</title>

<script language="javascript">
function addFaculty()
{
  document.myForm.action="raceEditor.jsp";
  document.myForm.submit();  
}
</script>

<%@ page contentType="text/html; charset=Big5" %>

<body>
<jsp:include page="adminHeader.jsp?activeTab=school" />

<form name="myForm" method="post">

<table>
  <tr>
    <td><a href="facultyManagement.jsp">Faculty Management</a></td>
    <td>Manage faculties at the school<td>
  </tr>
  <tr>
    <td><a href="courseManagement.jsp">Course Management</a></td>
    <td>Assign courses for current semester or define a new semester</td>
  </tr>
  <tr>
    <td><a href="classManagement.jsp">Class Management</a></td>
    <td>Define available classes for the school</td>
  </tr>
</table>
</form>
</body>
</html>
