<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite page="/common/css/jquery.bigautocomplete.css"/>">
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery.bigautocomplete.js"/>"></script>
<br>
<head>
  <title>XJSCRM</title>
</head>

  <html:errors/>
  <html:form action="/elevatorTransferCaseRegisterManageAction.do?method=toAddRecord">
    <%-- <%@ include file="addOrModify.jsp" %> --%>
<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden name="elevatorTransferCaseRegisterBean" property="billno"/>
<html:hidden property="submitType" />
<html:hidden property="hecirs" />
<html:hidden property="nextId" />
<input type="hidden" name="historyBillNo" id="historyBillNo">

<!-- <div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">

</div> -->
<!--   <script type="text/javascript">
  $(document).ready(function(){
	  var t2=document.getElementById("userList").value;
      var data=eval(t2);  
	  $("#username").bigAutocomplete({data:data,showname:"username",callback:function(data){
		  document.getElementById("staffName").value=data.userid;
		}});
  });
</script> -->
<table id="party_a" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr height="23">
			<td colspan="13">
				&nbsp;&nbsp;<input type="button" name="toaddrow" value=" + " onclick="addElevator1('searchElevatorTransferCaseRegisterAction',this)"/>
				&nbsp;<input type="button" name="todelrow" value=" - " onclick="deleteRow(this)">
  				<b>&nbsp;检查电梯</b>
			</td>
		</tr>
		<tr class="wordtd_header">
			<td width="5"><input type="checkbox" onclick="checkTableAll(this)"></td>
			<td>电梯编号</td>
			<td>合同号</td>
			<td>项目名称</td>
			<td>项目省份<font color='red'>*</font></td>
			<td>项目地址</td>
			<td>电梯类型</td>
			<td>规格型号</td>
			<td>合同性质</td>
			<td>层/站/门</td>
			<td>提升高度</td>
			<td>载重</td>
			<td>额定速度</td>
		</tr>
	</thead>
	<tbody>
		<tr height="15"><td colspan="13"></td></tr>
	</tbody>
	<!-- <tfoot>
      <tr height="15"><td colspan="12"></td></tr>
    </tfoot> -->
</table>

<br/>

<table width="100%"  class="tb">
   <tr>
   <td nowrap="nowrap" class="wordtd"  >合同类型：</td>
   	<td width="15%"><html:select name="elevatorTransferCaseRegisterBean" property="contractType" onchange="cleanElevator1()">
   		    <html:option value="XS">销售合同</html:option>
   		    <html:option value="WG">维改合同</html:option>
   		</html:select>
   	</td>
   	<td nowrap="nowrap" class="wordtd" >厂检时间：</td>
   	<td></td>
   	<td nowrap="nowrap" class="wordtd" >厂检次数：</td>
   	<td nowrap="nowrap" ><html:text style="width: 200px" name="elevatorTransferCaseRegisterBean" property="checkNum" readonly="true" styleClass="default_input_noborder" /></td>   
   </tr>
   <%-- <tr>
   	<td nowrap="nowrap" class="wordtd">电梯编号：</td>
   	<td>
   		<html:text name="elevatorTransferCaseRegisterBean" property="elevatorNo" styleId="elevatorNo" readonly="true" styleClass="default_input" />
   		<input type="button" value=".." onclick="toElevator('searchElevatorTransferCaseRegisterAction')" class="default_input"/><font color="red">*</font>
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
   	<td>  <input type="text" id="elevatorTypeName" class="default_input_noborder" value="${elevatorTransferCaseRegisterBean.elevatorType}" readonly="true"/>
   	      <html:hidden name="elevatorTransferCaseRegisterBean" property="elevatorType" styleId="elevatorType"/></td>
   	<td nowrap="nowrap" class="wordtd">规格型号：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="elevatorParam" styleId="elevatorParam" readonly="true" styleClass="default_input_noborder" /></td>
   </tr>
   <tr>
   	<td class="wordtd">合同性质：</td>
   <td>
   	<html:text name="elevatorTransferCaseRegisterBean" property="salesContractType" styleId="salesContractType1" readonly="true" styleClass="default_input_noborder" /></td>
   	<td nowrap="nowrap" class="wordtd">层/站/门：</td>
   	<td><input type="text" id="fsd" class="default_input_noborder" value="${elevatorTransferCaseRegisterBean.r1}" readonly="true"/>
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
   	<td nowrap="nowrap" class="wordtd"></td>
   	<td></td>
   </tr>   --%> 
   <tr>
   	<td nowrap="nowrap"  class="wordtd">是否迅达安装：</td>
   	<td colspan="5"><html:select name="elevatorTransferCaseRegisterBean" property="isxjs" styleId="isxjs" onchange="toinsCompanyName()">
   	<html:option value="">请选择</html:option>
   	<html:option value="Y">是</html:option>
   	<html:option value="N">否</html:option>
   	</html:select>
   	<font color="red">*</font>
   	</td>
   	</tr>
   	<tr>
   	<td nowrap="nowrap" class="wordtd">安装公司名称：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="insCompanyName" styleId="insCompanyName" styleClass="default_input" size="40" /><font color="red">*</font></td>
   	<td nowrap="nowrap" class="wordtd">安装公司联系人和电话：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" size="25" property="insLinkPhone" styleClass="default_input" /><font color="red">*</font></td>
   	<td nowrap="nowrap" class="wordtd">安装公司邮箱：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" size="25" property="insEmail" styleClass="default_input" /></td>
   	</tr>  
   <tr>
   <td nowrap="nowrap" class="wordtd">所属部门：</td>
   	<td>
   	     
   		<logic:present name="departname">
   		<input name="departname" id="departname" readonly="readonly" class="default_input_noborder" value="${departname}" />
   		<html:hidden name="elevatorTransferCaseRegisterBean" property="department" />
   		</logic:present>
   		<logic:notPresent name="departname">
   		<html:select name="elevatorTransferCaseRegisterBean" property="department">
        	<html:option value="">全部</html:option>
		  <html:options collection="departmentList" property="comid" labelProperty="comname"/>
        </html:select>
        <font color="red">*</font>
   		</logic:notPresent>
   	</td>
   <td nowrap="nowrap" class="wordtd">厂检人员名称：</td>
   	<td>
   		<input type="hidden" name="userList" value=${userList }>
   		<html:hidden name="elevatorTransferCaseRegisterBean" property="staffName" styleId="staffName"/>
   		<input name="username" id="username" class="default_input" value="${username}" readonly="readonly" />
   		<input type="button" value=".." onclick="openWindowAndReturnValue2('searchStaffAction','')" class="default_input"/>
   		<font color="red">*</font>
   	</td>
   	<td nowrap="nowrap" class="wordtd">厂检人员联系电话：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="staffLinkPhone" styleId="staffLinkPhone" readonly="true" styleClass="default_input_noborder" /></td>        
   </tr>
   <tr>
   	<td nowrap="nowrap" class="wordtd">项目经理姓名及电话：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="projectManager" size="25" styleClass="default_input" /></td>
   	<td nowrap="nowrap" class="wordtd">调试人员姓名及电话：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="debugPers" size="25" styleClass="default_input" /></td>
    <td nowrap="nowrap" class="wordtd">是否初次安装：</td>
   	<td>
   		<html:select name="elevatorTransferCaseRegisterBean" property="firstInstallation">
   		    <html:option value="">请选择</html:option>
   		    <html:option value="Y">是</html:option>
   		    <html:option value="N">否</html:option>
   		</html:select>
   		<font color="red">*</font>
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
        	<html:textarea name="elevatorTransferCaseRegisterBean" property="specialClaim" rows="2" cols="100">
        	
        	<table id="searchCompany" style="border: 0;margin: 0;" width="350" class="tb">
        	<thead>
        		<tr>
        			<td colspan="2">
        				&nbsp;&nbsp;<input type="button" name="toaddrow" value="+" onclick="addElevators('searchSpecialRegisterAction',this)"/>
						&nbsp;<input type="button" name="todelrow" value="-" onclick="deleteRow(this)">
        			</td>
        		</tr>
        		<tr class="wordtd">
        			<td width="5%"><input type="checkbox" onclick="checkTableAll(this)"></td>
        			<td align="left">
        				特殊要求
        			</td>
        		</tr>
        		</thead>
        		<tfoot>
                  <tr height="15"><td colspan="2"></td></tr>
                </tfoot>
             <tbody>
        		<logic:present name="specialRegister">
					  <logic:iterate id="element" name="specialRegister">
						<tr>
							<td><input type="checkbox" onclick="cancelCheckAll(this)"/></td>
							<td>
								<bean:write name="element" property="r1"/>
								
								<logic:present name="departname">
						   		<input type="hidden" name="scId" value="${element.scId }"/>
						          <input type="hidden" name="scjnlno" />
						   		</logic:present>
						   		<logic:notPresent name="departname">
						          <input type="hidden" name="scId" value="${element.scId }"/>
						          <input type="hidden" name="scjnlno" value="${element.jnlno }"/>
						   		</logic:notPresent>
							</td>
						</tr>
					</logic:iterate>
				</logic:present>
				
				</tbody>
        	</table>
        <!-- 	<div id="caption_1" style="width: 350;padding-top: 0;padding-bottom: 2;border-top:0 none #ffffff;border-bottom: 1 solid #999999;" class="tb">
			</div> -->
        	<html:text name="advisoryComplaintsManageBean" property="dutyDepar" styleClass="default_input"></html:text>
        </td> 	
   	
   	
   </tr> --%>
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

<table id="searchCompany" style="border: 0;margin: 0;" width="100%" class="tb">
        	<thead>
        		<tr>
        			<td colspan="2">
        				&nbsp;&nbsp;<input type="button" name="toaddrow" value=" + " onclick="addElevators('searchSpecialRegisterAction',this)"/>
						&nbsp;<input type="button" name="todelrow" value=" - " onclick="deleteRow(this)">
        			</td>
        		</tr>
        		<tr class="wordtd">
        			<td width="2%"><input type="checkbox" onclick="checkTableAll(this)"></td>
        			<td align="left">
        				特殊要求
        			</td>
        		</tr>
        		</thead>
        		<tfoot>
                  <tr height="15"><td colspan="2"></td></tr>
                </tfoot>
             <tbody>
        		<logic:present name="specialRegister">
					  <logic:iterate id="element" name="specialRegister">
						<tr>
							<td><input type="checkbox" onclick="cancelCheckAll(this)"/></td>
							<td>
								<bean:write name="element" property="r1"/>
								
								<logic:present name="departname">
						   		<input type="hidden" name="scId" value="${element.scId }"/>
						          <input type="hidden" name="scjnlno" />
						   		</logic:present>
						   		<logic:notPresent name="departname">
						          <input type="hidden" name="scId" value="${element.scId }"/>
						          <input type="hidden" name="scjnlno" value="${element.jnlno }"/>
						   		</logic:notPresent>
							</td>
						</tr>
					</logic:iterate>
				</logic:present>
				
				</tbody>
        	</table>
<br/>

<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  <b>&nbsp;检查电梯问题</b>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
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
</table>
<br/>
  </html:form>
