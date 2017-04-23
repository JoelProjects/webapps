<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
<title>Payment Search</title>
<LINK rel="stylesheet" href="../tzuchi.css" type="text/css">
</head>
<SCRIPT LANGUAGE="javascript" SRC="utility.js">
</SCRIPT>
<script language="javascript">
function updateInfo(id)
{
  document.myForm.action="classRegistration.jsp?studentId=" + id;
  document.myForm.submit();	
}
function getTuitionInfo()
{
  var year = document.myForm.YearSelect.options[document.myForm.YearSelect.selectedIndex].text;
  var semester = document.myForm.SemesterSelect.options[document.myForm.SemesterSelect.selectedIndex].text;
  var query = year + "&semester=" +semester;
  document.myForm.action="paymentSearch.jsp?year=" + query;
  document.myForm.submit();
}

function goBack()
{
  document.myForm.action="reports.jsp";
  document.myForm.submit();
}

function generateFile()
{
	var year = document.myForm.reqYear.value;
  	var semester = document.myForm.reqSemester.value;
	var query = year+"&semester=" + semester;
	window.open("../../CSVReportServlet?type=tuition&year=" + query, "Tuition", "scrollbars=yes,resizable=yes,width=600,height=500");
}
</script>
<%@ page contentType="text/html; charset=Big5" %>
<%@ page import="java.sql.*" %>
<%@ page import="util.*" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="studentPay" scope="page" class="jsp.StudentPaymentBean" />
<jsp:useBean id="status" scope="page" class="jsp.SchoolStatusBean" />
<body>
<jsp:include page="adminHeader.jsp?activeTab=reports" />
<%
	Connection con = connection.getConnection();
	String reqYear = request.getParameter("year");
	String reqSemester = request.getParameter("semester");
%>

<form name="myForm" method="post">
<input type="hidden" name="familyId" value="396834">
<input type="hidden" name="reqYear" value="<%=reqYear%>">
<input type="hidden" name="reqSemester" value="<%=reqSemester%>">
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
<hr>
<%
	if(!Utility.isEmpty(reqYear) && !Utility.isEmpty(reqSemester))
	{
		int year = Integer.parseInt(reqYear);
%>

<%=studentPay.getTuitionByTerm(con, year, reqSemester)%>

<%
}
%>

</body>
</html>