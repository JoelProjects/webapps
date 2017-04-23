var agree = null;
function checkAgreement()
{
  if(agree == null)
  {
    alert("You need to choose one.");
    return false;
  }
  else
  {
    if(agree != true)
      document.forms[0].action = "../index.html";
    else
      document.forms[0].action = "newAccount.html";
    document.forms[0].submit();
  }
}
function checkRadio(index)
{
  if(index == 1)
    agree = true;
  else
    agree = false;
}
