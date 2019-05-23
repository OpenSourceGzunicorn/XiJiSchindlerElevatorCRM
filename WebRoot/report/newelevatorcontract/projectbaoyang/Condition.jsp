<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.HashMap"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>

<html:form action="/ProjectBaoYangAction.do?method=toSearchRecord">
	<html:hidden property="property(genReport)" styleId="genReport"/>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
		<tr>
			<td width="20%" height="28" class="wordtd">维保合同号:</td>
			<td width="80%"><html:text property="property(maintContractNo)" styleId="maintContractNo" /></td>
		</tr>
		<tr>
		<td width="20%" height="28" class="wordtd">收款日期:</td>
		<td width="80%">			
			<html:text property="property(startDate)" styleClass="Wdate" size="16" onfocus="WdatePicker({readOnly:true,isShowClear:true})"/>
			-
			<html:text property="property(endDate)" styleClass="Wdate" size="16" onfocus="WdatePicker({readOnly:true,isShowClear:true})"/>
		</td>
		</tr>
		<tr>
			<td width="20%" height="28" class="wordtd">甲方单位:</td>
			<td width="80%"><html:text property="property(companyName)"/></td>
		</tr>
		<tr>
			<td width="20%" height="28" class="wordtd">续保标志:</td>
			<td>
				<html:select property="property(contractStatus)">
					<html:option value=""><bean:message key="pageword.all"/></html:option>
					<html:option value="ZB">新签</html:option>
					<html:option value="XB">续保</html:option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td width="20%" height="28" class="wordtd">所属分部:</td>
			<td>
				<html:select property="property(maintDivision)">
					<html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
				</html:select>
			</td>
		</tr>
	</table>
</html:form>