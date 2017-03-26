<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<link rel="stylesheet" type="text/css" href="../tzuchi.css"/>
</head>
<title>Profile</title>

<script language="javascript">
function goBack()
{
  actionStr = "personManagement.jsp?selected=0";
  
  document.myForm.action=actionStr;
  document.myForm.submit();  
}

function validateInput() 
{
  /* check required fields */
  var mesg = "";
  if(document.myForm.selectedGroupIds.selectedIndex == -1)
    mesg = mesg + "At least one group needs to be assigned.\n";
  if(document.myForm.chineseName.value == '' &&
     document.myForm.lastName.value == '' &&
     document.myForm.firstName.value == '') 
    mesg =  mesg + "At least one name field should not be empty.\n";

  if(mesg != '')
  {
    alert(mesg);
    return false;
  }
}
</script>

<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="datamodel.*" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="person" scope="page" class="jsp.PersonManagementBean" />
<jsp:useBean id="group" scope="page" class="jsp.GroupManagementBean" />

<%
  Connection con = connection.getConnection();

  String personIdStr = request.getParameter("personId");
  String groupIdStr = request.getParameter("groupId");
  String groupName = request.getParameter("groupName");
  
  String isGroupLeader = "";
  int[] groupIds = null;
  if(personIdStr != null)
  {
    int personId = Integer.parseInt(personIdStr);
    person.getDataById(con, personId);
    
    PersonGroupManager groupMgr = new PersonGroupManager();
    
    // groups
    Vector groups = groupMgr.getDataByPersonId(con, personId);
    if(groups.size() > 0)
    {
      groupIds = new int[groups.size()];
      for(int i = 0; i < groups.size(); i++)
      {
        PersonGroup pgroup = (PersonGroup)groups.get(i);
        groupIds[i] = pgroup.getGroupId();
      }
    }

    // is group leader?
    if(groupMgr.isGroupLeader(con, personId, Integer.parseInt(groupIdStr)))
      isGroupLeader = "checked";
  }
  else
  {
    // current group as default
    groupIds = new int[1];
    groupIds[0] = Integer.parseInt(groupIdStr);
  }
%>

<body>
<jsp:include page="addressTabs.jsp" />

<center>
<form name="myForm" action="../../PersonManagementServlet?action=update" method="post" onSubmit="return validateInput()">
<%
  if(personIdStr != null)
  {
%>
<input type="hidden" name="personId" value="<%= personIdStr %>">
<%
  }
%>
<input type="hidden" name="groupId" value="<%= groupIdStr %>">
<input type="hidden" name="groupName" value="<%= groupName %>">

  <table width="90%" border="0" aligh="center">
    <tr> 
      <td>&nbsp;</td>
      <td><div id="subTitle" CLASS="subTitle">Person Information</div></td>
    </tr>
    <tr> 
      <td COLSPAN="2">&nbsp;</td>
    </tr>
    <tr>
      <td>Group(s):</td>
      <td><select name="selectedGroupIds" multiple size="10"><%= group.getGroupOptions(con, groupIds) %></select></td>
    </tr>
    <tr>
      <td>Group Leader:</td>
      <td><input type="checkbox" name="type" <%= isGroupLeader %>></td>
    </tr>
    <tr>
      <td>Chinese Name:</td>
      <td><input type="text" name="chineseName" size="30" value="<jsp:getProperty name="person" property="chineseName"/>"></td>
    </tr>
    <tr>
      <td>Last Name:</td>
      <td><input type="text" name="lastName" size="30" value="<jsp:getProperty name="person" property="lastName"/>"></td>
    </tr>
    <tr>
      <td>First Name:</td>
      <td><input type="text" name="firstName" size="30" value="<jsp:getProperty name="person" property="firstName"/>"></td>
    </tr>
    <tr>
      <td>Street:</td>
      <td><input type="text" name="street" size="30" value="<jsp:getProperty name="person" property="street"/>"></td>
    </tr>
    <tr> 
      <td>City:</td>
      <td><input type="text" name="city" size="30" value="<jsp:getProperty name="person" property="city"/>"></td>
    </tr>
    <tr> 
      <td>State:</td>
      <td><input type="text" name="state" size="2" maxLength="2" value="<jsp:getProperty name="person" property="state"/>"></td>
    </tr>
    <tr> 
      <td>Zip:</td>
      <td><input type="text" name="zip" size="5" maxLength="5" value="<jsp:getProperty name="person" property="zip"/>"></td>
    </tr>
    <tr> 
      <td>E-Mail:</td>
      <td><input type="text" name="email" size="30" value="<jsp:getProperty name="person" property="email"/>"></td>
    </tr>
    <tr> 
      <td>Home Phone:</td>
      <td><input type="text" name="homePhone" size="15" value="<jsp:getProperty name="person" property="homePhone"/>"> 
      </td>
    </tr>
    <tr> 
      <td>Cell Phone:</td>
      <td><input type="text" name="cellPhone" size="15" value="<jsp:getProperty name="person" property="cellPhone"/>"> 
      </td>
    </tr>
    <tr> 
      <td>Work Phone:</td>
      <td><input type="text" name="workPhone" size="15" value="<jsp:getProperty name="person" property="workPhone"/>"> 
      </td>
    </tr>
    <tr> 
      <td COLSPAN="2">&nbsp;</td>
    </tr>
    <tr> 
      <td COLSPAN="2" align="center">
        <input type="submit" value="Submit">
        <input type="button" value="Back" onClick="goBack()">
      </td>
    </tr>
  </table>
</form>
<center>
</body>
</html>
