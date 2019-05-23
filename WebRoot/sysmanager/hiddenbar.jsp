<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<html>
<head>
<style>
.hidden_bar{cursor:hand;width:100%;height:100%;padding-top:2px;background :#C2D5F9;BORDER: #646c94 1px solid;}
</style>
<script language="javascript">
<!--
function show(){
  window.parent.main.cols="0,35%,*";
}
//-->
</script>
</head>
<body leftmargin="0" topmargin="0" class="tree_body">
   <div class="hidden_bar" align="center" onclick="javascript:show();" title="<bean:message key='catalog.restore'/>">
     <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
       <tr>
         <td align="center" valign="middle">
           <img src="../common/images/arrow.gif">
         </td>
       </tr>
     </table>
   </div>
</body>
</html>

