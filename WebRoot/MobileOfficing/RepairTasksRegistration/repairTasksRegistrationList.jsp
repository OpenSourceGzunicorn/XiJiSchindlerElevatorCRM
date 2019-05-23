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
      急修编号:
      </td>
      <td>
        <html:text property="property(calloutMasterNo)" styleClass="default_input" />
      </td> 
      <td>      
         &nbsp;&nbsp;
         报修单位名称:
      </td>
      <td>
        <html:text property="property(companyId)"  styleClass="default_input" />
      </td>         
    
        <td> 
       &nbsp;&nbsp;
       处理状态:
      </td>
      <td>
        <html:select property="property(handleStatus)">
        <html:option value="%">请选择</html:option>
         <html:option value="Y">已完工</html:option>
          <html:option value="N">未完工</html:option>
          
        </html:select>
      </td>   
      </tr>
  </table>
  <br>
  <table:table id="guiRepairTasksRegistration" name="repairTasksRegistrationList">
    <logic:iterate id="element" name="repairTasksRegistrationList">
      <table:define id="c_cb">
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${element.calloutMasterNo }" />      
      </table:define>
      <table:define id="c_calloutMasterNo">
       <a href="<html:rewrite page="/repairTasksRegistrationAction.do"/>?method=toDisplayRecord&id=${element.calloutMasterNo }">${element.calloutMasterNo }</a>     
      </table:define>
      <table:define id="c_companyId">
	  ${element.companyName }
      </table:define>
      <table:define id="c_isTrap">
	 ${element.isTrap }
      </table:define>
       <table:define id="c_repairMode">
	 ${element.repairMode }
      </table:define>
       <table:define id="c_serviceObjects">
	 ${element.serviceObjects } 
      </table:define>
        <table:define id="c_handleStatus">
	  ${element.handleStatusName }
	  <html:hidden name="element" property="handleStatus"/>
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>