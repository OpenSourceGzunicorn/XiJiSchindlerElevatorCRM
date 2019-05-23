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
<a href="" id="avf" target="_blank"></a>
  <table>
    <tr>
     <td>报价流水号:</td>
       <td>
        <html:text property="property(billNo)"  styleClass="default_input" />
      </td> 
      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;经办人:</td>
       <td>
        <html:text property="property(attn)"  styleClass="default_input" />
      </td> 
      <td> 
        &nbsp;&nbsp;      
        <bean:message key="maintContractQuote.companyName" />
        :
      </td>
      <td>
        <html:text property="property(companyname)" size="35" styleClass="default_input" />
      </td>       
    </tr>
    <tr>                         
      <td> &nbsp; <bean:message key="maintContractQuote.status" />:
      </td>
      <td>
        <html:select property="property(status)">
          <html:option value="%">
            <bean:message key="pageword.all" />
            <html:options collection="processStateList" property="typeid" labelProperty="typename"/>
          </html:option>
        </html:select>
      </td>     
      <td>  
        &nbsp;&nbsp;      
        <bean:message key="maintContractQuote.submitType" />
        :
      </td>
      <td>
        <html:select property="property(submittype)">
          <html:option value="%">
            <bean:message key="pageword.all" />
          </html:option>
          <html:option value="Y">已提交</html:option>
          <html:option value="N">未提交</html:option>
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
    <tr>
      <td >&nbsp;&nbsp;业务类别:</td>
      <td >
      <html:select property="property(busType)">
      <html:option value="%">全部</html:option>
      <html:option value="W">维修</html:option>
      <html:option value="G">改造</html:option>
      </html:select>
      </td>
      </tr>
  </table>
  <br>
  <table:table id="guiContractQuoteMaster" name="wgchangeContractAuQuoteList">
    <logic:iterate id="element" name="wgchangeContractAuQuoteList">
      <table:define id="c_cb">       
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${element.billNo }" />
        <html:hidden name="element" property="submitType"/>
        <input type="hidden" name="hstatus" id="hstatus" value="${element.status }"/>
        <input type="hidden" name="flowname" id="flowname" value="${flowname }"/>
        <input type="hidden" name="tokenid" id="tokenid" value="${element.tokenId }"/>
      </table:define>
      <table:define id="c_billNo">
       <a href="<html:rewrite page='/wgchangeAuditAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billNo"/>">
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
      <table:define id="c_status">
        <bean:write name="element" property="r1" />
      </table:define>
      <table:define id="c_processName">
        <bean:write name="element" property="processName" />
      </table:define>
      <table:define id="c_submitType">
        <bean:write name="element" property="submitType" />
      </table:define>
      <table:define id="c_maintDivision">
        <bean:write name="element" property="maintDivision" />
      </table:define>
      <table:define id="c_bustype">
        <bean:write name="element" property="busType" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>