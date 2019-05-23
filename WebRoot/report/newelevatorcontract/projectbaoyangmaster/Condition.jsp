<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.HashMap"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
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
input
{
	border:1px solid #666666;
	height:18px;
	font-size:12px;
}
.leftbottom { BORDER-BOTTOM: #666666 1px solid; BORDER-LEFT: #666666 1px solid;BORDER-top: #666666 1px solid; BORDER-right: #666666 1px solid; background-image:url(images/botton_bg.gif); padding-top:2px; height:20px;}
.bottom { BORDER-BOTTOM: #666666 1px solid; BORDER-LEFT: #666666 1px solid;BORDER-top: #666666 1px solid; BORDER-right: #666666 1px solid;padding-top:3px; background-color:#D4D0C8; height:20px;}
-->
</style>
<html:form action="/ProjectBaoYangMasterAction.do?method=toSearchRecord">
	<html:hidden property="property(genReport)" styleId="genReport" value="N" />
	<html:hidden property="property(searchflags)" styleId="searchflags"/>
	<table width="98%" border="0" bgcolor="#000000" cellpadding="0"
				cellspacing="0" class="tb">
						<tr>
			<td width="20%" height="28" class="wordtd">
			<div align="right">所属维保分部:</div>
			</td>
			<td  ><html:select
				property="property(grcid)">
				<html:options collection="grcidlist" property="grcid"
					labelProperty="grcname"></html:options>
			</html:select></td>
		</tr>
				<tr>
			<td width="20%" height="28" class="wordtd">
			<div align="right">合同性质:
				<html:checkbox property="property(searchflag)" styleId="searchflag" value="" onclick="checkAll(this)"></html:checkbox>
			</div>
			</td>
			<td  >
			<table>
				<tr>
				 <td><html:checkbox property="property(searchflag)" styleId="searchflag" value="ZB" onclick="checkAll(this)">在保&nbsp;</html:checkbox></td>
				 <td><html:checkbox property="property(searchflag)" styleId="searchflag" value="XB" onclick="checkAll(this)">续保&nbsp;</html:checkbox></td>
				 <td><html:checkbox property="property(searchflag)" styleId="searchflag" value="TB" onclick="checkAll(this)">退保&nbsp;</html:checkbox></td>
				 <td><html:checkbox property="property(searchflag)" styleId="searchflag" value="LS" onclick="checkAll(this)">历史&nbsp;</html:checkbox></td>		
				</tr>
			</table>
			</td>			
		</tr>
		<tr>
			<td width="20%" height="28" class="wordtd">
			<div align="right">合同编号:</div>
			</td>
			<td  width="80%"><html:text
				property="property(contractid)" styleId="ContractID" /></td>
		</tr>
		<tr>
			<td width="20%" height="28" class="wordtd">
			<div align="right">甲方单位:</div>
			</td>
			<td  width="80%"><html:text
				property="property(custname)"/></td>
		</tr>
		<tr>
			<td width="20%" height="28" class="wordtd">
			<div align="right">电梯编号:</div>
			</td>
			<td  width="80%"><html:text
				property="property(elevatorid)"/></td>
		</tr>

		<tr>
		<td width="20%" height="28" class="wordtd">
		<div align="right">保养日期 起</div>
		</td>
		<td  width="80%"><html:text property="property(startdate)" styleClass="Wdate" onclick="setday(this)" readonly="true"/>
					止:<html:text property="property(enddate)" styleClass="Wdate" onclick="setday(this)" readonly="true"/></td>
		</tr>
		<tr>
		<td width="20%" height="28" class="wordtd">
		<div align="right">退保日期 起</div>
		</td>
		<td  width="80%"><html:text property="property(tstartdate)" styleClass="Wdate" onclick="setday(this)" readonly="true"/>
					止:<html:text property="property(tenddate)" styleClass="Wdate" onclick="setday(this)" readonly="true"/></td>
		</tr>

	</table>
	<script>
		init();
		
		function init(){
			initCheckAll("searchflag");
		}
			
		function checkAll(input){	
			var obj = document.getElementsByName(input.name);
			if(input.value==obj[0].value){				
				if(obj[0].checked){				
					for(i=1;i<obj.length;i++){
						obj[i].checked = "checked"
					}
				}else{
					for(i=1;i<obj.length;i++){
						obj[i].checked = ""
					}
				}
			}else{
				obj[0].checked = ""
			}
		}	
		
		function initCheckAll(name){	
			var obj = document.getElementsByName(name);
			for(i=0;i<obj.length;i++){
				obj[i].checked = "checked"
			}
		}
	</script>
</html:form>


