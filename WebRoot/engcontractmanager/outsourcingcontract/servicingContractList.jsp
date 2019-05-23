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
        <html:text property="property(billno)" styleClass="default_input" />
      </td> 
        <td>      
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;经办人:
      </td>
      <td>
        <html:text property="property(attn)"  styleClass="default_input" />
      </td>
      <td>      
        &nbsp;&nbsp;乙方单位名称:
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
      &nbsp;&nbsp;委托合同号:
      </td>
      <td>
        <html:text property="property(outContractNo)" styleClass="default_input" />
      </td> 
                       
      <td>       
        &nbsp;&nbsp;<bean:message key="maintContract.maintDivision" />:
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
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;审核状态:
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
  <table:table id="guiOutsourcingContract" name="outsourcingContractList">
    <logic:iterate id="element" name="outsourcingContractList">
      <table:define id="c_cb">
        <html:hidden name="element" property="billno"/>
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${element.billno}" />
        <html:hidden name="element" property="submitType"/>
        <html:hidden name="element" property="auditStatus"/>
      </table:define>
      <table:define id="c_billNo">
        <a href="<html:rewrite page='/outsourcingContractAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billno"/>">
          <bean:write name="element" property="billno" />
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
      <table:define id="c_maintDivision">
        <bean:write name="element" property="maintDivision" />
      </table:define>
      <table:define id="c_companyId">
        <bean:write name="element" property="companyId" />
      </table:define>
      
      <table:define id="c_outContractNo">
        <bean:write name="element" property="outContractNo" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>