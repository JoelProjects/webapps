<html>
<head>
<link rel="stylesheet" href="../tzuchi.css" type="text/css">
</head>
<title>Student Management</title>

<script language="javascript">
function addFamily()
{
  document.myForm.action="familyEditor.jsp";
  document.myForm.submit();  
}

function search()
{
  document.myForm.action="searchResults.jsp";
  document.myForm.submit();  
}
</script>

<%@ page contentType="text/html; charset=Big5" %>

<body>
<jsp:include page="adminHeader.jsp?activeTab=student" />

<form name="myForm" method="post">
<!-- <P>
School Year:&nbsp;
<SELECT name="schoolYear">
<OPTION selected>2003</OPTION>
</SELECT>
&nbsp;&nbsp;&nbsp;&nbsp;
School Semester:&nbsp;
<SELECT name="schoolSemster">
<OPTION>Fall</OPTION>
<OPTION>Spring</OPTION>
</SELECT>
</P> -->
Search<br>
<input type="text" name="searchStr" size="40"><br>
<input type="radio" name="searchBy" value="studentName" checked>Student Name&nbsp;
<input type="radio" name="searchBy" value="guardianName">Guardian Name<br>
<input type="button" value="Go" onClick="search()"><br>

<br><br>
<p>Add a new family and student information</p>
<input type="button" value="New Family Info" onClick="addFamily()">

</body>
</html>
