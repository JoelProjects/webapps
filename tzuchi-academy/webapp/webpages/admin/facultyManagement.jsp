<html>
<head>
<link rel="stylesheet" href="../tzuchi.css" type="text/css">
</head>
<title>Faculty Management</title>

<script language="javascript">
function addFaculty()
{
  document.myForm.action="facultyEditor.jsp";
  document.myForm.submit();  
}

function editFaculty(facultyId)
{
  document.myForm.action="facultyEditor.jsp?facultyId=" + facultyId;
  document.myForm.submit();  
}

function assignTeacher(facultyId)
{
  alert("This function will implment in next version\n use course managerment instead");   
  //document.myForm.action="teacherReg.jsp?facultyId=" + facultyId;
  //document.myForm.submit();
}

function deleteFaculty(facultyId)
{
  if(confirm("Are you sure to delete it?"))
  {
    document.myForm.action="../../AdminFacultyManagementServlet?action=delete&facultyId=" + facultyId;
    document.myForm.submit();  
  }
  else
    return false;
}
</script>

<%@ page import="java.sql.*" %>
<%@ page contentType="text/html; charset=Big5" %>

<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="facultyMgrBean" scope="page" class="jsp.FacultyManagementBean" />
<%
  Connection con = connection.getConnection();
  
  String error = request.getParameter("error");
%>

<body>
<jsp:include page="adminHeader.jsp?activeTab=school" />
<h3>Faculty Management</h3>


<form name="myForm" method="post">
<%
  if(error != null)
  {
%>
<font color="red"><%= error %></font>
<p>
<%
  }
%>

<input type="button" value="New Faculty Info" onClick="addFaculty()">

<p>
<table>
  <tr valign="top" align="left">
    <td>
      <h5>Faculty List</h5>
      <table border="1">
        <th>Faculty Name</th><th>Chinese Name</th><th>Poistion</th>
	<th></th><th></th><th></th>		    
        <%= facultyMgrBean.getFacultyList(con) %>
      </table>
    </td>
    <td>&nbsp;</td>
  </tr>
</table>
		      

</form>
</body>
</html>
