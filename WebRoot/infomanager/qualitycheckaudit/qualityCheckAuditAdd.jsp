<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/qualityCheckManagementAction.do?method=toAuditRecord" enctype="multipart/form-data">
	<html:hidden property="isreturn" />
	<html:hidden name="qualityCheckManagementBean" property="billno" />
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
			<td class="wordtd"><bean:message key="elevatorSale.elevatorNo"/>:</td>
			<td width="20%"><html:text name="qualityCheckManagementBean" property="elevatorNo" style="border:0;" styleClass="default_input" readonly="true" /></td>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.ChecksPeople"/>:</td>
			<td width="20%"><html:text name="qualityCheckManagementBean" property="checksPeople" style="border:0;" styleClass="default_input" readonly="true"/></td>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.ChecksDateTime"/>:</td>
			<td width="20%"><html:text name="qualityCheckManagementBean" property="checksDateTime" style="border:0;" styleClass="default_input" readonly="true"/></td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.maintContractNo"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintContractNo" /></td>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.salesContractNo"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="salesContractNo" /></td>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.projectName"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="projectName" /></td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.maintDivision"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintDivision" /></td>
			<td class="wordtd"><bean:message key="elevatorArchivesInfo.maintStation"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintStation" /></td>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.MaintPersonnel"/>:</td>
			<td><bean:write name="qualityCheckManagementBean" property="maintPersonnel" /></td>
		</tr>
		<tr>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.TotalPoints"/>:</td>
			<td>
				<bean:write name="qualityCheckManagementBean" property="totalPoints" />
				
			</td>
			<td class="wordtd"><bean:message key="qualitycheckmanagement.ScoreLevel"/>:</td>
			<td colspan="3"><bean:write name="qualityCheckManagementBean" property="scoreLevel" /></td>
		</tr>        
    </table>
    <br>
    
    <table id="mtc" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <thead>
        <tr class="wordtd_header">
			<td width="9%"><bean:message key="markingitems.mtId"/></td>
			<td><bean:message key="markingitems.mtName"/></td>
			<td width="30"><bean:message key="markingitems.fraction"/></td>
			<td width="50"><bean:message key="qualitycheckmanagement.fraction"/></td>
			<td width="10%"><bean:message key="qualitycheckmanagement.appendix"/></td>
        </tr>
      </thead>
      <tbody>
        <logic:present name="markingItemsComplyList">
          <logic:iterate id="element1" name="markingItemsComplyList">
          
          	<tr>
	         <td align="center">${element1.mtId}</td>
	         <td align="left">${element1.mtName}</td>
	         <td align="center">${element1.fraction2}</td>
	         <td align="center">${element1.fraction}</td>
	         <td align="center">
	         	<logic:notEmpty name="element1" property="appendix">
				    <logic:notEqual value="4" name="element1" property="appendix">
				      	<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element1.appendix}','MarkingItemsComply')"><bean:message key="qualitycheckmanagement.check"/></a>
				    </logic:notEqual>
				</logic:notEmpty>
	         </td>
	        </tr>
          </logic:iterate>
        </logic:present>
      </tbody>    
    </table>
    <br>
    
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <thead>
        <tr class="wordtd_header">
			<td width="9%"><bean:message key="termsecurityrisks.tsrId"/></td>
			<td><bean:message key="termsecurityrisks.tsrDetail"/></td>
			<td width="10%"><bean:message key="qualitycheckmanagement.appendix"/></td>
        </tr>
      </thead>
      <tbody>
        <logic:present name="termSecurityRisksComplyList">
          <logic:iterate id="element2" name="termSecurityRisksComplyList" indexId="eid2">
	          <tr>
		         <td align="center"><input type="text" name="tsrcId" id="tsrcId" value="${element2.tsrId}" size="3" readonly="readonly" class="noborder"/></td>
		         <td align="left">${element2.tsrDetail}</td>
		         <td align="center">
		         <logic:notEmpty name="element2" property="appendix">
				    <logic:notEqual value="4" name="element2" property="appendix">
				      	<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element2.appendix}','TermSecurityRisksComply')"><bean:message key="qualitycheckmanagement.check"/></a>
				    </logic:notEqual>
				</logic:notEmpty>
		         </td>
	        </tr>
          </logic:iterate>
        </logic:present>
      </tbody>    
    </table>
    <br>
    
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <thead>
        <tr class="wordtd_header">
			<td width="9%"><bean:message key="shouldexamineitems.seiid"/></td>
			<td><bean:message key="shouldexamineitems.seiDetail"/></td>
			<td width="10%"><bean:message key="qualitycheckmanagement.appendix"/></td>
        </tr>
      </thead>
      <tbody>
        <logic:present name="shouldExamineItemsComplyList">
          <logic:iterate id="element3" name="shouldExamineItemsComplyList" indexId="eid3">
          	<tr>
	         <td align="center"><input type="text" name="seicid" id="seicid" value="${element3.seiid}" size="3" readonly="readonly" class="noborder"/></td>
	         <td align="left">${element3.seiDetail}</td>
	         <td align="center">
	         <logic:notEmpty name="element3" property="appendix">
				    <logic:notEqual value="4" name="element3" property="appendix">
				      	<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${element3.appendix}','ShouldExamineItemsComply')"><bean:message key="qualitycheckmanagement.check"/></a>
				    </logic:notEqual>
				</logic:notEmpty>
	         </td>
	        </tr>
          </logic:iterate>
        </logic:present>
      </tbody>    
    </table>
    <br>
    
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
        <td class="wordtd"><bean:message key="qualitycheckmanagement.SupervOpinion"/>:</td>
        <td><bean:write name="qualityCheckManagementBean" property="supervOpinion" /></td>
      </tr>
    </table>
    <br>
    
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
        <td class="wordtd"><bean:message key="qualitycheckmanagement.PartMinisters"/>:</td>
        <td width="30%"><bean:write name="qualityCheckManagementBean" property="partMinisters" /></td>
        <td class="wordtd"><bean:message key="qualitycheckmanagement.RemDate"/>:</td>
        <td><bean:write name="qualityCheckManagementBean" property="remDate" /></td>
      </tr>        
      <tr>
        <td class="wordtd"><bean:message key="qualitycheckmanagement.PartMinistersRem"/>:</td>
        <td colspan="3"><html:textarea property="partMinistersRem" rows="4" cols="100" /></td>      
      </tr>
    </table>
</html:form>