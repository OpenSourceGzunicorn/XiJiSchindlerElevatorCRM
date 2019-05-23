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
<html:form action="/bywcReportAction.do?method=toSearchResults">

<input type="hidden" name="maintdivision" id="maintdivision" value="${rmap.maintdivision }"/>
<input type="hidden" name="maintstation" id="maintstation" value="${rmap.maintstation }"/>
<input type="hidden" name="maintPersonnel" id="maintPersonnel" value="${rmap.maintPersonnel }"/>
<input type="hidden" name="sdate1" id="sdate1" value="${rmap.sdate1 }"/>
<input type="hidden" name="edate1" id="edate1" value="${rmap.edate1 }"/>
<html:hidden property="genReport" styleId="genReport" />

	<%int i=1; %>
<table  width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<td>
[维保计划日期: ${rmap.sdate1 }至${rmap.edate1 }]
</td>
</table>
</br>
<table class=tb  width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr class=wordtd_header>
	<td nowrap="nowrap"  style="text-align: center;">维保分部</td>	
	<td nowrap="nowrap"  style="text-align: center;">维保站</td>
	<td nowrap="nowrap"  style="text-align: center;">维保工身份证号</td>
	<td nowrap="nowrap"  style="text-align: center;">维保工</td>
	<td nowrap="nowrap"  style="text-align: center;">计划保养次数</td>
	<td nowrap="nowrap"  style="text-align: center;">实际完成次数</td>
	<td nowrap="nowrap"  style="text-align: center;">得分为0次数</td>
	<td nowrap="nowrap"  style="text-align: center;">实际完成比例(%)</td>
	<td nowrap="nowrap"  style="text-align: center;">平均保养得分</td>
	<td nowrap="nowrap"  style="text-align: center;">变红台数</td>
	</tr>
	
	<logic:present name="maintenanceBywcReportList" >
	  <logic:iterate id="ele" name="maintenanceBywcReportList">
	  <tr class="inputtd" align="center" height="20">
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="ComName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="StorageName"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele"  property="idCardNo"/>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele"  property="maintpersonnel"/>
	  </td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="planmaintnum"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="afinishtimes"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write name="ele" property="zeroscoretimes"/></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write  name="ele" property="shijibili" /></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write  name="ele" property="averagescore" /></td>
	  <td nowrap="nowrap"  style="text-align: center;"><bean:write  name="ele" property="elenum" /></td>
	  </tr>
	  </logic:iterate>
	</logic:present>
<%-- 	<logic:notPresent name="maintenanceBywcReportList">
	  <tr class="noItems" align="center" height="20">
	  	<td colspan="10" >
	  		<bean:write name="showinfostr"/>
	  	</td>
	  </tr>
	</logic:notPresent> --%>
</table>

		
</html:form>
