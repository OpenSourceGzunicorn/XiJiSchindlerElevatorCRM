<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<script type="text/javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<br>
<html:errors/>
<html:form action="/elevatorTransferCaseRegisterAction.do?method=toDisplayRecord">
<style>
  .show{display:block;}
  .hide{display:none;}

</style>

<style type="text/css">
		*{
			font-family: SongTi_GB2312;
		}
		.divtable table{border-collapse: collapse;border: 1px outset #999999;background-color: #FFFFFF;}
        .divtable table td{font-size: 12px;border: 1px outset #999999;}
</style>
<logic:present name="elevatorTransferCaseRegisterBean">
<html:hidden name="elevatorTransferCaseRegisterBean" property="billno"/>
    <h2 style="text-align: center;">整改通知单</h2>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
   <td nowrap="nowrap" class="wordtd" >合同类型：</td>
   	<td>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="contractType" value="XS">销售合同</logic:match>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="contractType" value="WG">维改合同</logic:match>
   	<input type="hidden" id="pdfstatus" value='<bean:write name="elevatorTransferCaseRegisterBean" property="processStatus"/>'>
   	</td>
   	<td nowrap="nowrap" class="wordtd" >厂检时间：</td>
   	<td><bean:write name="elevatorTransferCaseRegisterBean" property="checkTime" /></td>
   	<td nowrap="nowrap" class="wordtd" >厂检次数：</td>
   	<td nowrap="nowrap" ><html:text style="width: 200px" name="elevatorTransferCaseRegisterBean" property="checkNum" readonly="true" styleClass="default_input_noborder" /></td>   
   </tr>
   <tr>
   	<td nowrap="nowrap" class="wordtd">电梯编号：</td>
   	<td>
   		<html:text name="elevatorTransferCaseRegisterBean"  property="elevatorNo" styleId="elevatorNo" readonly="true" styleClass="default_input_noborder" />
   	</td>
   	<td nowrap="nowrap" class="wordtd">合同号：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean" property="salesContractNo" styleId="salesContractNo" readonly="true" styleClass="default_input_noborder" /></td>
   
   	<td nowrap="nowrap" class="wordtd">项目名称：</td>
   	<td>
   	${elevatorTransferCaseRegisterBean.projectName }
   	<%-- html:text name="elevatorTransferCaseRegisterBean" property="projectName" styleId="projectName" readonly="true" styleClass="default_input_noborder" /--%>
   	</td>
   </tr>
   <tr>
   	<td nowrap="nowrap" class="wordtd">项目地址：</td>
   	<td>
   	${elevatorTransferCaseRegisterBean.projectAddress }
   	<%-- html:text name="elevatorTransferCaseRegisterBean"  property="projectAddress" styleId="projectAddress" readonly="true" styleClass="default_input_noborder" size="50" /--%>
   	</td>
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
   	<td>${elevatorTransferCaseRegisterBean.r1}
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
   </tr>   
   <tr>
   	<td nowrap="nowrap"  class="wordtd">是否迅达安装：</td>
   	<td>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="isxjs" value="Y">是</logic:match>
   	<logic:match name="elevatorTransferCaseRegisterBean" property="isxjs" value="N">否</logic:match>
   	</td>
   	
   	<td nowrap="nowrap" class="wordtd">安装公司名称：</td>
   	<td>
   	${elevatorTransferCaseRegisterBean.insCompanyName }
   	<%-- html:text name="elevatorTransferCaseRegisterBean" property="insCompanyName" readonly="true" size="40" styleClass="default_input_noborder"  /--%>
   	</td>
   	<td nowrap="nowrap" class="wordtd">安装公司联系人和电话：</td>
   	<td><html:text name="elevatorTransferCaseRegisterBean"  size="25" property="insLinkPhone" readonly="true" styleClass="default_input_noborder"  /></td>
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
   	
   	<%-- <td nowrap="nowrap" class="wordtd">特殊要求：</td>
   	<td colspan="3">
   	<table id="searchCompany" style="border: none;">
              	<%int i=1; %>
        		<logic:present name="specialRegister">
					 <tr>
					  <logic:iterate id="element" name="specialRegister" >
						
							<td nowrap="nowrap"  style="width: 5%;border: none;">
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
						<% if(i%2==0){ %></tr><tr><%} %>
						<%i++; %>
					</logic:iterate>
					</tr>
				</logic:present>
        	</table></td> --%> 
   	</tr>
</table>
<br>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td nowrap="nowrap" class="wordtd">特殊要求：</td>
		<td>
			<table id="searchCompany" style="border: none;">
              	<%int i=1; %>
        		<logic:present name="specialRegister">
					 <tr>
					  <logic:iterate id="element" name="specialRegister" >
						
							<td nowrap="nowrap"  style="width: 5%;border: none;">
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
			<td width="15%">检查类型</td>
			<td width="15%">检查项目</td>
			<td width="10%">问题编号</td>
			<td width="40%">问题内容</td>
			<td width="20%">备注</td>
		</tr>
	</thead>
	<tfoot>
      <tr height="15"><td colspan="6"></td></tr>
    </tfoot>
    <logic:present name="hecirList">
    <logic:iterate id="ele" name="hecirList">
      <tr>
			 <td >${ele.examType}</td>
			 <td >${ele.checkItem}</td>
	         <td >${ele.issueCoding}</td>
	         <td >${ele.issueContents1}</td>
	         <td >${ele.issueContents}</td>
      </tr>
      </logic:iterate>
    </logic:present>
</table>
</logic:present>
</html:form>
<script>
	/**
	*页面加载完就保存html文件
	*/
	$(function(){
		var url1 = '<html:rewrite page="/elevatorTransferCaseRegisterManageAction.do"/>';
		 var test=document.getElementsByTagName('html')[0].innerHTML; 
		 var top=$("table.top_navigation").parent().html();	
		 var tabToolBar=$("td.table_outline3").html();
		 	$.ajax({
					url : url1,// 要请求的action
				data : {
					method : "createPdfFile",
					test : test,
					top : top,
					tabToolBar : tabToolBar,
				    htmlName : "ElevatorTransferCaseRegister.html"
				},// 请求的参数和方法
				type : "POST",
				dataType : "text",// 是什么数据类型
				async : "false",// 是否异步请求，如果是异步，则服务器不会等待这个方法返回就执行完了这个函数
				cache : "false",// 是否进行缓存
				contentType: "application/x-www-form-urlencoded; charset=utf-8", 
				success : function(arr) {

				},
				error: function(){
					
				}
			});
	})
</script>