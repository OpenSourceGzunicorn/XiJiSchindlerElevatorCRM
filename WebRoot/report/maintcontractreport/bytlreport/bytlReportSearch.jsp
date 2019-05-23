
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>

<br>

<html:form action="/bytlReportAction.do?method=toSearchResults">
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
     <tr>
    	<td width="20%" class="wordtd">所属维保站:</td>
    	<td width="80%" class="inputtd">
    		<html:select property="maintstation" styleId="maintstation">
    			<%-- html:option value="">全部</html:option--%>
		    	<html:options collection="mainStationList" property="storageid" labelProperty="storagename"/>
    		</html:select>
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">维保合同号:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="maintcontractno" styleClass="default_input" size="30" />
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">销售合同号:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="salescontractno" styleClass="default_input" size="30" />
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">电梯编号:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="elevatorno" styleClass="default_input" size="30" />
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">维保工:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="maintpersonnel" styleClass="default_input" size="30" />
    	</td>
    </tr>
    <%-- tr>
	    <td width="20%" class="wordtd">下达日期:</td>
	    <td width="80%" class="inputtd">
		    <html:text property="sdate1" styleId="sdate1" styleClass="Wdate" size="13" onfocus="WdatePicker({readOnly:true,isShowClear:true})" />
			- 
			<html:text property="edate1" styleId="edate1" styleClass="Wdate" size="13" onfocus="WdatePicker({readOnly:true,isShowClear:true})" />
	    </td>
    </tr--%>
    </table>
</html:form>


