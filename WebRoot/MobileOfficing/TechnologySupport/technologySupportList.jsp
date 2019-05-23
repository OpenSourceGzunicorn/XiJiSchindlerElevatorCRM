<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<html:errors />
<html:form action="/ServeTable.do">
<html:hidden property="property(genReport)" styleId="genReport" />
<table>
		<tr>
		    <td>
			  <bean:message key="technologySupport.singleNo"/>:
			</td>
		    <td>
			  <html:text property="property(singleNo)" size="15"></html:text>
			</td>
		    <td>
		    &nbsp;&nbsp;
			  <bean:message key="technologySupport.assignUser"/>:
			</td>
		    <td>
			  <html:text property="property(assignUser)" size="15"></html:text>
			</td>
			<td>
			&nbsp;&nbsp;
		    <bean:message key="technologySupport.date"/>:
			</td>
		   <td>
		   		<html:text property="property(sdate1)" styleClass="Wdate" size="12" styleId="sdate1"  onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
				- 
				<html:text property="property(edate1)" styleClass="Wdate" size="12" styleId="edate1" onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
			</td>
			<tr>
			<td>
				维保分部:
			</td>
			<td>
			<html:select property="property(maintDivision)">
				    <html:options collection="CompanyList" property="grcid"
						labelProperty="grcname" />
				</html:select>
			</td>
			<td>
			&nbsp;&nbsp;
			处理状态:
			</td>
			<td>
			<html:select property="property(hproStatus)">
				    <html:option value="">全部</html:option>
				    <html:option value="1">站长处理</html:option>
				    <html:option value="2">经理处理</html:option>
				    <html:option value="3">公司技术支持处理</html:option>
				    <html:option value="4">处理完成</html:option>
				</html:select>
			</td>
			</tr>
	</table>
<br>
	 <table:table id="guiTechnologySupportMain" name="technologySupportList">
		<logic:iterate id="element" name="technologySupportList">
			<table:define id="c_cb">
			 
			 <bean:define id="billno" name="element" property="billno" />  
				  <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=billno.toString()%>" />
			
			</table:define>
			 <table:define id="c_singleNo">
			 <a href="<html:rewrite page='/technologySupportAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billno"/>">
				<bean:write name="element" property="singleNo" />	
			 </a>	
			 </table:define>
			 <table:define id="c_assignUser">			 
				<bean:write name="element" property="assignUser" />	
		
			</table:define> 
			
			<table:define id="c_assignUserTel">
				<bean:write name="element" property="assignUserTel" />
			</table:define> 
			<table:define id="c_operDate">
				<bean:write name="element" property="operDate" />
			</table:define>	
			 <table:define id="c_maintDivision">
				<bean:write name="element" property="maintDivision" />
			</table:define> 
			
			<table:define id="c_maintStation">
				<bean:write name="element" property="maintStation" />				
			</table:define>
			

			<table:define id="c_proStatus">
				<bean:write name="element" property="proStatus" />
			</table:define>
			<table:define id="c_msprocessPeople">
				<bean:write name="element" property="msprocessPeople" />
			</table:define>
			<table:define id="c_mmprocessPeople">
				<bean:write name="element" property="mmprocessPeople" />
			</table:define>
			<table:define id="c_tsprocessPeople">
				<bean:write name="element" property="tsprocessPeople" />
			</table:define>					
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>