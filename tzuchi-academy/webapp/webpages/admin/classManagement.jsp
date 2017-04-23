<html>
<head>
<link rel="stylesheet" href="../tzuchi.css" type="text/css">
</head>
<title>Class Management</title>

<script language="javascript">
function addClass()
{
  document.myForm.action="classEditor.jsp";
  document.myForm.submit();  
}

function editClass(classId)
{
  document.myForm.action="classEditor.jsp?classId=" + classId;
  document.myForm.submit();  
}

function deleteClass(classId)
{
  if(confirm("Are you sure to delete it?"))
  {
    document.myForm.action="../../AdminClassManagementServlet?action=delete&classId=" + classId;
    document.myForm.submit();  
  }
  else
    return false;
}
</script>

<%@ page import="java.sql.*" %>
<%@ page contentType="text/html; charset=Big5" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="cl" scope="page" class="jsp.ClassManagementBean" />
<%
  Connection con = connection.getConnection();
  
  String error = request.getParameter("error");
%>

<body>
<jsp:include page="adminHeader.jsp?activeTab=school" />

<h3>Class Management</h3>

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

<input type="button" value="New Class" onClick="addClass()">
<p>
<table>
  <tr valign="top" align="left">
    <td>
      <h5>Chinese</h5>
      <table border="1">
        <th>Class Name</th><th></th><th></th>
        <%= cl.getChineseClassList(con) %>
      </table>
    </td>
    <td>&nbsp;</td>
    <td>
      <h5>Activity</h5>
      <table border="1">
       <th>Class Name</th><th></th><th></th>
       <%= cl.getActivityClassList(con) %>
      </table>
    </td>
  </tr>
</table>
</form>
</body>
</html>
