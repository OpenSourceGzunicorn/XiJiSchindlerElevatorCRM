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
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;流水号:
      </td>
      <td>
        <html:text property="property(wgBillno)" styleClass="default_input" />
      </td> 
        <td>      
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;经办人:
      </td>
      <td>
        <html:text property="property(attn)"  styleClass="default_input" />
      </td>
      <td>      
        <bean:message key="maintContract.companyName" />:
      </td>
      <td>
        <html:text property="property(companyname)" size="30" styleClass="default_input" />
      </td>          
    </tr>
    <tr> 
         <td>       
      &nbsp;&nbsp;维改合同号:
      </td>
      <td>
        <html:text property="property(MaintContractNo)" styleClass="default_input" />
      </td>   
      <td> 
     任务下达标志:
      </td>
      <td>
        <html:select property="property(taskSubFlag)">
          <html:option value="%">全部</html:option>
		  <html:option value="Y">已下达</html:option>
		  <html:option value="N">未下达</html:option>
        </html:select>
      </td>                  
      <td>       
        <bean:message key="maintContract.maintDivision" />:
      </td>
      <td>
        <html:select property="property(maintDivision)">
		  <html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
        </html:select>
      </td>      
    </tr>
    <tr>
    <td> 
      &nbsp;&nbsp;&nbsp;&nbsp;提交标志:
      </td>
      <td>
        <html:select property="property(submitType)">
          <html:option value="%">全部</html:option>
		  <html:option value="Y">已提交</html:option>
		  <html:option value="N">未提交</html:option>
		  <html:option value="R">驳回</html:option>
        </html:select>
      </td>
      <td >&nbsp;&nbsp;&nbsp;&nbsp;业务类别:</td>
      <td >
      <html:select property="property(busType)">
      <html:option value="%">全部</html:option>
      <html:option value="W">维修</html:option>
      <html:option value="G">改造</html:option>
      </html:select>
      </td> 
      <td> 
       &nbsp;&nbsp;&nbsp;&nbsp;审核状态:
      </td>
      <td>
        <html:select property="property(auditStatus)">
          <html:option value="%">全部</html:option>
		  <html:option value="Y">已审核</html:option>
		  <html:option value="N">未审核</html:option>
        </html:select>
      </td>
    </tr>
  </table>
  <br>
  <table:table id="guiServicingContract" name="wgchangeContractList">
    <logic:iterate id="element" name="wgchangeContractList">
      <table:define id="c_cb">
        <bean:define id="billNo" name="element" property="billNo" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${element.wgBillno}" />
        <html:hidden name="element" property="submitType"/>
        <html:hidden name="element" property="auditStatus"/>
        <html:hidden name="element" property="taskSubFlag"/>
      </table:define>
      <table:define id="c_billNo">
        <a href="<html:rewrite page='/wgchangeContractAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="wgBillno"/>">
          <bean:write name="element" property="wgBillno" />
        </a>
      </table:define>
      <table:define id="c_maintContractNo">
          <bean:write name="element" property="maintContractNo" />
      </table:define>
      <table:define id="c_attn">
        <bean:write name="element" property="attn" />
      </table:define>
      <table:define id="c_busType">
        <bean:write name="element" property="busType" />
      </table:define>
      <table:define id="c_signingDate">
        <bean:write name="element" property="signingDate" />
      </table:define>
      <table:define id="c_submitType">
        <bean:write name="element" property="submitType" />
      </table:define>
      <table:define id="c_auditStatus">
        <bean:write name="element" property="auditStatus" />
      </table:define>
      <table:define id="c_taskSubFlag">
        <bean:write name="element" property="taskSubFlag" />
      </table:define>
      <table:define id="c_maintDivision">
        <bean:write name="element" property="maintDivision" />
      </table:define>
      <table:define id="c_companyId">
        <bean:write name="element" property="companyId" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>