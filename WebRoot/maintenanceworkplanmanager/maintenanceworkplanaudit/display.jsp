<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<logic:present name="maintContractMasterBean">
   <table id="contractInfoTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td height="23" colspan="6">&nbsp;<b>合同主信息</td>
    </tr>
    <tr >
      <td class="wordtd_a"><bean:message key="maintContract.maintContractNo"/>:</td>
      <td width="20%">
         
        <a href="<html:rewrite page='/maintContractAction.do'/>?method=toDisplayRecord&typejsp=Yes&id=<bean:write name="maintContractMasterBean"  property="billNo"/>" target="_blnk"><bean:write name="maintContractMasterBean"  property="maintContractNo"/></a>          
        <html:hidden name="maintContractMasterBean"  property="billNo"/>        
      </td>          
      <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContract.contractNatureOf"/>:</td>
      <td width="20%">自保</td>
      <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContract.mainMode"/>:</td>
      <td width="20%">
        <logic:match name="maintContractMasterBean" property="mainMode" value="FREE">免费</logic:match>
        <logic:match name="maintContractMasterBean" property="mainMode" value="PAID">收费</logic:match>
      </td>         
    </tr>  
    <tr>
      <td class="wordtd_a"><bean:message key="maintContract.contractPeriod"/>:</td>
      <td>
        <span class="renewal show"><bean:write name="maintContractMasterBean" property="contractPeriod"/></span>  
        <span class="renewal hide"><input type="text" name="contractPeriod" class="default_input_noborder"></span>         
      </td>          
      <td class="wordtd_a"><bean:message key="maintContract.contractSdate"/>:</td>
      <td>
        <span class="renewal show"><bean:write name="maintContractMasterBean" property="contractSdate"/></span>      
      </td>
      <td class="wordtd_a"><bean:message key="maintContract.contractEdate"/>:</td>
      <td>
        <span class="renewal show"><bean:write name="maintContractMasterBean" property="contractEdate"/></span>
      </td>         
    </tr>        
    <tr>
      <td class="wordtd_a"><bean:message key="maintContract.maintDivision"/>:</td>
      <td>
        <bean:write name="maintContractMasterBean" property="maintDivision"/>
      </td> 
      <td class="wordtd_a"><bean:message key="maintContract.attn"/>:</td>
      <td>
        <bean:write name="maintContractMasterBean" property="attn"/>
      </td>
      <td class="wordtd_a"></td>
      <td></td>         
    </tr>    
  </table>
 </logic:present>
 <br/>
  <logic:present name="maintContractDetailList">
  <div style="border:solid #999999 1px;border-bottom:none;padding-top: 4;padding-bottom: 4;background:#ffffff;">        
    <b>&nbsp;合同明细</b>
  </div>
  <div id="scrollBox" style="overflow:scroll;">

  <table id="scrollTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">

    <thead>
    <tr >
    <td class="wordtd_header" nowrap="nowrap" style="text-align: center;">电梯编号</td>
    <td class="wordtd_header" nowrap="nowrap" style="text-align: center;">电梯类型</td>
    <td class="wordtd_header" nowrap="nowrap" style="text-align: center;">层/站/门</td>
        <td class="wordtd_header" nowrap="nowrap" style="text-align: center;">提升高度</td>
    <td class="wordtd_header" nowrap="nowrap" style="text-align: center;">年检日期</td>
    <td class="wordtd_header" nowrap="nowrap" style="text-align: center;">销售合同号</td>
    <td class="wordtd_header" nowrap="nowrap" style="text-align: center;">项目名称</td>
    <td class="wordtd_header" nowrap="nowrap" style="text-align: center;">维保开始时间</td>
    <td class="wordtd_a" style="text-align: center;" nowrap="nowrap">维保计划开始时间</td>
    <td class="wordtd_header" nowrap="nowrap" style="text-align: center;">维保结束时间</td>
    <td class="wordtd_header" nowrap="nowrap" style="text-align: center;">下达维保站</td>
    <td class="wordtd_header" nowrap="nowrap" style="text-align: center;">维保工</td>
    <td class="wordtd_header" nowrap="nowrap" style="text-align: center;">保养逻辑</td>
    </tr>
    </thead>
    <tbody>
    <logic:iterate id="element" name="maintContractDetailList">
    <tr>
    <td nowrap="nowrap" style="text-align: center;">
	<input style="width: 110px;" onclick="simpleOpenWindow('elevatorSaleAction',this.value);" class="link noborder_center" readonly="readonly" value="<bean:write name="element" property="elevatorNo" />" name="elevatorNo">	
	<input type="hidden" name="rowid" value="${element.rowid}" >
	</td>
	<td nowrap="nowrap" style="text-align: center;">${element.elevatorType=='T'?'直梯':'扶梯'}
	</td>
	<td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="floor" />/<bean:write name="element" property="stage" />/<bean:write name="element" property="door" /></td>
	<td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="high" /></td>
	<td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="annualInspectionDate" /></td>
	
	<td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="salesContractNo" /></td>
	<td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="projectName" /></td>
	<td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="mainSdate" ></bean:write> </td>
	<td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="shippedDate" ></bean:write> </td>
	<td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="mainEdate" ></bean:write></td>
	<td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="assignedMainStation" /></td>
    <td nowrap="nowrap" style="text-align: center;">
    &nbsp;<a href="<html:rewrite page='/maintenanceWorkPlanAuditAction.do'/>?method=toMaintenanceWorkPlanDisplayRecord&isOpen=Y&id=${element.r1}" target="_blnk"><bean:write name="element" property="maintPersonnel"/></a>&nbsp;
	</td>
    <td nowrap="nowrap" style="text-align: center;">
		<logic:equal name="element" property="r2" value="1">逻辑一</logic:equal>
		<logic:equal name="element" property="r2" value="2">逻辑二</logic:equal>
		<logic:equal name="element" property="r2" value="3">逻辑三</logic:equal>
		<logic:equal name="element" property="r2" value="4">逻辑四</logic:equal>
		<logic:equal name="element" property="r2" value="5">逻辑五</logic:equal>
	</td>   
    </tr>
    </logic:iterate>
    </tbody>
    </table>
    </div>
    <script type="text/javascript"> 
	window.onload = function() {			
		setScrollTable('scrollBox','scrollTable',10); // 设置滚动表格
	}
  </script> 
</logic:present>