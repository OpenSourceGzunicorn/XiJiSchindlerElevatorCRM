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
        委托合同号
        :
      </td>
      <td>
        <html:text property="property(contractNo)" styleClass="default_input" />
      </td>
       <td>  
        &nbsp;&nbsp;     
        委托单位
        :
      </td>
      <td>
        <html:text property="property(companyName)" styleClass="default_input" size="50" />
      </td> 
      <td>  
        &nbsp;&nbsp;
        合同金额
        :
      </td>
      <td>
        <html:text property="property(contractTotal)" styleClass="default_input"/>
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
      
      <td> 
        &nbsp;&nbsp;
        所属分部
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
  <table:table id="guiContractPaymentEnquiry" name="contractPaymentEnquiryList">
    <logic:iterate id="element" name="contractPaymentEnquiryList">
      <table:define id="c_cb">
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${element.entrustContractNo}" />
        <html:hidden name="element" property="entrustContractNo" />     
      </table:define>
      <table:define id="c_entrustContractNo">
       <a href="<html:rewrite page='/contractPaymentEnquiryAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="entrustContractNo"/>">
          <bean:write name="element" property="entrustContractNo" />
       </a>
      </table:define>
      <table:define id="c_companyName">
        <bean:write name="element" property="companyName" />
      </table:define>
      <table:define id="c_contractTotal">
        <bean:write name="element" property="contractTotal" />
      </table:define>     
      <table:define id="c_accountsPayable">
        <bean:write name="element" property="accountsPayable" />
      </table:define>
      <table:define id="c_invoice">
        <bean:write name="element" property="invoice" />
      </table:define>
      <table:define id="c_payment">
        <bean:write name="element" property="payment" />
      </table:define>	
      <table:define id="c_debitMoney">
        <bean:write name="element" property="debitMoney" />
      </table:define>
      <table:define id="c_noPayment">
        <bean:write name="element" property="noPayment" />
      </table:define>
      <table:define id="c_contractnoPayment">
         <bean:write name="element" property="contractnoPayment" /> 
      </table:define>
      <table:define id="c_maintDivision">
      	<bean:write name="element" property="maintDivision" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
<table>
  	<tr>
  		<td>&nbsp;&nbsp;合同总应付款总额：</td><td><bean:write name="accountsPayable"/></td>
  		<td>&nbsp;&nbsp;合同总收票总额：</td><td><bean:write name="invoice"/></td>
  		<td>&nbsp;&nbsp;合同总付款总额：</td><td><bean:write name="payment"/></td>
  		<td>&nbsp;&nbsp;合同总收票欠款总额：</td><td><bean:write name="noPayment"/></td>
  		<td>&nbsp;&nbsp;合同总欠款总额：</td><td><bean:write name="contractnoPayment"/></td>
  	</tr>
  </table>
</html:form>