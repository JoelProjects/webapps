<%
  String activeTab = request.getParameter("activeTab");
  if(activeTab == null)
    activeTab = "none";  // don't change it, user current one
%>

<div align="center">
  <img src="../images/tzuchi-academy.jpg" width="140" alt="Tzu-Chi Academy">
</div>

<br>
<b>User: <%= request.getRemoteUser() %>&nbsp;&nbsp;&nbsp;<b>

<a href="studentManagement.jsp">
<%
  if(activeTab.equals("student"))
    out.println("<font color='blue'>");
%>
Student Management
<%
  if(activeTab.equals("student"))  
    out.println("</font>");
%>
</a>&nbsp;|&nbsp; 
<a href="reports.jsp">
<%
  if(activeTab.equals("reports"))
    out.println("<font color='blue'>");
%>
Reports
<%
  if(activeTab.equals("reports"))
    out.println("</font>");
%>
</a>&nbsp;|&nbsp; 
<a href="schoolManagement.jsp">
<%
  if(activeTab.equals("school"))
    out.println("<font color='blue'>");
%>
School Management
<%
  if(activeTab.equals("school"))
    out.println("</font>");
%>
</a>&nbsp;|&nbsp; 
<a href="../logout.jsp">Log Out</a>
<br>
<hr size="2" color="green">
<br>
