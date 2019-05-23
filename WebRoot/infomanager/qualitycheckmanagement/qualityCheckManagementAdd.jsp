<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<br>
<html:errors />
<html:form action="/qualityCheckManagementAction.do?method=toAddRecord">
	<html:hidden property="isreturn" />
	<html:hidden property="issubmit"/>
	<table id="party_a" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr height="23">
			<td colspan="12">
				&nbsp;&nbsp;<input type="button" name="toaddrow" value=" + " onclick="addElevator1('searchMaintContractMasterAction',this)"/>
				&nbsp;<input type="button" name="todelrow" value=" - " onclick="deleteRow(this)">
  				<b>&nbsp;检查电梯</b>
			</td>
		</tr>
		<tr class="wordtd_header">
			<td width="5"><input type="checkbox" onclick="checkTableAll(this)"></td>
			<td>电梯编号</td>
			<td>电梯类型</td>
			<td>维保合同号</td>
			<td>销售合同号</td>
			<td>项目名称</td>
			<td>维保分部</td>
			<td>维保站</td>
			<td>维保工</td>
			<td>维保工联系电话</td>
			<td>备注</td>
		</tr>
	</thead>
	<tbody>
		<tr height="15"><td colspan="12"></td></tr>
	</tbody>
	<!-- <tfoot>
      <tr height="15"><td colspan="12"></td></tr>
    </tfoot> -->
</table>
	<br>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
			
			<td class="wordtd">现场督查人员:</td>
			<td width="20%">
				<input name="username" id="username" readonly="readonly" class="default_input" value="${username}" />
				<html:hidden property="superviseId" styleId="superviseId"/>
			 	<input type="button" value=".." onclick="openWindowAndReturnValue3('searchStaffAction','toSearchRecord2','')" class="default_input" />
			 	<font color="red">*</font>
			</td>
			<td class="wordtd">督查人员联系电话:</td>
			<td width="20%"><html:text property="supervisePhone" styleId="supervisePhone" style="border:0;" styleClass="default_input" readonly="true" /></td>
		
			<td class="wordtd"><bean:message key="qualitycheckmanagement.TotalPoints"/>:</td>
			<td></td>
			</tr>
			<tr>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.ScoreLevel"/>:</td>
			<td></td>
		
		
			<td class="wordtd"><bean:message key="qualitycheckmanagement.ChecksDateTime"/>:</td>
			<td></td>
			<td class="wordtd"></td>
			<td></td>
		</tr> 
    </table>
    <br>
    <%@ include file="addOrModify.jsp" %>
    <br>
    
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
        <td class="wordtd"><bean:message key="qualitycheckmanagement.SupervOpinion"/>:</td>
        <td></td>
      </tr>
    </table>
    <br>
</html:form>
<html:javascript formName="qualityCheckManagementForm" />