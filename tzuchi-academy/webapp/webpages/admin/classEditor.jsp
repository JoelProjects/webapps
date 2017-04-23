<HTML>
<HEAD>
<TITLE>Class Editor</TITLE>
<LINK rel="stylesheet" href="../tzuchi.css" type="text/css">
</HEAD>
<SCRIPT LANGUAGE="javascript" SRC="../utility.js">
</SCRIPT>
<script language="javascript">
function goBack()
{
  document.myForm.action="classManagement.jsp";
  document.myForm.submit();
}
function validateInput() 
{
  /* check required fields */
  var mesg = "";
  if(trim(document.myForm.name.value) == '')
    mesg =  mesg + "Name is required\n";
  if(!isInteger(document.myForm.maxStudents.value))
    mesg =  mesg + "Max Number of Students should be an integer.\n";
  if(mesg != '')
  {
    alert(mesg);
    return false;
  }
  else
  {
    if(confirm("Are you sure to update this form?"))
      return true;
    else
      return false;
  }
}
</script>
<%@ page import="java.sql.*" %>
<%@ page contentType="text/html; charset=Big5" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="cl" scope="page" class="jsp.ClassManagementBean" />
<%
  Connection con = connection.getConnection();
  String classIdStr = request.getParameter("classId");  
  if(classIdStr != null)
  {
    cl.getDataById(con, Integer.parseInt(classIdStr));
  }
%>
<BODY>
<jsp:include page="adminHeader.jsp?activeTab=school"/>
<FORM name="myForm" method="post" action="../../AdminClassManagementServlet?action=update" onSubmit="return validateInput()">
<%
  if(classIdStr != null)
  {
%>
<input type="hidden" name="classId" value="<%= classIdStr %>">
<%
  }
%>
<a href="javascript:goBack()"><< Back</a>
<h3>Class Editor</h3>
<br>
<SPAN class="asterisk">*</SPAN>: Required Field
<HR>
<table>
  <tr>
    <td align="right"><SPAN class="asterisk">*</SPAN>Class Name</td>
    <td><INPUT type="text" name="name" size="25" value="<jsp:getProperty name="cl" property="name"/>"></td> 
  </tr>
  <tr>
    <td align="right">Category</td>
    <td><select name="category"><%= cl.getCategory() %></select></td>
  </tr>
  <tr>
    <td align="right">Description</td>
    <td><input type="text" name="description" size="70" value="<jsp:getProperty name="cl" property="description"/>"></td>
  </tr>
  <tr>
    <td align="right">Max Number of Students</td>
    <td><INPUT type="text" name="maxStudents" size="5" value="<jsp:getProperty name="cl" property="maxStudents"/>"></td>
  </tr>
</table>
<HR>
<INPUT type="submit"><INPUT type="reset">
</FORM>
</BODY>
</HTML>
