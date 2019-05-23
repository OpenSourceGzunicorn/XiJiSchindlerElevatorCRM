<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="qclogManagementBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd">录入人:</td>
    <td style="width: 35%"><bean:write name="qclogManagementBean" property="operId"/></td>
    <td class="wordtd">录入日期:</td>
    <td style="width: 35%"><bean:write name="qclogManagementBean" property="operDate"/></td>
 </tr>
  <tr>
    <td class="wordtd">维保站:</td>
    <td style="width: 35%"><bean:write name="qclogManagementBean" property="maintStation"/></td>
    <td class="wordtd">维保工:</td>
    <td style="width: 35%"><bean:write name="qclogManagementBean" property="maintPersonnel"/></td>
 </tr>
  <tr>
    <td class="wordtd">电梯编号:</td>
    <td style="width: 35%"><bean:write name="qclogManagementBean" property="elevatorNo"/></td>
    <td class="wordtd">月度例会:</td>
    <td style="width: 35%"><bean:write name="qclogManagementBean" property="ydlh"/></td>
 </tr>
  <tr>
    <td class="wordtd">维保负责人是否对工作负责:</td>
    <td style="width: 35%"><bean:write name="qclogManagementBean" property="isgzfz"/></td>
    <td class="wordtd">维保负责人是否存在作风问题:</td>
    <td style="width: 35%"><bean:write name="qclogManagementBean" property="iszfwt"/></td>
 </tr>
  <tr>
    <td class="wordtd">甲方反馈问题:</td>
    <td style="width: 35%"><bean:write name="qclogManagementBean" property="jffkwt"/></td>
    <td class="wordtd">远程监控问题:</td>
    <td style="width: 35%"><bean:write name="qclogManagementBean" property="ycjkwt"/></td>
 </tr>
   <tr>
    <td class="wordtd">备注:</td>
    <td style="width: 35%" colspan="3"><bean:write name="qclogManagementBean" property="rem"/></td>
 </tr>
 </table>
	
</logic:present>