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
        保养单号
        :
      </td>
      <td>
        <html:text property="property(singleno)" styleClass="default_input" />
      </td>                 
      <td>
        &nbsp;&nbsp;               
                     电梯编号
        :
      </td> 
      <td>
        <html:text property="property(elevatorNo)" styleClass="default_input" />
      </td>              
      <td>
      &nbsp;&nbsp;
              保养开始日期
        :
      </td>
      <td>
        <html:text property="property(sdate)" styleClass="default_input" styleClass="Wdate" size="12" styleId="sdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/>
      </td> 
      <td>  
        &nbsp;&nbsp;     
               保养结束日期
        :
      </td>
      <td>
        <html:text property="property(edate)" styleClass="default_input" styleClass="Wdate" size="12" styleId="edate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/>
      </td>                        
	<td>
	&nbsp;&nbsp;处理状态
		:<html:select 
		property="property(handleStatus)">
		 <html:option value="%">请选择</html:option>
		 <html:option value="Y">未完工</html:option>
		 <html:option value="N">已完工</html:option>			
		</html:select>
	</td>
	</tr>
	
  </table>
  <br>
   <table:table id="guiMaintenanceRegistration" name="maintenanceRegistrationList">
    <logic:iterate id="element" name="maintenanceRegistrationList">
      <table:define id="c_cb">
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${element.numno}" />    					
      </table:define>	
      <table:define id="c_singleno">
          <bean:write name="element" property="singleno" />      
      </table:define>
      <table:define id="c_elevatorNo">
       <a href="<html:rewrite page='/maintenanceRegistrationAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="numno"/>">
        <bean:write name="element" property="elevatorNo" />
        </a>
      </table:define>
      <table:define id="c_projectName">
        <bean:write name="element" property="projectName" />
      </table:define>
      <table:define id="c_maintType">
       <logic:match name="element" property="maintType" value="halfMonth" >
		半月保养
       </logic:match>
       <logic:match name="element" property="maintType" value="quarter" >
		季度保养 
       </logic:match>
       <logic:match name="element" property="maintType" value="halfYear" >
		半年保养
       </logic:match>
       <logic:match name="element" property="maintType" value="yearDegree" >
		年度保养
       </logic:match>    
      </table:define>
       <table:define id="c_maintDate">
       <bean:write name="element" property="maintDate" />
      </table:define>
      <table:define id="c_handleStatus">
      <html:hidden name="element" property="handleStatus"/>
      <logic:empty name="element" property="handleStatus">
      未接收
      </logic:empty>
      <logic:notEmpty name="element" property="handleStatus">
        <logic:match name="element" property="handleStatus" value="0">已转派</logic:match>  
        <logic:match name="element" property="handleStatus" value="1">已接收</logic:match>  
        <logic:match name="element" property="handleStatus" value="2">已到场</logic:match>  
        <logic:match name="element" property="handleStatus" value="3">已完工</logic:match>
      </logic:notEmpty>  
      </table:define>
      <table:tr />
    </logic:iterate>
   </table:table>
</html:form>