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
  <html:hidden property="property(isMaint)" />
  <html:hidden property="property(state)"/>
  <table>
    <tr>
      <td>
        <bean:message key="elevatorSale.elevatorNo" />
        :
      </td>
      <td>
        <html:text property="property(elevatorNo)" styleClass="default_input" />
      </td> 
     <td>
     &nbsp;&nbsp;
        合同编号
        :
      </td>
      <td>
        <html:text property="property(contractNo)"  styleClass="default_input" />
      </td>
      <td>
      &nbsp;&nbsp;
        合同名称
        :
      </td>
      <td>
        <html:text property="property(salesContractName)" size="25" styleClass="default_input" />
      </td>
      </tr>
      <tr>
      <td>
        <bean:message key="elevatorSale.elevatorType" />
        :
      </td>
      <td>
        <html:select property="property(elevatorType)">
          <html:option value="">
            <bean:message key="pageword.all" />
          </html:option>
          <logic:present name="elevatorTypeList">
		    <html:options collection="elevatorTypeList" property="id.pullid" labelProperty="pullname"/>
		  </logic:present>
        </html:select>
      </td>
      <td>
      &nbsp;&nbsp;
        是否外揽
        :
      </td>
      <td>
         <html:select property="property(isOutsideFootball)">
          <html:option value="">
            <bean:message key="pageword.all" />
          </html:option>
          <html:option value="Y">
            <bean:message key="pageword.yes" />
          </html:option>
          <html:option value="N">
            <bean:message key="pageword.no" />
          </html:option>
        </html:select>
      </td> 
    </tr>
  </table>
  <br>
  <table:table id="guiSearchElevator" name="searchElevatorList">
    <logic:iterate id="element" name="searchElevatorList">
      <table:define id="c_cb">
        <bean:define id="elevatorNo" name="element" property="elevatorNo" />
        <html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=elevatorNo.toString()%>" />
        <html:hidden name="element" property="elevatorNo" styleId="elevatorNo"/>
		<logic:match name="element" property="elevatorType" value="T">
		  <input type="hidden" id="elevatorType" value="直梯"/>
		</logic:match>
        <logic:match name="element" property="elevatorType" value="F">
          <input type="hidden" id="elevatorType" value="扶梯"/>
        </logic:match>  
        <html:hidden name="element" property="salesContractNo" styleId="salesContractNo"/>
        <html:hidden name="element" property="salesContractName" styleId="projectName"/>
		<html:hidden name="element" property="floor" styleId="floor"/>
		<html:hidden name="element" property="stage" styleId="stage"/> 
		<html:hidden name="element" property="door" styleId="door"/> 
		<html:hidden name="element" property="high" styleId="high"/> 
		<html:hidden name="element" property="elevatorParam" styleId="elevatorParam"/>
		<html:hidden name="element" property="useUnit" styleId="useUnit"/>
		<html:hidden name="element" property="dealer" styleId="dealer"/>
		<html:hidden name="element" property="deliveryAddress" styleId="projectAddress"/>
		<html:hidden name="element" property="r1" styleId="signWay"/>
		<html:hidden name="element" property="r2" styleId="signWayName"/>
		<html:hidden name="element" property="weight" styleId="weight"/> 
		<html:hidden name="element" property="speed" styleId="speed"/> 
      </table:define>
      <table:define id="c_elevatorNo">
        <bean:write name="element" property="elevatorNo" />
      </table:define>
      <table:define id="c_salesContractNo">
        <bean:write name="element" property="salesContractNo" />
      </table:define>
      <table:define id="c_salesContractName">
        <bean:write name="element" property="salesContractName" />
      </table:define>
      
      
      <table:define id="c_elevatorType">
      	<logic:match name="element" property="elevatorType" value="T">直梯 </logic:match>
        <logic:match name="element" property="elevatorType" value="F">扶梯</logic:match>
      </table:define>
      <table:define id="c_floor">
        <bean:write name="element" property="floor" />
      </table:define>
      <table:define id="c_stage">
        <bean:write name="element" property="stage" />
      </table:define>
      <table:define id="c_door">
        <bean:write name="element" property="door" />
      </table:define>
      <table:define id="c_high">
        <bean:write name="element" property="high" />
      </table:define>
      <table:define id="c_weight">
        <bean:write name="element" property="weight" />
      </table:define>
      <table:define id="c_speed">
        <bean:write name="element" property="speed" />
      </table:define>
      <table:define id="c_elevatorParam">
        <bean:write name="element" property="elevatorParam" />
      </table:define>
      <table:define id="c_isOutsideFootball">
      
      <logic:present name="element" property="isOutsideFootball">
      <logic:match name="element" property="isOutsideFootball" value="Y">是</logic:match>
      <logic:notMatch name="element" property="isOutsideFootball" value="Y">否</logic:notMatch>
      </logic:present>
      <logic:notPresent name="element" property="isOutsideFootball">否</logic:notPresent>
      </table:define>
     
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>