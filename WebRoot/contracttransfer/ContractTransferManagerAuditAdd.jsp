<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<html:errors />
<br>
<html:form action="/ContractTransferManagerAuditAction.do?method=toAddRecord" >
<html:hidden property="isreturn" value="N"/>
<html:hidden property="billNo" value="${billNo}"/>
<html:hidden property="id"/>
<html:hidden property="auditStatus2"/>

<%@ include file="display.jsp" %>

<logic:equal name="addflag" value="Y">
<br/>
<table width="100%" border="0" cellpadding="1" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd">经理文员审核人：</td>
		<td>${auditOperid2}</td>
	</tr>
	<tr>
		<td class="wordtd">经理文员审核日期：</td>
		<td>${auditDate2}</td>
	</tr>
	<tr>
		<td class="wordtd">经理文员审核意见：</td>
		<td>
		<html:textarea property="auditRem2" rows="3" cols="62"></html:textarea>
		</td>
	</tr>
</table>
</logic:equal>
</html:form>

