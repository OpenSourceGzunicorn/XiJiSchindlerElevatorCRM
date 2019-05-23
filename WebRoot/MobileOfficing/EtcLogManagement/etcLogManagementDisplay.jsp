<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="etclogManagementBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd">录入人:</td>
    <td style="width: 35%"><bean:write name="etclogManagementBean" property="operId"/></td>
    <td class="wordtd">录入日期:</td>
    <td style="width: 35%"><bean:write name="etclogManagementBean" property="operDate"/></td>
 </tr>
  <tr>
    <td class="wordtd">合同号:</td>
    <td style="width: 35%"><bean:write name="etclogManagementBean" property="contractNo"/></td>
    <td class="wordtd">项目名称:</td>
    <td style="width: 35%"><bean:write name="etclogManagementBean" property="projectName"/></td>
 </tr>
  <tr>
    <td class="wordtd">安装单位:</td>
    <td style="width: 35%"><bean:write name="etclogManagementBean" property="insCompanyName"/></td>
    <td class="wordtd">配合人数:</td>
    <td style="width: 35%"><bean:write name="etclogManagementBean" property="phnum"/></td>
 </tr>
  <tr>
    <td class="wordtd">厂检/维修:</td>
    <td style="width: 35%"><bean:write name="etclogManagementBean" property="iscjwx"/></td>
    <td class="wordtd">是否自检:</td>
    <td style="width: 35%"><bean:write name="etclogManagementBean" property="iszj"/></td>
 </tr>
  <tr>
    <td class="wordtd">现场反馈问题:</td>
    <td style="width: 35%"><bean:write name="etclogManagementBean" property="xcfkwt"/></td>
    <td class="wordtd">厂检完成情况汇总:</td>
    <td style="width: 35%"><bean:write name="etclogManagementBean" property="workContent"/></td>
 </tr>
   <tr>
    <td class="wordtd">备注:</td>
    <td style="width: 35%" colspan="3"><bean:write name="etclogManagementBean" property="rem"/></td>
 </tr>
 </table>
	
</logic:present>