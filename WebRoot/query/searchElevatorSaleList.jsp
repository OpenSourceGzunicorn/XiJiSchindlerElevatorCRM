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
  <table>
    <tr>
      <td>
        <bean:message key="elevatorSale.salesContractNo" />
        :
      </td>
      <td>
        <html:text property="property(salesContractNo)" styleClass="default_input" />
      </td> 
    </tr>
  </table>
  <br>
  <table:table id="guiSearchElevatorSale" name="searchElevatorSaleList">
    <logic:iterate id="element" name="searchElevatorSaleList">
      <table:define id="c_cb">
        <bean:define id="salesContractNo" name="element" property="salesContractNo" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=salesContractNo.toString()%>" />
        <html:hidden name="element" property="salesContractNo" styleId="salesContractNo"/>
		<html:hidden name="element" property="salesContractName" styleId="salesContractName"/>  
		<html:hidden name="element" property="deliveryAddress" styleId="deliveryAddress"/>
      </table:define>
      <table:define id="c_salesContractNo">
        <bean:write name="element" property="salesContractNo" />
      </table:define>
      <table:define id="c_salesContractName">
        <bean:write name="element" property="salesContractName" />
      </table:define>
      <table:define id="c_salesContractType">
        <bean:write name="element" property="salesContractType" />
      </table:define>
      <table:define id="c_deliveryAddress">
        <bean:write name="element" property="deliveryAddress" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>