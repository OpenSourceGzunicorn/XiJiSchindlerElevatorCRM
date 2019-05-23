<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"";
%>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<style>
  .show{display:block;}
  .hide{display:none;}

</style>
<logic:present name="elevatorTransferCaseRegisterBean">
<html:hidden name="elevatorTransferCaseRegisterBean" property="billno"/>
   <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
   <td nowrap="nowrap" class="wordtd" >合同类型：</td>
   	<td>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="contractType" value="XS">销售合同</logic:match>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="contractType" value="WG">维改合同</logic:match>
   	</td>
   	<td nowrap="nowrap" class="wordtd" >厂检次数：</td> 
   	<td>${elevatorTransferCaseRegisterBean.checkNum }
   	<%-- <html:text  name="elevatorTransferCaseRegisterBean" property="checkNum" readonly="true" styleClass="default_input_noborder" /> --%>
   	</td> 
   	<td nowrap="nowrap" class="wordtd" >&nbsp;</td>
   	<td>&nbsp;</td> 
   	</tr>
   	<tr>
   	<td nowrap="nowrap" class="wordtd" >实际厂检时间：</td>
   	<td>
   		${elevatorTransferCaseRegisterBean.checkDate }&nbsp;${elevatorTransferCaseRegisterBean.checkTime2 }
   	</td>
   	<td nowrap="nowrap" class="wordtd" >提交厂检时间：</td>
   	<td><bean:write name="elevatorTransferCaseRegisterBean" property="checkTime" /></td>
   	 <td nowrap="nowrap" class="wordtd" >&nbsp;</td>
   	<td>&nbsp;</td> 
   </tr>
   <tr>
   	<td nowrap="nowrap" class="wordtd">电梯编号：</td>
   	<td>${elevatorTransferCaseRegisterBean.elevatorNo }
   		<%-- <html:text name="elevatorTransferCaseRegisterBean"  property="elevatorNo" styleId="elevatorNo" readonly="true" styleClass="default_input_noborder" /> --%>
   	</td>
   	<td nowrap="nowrap" class="wordtd">合同号：</td>
   	<td>${elevatorTransferCaseRegisterBean.salesContractNo }
   	<%-- <html:text name="elevatorTransferCaseRegisterBean" property="salesContractNo" styleId="salesContractNo" readonly="true" styleClass="default_input_noborder" /> --%>
   	</td>
   	<td nowrap="nowrap" class="wordtd">项目名称：</td>
   	<td>${elevatorTransferCaseRegisterBean.projectName }
   	<%-- <html:text name="elevatorTransferCaseRegisterBean" property="projectName" styleId="projectName" readonly="true" styleClass="default_input_noborder" /> --%>
   	</td>
   </tr>
   <tr>
   	<td nowrap="nowrap" class="wordtd">项目地址：</td>
   	<td>${elevatorTransferCaseRegisterBean.projectAddress }
   	<%-- <html:text name="elevatorTransferCaseRegisterBean"  property="projectAddress" styleId="projectAddress" readonly="true" styleClass="default_input_noborder" size="35" /> --%></td>
   	<td nowrap="nowrap" class="wordtd">电梯类型：</td>
   	<td><logic:match name="elevatorTransferCaseRegisterBean" property="elevatorType" value="T">直梯</logic:match>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="elevatorType" value="F">扶梯</logic:match>
   	</td>
   	<td nowrap="nowrap" class="wordtd">规格型号：</td>
   	<td>${elevatorTransferCaseRegisterBean.elevatorParam }
   	<%-- <html:text name="elevatorTransferCaseRegisterBean" property="elevatorParam" styleId="elevatorParam" readonly="true" styleClass="default_input_noborder" /> --%>
   	</td>
   </tr>
   <tr>
   	<td class="wordtd">合同性质：</td>
   <td>
   	<html:text name="elevatorTransferCaseRegisterBean" property="salesContractType" styleId="salesContractType" readonly="true" styleClass="default_input_noborder" /></td>
   	<td nowrap="nowrap" class="wordtd">层/站/门：</td>
   	<td>${elevatorTransferCaseRegisterBean.r1}
   	</td>
   	<td nowrap="nowrap" class="wordtd">提升高度：</td>
   	<td>${elevatorTransferCaseRegisterBean.high }
   	 <%-- <html:text name="elevatorTransferCaseRegisterBean" property="high" styleId="high" readonly="true" styleClass="default_input_noborder" /> --%></td>
   	 </tr>
   <tr>
   	<td nowrap="nowrap" class="wordtd">载重：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="weight" styleId="weight" readonly="true" styleClass="default_input_noborder" /></td>
   	<td nowrap="nowrap" class="wordtd">额定速度：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="speed" styleId="speed" readonly="true" styleClass="default_input_noborder" /></td>  	
   	<td nowrap="nowrap" class="wordtd">项目省份：</td>
   	<td>${elevatorTransferCaseRegisterBean.projectProvince }
   	
   </tr>   
   <tr>
   		<td nowrap="nowrap"  class="wordtd">是否迅达安装：</td>
   	<td colspan="5">
   	<logic:match name="elevatorTransferCaseRegisterBean" property="isxjs" value="Y">是</logic:match>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="isxjs" value="N">否</logic:match>
   	</td>
   	</tr>
   	<tr>
   	<td nowrap="nowrap" class="wordtd">安装公司名称：</td>
   	<td>${elevatorTransferCaseRegisterBean.insCompanyName }
   	<%-- <html:text name="elevatorTransferCaseRegisterBean" property="insCompanyName" readonly="true" size="35" styleClass="default_input_noborder"  /> --%></td>
   	<td nowrap="nowrap" class="wordtd">安装公司联系人和电话：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean"  property="insLinkPhone" readonly="true" styleClass="default_input_noborder"  /></td>
   	<td nowrap="nowrap" class="wordtd">安装公司邮箱：</td>
   	<td>${elevatorTransferCaseRegisterBean.insEmail }</td>
   	</tr>  
   <tr>
   <td nowrap="nowrap" class="wordtd">所属部门：</td>
   	<td>
   		<html:text name="elevatorTransferCaseRegisterBean" readonly="true" property="department" styleClass="default_input_noborder" />
   	</td>
   <td nowrap="nowrap" class="wordtd">厂检人员名称：</td>
   	<td>
   		<html:text name="elevatorTransferCaseRegisterBean" readonly="true" property="staffName" styleClass="default_input_noborder" />
   	</td>
   	<td nowrap="nowrap" class="wordtd">厂检人员联系电话：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" readonly="true" property="staffLinkPhone" readonly="true" styleClass="default_input_noborder" /></td>        
   </tr>
   <tr>
   	<td nowrap="nowrap" class="wordtd">项目经理姓名及电话：</td>
   	<td><bean:write name="elevatorTransferCaseRegisterBean" property="projectManager"/></td>
   	<td nowrap="nowrap" class="wordtd">调试人员姓名及电话：</td>
   	<td><bean:write name="elevatorTransferCaseRegisterBean" property="debugPers"/></td>
    <td nowrap="nowrap"  class="wordtd">是否初次安装：</td>
   	<td>
   	<logic:equal name="elevatorTransferCaseRegisterBean" property="firstInstallation" value="Y">是</logic:equal>
   	<logic:equal name="elevatorTransferCaseRegisterBean" property="firstInstallation" value="N">否</logic:equal>
   	</td>
   </tr>
   <tr>          
   	<td nowrap="nowrap" class="wordtd">厂检结果：</td>
   	<td ><html:text name="elevatorTransferCaseRegisterBean" property="factoryCheckResult" readonly="true" styleClass="default_input_noborder" /></td> 
   	<td nowrap="nowrap" class="wordtd">厂检分数：</td>
   	<td >
   		<logic:equal name="elevatorTransferCaseRegisterBean" property="checkNum" value="1">
	   		<html:text name="elevatorTransferCaseRegisterBean" property="r2" readonly="true" styleClass="default_input_noborder" />
	   	</logic:equal>
	   	<logic:notEqual name="elevatorTransferCaseRegisterBean" property="checkNum" value="1">
	   		<html:text name="elevatorTransferCaseRegisterBean" property="r2" readonly="true" value="" styleClass="default_input_noborder" />
	   	</logic:notEqual>
   	</td> 
   	<td nowrap="nowrap" class="wordtd">厂检结果2：</td>
   	<td ><html:text name="elevatorTransferCaseRegisterBean" property="factoryCheckResult2" readonly="true" styleClass="default_input_noborder" /></td>
   	
   	<%-- <td nowrap="nowrap" class="wordtd">特殊要求：</td>
   	<td colspan="3">
   	<table id="searchCompany" style="border: none;">
<%int i=1; %>
        		<logic:present name="specialRegister">
        		<tr>
					  <logic:iterate id="element" name="specialRegister">
							<td nowrap="nowrap" style="border: none;" style="border: none;" width="5%" >
							<logic:match name="element" property="isOk" value="Y">
							<input type="checkbox" disabled="disabled" checked="checked">
							</logic:match>
                            <logic:match name="element" property="isOk" value="N">
                             <input type="checkbox" disabled="disabled" >                                        
                            </logic:match></td>
							<td style="border: none;" >
								<bean:write name="element" property="r1"/>
								<input type="hidden" name="scId" value="${element.scId }"/>
								<input type="hidden" name="scjnlno" value="${element.jnlno }"/>
							</td>
						<% if(i%2==0){ %></tr><tr><%} %>
						<%i++; %>
					</logic:iterate>
					</tr>
				</logic:present>
        	</table>
        	</td> 
   	</tr> --%>
   	</tr>
   	<tr>
   	<td nowrap="nowrap" class="wordtd">电梯位置：</td>
   	<td colspan="5">${elevatorTransferCaseRegisterBean.elevatorAddress }</td> 
   	</tr>
   	<tr>
   	<td class="wordtd">录入人：</td>
   	<td><bean:write name="elevatorTransferCaseRegisterBean" property="operId"/></td>
   	<td class="wordtd">录入日期：</td>
   	<td><bean:write name="elevatorTransferCaseRegisterBean" property="operDate"/></td>
    <td class="wordtd">接收日期：</td>
   	<td><bean:write name="elevatorTransferCaseRegisterBean" property="receiveDate"/></td>
   </tr>
		<tr>
		<td nowrap="nowrap" class="wordtd">驳回日期：</td>
		<td><bean:write name="elevatorTransferCaseRegisterBean" property="bhDate"/></td>
		<td nowrap="nowrap" class="wordtd">驳回分类：</td>
		<td>${elevatorTransferCaseRegisterBean.bhType }</td>
		<td class="wordtd">驳回原因：</td>
		<td><bean:write name="elevatorTransferCaseRegisterBean" property="bhRem" /></td>
		</tr>
		<tr>
			<td nowrap="nowrap" class="wordtd">转派人：</td>
			<td><bean:write name="elevatorTransferCaseRegisterBean" property="transferId"/></td>
			<td nowrap="nowrap" class="wordtd">转派日期：</td>
			<td><bean:write name="elevatorTransferCaseRegisterBean" property="transferDate"/></td>
			<td nowrap="nowrap" class="wordtd"></td>
			<td></td>
		</tr>
</table>

<br/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td nowrap="nowrap" class="wordtd">特殊要求：</td>
		<td>
			<table id="searchCompany" style="border: none;">
				<%int i=1; %>
        		<logic:present name="specialRegister">
        		<tr>
					  <logic:iterate id="element" name="specialRegister">
							<td nowrap="nowrap" style="border: none;" style="border: none;" width="5%" >
							<logic:match name="element" property="isOk" value="Y">
							<input type="checkbox" disabled="disabled" checked="checked">
							</logic:match>
                            <logic:match name="element" property="isOk" value="N">
                             <input type="checkbox" disabled="disabled" >                                        
                            </logic:match></td>
							<td style="border: none;" >
								<bean:write name="element" property="r1"/>
								<input type="hidden" name="scId" value="${element.scId }"/>
								<input type="hidden" name="scjnlno" value="${element.jnlno }"/>
							</td>
						<% if(i%4==0){ %></tr><tr><%} %>
						<%i++; %>
					</logic:iterate>
					</tr>
				</logic:present>
        	</table>
		</td>
	</tr>
</table>

<br>
<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  <b>&nbsp;检查电梯问题(红色为漏检问题)</b>
</div>
<table id="party_a" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="5%">序号</td>
			<td width="10%">检查类型</td>
			<td width="15%">检查项目</td>
			<td width="10%">问题编号</td>
			<td width="35%">问题内容</td>
			<td width="20%">备注</td>
			<td width="10%">附件</td>
		</tr>
	</thead>
	<tfoot>
      <tr height="15"><td colspan="6"></td></tr>
    </tfoot>
    <logic:present name="hecirList">
    <logic:iterate id="ele" name="hecirList">
      <tr>
      		<td align="center">${ele.examTypeNum}</td>
			 <td style="color: ${ele.color}" >${ele.examType}</td>
			 <td >${ele.checkItem}</td>
	         <td >${ele.issueCoding}</td>
	         <td >${ele.issueContents1}</td>
	         <td >${ele.rem}</td>
	         <td >
	         	<table width="100%" class="tb">
				<logic:present name="ele" property="fileList">
					<logic:iterate id="element2" name="ele" property="fileList">
						<tr>
						<td>
							<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element2.newFileName}','${element2.oldFileName}','${element2.filePath}')">${element2.oldFileName}</a>
						</td></tr>
					</logic:iterate>
				</logic:present>
				</table>
	         </td>
      </tr>
      </logic:iterate>
    </logic:present>
</table>
<br> 
 <logic:present name="hecirDeketeList">
   <div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  <b>&nbsp;厂检部长删除问题</b>
</div>
<table id="party_a" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="5%">序号</td>
			<td width="10%">检查类型</td>
			<td width="8%">检查项目</td>
			<td width="8%">问题编号</td>
			<td width="20%">问题内容</td>
			<td width="7%">备注</td>
			<td width="10%">附件</td>
			<td width="20%">删除原因</td>
			<td width="22%">删除原因附件</td>
		</tr>
	</thead>
	<tfoot>
      <tr height="15"><td colspan="6"></td></tr>
    </tfoot>
    <logic:iterate id="eleDelete" name="hecirDeketeList">
      <tr>
      		<td align="center">${eleDelete.examTypeNum}</td>
			 <td style="color: ${eleDelete.color}">${eleDelete.examType}</td>
			 <td >${eleDelete.checkItem}</td>
	         <td >${eleDelete.issueCoding}</td>
	         <td >${eleDelete.issueContents1}</td>
	         <td >${eleDelete.rem}</td>
	         <td >
	         <table width="100%" class="tb">
				<logic:present name="eleDelete" property="fileList">
					<logic:iterate id="element2" name="eleDelete" property="fileList">
					<logic:notEqual name="element2" property="ext1" value="Y">
						<tr>
						<td>
							<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element2.newFileName}','${element2.oldFileName}','${element2.filePath}')">${element2.oldFileName}</a>
						</td></tr>
						</logic:notEqual>
					</logic:iterate>
				</logic:present>
				</table>
	         </td>
	          <td>${eleDelete.deleteRem}</td>
	           <td >
	         <table width="100%" class="tb">
				<logic:present name="eleDelete" property="fileList">
					<logic:iterate id="element2" name="eleDelete" property="fileList">
					<logic:match name="element2" property="ext1" value="Y">
						<tr>
						<td>
							<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element2.newFileName}','${element2.oldFileName}','${element2.filePath}')">${element2.oldFileName}</a>
						</td></tr>
						</logic:match>
					</logic:iterate>
				</logic:present>
				</table>
	         </td>
      </tr>
      </logic:iterate>
</table>
<br>
 </logic:present>
  <table  width="99%" border="0" cellpadding="0" cellspacing="0" class="tb" >
 <tr>
 <td class="wordtd">客户签名:</td>
    <td class="inputtd">
    <logic:notEmpty name="elevatorTransferCaseRegisterBean" property="customerSignature">
		<img src="<%=basePath%>/elevatorTransferCaseRegisterDisplayAction.do?method=toDownloadFileRecord1&customerSignature=${elevatorTransferCaseRegisterBean.customerSignature}"
			width="${CSwidth}" height="${CSheight}" id="${elevatorTransferCaseRegisterBean.customerSignature}_1"> 
	</logic:notEmpty>	
	<logic:notEmpty name="elevatorTransferCaseRegisterBean" property="customerImage">	
		<img src="<%=basePath%>/elevatorTransferCaseRegisterDisplayAction.do?method=toDownloadFileRecord1&customerImage=${elevatorTransferCaseRegisterBean.customerImage}"
			width="${CIwidth}" height="${CIheight}" id="${elevatorTransferCaseRegisterBean.customerImage}_2">
	</logic:notEmpty>
    </td>
     </tr>

</table>
 </br>
 <div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
	  <b>&nbsp;厂检部长上传附件</b>
	</div>
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td nowrap="nowrap" class="wordtd" width="10%">备注:</td>
		<td><bean:write name="elevatorTransferCaseRegisterBean" property="checkRem"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="wordtd" width="10%">附件:</td>
		<td>
			
			<table width="45%" class="tb">
				<logic:present name="wbfilelist">
					<logic:iterate id="wbele" name="wbfilelist">
						<tr><td>
							<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${wbele.newFileName}','${wbele.oldFileName}','${wbele.filePath}')">${wbele.oldFileName}</a>
						</td></tr>
					</logic:iterate>
				</logic:present>
			</table>
		</td>
	</tr>
	</table><br>
	
 <table id="party_a" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	
	 <tr>
		<td class="wordtd">是否扣款：</td>
		<td colspan="3">
		<logic:present  name="elevatorTransferCaseRegisterBean" property="isDeductions">
		   <logic:match name="elevatorTransferCaseRegisterBean" property="isDeductions" value="Y">同意扣款</logic:match>
		   <logic:match name="elevatorTransferCaseRegisterBean" property="isDeductions" value="N">同意不扣款</logic:match>		
		</logic:present>
		</td>
      </tr>
       <tr>
		<td class="wordtd">扣款金额(元)：</td>
		<td colspan="3">
		<bean:write name="elevatorTransferCaseRegisterBean" property="deductMoney"/>
		
		
		</td>
      </tr>
      <tr>
		<td class="wordtd">扣款备注：</td>
		<td colspan="3">
		<textarea rows="3" cols="158" style="width: 100%;" readonly="readonly" class=default_input_noborder><bean:write name="elevatorTransferCaseRegisterBean" property="processResult"/></textarea></td>
      </tr>
      <tr>
	<td nowrap="nowrap" class="wordtd">关闭备注:</td>
	<td>
		<textarea rows="3" cols="158" style="width: 100%;" readonly="readonly" class=default_input_noborder>${elevatorTransferCaseRegisterBean.colseRem }</textarea></td>
	</td>
   </tr>
</table>
<br> 

 
 
 
  <table width="100%" border="0" cellpadding="3" cellspacing="0"  class="tb">
    <tr height="23"><td colspan="6">&nbsp;<b>审批流程</b></td></tr>
    <tr>
      <td class="wordtd" ><div align="center">任务号</div></td>
      <td class="wordtd" ><div align="center">任务名称</div></td> 
      <td class="wordtd" ><div align="center">审批人</div></td>
      <td class="wordtd" ><div align="center">审批意见</div></td>
      <td class="wordtd" ><div align="center">审批日期</div></td>
      <td class="wordtd" ><div align="center">审批结果</div></td>
    </tr>  
    <logic:present name="etcpList">
     <logic:iterate name="etcpList" id="item">
       <tr>
         <td width="10%" ><bean:write name="item" property="taskId"/></td>
         <td width="10%" ><bean:write name="item" property="taskName"/></td>
         <td width="10%" ><bean:write name="item" property="userId"/></td>
         <td><bean:write name="item" property="approveRem"/></td>
         <td width="10%" ><bean:write name="item" property="date"/>&nbsp;&nbsp;<bean:write name="item" property="time1"/></td>
         <td width="10%" ><bean:write name="item" property="approveResult"/></td>
       </tr>
     </logic:iterate>   
    </logic:present> 
  </table>
</logic:present>