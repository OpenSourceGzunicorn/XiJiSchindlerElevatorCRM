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


<html:form
	action="/newelevatorContractReportAction.do?method=toSearchRecord">




	<html:hidden property="property(genReport)" styleId="genReport"
		value="" />
	<table width="98%" border="0" align="center" cellpadding="0"
		cellspacing="1" bgcolor="#FFFFFF">
		<tr>
			<td height="23" colspan="4" background="../../common/images/bg1.gif">&nbsp;</td>
		</tr>
		<tr>
			<td width="80" height="28" bgcolor="#81A5C7">
			<div align="right">输入日期(起):</div>
			</td>
			<td bgcolor="#D2E7FC" width="80"><html:text
				property="property(inputdate1)" styleId="inputdate1"
				onclick="setday(this)" /></td>

			<td width="80" height="28" bgcolor="#81A5C7">
			<div align="right">(止):</div>
			</td>
			<td bgcolor="#D2E7FC"><html:text property="property(inputdate2)"
				onclick="setday(this)" styleId="inputdate2" /></td>
		</tr>
		<tr>
			<td width="80" height="28" bgcolor="#81A5C7">
			<div align="right">续保日期(起):</div>
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
			</html:select></td>
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
   
     startmonth=parseInt(date1.getMonth()+1)<10?"0"+parseInt(date1.getMonth()+1):parseInt(date1.getMonth());
     endmonth=parseInt(date2.getMonth()+1)<10?"0"+parseInt(date2.getMonth()+1):parseInt(date2.getMonth());
 
     startdate=date1.getYear()+"-"+startmonth+"-01";
     enddate=date2.getYear()+"-"+endmonth+"-31";
     
    
	document.serveTableForm.startdate.value=startdate;
	document.serveTableForm.inputdate1.value=startdate;
 
 	document.serveTableForm.enddate.value=enddate;
    document.serveTableForm.inputdate2.value=enddate;
 </script>



