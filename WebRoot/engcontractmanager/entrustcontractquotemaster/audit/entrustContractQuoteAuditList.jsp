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
<input type="hidden" name="flowname" id="flowname" value="${flowname}"/>
<a href="" id="avf" target="_blank"></a>
  <table>
    <tr>            
      <td> 
      &nbsp;&nbsp; 
        <bean:message key="maintContractQuote.billNo" />
        :
      </td>
      <td>
        <html:text property="property(billNo)" styleClass="default_input" />
      </td>    
      <td> 
        &nbsp;&nbsp;      
        委托单位
        :
      </td>
      <td>
        <html:text property="property(companyName)" size="35" styleClass="default_input" />
      </td>
      <td> 
        &nbsp;&nbsp;      
        维保合同号
        :
      </td>
      <td>
        <html:text property="property(maintContractNo)" styleClass="default_input" />
      </td>
     </tr>
     <tr> 
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
      <td>
      &nbsp;&nbsp;                 
        <bean:message key="maintContractQuote.status" />
        :
      </td>
      <td>
        <html:select property="property(status)">
          <html:option value="">全部</html:option>
		  <html:options collection="processStatusList" property="typeid" labelProperty="typename"/>
        </html:select>
      </td>     
    </tr>
    <%-- <tr>                         
      <td>  
        &nbsp;&nbsp;      
        <bean:message key="maintContractQuote.submitType" />
        :
      </td>
      <td>
        <html:select property="property(submitType)">
          <html:option value="">全部</html:option>
	      <html:option value="Y">已提交</html:option>
	      <html:option value="N">未提交</html:option>
        </html:select>
      </td>              
    </tr> --%>
  </table>
  <br>
  
  <table:table id="guiEntrustContractQuote" name="entrustContractQuoteAuditList">
    <logic:iterate id="element" name="entrustContractQuoteAuditList">
      <table:define id="c_cb">
        <bean:define id="billNo" name="element" property="billNo" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${billNo}" />
        <input type="hidden" name="submitType" id="submitType" value="${element.submitType}"/>
        <input type="hidden" name="hstatus" id="hstatus" value="${element.status}"/>
        <input type="hidden" name="tokenid" id="tokenid" value="${element.tokenId}"/>      
        <input type="hidden" name="iscontract" id="iscontract" value="${element.r2}"/> 
      </table:define>
      <table:define id="c_billNo">
        <a href="<html:rewrite page='/entrustContractQuoteAuditAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billNo"/>">
          <bean:write name="element" property="billNo" />
        </a>
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
     <%--  <table:define id="c_standardPrice">
        <bean:write name="element" property="standardPrice" />
      </table:define>
      <table:define id="c_realPrice">
        <bean:write name="element" property="realPrice" />
      </table:define>
      <table:define id="c_markups">
        <bean:write name="element" property="markups" />
      </table:define> --%>
      <table:define id="c_processName">
        <bean:write name="element" property="processName" />
      </table:define>
      <table:define id="c_status">
        <bean:write name="element" property="r1" />
      </table:define>
      <table:define id="c_submitType">
        <logic:match name="element" property="submitType" value="Y">已提交</logic:match>
        <logic:match name="element" property="submitType" value="N">未提交</logic:match>
      </table:define>
      <table:define id="c_iscontract">
        <bean:write name="element" property="r2" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>