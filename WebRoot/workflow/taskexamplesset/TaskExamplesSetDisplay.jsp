<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
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

 <div style="font-size:1px; height:16px;"></div>
  <table width="99%" border="0" align="center" cellpadding="2" cellspacing="0" class="tb">
   <tr>
    <td width="15%" class="wordtd"><bean:message key="TaskExamplesSet.userid"/>：</td>
    <td colspan="3">
	   <%-- <bean:write name="TaskInstancesetresultmap" property="actorid"/>&nbsp;&nbsp;
	    &nbsp;&nbsp;--%>
	    <bean:write name="TaskInstancesetresultmap" property="username"/>
    </td>
  </tr>
  </table>
 </logic:present>

 <div style="font-size:1px; height:1px;"></div>

 	<table width="99%" border="0" align="center" cellpadding="2" cellspacing="0" class="tb">
  		<tr>
	    	<td width="15%" class="wordtd">
	    		<div align="center"><bean:message key="TaskExamplesSet.userid2"/></div>
	    	</td>
	    	<td colspan="3">&nbsp;</td>
	    </tr>
  		<tr>
  			<td class="wordtd"><div align="center">序号</div></td>
  			<td class="wordtd"><div align="center">用户编号</div></td> 
  			<td class="wordtd"><div align="center">用户名称</div></td>
  		</tr>
  		<logic:present name="TaskInstancesetList">
	   	<logic:iterate id="taskinstanid" name="TaskInstancesetList" indexId="tid">
	  		<tr>
	  			<td>  			
				  <div align="center">
				  	${tid+1}	
				  </div>
		        </td>
	  			<td class="wordtd1_td1"><bean:write name="taskinstanid" property="userid2"/></td>
	  			<td class="wordtd1_td1"><bean:write name="taskinstanid" property="username2"/></td>
	  		</tr>  
  		</logic:iterate>
 	</logic:present>
 	<logic:notPresent name="TaskInstancesetList">
 		<tr><td colspan="3" style="color:red"><div align="center">当前无候选操作人</div></td></tr>
 	</logic:notPresent>
  	</table>
  <br>


