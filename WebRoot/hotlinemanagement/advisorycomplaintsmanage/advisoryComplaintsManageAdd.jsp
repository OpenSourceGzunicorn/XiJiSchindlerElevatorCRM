<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>

<br>
<html:errors />
<html:form action="/advisoryComplaintsManageAction.do?method=toAddRecord">
	<html:hidden property="isreturn" />
	<html:hidden property="submitType"/>
	<html:hidden property="dispatchType"/>
	<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tbhead">
    	问题登记
    </div>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
			<td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.ProcessSingleNo"/>:</td>
			<td width="30%"></td>
			<td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.MessageSource"/>:</td>
			<td width="30%"><%-- <html:text property="messageSource" styleClass="default_input"></html:text> --%>
				<html:select property="messageSource">
					<html:option value="">--请选择--</html:option>
					<html:options collection="MessageSourceList" property="id.pullid" labelProperty="pullname"/>
				</html:select>
				<font color="red">*</font>
			</td>
	</tr>
	 <tr>
			<td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ReceivePer"/>:</td>
			<td><html:text property="receivePer" styleClass="default_input_noborder"></html:text></td>
			<td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ReceiveDate"/>:</td>
			<td><html:text property="receiveDate" styleClass="default_input_noborder" readonly="true" value="${receiveDate}"></html:text></td>
	</tr>
	 <tr>
			<td class="wordtd"><bean:message key="AdvisoryComplaintsManage.FeedbackPer"/>:</td>
			<td><html:text property="feedbackPer" styleClass="default_input"></html:text><font color="red">*</font></td>
			<td class="wordtd"><bean:message key="AdvisoryComplaintsManage.FeedbackTel"/>:</td>
			<td><html:text property="feedbackTel" styleClass="default_input"></html:text><font color="red">*</font></td>
	</tr>
	<tr>
		<td class="wordtd">是否委托处理:</td>
		<td>
			<html:select property="isEntrust" styleClass="default_input" onchange="isdepute(this,'${processPer}')">
		        	<html:option value="">--请选择--</html:option>
		        	<html:option value="Y">是</html:option>
		        	<html:option value="N">否</html:option>
	        </html:select><font color="red">*</font>
		</td>
		<td class="wordtd"><bean:message key="AdvisoryComplaintsManage.process"/>:</td>
		<td><span id="processPer"></span></td>
	</tr>
	 <tr>
			<td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ProblemDesc"/>:</td>
			<td colspan="3"><html:textarea property="problemDesc" rows="4" cols="100"></html:textarea></td>
	</tr>    
    </table>
    <br>
    <div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tbhead">
    	问题处理
    </div>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
        <td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.processPer"/>:</td>
        <td width="30%"></td>
        <td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.processDate"/>:</td>
        <td width="30%"></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.projectName"/>:</td>
        <td></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ContractNo"/>:</td>
        <td></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.InfoNo"/>:</td>
        <td></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ElevatorNo"/>:</td>
        <td></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.octId"/>:</td>
        <td></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.FactoryName"/>:</td>
        <td></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.DutyDepar"/>:</td>
        <td colspan="3"></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.CarryOutSituat"/>:</td>
        <td colspan="3"></td>
      </tr>
    </table>
    <br>
    <div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tbhead">
    	分析、总结
    </div>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
        <td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.IssueSort1"/>:</td>
        <td width="30%"></td>
        <td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.IssueSort2"/>:</td>
        <td width="30%"></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.IssueSort3"/>:</td>
        <td></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.IssueSort4"/>:</td>
        <td></td>
      </tr> 
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.PartsName"/>:</td>
        <td></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.QualityIndex"/>:</td>
        <td></td>
      </tr> 
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.occId"/>:</td>
        <td></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.AssessNo"/>:</td>
        <td></td>
      </tr> 
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ocaId"/>:</td>
        <td></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ProcessDifficulty"/>:</td>
        <td></td>
      </tr> 
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.IsClose"/>:</td>
        <td></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.CompleteDate"/>:</td>
        <td></td>
      </tr> 
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ProcessTime"/>:</td>
        <td colspan="3"></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.AuditOperid"/>:</td>
        <td></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.AuditDate"/>:</td>
        <td></td>
      </tr> 
    </table>
</html:form>
