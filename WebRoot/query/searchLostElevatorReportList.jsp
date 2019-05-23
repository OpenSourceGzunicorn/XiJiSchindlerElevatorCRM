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
<html:hidden property="property(contactPhone)"/>
  <table>
    <tr>
      <td>
        维保合同号
        :
      </td>
      <td>
        <html:text property="property(MaintContractNo)" styleClass="default_input" />
      </td>
       <td> 
       &nbsp;&nbsp;     
        销售合同号
        :
      </td>
      <td>
        <html:text property="property(salesContractNo)" styleClass="default_input" />
      </td>
      <td>
      &nbsp;&nbsp;
      	丢梯日期范围
      	:
      </td>
      		<td>
		   		<html:text property="property(sdate1)" styleClass="Wdate"  size="12" styleId="sdate1"  onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
				- 
				<html:text property="property(edate1)" styleClass="Wdate"  size="12" styleId="edate1" onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
			 </td>
    </tr>
  </table>
  <br>
  <table:table id="guiSearchLostElevatorReport" name="searchLostElevatorReportList">
    <logic:iterate id="element" name="searchLostElevatorReportList">
      <table:define id="c_cb">
        <bean:define name="element" property="jnlno" id="jnlno"/>
        <html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=jnlno.toString()%>" />
        
        <html:hidden name="element" property="jnlno" styleId="jnlno"/>
        <html:hidden name="element" property="numno" styleId="numno"/>
		<html:hidden name="element" property="billNo" styleId="wb_billno"/>
		<html:hidden name="element" property="maintContractNo" styleId="maintContractNo"/>
		<html:hidden name="element" property="lostElevatorDate" styleId="lostElevatorDate"/>
        <html:hidden name="element" property="causeAnalysis" styleId="causeAnalysis"/>
        <html:hidden name="element" property="pullname" styleId="pullname"/>   
      </table:define>
      <table:define id="c_maintContractNo">
        <bean:write name="element" property="maintContractNo" />
      </table:define>
      <table:define id="c_lostElevatorDate">
        <bean:write name="element" property="lostElevatorDate" />
      </table:define>
      <table:define id="c_causeAnalysis">
        <bean:write name="element" property="pullname" />
      </table:define>
      
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>