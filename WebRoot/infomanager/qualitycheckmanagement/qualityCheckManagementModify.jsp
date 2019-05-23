<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/qualityCheckManagementAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="issubmit"/>
<html:hidden property="id" value="${qualityCheckManagementBean.billno}"/>
<html:hidden name="qualityCheckManagementBean" property="billno"/>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
			<td class="wordtd"><bean:message key="elevatorSale.elevatorNo"/>:</td>
			<td width="20%">
			<html:text name="qualityCheckManagementBean" property="elevatorNo" styleId="elevatorNo" styleClass="default_input" readonly="true" />
			<input type="button" value=".." onclick="selelevatorNo()" class="default_input" />
			<font color="red">*</font>
			</td>
			<td class="wordtd">现场督查人员:</td>
			<td width="20%">
				<input name="username" id="username" readonly="readonly" class="default_input" value="${supervname}" />
				<html:hidden name="qualityCheckManagementBean" property="superviseId" styleId="superviseId"/>
			 	<input type="button" value=".." onclick="openWindowAndReturnValue3('searchStaffAction','toSearchRecord2','')" class="default_input" />
			 	<font color="red">*</font>
			</td>
			<td class="wordtd">督查人员联系电话:</td>
			<td width="20%"><html:text name="qualityCheckManagementBean" property="supervisePhone" style="border:0;" styleClass="default_input" readonly="true" /></td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.maintContractNo"/>:</td>
			<td><html:text name="qualityCheckManagementBean" property="maintContractNo" style="border:0;" styleClass="default_input" readonly="true" /></td>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.salesContractNo"/>:</td>
			<td><html:text name="qualityCheckManagementBean" property="salesContractNo" style="border:0;" styleClass="default_input" readonly="true" /></td>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.projectName"/>:</td>
			<td><html:text name="qualityCheckManagementBean" property="projectName" style="border:0;" styleClass="default_input" readonly="true" size="30"/></td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.maintDivision"/>:</td>
			<td><html:text name="qualityCheckManagementBean" property="maintDivision" style="border:0;" styleClass="default_input" readonly="true" /></td>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.maintStation"/>:</td>
			<td><html:text name="qualityCheckManagementBean" property="maintStation" style="border:0;" styleClass="default_input" readonly="true" /></td>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.MaintPersonnel"/>:</td>
			<td>
			
			<html:select name="qualityCheckManagementBean" property="maintPersonnel" styleId="maintPersonnel" onchange="changeuser2();">
		    	<html:options collection="userstorageidlist2" property="userid" labelProperty="username"/>
    		</html:select>
    		<font color="red">*</font>
			</td>
		</tr>
		<tr>
			<td class="wordtd">维保工联系电话:</td>
			<td><html:text name="qualityCheckManagementBean"  property="personnelPhone" styleId="personnelPhone" style="border:0;" styleClass="default_input" readonly="true" /></td>
			<td class="wordtd">电梯类型</td>
			<td>
			<html:hidden name="qualityCheckManagementBean" property="elevatorType"/>
			<input type="text" name="elevatorTypeName" id="elevatorTypeName" value="${elevatorTypeName }" style="border:0;" class="default_input" readonly="true"/>
			</td>
			<td class="wordtd">备注</td>
			<td><html:text name="qualityCheckManagementBean" property="r4" styleClass="default_input" size="30"/></td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.ChecksDateTime"/>:</td>
			<td>&nbsp;</td>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.TotalPoints"/>:</td>
			<td>&nbsp;</td>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.ScoreLevel"/>:</td>
			<td>&nbsp;</td>
		</tr>        
    </table>
    <br>
    <%@ include file="addOrModify.jsp" %>
    <br>
    
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
        <td class="wordtd"><bean:message key="qualitycheckmanagement.SupervOpinion"/>:</td>
        <td></td>
      </tr>
    </table>
    <br>
    
    <%-- <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
        <td class="wordtd"><bean:message key="qualitycheckmanagement.PartMinisters"/>:</td>
        <td width="30%"></td>
        <td class="wordtd"><bean:message key="qualitycheckmanagement.RemDate"/>:</td>
        <td></td>
      </tr>        
      <tr>
        <td class="wordtd"><bean:message key="qualitycheckmanagement.PartMinistersRem"/>:</td>
        <td colspan="3"></td>      
      </tr>
    </table> --%>
</html:form>
<html:javascript formName="returningMaintainForm"/>

<script>
	function selelevatorNo(){
		var paramstring = "flag=update";
		var returnvalue = openWindowWithParams("searchMaintContractMasterAction","toSearchRecord",paramstring);
		if(returnvalue!=null && returnvalue.length>0){
			var array = returnvalue[0];
			var values=new Array();
			for(var i=0;i<array.length;i++){
				values[i]=array[i].split("=");
			}
			var valobj = values[6]
			var maintPersonnel = document.getElementById("maintPersonnel");
			maintPersonnel.options.length = 0;
			<logic:present name="userstorageidlist">
		    	<logic:iterate id="element" name="userstorageidlist">
				    var sname='<bean:write name="element" property="storagename"/>';
				    if(sname==valobj[1]){
					    var uid='<bean:write name="element" property="userid"/>';
					    var uname='<bean:write name="element" property="username"/>';
					    maintPersonnel.add(new Option(uname,uid))
				    }
		    	</logic:iterate>
	    	</logic:present>
		}
		
		toSetInputValue2(returnvalue,"");
		
	}
	
</script>
