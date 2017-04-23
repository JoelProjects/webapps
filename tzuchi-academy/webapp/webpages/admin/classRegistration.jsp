<html>
<head>
<link rel="stylesheet" href="../tzuchi.css" type="text/css">
</head>
<title>Class Registration</title>

<SCRIPT LANGUAGE="javascript" SRC="../utility.js">
</SCRIPT>

<script language="javascript">

var oldChineseId = -1;

function goBack(id, reqYear, reqSemester)
{
   //396834 is the number tell us to go back to paymentSearch
  if(id == "396834")
  {
    var query = reqYear + "&semester="+reqSemester;
    document.myForm.action="paymentSearch.jsp?year=" + query;
   document.myForm.submit();
  }else{
   document.myForm.action="studentList.jsp?familyId=" + id;
   document.myForm.submit();
   }
}

function generateReceipt()
{
  /* check required fields */
  var mesg = "";
  if(trim(document.myForm.amount.value) == '')
    mesg =  mesg + "Amount should not be empty.\n";
  if(document.myForm.term.value == -1)
    mesg =  mesg + "School Term should not be empty.\n";
  if(document.myForm.payment.value == -1)
    mesg =  mesg + "Payment Type should not be empty.\n";

  // number
  if(!isNumber(trim(document.myForm.amount.value)))
    mesg =  mesg + "Amount should be a number.\n";

  if(mesg != '')
  {
    alert(mesg);
    return false;
  }
  else
  {
    var year = document.myForm.schoolYear.value;
    var semester = document.myForm.schoolSemester.value;
    var amount = document.myForm.amount.value;
    var term = document.myForm.term.options[document.myForm.term.selectedIndex].text;
    var payment = document.myForm.payment.options[document.myForm.payment.selectedIndex].text;
    var checkNo = document.myForm.checkNo.value;
    var studentId = document.myForm.studentId.value;

    var query = "&schoolYear=" + year + "&schoolSemester=" + semester;
    query = query + "&amount=" + amount + "&term=" + term + "&payment=" + payment;
    query = query + "&studentId=" + studentId + "&checkNo=" + checkNo;

    window.open("../../AdminPdfReportsServlet?type=bill" + query, "receipt", "scrollbars=yes,resizable=yes,width=600,height=500");
  }
}

function validateInput()
{
  /* check required fields */
  var mesg = "";

  if(document.myForm.chineseId.value == -1)
  {
     var oldActivityId = document.myForm.activityId.value;
     document.myForm.activityId.value = -1;

     if(confirm("Chinese class is not selected. Are you going to drop this student from the class?\n" +
     "If this is for class registration, Chinese class should be selected."))
        return true;
     else
     {
        document.myForm.chineseId.value = oldChineseId;
        document.myForm.activityId.value = oldActivityId;
        return false;
     }
  }

  // number
  if(!isNumber(trim(document.myForm.amount.value)))
    mesg =  mesg + "Tuition Amount should be a number.\n";
  else if(document.myForm.amount.value > 0 && document.myForm.payment.value == -1)
    mesg =  mesg + "Tuition Payment Type should not be empty.\n";

  if(!isNumber(trim(document.myForm.uniformAmount.value)))
    mesg =  mesg + "Uniform Amount should be a number.\n";
  else if(trim(document.myForm.uniformAmount.value) != '' && document.myForm.uniformPayment.value == -1)
    mesg =  mesg + "Uniform Payment Type should not be empty.\n";

  if(mesg != '')
  {
    alert(mesg);
    return false;
  }
  else
  {
    if(confirm("Are you sure to update this form?"))
      return true;
    else
      return false;
  }
}

</script>

<%@ page import="java.sql.*" %>
<%@ page import="datamodel.*" %>
<%@ page contentType="text/html; charset=Big5" %>
<jsp:useBean id="connection" scope="session" class="jsp.ConnectionBean" />
<jsp:useBean id="status" scope="page" class="jsp.SchoolStatusBean" />
<jsp:useBean id="reg" scope="page" class="jsp.RegistrationManagementBean" />
<jsp:useBean id="student" scope="page" class="jsp.StudentManagementBean" />
<jsp:useBean id="tuition" scope="page" class="jsp.StudentPaymentBean" />
<jsp:useBean id="uniform" scope="page" class="jsp.UniformPaymentBean" />

<%
  Connection con = connection.getConnection();

  String familyIdStr = request.getParameter("familyId");
  String studentIdStr = request.getParameter("studentId");
  //parse the infomation that come from paymentSearch.jsp
  String reqYear = request.getParameter("reqYear");
  String reqSemester = request.getParameter("reqSemester");
  long familyId = Long.parseLong(familyIdStr);
  long studentId = Long.parseLong(studentIdStr);

  SchoolStatus schoolStatus = status.getStatus(con);
  student.getDataById(con, studentId);
%>

<body>
<jsp:include page="adminHeader.jsp?activeTab=student" />

<form name="myForm" method="post" action="../../AdminRegistrationManagementServlet?action=update" onSubmit="return validateInput()">
<a href="javascript:goBack('<%= familyIdStr %>', '<%= reqYear %>', '<%= reqSemester %>' )"><< Back</a>

<h3>Class Registration</h3>
<%= student.getFirstName() %>&nbsp;<%= student.getLastName() %>&nbsp;<%= student.getChineseName() %>
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

    int chineseId = reg.getRegisteredCourse(con, studentId, year, semester, ClassManager.CHINESE);
    int activityId = reg.getRegisteredCourse(con, studentId, year, semester, ClassManager.ACTIVITY);
    StudentPayment sp = tuition.getTuitionFor(con, studentId, year, semester);
    int amount = sp.getTuition();
    String checkNo = sp.getCheckNo();
    String payTerm =sp.getSemester();

    // check if Generate Receipt should be disabled or not
    String disabled = "";
    if(amount <= 0)
      disabled = "DISABLED";

    // uniform payment
    String uniformAmount = "";
    String uniformCheckNo = "";
    String newUniformPayment = "no";
    UniformPayment uniformPay = uniform.getUniformPayment(con, studentId, year, semester);
    if(uniformPay != null)
    {
       if(uniformPay.getAmount() != 0)
         uniformAmount = String.valueOf(uniformPay.getAmount());
       if(uniformPay.getCheckNo() != null)
         uniformCheckNo = uniformPay.getCheckNo();
    }
    else
      newUniformPayment = "yes";
%>

<script language="javascript">

oldChineseId = <%= chineseId %>;

</script>

<input type="hidden" name="familyId" value="<%= familyId %>">
<input type="hidden" name="studentId" value="<%= studentId %>">
<input type="hidden" name="oldChineseId" value="<%= chineseId %>">
<input type="hidden" name="oldActivityId" value="<%= activityId %>">
<input type="hidden" name="schoolYear" value="<%= year %>">
<input type="hidden" name="schoolSemester" value="<%= semester %>">
<input type="hidden" name="oldAmount" value="<%= amount %>">
<input type="hidden" name="oldCheckNo" value="<%= checkNo %>">
<input type="hidden" name="oldPayTerm" value="<%= payTerm %>">
<input type="hidden" name="reqYear" value="<%= reqYear %>">
<input type="hidden" name="reqSemester" value="<%= reqSemester%>">
<input type="hidden" name="newUniformPayment" value="<%= newUniformPayment %>">

<P>
School Calendar Year:&nbsp;<%= year %>
&nbsp;&nbsp;
School Semester:&nbsp;<%= semester %>
</P>

<table>
  <tr>
    <td align="right">Chinese</td>
    <td><select name="chineseId"><%= reg.getChineseOptions(con, year, semester, chineseId) %></select></td>
  </tr>
  <tr>
    <td align="right">Activity</td>
    <td><select name="activityId"><%= reg.getActivityOptions(con, year, semester, activityId) %></select></td>
  </tr>
  <tr>
    <td align="right">µu³S Summer Uniform Size</td>
    <td><select size="1" name="summerUniformSize">
        <jsp:getProperty name="student" property="summerUniformSizeOptions"/>
        </select><!--&nbsp;&nbsp;Received<input type="checkbox" name="summerUniformReceived" <%= student.getSummerUniformReceived() %>>-->
    </td>
  </tr>
  <tr>
    <td align="right">ªø³S Winter Uniform Size</td>
    <td><select size="1" name="winterUniformSize">
        <jsp:getProperty name="student" property="winterUniformSizeOptions"/>
        </select><!--&nbsp;&nbsp;Received<input type="checkbox" name="winterUniformReceived" <%= student.getWinterUniformReceived() %>>-->
    </td>
  </tr>
  <tr>
    <td align="right">Current School Grade Level</td>
    <td><INPUT type="text" name="gradeOfSchool" size="5" value="<jsp:getProperty name="student" property="gradeOfSchool"/>"></td>
  </tr>
</table>
<br>
<table><tr>
<td><h4>Tuition</h4>
<table border="1">
  <tr><th>Amount</th><th>School Term</th><th>Payment Type</th><th>Check No.</th></tr>
  <tr align="center">
    <td>$<input type="text" name="amount" size="10" value ="<%=amount%>"></td>
    <td>
      <select name="term">
      <%=tuition.getTermOptions(payTerm)%>
      </select>
    </td>
    <td>
      <select name="payment">
         <jsp:getProperty name="tuition" property="paymentTypeOptions"/>
      </select>
    </td>
    <td><input type="text" name="checkNo" size="10" value ="<%=checkNo%>"></td>
  </tr>
</table></td>
<td><h4>Uniform</h4>
<table border="1">
  <tr><th>Amount</th><th>Payment Type</th><th>Check No.</th><th>µu³S</th><th>ªø³S</th></tr>
  <tr align="center">
    <td>$<input type="text" name="uniformAmount" size="10" value ="<%= uniformAmount%>"></td>
    <td>
      <select name="uniformPayment">
         <jsp:getProperty name="uniform" property="paymentTypeOptions"/>
      </select>
    </td>
    <td><input type="text" name="uniformCheckNo" size="10" value ="<%= uniformCheckNo %>"></td>
    <td>
      <select size="1" name="paySummerUniformSize">
        <jsp:getProperty name="uniform" property="summerUniformSizeOptions"/>
      </select>
    </td>
    <td>
      <select size="1" name="payWinterUniformSize">
        <jsp:getProperty name="uniform" property="winterUniformSizeOptions"/>
      </select>
    </td>
  </tr>
</table></td>
</tr></table>
<hr><br>
<INPUT type="submit" value="Update">&nbsp;&nbsp;&nbsp;&nbsp;
<input type="button" value="Generate Receipt" onClick="generateReceipt()" <%= disabled %>>
<%
  }
%>

</form>
</body>
</html>
