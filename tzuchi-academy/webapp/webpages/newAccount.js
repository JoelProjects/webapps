/* display some info */
function displayInfo()
{
  var queryString = location.search;  // get query string
  if(getParameter("error", queryString) == "1")  // if error
  {
    var username = getParameter("username", queryString);
    document.write("<FONT COLOR=red>Username '" + username + "' exists!<BR>");
    document.write("Please choose other username.</FONT>");
  }
}
/* get value part for a given name from a query string */
function getParameter(name, queryString) 
{
  var value = "";
  var strArray = queryString.split("&");
  name = name + "=";
  for(var i = 0; i < strArray.length; i++) 
  {
    if(strArray[i].indexOf(name) != -1)
    {
      var nameValue = strArray[i].split("=");
      value = nameValue[1];  // get value part
    }
  }
  return value;
}
/* validate username and password */
function validateInput() 
{
  var invalid = " "; // invalid character is a space
  var minLength = 6; // minimum length
  var maxLength = 40; // maximum length
  var username = document.myForm.username.value;
  var pw1 = document.myForm.passwd.value;
  var pw2 = document.myForm.repasswd.value;
  /* check for username */
  if(username == '')
  {
    alert("Username should not be empty");
    return false;
  }
  if(username.length < minLength)
  {
    alert("Username should be between " + minLength + " and " + maxLength + " characters long.");
    return false;
  }
  if(username.indexOf(invalid) > -1) 
  {
    alert("Spaces are not allowed.");
    return false;
  }
  /* check for password */
  if(pw1 == '' || pw2 == '')
  {
    alert("Please enter your password twice.");
    return false;
  }
  if(pw1.length < minLength)
  {
    alert("Password should be between " + minLength + " and " + maxLength + " characters long.");
    return false;
  }
  if(pw1.indexOf(invalid) > -1) 
  {
    alert("Spaces are not allowed.");
    return false;
  }
  else 
  {
    if(pw1 != pw2) 
    {
      alert("You did not enter the same password. Please re-enter your password.");
      return false;
    }
    else 
    {
      if(confirm("Are you sure?"))
        return true;
      else
        return false;
    }
  }
}
