function isInteger(value)
{
  if(isEmpty(value))
    return true;
  var anum = /(^\d+$)/;
  if(anum.test(value))
    testresult = true;
  else
  {
    testresult = false;
  }
  return (testresult);
}
function isNumber(value)
{
  if(isEmpty(value))
    return true;
  var anum = /(^\d+$)|(^\d+\.\d+$)/;
  if(anum.test(value))
    testresult = true;
  else
  {
    testresult = false;
  }
  return (testresult);
}
function isEmpty(s)
{   
  return ((s == null) || (s.length == 0));
}
// trim white spaces from a string
function trim(str)
{
  var re = /\s*/;
  return str.replace(re, "");
}
