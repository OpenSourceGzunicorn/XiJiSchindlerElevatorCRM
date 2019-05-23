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
      <td>  
        &nbsp;&nbsp;      
        <bean:message key="maintContractQuote.submitType" />
        :
      </td>
      <td>
        <html:select property="property(submitType)">
          <html:option value="%">全部</html:option>
	      <html:option value="Y">已提交</html:option>
	      <html:option value="N">未提交</html:option>
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
      <td>&nbsp;&nbsp;          
        <bean:message key="maintContractQuote.status" />
        :
      </td>
      <td>
        <html:select property="property(status)">
          <html:option value="">全部</html:option>
		  <html:options collection="processStatusList" property="typeid" labelProperty="typename"/>
        </html:select>
      </td>              
      <td>  
        &nbsp;&nbsp;      
                        生成合同
        :
      </td>
      <td>
        <html:select property="property(iscont)">
          <html:option value="">全部</html:option>
	      <html:option value="Y">是</html:option>
	      <html:option value="N">否</html:option>
        </html:select>
      </td>     
    </tr>
  </table>
  <br>
  <table:table id="guiMaintContractQuote" name="maintContractQuoteList">
    <logic:iterate id="element" name="maintContractQuoteList">
      <table:define id="c_cb">
        <bean:define id="billNo" name="element" property="billNo" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${billNo}" />
        <input type="hidden" name="submitType" id="submitType" value="${element.submitType}"/>
        <input type="hidden" name="iscontract" id="iscontract" value="${element.iscontract}"/> 
        <input type="hidden" name="hstatus" id="hstatus" value="${element.status}"/>
      </table:define>
      <table:define id="c_billNo">
        <a href="<html:rewrite page='/maintContractQuoteAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billNo"/>">
          <bean:write name="element" property="billNo" />
        </a>
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
      <table:define id="c_statusName">
        <bean:write name="element" property="statusName" />
      </table:define>
      <table:define id="c_processName">
        <bean:write name="element" property="processName" />
      </table:define>
      <table:define id="c_submitType">
        <logic:match name="element" property="submitType" value="Y">已提交</logic:match>
        <logic:match name="element" property="submitType" value="N">未提交</logic:match>
      </table:define>
      <table:define id="c_maintDivisionName">
        <bean:write name="element" property="maintDivisionName" />
      </table:define>
      <table:define id="c_storagename">
        <bean:write name="element" property="storagename" />
      </table:define>
      <table:define id="c_iscontract">
        <bean:write name="element" property="iscontract" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>