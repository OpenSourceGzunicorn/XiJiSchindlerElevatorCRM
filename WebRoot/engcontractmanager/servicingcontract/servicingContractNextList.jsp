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
    <td>报价流水号:</td>
       <td>
        <html:text property="property(billNo)"  styleClass="default_input" />
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
  </table>
  <br>
  <table:table id="guiSevycingContractMaster" name="wgchangeContractQuoteList2">
    <logic:iterate id="element" name="wgchangeContractQuoteList2">
      <table:define id="c_cb">
        <bean:define id="billNo" name="element" property="billNo" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${billNo}" />
        <html:hidden name="element" property="companyId"/>
      </table:define>
      <table:define id="c_billNo">
          <bean:write name="element" property="billNo" />
      </table:define>
      <table:define id="c_attn">
        <bean:write name="element" property="attn" />
      </table:define>
      <table:define id="c_applyDate">
        <bean:write name="element" property="applyDate" />
      </table:define>
      <table:define id="c_companyId">
        <bean:write name="element" property="companyId" />
      </table:define>
      <table:define id="c_maintDivision">
        <bean:write name="element" property="maintDivision" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>