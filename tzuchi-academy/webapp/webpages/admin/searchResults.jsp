<HTML>
<HEAD>
<TITLE>Search Results</TITLE>
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

function manageFamily(id)
{
  document.myForm.action="studentList.jsp?familyId=" + id;
  document.myForm.submit();
}

function editStudent(id)
{
  document.myForm.action="studentEditor.jsp?studentId=" + id;
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

function deleteFamily(id)
{
  if(confirm("Are you sure to delete it?"))
  {
    document.myForm.action="../../AdminFamilyManagementServlet?action=delete&familyId=" + id;
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
<jsp:useBean id="search" scope="page" class="jsp.SearchResultsBean" />
<%
  Connection con = connection.getConnection();

  String error = request.getParameter("error");
  String searchBy = request.getParameter("searchBy");
  String searchStr = request.getParameter("searchStr");
%>

<BODY>
<jsp:include page="adminHeader.jsp?activeTab=student" />

<FORM name="myForm" method="post">
<input type="hidden" name="searchBy" value="<%= searchBy %>">
<input type="hidden" name="searchStr" value="<%= searchStr %>">

<a href="javascript:goBack()"><< Back</a>
<h3>Search Results</h3>

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
<%
  if(searchBy.equals("studentName") || searchBy.equals("course"))
  {
%>
<tr>
  <th>Student English Name</th><th>Student Chinese Name</th><th>Registered Courses</th><th>&nbsp;</th>
</tr>
    <%
      if(searchBy.equals("studentName"))
        out.print(search.getStudentList(con, searchBy, searchStr));
      else
      {
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        int year = Integer.parseInt(request.getParameter("year"));
        String semester = request.getParameter("semester");
        out.print(search.getStudentList(con, courseId, year, semester));
      }
    %>
<%
  }
  else
  {
%>
<tr>
  <th>Guardian English Name</th><th>Guardian Chinese Name</th><th>Relationship</th><th>&nbsp;</th><th>&nbsp;</th>
</tr>
<%= search.getFamilyList(con, searchBy, searchStr) %>
<%
  }
%>
</table>

</FORM>
</BODY>
</HTML>
