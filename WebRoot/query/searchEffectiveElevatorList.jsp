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
  
        联系人
        :
      </td>
      <td>
        <html:text property="property(contacts)" styleClass="default_input" />
      </td>
      <td>
        &nbsp;&nbsp;
        联系电话
        :
      </td>
      <td>
        <html:text property="property(contactPhone)" styleClass="default_input" />
      </td>
     
      <td>
         &nbsp;&nbsp;
        单位名称
        :
      </td>
      <td>
        <html:text property="property(companyName)" size="30" styleClass="default_input" />
      </td>
    </tr>
  </table>
  <br>
  <table:table id="guiSearchLostCustomer" name="searchEffectiveElevatorList">
    <logic:iterate id="element" name="searchEffectiveElevatorList">
      <table:define id="c_cb">
        <bean:define name="element" property="companyId" id="companyId"/>
        <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=companyId.toString()%>" />
        <html:hidden name="element" property="companyId" styleId="companyId"/>
		<html:hidden name="element" property="contacts" styleId="contacts"/>
        <html:hidden name="element" property="contactPhone" styleId="contactPhone"/>
		<html:hidden name="element" property="companyName" styleId="companyName"/>
      </table:define>
      <table:define id="c_contacts">
        <bean:write name="element" property="contacts" />
      </table:define>
      <table:define id="c_contactPhone">
        <bean:write name="element" property="contactPhone" />
      </table:define>
      <table:define id="c_companyName">
        <bean:write name="element" property="companyName" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table> 
</html:form>