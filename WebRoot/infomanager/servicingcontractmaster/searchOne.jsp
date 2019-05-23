<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html>
<body>
<br/>
	<table width="80%" border="0" cellpadding="0" cellspacing="0" class="tb" align="center">
	    <tr >
	    	<td width="8%" class="wordtd_k2"  ><div align="center" >序号</div></td>
	    	<td class="wordtd_k2" ><div align="center">任务列表</div></td>
	    </tr>
	    	<tr>
	    	<td align="center">1</td>
	    	<td>&nbsp;
	    	<logic:notEqual name="Arrival" value="0">
	    		<a href="<html:rewrite page="/ServicingContractMasterTaskSearchAction.do"/>?typejsp=arrival" target="_self">预计/确认到货日期共( ${Arrival})份合同 </a>
	    	</logic:notEqual>
	    	<logic:equal name="Arrival" value="0">
	    		预计/确认到货日期共( 0)份合同 
	    	</logic:equal>
	    	</td>
	    	</tr>
	    	<tr>
	    	<td align="center">2</td>
	    	<td>&nbsp;
	    		<a href="<html:rewrite page="/ServicingContractMasterTaskSearchAction.do"/>?typejsp=complete" target="_self">项目完工信息共( ${Complete} )份合同  </a>
	    	</td>
	    	</tr>
	</table>
</body>
<br/>
</html>
