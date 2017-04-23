<html>
<head>
<link rel="stylesheet" href="../tzuchi.css" type="text/css">
</head>
<title>Course Management</title>

<script language="javascript">

function viewStudents(courseId)
{
  var year = document.myForm.schoolYear.value;
  var semester = document.myForm.schoolSemester.value;

  var query = "searchBy=course&courseId=" + courseId + "&year=" + year + "&semester=" + semester;
  document.myForm.action="searchResults.jsp?" + query;
  document.myForm.submit();
}

function schoolInfo()
{
  document.myForm.action="schoolInfoEditor.jsp";
  document.myForm.submit();
}

function addCourse(categoryId)
{
  document.myForm.action="courseEditor.jsp?categoryId=" + categoryId;
  document.myForm.submit();
}

function editCourse(categoryId, courseId)
{
  document.myForm.action="courseEditor.jsp?categoryId=" + categoryId +"&courseId=" + courseId;
  document.myForm.submit();
}

function deleteCourse(courseId)
{
  if(confirm("Are you sure to delete it?"))
  {
    document.myForm.action="../../AdminCourseManagementServlet?action=delete&courseId=" + courseId;
    document.myForm.submit();
  }
  else
    return false;
}
</script>

<%@ page import="java.sql.*" %>
<%@ page import="datamodel.*" %>
<%@ page contentType="text/html; charset=Big5" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="status" scope="page" class="jsp.SchoolStatusBean" />
<jsp:useBean id="course" scope="page" class="jsp.CourseManagementBean" />
<%
  Connection con = connection.getConnection();

  SchoolStatus schoolStatus = status.getStatus(con);
  String error = request.getParameter("error");
%>

<body>
<jsp:include page="adminHeader.jsp?activeTab=school" />

<h3>Course Management</h3>

<form name="myForm" method="post">

<input type="button" value="Change Semester" onClick="schoolInfo()">

<%
  if(schoolStatus == null)
  {
%>
  <p><font color="red">Current School Year and School Semester are not defined</font>
<%
  }
  else
  {
    int year = schoolStatus.getYear();
    String semester = schoolStatus.getSemester();
%>
<input type="hidden" name="schoolYear" value="<%= year %>">
<input type="hidden" name="schoolSemester" value="<%= semester %>">

<P>
School Calendar Year:&nbsp;<%= year %>
&nbsp;&nbsp;
School Semester:&nbsp;<%= semester %>
</P>

<%
  if(error != null)
  {
%>
<font color="red"><%= error %></font>
<p>
<%
  }
%>

<p>
<table>
  <tr valign="top" align="left">
    <td>
      <h5>Chinese</h5>
      <input type="button" value="New Course" onClick="addCourse(<%= ClassManager.CHINESE%>)">
      <table border="1">
        <th>Course Name</th><th>Teacher 1</th><th>Teacher 2</th><th></th><th></th>
        <%= course.getChineseCourseList(con, year, semester) %>
      </table>
    </td>
    <td>&nbsp;</td>
    <td>
      <h5>Activity</h5>
      <input type="button" value="New Course" onClick="addCourse(<%= ClassManager.ACTIVITY %>)">
      <table border="1">
       <th>Course Name</th><th>Teacher 1</th><th>Teacher 2</th><th></th><th></th>
       <%= course.getActivityCourseList(con, year, semester) %>
      </table>
    </td>
  </tr>
</table>
<%
  }
%>

</form>
</body>
</html>
