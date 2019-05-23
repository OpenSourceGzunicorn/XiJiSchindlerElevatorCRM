<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"";
%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/qualityCheckManagementJbpmAction.do?method=toSaveApprove" enctype="multipart/form-data">
<html:hidden property="id"/>
<html:hidden name="qualityCheckManagementBean" property="billno"/>
<html:hidden property="processStatus"/>
<html:hidden property="isreturn"/>
<html:hidden property="tokenid" styleId="tokenid"/>
<html:hidden property="taskid"/>
<html:hidden property="taskname"/>
<html:hidden property="tasktype"/>
<a href="" id="avf" target="_blank"></a>
<html:hidden property="status" styleId="status"/>
<html:hidden property="flowname" styleId="flowname"/>
<html:hidden property="score"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
<tr>
			<td class="wordtd"><bean:message key="elevatorSale.elevatorNo"/>:</td>
			<td width="20%"><bean:write name="qualityCheckManagementBean" property="elevatorNo" />
			</td>
			<td class="wordtd">现场督查人员:</td>
			<td width="20%"><bean:write name="qualityCheckManagementBean" property="superviseId" /></td>
			<td class="wordtd">督查人员联系电话:</td>
			<td width="20%"><bean:write name="qualityCheckManagementBean" property="supervisePhone" /></td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.maintContractNo"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintContractNo" /></td>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.salesContractNo"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="salesContractNo" /></td>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.projectName"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="projectName" /></td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.maintDivision"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintDivision" /></td>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.maintStation"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintStation" /></td>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.MaintPersonnel"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintPersonnel" /></td>
		</tr>
		<tr>
			<td class="wordtd">维保工联系电话:</td>
			<td><bean:write name="qualityCheckManagementBean" property="personnelPhone" /></td>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.TotalPoints"/>:</td>
			<td>
				<%-- <bean:write name="qualityCheckManagementBean" property="totalPoints" /> --%>
				<html:text name="qualityCheckManagementBean" property="totalPoints" styleClass="default_input_noborder" readonly="true"/>
			</td>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.ScoreLevel"/>:</td>
			<td>
				<%-- <bean:write name="qualityCheckManagementBean" property="scoreLevel" /> --%>
				<html:text name="qualityCheckManagementBean" property="scoreLevel" styleClass="default_input_noborder" readonly="true"/>
			</td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.ChecksDateTime"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="checksDateTime" /></td>
			<td class="wordtd">参与维保人员:</td>
			<td >
				<bean:write name="qualityCheckManagementBean" property="r5" />
			</td>
			<td class="wordtd">备注</td>
			<td><bean:write name="qualityCheckManagementBean" property="r4" /></td>
		</tr>        
    </table>
    <br>
    <%int i=0; %>
    <table id="party_a" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <thead>
      <tr height="12">
          <td colspan="5"></td>
        </tr>
        <tr class="wordtd_header">
        	<logic:equal name="taskname" value="督查组长审核"><td>操作</td></logic:equal>
			<td width="15%">评分名称</td>
			<td>评分明细</td>
			<td width="5%">分值</td>
			<td width="20%">备注</td>
			<td width="10%"><bean:message key="qualitycheckmanagement.appendix"/></td>
        </tr>
      </thead>
      <tbody>
        <logic:present name="markingItemsComplyList">
          <logic:iterate id="ele" name="markingItemsComplyList" >
          	<tr id="tr_<%=i%>">
          	 <logic:equal name="taskname" value="督查组长审核">
          	 	<td align="center">
          	 		<input type="button" value="删除" class="default_input" onclick="deleRow('tr_<%=i%>')"/>
          	 		<input type="hidden" name="isDelete" value="N"/>
          	 	</td>
          	 </logic:equal>
	         <td>
	         	${ele.msName}
	         	<input type="hidden" name="jnlno" id="jnlno" value="${ele.jnlno}"/>
	         </td>
	         <td valign="top">
	         	<table width="100%" border="0" >
	         		<logic:present name="ele" property="detailList">
	         			<logic:iterate id="e" name="ele" property="detailList">
	         				<tr>
	         					<td>${e.detailName}</td>
	         				</tr>
	         			</logic:iterate>
	         		</logic:present>
	         	</table>
	         </td>
	         <td align="center">${ele.fraction}<input type="hidden" name="fraction" value="${ele.fraction}"/></td>
	         <td>${ele.rem}</td>
	         <td valign="top" align="center">
	         <table width="100%"  border="0" class="tb">
	         		<logic:present name="ele" property="fileList">
	         			<logic:iterate id="ele2" name="ele" property="fileList">
	         			<logic:notEqual name="ele2" property="ext1" value="Y">
	         				<tr><td  align="center">
	         					<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${ele2.newFileName}','${ele2.oldFileName}')">${ele2.oldFileName}</a>
	         					</td>
	         				</tr>
	         				</logic:notEqual>
	         			</logic:iterate>
	         		</logic:present>
	         	</table>
	         	<%-- <logic:notEmpty name="element1" property="annexPath">
				    <logic:notEqual value="" name="element1" property="annexPath">
				      	<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element1.annexPath}','MarkingScoreRegister')"><bean:message key="qualitycheckmanagement.check"/></a>
				    </logic:notEqual>
				</logic:notEmpty> --%>
	         </td>
	         <td align="center" style="display: none;"><textarea cols="18" rows="2" name="deleteRem"></textarea></td>
	         <td align="center" style="display: none;">
	         	<table width="100%" class="tb">
				<tr class="wordtd"><td width="5%" align="center"><input type="checkbox" onclick="checkTableFileAll(this)"></td>
				<td align="left">&nbsp;&nbsp;<input type="button" name="toaddrow" value=" + " onclick="AddRow(this,'Y','${ele.msId}')"/>
				&nbsp;<input type="button" name="todelrow" value=" - " onclick="deleteFileRow(this)">
				</td></tr>
				</table>
	         </td>
	        </tr>
	        <%i++; %>
          </logic:iterate>
        </logic:present>
      </tbody>
      <tfoot>
        <tr height="12"><td colspan="5"></td></tr>
      </tfoot>   
    </table>
    <br>
     
     <table id="party_b" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
     	<thead>
      <tr height="23">
          <td colspan="7">&nbsp;&nbsp;维保质量评分删除项</td>
        </tr>
        <tr class="wordtd_header" id="titleRow_0">
        	<logic:equal name="taskname" value="督查组长审核"><td width="5%">操作</td></logic:equal>
			<td width="15%">评分名称</td>
			<td>评分明细</td>
			<td width="5%">分值</td>
			<td width="12%">备注</td>
			<td width="10%"><bean:message key="qualitycheckmanagement.appendix"/></td>
			<td width="20%">删除原因<logic:equal name="taskname" value="督查组长审核"><font style="color:red;">*</font></logic:equal></td>
     		<td width="16%">删除原因附件<logic:equal name="taskname" value="督查组长审核"><font style="color:red;">*</font></logic:equal></td>
      </tr>
      </thead>
      <tbody>
        <logic:present name="deleList">
          <logic:iterate id="element1" name="deleList" >
          	<tr id="tr_<%=i%>">
          	 <logic:equal name="taskname" value="督查组长审核">
          	 	<td align="center">
          	 		<input type="button" value="删除" class="default_input" onclick="returnDeleRow('tr_<%=i%>')"/>
          	 		<input type="hidden" name="isDelete" value="Y"/>
          	 	</td>
          	 </logic:equal>
	         <td>
	         	${element1.msName}
         		<input type="hidden" name="jnlno" id="jnlno" value="${element1.jnlno}"/>
	         </td>
	         <td valign="top">
	         	<table width="100%" border="0">
	         		<logic:present name="element1" property="detailList">
	         			<logic:iterate id="e" name="element1" property="detailList">
	         				<tr>
	         					<td>${e.detailName}</td>
	         				</tr>
	         			</logic:iterate>
	         		</logic:present>
	         	</table>
	         </td>
	         <td align="center">${element1.fraction}<input type="hidden" name="fraction" value="${element1.fraction}"/></td>
	         <td>${element1.rem}</td>
	         <td align="center">
	         <table width="100%"  border="0" class="tb">
	         		<logic:present name="element1" property="fileList">
	         			<logic:iterate id="element2" name="element1" property="fileList">
	         				<logic:notEqual name="element2" property="ext1" value="Y">
	         				<tr><td  align="center">
	         					<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element2.newFileName}','${element2.oldFileName}')">${element2.oldFileName}</a>
	         					</td>
	         				</tr>
	         				</logic:notEqual>
	         			</logic:iterate>
	         		</logic:present>
	         </table>
	         </td>
	         <td valign="top">
	         	<logic:equal name="taskname" value="督查组长审核">
	         		<textarea cols="20" rows="2" name="deleteRem">${element1.deleteRem}</textarea>
	         	</logic:equal>
	         	<logic:notEqual name="taskname" value="督查组长审核">${element1.deleteRem}</logic:notEqual>
	         </td>
	         <td valign="top">
	         <table width="100%"  border="0" class="tb">
	         		<logic:present name="element1" property="fileList">
	         			<logic:iterate id="element3" name="element1" property="fileList">
	         				<logic:equal name="element3" property="ext1" value="Y">
	         				<tr><td  align="center">
	         					<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element3.newFileName}','${element3.oldFileName}')">${element3.oldFileName}</a>
	         					</td>
	         				</tr>
	         				</logic:equal>
	         			</logic:iterate>
	         		</logic:present>
	         </table>
	         </td>
	         
	        </tr>
	        <%i++; %>
          </logic:iterate>
        </logic:present>
      </tbody>
      <tfoot>
        <tr height="12"><td colspan="7"></td></tr>
      </tfoot> 
     </table>
     <br/>
       <table  width="99%" border="0" cellpadding="0" cellspacing="0" class="tb" >
 <tr>
 <td class="wordtd">维保工签名:</td>
    <td class="inputtd">
    <logic:notEmpty name="qualityCheckManagementBean" property="customerSignature">
		<img src="<%=basePath%>/qualityCheckManagementJbpmAction.do?method=toDownloadFileRecord1&customerSignature=${qualityCheckManagementBean.customerSignature}"
			width="${CSwidth}" height="${CSheight}" id="${qualityCheckManagementBean.customerSignature}_1"> 
	</logic:notEmpty>
	 <logic:notEmpty name="qualityCheckManagementBean" property="customerImage">
		<img src="<%=basePath%>/qualityCheckManagementJbpmAction.do?method=toDownloadFileRecord1&customerImage=${qualityCheckManagementBean.customerImage}"
			width="${CIwidth}" height="${CIheight}" id="${qualityCheckManagementBean.customerImage}_2">
	</logic:notEmpty>
    </td>
     </tr>

</table> 
 </br>
    <table width="100%" border="0" cellpadding="0"  class="tb">
      <tr>
        <td class="wordtd"><bean:message key="qualitycheckmanagement.SupervOpinion"/>:</td>
        <td><bean:write name="qualityCheckManagementBean" property="supervOpinion"/></td>
      </tr>
      <logic:equal name="taskname" value="督查组长审核">
      <tr>
       	<td class="wordtd">扣款金额:</td>
        <td><html:text name="qualityCheckManagementBean" property="deductMoney" styleId="deductMoney" onkeypress="f_check_number3()"></html:text>
        	<logic:match name="scoreLevel" value="不合格">
        		<font color="red">*</font>
        	</logic:match>
        </td>
      </tr>
      <tr>
        <td class="wordtd">考核意见:</td>
        <td><html:textarea name="qualityCheckManagementBean" property="assessRem" cols="78" rows="3"></html:textarea>
        	<logic:match name="scoreLevel" value="不合格">
        		<font color="red">*</font>
        	</logic:match>
        </td>
      </tr>
      </logic:equal>
      <logic:notEqual value="督查组长审核" name="taskname">
      	<logic:equal name="taskname" value="督查组长审核2">
      <tr>
       	<td class="wordtd">扣款金额:</td>
        <td><html:text name="qualityCheckManagementBean" property="deductMoney" styleId="deductMoney" onkeypress="f_check_number3()"></html:text>
        	<logic:match name="scoreLevel" value="不合格">
        		<font color="red">*</font>
        	</logic:match>
        </td>
      </tr>
      <tr>
        <td class="wordtd">考核意见:</td>
        <td><bean:write name="qualityCheckManagementBean" property="assessRem"/></td>
      </tr>
      </logic:equal>

      	<logic:notEqual name="taskname" value="督查组长审核2">
      	<tr>
       	<td class="wordtd">扣款金额:</td>
        <td><bean:write name="qualityCheckManagementBean" property="deductMoney"/></td>
      </tr>
      <tr>
        <td class="wordtd">考核意见:</td>
        <td><bean:write name="qualityCheckManagementBean" property="assessRem"/></td>
      </tr>
      </logic:notEqual>
      </logic:notEqual>
    </table>
    <br/>
<%@ include file="/workflow/approveResult.jsp" %>
<br/>
<logic:present name="processApproveList">
	<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
	  &nbsp;<b>&nbsp;审批流程</b>
	</div>	
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
		<tr class="wordtd_header">  <td class="wordtd" nowrap><div align="center">任务号</div></td>
      <td class="wordtd" nowrap><div align="center">任务名称</div></td> 
      <td class="wordtd" nowrap><div align="center">审批人</div></td>
      <td class="wordtd" nowrap><div align="center">审批意见</div></td>
      <td class="wordtd" nowrap><div align="center">审批日期</div></td>
      <td class="wordtd" nowrap><div align="center">审批结果</div></td>
      </tr>
		<logic:iterate id="element4" name="processApproveList" >
			<tr>
			    <td><bean:write name="element4" property="taskId" /></td>
				<td><bean:write name="element4" property="taskName" /></td>
				<td><bean:write name="element4" property="userId" /></td>
				<td><bean:write name="element4" property="approveRem" /></td>
				<td><bean:write name="element4" property="date1"/>&nbsp;<bean:write name="element4" property="time1"/></td>
				<td><bean:write name="element4" property="approveResult" /></td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>
</html:form>