<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.HashMap"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='CalendarCSS2'/>">
<script language="javascript" src="<html:rewrite forward='calendarJS'/>"></script>

<style type="text/css">
<!--
.add{font-family:Arial,"宋体";
    font-size:9pt;
	font-weight:bold;
	color: #ffffff;
	background-color:#9E0B0E;
	padding-right:13px;
	padding-bottom:2px;
	padding-left:13px;
	}
body {
	background-color: #F4F2F5;
}
.style5 {
	color: #9e0b0e;
	font-weight: bold;
}
.leftbottom { BORDER-BOTTOM: #666666 1px solid; BORDER-LEFT: #666666 1px solid;BORDER-top: #666666 1px solid; BORDER-right: #666666 1px solid; background-image:url(images/botton_bg.gif); padding-top:2px; height:20px;}
.bottom { BORDER-BOTTOM: #666666 1px solid; BORDER-LEFT: #666666 1px solid;BORDER-top: #666666 1px solid; BORDER-right: #666666 1px solid;padding-top:3px; background-color:#D4D0C8; height:20px;}
-->
</style>


<html:form action="/newContractReportAction.do?method=toSearchRecord">




	<html:hidden property="property(genReport)" styleId="genReport"
		value="" />
	<table width="98%" border="0" align="center" cellpadding="0"
		cellspacing="1" bgcolor="#FFFFFF">
		<tr>
			<td height="23" colspan="4" background="../../common/images/bg1.gif">&nbsp;</td>
		</tr>

		<tr>
			<td width="80" height="28" bgcolor="#81A5C7">
			<div align="right">日期(起):</div>
			</td>
			<td bgcolor="#D2E7FC" width="80"><html:text
				property="property(startdate)" styleId="startdate"
				onclick="setday(this)" /></td>

			<td width="80" height="28" bgcolor="#81A5C7">
			<div align="right">(止):</div>
			</td>
			<td bgcolor="#D2E7FC"><html:text property="property(enddate)"
				onclick="setday(this)" styleId="enddate" /></td>
		</tr>
		<tr>
			<td width="80" height="28" bgcolor="#81A5C7">
			<div align="right">维保站:</div>
			</td>
			<td bgcolor="#D2E7FC" colspan="3"><html:select
				property="property(mugStorages)">
				<html:option value="%">全部</html:option>
				<html:options collection="mugStorages" property="storageid"
					labelProperty="storagename"></html:options>
			</html:select> 
			</td>


		</tr>

		<tr>
			<td height="23" colspan="4" background="../../common/images/bg.gif">&nbsp;</td>
		</tr>
	</table>
</html:form>

<script language="Javascript">
     var date=new Date();
     var date1=new Date();
     var date2=new Date();
     date1.setMonth(date.getMonth());
     date2.setMonth(date.getMonth()+1);
   
    
    if(date1.getMonth().toString().lenght==2){
 		document.serveTableForm.startdate.value=date1.getYear()+"-"+date1.getMonth()+"-26";
 	}
 	else{
 	    document.serveTableForm.startdate.value=date1.getYear()+"-0"+date1.getMonth()+"-26";
 	  
 	}
  
  	if(date2.getMonth().toString().length==2){
     	document.serveTableForm.enddate.value=date1.getYear()+"-"+date2.getMonth()+"-26";
    }
    else{
    	document.serveTableForm.enddate.value=date1.getYear()+"-0"+date2.getMonth()+"-26";
    }
 </script>



