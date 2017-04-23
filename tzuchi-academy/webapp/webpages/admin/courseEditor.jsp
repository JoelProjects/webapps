<HTML>
<HEAD>
<TITLE>Course Editor</TITLE>
<LINK rel="stylesheet" href="../tzuchi.css" type="text/css">
</HEAD>

<SCRIPT LANGUAGE="javascript" SRC="../utility.js">
</SCRIPT>

<script language="javascript">

function goBack()
{
  document.myForm.action="courseManagement.jsp";
  document.myForm.submit();
}

function validateInput()
{
  /* check required fields */
  var mesg = "";

  if(document.myForm.classId.value == -1)
    mesg =  mesg + "Course Name is required\n";
  //if(document.myForm.primaryTeacherId.selectedIndex == -1)
  //  mesg =  mesg + "Teacher is required\n";

  if(mesg != '')
  {
    alert(mesg);
    return false;
  }
  else
  {
    if(confirm("Are you sure to update this?"))
      return true;
    else
      return false;
  }
}
</script>

<%@ page import="java.sql.*" %>
<%@ page contentType="text/html; charset=Big5" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="course" scope="page" class="jsp.CourseManagementBean" />

<%
  Connection con = connection.getConnection();

  String categoryIdStr = request.getParameter("categoryId");
  String courseIdStr = request.getParameter("courseId");
  String year = request.getParameter("schoolYear");
  String semester = request.getParameter("schoolSemester");

  if(courseIdStr != null)
  {
    course.getDataById(con, Integer.parseInt(courseIdStr));
  }
%>

<BODY>
<jsp:include page="adminHeader.jsp?activeTab=school"/>

<FORM name="myForm" method="post" action="../../AdminCourseManagementServlet?action=update" onSubmit="return validateInput()">

<%
  if(courseIdStr != null)
  {
%>
<input type="hidden" name="courseId" value="<%= courseIdStr %>">
<%
  }
%>

<input type="hidden" name="schoolYear" value="<%= year %>">
<input type="hidden" name="schoolSemester" value="<%= semester %>">

<a href="javascript:goBack()"><< Back</a>

<h3>Course Editor</h3>
<br>
<SPAN class="asterisk">*</SPAN>: Required Field
<HR>
<table>
  <tr>
    <td align="right"><SPAN class="asterisk">*</SPAN>Course Name</td>
    <td><select name="classId"><%= course.getCourse(con, Integer.parseInt(categoryIdStr)) %></select></td>
  </tr>
  <tr>
    <td align="right">Teacher 1</td>
    <td><select name="primaryTeacherId"><%= course.getPrimaryTeacher(con) %></select></td>
  </tr>
  <tr>
    <td align="right">Teacher 2</td>
    <td><select name="secondaryTeacherId"><%= course.getSecondaryTeacher(con) %></select></td>
  </tr>
  <tr>
    <td align="right">Class Room</td>
    <td><INPUT type="text" name="classRoom" size="10" value="<jsp:getProperty name="course" property="classRoom"/>"></td>
  </tr>
</table>

<HR>
<INPUT type="submit"><INPUT type="reset">

</FORM>
</BODY>
</HTML>
