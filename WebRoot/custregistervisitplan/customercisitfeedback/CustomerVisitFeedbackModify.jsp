<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<br>
<html:errors/>
<html:form action="/customerVisitFeedbackAction.do?method=toUpdateRecord">
	<html:hidden property="isreturn" />
	<html:hidden property="id" value="${customerVisitPlanDetailBean.jnlno}" />
	<table width="100%" class="tb">
		<table width="100%" class="tb">
	<tr>
		<td class="wordtd" >拜访人 ： </td>
			<td style="width: 26%">${cvpdbean.visitStaff}
			<td class="wordtd">职位 ：</td>
			<td >${visitPositionName}
		</td>
	</tr>
	</table>
	<br/>
	<table width="100%" class="tb">
		<tr>
			<td class="wordtd"><bean:message
					key="customerVisitPlan.customerlevel" /></td>
			<td style="width: 26%"><font><bean:write name="cvpmbean" property="custLevel"></bean:write></font>
                 <html:hidden property="jnlno" name="cvpdbean" styleId="jnlno"/>
			<td class="wordtd"><bean:message key="customerlevel.companyName" /></td>
			<td><bean:write name="cvpmbean" property="companyName"></bean:write>
			</td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message
					key="customerlevel.principalName" /></td>
			<td style="width: 26%"><bean:write name="cvpmbean" property="principalName"></bean:write></td>
			<td class="wordtd"><bean:message
					key="customerlevel.principalPhone" /></td>
			<td><bean:write name="cvpmbean" property="principalPhone"></bean:write></td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message
					key="customerlevel.maintDivision" /></td>
			<td style="width: 26%">${maintDivisionName}</td>
			<td class="wordtd"><bean:message key="customerlevel.mainStation" /></td>
			<td></font>${maintStationName}</td>
		</tr>
		<tr> 
			<td class="wordtd"><bean:message
					key="customerVisitPlan.visitDate" /></td>
			<td style="width: 26%"><bean:write name="cvpdbean" property="visitDate"></bean:write></td>
			<td class="wordtd"><bean:message key="customerVisitPlan.rem" /></td>
			<td><bean:write name="cvpdbean" property="rem"></bean:write></td>
		</tr>
		<tr> 
			<td class="wordtd">被转派职位</td>
			<td style="width: 26%">
				<logic:equal name="currvpt" value="20">
					<html:select property="visitPosition" styleId="visitPosition" onchange="isshow(this)">
				          <html:option value="22">维保站长</html:option>
				          <html:option value="100">服务销售专员</html:option>
			        </html:select>
				</logic:equal>
				<logic:notEqual name="currvpt" value="20">
					${cvpdbean.r1 }
					<html:hidden name="cvpdbean" property="visitPosition"/>
				</logic:notEqual>
			</td>
			<td class="wordtd">被转派人</td>
			<td>
			<html:select property="visitStaff" styleId="visitStaff">
				<html:options collection="loginuserList" property="userid" labelProperty="username" />
			</html:select>
			
			</td>
		</tr>
		<tr>
		<td class="wordtd">转派日期</td>
		<td><input type="hidden" name="transferDate" value="${toToday }"/>${toToday }</td>
		<td class="wordtd">转派原因</td>
		<td><textarea rows="2" cols="30" name="transfeRem"></textarea> </td>
		</tr>
		</table>
</html:form>
<script language="javascript">

function isshow(obj){
	var objval=obj.value;
	var visitStaff=document.getElementById("visitStaff");
	visitStaff.length=0;
	
	<logic:present name="loginuserList">
		<logic:iterate name="loginuserList" id="ele2">
			var class1id='<bean:write name="ele2" property="class1id"/>';
			if(objval==class1id){
				visitStaff.add(new Option('<bean:write name="ele2" property="username"/>','<bean:write name="ele2" property="userid"/>'));
			}
		</logic:iterate>
    </logic:present>
}

var visitPosition=document.getElementById("visitPosition");
isshow(visitPosition);

</script>


