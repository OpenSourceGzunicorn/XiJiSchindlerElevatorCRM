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
        <bean:message key="elevatorSale.elevatorNo" />
        :
      </td>
      <td>
        <html:text property="property(elevatorNo)" styleClass="default_input" />
      </td>
      <td>
        &nbsp;&nbsp;
        <bean:message key="elevatorSale.salesContractNo" />
        :
      </td>
      <td>
        <html:text property="property(salesContractNo)" styleClass="default_input" />
      </td>
      <td>
        &nbsp;&nbsp;
        <bean:message key="elevatorSale.inspectDate" />
        : 
        <html:text property="property(inspectDates)" size="15" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/>
      </td>  
      <td>
        &nbsp;µ½&nbsp;
        <html:text property="property(inspectDatee)" size="15" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/>
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
        <bean:message key="elevatorSale.enabledFlag" />
        :
      </td>
      <td>
        <html:select property="property(enabledFlag)">
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
      <html:hidden property="property(genReport)" styleId="genReport" />
    </tr>
  </table>
  <br>
  <table:table id="guiElevatorSale" name="outsideFootballElevatorList">
    <logic:iterate id="element" name="outsideFootballElevatorList">
      <table:define id="c_cb">
        <bean:define id="elevatorNo" name="element" property="elevatorNo" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=elevatorNo.toString()%>" />
      </table:define>
      <table:define id="c_elevatorNo">
        <a href="<html:rewrite page='/outsideFootballElevatorAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="elevatorNo"/>">
        <bean:write name="element" property="elevatorNo" />
        </a>
      </table:define>
      <table:define id="c_salesContractNo">
        <bean:write name="element" property="salesContractNo" />
      </table:define>
      <table:define id="c_salesContractName">
        <bean:write name="element" property="salesContractName" />
      </table:define>
      <table:define id="c_inspectDate">
        <bean:write name="element" property="inspectDate" />
      </table:define>
      <table:define id="c_weight">
        <bean:write name="element" property="weight" />
      </table:define>
      <table:define id="c_speed">
        <bean:write name="element" property="speed" />
      </table:define>
      <table:define id="c_seriesName">
        <bean:write name="element" property="seriesName" />
      </table:define>
      <table:define id="c_elevatorType">
      	<logic:match name="element" property="elevatorType" value="T">Ö±ÌÝ </logic:match>
        <logic:match name="element" property="elevatorType" value="F">·öÌÝ</logic:match>
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
      <table:define id="c_elevatorParam">
        <bean:write name="element" property="elevatorParam" />
      </table:define>
      <table:define id="c_enabledFlag">
        <logic:match name="element" property="enabledFlag" value="Y">
          <bean:message key="pageword.yes" />
        </logic:match>
        <logic:match name="element" property="enabledFlag" value="N">
          <bean:message key="pageword.no" />
        </logic:match>
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>