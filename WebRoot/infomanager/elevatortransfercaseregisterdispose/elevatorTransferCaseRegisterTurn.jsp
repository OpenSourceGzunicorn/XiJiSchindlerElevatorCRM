<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/elevatorTransferCaseRegisterAction.do?method=toDisposeTurnRecord">
<html:hidden property="id"/>
<html:hidden name="elevatorTransferCaseRegisterBean" property="billno"/>
<html:hidden property="processStatus"/>
<html:hidden property="isreturn"/>
<html:hidden property="hecirs" />

  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
   <td nowrap="nowrap" class="wordtd"  >合同类型：</td>
   	<td>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="contractType" value="XS">销售合同</logic:match>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="contractType" value="WG">维改合同</logic:match>
   	</td>
   	<td nowrap="nowrap" class="wordtd" >厂检时间：</td>
   	<td><bean:write name="elevatorTransferCaseRegisterBean" property="checkTime"/></td>
   	<td nowrap="nowrap" class="wordtd" >厂检次数：</td>
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
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="projectAddress" styleId="projectAddress" readonly="true" styleClass="default_input_noborder" size="50" /></td>
   	<td nowrap="nowrap" class="wordtd">电梯类型：</td>
   	<td><logic:match name="elevatorTransferCaseRegisterBean" property="elevatorType" value="T">直梯</logic:match>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="elevatorType" value="F">扶梯</logic:match>
   	</td>
   	<td nowrap="nowrap" class="wordtd">规格型号：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="elevatorParam" styleId="elevatorParam" readonly="true" styleClass="default_input_noborder" /></td>
   </tr>
   <tr>
   	<td class="wordtd">合同性质：</td>
   <td>
   	<html:text name="elevatorTransferCaseRegisterBean" property="salesContractType" styleId="salesContractType" readonly="true" styleClass="default_input_noborder" /></td>
   	<td nowrap="nowrap" class="wordtd">层/站/门：</td>
   	<td><%-- <input type="text" id="fsd" class="default_input_noborder" value="${elevatorTransferCaseRegisterBean.r1}" readonly="true"/> --%>
   	<bean:write name="elevatorTransferCaseRegisterBean" property="floor" />/<bean:write name="elevatorTransferCaseRegisterBean" property="stage" />/<bean:write name="elevatorTransferCaseRegisterBean" property="door" />
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
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="insCompanyName" size="40" styleClass="default_input_noborder"  /></td>
   	<td nowrap="nowrap" class="wordtd">安装公司联系人和电话：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean"  size="25" property="insLinkPhone" styleClass="default_input_noborder"  /></td>
   	</tr>  
   <tr>
   <td nowrap="nowrap" class="wordtd">所属部门：</td>
   	<td>
   		<html:text name="elevatorTransferCaseRegisterBean" property="department" styleClass="default_input_noborder" />
   	</td>
   <td nowrap="nowrap" class="wordtd">厂检人员名称：</td>
   	<td>
   		<input name="username" id="username" readonly="readonly" class="default_input" value="${username}" />
   		<html:hidden name="elevatorTransferCaseRegisterBean" property="staffName" styleId="staffName" />
   		<input type="button" value=".." onclick="openWindowAndReturnValue2('searchStaffAction','')" class="default_input"/>
   		<font color="red">*</font>
   	</td>
   	<td nowrap="nowrap" class="wordtd">厂检人员联系电话：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="staffLinkPhone" styleId="staffLinkPhone" readonly="true" styleClass="default_input_noborder" /></td>        
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
   	<td ><html:text name="elevatorTransferCaseRegisterBean" property="factoryCheckResult" readonly="true" styleClass="default_input_noborder"></html:text></td>
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
   	
   <%--  <td nowrap="nowrap" class="wordtd">特殊要求：</td>
   	<td colspan="3"><table id="searchCompany" style="border: none;" class="tb">
        		<%int searchno=1; %>
        		<logic:present name="specialRegister">
        		<tr>
					  <logic:iterate id="element" name="specialRegister">
							<td style="border: none;" width="5%">
							<logic:match name="element" property="isOk" value="Y">
							<input type="checkbox" disabled="disabled" checked="checked">
							</logic:match>
                            <logic:match name="element" property="isOk" value="N">
                             <input type="checkbox" disabled="disabled" >                                        
                            </logic:match></td>
							<td style="border: none;">
								<bean:write name="element" property="r1"/>
								<input type="hidden" name="scId" value="${element.scId }"/>
								<input type="hidden" name="scjnlno" value="${element.jnlno }"/>
							</td>
						<% if(searchno%2==0){ %></tr><tr><%} %>
						<%searchno++; %>
					</logic:iterate>
					</tr>
				</logic:present>
        	</table>
        	</td> --%> 
  </tr>
  
   	<tr>
   	<td nowrap="nowrap" class="wordtd">电梯位置：</td>
   	<td colspan="5">${elevatorTransferCaseRegisterBean.elevatorAddress }</td> 
   	</tr>
  <tr>
   <td nowrap="nowrap" class="wordtd">录入人：</td>
   	<td><bean:write name="elevatorTransferCaseRegisterBean" property="operId"/></td>
   	<td nowrap="nowrap" class="wordtd">录入日期：</td>
   	<td><bean:write name="elevatorTransferCaseRegisterBean" property="operDate"/></td>
   	<td nowrap="nowrap" class="wordtd">接收日期：</td>
   	<td><bean:write name="elevatorTransferCaseRegisterBean" property="receiveDate"/></td>
   </tr>
	<tr>
		<td nowrap="nowrap" class="wordtd">转派人：</td>
		<td><bean:write name="elevatorTransferCaseRegisterBean" property="transferId"/></td>
		<td nowrap="nowrap" class="wordtd">转派日期：</td>
		<td><%-- bean:write name="elevatorTransferCaseRegisterBean" property="transferDate"/--%>
			<html:text name="elevatorTransferCaseRegisterBean" property="transferDate" size="23" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"></html:text>
		</td>
		<td nowrap="nowrap" class="wordtd"></td>
		<td></td>
	</tr>
</table>
<br>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td nowrap="nowrap" class="wordtd">特殊要求：</td>
		<td>
			<table id="searchCompany" style="border: none;" class="tb">
        		<%int searchno=1; %>
        		<logic:present name="specialRegister">
        		<tr>
					  <logic:iterate id="element" name="specialRegister">
							<td style="border: none;" width="5%">
							<logic:match name="element" property="isOk" value="Y">
							<input type="checkbox" disabled="disabled" checked="checked">
							</logic:match>
                            <logic:match name="element" property="isOk" value="N">
                             <input type="checkbox" disabled="disabled" >                                        
                            </logic:match></td>
							<td style="border: none;">
								<bean:write name="element" property="r1"/>
								<input type="hidden" name="scId" value="${element.scId }"/>
								<input type="hidden" name="scjnlno" value="${element.jnlno }"/>
							</td>
						<% if(searchno%4==0){ %></tr><tr><%} %>
						<%searchno++; %>
					</logic:iterate>
					</tr>
				</logic:present>
        	</table>
		</td>
	</tr>
</table>

<br>
<% int i=0; int j=0;%>
 <div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  &nbsp;<b>&nbsp;检查电梯问题</b>
</div>
<table id="important" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="5%"><input type="checkbox" name="cbAll" onclick="checkTableAll('important',this)"/></td>
			<td width="10%">检查类型</td>
			<td width="8%">检查项目</td>
			<td width="8%">问题编号</td>
			<td width="35%">问题内容</td>
			<td width="29%">备注</td>
			<td width="5%">附件</td>
		</tr>
	</thead>
	<tbody>
		<logic:present name="hecirList">
          <logic:iterate id="element1" name="hecirList" >
          	<tr id="party_1" name="party_1">
          	 <td align="center"><input type="checkbox" onclick="cancelCheckAll('important','cbAll')"/></td>
          	 <td>${element1.examTypeName}</td>
	         <td align="center">${element1.checkItem}</td>
	         <td align="center">${element1.issueCoding}</td>
	         <td>${element1.issueContents1}</td>
	         <td>${element1.issueContents}</td>
	         <td align="center">
	         	<%i++;%>
	         	<logic:notEmpty name="element1" property="appendix">
				    <logic:notEqual value="" name="element1" property="appendix">
				    <span>
				      	<a style="cursor:hand;text-decoration: underline;color: blue;" id="mtapp_<%=j %>" onclick="downloadFile('${element1.appendix}')"><bean:message key="qualitycheckmanagement.check"/></a>
				    </span>
				    </logic:notEqual>
				</logic:notEmpty>
				<%j++; %>
	         </td>
	        </tr>
          </logic:iterate>
        </logic:present>
	</tbody>
	<input type="hidden" name="file_length" value="<%=i %>" />
</table> 
<div id="caption_1" style="width: 100%;padding-top: 0;padding-bottom: 2;border-top:0 none #ffffff;border-bottom: 1 solid #999999;" class="tb">
</div>

<script type="text/javascript">
	/* setDynamicTable("important","important_0");// 设置动态增删行表格 */
	
</script> 

</html:form>