
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
<html:form action="/maintenanceReportAction.do?method=toSearchResults">

    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
    <td width="20%" class="wordtd">维保分部:</td>
    <td width="80%" class="inputtd">
    <html:select property="maintDivision" styleId="maintDivision" onchange="getMainStation(this,'mainStation');">
    <%-- html:option value="">请选择</html:option--%>
    <html:options collection="CompanyList"  property="grcid" labelProperty="grcname" />
    </html:select><font color="red">*</font>
    </td>
    </tr>
     <tr>
    <td width="20%" class="wordtd">维保站:</td>
    <td width="80%" class="inputtd">
    <select id="mainStation" name="mainStation" onchange="getMaintPersonnel(this,'maintPersonnel')">
	    <%-- option value="">请选择</option--%>
    	<logic:iterate id="ele" name="mainStationList">
    		<option value="${ele.storageid }">${ele.storagename }</option>
    	</logic:iterate>
    </select><font color="red">*</font>
    </td>
    </tr>
     <tr>
    <td width="20%" class="wordtd">维保人员:</td>
    <td width="80%" class="inputtd">
    <select id="maintPersonnel" name="maintPersonnel">
    <option value="">全部</option>
    </select><font color="red">*</font>
    </td>
    </tr>
     <tr>
    <td width="20%" class="wordtd">维保开始结束日期:</td>
    <td width="80%" class="inputtd">
    <html:text property="sdate1" styleId="sdate1" styleClass="Wdate" size="16" onfocus="WdatePicker({readOnly:true,isShowClear:true});addEdate(this.value,'edate1');" onchange=""></html:text>
	- 
	<html:text property="edate1" styleId="edate1" styleClass="Wdate" size="16" onfocus="WdatePicker({readOnly:true,isShowClear:true})"></html:text>
    <html:hidden property="genReport" styleId="genReport" /><font color="red">*</font>
    </td>
    </tr>
    </table>
</html:form>

 <script type="text/javascript">
 
 //获取维保站里面的维保工
 var mainStation=document.getElementById("mainStation");
 getMaintPersonnel(mainStation,'maintPersonnel');
 
 </script>


