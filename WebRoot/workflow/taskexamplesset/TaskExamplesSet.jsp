<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='AJAX'/>"></script>
<script language="javascript" src="<html:rewrite forward='SystemJS'/>"></script>
<br>
<html:errors/>
<html:form action="/taskExamplesSetAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden property="taskid"/>

  <logic:present name="TaskInstancesetresultmap">
<table width="99%" border="0" align="center" cellpadding="2" cellspacing="0" class="tb">

  <tr>
  <td width="15%" class="wordtd"><bean:message key="TaskExamplesSet.taskid"/>：</td>
  <td width="35%"><bean:write name="TaskInstancesetresultmap" property="taskid"/></td>
  <td width="15%" class="wordtd"><bean:message key="TaskExamplesSet.taskname"/>：</td>
  <td width="35%"><bean:write name="TaskInstancesetresultmap" property="taskname"/></td>
  </tr>
  <tr>
  <td width="15%" class="wordtd"><bean:message key="TaskExamplesSet.token"/>：</td>
  <td width="35%"><bean:write name="TaskInstancesetresultmap" property="token"/></td>
  <td width="15%" class="wordtd"><bean:message key="TaskExamplesSet.nodename"/>：</td>
  <td width="35%"><bean:write name="TaskInstancesetresultmap" property="nodename"/></td>
  </tr>
  <tr>
  <td width="15%" class="wordtd"><bean:message key="TaskExamplesSet.creatdate"/>：</td>
  <td width="35%"><bean:write name="TaskInstancesetresultmap" property="creatdate"/></td>
  <td width="15%" class="wordtd"><bean:message key="TaskExamplesSet.enddate"/>：</td>
  <td width="35%"><bean:write name="TaskInstancesetresultmap" property="enddate"/></td>
  </tr>
  <tr>
  <td width="15%" class="wordtd"><bean:message key="TaskExamplesSet.isopen"/>：</td>
  <td colspan="3">
  	<logic:match name="TaskInstancesetresultmap" property="isopenid" value="0">
		<bean:message key="enabledflag.yes"/>
	</logic:match>
	<logic:match name="TaskInstancesetresultmap" property="isopenid" value="1">
		<bean:message key="enabledflag.no"/>
	</logic:match>
  </tr>
  </table>
 </logic:present>
 <div style="font-size:1px; height:16px;"></div>
  <table width="99%" border="0" align="center" cellpadding="2" cellspacing="0" class="tb">
   <tr>
    <td width="15%" class="wordtd"><bean:message key="TaskExamplesSet.userid"/>：</td>
    <td colspan="3">
	    <html:text property="userid" styleClass="default_input" readonly="true"/>
	    <html:text property="username" styleClass="default_input" readonly="true"/>
	    <input type="button" value=".." onclick="openNewWin();" class="default_bottom"> 
    </td>
  </tr>
  </table>
 <div style="font-size:1px; height:1px;"></div>
 <table id="TaskExamplesSetTable" name="TaskExamplesSetTable" width="99%" border="0" align="center" cellpadding="2" cellspacing="0" class="tb">
   <thead>
   		<tr>
		  <td width="15%" class="wordtd"><bean:message key="TaskExamplesSet.userid2"/>：</td>
		  <td colspan="3">
		   	<input type="button" value="增 加" onclick="openNewWin2();" class="default_bottom"> 
		   	<input type="button" value="删 除" onclick="deleteRow10(TaskExamplesSetTable,'taskExamplesSetForm','ids')" class="default_bottom"> 
		  </td>
		</tr>
  		<tr>
  			<td class="wordtd"><div align="center">序号</div></td>
  			<td class="wordtd"><div align="center">用户编号</div></td> 
  			<td class="wordtd"><div align="center">用户名称</div></td>
  		</tr>
  		<tr style="display:none">
  			<td>  			
			  <div align="center">
			    <input type="checkbox" name="ids" id="ids" value="" class="inputTableData" style="width:30"/>
			    </div>
	        </td>
  			<td class="wordtd1_td1">
  				<input name="userid2" id="userid2" value="" class="inputTableData" readonly="true"/>	
  			</td>
  			<td class="wordtd1_td1">
  				<input name="username2" id="username2" value="" class="inputTableData" readonly="true" />	
  			</td>
  		</tr>
  	</thead>
  </table>
 <br>
 <br>
</html:form>
<%--html:javascript formName="taskExamplesSetForm"/--%>

<script language="javascript">
 function initpage(){
 	var keys = ['userid2','username2'];
   <logic:present name="TaskInstancesetList">
   		<logic:iterate id="taskinstanid" name="TaskInstancesetList">
   		var value=['<bean:write name="taskinstanid" property="userid2"/>','<bean:write name="taskinstanid" property="username2"/>'];
   		addInstanceRow(TaskExamplesSetTable,keys,value);
   		</logic:iterate>
   </logic:present>
 }
	initpage();
</script>


