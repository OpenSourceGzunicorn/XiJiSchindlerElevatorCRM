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
       应收款流水号
        :
      </td>
      <td>
        <html:text property="property(ARF_JnlNo)" styleClass="default_input" />
      </td>
      <td>  
        &nbsp;&nbsp;     
        合同号
        :
      </td>
      <td>
        <html:text property="property(contractNo)" styleClass="default_input" />
      </td>
      <td>
        &nbsp;&nbsp;               
        合同类型
        :
      </td>
      <td>
        <html:select property="property(contractType)" styleClass="default_input" >
          <html:option value="">全部</html:option>
          <html:option value="B">保养</html:option>
		  <html:option value="W">维修</html:option>
		  <html:option value="G">改造</html:option>
        </html:select>
      </td>
    </tr>
    <tr>
      <td>
        &nbsp;&nbsp;               
        凭证号
        :
      </td>
      <td>
        <html:text property="property(paragraphNo)" styleClass="default_input" />
      </td> 
      <td>  
        &nbsp;&nbsp;     
        来款金额
        :
      </td>
      <td>
        <html:text property="property(paragraphMoney)" styleClass="default_input" />
      </td>
      <td>
      &nbsp;&nbsp;     
        来款日期
        :
      </td>
      <td><html:text property="property(paragraphDate)" size="12" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})" /></td>
    </tr>
    <tr>
      <td> 
        &nbsp;&nbsp;
        提交标志
        :
      </td>
      <td>
        <html:select property="property(submitType)" styleClass="default_input" >
          <html:option value="">全部</html:option>
		  <html:option value="Y">已提交</html:option>
		  <html:option value="N">未提交</html:option>
		  <html:option value="R">驳回</html:option>
        </html:select>
      </td> 
      <td> 
        &nbsp;&nbsp;
        审核状态
        :
      </td>
      <td>
        <html:select property="property(auditStatus)" styleClass="default_input" >
          <html:option value="">全部</html:option>
		  <html:option value="Y">已审核</html:option>
		  <html:option value="N">未审核</html:option>
		  
        </html:select>
      </td>      
    	<td> 
        &nbsp;&nbsp;
        所属分部
        :
      </td>
      <td>
        <html:select property="property(maintDivision)" styleClass="default_input" >
          <html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
        </html:select>
      </td> 
    </tr>
  </table>
  <br>
  <table:table id="guiContractParagraphManage" name="contractParagraphManageList">
    <logic:iterate id="element" name="contractParagraphManageList">
      <table:define id="c_cb">
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${element.jnlNo}" />
        <html:hidden name="element" property="jnlNo" />     
        <html:hidden name="element" property="submitType" />
        <html:hidden name="element" property="auditStatus" />
      </table:define>
      <table:define id="c_ARF_JnlNo">
      	<a href="<html:rewrite page='/contractParagraphManageAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="jnlNo"/>">
      		<bean:write name="element" property="ARF_JnlNo" />
      	</a>
      </table:define>
      <table:define id="c_preDate">
        <bean:write name="element" property="preDate" />
      </table:define>
       <table:define id="c_contractNo">
        <bean:write name="element" property="contractNo" />
      </table:define>
      <table:define id="c_contractType">
        <logic:match name="element" property="contractType" value="B">保养</logic:match>
        <logic:match name="element" property="contractType" value="W">维修</logic:match>
        <logic:match name="element" property="contractType" value="G">改造</logic:match>
      </table:define>
      <table:define id="c_paragraphNo">
        <bean:write name="element" property="paragraphNo" />
      </table:define>
      <table:define id="c_paragraphMoney">
        <bean:write name="element" property="paragraphMoney" />
      </table:define>
      <table:define id="c_paragraphDate">
        <bean:write name="element" property="paragraphDate" />
      </table:define>
      <table:define id="c_rem">
        <bean:write name="element" property="rem" />
      </table:define>
      <table:define id="c_maintDivision">
        <bean:write name="element" property="maintDivision" />
      </table:define>
      <table:define id="c_submitType">
        <logic:match name="element" property="submitType" value="Y">已提交</logic:match>
        <logic:match name="element" property="submitType" value="N">未提交</logic:match>
        <logic:match name="element" property="submitType" value="R">驳回</logic:match>
      </table:define>
      <table:define id="c_auditStatus">
        <logic:match name="element" property="auditStatus" value="Y">已审核</logic:match>
        <logic:match name="element" property="auditStatus" value="N">未审核</logic:match>
        
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>