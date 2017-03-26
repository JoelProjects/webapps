<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<link rel="stylesheet" type="text/css" href="tzuchi.css">
</head>
<title>Logout</title>

<script type="text/javascript">
// remove frame set
if(parent.frames.length!=0) 
{
  window.top.location.replace("logout.jsp") 
}
else 
{}
</script>

<%request.getSession().removeAttribute("userID");
         request.getSession().invalidate();
      %>

<body>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
   <tr>
      <td align="center">
      <table width="60%" cellpadding="0" cellspacing="0" border="0">
         <tr>
            <td align="center">
            <h2>Logout Instructions
            <h2>
            </td>
         </tr>
         <tr>
            <td><br>
            <br>
            Please close all of your open browser windows to completely
            log out of your account.</td>
         </tr>
         <tr>
            <td><br>
            <br>
            Thanks!</td>
         </tr>
      </table>
      </td>
   </tr>
</table>
</body>
</html>
