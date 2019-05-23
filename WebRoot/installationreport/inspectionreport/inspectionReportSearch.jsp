
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
<html:form action="/inspectionReportAction.do?method=toSearchResults">

    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
     <tr>
	    <td width="20%" class="wordtd">提交厂检时间:</td>
	    <td width="80%" class="inputtd">
	    <html:text property="sdate1" styleId="sdate1" styleClass="Wdate" size="16" onfocus="WdatePicker({readOnly:true,isShowClear:true})" />
		- 
		<html:text property="edate1" styleId="edate1" styleClass="Wdate" size="16" onfocus="WdatePicker({readOnly:true,isShowClear:true})" />
	    <html:hidden property="genReport" styleId="genReport" />
	    </td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">问题项编号:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="issueCoding" styleClass="default_input"></html:text>
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">电梯类型:</td>
    	<td width="80%" class="inputtd">
    		<html:select property="elevatorType" styleId="elevatorType">
    			<html:option value="">请选择</html:option>
    			<html:option value="T">直梯</html:option>
    			<html:option value="F">扶梯</html:option>
		    	<%-- <html:options collection="departmentList" property="comid" labelProperty="comfullname"/> --%>
    		</html:select><font color="red">*</font>
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">安装单位名称:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="insCompanyName" styleClass="default_input" size="40"></html:text>
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">项目经理:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="projectManager" styleClass="default_input"></html:text>
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
  <%--   <tr>
    	<td width="20%" class="wordtd">审批结果:</td>
    	<td width="80%" class="inputtd">
    		<html:select property="approveResult">
    			<html:option value="">请选择</html:option>
    			<html:option value="驳回">驳回</html:option>
    			<html:option value="不通过">不通过</html:option>
    		</html:select>
    	</td>
    </tr> --%>
    </table>
</html:form>