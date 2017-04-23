<html>
<head>
<link rel="stylesheet" href="../tzuchi.css" type="text/css">
</head>
<title>Reports</title>

<script language="javascript">

function goBack(id)
{
  document.myForm.action="studentList.jsp?familyId=" + id;
  document.myForm.submit();
}

function studentPdf(version)
{
  var year = document.myForm.schoolYear.value;
  var semester = document.myForm.schoolSemester.value;
  var courseId = document.myForm.courseId.options[document.myForm.courseId.selectedIndex].value;
    
  var query = "&schoolYear=" + year + "&schoolSemester=" + semester;
  query = query + "&courseId=" + courseId;
  
  if(version == "detailed")
    window.open("../../AdminPdfReportsServlet?type=student" + query, "student", "scrollbars=yes,resizable=yes,width=600,height=500");
  else
    window.open("../../AdminPdfReportsServlet?type=student&version=" + version + query, "student", "scrollbars=yes,resizable=yes,width=600,height=500");
}

function facultyPdf()
{
  var year = document.myForm.schoolYear.value;
  var semester = document.myForm.schoolSemester.value;
  var facultyType = document.myForm.facultyType.options[document.myForm.facultyType.selectedIndex].text;
    
  var query = "&schoolYear=" + year + "&schoolSemester=" + semester + "&facultyType=" + facultyType;
    
  window.open("../../AdminPdfReportsServlet?type=faculty" + query, "faculty", "scrollbars=yes,resizable=yes,width=600,height=500");
}

function getTuitionInfo()
{
  var year = document.myForm.YearSelect.options[document.myForm.YearSelect.selectedIndex].text;
  var semester = document.myForm.SemesterSelect.options[document.myForm.SemesterSelect.selectedIndex].text;
  var query = year + "&semester=" +semester;
  document.myForm.action="paymentSearch.jsp?year=" + query;
  document.myForm.submit();
}

</script>

<%@ page import="java.sql.*" %>
<%@ page import="datamodel.*" %>
<%@ page contentType="text/html; charset=Big5" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="status" scope="page" class="jsp.SchoolStatusBean" />
<jsp:useBean id="reg" scope="page" class="jsp.RegistrationManagementBean" />

<%
  Connection con = connection.getConnection();
  
  SchoolStatus schoolStatus = status.getStatus(con);
%>

<body>
<jsp:include page="adminHeader.jsp?activeTab=reports" />

<form name="myForm" method="post">

<h3>Student Information</h3>

<%
  if(schoolStatus == null)
  {
%>
  <font color="red">Current School Year and School Semester are not defined</font>
<%
  }
  else
  {
    int year = schoolStatus.getYear();
    String semester = schoolStatus.getSemester();    
%>
<input type="hidden" name="schoolYear" value="<%= year %>">
<input type="hidden" name="schoolSemester" value="<%= semester %>">

<P>
School Year:&nbsp;<%= year %>
&nbsp;&nbsp;
School Semester:&nbsp;<%= semester %>
</P>
Course: <select name="courseId">
<%= reg.getAllCoursesOptions(con, year, semester) %>
</select>
<p>
<input type="button" value="Student PDF (detailed version)" onClick="studentPdf('detailed')">
&nbsp;
<input type="button" value="Student PDF (simplified version)" onClick="studentPdf('simplified')">
&nbsp;
<input type="button" value="Student Attendance PDF" onClick="studentPdf('attendance')">
<%
  }
%>

<h3>Faculty Information</h3>
Type: <select name="facultyType">
<option></option>
<option selected>teacher</option>
<option>staff</option>
<option>volunteer</option>
</select>
&nbsp;<input type="button" value="Faculty PDF" onClick="facultyPdf()">
<p><br></p>
<!-- Student Payment Information -->
<h3>Student Payment Information</h3>
Search For Year:&nbsp;
<select name="YearSelect">
	<%=status.getSchoolYearOption(con)%>
</select>
Semester:&nbsp;
<select name="SemesterSelect">
	<%=status.getSchoolSemesterOption(con)%>
</select><br>
<input type="button" value="Search" onClick="getTuitionInfo()">

</form>

</body>
</html>
