<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<link rel="stylesheet" href="../tzuchi.css" type="text/css">
</head>
<title>Person Management</title>

<script language="javascript">
function viewPerson(id)
{
  var query = "?selected=0&personId=" + id;
  window.open("profileViewer.jsp" + query, "profileViewer", "scrollbars=yes,resizable=yes,width=500,height=300");
}

function editPerson(id)
{
  if(id == 0)
    actionStr = "profileEditor.jsp?selected=0";  // create new item
  else
    actionStr = "profileEditor.jsp?selected=0&personId=" + id;  // edit item
  
  document.myForm.action=actionStr;
  document.myForm.submit();  
}

function deletePerson(id)
{
  if(confirm("This person will be removed from the addressbook completely, not just this group. If you only want to remove this person from current group, edit this person's group information."))
  {
    document.myForm.action="../../PersonManagementServlet?action=delete&personId=" + id;
    document.myForm.submit();
  }
  else
    return false;
}
</script>

<%@ page import="java.sql.*" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="person" scope="page" class="jsp.PersonManagementBean" />

<%
  Connection con = connection.getConnection();
  String groupIdStr = request.getParameter("groupId");
  String groupName = request.getParameter("groupName");
%>

<body>
<jsp:include page="addressTabs.jsp"/>

<form name="myForm" method="post">
<input type="hidden" name="groupId" value="<%= groupIdStr %>">
<input type="hidden" name="groupName" value="<%= groupName %>">

<h2>Group: <%= groupName %></h2>

<%
  if(request.isUserInRole("addressAdmin"))
  {
%>
<input type="button" value="Add Person Info" onClick="editPerson(0)">
<%
  }
%>

<p>
<table border=1 cellspacing=1 cellpadding=5>
<tr>
  <th>Chinese Name</th><th>Last Name</th><th>First Name</th><th>Home Phone</th><th>Cell Phone</th><th>E-Mail</th><th>Details</th><th></th>
</tr>
<%
  if(request.isUserInRole("addressAdmin"))
    out.print(person.getPersonList(con, Integer.parseInt(groupIdStr), true));
  else
    out.print(person.getPersonList(con, Integer.parseInt(groupIdStr), false));  
%>
</table>
<hr>
<br>
<center><h3><a href="groupManagement.jsp?type=address&selected=0">Back</a></h3></center>
</form>
</body>
</html>
