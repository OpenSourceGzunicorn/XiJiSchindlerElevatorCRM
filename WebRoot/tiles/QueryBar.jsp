<%@ page contentType="text/html;charset=GBK" %>
<html>
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
  <tr>
    <td width="100%" height="20" class="searchTable">
      <fieldset style="height:70px;">
  <legend>列表查询</legend>
      <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" align="center" >
        <form action="Defaultflow.jsp" method="post" name="manager" OnSubmit="javascript: return CheckAction()">
        <tr>
          <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td align="right" width="15%" valign="bottom">

                </td>

                </td>
              </tr>
            </table>
          </td>
          <td align="middle">
            <input type="hidden" name="folder_code" id="folder_code" value="">
            <span class="btn_common" onclick="javascript:querySubmit('manager');">查询</span>
          </td>
        </tr>
        </form>
        <tr></tr>
      </table>
      </fieldset>
    </td>
  </tr>
</html>
