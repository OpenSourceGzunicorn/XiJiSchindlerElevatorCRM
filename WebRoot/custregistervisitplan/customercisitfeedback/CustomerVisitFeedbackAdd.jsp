<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">

<br>
<html:errors/>
<html:form action="/customerVisitFeedbackAction.do?method=toAddRecord">
<html:hidden property="isreturn" />
<html:hidden property="id" value="${cvpdbean.jnlno}" />
	<table width="100%" class="tb">
	<tr>
		<td class="wordtd" >拜访人: </td>
			<td style="width: 26%"><bean:write name="userName"></bean:write></td>
			<td class="wordtd">职位 :</td>
			<td ><bean:write name="class1"></bean:write><input type="hidden" name="isSubmitType" id="isSubmitType"> 
		</td>
	</tr>
	</table>
	<br/>
	<table width="100%" class="tb">
		<tr>
			<td class="wordtd"><bean:message key="customerVisitPlan.customerlevel" />:</td>
			<td style="width: 26%">
				<bean:write name="cvpmbean" property="custLevel"/>
                <html:hidden property="jnlno" name="cvpdbean" styleId="jnlno"/>
            </td>
			<td class="wordtd"><bean:message key="customerlevel.companyName" />:</td>
			<td><bean:write name="cvpmbean" property="companyName"/>
			</td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="customerlevel.principalName" />:</td>
			<td style="width: 26%"><bean:write name="cvpmbean" property="principalName"/></td>
			<td class="wordtd"><bean:message key="customerlevel.principalPhone" />:</td>
			<td><bean:write name="cvpmbean" property="principalPhone"/></td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="customerlevel.maintDivision" />:</td>
			<td style="width: 26%"><bean:write name="cvpmbean" property="maintDivision"/></td>
			<td class="wordtd"><bean:message key="customerlevel.mainStation" />:</td>
			<td></font><bean:write name="cvpmbean" property="maintStation"/></td>
		</tr>
		<tr> 
			<td class="wordtd"><bean:message key="customerVisitPlan.visitDate" />:</td>
			<td style="width: 26%"><bean:write name="cvpdbean" property="visitDate"/></td>
			<td class="wordtd"><bean:message key="customerVisitPlan.rem" />:</td>
			<td>
				<logic:notEmpty name="cvpmbean" property="maintContractNo">
					<a href="<html:rewrite page='/maintContractAction.do'/>?method=toDisplayRecord&typejsp=Yes&maintcontractno=<bean:write name="cvpmbean"  property="maintContractNo"/>" target="_blnk"> 
						<bean:write name="cvpdbean" property="rem"/>
					</a>
				</logic:notEmpty>
				<logic:empty name="cvpmbean" property="maintContractNo">
					<bean:write name="cvpdbean" property="rem"/>
				</logic:empty>
			</td>
		</tr>
		<tr> 
			<td class="wordtd">驳回日期:</td>
			<td style="width: 26%">${cvpdbean.bhDate }</td>
			<td class="wordtd">驳回原因:</td>
			<td>${cvpdbean.bhRem }</td>
		</tr>
		</table>
		<br/>

<table width="100%" class="tb">
	<tr>
		<td class="wordtd" >被拜访人:</td>
		<td style="width: 26%"><html:text name="cvpdbean" property="visitPeople" styleId="visitPeople" styleClass="default_input"/><font color="red">*</font></td>
		<td class="wordtd">被拜访人电话:</td>
		<td ><html:text name="cvpdbean" property="visitPeoplePhone" styleId="visitPeoplePhone" styleClass="default_input"/><font color="red">*</font></td>
	</tr>
</table>
<br/>
<table id="dynamictable_0" class="dynamictable tb" width="100%" border="0" cellpadding="0" cellspacing="0">
  <thead>
    <tr height="23">
      <td colspan="4">
      <logic:empty name="cvpmbean" property="maintContractNo">          
        &nbsp;<input type="button" value=" + " onclick="addElevators('dynamictable_0')" class="default_input">
        <input type="button" value=" - " onclick="deleteRow('dynamictable_0')" class="default_input">
      </logic:empty> 
        <b>&nbsp;拜访项目信息</b>
      </td>
    </tr>
    <tr id="titleRow_0">
    <logic:empty name="cvpmbean" property="maintContractNo">
      <td class="wordtd_header"><input type="checkbox" name="cbAll" onclick="checkTableAll('dynamictable_0',this)"/></td>
    </logic:empty>
      <td class="wordtd_header">拜访项目<font color="red">*</font></td>
      <td class="wordtd_header">实际拜访日期<font color="red">*</font></td>
      <td class="wordtd_header">拜访内容/收获<font color="red">*</font></td>
    </tr>
  </thead>
  <tfoot>
    <tr height="23"><td colspan="4"></td></tr>
  </tfoot>
  <tbody>

    <tr id="sampleRow_0">
      <td align="center"><input type="checkbox" onclick="cancelCheckAll('dynamictable_0','cbAll')"/><input type="hidden" id="billNo" name="billNo"></td>
      <td align="center"><input type="text" name="projectName" id="projectName" class="noborder_center" readonly="readonly" size="50" /></td>
      <td align="center"><input type="text" name="realvisitDate" id="realvisitDate" value='<bean:write name="toToday"/>' size="12" class="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/></td>
      <td align="center"><textarea name="visitContent" id="visitContent" rows="3" cols="50"></textarea></td>
    </tr>

    <logic:present name="cvpfList">
    <logic:iterate id="ele" name="cvpfList">
    	<tr>
    		<logic:empty name="cvpmbean" property="maintContractNo">
    	    <td align="center"><input type="checkbox" onclick="cancelCheckAll('dynamictable_0','cbAll')"/></td>
    	    </logic:empty>
      		<td align="center"><input type="text" name="projectName" id="projectName" value="${ele.visitProject}" class="noborder_center" readonly="readonly" size="50" /></td>
      		<td align="center"><input type="text" name="realvisitDate" id="realvisitDate" value="${ele.realVisitDate}" size="12" class="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/></td>
      		<td align="center"><textarea name="visitContent" id="visitContent" rows="3" cols="50">${ele.visitContent }</textarea></td>
    	</tr>
    	</logic:iterate>
    </logic:present>

    <logic:present name="projectNameList2">
    	<logic:iterate id="projectName" name="projectNameList2">
    	<tr>
      		<td align="center"><input type="text" name="projectName" id="projectName" value="${projectName}" class="noborder_center" readonly="readonly" size="50" /></td>
      		<td align="center"><input type="text" name="realvisitDate" id="realvisitDate" value='<bean:write name="toToday"/>' size="12" class="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/></td>
      		<td align="center"><textarea name="visitContent" id="visitContent" rows="3" cols="50"></textarea></td>
    	</tr>
    	</logic:iterate>
    </logic:present>

  </tbody>    
</table>
<script type="text/javascript"> 
	window.onload=function() {
		initPage();  
	}
	
	function initPage(){	
		setDynamicTable("dynamictable_0","sampleRow_0");// 设置动态增删行表格
	}
</script>
</html:form>