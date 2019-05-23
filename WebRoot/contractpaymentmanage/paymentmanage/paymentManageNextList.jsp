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
	    <td>
			收票流水号:
		</td>
		<td>
			<html:text property="property(jnlNo)" styleClass="default_input" />
		</td>
		<td>
		&nbsp;&nbsp;合同号:
		</td>
		<td>
			<html:text property="property(entrustContractNo)" styleClass="default_input" />
		</td>
		 <td>      
        &nbsp;&nbsp;销售合同号:
      </td>
      <td>
        <html:text property="property(salesContractNo)" styleClass="default_input" />
      </td>
    	<td> 
        &nbsp;&nbsp;所属分部:
      </td>
      <td>
        <html:select property="property(maintDivision)" >
          <html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
        </html:select>
      </td> 
    </tr>
  </table>
  <br>
  <table:table id="guiPaymentManageNext" name="paymentManageNextList">
    <logic:iterate id="element" name="paymentManageNextList">
     <table:define id="c_cb">
		<bean:define id="jnlNo" name="element" property="jnlNo" />
			<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=jnlNo.toString()%>" />
		</table:define>
		<table:define id="c_jnlNo">
			<bean:write name="element" property="jnlNo" />
		</table:define>
		<table:define id="c_entrustContractNo">
			<bean:write name="element" property="entrustContractNo" />
		</table:define>
		<table:define id="c_invoiceMoney">
			<bean:write name="element" property="invoiceMoney" />
		</table:define>
		<table:define id="c_maintDivision">
			<bean:write name="element" property="maintDivision" />
		</table:define>
		<table:define id="c_companyId">
			<bean:write name="element" property="companyID" />
		</table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>