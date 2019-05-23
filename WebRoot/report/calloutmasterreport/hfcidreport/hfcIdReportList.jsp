<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
  <script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
  <link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
  <script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>

<br>
<html:form action="/calloutMasterHfcIdReportAction.do?method=toSearchResults">

<input type="hidden" name="maintdivision" id="maintdivision" value="${rmap.maintdivision }"/>
<input type="hidden" name="maintstation" id="maintstation" value="${rmap.maintstation }"/>
<input type="hidden" name="companyid" id="companyid" value="${rmap.companyid }"/>
<input type="hidden" name="salescontractno" id="salescontractno" value="${rmap.salescontractno }"/>
<input type="hidden" name="elevatorNo" id="elevatorNo" value="${rmap.elevatorNo }"/>
<input type="hidden" name="sdate1" id="sdate1" value="${rmap.sdate1 }"/>
<input type="hidden" name="edate1" id="edate1" value="${rmap.edate1 }"/>
<input type="hidden" name="serviceobjects" id="serviceobjects" value="${rmap.serviceobjects }"/>
<html:hidden property="genReport" styleId="genReport" />

 <table width="100%" height="24" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr class="td_2">
      <td width="2" background="/images/line.gif"></td>
      <td width="10%" class="tab-on" id="navcell" onclick="switchCell('0')">明细信息</td>
      <td width="3" background="/images/line_2.gif"><img src="/images/line_1.gif" width="2" height="25"></td>
      <td width="10%" class="tab-off" id="navcell" onclick="switchCell('1')">数据分析</td>  
      <td width="80%" background="/images/line_2.gif"><img src="/images/line_1.gif" width="2" height="25"></td>
 </tr>
 </table>
 
<table id="contents" width="100%" border="0" align="center" cellpadding="0" cellspacing="0">  
    <tr id="tb">
      <td width="3" background="/images/line_3.gif" bgcolor="#FFFFFF" style="background-repeat:repeat-y;">&nbsp;</td>
      <td bgcolor="#ffffff" style="text-align: center;">
      <br>

			<table class=tb  width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr class=wordtd_header>
					<td nowrap="nowrap"  style="text-align: center;">序号</td>
					<logic:iterate id="tname" name="totalhmap" property="titlename">
						<td nowrap="nowrap"  style="text-align: center;"><bean:write name="tname"/></td>
					</logic:iterate>
				</tr>
				
				<logic:present name="calloutMasterHfcIdList" >
				  <logic:iterate id="ele" name="calloutMasterHfcIdList" indexId="hfcindex">
				  <tr class="inputtd" align="center" height="20">
					  	<td nowrap="nowrap"  style="text-align: center;">${hfcindex+1 }</td>
						<logic:iterate id="tid" name="totalhmap" property="titleid">
							<logic:equal name="tid" value="calloutmasterno">
							<td nowrap="nowrap"  style="text-align: center;">
						  		<a href="<html:rewrite page='/hotphoneAction.do'/>?typejsp=display&isopenshow=Yes&id=<bean:write name="ele" property="calloutmasterno"/>" target="_blnk"><bean:write name="ele" property="calloutmasterno"/></a>
						  	</td>
						  	</logic:equal>
							<logic:notEqual name="tid" value="calloutmasterno">
								<td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="${tid }"/></td>
							</logic:notEqual>
						</logic:iterate>
				  </tr>
				  </logic:iterate>
				</logic:present>
				<logic:notPresent name="calloutMasterHfcIdList">
				  <tr class="noItems" align="center" height="20">
				  	<td colspan='<bean:write name="totalhmap" property="tlen"/>' >
				  		<bean:write name="showinfostr"/>
				  	</td>
				  </tr>
				</logic:notPresent>
				
			</table>
		<br/><br/>
   </td>
   </tr> 
   
    <tr id="tb" style="display:none;" >
      <td width="3" background="/images/line_3.gif" bgcolor="#FFFFFF" style="background-repeat:repeat-y;">&nbsp;</td>
      <td bgcolor="#ffffff">
			<br/>
			<table width="50%" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr><td>
				<table class=tb  width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
					<tr class=wordtd_header>
						<td nowrap="nowrap"  style="text-align: center;" width="15%">序号</td>
						<td nowrap="nowrap"  style="text-align: center;">数据分析</td>
					</tr>
					
				<logic:present name="totalhmap" property="sjfxlist" >
					  <logic:iterate id="sjfx" name="totalhmap" property="sjfxlist" indexId="hfcindex">
					  <tr class="inputtd" align="center" height="20">
						  	<td nowrap="nowrap"  style="text-align: center;" >${hfcindex+1 }</td>
							<td nowrap="nowrap"  style="text-align: left;">${sjfx }</td>
					  </tr>
					  </logic:iterate>
				</logic:present>
					
				</table>
			</td></tr>
			<tr><td><br/>
				<table class=tb  width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
					<tr class=wordtd_header>
						<td nowrap="nowrap"  style="text-align: center;" width="15%">序号</td>
						<td nowrap="nowrap"  style="text-align: center;" width="35%">故障类型名称</td>
						<td nowrap="nowrap"  style="text-align: center;" width="15%">故障类型代码</td>
						<td nowrap="nowrap"  style="text-align: center;" width="15%">次数</td>
						<td nowrap="nowrap"  style="text-align: center;" width="15%">占比</td>
					</tr>
					
				<logic:present name="totalhmap" property="hftidlist" >
					  <logic:iterate id="hele" name="totalhmap" property="hftidlist" indexId="heleid">
					  <tr class="inputtd" align="center" height="20">
						  	<td nowrap="nowrap"  style="text-align: center;" >${heleid+1 }</td>
							<td nowrap="nowrap"  style="text-align: center;"><bean:write name="hele" property="hfcIdName"/></td>
							<td nowrap="nowrap"  style="text-align: center;"><bean:write name="hele" property="hfcId"/></td>
							<td nowrap="nowrap"  style="text-align: center;"><bean:write name="hele" property="hfcnum"/></td>
							<td nowrap="nowrap"  style="text-align: center;"><bean:write name="hele" property="zhanbi"/></td>
					  </tr>
					  </logic:iterate>
				</logic:present>
					
				</table>
			</td></tr>
			<tr><td><br/>
				<table class=tb  width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
					<tr class=wordtd_header>
						<td nowrap="nowrap"  style="text-align: center;" width="15%">序号</td>
						<td nowrap="nowrap"  style="text-align: center;" width="35%">主板型号</td>
						<td nowrap="nowrap"  style="text-align: center;" width="15%">故障代码</td>
						<td nowrap="nowrap"  style="text-align: center;" width="15%">次数</td>
						<td nowrap="nowrap"  style="text-align: center;" width="15%">占比</td>
					</tr>
					
				<logic:present name="totalhmap" property="hmtidlist" >
					  <logic:iterate id="hele" name="totalhmap" property="hmtidlist" indexId="heleid">
					  <tr class="inputtd" align="center" height="20">
						  	<td nowrap="nowrap"  style="text-align: center;" >${heleid+1 }</td>
							<td nowrap="nowrap"  style="text-align: center;"><bean:write name="hele" property="hmtIdName"/></td>
							<td nowrap="nowrap"  style="text-align: center;"><bean:write name="hele" property="FaultCode"/></td>
							<td nowrap="nowrap"  style="text-align: center;"><bean:write name="hele" property="fatnum"/></td>
							<td nowrap="nowrap"  style="text-align: center;"><bean:write name="hele" property="zhanbi"/></td>
					  </tr>
					  </logic:iterate>
				</logic:present>
				</table>
			</td></tr>
			</table>
			<br/><br/>
      </td>
    </tr>   
  </table>
		
</html:form>
