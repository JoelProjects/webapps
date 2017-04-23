<HTML>
<HEAD>
<TITLE>Student History Viewer</TITLE>
<LINK rel="stylesheet" href="../tzuchi.css" type="text/css">
</HEAD>
<%@ page import="java.sql.*" %>
<%@ page contentType="text/html; charset=Big5" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="student" scope="page" class="jsp.StudentManagementBean" />
<%
  Connection con = connection.getConnection();
  String studentIdStr = request.getParameter("studentId");  
%>
<BODY>
<h3>History of Registered Courses</h3>
<p>
<table border="1">
<tr>
  <th>School Year</th><th>Courses</th>
</tr>
<tr>
<%= student.getHistory(con, Long.parseLong(studentIdStr)) %>
</tr>
</table>
</BODY>
</HTML>
