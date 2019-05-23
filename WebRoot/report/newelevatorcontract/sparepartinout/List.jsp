<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<script language="javascript" src="<html:rewrite forward='calendarJS'/>"></script>
<html:base />
<html>
<head>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='reportCSS'/>">
<script language="javascript">
function toExcelRecord(){
    document.serveTableForm.report.value="Y";
    document.serveTableForm.genReport.value="Y";
	document.serveTableForm.target = "_blank";
	document.serveTableForm.submit();
}
function tosearchMethod(){
	serveTableForm.genReport.value="";
	serveTableForm.target = "_self";
	serveTableForm.report.value="";
	document.serveTableForm.submit();
}
</script>
<title>每月材料入库出库报表</title>

</head>
<html:form action="/sparepartInOutReportAction.do?method=toSearchRecord">
<html:hidden property="property(report)" styleId="report" />
<html:hidden property="property(genReport)" styleId="genReport" />
<input type="hidden" name="genReport"/>
<table width="100%" border="0" cellspacing="0" cellpadding="3">
	<tr>
		<td width="166" height="25" rowspan="2"></td>
		<td align="right">&nbsp;</td>
	</tr>
	<tr>
		<td class="S_height">查询日期：<bean:write name="search" property="today" /><br />
		物料出入库日期：从：<bean:write name="search" property="date1" />到：<bean:write name="search" property="date2" /></td>
	</tr>
</table>
</html:form>
<body>



<table width="990" border="1" cellpadding="0" cellspacing="0" class="s_table">
  <tr>
    <td class="S_title s_table_td">每月材料入库出库报表</td>
  </tr>

<tr>
  
    <td class="s_table_td"><table width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#CCCCCC">
      <tr>
        <th class="S_title_1" nowrap>序号 </th>
        <th class="S_title_1" nowrap>所属仓库 </th>
		<th class="S_title_1" nowrap>材料名称 </th>	
		<th class="S_title_1" nowrap>规格型号 </th>	
		<th class="S_title_1" nowrap>单位 </th>
		<th class="S_title_1" nowrap>上月库存 </th>	
		<th class="S_title_1" nowrap>单价 </th>	
		<th class="S_title_1" nowrap>上月金额 </th>	
		<th class="S_title_1" nowrap>本月入库数量 </th>	
		<th class="S_title_1" nowrap>入库单价 </th>	
		<th class="S_title_1" nowrap>本月入库金额 </th>
		<th class="S_title_1" nowrap>本月出库数量 </th>	
		<th class="S_title_1" nowrap>出库单价 </th>
		<th class="S_title_1" nowrap>本月出库金额 </th>	
		<th class="S_title_1" nowrap>本月结存 </th>		
  </tr>
<%int i = 1;%>
<logic:present name="report_list">
 <logic:iterate id="element" name="report_list">
  <%if (i % 2 != 0) {%>

  	<tr>
        <th class="S_td_name"><%=i%></th>
		<td class="S_td_name"><bean:write name="element" property="storagename"/></td>
		<td class="S_td_name"><bean:write name="element" property="materialsname"/></td>
		<td class="S_td_name"><bean:write name="element" property="typestandards"/></td>		 
		<td class="S_td_name"><bean:write name="element" property="unit"/></td>
		<td class="S_td_name"><bean:write name="element" property="lastnum"/></td>
		<td class="S_td" ><bean:write name="element" property="lastprice"/></td>
		<td class="S_td"><bean:write name="element" property="lasttoprice"/></td>
		<td class="S_td_name"><bean:write name="element" property="innum"/></td>
		<td class="S_td"><bean:write name="element" property="inprice"/></td>
		<td class="S_td"><bean:write name="element" property="intoprice"/></td>
		<td class="S_td_name"><bean:write name="element" property="outnum"/></td>
		<td class="S_td"><bean:write name="element" property="outprice"/></td>
		<td class="S_td"><bean:write name="element" property="outtoprice"/></td>
		<td class="S_td"><bean:write name="element" property="totalprice"/></td>
  	</tr>
		<%} else {%>
		<tr>
		<th class="S_td1_name"><%=i%></th>
		<td class="S_td1_name"><bean:write name="element" property="storagename"/></td>
		<td class="S_td1_name"><bean:write name="element" property="materialsname"/></td>
		<td class="S_td1_name"><bean:write name="element" property="typestandards"/></td>		 
		<td class="S_td1_name"><bean:write name="element" property="unit"/></td>
		<td class="S_td1_name"><bean:write name="element" property="lastnum"/></td>
		<td class="S_td1" ><bean:write name="element" property="lastprice"/></td>
		<td class="S_td1"><bean:write name="element" property="lasttoprice"/></td>
		<td class="S_td1_name"><bean:write name="element" property="innum"/></td>
		<td class="S_td1"><bean:write name="element" property="inprice"/></td>
		<td class="S_td1"><bean:write name="element" property="intoprice"/></td>
		<td class="S_td1_name"><bean:write name="element" property="outnum"/></td>
		<td class="S_td1"><bean:write name="element" property="outprice"/></td>
		<td class="S_td1"><bean:write name="element" property="outtoprice"/></td>
		<td class="S_td1"><bean:write name="element" property="totalprice"/></td>
  	</tr>
			<%}%>
       <%i++;%>
			</logic:iterate>
			</logic:present>
			</tr>
    </table></td>
    
    

    

<table width="990" height="30" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="right"><input name="Submit2" type="submit" class="S_botton" value="导出Excel"  onclick="toExcelRecord()"/></td>
  </tr>
</table></div>
</body>