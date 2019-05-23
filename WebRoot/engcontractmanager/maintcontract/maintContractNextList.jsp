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
<html:hidden property="property(genReport)" styleId="genReport" />
  <table>
    <tr>
      <td>       
        <bean:message key="maintContractQuote.billNo" />
        :
      </td>
      <td>
        <html:text property="property(billNo)" styleClass="default_input" />
      </td>                   
      <td> 
        &nbsp;&nbsp;      
        <bean:message key="maintContractQuote.companyName" />
        :
      </td>
      <td>
        <html:text property="property(companyName)" size="35" styleClass="default_input" />
      </td>
      <td> 
        &nbsp;&nbsp;        
        <bean:message key="maintContractQuote.maintDivision" />
        :
      </td>
      <td>
        <html:select property="property(maintDivision)">
		  <html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
        </html:select>
      </td>        
    </tr>
    
    <tr>             
      <td>
      	销售合同号
        :
      </td>
      <td>
        <html:text property="property(salesContractNo)" styleClass="default_input" />
      </td>         
      <td>  &nbsp;&nbsp;  
       	 电梯编号
        :
      </td>
      <td>
        <html:text property="property(elevatorNo)" styleClass="default_input" />
      </td> 
      </tr>
  </table>
  <br>
  <table:table id="guiMaintContractNext" name="maintContractQuoteNextList">
    <logic:iterate id="element" name="maintContractQuoteNextList">
      <table:define id="c_cb">
        <bean:define id="billNo" name="element" property="billNo" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${billNo}" />
      </table:define>
      <table:define id="c_billNo">
          <bean:write name="element" property="billNo" />
      </table:define>
      <table:define id="c_attnName">
        <bean:write name="element" property="attnName" />
      </table:define>
      <table:define id="c_applyDate">
        <bean:write name="element" property="applyDate" />
      </table:define>
      <table:define id="c_companyName">
        <bean:write name="element" property="companyName" />
      </table:define>
      <table:define id="c_maintDivisionName">
        <bean:write name="element" property="maintDivisionName" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>