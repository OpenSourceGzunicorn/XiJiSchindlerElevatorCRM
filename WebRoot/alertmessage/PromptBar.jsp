<html>

<head>
<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-tiles.tld" prefix="tiles"%>
 <logic:messagesPresent message="true">

  <html:messages id="message" message="true">
  
  
<style>
    .error_field{color:red;text-align:center;width:100%;background-color:#eeeeee;padding-left:2px;padding-right:2px;padding-top:2px;padding-bottom:2px;border:black 0px solid;}
</style>

   <table border="0" width="720" bgcolor="#C0C0C0" height="4">
    <tr>
		<td class="error_field" width="100%" height="4">
		<font size="2"><bean:write name="message"/>
		<script>alert('<bean:write name="message"/>');</script>
		</font>
		</td>
    </tr>
  </table>
 </html:messages>
</logic:messagesPresent>
</html>

