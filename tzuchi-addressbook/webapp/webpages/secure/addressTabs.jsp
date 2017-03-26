<%
  if(request.isUserInRole("addressAdmin"))
  {
%>
  <jsp:include page="adminTabs.jsp"/>
<%
  }
  else
  {
%>
  <jsp:include page="userTabs.jsp"/>
<%
  }
%>
