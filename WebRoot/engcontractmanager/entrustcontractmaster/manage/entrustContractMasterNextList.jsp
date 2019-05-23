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
      &nbsp;&nbsp; 
        <bean:message key="maintContractQuote.billNo"/>
        :
      </td>
      <td>
        <html:text property="property(billNo)" styleClass="default_input" />
      </td>
      <td> 
      &nbsp;&nbsp; 
        维保合同号
        :
      </td>
      <td>
        <html:text property="property(maintContractNo)" styleClass="default_input" />
      </td>
      <td> 
        &nbsp;&nbsp;      
        委托单位
        :
      </td>
      <td>
        <html:text property="property(companyName)" size="35" styleClass="default_input" />
      </td>
    </tr>
    <tr>   
    
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
        <bean:message key="maintContractQuote.maintDivision" />
        :
      </td>
      <td>
        <html:select property="property(maintDivision)">
		  <html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
        </html:select>
      </td>             
    </tr>
  </table>
  <br>
  <table:table id="guiEntrustContractMasterNext" name="entrustContractMasterNextList">
    <logic:iterate id="element" name="entrustContractMasterNextList">
      <table:define id="c_cb">
        <bean:define id="billNo" name="element" property="billNo" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${billNo}" />
      </table:define>
      <table:define id="c_billNo">
          <bean:write name="element" property="billNo" />
      </table:define>
      <table:define id="c_maintContractNo">
          <bean:write name="element" property="maintContractNo" />
      </table:define>
       <table:define id="c_companyId">
          <bean:write name="element" property="companyId" />
      </table:define>
      <table:define id="c_assLevel">
        <bean:write name="element" property="assLevel" />
      </table:define>
      <table:define id="c_operId">
        <bean:write name="element" property="operId" />
      </table:define>
      <table:define id="c_maintDivision">
          <bean:write name="element" property="maintDivision" />
      </table:define>
     <%-- <table:define id="c_standardPrice">
        <bean:write name="element" property="standardPrice" />
      </table:define>
      <table:define id="c_realPrice">
        <bean:write name="element" property="realPrice" />
      </table:define>
      <table:define id="c_markups">
        <bean:write name="element" property="markups" />
      </table:define> --%>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>