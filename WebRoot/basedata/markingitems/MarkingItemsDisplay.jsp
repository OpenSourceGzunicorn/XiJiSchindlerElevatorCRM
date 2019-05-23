<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="markingScoreBean">
<html:hidden property="id" value="${markingScoreBean.msId}" />
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td width="20%" class="wordtd"><bean:message key="markingitems.mtId"/>:</td>
    <td width="80%" class="inputtd"><bean:write name="markingScoreBean" scope="request" property="msId"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="markingitems.mtName"/>:</td>
    <td class="inputtd"><bean:write name="markingScoreBean" scope="request" property="msName"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="markingitems.fraction"/>:</td>
    <td class="inputtd"><bean:write name="markingScoreBean" scope="request" property="fraction"/></td>
  </tr>
  <tr>
    <td class="wordtd">电梯类型:</td>
    <td class="inputtd"><bean:write name="markingScoreBean" scope="request" property="elevatorType"/></td>
  </tr>
     <tr>
    <td class="wordtd">排序号:</td>
    <td nowrap="nowrap">
    <bean:write name="markingScoreBean" scope="request" property="orderby"/>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="markingitems.enabledflag"/>:</td>
    <td class="inputtd">
	<logic:match name="markingScoreBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="markingScoreBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
	</tr>
	
  <tr>
    <td class="wordtd"><bean:message key="markingitems.rem"/>:</td>
    <td class="inputtd"><bean:write name="markingScoreBean" scope="request" property="rem"/></td>
  </tr>
</table>
<br/>
<table id="table_ms" class="dynamictable tb" width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr height="23">
      <td colspan="3">            
        <b>评分明细</b>
      </td>
    </tr>
    
    <tr id="titleRow_0">
      <td class="wordtd_header">评分明细代码</font></td>
      <td class="wordtd_header">评分明细名称</font></td>
      
    </tr>
 <logic:present name="msdList">
    <logic:iterate id="ele" name="msdList">
    <tr>
    <td width="30%"><bean:write name="ele" property="detailId"/></td>
    <td><bean:write name="ele" property="detailName"/></td>
    </tr>
    </logic:iterate>
  </logic:present>  
</table>
</logic:present>
