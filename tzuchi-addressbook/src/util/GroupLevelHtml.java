package util;

public class GroupLevelHtml
{
   private StringBuffer htmlList;

   public static String getHtml(int level)
   {
      return getHtml(level, false);
   }

   public static String getHtml(int level, boolean editButtonOnly)
   {
      StringBuffer htmlList = new StringBuffer();

      String group;
      for(int i = 0; i < level; i++)
      {
         if(i == 0)
            htmlList.append("<div id=\"" + i).append("\">").append("\n");
         else
            htmlList.append("<div id=\"" + i).append(
                  "\" style=\"visibility:hidden\">").append("\n");

         htmlList.append("<table>").append("\n");
         htmlList.append("<tr>").append("\n");
         htmlList.append("<td rowspan=\"6\">").append("\n");

         group = "group" + i;
         if(i < level - 1)
            htmlList.append("<select name=\"").append(group).append(
                  "\" size=\"6\" style=\"width:300\" onChange=\"setVisible("
                        + (i + 1)).append(")\">").append("\n");
         else
            htmlList.append("<select name=\"").append(group).append(
                  "\" size=\"6\" style=\"width:300\">").append("\n");

         if(i == 0)
         {
            htmlList.append("<script language=\"javascript\">").append("\n");
            htmlList.append("getGroupList(0)").append("\n");
            htmlList.append("</script>").append("\n");
         }

         htmlList.append("</select>").append("\n");
         htmlList.append("</td>").append("\n");
         htmlList.append(
               "<td rowspan=\"1\" align=\"center\" valign=\"center\">").append(
               "\n");

         if(editButtonOnly)
            htmlList
                  .append(
                        "<input type=\"button\" name=\"edit\" value=\"Edit\" onClick=\"editGroup('")
                  .append(group).append("')\">").append("\n");
         else
         {
            htmlList
                  .append(
                        "<input type=\"button\" name=\"add\" value=\"Add\" onClick=\"addGroup('")
                  .append(group).append("')\">").append("\n");
            htmlList
                  .append(
                        "<input type=\"button\" name=\"delete\" value=\"Delete\" onClick=\"deleteGroup('")
                  .append(group).append("')\">").append("\n");
            htmlList
                  .append(
                        "<input type=\"button\" name=\"rename\" value=\"Rename\" onClick=\"renameGroup('")
                  .append(group).append("')\">").append("\n");
         }

         htmlList.append("</td>").append("\n");
         htmlList.append("</table>").append("\n");
         htmlList.append("</div>").append("\n");
         htmlList.append("<br>").append("\n");
      }

      return htmlList.toString();
   }
}
