<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>

<!--
	控制导航栏的显示
	后续页面要提供多一个</table>与此处匹配第一个<table width="100%">
-->

<table width="100%">
  <tr>
    <td width="100%">
      <table width="100%" cellspacing="0" cellpadding="0" class="top_navigation" >
        <tr>
          <td valign="middle" height="23">
            &nbsp;&nbsp;<bean:message key="navigator.user" /> ：<bean:write name="USER_INFO" scope="session" property="userName"/>
			&nbsp;&nbsp;<bean:message key="navigator.location" /> ：
						<logic:present name="navigator.location" >
							<bean:write name="navigator.location"/>
						</logic:present>
          </td>
        </tr>
      </table>
    </td>
  </tr>