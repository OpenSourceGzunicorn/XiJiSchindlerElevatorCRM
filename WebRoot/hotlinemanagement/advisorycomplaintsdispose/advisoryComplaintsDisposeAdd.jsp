<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/advisoryComplaintsManageAction.do?method=toDisposeRecord">
	<html:hidden property="isreturn" />
	<html:hidden property="processType"/>
	<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tbhead">
    	问题登记
    </div>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
			<td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.ProcessSingleNo"/>:</td>
			<td width="30%">
				<html:text name="advisoryComplaintsManageBean" property="processSingleNo" styleClass="noborder" readonly="true"/>
			</td>
			<td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.MessageSource"/>:</td>
			<td width="30%">
				<bean:write name="advisoryComplaintsManageBean" property="messageSource"/>
				<html:hidden name="advisoryComplaintsManageBean" property="messageSource"/>
			</td>
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
			<td><bean:write name="processPerName"/></td>
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
        <td width="30%">
        	<bean:write name="processPerName"/>
        	<html:hidden name="advisoryComplaintsManageBean" property="processPer"/>
        </td>
        <td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.processDate"/>:</td>
        <td width="30%"><html:text name="advisoryComplaintsManageBean" property="processDate" styleClass="noborder" readonly="true"></html:text></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.projectName"/>:</td>
        <td><html:text name="advisoryComplaintsManageBean" property="projectName" styleClass="default_input" size="50"></html:text><font color="red">*</font></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ContractNo"/>:</td>
        <td><html:text name="advisoryComplaintsManageBean" property="contractNo" styleClass="default_input"></html:text><font color="red">*</font></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.InfoNo"/>:</td>
        <td><html:text name="advisoryComplaintsManageBean" property="infoNo" styleClass="default_input"></html:text></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ElevatorNo"/>:</td>
        <td><html:text name="advisoryComplaintsManageBean" property="elevatorNo" styleClass="default_input"></html:text><font color="red">*</font></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.octId"/>:</td>
        <td>
	        <html:select name="advisoryComplaintsManageBean" property="octId" styleId="octId" styleClass="default_input">
	        	<html:option value="">--请选择--</html:option>
	        	<html:options  collection="octList" property="octId" labelProperty="octName"></html:options>
	        </html:select><font color="red">*</font>
        </td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.FactoryName"/>:</td>
        <td><html:text name="advisoryComplaintsManageBean" property="factoryName" styleClass="default_input" size="50"></html:text></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.DutyDepar"/>:</td>
        <td colspan="3">
        	<table id="searchCompany" style="border: 0;margin: 0;" width="350" class="tb">
        		<tr>
        			<td colspan="2">
        				&nbsp;&nbsp;<input type="button" name="toaddrow" value="+" onclick="addElevators('searchCompanyAction',this)"/>
						&nbsp;<input type="button" name="todelrow" value="-" onclick="deleteRow(this)">
        			</td>
        		</tr>
        		<tr class="wordtd">
        			<td width="5%"><input type="checkbox" onclick="checkTableAll(this)"></td>
        			<td align="left">
        				部门名称
        			</td>
        		</tr>
        		<logic:present name="dutyDepar">
					<logic:iterate id="element" name="dutyDepar">
						<tr>
							<td><input type="checkbox" onclick="cancelCheckAll(this)"/></td>
							<td>
								<bean:write name="element" property="comfullname"/>
								<input type="hidden" name="comid" value="${element.comid }"/>
							</td>
						</tr>
					</logic:iterate>
				</logic:present>
        	</table>
        	<div id="caption_1" style="width: 350;padding-top: 0;padding-bottom: 2;border-top:0 none #ffffff;border-bottom: 1 solid #999999;" class="tb">
			</div>
        	<%-- <html:text name="advisoryComplaintsManageBean" property="dutyDepar" styleClass="default_input"></html:text> --%>
        </td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.CarryOutSituat"/>:</td>
        <td colspan="3"><html:textarea name="advisoryComplaintsManageBean" property="carryOutSituat" rows="4" cols="100"></html:textarea></td>
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
      <%-- <tr>
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
      </tr> --%>
    </table>
</html:form>
<script language="javascript">
/*--时钟函数--*/
function timenow(){	
 var nowtime=new Date().toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
 var processDate=document.getElementById("processDate");
 processDate.value=nowtime;
 //receiveDate.innerText=nowtime;
}
timenow();
</script>