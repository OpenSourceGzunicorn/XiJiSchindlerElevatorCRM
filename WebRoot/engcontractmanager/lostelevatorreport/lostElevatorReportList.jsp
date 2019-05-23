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
                     流水号
        :
      </td>
      <td>
        <html:text property="property(jnlno)" styleClass="default_input" />
      </td>                 
      <td>
        &nbsp;&nbsp;               
                    维保合同号
        :
      </td>
      <td>
        <html:text property="property(maintContractNo)" styleClass="default_input" />
      </td> 
      <td> 
        &nbsp;&nbsp;      
                    项目名称
        :
      </td>
      <td>
        <html:text property="property(projectName)" size="35" styleClass="default_input" />
      </td>           
    </tr>
    <tr>
      <td>       
                    提交标志
        :
      </td>
      <td>
        <html:select property="property(submitType)">
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
        <html:select property="property(auditStatus)">
          <html:option value="">全部</html:option>
		  <html:option value="Y">已审核</html:option>
		  <html:option value="N">未审核</html:option>
        </html:select>
      </td>    
      <td> 
        &nbsp;&nbsp;      
        <bean:message key="maintContract.maintDivision" />
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
  <table:table id="guiLostElevatorReport" name="lostElevatorReportList">
    <logic:iterate id="element" name="lostElevatorReportList">
      <table:define id="c_cb">
        <bean:define id="jnlno" name="element" property="jnlno" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${jnlno}" />
        <html:hidden name="element" property="submitType" />
        <html:hidden name="element" property="causeAnalysis"/>
        <html:hidden name="element" property="competeCompany"/>
        <html:hidden name="element" property="recoveryPlan"/>
      </table:define>
      <table:define id="c_jnlno">
        <a href="<html:rewrite page='/lostElevatorReportAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="jnlno"/>">
          <bean:write name="element" property="jnlno" />
        </a>
      </table:define>
      <table:define id="c_maintContractNo">
        <bean:write name="element" property="maintContractNo" />
      </table:define>
      <table:define id="c_projectName">
        <bean:write name="element" property="projectName" />
      </table:define>
      <table:define id="c_contractNatureOf">
        <bean:write name="element" property="contractNatureOf" />
      </table:define>
      <table:define id="c_lostElevatorDate">
        <bean:write name="element" property="lostElevatorDate" />
      </table:define>
      <table:define id="c_contacts">
        <bean:write name="element" property="contacts" />
      </table:define>
      <table:define id="c_maintDivision">
        <bean:write name="element" property="maintDivision" />
      </table:define>
      <table:define id="c_maintStation">
        <bean:write name="element" property="maintStation" />
      </table:define>
      <table:define id="c_submitType">
        <logic:equal name="element" property="submitType" value="Y">已提交</logic:equal>
        <logic:equal name="element" property="submitType" value="N">未提交</logic:equal>
        <logic:equal name="element" property="submitType" value="R">驳回</logic:equal>
      </table:define>
      <table:define id="c_auditStatus">
        <logic:equal name="element" property="auditStatus" value="Y">已审核</logic:equal>
        <logic:notEqual name="element" property="auditStatus" value="Y">未审核</logic:notEqual>
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>