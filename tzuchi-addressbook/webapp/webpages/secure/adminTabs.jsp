<%
  String ctx = request.getContextPath();
  String selectedTab = request.getParameter("selected");

  String[] tabStyle = {"", "", "", ""};
  if(selectedTab != null)
  {
    int selectedNum = Integer.parseInt(selectedTab);
    for(int i = 0; i < 3; i++)
    {
      if(i == selectedNum)
      {
        tabStyle[i] = "style=color:blue";
        break;
      }
    }
  }
%>
<table>
  <tr>
    <td>User: <%= request.getRemoteUser() %> | </td>
    <td><a href="<%= ctx %>/webpages/secure/groupManagement.jsp?type=address&selected=0" <%= tabStyle[0] %>>Address Book</a> | </td>
    <td><a href="<%= ctx %>/webpages/secure/groupManagement.jsp?type=group&selected=1" <%= tabStyle[1] %>>Group Management</a> | </td>
    <td><a href="<%= ctx %>/webpages/secure/tool.jsp?type=tool&selected=2" <%= tabStyle[2] %>>Tool</a></td>
    <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td><a href="<%= ctx %>/webpages/logout.jsp">[Log Out]</a></td>
  </tr>
</table>
<hr>
<br>
