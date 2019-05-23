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
    维改流水号:</td>
       <td>
        <html:text property="property(billNo)"  styleClass="default_input" />
      </td> 
      <td> 
        &nbsp;&nbsp;      
        维改合同号
        :
      </td>
      <td>
        <html:text property="property(maintContractNo)" size="35" styleClass="default_input" />
      </td>
      <td> 
        &nbsp;&nbsp;      
        经办人
        :
      </td>
      <td>
        <html:text property="property(attn)" styleClass="default_input" />
      </td>
      </tr>
      <tr>
      <td> 
        &nbsp;&nbsp;        
        业务类型
        :
      </td>
      <td>
        <html:select property="property(busType)">
          <html:option value="">请选择</html:option>
		  <html:option value="W">维修</html:option>
          <html:option value="G">改造</html:option>
        </html:select>
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
  <table:table id="guiOutsourcingContractQuoteNext" name="outsourceContractQuoteNextList">
    <logic:iterate id="element" name="outsourceContractQuoteNextList">
      <table:define id="c_cb">
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${element.wgBillno}" />
        <html:hidden name="element" property="wgBillno"/>
      </table:define>
      <table:define id="c_wgBillno">
          <bean:write name="element" property="wgBillno" />
      </table:define>
      <table:define id="c_maintContractNo">
          <bean:write name="element" property="maintContractNo" />
      </table:define>
      <table:define id="c_attn">
          <bean:write name="element" property="attn" />
      </table:define>
      <table:define id="c_busType">
          <%-- <bean:write name="element" property="busType" /> --%>
          <logic:match name="element" property="busType" value="G">改造</logic:match>
        <logic:match name="element" property="busType" value="W">维修</logic:match>
      </table:define>	
      <table:define id="c_signingDate">
          <bean:write name="element" property="signingDate" />
      </table:define>
      <table:define id="c_maintDivision">
          <bean:write name="element" property="maintDivision" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>