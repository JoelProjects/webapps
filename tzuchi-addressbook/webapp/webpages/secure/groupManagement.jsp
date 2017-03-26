<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<link rel="stylesheet" type="text/css" href="../tzuchi.css"/>
</head>
<title>Group Management</title>

<%@ page import="java.sql.*" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="group" scope="page" class="jsp.GroupManagementBean" />

<%
  Connection con = connection.getConnection();
  String type = request.getParameter("type");  // either address or group
  String error = request.getParameter("error");
%>

<script language="javascript">
var isIE=document.all?true:false; /* IE4, IE5, IE6 */
var isDOM=document.getElementById?true:false; /* IE6, NS6 */

var maxLevel = 4;
var groupList = [{}, {}, {}, {}];  // array of hashtables
var parentIds = [0, 0, 0, 0];

// groups inside each level
<%= group.getGroupObjects(con) %>
  
function addGroup(group)
{
  var groupObjStr = "document.myForm." + group;
  var name = prompt("Group Name", "");
  
  if(name != undefined && name != "")
  {
    // use form field to avoid problem in chinese encoding
    document.myForm.groupName.value = name;

    var levelId = group.substring("group".length, group.length);
    
    document.myForm.action="../../GroupManagementServlet?action=add&levelId=" + levelId + "&parentId=" + parentIds[levelId];
    document.myForm.submit();    
  }
}

function deleteGroup(group)
{
  var groupObjStr = "document.myForm." + group;
  var len = eval(groupObjStr + ".length");
  var index = eval(groupObjStr + ".selectedIndex");
  
  if(index > -1)
  {
    // check if there is any sub-group exists
    var error = false; 
    var level = group.substring("group".length, group.length);
    level++;  // child level

    var groupId = eval(groupObjStr + ".options[index].value");
    
    if(level < maxLevel)
    {
      var c = groupList[level];
      for(var id in c)
      {
        if(c[id].parentId == groupId)
        {
          error = true;
          break;
        }
      }    
      
      if(error)
      {
        alert("Sub-groups need to be removed first.");
        return false;
      }
    }

    if(confirm("Are you sure?"))
    {
      document.myForm.action="../../GroupManagementServlet?action=delete&groupId=" + groupId;
      document.myForm.submit();
    }
  }
}

function renameGroup(group)
{
  var groupObjStr = "document.myForm." + group;
  var index = eval(groupObjStr + ".selectedIndex");
  
  if(index > -1)
  {
    var name = prompt("Group Name", eval(groupObjStr + ".options[index].text"));
    
    if(name != undefined && name != "")
    {
      // use form field to avoid problem in chinese encoding
      document.myForm.groupName.value = name;
      
      var groupId = eval(groupObjStr + ".options[index].value");
    
      document.myForm.action="../../GroupManagementServlet?action=rename&groupId=" + groupId;
      document.myForm.submit();     
    }
  }
}

function editGroup(group)
{
  var groupObjStr = "document.myForm." + group;
  var index = eval(groupObjStr + ".selectedIndex");
  
  if(index > -1)
  {  
    var groupId = eval(groupObjStr + ".options[index].value"); 
    var groupName = eval(groupObjStr + ".options[index].text");

    // use form field to avoid problem in chinese encoding
    document.myForm.groupName.value = groupName;

    document.myForm.action="personManagement.jsp?selected=0&groupId=" + groupId;
    document.myForm.submit(); 
  }
}

function setVisible(level)
{
  if(level >= maxLevel)
    return true;
  
  if(isDOM)
    document.getElementById(level).style.visibility="visible";    
  else
    eval("document.all." + level + ".style.visibility='visible'");

  var groupObjStr = "document.myForm.group" + (level-1);

  // get ID of selected group
  var index = eval(groupObjStr + ".selectedIndex");
  var selectedId = eval(groupObjStr + ".options[index].value");
  
  // set parent ID
  parentIds[level] = selectedId;

  // remove old items in the sub-group
  groupObjStr = "document.myForm.group" + level;    
  var len = eval(groupObjStr + ".length");
  for(i = 0; i < len; i++)
  {
    // numbering of the options will be changed after it's removed
    // if option 0 is removed, original option 1 will become option 0
    eval(groupObjStr + ".options[0] = null");
  }

  // get sub-groups for selected group  
  index = 0;
  var c = groupList[level];
  for(var id in c)
  {
    if(c[id].parentId == selectedId)
    {
      eval(groupObjStr + ".options[index] = new Option(c[id].name, id)");
      index++;
    }
  }
  
  // hide levels below child level
  for(i = level + 1; i < maxLevel; i++)
  {
    if(isDOM)
      document.getElementById(i).style.visibility="hidden";    
    else
      eval("document.all." + i + ".style.visibility='hidden'");  
  }
}

function getGroupList(levelId)
{
  var c = groupList[levelId];
  for(var id in c)
    document.write("<option value=" + id + ">" + c[id].name + "</option>");
}

function Group(id, name)
{
  this.parentId = id;
  this.name = name;
}

</script>

<body>
<jsp:include page="addressTabs.jsp"/>

<form name="myForm" method="post">
<input type="hidden" name="groupName">
<%
  if(error != null)
  {
%>
<font color="red"><%= error %></font>
<p>
<%
  }
%>
<%
  if(type.equals("address"))
  {
    // for editing address book
    out.print(util.GroupLevelHtml.getHtml(4, true));
  }
  else
  {
    // for defining groups
    out.print(util.GroupLevelHtml.getHtml(4));
  }
%>  
</form>
</body>
</html>
