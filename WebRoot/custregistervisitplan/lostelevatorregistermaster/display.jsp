<%@ page contentType="text/html;charset=GBK" %>
<%-- <%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %> --%>


<logic:present name="custReturnRegisterMasterBean">
<html:hidden name="custReturnRegisterMasterBean" property="billno"/>
<%-- <input type="hidden" name="billno" value='<bean:write name="custReturnRegisterMasterBean" scope="request" property="billno" />'> --%>
<html:hidden property="id" value="${custReturnRegisterMasterBean.billno}"/><!--  value="${custReturnRegisterMasterBean.billno}" -->
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td nowrap="nowrap" class="wordtd"><bean:message key="custreturnregister.companyName"/>:</td>
    <td width="20%"><bean:write name="custReturnRegisterMasterBean" scope="request" property="r1"/>
    <input type="hidden" id="companyId" value='<bean:write name="custReturnRegisterMasterBean" scope="request" property="companyId" />'>
    <input type="hidden" id="isreturn2" value='<bean:write name="custReturnRegisterMasterBean" scope="request" property="r4" />'>
    </td>
    
    <td nowrap="nowrap" class="wordtd"><bean:message key="custreturnregister.contacts"/>:</td>
    <td width="20%"><bean:write name="custReturnRegisterMasterBean" scope="request" property="contacts"/></td>
    <td class="wordtd"><bean:message key="custreturnregister.contactPhone"/>:</td>
    <td width="20%"><bean:write name="custReturnRegisterMasterBean" scope="request" property="contactPhone"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="custreturnregister.ministerHandle"/>:</td>
    <td>
    <input type="hidden" name="ministerHandle" value="${custReturnRegisterMasterBean.ministerHandle}"/>
	<logic:match name="custReturnRegisterMasterBean" property="ministerHandle" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="custReturnRegisterMasterBean" property="ministerHandle" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
    <td nowrap="nowrap" class="wordtd"><bean:message key="custreturnregister.rem"/>:</td>
    <td colspan="3"><bean:write name="custReturnRegisterMasterBean" scope="request" property="rem"/></td>
  </tr>
  <tr>
    <td nowrap="nowrap" class="wordtd"><bean:message key="custreturnregister.operId"/>:</td>
    <td ><bean:write name="custReturnRegisterMasterBean" scope="request" property="r3"/></td>
    <td nowrap="nowrap" class="wordtd"><bean:message key="custreturnregister.operDate"/>:</td>
    <td ><bean:write name="custReturnRegisterMasterBean" scope="request" property="operDate"/></td>
    <td class="wordtd"><bean:message key="custreturnregisterhand.maintDivision"/>:</td>
    <td ><bean:write name="custReturnRegisterMasterBean" scope="request" property="maintDivision"/></td>
  </tr>
 </table> 
 <br/>
  <logic:present name="custReturnRegisterDetailList">
<logic:iterate id="ele" name="custReturnRegisterDetailList">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">

<tr>
    <td class="wordtd">维保合同号:</td>
    <td width="20%">
    	<a onclick="simpleOpenWindow('lostElevatorReportAction','${ele.dtJnlno}');" class="link">
    		<bean:write name="ele" property="maintContractNo"/>
    	</a>
    </td>
    <td nowrap="nowrap" class="wordtd">丢梯日期:</td>
    <td width="20%"><bean:write name="ele" property="lostElevatorDate"/></td>
    <td class="wordtd">原因分析:</td>
    <td width="20%"><bean:write name="ele" property="causeAnalysis"/></td>
</tr>
 
 <tr>
     <td class="wordtd">回访详情:</td>
    <logic:iterate id="ele2" name="ele" property="custReturnRegisterLssues" >
	    <td colspan="5">
	    	<bean:write name="ele2" property="lssueDetail"/>
	    </td>
    </logic:iterate>
 </tr>
   <%-- <logic:iterate id="ele2" name="ele" property="custReturnRegisterLssues" >
  <tr>
     
  <td class="inputtd"><bean:write name="ele2" property="lssueSort"/></td>
    <td class="inputtd" colspan="5" ><bean:write name="ele2" property="lssueDetail"/></td>
  </tr>
   </logic:iterate> --%>
</table>
<br/>

</logic:iterate>
</logic:present>
 
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
 <tr>
    <td nowrap="nowrap"  class="wordtd"><bean:message key="custreturnregister.handleId"/>:</td>
    <td width="20%" class="inputtd">
    	<bean:write name="custReturnRegisterMasterBean" scope="request" property="r2"/>
    	<html:hidden name="custReturnRegisterMasterBean" property="handleId"/>
    </td>
    <td  nowrap="nowrap"  class="wordtd"><bean:message key="custreturnregister.handleDate"/>:</td>
    <td width="20%" class="inputtd">
    	<bean:write name="custReturnRegisterMasterBean" scope="request" property="handleDate"/>
    	<html:hidden name="custReturnRegisterMasterBean" property="handleDate"/>
    </td>
    <td nowrap="nowrap" class="wordtd"></td>
    <td width="20%" class="inputtd">&nbsp;&nbsp;</td>
 </tr>
  <tr>
    <td  class="wordtd"><bean:message key="custreturnregister.handleResult"/>:</td>
    <td colspan="5">
    	<logic:notPresent name="ishandle">
    		<bean:write name="custReturnRegisterMasterBean" scope="request" property="handleResult"/>
    	</logic:notPresent>
    	<logic:present name="ishandle">
    		<html:textarea name="custReturnRegisterMasterBean" property="handleResult" rows="3" cols="50" styleClass="default_textarea" style=" width: 100%"></html:textarea>
    	</logic:present>
    </td>
  </tr>  
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
<tr>
    <td  class="wordtd">回访结果:</td>
    <td colspan="3" class="inputtd">
    	<logic:notPresent name="isfeedback">
    		<%-- <bean:write name="custReturnRegisterMasterBean" scope="request" property="returnResult"/> --%>
    		<logic:present name="custReturnRegisterMasterBean" property="returnResult">
    			<logic:match name="custReturnRegisterMasterBean" property="returnResult" value="Y">解决</logic:match>
    			<logic:match name="custReturnRegisterMasterBean" property="returnResult" value="N">未解决</logic:match>
    		</logic:present>
    	</logic:notPresent>
    	<logic:present name="isfeedback">
    		<html:radio name="custReturnRegisterMasterBean" property="returnResult" value="Y"/>解决
    		<html:radio name="custReturnRegisterMasterBean" property="returnResult" value="N"/>未解决
    	</logic:present>
    </td>
</tr>
<tr>
    <td  class="wordtd">回访备注:</td>
    <td colspan="3">
    	<logic:notPresent name="isfeedback">
    		<bean:write name="custReturnRegisterMasterBean" scope="request" property="returnRem"/>
    	</logic:notPresent>
    	<logic:present name="isfeedback">
    		<html:textarea name="custReturnRegisterMasterBean" property="returnRem" rows="3" cols="50" styleClass="default_textarea" style=" width: 100%"></html:textarea>
    	</logic:present>
    </td>
</tr>
</table>
</logic:present>