
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>

<br>
<html:form action="/calloutMasterHfcIdReportAction.do?method=toSearchResults">
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
	    <td width="20%" class="wordtd">报修单位名称:</td>
	    <td width="80%" class="inputtd">
		    <html:text property="companyid" styleId="companyid" styleClass="default_input" size="60" />
		</td>
    </tr>
    <tr>
	    <td width="20%" class="wordtd">销售合同号:</td>
	    <td width="80%" class="inputtd">
		    <html:text property="salescontractno" styleId="salescontractno" styleClass="default_input" size="60" />
		</td>
    </tr>
    <tr>
	    <td width="20%" class="wordtd">电梯编号:</td>
	    <td width="80%" class="inputtd">
		    <html:textarea property="elevatorNo" styleId="elevatorNo" rows="8" cols="50" readonly="true"></html:textarea>
		    <input type="button" value="请选择" onclick="tckselect();" class="default_input"/>
		    &nbsp;
    		<input type="button" value="清 空" onclick="delselect();" class="default_input"/>
		</td>
    </tr>
    <tr>
	    <td width="20%" class="wordtd">报修时间:</td>
	    <td width="80%" class="inputtd">
		    <html:text property="sdate1" styleId="sdate1" styleClass="Wdate" size="13" onfocus="WdatePicker({readOnly:true,isShowClear:true})" />
			- 
			<html:text property="edate1" styleId="edate1" styleClass="Wdate" size="13" onfocus="WdatePicker({readOnly:true,isShowClear:true})" />
	    </td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">是否技术支持:</td>
    	<td width="80%" class="inputtd">
    		<html:select property="serviceobjects" styleId="serviceobjects">
    			<html:option value="%">全部</html:option>
    			<html:option value="Y">是</html:option>
    			<html:option value="N">否</html:option>
    		</html:select>
    	</td>
    </tr>
    </table>
</html:form>


