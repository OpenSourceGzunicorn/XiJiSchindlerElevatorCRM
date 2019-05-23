<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>
<html:form action="/ServeTable.do">
	<table>
		<tr>
		    <td>
				&nbsp;&nbsp;
				 <bean:message key="AdvisoryComplaintsManage.ProcessSingleNo"/>
				:
			</td>
			<td>
			    <html:text property="property(processSingleNo)" styleClass="default_input" ></html:text>			
			</td>		
			<td>
				&nbsp;&nbsp;
				<bean:message key="AdvisoryComplaintsManage.ReceivePer"/>
				:
			</td>
			<td>
				<html:text property="property(receivePer)" styleClass="default_input" ></html:text>
			</td>
			<td>
				&nbsp;&nbsp;
				 <bean:message key="AdvisoryComplaintsManage.MessageSource"/>
				:
			</td>
			<td>
				<%-- <html:text property="property(messageSource)" styleClass="default_input" size="50" ></html:text> --%>
				<html:select property="property(messageSource)">
					<html:option value=""><bean:message key="qualitycheckmanagement.all"/></html:option>
					<html:options collection="MessageSourceList" property="id.pullid" labelProperty="pullname"/>
				</html:select>
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="AdvisoryComplaintsManage.ProcessType"/>
				:
			</td>
			<td>
				<html:select property="property(processType)">
		          <html:option value=""><bean:message key="qualitycheckmanagement.all"/></html:option>
			      <html:option value="Y">已处理未提交</html:option>
			      <html:option value="N">未处理</html:option>
			      <html:option value="S">已处理已提交</html:option>
			      <html:option value="R">驳回</html:option>
		        </html:select>
			</td>
			
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
	<br>
	<table:table id="guiAdvisoryComplaintsDispose" name="advisoryComplaintsDisposeList">
		<logic:iterate id="element" name="advisoryComplaintsDisposeList">
			<table:define id="c_cb">
			 <bean:define id="processSingleNo" name="element" property="processSingleNo" />  
				<%--  <html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox>  --%>
				
				  <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=processSingleNo.toString()%>" />
				  <html:hidden property="List(processType)" styleId="processType" value="${element.processType }"/>
			</table:define>
			 <table:define id="c_processSingleNo">
				 <a href="<html:rewrite page='/advisoryComplaintsManageAction.do'/>?method=toDisposeDisplay&id=<bean:write name="element"  property="processSingleNo"/>&returnMethod=toSearchRecordDispose"> 
				<bean:write name="element" property="processSingleNo" />
				</a>
			</table:define> 
			
			<table:define id="c_messageSource">
				<bean:write name="element" property="messageSource" />
			</table:define> 
			
			 <table:define id="c_receivePer">
				<bean:write name="element" property="receivePer" />
			</table:define> 
			
			<table:define id="c_receiveDate">
				<bean:write name="element" property="receiveDate" />
			</table:define>
			
			<table:define id="c_feedbackPer">
				<bean:write name="element" property="feedbackPer" />
			</table:define>
			
			<table:define id="c_feedbackTel">
				<bean:write name="element" property="feedbackTel" />
			</table:define>
			<table:define id="c_processType">
				<logic:match name="element" property="processType" value="Y">
					已处理已提交
				</logic:match>
				<logic:match name="element" property="processType" value="N">
					<bean:message key="AdvisoryComplaintsManage.unDisposed"/>
				</logic:match>
				<logic:match name="element" property="processType" value="S">
					已处理未提交
				</logic:match>
				<logic:match name="element" property="processType" value="R">
					驳回
				</logic:match>
			</table:define>
			<table:define id="c_auditType">
				<logic:match name="element" property="auditType" value="Y">
					已分析总结
				</logic:match>
				<logic:match name="element" property="auditType" value="N">
					未分析总结
				</logic:match>
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>