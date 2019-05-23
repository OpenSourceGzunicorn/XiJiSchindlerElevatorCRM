<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/MaintManagerFollowAction.do?method=toUpdateRecord">
<html:hidden property="id" value="${cvpdbean.jnlno}" />
	<table width="100%" class="tb">
	<tr>
		<td class="wordtd" >拜访人 ： </td>
			<td style="width: 35%"><bean:write name="cvpdbean" property="visitStaff"></bean:write>
			<td class="wordtd">职位 ：</td>
			<td ><bean:write name="cvpdbean" property="visitPosition"></bean:write>
		</td>
	</tr>
	</table>
	<br/>
	<table width="100%" class="tb">
		<tr>
			<td class="wordtd"><bean:message
					key="customerVisitPlan.customerlevel" /></td>
			<td style="width: 35%"><font><bean:write name="cvpmbean" property="custLevel"></bean:write></font>
                 <html:hidden property="jnlno" name="cvpdbean" styleId="jnlno"/>
			<td class="wordtd"><bean:message key="customerlevel.companyName" /></td>
			<td><bean:write name="cvpmbean" property="companyName"></bean:write>
			</td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message
					key="customerlevel.principalName" /></td>
			<td style="width: 35%"><bean:write name="cvpmbean" property="principalName"></bean:write></td>
			<td class="wordtd"><bean:message
					key="customerlevel.principalPhone" /></td>
			<td><bean:write name="cvpmbean" property="principalPhone"></bean:write></td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message
					key="customerlevel.maintDivision" /></td>
			<td style="width: 35%"><bean:write name="cvpmbean" property="maintDivision"></bean:write></td>
			<td class="wordtd"><bean:message key="customerlevel.mainStation" /></td>
			<td></font><bean:write name="cvpmbean" property="maintStation"></bean:write></td>
		</tr>
		<tr> 
			<td class="wordtd"><bean:message
					key="customerVisitPlan.visitDate" /></td>
			<td style="width: 35%"><bean:write name="cvpdbean" property="visitDate"></bean:write>
			<input type="hidden" name="auditStatus" id="auditStatus">
			</td>
			<td class="wordtd"><bean:message key="customerVisitPlan.rem" /></td>
			<td><bean:write name="cvpdbean" property="rem"></bean:write></td>
		</tr>
		<tr><td class="wordtd">驳回日期</td>
			<td>${cvpdbean.bhDate }<input type="hidden" name="bhDate" id="bhDate" value="${cvpdbean.bhDate }"></td>
			<td class="wordtd">驳回原因</td>
			<td>${cvpdbean.bhRem }</td>
		</tr>
		</table>
		<br/>

<table width="100%" class="tb">
	<tr>
		<td class="wordtd" >被拜访人:</td>
		<td style="width: 35%"><bean:write name="cvpdbean" property="visitPeople"/></td>
		<td class="wordtd">被拜访人电话:</td>
		<td ><bean:write name="cvpdbean" property="visitPeoplePhone"/></td>
	</tr>
</table>
<br/>

<table id="dynamictable_0" class="dynamictable tb" width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr height="23">
      <td colspan="3">            
        <b>拜访项目信息</b>
      </td>
    </tr>
    <tr id="titleRow_0">
      <td class="wordtd_header">拜访项目</font></td>
      <td class="wordtd_header">实际拜访日期</font></td>
      <td class="wordtd_header">拜访内容/收获</font></td>
    </tr>
  
  <logic:present name="cvpfList">
    <logic:iterate id="ele" name="cvpfList">
    <tr>
    <td><bean:write name="ele" property="visitProject"/></td>
    <td><bean:write name="ele" property="realVisitDate"/></td>
    <td><bean:write name="ele" property="visitContent"/></td>
    </tr>
    </logic:iterate>
  </logic:present>
  <logic:notPresent name="cvpfList">
  <td colspan="3" style="text-align: center;">            
        没有记录！
      </td>
  </logic:notPresent>
</table>
<br>
<table class="dynamictable tb" width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr height="23">
      <td colspan="5">            
        <b>客户转派信息</b>
      </td>
    </tr>
    <tr>
      <td class="wordtd_header">转派人</td>
      <td class="wordtd_header">转派日期</td>
      <td class="wordtd_header">被转派人职位</td>
      <td class="wordtd_header">被转派人</td>
      <td class="wordtd_header">转派备注</td>
    </tr>
  <logic:present name="cvdList">
    <logic:iterate id="cvd" name="cvdList">
    <tr>
    <td><bean:write name="cvd" property="transferId"/></td>
    <td><bean:write name="cvd" property="transferDate"/></td>
    <td><bean:write name="cvd" property="visitPosition"/></td>
    <td><bean:write name="cvd" property="visitStaff"/></td>
    <td><bean:write name="cvd" property="transfeRem"/></td>
    </tr>
    </logic:iterate>
  </logic:present>
  <logic:notPresent name="cvdList">
  <td colspan="5" style="text-align: center;">            
        没有记录！ </td>
  </logic:notPresent>

</table>
<br>
<table class="dynamictable tb" width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
		    <td class="wordtd">审核人</td>
			<td style="width: 35%">${cvpdbean.auditOperid }</td> 
			<td class="wordtd">审核日期</td>
			<td style="width: 35%">${cvpdbean.auditDate } </td>
		</tr>
		<tr><td class="wordtd">审核意见</td>
			<td colspan="3">${cvpdbean.auditRem }</td>
		</tr>
</table>
<br>
<table class="dynamictable tb" width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
		    <td class="wordtd">维保经理跟进人</td>
			<td style="width: 35%">${cvpdbean.managerFollow}</td> 
			<td class="wordtd">维保经理跟进日期</td>
			<td style="width: 35%">${cvpdbean.managerFollowDate}<input type="hidden" name="managerFollowDate" id="managerFollowDate" value="${cvpdbean.managerFollowDate}"></td>
		</tr>
		<tr><td class="wordtd">维保经理跟进办法</td>
			<td colspan="3"><textarea rows="2" cols="80" name="managerFollowRem" id="managerFollowRem"></textarea></td>
		</tr>
				
</table>
<br>
<table class="dynamictable tb" width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
		    <td class="wordtd">维保分部长跟进人</td>
			<td style="width: 35%">${cvpdbean.fMinisterFollow}</td> 
			<td class="wordtd">维保分部长跟进日期</td>
			<td style="width: 35%">${cvpdbean.fMinisterFollowDate}</td>
		</tr>
		<tr><td class="wordtd">维保分部长跟进办法</td>
			<td colspan="3">${cvpdbean.fMinisterFollowRem }</textarea></td>
		</tr>
				
</table>
<br>
<table class="dynamictable tb" width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
		    <td class="wordtd">维保总部长跟进人</td>
			<td style="width: 35%">${cvpdbean.zMinisterFollow }</td> 
			<td class="wordtd">维保总部长跟进日期</td>
			<td style="width: 35%">${cvpdbean.zMinisterFollowDate }</td>
		</tr>
		<tr><td class="wordtd">维保总部长跟进办法</td>
			<td colspan="3">${cvpdbean.zMinisterFollowRem}</textarea></td>
		</tr>
				
</table>

</html:form>

