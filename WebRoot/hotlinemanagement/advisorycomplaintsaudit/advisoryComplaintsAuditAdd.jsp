<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">

<br>
<html:errors/>
<html:form action="/advisoryComplaintsManageAction.do?method=toAuditRecord" >
	<html:hidden property="isreturn" />
	<html:hidden property="auditType"/>
	<html:hidden property="processType"/>
	<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tbhead">
    	问题登记
    </div>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
			<td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.ProcessSingleNo"/>:</td>
			<td width="30%">
				<bean:write name="advisoryComplaintsManageBean" property="processSingleNo"/>
				<html:hidden name="advisoryComplaintsManageBean" property="processSingleNo"/>
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
        <td width="30%"><html:text name="advisoryComplaintsManageBean" property="processDate" styleClass="noborder" readonly="true"></html:text></td>
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
        <td width="30%">
        	<html:select name="advisoryComplaintsManageBean" property="issueSort1" styleId="issueSort1" styleClass="default_input" 
        	onchange="isFirm('${advisoryComplaintsManageBean.issueSort2}','${advisoryComplaintsManageBean.issueSort3}','${advisoryComplaintsManageBean.ocaId}');Evenmore('issueSort4','td1');"
        	 onfocus="Evenmore('issueSort4','td1');">
		        	<html:option value="">--请选择--</html:option>
		        	<html:options  collection="IS1List" property="id.pullid" labelProperty="pullname"></html:options>
	        </html:select><font color="red">*</font>
        </td>
        <td class="wordtd" width="20%"><bean:message key="AdvisoryComplaintsManage.IssueSort2"/>:</td>
        <td width="30%">
        	<input id="issue2" value="-" class="noborder" readonly="readonly" />
        	<span id="Sort2" style="display:none;">
        	<html:select name="advisoryComplaintsManageBean" property="issueSort2" styleClass="default_input" onchange="Evenmore('issueSort4','td1');" onfocus="Evenmore('issueSort4','td1');">
		        	<html:option value="">--请选择--</html:option>
		        	<html:options  collection="IS2List" property="id.pullid" labelProperty="pullname"></html:options>
	        </html:select>
	        <font color="red">*</font>
	        </span>
	        <%-- </logic:match> --%>
	        
        </td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.IssueSort3"/>:</td>
        <td>
        	<input id="issue3" value="-" class="noborder" readonly="readonly" />
        	<span id="Sort3" style="display:none;">
        	<html:select name="advisoryComplaintsManageBean" property="issueSort3" styleClass="default_input">
		        	<html:option value="">--请选择--</html:option>
		        	<html:options collection="IS3List" property="id.pullid" labelProperty="pullname"></html:options>
	        </html:select>
	        <font color="red">*</font>
	        </span>
        </td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.IssueSort4"/>:</td>
        <td id="td1"><%-- <html:text name="advisoryComplaintsManageBean" property="issueSort4" styleClass="default_input"></html:text> --%>
        	<html:select name="advisoryComplaintsManageBean" property="issueSort4">
        		<html:option value=""><bean:message key="pageword.pleaseselect"/></html:option>
        		<logic:present name="IS4List">
        			<html:options collection="IS4List" property="id.pullid" labelProperty="pullname"/>
        		</logic:present>
        	</html:select>
        	<font color="red">*</font>
        </td>
      </tr> 
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.PartsName"/>:</td>
        <td style="vertical-align:top;"><html:text name="advisoryComplaintsManageBean" property="partsName" styleClass="default_input"></html:text><font color="red">*</font></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.QualityIndex"/>:</td>
        <td>
        	<%-- <html:select name="advisoryComplaintsManageBean" property="qualityIndex" styleId="qualityIndex" styleClass="default_input">
		        	<html:option value="">--请选择--</html:option>
		        	<html:options  collection="qiList" property="id.pullid" labelProperty="pullname"></html:options>
	        </html:select> --%>
	        <logic:iterate id="ele" name="qiList">
	        	<input type="checkbox" name="qualityIndex" id="qualityIndex" value="${ele.id.pullid}"/>${ele.pullname} 
	        </logic:iterate>
	        <font color="red">*</font>
        </td>
      </tr> 
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.occId"/>:</td>
        <td>
        	<logic:match name="advisoryComplaintsManageBean" property="messageSource" value="400">
		        <html:select name="advisoryComplaintsManageBean" property="occId" styleId="occId" styleClass="default_input">
		        	<html:option value="">--请选择--</html:option>
		        	<html:options  collection="occList" property="occId" labelProperty="occName"></html:options>
		        </html:select><font color="red">*</font>
        	</logic:match>
        	<logic:notEqual value="400" name="advisoryComplaintsManageBean" property="messageSource">
        		<html:text name="advisoryComplaintsManageBean" property="occId" style="border:0;" styleClass="default_input" readonly="true" value="-"></html:text>
        	</logic:notEqual>
        </td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.AssessNo"/>:</td>
        <td><html:text name="advisoryComplaintsManageBean" property="assessNo" styleClass="default_input"></html:text></td>
      </tr> 
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ocaId"/>:</td>
        <td>
        	<input id="oca" value="-" class="noborder" readonly="readonly" />
        	<span id="O" style="display:none;">
        	<html:select name="advisoryComplaintsManageBean" property="ocaId" styleId="ocaId" styleClass="default_input">
		        	<html:option value="">--请选择--</html:option>
		        	<html:options  collection="ocaList" property="ocaId" labelProperty="ocaName"></html:options>
	        </html:select>
	        <font color="red">*</font>
	        </span>
        </td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ProcessDifficulty"/>:</td>
        <td>
        	<html:select name="advisoryComplaintsManageBean" property="processDifficulty" styleId="processDifficulty" styleClass="default_input">
		        	<html:option value="">--请选择--</html:option>
		        	<html:options  collection="pdList" property="id.pullid" labelProperty="pullname"></html:options>
	        </html:select><font color="red">*</font>
        </td>
      </tr> 
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.IsClose"/>:</td>
        <td>
        	<html:select name="advisoryComplaintsManageBean" property="isClose" styleClass="default_input" onchange="Close(this,'${advisoryComplaintsManageBean.receiveDate}')">
        		<html:option value="">--请选择--</html:option>
        		<html:option value="N"><bean:message key="AdvisoryComplaintsManage.noClose"/></html:option>
        		<html:option value="Y"><bean:message key="AdvisoryComplaintsManage.Close"/></html:option>
        	</html:select>
        	<font color="red">*</font>
        </td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.CompleteDate"/>:</td>
        <td><html:text name="advisoryComplaintsManageBean" property="completeDate" styleId="completeDate" styleClass="noborder" readonly="true"></html:text></td>
      </tr> 
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.ProcessTime"/>:</td>
        <td colspan="3"><html:text name="advisoryComplaintsManageBean" property="processTime" styleId="processTime" styleClass="noborder" readonly="true"></html:text></td>
      </tr>
      <tr>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.AuditOperid"/>:</td>
        <td><html:text name="advisoryComplaintsManageBean" property="auditOperid" styleClass="noborder" readonly="true"></html:text></td>
        <td class="wordtd"><bean:message key="AdvisoryComplaintsManage.AuditDate"/>:</td>
        <td><html:text name="advisoryComplaintsManageBean" property="auditDate" styleClass="noborder" readonly="true"></html:text></td>
      </tr> 
    </table>
</html:form>
<script language="javascript">
isShow('${advisoryComplaintsManageBean.issueSort1}');
/*--时钟函数--*/
function timenow(){	
 var nowtime=new Date().toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
 var auditDate=document.getElementById("auditDate");
 auditDate.value=nowtime;
 //receiveDate.innerText=nowtime;
}
function settypeid(){

	 var iddds=document.getElementsByName("qualityIndex");
	<logic:present name="list2">
    <logic:iterate id="eleid" name="list2" >            
		var typeid2='<bean:write name="eleid"/>';
		for(var j=0;j<iddds.length;j++){
			 //alert(typeid2+"=="+iddds[j].value);
			  if(typeid2==iddds[j].value){
				  iddds[j].checked=true;
				  break;
			  }
		  }
	</logic:iterate>
	</logic:present>	
 }
 settypeid();
timenow();
</script>
