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
        维保流水号
        :
      </td>
      <td>
        <html:text property="property(billNo)" styleClass="default_input" />
      </td>
      <td>
        &nbsp;&nbsp;               
        <bean:message key="maintContract.maintContractNo" />
        :
      </td>
      <td>
        <html:text property="property(maintContractNo)" styleClass="default_input" />
      </td>  
      <td>  
        &nbsp;&nbsp;     
       	 甲方单位
        :
      </td>
      <td>
        <html:text property="property(companyID)" styleClass="default_input" size="40" />
      </td>     
    </tr>
    <tr>
    
      <td>   
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
       	 销售合同号
        :
      </td>
      <td>
        <html:text property="property(salesContractNo)" styleClass="default_input" />
      </td>                 
      <td>
        &nbsp;&nbsp;               
                      项目名称
        :
      </td>
      <td>
        <html:text property="property(projectName)" styleClass="default_input" size="40" />
      </td> 
    </tr>
    
  </table>
  <br>
  <table:table id="guiMaintContractDelayNext" name="lostElevatorReportNextList">
    <logic:iterate id="element" name="lostElevatorReportNextList">
      <table:define id="c_cb">
        <bean:define id="billNo" name="element" property="billNo" />
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${billNo}" />
      </table:define>
      <table:define id="c_billNo">
        <bean:write name="element" property="billNo" />
      </table:define>
      <table:define id="c_maintContractNo">
        <a target="_blank" href="<html:rewrite page='/maintContractAction.do'/>?method=toDisplayRecord&workisdisplay=N&id=<bean:write name="element"  property="billNo"/>">
        	<bean:write name="element" property="maintContractNo" />
        </a>
      </table:define>
      <table:define id="c_companyName">
        <bean:write name="element" property="companyId" />
      </table:define>
      <table:define id="c_contractSdate">
        <bean:write name="element" property="contractSdate" />
      </table:define>
      <table:define id="c_contractEdate">
        <bean:write name="element" property="contractEdate" />
      </table:define>
      <table:define id="c_attn">
        <bean:write name="element" property="attn" />
      </table:define>
      <table:define id="c_maintDivision">
        <bean:write name="element" property="maintDivision" />
      </table:define>
      <table:define id="c_maintStation">
        <bean:write name="element" property="maintStation" />
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>