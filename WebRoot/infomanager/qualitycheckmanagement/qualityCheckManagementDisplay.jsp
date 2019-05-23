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
<logic:present name="qualityCheckManagementBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
<tr>
			<td class="wordtd"><bean:message key="elevatorSale.elevatorNo"/>:</td>
			<td width="20%"><bean:write name="qualityCheckManagementBean" property="elevatorNo" />
			</td>
			<td class="wordtd">电梯类型</td>
			<td>${elevaorTypeName}</td>
			<td class="wordtd">现场督查人员:</td>
			<td width="20%"><bean:write name="qualityCheckManagementBean" property="superviseId" /></td>
			</tr>
		<tr>
			
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.maintContractNo"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintContractNo" /></td>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.salesContractNo"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="salesContractNo" /></td>
			<td class="wordtd">督查人员联系电话:</td>
			<td width="20%"><bean:write name="qualityCheckManagementBean" property="supervisePhone" /></td>
		
			</tr>
		<tr>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.projectName"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="projectName" /></td>
		
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.maintDivision"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintDivision" /></td>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.maintStation"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintStation" /></td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.MaintPersonnel"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintPersonnel" /></td>
		
			<td class="wordtd">维保工联系电话:</td>
			<td><bean:write name="qualityCheckManagementBean" property="personnelPhone" /></td>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.TotalPoints"/>:</td>
			<td>
				<logic:notPresent name="dispose">
					<bean:write name="qualityCheckManagementBean" property="totalPoints" />
				</logic:notPresent>
				<logic:present name="dispose">
				<logic:equal name="isStatus" value="Y">
				<bean:write name="qualityCheckManagementBean" property="totalPoints" />
				</logic:equal>
				</logic:present>
				
			</td>
			</tr>
		<tr>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.ScoreLevel"/>:</td>
			<td>
				<logic:notPresent name="dispose">
					<bean:write name="qualityCheckManagementBean" property="scoreLevel" />
				</logic:notPresent>
				<logic:present name="dispose">
				<logic:equal name="isStatus" value="Y">
				<bean:write name="qualityCheckManagementBean" property="scoreLevel" />
				</logic:equal>
				</logic:present>
				
			</td>
		
			<td class="wordtd"><bean:message key="qualitycheckmanagement.ChecksDateTime"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="checksDateTime" /></td>
			
			<td class="wordtd">备注</td>
			<td><bean:write name="qualityCheckManagementBean" property="r4" /></td>
		</tr> 
		<tr>
			<td class="wordtd">参与维保人员:</td>
			<td colspan="5">
				<bean:write name="qualityCheckManagementBean" property="r5" />
			</td>
		</tr> 
    </table>
    <br>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <thead>
      <tr height="12">
          <td colspan="4"></td>
        </tr>
        <tr class="wordtd_header">
			<td width="20%">评分名称</td>
			<td>评分明细</td>
			<logic:notPresent name="dispose"><td width="5%">分值</td></logic:notPresent>
			<logic:present name="dispose">
				<logic:equal name="isStatus" value="Y">
				<td width="5%">分值</td>
				</logic:equal>
			</logic:present>
			
			
			<td width="20%">备注</td>
			<td width="8%"><bean:message key="qualitycheckmanagement.appendix"/></td>
        </tr>
      </thead>
      <tbody>
        <logic:present name="markingItemsComplyList">
          <logic:iterate id="element1" name="markingItemsComplyList" >
          	<tr>
	         <td>${element1.msName}</td>
	         <td valign="top">
	         	<table width="100%" border="0" >
	         		<logic:present name="element1" property="detailList">
	         			<logic:iterate id="e" name="element1" property="detailList">
	         				<tr>
	         					<td>${e.detailName}</td>
	         				</tr>
	         			</logic:iterate>
	         			
	         		</logic:present>
	         	</table>
	         </td>
	         <logic:notPresent name="dispose"><td align="center">${element1.fraction}</td></logic:notPresent>
	         <logic:present name="dispose">
				<logic:equal name="isStatus" value="Y">
				<td align="center">${element1.fraction}</td>
				</logic:equal>
			</logic:present>
	         
	         
	         <td>${element1.rem}</td>
	         <td>
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
	         	<%-- <logic:notEmpty name="element1" property="annexPath">
				    <logic:notEqual value="" name="element1" property="annexPath">
				      	<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element1.annexPath}','MarkingScoreRegister')"><bean:message key="qualitycheckmanagement.check"/></a>
				    </logic:notEqual>
				</logic:notEmpty> --%>
	         </td>
	        </tr>
          </logic:iterate>
        </logic:present>
      </tbody>
      <tfoot>
        <tr height="12"><td colspan="4"></td></tr>
      </tfoot>   
    </table>
    <br>
    
    <table id="party_b" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
     	<thead>
      <tr height="23">
          <td colspan="7">&nbsp;&nbsp;维保质量评分删除项</td>
        </tr>
        <tr class="wordtd_header" id="titleRow_0">
			<td width="15%">评分名称</td>
			<td>评分明细</td>
			<logic:notPresent name="dispose"><td width="5%">分值</td></logic:notPresent>
			<logic:present name="dispose">
				<logic:equal name="isStatus" value="Y">
				<td width="5%">分值</td>
				</logic:equal>
			</logic:present>
			
			
			<td width="20%">备注</td>
			<td width="8%"><bean:message key="qualitycheckmanagement.appendix"/></td>
			<td width="15%">删除原因</td>
        	<td width="16%">删除原因附件</td>
      </tr>
      </thead>
      <tbody>
        <logic:present name="deleList">
          <logic:iterate id="ele1" name="deleList" >
          	<tr>
	         <td>
	         	${ele1.msName}
         		<input type="hidden" name="jnlno" id="jnlno" value="${ele1.jnlno}"/>
	         </td>
	         <td valign="top">
	         	<table width="100%" border="0" >
	         		<logic:present name="ele1" property="detailList">
	         			<logic:iterate id="e" name="ele1" property="detailList">
	         				<tr>
	         					<td>${e.detailName}</td>
	         				</tr>
	         			</logic:iterate>
	         		</logic:present>
	         	</table>
	         </td>
	         <logic:notPresent name="dispose"><td align="center">${ele1.fraction}<input type="hidden" name="fraction" value="${ele1.fraction}"/></td></logic:notPresent>
	         <logic:present name="dispose">
				<logic:equal name="isStatus" value="Y">
				<td align="center">${ele1.fraction}<input type="hidden" name="fraction" value="${ele1.fraction}"/></td>
				</logic:equal>
			</logic:present>
	         
	         <td>${ele1.rem}</td>
	         <td align="center">
	         <table width="100%"  border="0" class="tb">
	         		<logic:present name="ele1" property="fileList">
	         			<logic:iterate id="ele2" name="ele1" property="fileList">
	         			<logic:notEqual name="ele2" property="ext1" value="Y">
	         				<tr><td  align="center">
	         					<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${ele2.newFileName}','${ele2.oldFileName}')">${ele2.oldFileName}</a>
	         					</td>
	         				</tr>
	         				</logic:notEqual>
	         			</logic:iterate>
	         		</logic:present>
	         </table>
	         </td>
	         <td>
	         	${ele1.deleteRem}
	         </td>
	         <td valign="top">
	         <table width="100%"  border="0" class="tb">
	         		<logic:present name="ele1" property="fileList">
	         			<logic:iterate id="element3" name="ele1" property="fileList">
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
		<img src="<%=basePath%>/qualityCheckManagementAction.do?method=toDownloadFileRecord1&customerSignature=${qualityCheckManagementBean.customerSignature}"
			width="${CSwidth}" height="${CSheight}" id="${qualityCheckManagementBean.customerSignature}_1">
	</logic:notEmpty>	 
	<logic:notEmpty name="qualityCheckManagementBean" property="customerImage">	
		<img src="<%=basePath%>/qualityCheckManagementAction.do?method=toDownloadFileRecord1&customerImage=${qualityCheckManagementBean.customerImage}"
			width="${CIwidth}" height="${CIheight}" id="${qualityCheckManagementBean.customerImage}_2">
	</logic:notEmpty>
    </td>
     </tr>

</table> 
 </br>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
        <td class="wordtd"><bean:message key="qualitycheckmanagement.SupervOpinion"/>:</td>
        <td><bean:write name="qualityCheckManagementBean" property="supervOpinion" /></td>
      </tr>
      <tr>
        <td class="wordtd">扣款金额:</td>
        <td><bean:write name="qualityCheckManagementBean" property="deductMoney" /></td>
      </tr>
      <tr>
        <td class="wordtd">考核意见:</td>
        <td><bean:write name="qualityCheckManagementBean" property="assessRem" /></td>
      </tr>
    </table>

    <br>
    <table width="100%" border="0" cellpadding="3" cellspacing="0"  class="tb">
    <tr height="23"><td colspan="6">&nbsp;<b>审批流程</b></td></tr>
    <tr>
      <td class="wordtd" nowrap><div align="center">任务号</div></td>
      <td class="wordtd" nowrap><div align="center">任务名称</div></td> 
      <td class="wordtd" nowrap><div align="center">审批人</div></td>
      <td class="wordtd" nowrap><div align="center">审批意见</div></td>
      <td class="wordtd" nowrap><div align="center">审批日期</div></td>
      <td class="wordtd" nowrap><div align="center">审批结果</div></td>
    </tr>  
    <logic:present name="processApproveList">
     <logic:iterate name="processApproveList" id="item">
       <tr>
         <td width="10%" nowrap><bean:write name="item" property="taskId"/></td>
         <td width="10%" nowrap><bean:write name="item" property="taskName"/></td>
         <td width="10%" nowrap><bean:write name="item" property="userId"/></td>
         <td><bean:write name="item" property="approveRem"/></td>
         <td width="10%" nowrap><bean:write name="item" property="date1"/>&nbsp;&nbsp;<bean:write name="item" property="time1"/></td>
         <td width="10%" nowrap><bean:write name="item" property="approveResult"/></td>
       </tr>
     </logic:iterate>   
    </logic:present> 
  </table>
    
</logic:present>