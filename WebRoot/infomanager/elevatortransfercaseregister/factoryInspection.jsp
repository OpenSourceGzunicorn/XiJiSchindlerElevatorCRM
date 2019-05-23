<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">

<br>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=GBK">
  <title>XJSCRM</title>
</head>
<body>
  <html:errors/>
  <html:form action="/elevatorTransferCaseRegisterManageAction.do?method=toFactoryRecord">
<html:hidden property="isreturn"/>
<input type="hidden" name="id">
<html:hidden property="submitType" />
<html:hidden property="hecirs" />
<input type="hidden" name="historyBillNo" id="historyBillNo" value="${historyBillNo }">

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
   <td nowrap="nowrap" class="wordtd">合同类型：</td>
   	<td>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="contractType" value="XS">销售合同</logic:match>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="contractType" value="WG">维改合同</logic:match>
    <html:hidden name="elevatorTransferCaseRegisterBean" property="contractType"/>
   	</td>
   	<td nowrap="nowrap" class="wordtd">厂检时间：</td>
   	<td><bean:write name="elevatorTransferCaseRegisterBean" property="checkTime" /></td>
   	<td nowrap="nowrap" class="wordtd">厂检次数：</td>
   	<td nowrap="nowrap" ><html:text style="width: 200px" name="elevatorTransferCaseRegisterBean" property="checkNum" readonly="true" styleClass="default_input_noborder" /></td>   
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
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="projectAddress" styleId="projectAddress" readonly="true" styleClass="default_input_noborder" size="40" /></td>
   	<td nowrap="nowrap" class="wordtd">电梯类型：</td>
   	<td><logic:match name="elevatorTransferCaseRegisterBean" property="elevatorType" value="T">直梯</logic:match>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="elevatorType" value="F">扶梯</logic:match>
   	<html:hidden name="elevatorTransferCaseRegisterBean" property="elevatorType"/>
   	</td>
   	<td nowrap="nowrap" class="wordtd">规格型号：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="elevatorParam" styleId="elevatorParam" readonly="true" styleClass="default_input_noborder" /></td>
   </tr>
   <tr>
   	<td class="wordtd">合同性质：</td>
   <td>
   	<html:text name="elevatorTransferCaseRegisterBean" property="salesContractType" styleId="salesContractType" readonly="true" styleClass="default_input_noborder" /></td>
   	<td nowrap="nowrap" class="wordtd">层/站/门：</td>
   	<td>${elevatorTransferCaseRegisterBean.r1}
   	 <html:hidden name="elevatorTransferCaseRegisterBean" property="floor" styleId="floor"/>
   	 <html:hidden name="elevatorTransferCaseRegisterBean" property="stage" styleId="stage"/>
   	 <html:hidden name="elevatorTransferCaseRegisterBean" property="door" styleId="door"/>
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
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="projectProvince" styleId="projectProvince" styleClass="default_input" /><font color='red'>*</font></td>  	
   	
   </tr>   
   <tr>
   		<td nowrap="nowrap"  class="wordtd">是否迅达安装：</td>
   	<td colspan="5">
   	<logic:match name="elevatorTransferCaseRegisterBean" property="isxjs" value="Y">是</logic:match>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="isxjs" value="N">否</logic:match>
   	<html:hidden name="elevatorTransferCaseRegisterBean" property="isxjs"/>
   	</td>
   	</tr>
   	<tr>
   	<td nowrap="nowrap" class="wordtd">安装公司名称：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="insCompanyName" size="40" styleClass="default_input"/><font color="red">*</font></td>
   	<td nowrap="nowrap" class="wordtd">安装公司联系人和电话：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="insLinkPhone" size="30" styleClass="default_input"/><font color="red">*</font></td>
   	<td nowrap="nowrap" class="wordtd">安装公司邮箱：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" size="25" property="insEmail" styleClass="default_input" /></td>
   	</tr>  
   <tr>
   	<td class="wordtd">所属部门：</td>
   	<td>
   		<bean:write name="departmentName"/>
       <html:hidden name="elevatorTransferCaseRegisterBean" property="department"/>
   	</td>
   	<td class="wordtd">厂检人员名称：</td>
   	<td>
   		<input name="username" id="username" readonly="readonly" class="default_input" value="${username}" />
   		<html:hidden name="elevatorTransferCaseRegisterBean" property="staffName" styleId="staffName" />
   		<input type="button" value=".." onclick="openWindowAndReturnValue2('searchStaffAction','')" class="default_input"/>
   		<font color="red">*</font>
   	</td>
   	<td class="wordtd">厂检人员联系电话：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="staffLinkPhone" styleId="staffLinkPhone" readonly="true" styleClass="default_input_noborder" /></td>         
   	</tr>
   	<tr>
   	<td nowrap="nowrap" class="wordtd">项目经理姓名及电话：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="projectManager" styleClass="default_input" /></td>
   	<td nowrap="nowrap" class="wordtd">调试人员姓名及电话：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="debugPers" styleClass="default_input" /></td>
    <td nowrap="nowrap"  class="wordtd">是否初次安装：</td>
   	<td>
   	<logic:equal name="elevatorTransferCaseRegisterBean" property="firstInstallation" value="Y">是</logic:equal>
   	<logic:equal name="elevatorTransferCaseRegisterBean" property="firstInstallation" value="N">否</logic:equal>
   	<html:hidden name="elevatorTransferCaseRegisterBean" property="firstInstallation"/>
   	</td>
   </tr>
  <tr>          
   	<td nowrap="nowrap" class="wordtd">厂检结果：</td>
   	<td >
   		<html:text name="elevatorTransferCaseRegisterBean" property="factoryCheckResult" readonly="true" styleClass="default_input_noborder" />
   	</td>
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
   	<td colspan="5">
   	<html:hidden name="elevatorTransferCaseRegisterBean" property="elevatorAddress" styleId="elevatorAddress" />
   	${elevatorTransferCaseRegisterBean.elevatorAddress }
   	</td> 
   	</tr>
   <tr>
   <td nowrap="nowrap" class="wordtd">录入人：</td>
   	<td><bean:write name="operName"/></td>
   	<td nowrap="nowrap" class="wordtd">录入日期：</td>
   	<td><bean:write name="operDate"/></td>
    <td nowrap="nowrap" class="wordtd"></td>
   	<td></td>
   </tr>
</table>
<br>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td nowrap="nowrap" class="wordtd">特殊要求：</td>
		<td>
			<table id="searchCompany" style="border: none;"  width="100%">
        		<%int i=1; %>
        		<logic:present name="specialRegister">
					 <tr>
					  <logic:iterate id="element" name="specialRegister">
						
							<td nowrap="nowrap"  style="border: none;" width="5%">
							<logic:match name="element" property="isOk" value="Y">
							<input type="checkbox" style="border-right-color: black;" name="isCheck" onclick="isOkCheck()" checked="checked">
							</logic:match>
                            <logic:match name="element" property="isOk" value="N">
                             <input type="checkbox" style="border-right-color: black;" name="isCheck" onclick="isOkCheck()">                                        
                            </logic:match>						
							</td>
							<td style="border: none;">
								<bean:write name="element" property="r1"/>
								<input type="hidden" name="isOk" value="${element.isOk }">
								<input type="hidden" name="scId" value="${element.scId}"/>
								<input type="hidden" name="scjnlno"/>
							</td>
								<% if(i%2==0){ %></tr><tr><%} %>			
								<%i++; %>
					</logic:iterate>
					</tr>
				</logic:present>
        	</table>
		</td>
	</tr>
</table>

<br/>
<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  <b>&nbsp;检查电梯问题</b>
</div>
<table id="party_a" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
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
			 <td align="center">${ele.pullname}</td>
			 <td align="center">${ele.checkItem}</td>
	         <td align="center">${ele.issueCoding}</td>
	         <td align="center">${ele.issueContents1}</td>
	         <td align="center">${ele.rem}</td>
	         <td align="center">&nbsp;</td>
      </tr>
      </logic:iterate>
    </logic:present>

</table>
  </html:form>
</body>


