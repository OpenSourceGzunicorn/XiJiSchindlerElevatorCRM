<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>
<html:form action="/ServeTable.do">
  <table>
    <tr>
      <td>
        <bean:message key="customer.companyId" />
        :
      </td>
      <td>
        <html:text property="property(companyId)" styleClass="default_input" />
      </td>
      <td>
        &nbsp;&nbsp;
        <bean:message key="customer.companyName" />
        :
      </td>
      <td>
        <html:text property="property(companyName)" size="40" styleClass="default_input" />
      </td>
    </tr>
  </table>
  <br>
  <table:table id="guiSearchCustomer" name="customerList">
    <logic:iterate id="element" name="customerList">
      <table:define id="c_cb">
        <bean:define name="element" property="companyId" id="companyId"/>
        <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=companyId.toString()%>" />
        <html:hidden name="element" property="companyId" styleId="companyId"/>
		<html:hidden name="element" property="companyName" styleId="companyName"/>
		<html:hidden name="element" property="contacts" styleId="contacts"/>
        <html:hidden name="element" property="contactPhone" styleId="contactPhone"/>
        <html:hidden name="element" property="address" styleId="address"/>
        <html:hidden name="element" property="legalPerson" styleId="legalPerson"/>
        <html:hidden name="element" property="client" styleId="client"/>
        <html:hidden name="element" property="fax" styleId="fax"/>
        <html:hidden name="element" property="postCode" styleId="postCode"/>
        <html:hidden name="element" property="accountHolder" styleId="accountHolder"/>
        <html:hidden name="element" property="account" styleId="account"/>
        <html:hidden name="element" property="bank" styleId="bank"/>
        <html:hidden name="element" property="taxId" styleId="taxId"/>       
      </table:define>
      <table:define id="c_companyId">
        <bean:write name="element" property="companyId" />
      </table:define>
      <table:define id="c_companyName">
        <bean:write name="element" property="companyName" />
      </table:define>
      <table:define id="c_contacts">
        <bean:write name="element" property="contacts" />
      </table:define>
      <table:define id="c_contactPhone">
        <bean:write name="element" property="contactPhone" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>