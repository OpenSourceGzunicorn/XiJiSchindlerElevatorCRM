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
    </tr>
  </table>
  <br>
  <table:table id="guiSearchMaintDivisionBX" name="searchMaintDivisionBXList">
    <logic:iterate id="element" name="searchMaintDivisionBXList">
      <table:define id="c_cb">
        <bean:define name="element" property="comId" id="comId"/>
        <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=comId.toString()%>" />
        <html:hidden name="element" property="comId" styleId="maintDivisionBx"/>
		<html:hidden name="element" property="comName" styleId="comNameMsr"/>
		<html:hidden name="element" property="storageId" styleId="maintStationBx"/>
		<html:hidden name="element" property="storageName" styleId="storageNameMsr"/>
       </table:define> 
      <table:define id="c_comId">
        <bean:write name="element" property="comName" />
      </table:define>
      <table:define id="c_storageId">
        <bean:write name="element" property="storageName" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>