<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<link rel="stylesheet" type="text/css" href="../tzuchi.css"/>
</head>
<title>Profile</title>

<%@ page import="java.sql.*" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="person" scope="page" class="jsp.PersonManagementBean" />

<%
  String personIdStr = request.getParameter("personId");
  Connection con = connection.getConnection();
  person.getDataById(con, Integer.parseInt(personIdStr));
%>

<body>
<center>
<table width="80%" border="0" aligh="center">
  <tr>
  <td colspan="2" align="center"><div id="subTitle" CLASS="subTitle">Person Profile</div></td>
  </tr>
  <tr>
  <td COLSPAN="2">
    &nbsp;
  </td>
  <tr>
    <td>Chinese Name:</td>
    <td><jsp:getProperty name="person" property="chineseName"/></td>
  </tr>
  </tr>
  <tr>
    <td>Last Name:</td>
    <td><jsp:getProperty name="person" property="lastName"/></td>
  </tr>
  <tr>
    <td>First Name:</td>
    <td><jsp:getProperty name="person" property="firstName"/></td>
  </tr>
  <tr>
    <td>Street:</td>
    <td><jsp:getProperty name="person" property="street"/></td>
  </tr>
  <tr>
    <td>City:</td>
    <td><jsp:getProperty name="person" property="city"/></td>
  </tr>
  <tr>
    <td>State:</td>
    <td><jsp:getProperty name="person" property="state"/></td>
  </tr>
  <tr>
    <td>Zip:</td>
    <td><jsp:getProperty name="person" property="zip"/></td>
  </tr>
  <tr>
    <td>E-Mail:</td>
    <td><jsp:getProperty name="person" property="email"/></td>
  </tr>
  <tr>
    <td>Home Phone:</td>
    <td><jsp:getProperty name="person" property="homePhone"/></td>
  </tr>
  <tr>
    <td>Cell Phone:</td>
    <td><jsp:getProperty name="person" property="cellPhone"/></td>
  </tr>
  <tr>
    <td>Work Phone:</td>
    <td><jsp:getProperty name="person" property="workPhone"/></td>
  </tr>  
</table>
<center>
</body>
</html>
