<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>  

<html:form action="/ServeTable.do">
<html:hidden property="property(billnostr)"/>
	<table>
		<tr>
			<td>
				流水号
				:
			</td>
			<td>
				<html:text property="property(billno)" styleClass="default_input" />
			</td>
			<td>
			&nbsp;&nbsp;
				合同号
				:
			</td>
			<td>
				<html:text property="property(maintContractNo)" styleClass="default_input" />
			</td>

		<td>     
       	&nbsp;&nbsp; 销售合同号
        :
      </td>
      <td>
        <html:text property="property(salesContractNo)" styleClass="default_input" />
      </td>
      </tr>
      <tr>
      <td>
				甲方名称
				:
			</td>
			<td colspan="3">
				<html:text property="property(companyId)" styleClass="default_input" size="58"/>
			</td>
      	<td>
      &nbsp;&nbsp;  维保分部
        :
      </td>
      <td>
        <html:select property="property(comid)">
        <html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
        </html:select>
      </td>
	<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
	<br>
	<table:table id="guiMaintContractListMin" name="searchMaintContractMinList">
		<logic:iterate id="element" name="searchMaintContractMinList">
			<table:define id="c_cb">
			<bean:define id="billno" name="element" property="billno" />
				<html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=billno .toString()%>" />
				<html:hidden name="element" property="billno" styleId="hbillno"/>
				<html:hidden name="element" property="maintContractNo" styleId="hmaintContractNo"/>
				<html:hidden name="element" property="contractStatus" styleId="hcontractStatus"/>
			</table:define>
			<table:define id="c_billno">
				<bean:write name="element" property="billno" />
			</table:define>
			<table:define id="c_maintContractNo">
				<bean:write name="element" property="maintContractNo" />
			</table:define>
			<table:define id="c_companyId">
				<bean:write name="element" property="companyName" />
			</table:define>
			<table:define id="c_num">
				<bean:write name="element" property="connum" />
			</table:define>
			<table:define id="c_maintDivision">
				<bean:write name="element" property="comName" />
			</table:define>				
			<table:define id="c_maintStation">
				<bean:write name="element" property="storageName" />
			</table:define>			
			<table:define id="c_contractStatus">
				<bean:write name="element" property="contractStatusName" />
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>