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
        维保分部
        :
      </td>
      <td>
        <html:select property="property(comid)">
        <html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
        </html:select>
      </td>
      <td>
      &nbsp;&nbsp;
        维保站
        :
      </td>
      <td>
        <html:text property="property(storage)" styleClass="default_input" />
      </td>
      <td>
        &nbsp;&nbsp;
        报销人
        :
      </td>
      <td>
        <html:text property="property(user)" styleClass="default_input" />
      </td>
    </tr>
  </table>
  <br>
  <table:table id="guiReimbursPeopleation" name="searchReimbursPeopleList">
    <logic:iterate id="element" name="searchReimbursPeopleList">
      <table:define id="c_cb">
        <bean:define name="element" property="userId" id="userId"/>
        <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=userId.toString()%>" />
        <html:hidden name="element" property="comName" styleId="comName"/>
		<html:hidden name="element" property="storageName" styleId="storageName"/>
		<html:hidden name="element" property="userId" styleId="userId"/>
		<html:hidden name="element" property="userName" styleId="userName"/>
		<html:hidden name="element" property="comId" styleId="comId"/>
		<html:hidden name="element" property="storageId" styleId="storageId"/>
       </table:define> 
      <table:define id="c_comId">
        <bean:write name="element" property="comName" />
      </table:define>
      <table:define id="c_storageId">
        <bean:write name="element" property="storageName" />
      </table:define>
      <table:define id="c_userId">
        <bean:write name="element" property="userName" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>