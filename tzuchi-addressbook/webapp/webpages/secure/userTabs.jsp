<%
  String selectedTab = request.getParameter("selected");

  String[] tabStyle = {"", "", ""};
  if(selectedTab != null)
  {
    int selectedNum = Integer.parseInt(selectedTab);
    for(int i = 0; i < 1; i++)
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
    <td><a href="groupManagement.jsp?type=address&selected=0" <%= tabStyle[0] %>>Address Book</a></td>
    <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td><a href="../logout.jsp">[Log Out]</a></td>
  </tr>
</table>
<hr>
<br>
