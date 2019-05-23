
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>

<br>
<html:form action="/contractInvoiceReportAction.do?method=toSearchResults">
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
    	<td width="20%" class="wordtd">合同号:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="contractno" styleId="contractno" styleClass="default_input" />
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">发票类型:</td>
    	<td width="80%" class="inputtd">
    		<html:select property="invoicetype" styleId="invoicetype" styleClass="default_input" >
	          <html:option value="">全部</html:option>
	          <html:options collection="invoiceTypeList" property="inTypeId" labelProperty="inTypeName"/>
	        </html:select>
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">发票号:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="invoiceno" styleId="invoiceno" styleClass="default_input" size="33" />
    	</td>
    </tr>
    <tr>
    	<td width="20%" class="wordtd">开票名称:</td>
    	<td width="80%" class="inputtd">
    		<html:text property="invoicename" styleId="invoicename" styleClass="default_input" size="33" />
    	</td>
    </tr>
    <tr>
	    <td width="20%" class="wordtd">开票日期:</td>
	    <td width="80%" class="inputtd">
		    <html:text property="sdate1" styleId="sdate1" styleClass="Wdate" size="13" onfocus="WdatePicker({readOnly:true,isShowClear:true})" />
			- 
			<html:text property="edate1" styleId="edate1" styleClass="Wdate" size="13" onfocus="WdatePicker({readOnly:true,isShowClear:true})" />
	    </td>
    </tr>
    </table>
</html:form>


