<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<link rel="stylesheet" type="text/css" href="../tzuchi.css"/>
</head>
<title>Compose E-Mail</title>

<script language="javascript">

function validateInput() 
{
  /* check required fields */
  var mesg = "";
  if(document.myForm.selectedGroupIds.selectedIndex == -1)
    mesg = mesg + "At least one group needs to be selected.\n";
  if(document.myForm.subject.value == '')
    mesg =  mesg + "Subject is empty.\n";
  if(document.myForm.content.value == '')
    mesg =  mesg + "Content is empty.\n";

  if(mesg != '')
  {
    alert(mesg);
    return false;
  }
}
</script>

<%@ page import="java.sql.*" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="group" scope="page" class="jsp.GroupManagementBean" />

<%
  Connection con = connection.getConnection();
%>

<jsp:include page="addressTabs.jsp"/>

<body>

<form name="myForm" action="../../MailServlet?action=send" method="post" onSubmit="return validateInput()">

<table border="0">
<tr>
  <td>To</td>
  <td><select name="selectedGroupIds" multiple size="10"><%= group.getGroupOptions(con, null) %></select></td>
</tr>
<tr>
  <td>Subject</td>
  <td><input type="text" name="subject" size="70"></td>
</tr>
<tr>
  <td colspan="2"><textarea name="content" rows="12" cols="70"></textarea></td>
</tr>
<tr> 
  <td colspan="2" align="center">
    <input type="submit" value="Send">
  </td>
</tr>
</table>
</form>
</body>
</html>
