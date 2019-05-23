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
        收票流水号
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
        提交标志
        :
      </td>
      <td>
        <html:select property="property(submitType)">
          <html:option value="">请选择</html:option>
		  <html:option value="Y">已提交</html:option>
		  <html:option value="N">未提交</html:option>
        </html:select>
        </td>
      <td> 
        &nbsp;&nbsp;
        审核状态
        :
      </td>
      <td>
        <html:select property="property(auditStatus)">
          <html:option value="">全部</html:option>
		  <html:option value="Y">已审核</html:option>
		  <html:option value="N">未审核</html:option>
		  <html:option value="X">不通过</html:option>
        </html:select>
      </td>
    </tr>
    <tr>
      <td>
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
  <table:table id="guiInvoiceManage" name="invoiceManageList">
    <logic:iterate id="element" name="invoiceManageList">
      <table:define id="c_cb">
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${element.jnlNo}" />
        <html:hidden name="element" property="jnlNo" />     
        <html:hidden name="element" property="submitType" />
      </table:define>
      <table:define id="c_jnlNo">
        <a href="<html:rewrite page='/invoiceManageAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="jnlNo"/>">
          <bean:write name="element" property="jnlNo" />
        </a>
      </table:define>
	   <table:define id="c_entrustContractNo">
	    <bean:write name="element" property="entrustContractNo" />
	  </table:define>	 
	 <table:define id="c_companyId">
	    <bean:write name="element" property="companyId" />
	  </table:define>
	  <table:define id="c_invoiceNo">
	  <bean:write name="element" property="invoiceNo" />
	  </table:define>
	  <table:define id="c_invoiceDate">
	  <bean:write name="element" property="invoiceDate" />
	  </table:define>
	  <table:define id="c_invoiceType">
	  <bean:write name="element" property="invoiceType" />
	  </table:define>
	  <table:define id="c_invoiceMoney">
	  <bean:write name="element" property="invoiceMoney" />
	  </table:define>	
      <table:define id="c_maintDivision">
      	<bean:write name="element" property="maintDivision" />
      </table:define>     
      <table:define id="c_rem">
      	<bean:write name="element" property="rem" />
      </table:define>   
      <table:define id="c_submitType">
        <logic:match name="element" property="submitType" value="Y">已提交</logic:match>
        <logic:match name="element" property="submitType" value="N">未提交</logic:match>
        <logic:match name="element" property="submitType" value="R">驳回</logic:match>
      </table:define>
      <table:define id="c_auditStatus">
        <logic:match name="element" property="auditStatus" value="Y">已审核</logic:match>
        <logic:match name="element" property="auditStatus" value="N">未审核</logic:match>
        <html:hidden name="element" property="auditStatus"/>
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>