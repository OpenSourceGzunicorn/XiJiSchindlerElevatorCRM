<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='calendarCSS'/>">
<script language="javascript" src="<html:rewrite forward='calendarJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>


<br>
<html:errors/>
<html:form action="/WorkSpaceRoleConfigAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<input type="hidden" id="roleid" name="roleid" value="<bean:write name="role" property="roleid" />" />
<input type="hidden" id="rolename" name="rolename" value="<bean:write name="role" property="rolename" />" />
<%--主信息--%>
<logic:present name="role">
<table width="99%" border="0" align="center" cellpadding="2" cellspacing="0" class="tb">
  <tr>
    <td width="15%" class="wordtd">
    	<bean:message key="role.roleid" />：
    </td>
    <td width="35%">
	    <bean:write name="role" property="roleid"/>
    </td>
    <td width="15%" class="wordtd">
    	<bean:message key="role.name" />： 
    </td>
    <td width="35%">
	    <bean:write name="role" property="rolename"/>     
    </td>
  </tr>
</table>
</logic:present>


<div style="font-size:1px; height:1px;"></div>
<div style="font-size:1px; height:1px;"></div>
<div style="font-size:1px; height:1px;"></div>
<br>

<table width="99%" height="24" border="0" align="center" cellpadding="2" cellspacing="0" class="tb">

<tr>
  	<td width="15%" class="wordtd">工作区属性配置</td>
  	<td width="85%">
  		<input name="addproject" id="addproject" class="default_bottom"   
  		onclick="openWin()" 
  		type="button" value="<bean:message key="page.button.add"/>">　
  		
  		<input type="button" name="delproject" class="default_bottom"
  		id="delproject" value="<bean:message key="page.button.delete"/>" 
  		onclick="deleteLine(configList,'WorkSpaceRoleConfigForm','ids')" /></td>
</tr>
</table>

<div style="font-size:1px; height:1px;"></div>
<div style="font-size:1px; height:1px;"></div>
<table width="99%" border="0" cellpadding="2" cellspacing="0" id="configList" name="configList" class="tb" align="center">
	<thead>
  		<tr>
  			<td class="oddRow5"><bean:message key="file.no"/></td>
  			<td class="oddRow5"><bean:message key="Track.jnlno"/></td> 
  			<td class="oddRow5"><bean:message key="contract.filetitle"/></td>
  		</tr>
  		<tr style="display:none">
  			<td class="wordtd1_td1">
  			
			  <div align="center">
			    <input type="checkbox" name="ids" id="ids" value="" class="inputTableData" style="width:30"/>
			    </div>
	        </td>
  			<td class="wordtd1_td1">
  				<input name="wsid" id="wsid" value="" class="inputTableData" readonly="true" style="width:150"/>	
  			</td>
  			<td class="wordtd1_td1">
  				<input name="title" id="title" value="" class="inputTableData" readonly="true" />	
  			</td>
  		</tr>
  	</thead>
</table>
</html:form>


<script>
function initPage(param){
	var keys = new Array();
	var values = new Array();
	if(param == 'configList'){
		keys = ['wsid','title'];
		<logic:present name="initlist">
		<logic:iterate name="initlist" id="element" >
			var wsid = '<bean:write name="element" property="wsid"/>';
			var title = '<bean:write name="element" property="title"/>';
			values = [wsid,title];
			
			addInstanceRow(configList,keys,values);
		</logic:iterate>
		</logic:present>
	}
}

initPage('configList');


</script>


<script>

function openWin(){
	var keys = new Array();
	keys = ['ids','wsid','title'];
	openWindow3('query/Search.jsp?path=<html:rewrite page="/searchWorkSpaceBasePropertySelectAction.do?method=toSearchRecord"/>','configList',keys);
}

</script>
