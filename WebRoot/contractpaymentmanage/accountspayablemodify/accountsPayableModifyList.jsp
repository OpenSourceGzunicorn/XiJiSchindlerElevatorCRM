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
        应付款流水号
        :
      </td>
      <td>
        <html:text property="property(jnlNo)" styleClass="default_input" />
      </td>                 
      <td>  
        &nbsp;&nbsp;     
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
  <table:table id="guiAccountsPayableModify" name="accountsPayableModifyList">
    <logic:iterate id="element" name="accountsPayableModifyList">
      <table:define id="c_cb">
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${element.jnlNo}" />
      </table:define>
      <table:define id="c_jnlNo">
	       <bean:write name="element" property="jnlNo" />
      </table:define>
      <table:define id="c_entrustContractNo">
        <bean:write name="element" property="entrustContractNo" />
      </table:define>
      <table:define id="c_companyId">
        <bean:write name="element" property="companyId" />
      </table:define>     
      <table:define id="c_recName">
        <bean:write name="element" property="recName" />
      </table:define>
      <table:define id="c_preDate">
        <bean:write name="element" property="preDate" />
      </table:define>
      <table:define id="c_preMoney">
        <bean:write name="element" property="preMoney" />
      </table:define>
      <table:define id="c_maintDivision">
      	<bean:write name="element" property="maintDivision" />
      </table:define>
      <table:define id="c_auditStatus">
        <logic:match name="element" property="auditStatus" value="Y">已审核</logic:match>
        <logic:match name="element" property="auditStatus" value="N">未审核</logic:match>
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>