
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>

<br>

<html:form action="/maintenanceWorkPlanCompletionAction.do?method=toDeleteMoreRecord">
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
    	<td width="20%" class="wordtd">电梯编号:</td>
    	<td width="80%" class="inputtd">
    		<input type="text" name="elevatorNo" id="elevatorNo" styleClass="default_input" size="30" />
    	</td>
    </tr>
    <tr>
	    <td width="20%" class="wordtd">
         	保养计划日期:
		</td>
   		<td width="80%" class="inputtd">
   		<input type="text" name="sdate" id="sdate" styleClass="Wdate" size="13" onfocus="WdatePicker({isShowClear:true})"/>
		- 
   		<input type="text" name="edate" id="edate" styleClass="Wdate" size="13" onfocus="WdatePicker({isShowClear:true})"/>
	</td>
    </tr>
    </table>
</html:form>


