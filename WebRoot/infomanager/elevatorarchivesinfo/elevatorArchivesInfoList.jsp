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
      	&nbsp;&nbsp;
		<bean:message key="elevatorArchivesInfo.elevatorNo"/>
        :
      </td>
      <td>
        <html:text property="property(elevatorNo)" styleClass="default_input" />
      </td>
      <td>
      	&nbsp;&nbsp;
		<bean:message key="elevatorArchivesInfo.elevatorType"/>
        :
      </td>
      <td>
        <html:select property="property(elevatorType)">
        	<html:option value=""> <bean:message key="XJSCRM.zhz" /></html:option>
			<html:options collection="elevatorTypeList" property="id.pullid" labelProperty="pullname"/>
        </html:select>
      </td>
      <td>
      	&nbsp;&nbsp;
		<bean:message key="elevatorArchivesInfo.salesContractNo"/>
        :
      </td>
      <td>
        <html:text property="property(salesContractNo)" styleClass="default_input" />
      </td>
      <td>
        &nbsp;&nbsp;
		<bean:message key="elevatorArchivesInfo.maintContractNo"/>
        :
      </td>
      <td>
        <html:text property="property(maintContractNo)" styleClass="default_input" />
      </td>
      </tr>
      <tr> 
      <td>
        &nbsp;&nbsp;
		¹ºÂòµ¥Î»Ãû³Æ
        :
      </td>
      <td>
        <html:text property="property(projectName)" styleClass="default_input"/>
      </td>
      <td>
        &nbsp;&nbsp;
		<bean:message key="elevatorArchivesInfo.maintDivision"/>
        :
      </td>
       <td>
        <html:select property="property(maintDivision)">
			<html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
        </html:select>
      </td>
        <html:hidden property="property(genReport)" styleId="genReport" />
    </tr>
  </table>
  <br>
  
  <table:table id="guiElevatorArchivesInfo" name="elevatorArchivesInfoList">
    <logic:iterate id="element" name="elevatorArchivesInfoList">
      <table:define id="c_cb">
        <bean:define id="numno" name="element" property="numno" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=numno.toString()%>" />
      </table:define>
      <table:define id="c_elevatorNo">
        <a href="<html:rewrite page='/elevatorArchivesInfoAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="numno"/>">
        <bean:write name="element" property="elevatorNo" />
        </a>
      </table:define>
      <table:define id="c_elevatorType">
        <bean:write name="element" property="elevatorType" />
      </table:define>
      <table:define id="c_salesContractNo">
        <bean:write name="element" property="salesContractNo" />
      </table:define>
      <table:define id="c_maintContractNo">
        <bean:write name="element" property="maintContractNo" />
      </table:define>
      <table:define id="c_projectName">
        <bean:write name="element" property="projectName" />
      </table:define>
      <table:define id="c_projectAddress">
        <bean:write name="element" property="projectAddress" />
      </table:define>
      <table:define id="c_maintDivision">
        <bean:write name="element" property="maintDivision" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>