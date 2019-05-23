<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="advisoryComplaintsManageBean">
<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tbhead">
    	问题接收、登记
    </div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
			<td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.ProcessSingleNo"/>:</td>
			<td width="30%"><bean:write name="advisoryComplaintsManageBean" property="processSingleNo"/></td>
			<td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.MessageSource"/>:</td>
			<td width="30%"><bean:write name="advisoryComplaintsManageBean" property="messageSource"/></td>
	</tr>
	 <tr>
			<td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ReceivePer"/>:</td>
			<td><bean:write name="advisoryComplaintsManageBean" property="receivePer"/></td>
			<td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ReceiveDate"/>:</td>
			<td><bean:write name="advisoryComplaintsManageBean" property="receiveDate"/></td>
	</tr>
	 <tr>
			<td class="wordtd"><bean:message key="AdvisoryComplaintsManage.FeedbackPer"/>:</td>
			<td><bean:write name="advisoryComplaintsManageBean" property="feedbackPer"/></td>
			<td class="wordtd"><bean:message key="AdvisoryComplaintsManage.FeedbackTel"/>:</td>
			<td><bean:write name="advisoryComplaintsManageBean" property="feedbackTel"/></td>
	</tr>
	<tr>
			<td class="wordtd">是否委托处理:</td>
			<td>
				<logic:present name="advisoryComplaintsManageBean" property="isEntrust">
				<logic:match name="advisoryComplaintsManageBean" property="isEntrust" value="Y">是</logic:match>
				<logic:match name="advisoryComplaintsManageBean" property="isEntrust" value="N">否</logic:match>
				</logic:present>
			</td>
			<td class="wordtd"><bean:message key="AdvisoryComplaintsManage.process"/></td>
			<td><bean:write name="advisoryComplaintsManageBean" property="processPer"/></td>
	</tr>
	 <tr>
			<td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ProblemDesc"/>:</td>
			<td colspan="3"><bean:write name="advisoryComplaintsManageBean" property="problemDesc"/></td>
	</tr>    
    </table>
    <br>
    <div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tbhead">
    	问题处理
    </div>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
        <td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.processPer"/>:</td>
        <td width="30%"><bean:write name="advisoryComplaintsManageBean" property="processPer"/></td>
        <td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.processDate"/>:</td>
        <td width="30%"><bean:write name="advisoryComplaintsManageBean" property="processDate"/></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.projectName"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="projectName"/></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ContractNo"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="contractNo"/></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.InfoNo"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="infoNo"/></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ElevatorNo"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="elevatorNo"/></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.octId"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="octId"/></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.FactoryName"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="factoryName"/></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.DutyDepar"/>:</td>
        <td colspan="3"><bean:write name="advisoryComplaintsManageBean" property="dutyDepar"/></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.CarryOutSituat"/>:</td>
        <td colspan="3"><bean:write name="advisoryComplaintsManageBean" property="carryOutSituat"/></td>
      </tr>
    </table>
    <br>
    <div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tbhead">
    	分析、总结
    </div>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
        <td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.IssueSort1"/>:</td>
        <td width="30%"><bean:write name="advisoryComplaintsManageBean" property="issueSort1"/></td>
        <td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.IssueSort2"/>:</td>
        <td width="30%"><bean:write name="advisoryComplaintsManageBean" property="issueSort2"/></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.IssueSort3"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="issueSort3"/></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.IssueSort4"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="issueSort4"/></td>
      </tr> 
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.PartsName"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="partsName"/></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.QualityIndex"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="qualityIndex"/></td>
      </tr> 
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.occId"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="occId"/></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.AssessNo"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="assessNo"/></td>
      </tr> 
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ocaId"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="ocaId"/></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ProcessDifficulty"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="processDifficulty"/></td>
      </tr> 
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.IsClose"/>:</td>
        <td>
        	<logic:match name="advisoryComplaintsManageBean" property="isClose" value="Y">
					<bean:message key="AdvisoryComplaintsManage.Close"/>
			</logic:match>
			<logic:match name="advisoryComplaintsManageBean" property="isClose" value="N">
					<bean:message key="AdvisoryComplaintsManage.noClose"/>
			</logic:match>
        </td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.CompleteDate"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="completeDate"/></td>
      </tr> 
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ProcessTime"/>:</td>
        <td colspan="3"><bean:write name="advisoryComplaintsManageBean" property="processTime"/></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.AuditOperid"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="auditOperid"/></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.AuditDate"/>:</td>
        <td><bean:write name="advisoryComplaintsManageBean" property="auditDate"/></td>
      </tr> 
    </table>
</logic:present>