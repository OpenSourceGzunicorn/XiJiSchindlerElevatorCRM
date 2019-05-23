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
  <html:hidden property="property(salesContractNo)" />
  <html:hidden property="property(elevatorNos)" />
  <html:hidden property="property(mainStation)" />
  
  <table>
    <tr>
      	<td align="right"><bean:message key="loginuser.userid"/>:</td>
      	<td><html:text property="property(userid)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;<bean:message key="loginuser.username"/>:</td>
		<td><html:text property="property(username)" styleClass="default_input"/></td>
      </td> 
    </tr>
  </table>
  <br>
  <table:table id="guiSearchStaff" name="searchStaffList">
    <logic:iterate id="element" name="searchStaffList">
      <table:define id="c_cb">
        <bean:define id="userid" name="element" property="userid" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=userid.toString()%>" />
        <html:hidden name="element" property="userid" styleId="${id}"/>
        <html:hidden name="element" property="username" styleId="username"/>
        <html:hidden name="element" property="phone" styleId="${phone}"/>
      </table:define>
      <table:define id="c_userid">
        <bean:write name="element" property="userid" />
      </table:define>
      <table:define id="c_username">
        <bean:write name="element" property="username" />
      </table:define>
      <table:define id="c_phone">
        <bean:write name="element" property="phone" />
      </table:define>
      <table:define id="c_class1id">
        <bean:write name="element" property="class1id" />
      </table:define>
      <table:define id="c_storageid">
        <bean:write name="element" property="storageid" />
      </table:define>
      <table:define id="c_roleid">
        <bean:write name="element" property="roleid" />
      </table:define>
      <table:define id="c_grcid">
        <bean:write name="element" property="grcid" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>