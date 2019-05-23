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
<html:form action="/elevatorTransferCaseRegisterJbpmAction.do?method=toSaveApprove" enctype="multipart/form-data">
<html:hidden property="id"/>
<html:hidden name="elevatorTransferCaseRegisterBean" property="billno"/>
<html:hidden property="processStatus"/>
<html:hidden property="isreturn"/>
<html:hidden property="tokenId" styleId="tokenId"/>
<html:hidden property="taskId"/>
<html:hidden property="taskName" styleId="taskName"/>
<html:hidden property="tasktype"/>
<a href="" id="avf" target="_blank"></a>
<html:hidden property="status" styleId="status"/>
<html:hidden property="flowname" styleId="flowname"/>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
   <td nowrap="nowrap" class="wordtd"  >合同类型：</td>
   	<td>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="contractType" value="XS">销售合同</logic:match>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="contractType" value="WG">维改合同</logic:match>
   	</td>
   	<td nowrap="nowrap" class="wordtd" >厂检次数：</td> 
   	<td>
   		<html:text name="elevatorTransferCaseRegisterBean" property="checkNum" styleId="checkNum" readonly="true" styleClass="default_input_noborder" />
   	</td> 
   	<td nowrap="nowrap" class="wordtd" >&nbsp;</td>
   	<td>&nbsp;</td> 
   	</tr>
   	<tr>
   	<td nowrap="nowrap" class="wordtd" >实际厂检时间：</td>
   	<td>
   		${elevatorTransferCaseRegisterBean.checkDate }
   	</td>
   	<td nowrap="nowrap" class="wordtd" >提交厂检时间：</td>
   	<td><bean:write name="elevatorTransferCaseRegisterBean" property="checkTime" /></td>
   	 <td nowrap="nowrap" class="wordtd" >&nbsp;</td>
   	<td>&nbsp;</td> 
   </tr>
   <tr>
   	<td nowrap="nowrap" class="wordtd">电梯编号：</td>
   	<td>
   		<html:text name="elevatorTransferCaseRegisterBean" property="elevatorNo" styleId="elevatorNo" readonly="true" styleClass="default_input_noborder" />
   	</td>
   	<td nowrap="nowrap" class="wordtd">合同号：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="salesContractNo" styleId="salesContractNo" readonly="true" styleClass="default_input_noborder" /></td>
   
   	<td nowrap="nowrap" class="wordtd">项目名称：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="projectName" styleId="projectName" readonly="true" styleClass="default_input_noborder" /></td>
   </tr>
   <tr>
   	<td nowrap="nowrap" class="wordtd">项目地址：</td>
   	<td>${elevatorTransferCaseRegisterBean.projectAddress }
   	<%-- <html:text name="elevatorTransferCaseRegisterBean" property="projectAddress" styleId="projectAddress" readonly="true" styleClass="default_input_noborder" size="50" /> --%></td>
   	<td nowrap="nowrap" class="wordtd">电梯类型：</td>
   	<td><logic:match name="elevatorTransferCaseRegisterBean" property="elevatorType" value="T">直梯</logic:match>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="elevatorType" value="F">扶梯</logic:match>
   	<html:hidden name="elevatorTransferCaseRegisterBean" property="elevatorType" styleId="elevatorType"/>
   	</td>
   	<td nowrap="nowrap" class="wordtd">规格型号：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="elevatorParam" styleId="elevatorParam" readonly="true" styleClass="default_input_noborder" /></td>
   </tr>
   <tr>
   	<td class="wordtd">合同性质：</td>
   <td>
   	<html:text name="elevatorTransferCaseRegisterBean" property="salesContractType" styleId="salesContractType" readonly="true" styleClass="default_input_noborder" /></td>
   	<td nowrap="nowrap" class="wordtd">层/站/门：</td>
   	<td><bean:write name="elevatorTransferCaseRegisterBean" property="floor"/>/<bean:write name="elevatorTransferCaseRegisterBean" property="stage"/>/<bean:write name="elevatorTransferCaseRegisterBean" property="door"/>
   	</td>
   	<td nowrap="nowrap" class="wordtd">提升高度：</td>
   	<td>
   	 <html:text name="elevatorTransferCaseRegisterBean" property="high" styleId="high" readonly="true" styleClass="default_input_noborder" /></td>
   	 </tr>
   <tr>
   	<td nowrap="nowrap" class="wordtd">载重：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="weight" styleId="weight" readonly="true" styleClass="default_input_noborder" /></td>
   	<td nowrap="nowrap" class="wordtd">额定速度：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="speed" styleId="speed" readonly="true" styleClass="default_input_noborder" /></td>  	
   	<td nowrap="nowrap" class="wordtd">项目省份：</td>
   	<td>${elevatorTransferCaseRegisterBean.projectProvince }</td>
   </tr>   
   <tr>
   		<td nowrap="nowrap"  class="wordtd">是否迅达安装：</td>
   	<td>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="isxjs" value="Y">是</logic:match>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="isxjs" value="N">否</logic:match>
   	</td>
   	
   	<td nowrap="nowrap" class="wordtd">安装公司名称：</td>
   	<td>${elevatorTransferCaseRegisterBean.insCompanyName }
   	<%-- <html:text name="elevatorTransferCaseRegisterBean" property="insCompanyName" size="40" readonly="true" styleClass="default_input_noborder"  /> --%></td>
   	<td nowrap="nowrap" class="wordtd">安装公司联系人和电话：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="insLinkPhone" readonly="true" styleClass="default_input_noborder"  /></td>
   	</tr>  
   <tr>
   <td nowrap="nowrap" class="wordtd">所属部门：</td>
   	<td>
   		<html:text name="elevatorTransferCaseRegisterBean" property="department" readonly="true" styleClass="default_input_noborder" />
   	</td>
   <td nowrap="nowrap" class="wordtd">厂检人员名称：</td>
   	<td>
   		<html:text name="elevatorTransferCaseRegisterBean" property="staffName" readonly="true" styleClass="default_input_noborder" />
   	</td>
   	<td nowrap="nowrap" class="wordtd">厂检人员联系电话：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="staffLinkPhone" readonly="true" styleClass="default_input_noborder" /></td>        
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
   	<html:hidden name="elevatorTransferCaseRegisterBean" property="firstInstallation" styleId="firstInstallation"/>
   	</td>
   </tr>
    <tr>          
   	<td nowrap="nowrap" class="wordtd">厂检结果：</td>
   	<td ><html:text name="elevatorTransferCaseRegisterBean" property="factoryCheckResult" readonly="true" styleClass="default_input_noborder" /></td> 
   	<td nowrap="nowrap" class="wordtd">厂检分数：</td>
   	<td>
   		<logic:equal name="elevatorTransferCaseRegisterBean" property="checkNum" value="1">
	   		<html:text name="elevatorTransferCaseRegisterBean" property="r2" readonly="true" styleClass="default_input_noborder" />
	   	</logic:equal>
	   	<logic:notEqual name="elevatorTransferCaseRegisterBean" property="checkNum" value="1">
	   		<html:text name="elevatorTransferCaseRegisterBean" property="r2" readonly="true" value="" styleClass="default_input_noborder" />
	   	</logic:notEqual>
   	</td> 
   	<td nowrap="nowrap" class="wordtd">厂检结果2：</td>
   	<td ><html:text name="elevatorTransferCaseRegisterBean" property="factoryCheckResult2" readonly="true" styleClass="default_input_noborder" /></td> 
   	
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
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td nowrap="nowrap" class="wordtd">特殊要求：</td>
		<td>
			<table id="searchCompany"  style="border:none;" >
               <%int specialno=1; %>
        		<logic:present name="specialRegister">
					<tr>
					  <logic:iterate id="element" name="specialRegister">
						<logic:match name="element" property="isOk" value="Y">
							<td style="border:none;" width="5%">
							<logic:match name="element" property="isOk" value="Y">
							<input type="checkbox" disabled="disabled" checked="checked">
							</logic:match>
                            <logic:match name="element" property="isOk" value="N">
                             <input type="checkbox" disabled="disabled" >                                        
                            </logic:match></td>
							<td style="border:none;">
								<bean:write name="element" property="r1"/>
								<input type="hidden" name="scId" value="${element.scId }"/>
								<input type="hidden" name="scjnlno" value="${element.jnlno }"/>
							</td>
						</logic:match>
							<% if(specialno%4==0){ %></tr><tr><%} %>
                            <%specialno++; %>
					</logic:iterate>
					</tr>
				</logic:present>
        	</table>
		</td>
	</tr>
</table>
<br>
<%int i=1,j=0; %>
<logic:equal name="taskName" value="厂检部长审核">
<logic:notPresent name="display">
<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">       
  <b>&nbsp;检查电梯问题(红色为漏检问题)</b>
</div>
<table id="party_a" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
		    <td width="5%">操作</td>
			<td width="10%">检查类型</td>
			<td width="10%">检查项目</td>
			<td width="10%">问题编号</td>
			<td width="35%">问题内容</td>
			<td width="15%">备注</td>
			<td width="15%">附件</td>
			<td width="20%" nowrap="nowrap">是否已整改</td>
		</tr>
	</thead>
	
   <tfoot>
      <tr height="15"><td colspan="7"><input type="hidden" id="noDeleteLength"></td></tr>
    </tfoot>
    <tbody>
    <logic:present name="hecirList">
    <logic:iterate id="ele" name="hecirList">
      <tr id="tr_<%=i %>">
             <td><input type="hidden" name="deleteBtn"> <input type="button" value="删除" onclick="deleteRow('tr_<%=i %>')"><input type="hidden" name="isDelete" value="N"></td>
			 <td style="color: ${ele.color}" class="${ele.color}">${ele.examTypeName}
			 <input type="hidden" name="examType" value="${ele.examType}">
			 <input type="hidden" name="jnlno" value="${ele.jnlno}">
			 <input type="hidden" name="isRecheck" value="${ele.isRecheck}">
			 <input type="hidden" name="itemgroup" value="${ele.itemgroup}">
			 </td>
			 <td>${ele.checkItem}</td>
	         <td>${ele.issueCoding}</td>
	         <td>${ele.issueContents1}</td>
	         <td>${ele.rem}</td>
	         <td style="valign: top;">${ele.fileListsize}
	           <table width="100%" class="tb">
				<logic:present name="ele" property="fileList">
					<logic:iterate id="element2" name="ele" property="fileList">
						<logic:notEqual name="element2" property="ext1" value="Y">
						<tr>
						<td>
							<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element2.newFileName}','${element2.oldFileName}','${element2.filePath}')">${element2.oldFileName}</a>
						</td>
						</tr>
						</logic:notEqual>
					</logic:iterate>
				</logic:present>
				</table>
	         </td>
	         <td align="center">
	         	<logic:equal name="ele" property="isyzg" value="Y">已整改</logic:equal>
	         	<input type="hidden" name="isyzg" value="${ele.isyzg}"/>
	         </td>
	         <td style="display: none;"><textarea cols="30" rows="2" name="deleteRem"></textarea></td>
	         <td style="display: none;">
	         	<table width="100%" class="tb">
				<tr class="wordtd"><td width="5%" align="center"><input type="checkbox" onclick="checkTableFileAll(this)"></td>
				<td align="left">&nbsp;&nbsp;<input type="button" name="toaddrow" value=" + " onclick="AddRow(this,'Y','${ele.checkItem}','${ele.examType}','${ele.issueCoding}')"/>
				&nbsp;<input type="button" name="todelrow" value=" - " onclick="deleteFileRow(this)">
				</td></tr>
				</table>
	         </td>
	    <%i++;%> 
	    </tr>    
      </logic:iterate>
    </logic:present>
	</tbody>
</table>
<br/>
<logic:present name="hecirDeketeList">
<table id="party_b"  width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			 <td width="5%">操作</td>
			<td width="10%">检查类型</td>
			<td width="8%">检查项目</td>
			<td width="8%">问题编号</td>
			<td width="15%">问题内容</td>
			<td width="7%">备注</td>
			<td width="10%">附件</td>
			<td width="10%">是否已整改</td>
			<td width="10%">删除原因<font style="color: red">*</font></td>
			<td width="22%" nowrap="nowrap">删除原因附件</td>
		</tr>
		</thead>
		<tfoot>
      <tr height="15"><td colspan="9"></td></tr>
      </tfoot>
       <tbody>
          <logic:iterate id="eleDelete" name="hecirDeketeList">
            <tr id="tr_<%=i %>">
             <td ><input type="hidden" name="deleteBtn"> <input type="button" value="删除" onclick="returnDeleteRow('tr_<%=i %>')"><input type="hidden" name="isDelete" value="Y"></td>
			 <td  style="color: ${eleDelete.color}" class="${eleDelete.color}">${eleDelete.examTypeName}
			 <input type="hidden" name="examType" value="${eleDelete.examType}">
			 <input type="hidden" name="jnlno" value="${eleDelete.jnlno}">
			 <input type="hidden" name="isRecheck" value="${eleDelete.isRecheck}">
			 <input type="hidden" name="itemgroup" value="${eleDelete.itemgroup}">
			 </td>
			 <td >${eleDelete.checkItem}</td>
	         <td >${eleDelete.issueCoding}</td>
	         <td >${eleDelete.issueContents1}</td>
	         <td >${eleDelete.rem}</td>
	         <td ><table width="100%" class="tb">
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
	         <td align="center">
	         	<logic:equal name="eleDelete" property="isyzg" value="Y">已整改</logic:equal>
	         	<input type="hidden" name="isyzg" value="${eleDelete.isyzg}"/>
	         </td>
			 <td><textarea cols="30" rows="2" name="deleteRem">${eleDelete.deleteRem}</textarea></td>
			 <td>
	         	<table width="100%" class="tb">
				<tr class="wordtd"><td width="5%" align="center"><input type="checkbox" onclick="checkTableFileAll(this)"></td>
				<td align="left">&nbsp;&nbsp;<input type="button" name="toaddrow" value="+" onclick="AddRow(this,'Y','${eleDelete.checkItem}','${eleDelete.examType}','${eleDelete.issueCoding}')"/>
				&nbsp;<input type="button" name="todelrow" value="-" onclick="deleteFileRow(this)">
				</td></tr>
				<logic:present name="eleDelete" property="fileList">
					<logic:iterate id="element2" name="eleDelete" property="fileList">
						<logic:match name="element2" property="ext1" value="Y">
						<tr>
						<td></td>
						<td>
							<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element2.newFileName}','${element2.oldFileName}','${element2.filePath}')">${element2.oldFileName}</a>
				      	<a style="cursor:hand;" onclick="deleteFile(this,'${element2.fileSid}','N')"><img src="../../common/images/toolbar/del_attach.gif" alt="<bean:message key="delete.button.value"/>" /></a>
				      	<%j++; %>
						</td></tr>
						</logic:match>
					</logic:iterate>
				</logic:present>
				</table><br>
	         </td>
			<%i++;%> 
      </tr>
      </logic:iterate>
      </tbody>
</table>
<br/>
</logic:present>
<logic:notPresent name="hecirDeketeList">
<table id="party_b" style="display: none;"  width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			 <td width="5%">操作</td>
			<td width="10%">检查类型</td>
			<td width="8%">检查项目</td>
			<td width="8%">问题编号</td>
			<td width="15%">问题内容</td>
			<td width="7%">备注</td>
			<td width="10%">附件</td>
			<td width="10%">是否已整改</td>
			<td width="10%">删除原因<font style="color: red">*</font></td>
			<td width="22%" nowrap="nowrap">删除原因附件</td>
		</tr>
		</thead>
		<tfoot>
      <tr height="15"><td colspan="7"></td></tr>
      </tfoot>
</table>
<br/>
</logic:notPresent>
</logic:notPresent>
<logic:present name="display">
<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  <b>&nbsp;检查电梯问题(红色为漏检问题)</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="10%">检查类型</td>
			<td width="10%">检查项目</td>
			<td width="10%">问题编号</td>
			<td width="35%">问题内容</td>
			<td width="20%">备注</td>
			<td width="10%">附件</td>
			<td width="20%" nowrap="nowrap">是否已整改</td>
		</tr>
	</thead>
	<tfoot>
      <tr height="15"><td colspan="8"></td></tr>
    </tfoot>
    <logic:present name="hecirList">
    <logic:iterate id="ele" name="hecirList">
      <tr> 
			 <td style="color: ${ele.color}">${ele.examTypeName}</td>
			 <td >${ele.checkItem}</td>
	         <td >${ele.issueCoding}</td>
	         <td >${ele.issueContents1}</td>
	         <td >${ele.rem}</td>
	         <td >
	         	  <table width="100%" class="tb">
				<logic:present name="ele" property="fileList">
					<logic:iterate id="element2" name="ele" property="fileList">
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
	         <td align="center">
	         	<logic:equal name="ele" property="isyzg" value="Y">已整改</logic:equal>
	         	<input type="hidden" name="isyzg" value="${ele.isyzg}"/>
	         </td>
	         <td style="display: none;"><input name="deleteRem" type="text"></td>
      </tr>
      </logic:iterate>
    </logic:present>
</table>
<br> 
<logic:present name="hecirDeketeList">
   <div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  <b>&nbsp;厂检部长删除问题</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="10%">检查类型</td>
			<td width="10%">检查项目</td>
			<td width="10%">问题编号</td>
			<td width="20%">问题内容</td>
			<td width="15%">备注</td>
			<td width="10%">附件</td>
			<td width="10%">是否已整改</td>
			<td width="10%">删除原因</td>
			<td width="10%" nowrap="nowrap">删除原因附件</td>
		</tr>
	</thead>
	<tfoot>
      <tr height="15"><td colspan="6"></td></tr>
    </tfoot>
    <logic:iterate id="eleDelete" name="hecirDeketeList">
      <tr>
			 <td style="color: ${eleDelete.color}">${eleDelete.examTypeName}</td>
			 <td >${eleDelete.checkItem}</td>
	         <td >${eleDelete.issueCoding}</td>
	         <td >${eleDelete.issueContents1}</td>
	         <td >${eleDelete.rem}</td>
	         <td >  <table width="100%" class="tb">
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
				</table></td>
	         <td align="center">
	         	<logic:equal name="eleDelete" property="isyzg" value="Y">已整改</logic:equal>
	         	<input type="hidden" name="isyzg" value="${eleDelete.isyzg}"/>
	         </td>
		<td>${eleDelete.deleteRem}<input type="hidden" name="deleteRem" value="${eleDelete.deleteRem}"></td>
		<td>
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
</logic:present>
</logic:equal>
<logic:notEqual name="taskName" value="厂检部长审核">
<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  <b>&nbsp;检查电梯问题(红色为漏检问题)</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="10%">检查类型</td>
			<td width="10%">检查项目</td>
			<td width="10%">问题编号</td>
			<td width="35%">问题内容</td>
			<td width="20%">备注</td>
			<td width="10%">附件</td>
			<td width="20%" nowrap="nowrap">是否已整改</td>
		</tr>
	</thead>
	<tfoot>
      <tr height="15"><td colspan="6"></td></tr>
    </tfoot>
    <logic:present name="hecirList">
    <logic:iterate id="ele" name="hecirList">
      <tr>
			 <td style="color: ${ele.color}">${ele.examTypeName}</td>
			 <td >${ele.checkItem}</td>
	         <td >${ele.issueCoding}</td>
	         <td >${ele.issueContents1}</td>
	         <td >${ele.rem}</td>
	         <td >
	         	  <table width="100%" class="tb">
				<logic:present name="ele" property="fileList">
					<logic:iterate id="element2" name="ele" property="fileList">
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
	         
	         <td align="center">
	         	<logic:equal name="ele" property="isyzg" value="Y">已整改</logic:equal>
	         	<input type="hidden" name="isyzg" value="${ele.isyzg}"/>
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
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="10%">检查类型</td>
			<td width="10%">检查项目</td>
			<td width="10%">问题编号</td>
			<td width="20%">问题内容</td>
			<td width="15%">备注</td>
			<td width="10%">附件</td>
			<td width="10%">是否已整改</td>
			<td width="10%">删除原因</td>
			<td width="10%" nowrap="nowrap">删除原因附件</td>
		</tr>
	</thead>
	<tfoot>
      <tr height="15"><td colspan="8"></td></tr>
    </tfoot>
    <logic:iterate id="eleDelete" name="hecirDeketeList">
      <tr>
			 <td style="color: ${eleDelete.color}">${eleDelete.examTypeName}</td>
			 <td >${eleDelete.checkItem}</td>
	         <td >${eleDelete.issueCoding}</td>
	         <td >${eleDelete.issueContents1}</td>
	         <td >${eleDelete.rem}</td>
	         <td >  <table width="100%" class="tb">
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
				</table></td>
				
	         <td align="center">
	         	<logic:equal name="eleDelete" property="isyzg" value="Y">已整改</logic:equal>
	         	<input type="hidden" name="isyzg" value="${eleDelete.isyzg}"/>
	         </td>
			<td>${eleDelete.deleteRem}<input type="hidden" name="deleteRem" value="${eleDelete.deleteRem}"></td>
			<td>
			<table width="100%" class="tb">
			<logic:present name="eleDelete" property="fileList">
					<logic:iterate id="element3" name="eleDelete" property="fileList">
						<logic:match name="element3" property="ext1" value="Y">
						<tr>
						<td>
							<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element3.newFileName}','${element3.oldFileName}','${element3.filePath}')">${element3.oldFileName}</a>
						</td>
						</tr>
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
</logic:notEqual>
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
<logic:notPresent name="display">
		<logic:match name="taskName" value="厂检部长审核">
		<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
		  <b>&nbsp;厂检部长上传附件</b>
		</div>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
		<tr>
			<td nowrap="nowrap" class="wordtd" width="10%">备注:</td>
			<td><html:textarea name="elevatorTransferCaseRegisterBean" property="checkRem" styleId="checkRem" styleClass="default_textarea" cols="90" rows="3"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" class="wordtd" width="10%">附件:</td>
			<td>
			<table width="65%" class="tb">
				<tr class="wordtd"><td width="5%" align="center"><input type="checkbox" onclick="checkTableFileAll(this)"></td>
				<td align="left">&nbsp;&nbsp;<input type="button" name="toaddrow" value="+" onclick="AddRow2(this)"/>
				&nbsp;<input type="button" name="todelrow" value="-" onclick="deleteFileRow(this)">
				</td></tr>
				<logic:present name="wbfilelist">
					<logic:iterate id="wbele" name="wbfilelist">
						<tr>
						<td>&nbsp;</td>
						<td>
							<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${wbele.newFileName}','${wbele.oldFileName}','${wbele.filePath}')">${wbele.oldFileName}</a>
				      		<a style="cursor:hand;" onclick="deleteFile(this,'${wbele.fileSid}','Y')"><img src="../../common/images/toolbar/del_attach.gif" alt="<bean:message key="delete.button.value"/>" /></a>
				      		<%i++; %>
						</td></tr>
					</logic:iterate>
				</logic:present>
			</table>
			<br/>
			</td>
		</tr>
		</table><br>
		</logic:match>
		<logic:notEqual value="厂检部长审核" name="taskName">
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
		</logic:notEqual>
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
			<tr>
			<td nowrap="nowrap" class="wordtd" width="10%">是否扣款:</td>
			<td>
			<logic:match name="taskName" value="厂检部长审核">
				<html:select property="isDeductions" styleId="isDeductions" styleClass="default_input">
		          	<html:option value="">请选择</html:option>
		          	<html:option value="Y">同意扣款</html:option>
	          		<html:option value="N">同意不扣款</html:option>
		        </html:select><font color="red">*</font>
		     </logic:match>
		     <logic:notEqual value="厂检部长审核" name="taskName">
		     	<logic:present name="elevatorTransferCaseRegisterBean"  property="isDeductions">
		     		<logic:match name="elevatorTransferCaseRegisterBean" property="isDeductions" value="Y">同意扣款</logic:match>
					<logic:match name="elevatorTransferCaseRegisterBean" property="isDeductions" value="N">同意不扣款</logic:match>
				</logic:present>
			</logic:notEqual>
			</td>
			</tr>
			<tr>
				<td nowrap="nowrap" class="wordtd" width="10%">扣款金额(元):</td>
				<td id="td1">
				<logic:match name="taskName" value="安装部长审核">
				<script language="javascript">
				
					window.onload=function(){
						var approveResult=document.getElementById("approveResult");
						isDeductions(approveResult,'${elevatorTransferCaseRegisterBean.isDeductions}');
					}
				</script>
				</logic:match>
				<logic:notEqual value="装部长审核" name="taskName">
					<bean:write name="elevatorTransferCaseRegisterBean" property="deductMoney"/>
				</logic:notEqual>
				</td>
			</tr>
			<tr>
				<td nowrap="nowrap" class="wordtd" width="10%">扣款备注:</td>
				<td>
				<logic:match name="taskName" value="执行扣款">
					<html:textarea property="processResult" styleId="processResult" styleClass="default_textarea" cols="90" rows="2"/>
					<font color="red">*</font>
				</logic:match>
				<logic:notEqual value="执行扣款" name="taskName">
					<bean:write name="elevatorTransferCaseRegisterBean" property="processResult"/>
				</logic:notEqual>
				</td>
			</tr>
			<tr>
			<td nowrap="nowrap" class="wordtd" width="10%">关闭备注:</td>
				<td>
				<logic:match name="taskName" value="关闭流程">
				 <textarea rows="2" cols="90" class="default_textarea" name="colseRem" id="colseRem"></textarea> 
				</logic:match>
				<logic:notEqual value="关闭流程" name="taskName">
					${elevatorTransferCaseRegisterBean.colseRem }
				</logic:notEqual>
				</td>
			</tr>
		</table>
		<br/>
		
<div style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  <b>&nbsp;审批</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td nowrap="nowrap" class="wordtd">审批结果：</td>
		<td>
		<logic:notEqual value="安装部长审核" name="taskName">
			<html:select property="approveResult" styleId="approveResult" styleClass="default_input">
	          <html:options collection="ResultList" property="tranname" labelProperty="tranname"/>
	        </html:select>
	     </logic:notEqual>
	     <logic:match name="taskName" value="安装部长审核">
	     	<html:select property="approveResult" styleId="approveResult" styleClass="default_input" onchange="isDeductions(this,'${elevatorTransferCaseRegisterBean.isDeductions}');">
	          <html:options collection="ResultList" property="tranname" labelProperty="tranname"/>
	        </html:select>
	     </logic:match>
		</td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="wordtd">审批意见：</td>
		<td>
			<html:textarea property="approveRem" styleId="approveRem" styleClass="default_textarea" cols="90" rows="3"/>
		</td>
	</tr>
</table>
</logic:notPresent>
<logic:equal name="display" value="Y">

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

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
<tr>
	<td nowrap="nowrap" class="wordtd">是否扣款:</td>
	<td>
		<logic:present name="elevatorTransferCaseRegisterBean"  property="isDeductions">
			<logic:match name="elevatorTransferCaseRegisterBean" property="isDeductions" value="Y">同意扣款</logic:match>
			<logic:match name="elevatorTransferCaseRegisterBean" property="isDeductions" value="N">同意不扣款</logic:match>
		</logic:present>
	</td>
	</tr>
	<tr>
	<td nowrap="nowrap" class="wordtd">扣款金额(元):</td>
	<td>
		<bean:write name="elevatorTransferCaseRegisterBean" property="deductMoney"/>
	</td>
	</tr>
	<tr>
	<td nowrap="nowrap" class="wordtd">扣款备注:</td>
	<td>
		<bean:write name="elevatorTransferCaseRegisterBean" property="processResult"/>
	</td>
	</tr>
	
<tr>
	<td nowrap="nowrap" class="wordtd">关闭备注:</td>
	<td>
		
		${elevatorTransferCaseRegisterBean.colseRem }
	</td>
   </tr>
</table>
<br>
</logic:equal>
<br/>

<logic:present name="etcpList">
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
		<logic:iterate id="element4" name="etcpList" >
			<tr>
			    <td><bean:write name="element4" property="taskId" /></td>
				<td><bean:write name="element4" property="taskName" /></td>
				<td><bean:write name="element4" property="userId" /></td>
				<td><bean:write name="element4" property="approveRem" /></td>
				<td><bean:write name="element4" property="date1"/></td>
				<td><bean:write name="element4" property="approveResult" /></td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>
</html:form>