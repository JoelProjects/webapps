<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/webpages/tzuchi.css"/>
</head>
<title>Sent Result</title>
<jsp:include page="addressTabs.jsp"/>

<%
  String error = (String)request.getAttribute("error");
  String recipient = (String)request.getAttribute("recipient");
%>
<body>

<%
  if(error != null && error.trim().length() != 0)
  {
%>
<font color="red">Error!</font><p>
<pre>  
<%
    out.println(error);
  }
%>
</pre>
<%
  if(recipient != null && recipient.trim().length() != 0)
  {
%>
<font color="green">Your message has been sent successfully to:</font><p>
<pre>
<%
    out.println(recipient);
  }
%>
</pre>
</body>
</html>
