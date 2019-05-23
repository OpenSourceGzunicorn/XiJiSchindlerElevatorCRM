
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/common/javascript/highcharts/highcharts.js"/>"></script>
	<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>

<br>
<html:form action="/runReportAction.do?method=toSearchResults">

    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
     <tr>
	    <td width="20%" class="wordtd">驳回日期:</td>
	    <td width="80%" class="inputtd">
	    <html:text property="sdate1" styleId="sdate1" styleClass="Wdate" size="16" onfocus="WdatePicker({readOnly:true,isShowClear:true});"></html:text>
		- 
		<html:text property="edate1" styleId="edate1" styleClass="Wdate" size="16" onfocus="WdatePicker({readOnly:true,isShowClear:true})"></html:text>
	    <html:hidden property="genReport" styleId="genReport" />
	    </td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">项目经理:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="projectManager" styleClass="default_input"></html:text>
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">销售合同号:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="salesContractNo" styleClass="default_input"></html:text>
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">项目名称:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="projectName" styleClass="default_input" size="35"></html:text>
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">所属部门:</td>
    	<td width="80%" class="inputtd">
    		<html:select property="department">
    			<%-- <html:option value="">全部</html:option> --%>
		    	<html:options collection="departmentList" property="comid" labelProperty="comfullname"/>
    		</html:select>
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">安装单位:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="insCompanyName" styleClass="default_input" size="35"></html:text>
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">电梯编号:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="elevatorNo" styleClass="default_input"></html:text>
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">驳回分类:</td>
    	<td width="80%" class="inputtd">
    		<html:select property="bhType">
    			<html:option value="">全部</html:option>
    			<html:options collection="bhTypeList" property="id.pullid" labelProperty="pullname"/>
    		</html:select>
    	</td>
    </tr>
    </table>
</html:form>