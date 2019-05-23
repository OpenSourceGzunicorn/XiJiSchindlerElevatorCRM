<%@page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>


<logic:present name="returningMaintainBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd">合同联系人:</td>
    <td class="inputtd"><bean:write name="returningMaintainBean" scope="request" property="contacts"/></td>
  </tr>
  <tr>
    <td class="wordtd">合同联系人电话:</td>
    <td class="inputtd"><bean:write name="returningMaintainBean" scope="request" property="contactPhone"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="returningmaintain.reOrder"/>:</td>
    <td class="inputtd"><bean:write name="returningMaintainBean" scope="request" property="reOrder"/></td>
  </tr>
  
  <tr>
    <td class="wordtd"><bean:message key="returningmaintain.reMark"/>:</td>
    <td class="inputtd">
	<logic:match name="returningMaintainBean" property="reMark" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="returningMaintainBean" property="reMark" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  
  <tr>
    <td class="wordtd"><bean:message key="returningmaintain.enabledflag"/>:</td>
    <td class="inputtd">
	<logic:match name="returningMaintainBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="returningMaintainBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="returningmaintain.rem"/>:</td>
    <td class="inputtd"><bean:write name="returningMaintainBean" scope="request" property="rem"/></td>
  </tr>
</table>
<br/>
<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  &nbsp;<!-- <input type="button" value=" + " onclick="addElevators('dynamictable_0')" class="default_input">
  <input type="button" value=" - " onclick="deleteRow('dynamictable_0')" class="default_input"> -->           
  <b>&nbsp;回访客户维护明细</b>
</div>
<div id="wrap_0" style="overflow: scroll;">
  <table id="dynamictable_0" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <thead>
      <tr id="titleRow_0">
        <td class="wordtd_header" ><input type="checkbox" name="cbAll" onclick="checkTableAll('dynamictable_0',this)"/></td>
        <td class="wordtd_header" nowrap="nowrap">合同类型</td>
        <td class="wordtd_header" nowrap="nowrap">合同号</td>
        <td class="wordtd_header" nowrap="nowrap">合同开始日期</td>
        <td class="wordtd_header" nowrap="nowrap">合同结束日期</td>
      </tr>
    </thead>
    <tfoot>
      <tr height="15"><td colspan="20"></td></tr>
    </tfoot>
    <tbody>
      <!-- <tr id="sampleRow_0" style="display: none;">
        <td align="center"><input type="checkbox" onclick="cancelCheckAll('dynamictable_0','cbAll')"/></td>
        <td style="display: none;"><input type="text" name="billno" class="noborder_center"/></td>
        <td style="display: none;"><input type="text" name="wbBillno" readonly="readonly" class="noborder_center"/></td>
        <td align="center"><input type="text" name="maintContractNo" onclick="simpleOpenWindow('elevatorSaleAction',this.value);" readonly="readonly" class="link noborder_center"/></td>
        <td align="center"><input type="text" name="contractSdate" readonly="readonly" class="noborder_center"/></td>
        <td align="center"><input type="text" name="contractEdate" readonly="readonly" class="noborder_center"/></td>
      </tr> -->
      <logic:present name="returningMaintainDetailList">
        <logic:iterate id="e" name="returningMaintainDetailList">
          <tr>
            <td align="center"><input type="checkbox" onclick="cancelCheckAll(this)"/></td>
            <%-- <td style="display: none;"><input type="hidden" name="numno" value="${e.numno}"/></td>
            <td style="display: none;"><input type="hidden" name="billno" value="${e.billno}"/></td>
            <td style="display: none;"><input type="hidden" name="wbBillno" value="${e.wbBillno}"/></td> --%>
            <td align="center">${e.r4}<input type="hidden" name="r4" value="${e.r4}"/><input type="hidden" name="wbBillno" value="${e.wbBillno}"/></td>
            <td align="center">
            	<a onclick="contractdisplay(this);" class="link" >${e.maintContractNo}</a>
            	<input type="hidden" name="maintContractNo" value="${e.maintContractNo}"/>
            </td>
            <td align="center">${e.contractSdate}</td>
            <td align="center">${e.contractEdate}</td>
          </tr>
        </logic:iterate>
      </logic:present>
    </tbody>    
  </table>
</div>
</logic:present>