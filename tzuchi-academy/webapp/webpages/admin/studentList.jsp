<HTML>
<HEAD>
<TITLE>Student List Page</TITLE>
<LINK rel="stylesheet" href="../tzuchi.css" type="text/css">
</HEAD>

<SCRIPT LANGUAGE="javascript" SRC="utility.js">
</SCRIPT>
<script language="javascript">

function goBack()
{
  document.myForm.action="studentManagement.jsp";
  document.myForm.submit();
}

function editFamily(id)
{
  document.myForm.action="familyEditor.jsp?familyId=" + id;
  document.myForm.submit();
}

function addStudent()
{
  document.myForm.action="studentEditor.jsp";
  document.myForm.submit();
}

function editStudent(id)
{
  document.myForm.action="studentEditor.jsp?studentId=" + id;
  document.myForm.submit();
}

function registerStudent(id)
{
  document.myForm.action="classRegistration.jsp?studentId=" + id;
  document.myForm.submit();
}

function deleteStudent(id)
{
  if(confirm("Are you sure to delete it?"))
  {
    document.myForm.action="../../AdminStudentManagementServlet?action=delete&studentId=" + id;
    document.myForm.submit();
  }
  else
    return false;
}

function viewHistory(studentId)
{
  var query = "?studentId=" + studentId;
  window.open("studentHistoryViewer.jsp" + query, "history", "scrollbars=yes,resizable=yes,width=500,height=500");
}

</script>

<%@ page import="java.sql.*" %>
<%@ page contentType="text/html; charset=Big5" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="student" scope="page" class="jsp.StudentManagementBean" />
<%
  Connection con = connection.getConnection();

  String error = request.getParameter("error");
  String familyIdStr = request.getParameter("familyId");
  long familyId = Long.parseLong(familyIdStr);
%>

<BODY>
<jsp:include page="adminHeader.jsp?activeTab=student" />

<FORM name="myForm" method="post">

<input type="hidden" name="familyId" value="<%= familyIdStr %>">
<a href="javascript:goBack()"><< Back</a>

<p>Edit/View family information</p>
<input type="button" value="Family Info" onClick="editFamily('<%= familyIdStr %>')">
<!-- dummy infomation -->
<input type="hidden" name="reqYear" value="anyYear">
<input type="hidden" name="reqSemester" value="anySemester">

<p>Add a new student information</p>
<input type="button" value="New Student" onClick="addStudent()">
<p>
<%
  if(error != null)
  {
%>
<font color="red"><%= error %></font>
<p>
<%
  }
%>
<table border="1">
<tr>
  <th>English Name</th><th>Chinese Name</th><th>Current Registered Courses</th><th>&nbsp;</th><th>&nbsp;</th><th>&nbsp;</th>
</tr>
<%= student.getStudentList(con, familyId) %>
</table>

</FORM>
</BODY>
</HTML>
