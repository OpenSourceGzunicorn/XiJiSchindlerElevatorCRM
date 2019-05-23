<%@page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">

<html:errors/>
<logic:present name="errorstrinfo"><font color="red"><bean:write name="errorstrinfo"/></font></logic:present>

<html:form action="/effectiveElevatorAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden name="lostElevatorMaintainBean" property="billno"/>
<html:hidden property="id" value="${lostElevatorMaintainBean.billno}"/>
<%-- <html:hidden property="contracts" /> --%>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="returningmaintain.companyName"/>:</td>
    
      
      <td width="80%" class="inputtd">
      <bean:write name="lostElevatorMaintainBean" scope="request" property="companyName"/>
      <html:hidden name="lostElevatorMaintainBean" property="companyName"/>
      <%-- 
      <html:hidden name="lostElevatorMaintainBean" property="companyId"/>
      --%>
    </td>     
  </tr>
  <tr>
    <td class="wordtd">联系人:</td>
  
    	 <td class="inputtd"><bean:write name="lostElevatorMaintainBean" scope="request" property="contacts"/>
    	 <html:hidden name="lostElevatorMaintainBean" property="contacts"/>
    	 </td>
      
  </tr>
  <tr>
    <td class="wordtd">联系人电话:</td>
   <td class="inputtd"><bean:write name="lostElevatorMaintainBean" scope="request" property="contactPhone"/>
   	 <html:hidden name="lostElevatorMaintainBean" property="contactPhone"/>
   </td>
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="returningmaintain.reOrder"/>:</td>
    <td><html:text name="lostElevatorMaintainBean" property="reOrder" styleClass="default_input"  onblur="onlyNumber(this)" onkeypress="f_check_number2()" /><font color="red">*</font></td>
  </tr> 
 
  <tr>
    <td class="wordtd"><bean:message key="returningmaintain.reMark"/>:</td>
    <td>
	  <html:radio name="lostElevatorMaintainBean" property="reMark" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="lostElevatorMaintainBean" property="reMark" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
 
  <tr>
    <td class="wordtd"><bean:message key="returningmaintain.enabledflag"/>:</td>
    <td>
	  <html:radio name="lostElevatorMaintainBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="lostElevatorMaintainBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="returningmaintain.rem"/>:</td>
    <td><html:textarea name="lostElevatorMaintainBean" property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
<br/>
<%-- <div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  &nbsp;<input type="button" value=" + " onclick="addElevators('dynamictable_0')" class="default_input">
  <input type="button" value=" - " onclick="deleteRow('dynamictable_0')" class="default_input">           
  <b>&nbsp;丢梯客户维护明细</b>
</div>
<div id="wrap_0" style="overflow: scroll;">
  <table id="dynamictable_0" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <thead>
      <tr id="titleRow_0">
        <td class="wordtd_header" ><input type="checkbox" name="cbAll" onclick="checkTableAll('dynamictable_0',this)"/></td>
        <td class="wordtd_header" nowrap="nowrap">维保合同号</td>
        <td class="wordtd_header" nowrap="nowrap">丢梯日期</td>
        <td class="wordtd_header" nowrap="nowrap">原因分析</td>
      </tr>
    </thead>
    <tfoot>
      <tr height="15"><td colspan="20"></td></tr>
    </tfoot>
    <tbody>
      <tr id="sampleRow_0" style="display: none;">
        <td align="center"><input type="checkbox" onclick="cancelCheckAll('dynamictable_0','cbAll')"/></td>
        <td style="display: none;"><input type="text" name="wb_billno" readonly="readonly" class="noborder_center"/></td>
       	<td style="display: none;"><input type="text" name="jnlno" readonly="readonly" class="noborder_center"/></td>
        <td style="display: none;"><input type="text" name="causeAnalysis" readonly="readonly" class="noborder_center"/></td>
        <td align="center"><input type="text" name="maintContractNo" onclick="simpleOpenWindow('lostElevatorMaintainDetailAction',this.value);" readonly="readonly" class="link noborder_center"/></td>
        <td align="center"><input type="text" name="lostElevatorDate" readonly="readonly" class="noborder_center"/></td>
         <td align="center"><input type="text" name="pullname" readonly="readonly" class="noborder_center"/></td>
      </tr>
      <logic:present name="lostElevatorMaintainDetailList">
        <logic:iterate id="e" name="lostElevatorMaintainDetailList">
          <tr>
            <td align="center"><input type="checkbox" onclick="cancelCheckAll(this)"/></td>
       		<td style="display: none;"><input type="hidden" name="numno" value="${e.numno}"/></td>
            <td style="display: none;"><input type="hidden" name="billNo" value="${e.lostElevatorMaintain.billno}"/></td>
            <td style="display: none;"><input type="hidden" name="jnlno" value="${e.jnlno}"/></td>
            <td style="display: none;"><input type="hidden" name="wb_billno" value="${e.wb_billno}"/></td>
            <td align="center">
            <a onclick="simpleOpenWindow('lostElevatorMaintainDetailAction','${e.maintContractNo}');" class="link">
            ${e.maintContractNo}</a>
            <input type="hidden" name="maintContractNo" value="${e.maintContractNo}"/></td>
            <td align="center">${e.lostElevatorDate}<input type="hidden" name="lostElevatorDate" value="${e.lostElevatorDate}"/></td>
            <td align="center">${e.pullname}<input type="hidden" name="causeAnalysis" value="${e.causeAnalysis}"/></td>
          </tr>
        </logic:iterate>
      </logic:present>
    </tbody>    
  </table>
</div>
<br> --%>
<script type="text/javascript"> 
	/* $("document").ready(function() {
		initPage();  
	})
	
	function initPage(){	
		setDynamicTable("dynamictable_0","sampleRow_0");// 设置动态增删行表格
		
	} */
</script>
</html:form>