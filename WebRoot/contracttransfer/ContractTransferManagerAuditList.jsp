<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<html:errors />
<br>
<html:form action="/ServeTable.do">
<html:hidden property="property(genReport)" styleId="genReport" />
	<table>
		<tr>
			<td>流水号：</td>
			<td><html:text property="property(billNo)" styleClass="default_input" /></td>
			<td>&nbsp;&nbsp;维保合同号：</td>
			<td><html:text property="property(maintContractNo)" styleClass="default_input" /></td>
			<td>&nbsp;&nbsp;销售合同号：</td>
			<td><html:text property="property(salesContractNo)" styleClass="default_input" /></td>
			<td>&nbsp;&nbsp;甲方单位名称：</td>
			<td><html:text property="property(companyId)" size="30" styleClass="default_input" /></td>
		</tr>
		<tr>
			<td>电梯编号：</td>
			<td><html:text property="property(elevatorNo)" styleClass="default_input" /></td>
			<td>&nbsp;&nbsp;审核状态：</td>
			<td>
			<html:select property="property(auditStatus2)">
	          <html:option value="">全部</html:option>
			  <html:option value="N">未审核</html:option>
			  <html:option value="Y">已审核</html:option>
	        </html:select>
	        </td>                   
			<td>&nbsp;&nbsp;<bean:message key="maintContract.maintDivision" />:</td>
			<td>
				<html:select property="property(maintDivision)" styleId="maintdivision" onchange="Evenmore(this,'maintstation')">
				<html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
				</html:select>
			</td>   
			<td>&nbsp;&nbsp;所属维保站:</td>
			<td>
				<html:select property="property(maintStation)" styleId="maintstation">
				<html:options collection="mainStationList" property="storageid" labelProperty="storagename"/>
				</html:select>
			</td>
		</tr>
	</table>
	<br>
  <table:table id="guiContractTransferAuditList" name="contractTransferManagerAuditList">
    <logic:iterate id="element" name="contractTransferManagerAuditList">
      <table:define id="c_cb">
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${element.billNo}" />
        <input type="hidden" name="auditStatus" value="${element.auditStatus}"/>
      </table:define>
      <table:define id="c_BillNo">
        <a href="<html:rewrite page='/ContractTransferManagerAuditAction.do'/>?method=toPrepareAddRecord&id=<bean:write name="element"  property="billNo"/>&auditStatus2=${element.auditStatus2}">
          <bean:write name="element" property="billNo" />
        </a>
      </table:define>
      <table:define id="c_CompanyName">
        <bean:write name="element" property="companyId" />
      </table:define>
      <table:define id="c_MaintContractNo">
        <bean:write name="element" property="maintContractNo" />
      </table:define>
      <table:define id="c_SalesContractNo">
        <bean:write name="element" property="salesContractNo" />
      </table:define>
      <table:define id="c_ElevatorNo">
        <bean:write name="element" property="elevatorNo" />
      </table:define>
      <table:define id="c_MaintDivision">
        <bean:write name="element" property="maintDivision" />
      </table:define>
      <table:define id="c_MaintStation">
        <bean:write name="element" property="maintStation" />
      </table:define>
      <table:define id="c_ContractSDate">
        <bean:write name="element" property="contractSdate" />
      </table:define>
      <table:define id="c_ContractEDate">
        <bean:write name="element" property="contractEdate" />
      </table:define>
      <table:define id="c_auditStatus">
        <logic:equal name="element" property="auditStatus2" value="Y">已审核
        <input type="hidden" name="auditStatus2" value="Y" /></logic:equal>
        <logic:notEqual name="element" property="auditStatus2" value="Y">未审核
        <input type="hidden" name="auditStatus2" value="N" /></logic:notEqual>
      </table:define>
      <table:define id="c_IsCheck">
      	<logic:notEqual name="element" property="billNo2" value="">
      	<a href="javascript:void(0);" onclick="printMethod('${element.billNo2}')">查看</a>
      	</logic:notEqual>
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>

<script>
function printMethod(billno){
	window.open('<html:rewrite page="/elevatorTransferCaseRegisterManageAction.do"/>?id='+billno+'&method=toPreparePrintRecord');
}

</script>
