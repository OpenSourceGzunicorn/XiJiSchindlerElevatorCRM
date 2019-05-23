
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
<html:form action="/reimbursExpenseReportAction.do?method=toSearchResults">
<html:hidden property="genReport" styleId="genReport" />
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
    	<td width="20%" class="wordtd">所属维保分部:</td>
    	<td width="80%" class="inputtd">
    		<html:select property="maintdivision" styleId="maintdivision" onchange="Evenmore(this,'maintstation')">
		    	<html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
    		</html:select>
    	</td>
    </tr>
     <tr >
    	<td width="20%" class="wordtd">所属维保站:</td>
    	<td width="80%" class="inputtd">
    		<html:select property="maintstation" styleId="maintstation">
    			<%-- html:option value="">全部</html:option--%>
		    	<html:options collection="mainStationList" property="storageid" labelProperty="storagename"/>
    		</html:select>
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">报销人:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="reimburspeople" styleClass="default_input"></html:text>
    	</td>
    </tr>
    <tr>
	    <td width="20%" class="wordtd">报销时间:</td>
	    <td width="80%" class="inputtd">
		    <html:text property="sdate1" styleId="sdate1" styleClass="Wdate" size="13" onfocus="WdatePicker({readOnly:true,isShowClear:true})" />
			- 
			<html:text property="edate1" styleId="edate1" styleClass="Wdate" size="13" onfocus="WdatePicker({readOnly:true,isShowClear:true})" />
	    </td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">维保合同号:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="maintcontractno" styleClass="default_input"/>
    	</td>
    </tr>
     <tr >
    	<td width="20%" class="wordtd">报销范围:</td>
    	<td width="80%" class="inputtd">
    		<html:select property="reimburrange" styleId="reimburrange" onchange="EveReimburType(this)">
    			<html:option value="%">全部</html:option>
    			<html:option value="1">合同报销</html:option>
    			<html:option value="2">站内报销</html:option>
    			<html:option value="3">分部报销</html:option>
    			<html:option value="4">非维保成本</html:option>
    		</html:select>
    	</td>
    </tr>
    
     <tr >
    	<td width="20%" class="wordtd">报销类型:</td>
    	<td width="80%" class="inputtd">
    		<html:select property="reimburtype" styleId="reimburtype">
    			<html:option value="%">全部</html:option>
    			<%--
    			<html:options collection="mrTypeList" property="id.pullid" labelProperty="pullname"/>
    			<html:options collection="prTypeList" property="id.pullid" labelProperty="pullname"/>
    			--%>
    		</html:select>
    	</td>
    </tr>
    
    </table>
</html:form>


