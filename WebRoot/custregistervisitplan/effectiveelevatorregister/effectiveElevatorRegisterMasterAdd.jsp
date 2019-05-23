<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/effectiveElevatorRegisterMasterAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<input type="hidden" id="isreturn2" value="">
<%-- <logic:present name="contract"> --%>
<%-- <html:hidden property="lostElevators"/> --%>
<logic:present name="custReturnRegisterList2">
<logic:iterate id="ele" name="custReturnRegisterList2">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
   <td nowrap="nowrap" class="wordtd"><bean:message key="custreturnregister.companyName"/>:</td>
    <td width="20%" >
    <bean:write name="ele" property="companyName"/>
    <html:hidden name="ele" property="companyName"/>
    <%-- <html:hidden name="ele" property="comId"/> --%>
     </td>
    <td nowrap="nowrap" class="wordtd"><bean:message key="custreturnregister.contacts"/>:</td>
    <td width="20%">
    	<bean:write name="ele" property="contacts"/>
    	<html:hidden name="ele" property="contacts"/>
    </td>
    <td class="wordtd"><bean:message key="custreturnregister.contactPhone"/>:</td>
    <td  width="20%">
    	<bean:write name="ele" property="contactPhone"/>
    	<html:hidden name="ele" property="contactPhone"/>
    </td>
  </tr>
  <tr>
    <td  class="wordtd"><bean:message key="custreturnregister.ministerHandle"/>:</td>
    <td>
				<html:select property="ministerHandle" styleId="ministerHandle" style="width: 100px">
					<html:option value="">
						请选择
					</html:option>
					<html:option value="Y">
						<bean:message key="pageword.yes" />
					</html:option>
					<html:option value="N">
						<bean:message key="pageword.no" />
					</html:option>
				</html:select> 
				<font color="red">*</font>
    </td>
    <td class="wordtd"><bean:message key="custreturnregister.operId"/>:</td>
    <td>
    	<bean:write name="ele" property="username"/>
    	<html:hidden name="ele" property="operId"/>
    </td>
    <td class="wordtd"><bean:message key="custreturnregister.operDate"/>:</td>
    <td>
    	<bean:write name="ele" property="operDate"/>
    	<html:hidden name="ele" property="operDate"/>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="custreturnregister.rem"/>:</td>
    <td colspan="5"><html:textarea property="rem" rows="2" cols="100" styleClass="default_textarea" style=" width: 100%"></html:textarea></td>
    <%-- <td class="wordtd"><bean:message key="custreturnregisterhand.maintDivision"/>:</td>
    <td>
    	<bean:write name="ele" property="comname"/>
    	<html:hidden name="ele" property="maintDivision"/>
    </td> --%>
  </tr>
 </table> 
 <br/>
  <%-- <logic:present name="ele" property="detailList">
<logic:iterate id="ele2" name="ele" property="detailList">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">

<tr>
    <td class="wordtd">维保合同号:</td>
    <td width="20%">
    	<a onclick="simpleOpenWindow('lostElevatorReportAction','${ele2.dtJnlno}');" class="link">
    		<bean:write name="ele2" property="maintContractNo"/>
    	</a>
    	<html:hidden name="ele2" property="maintContractNo"/>
    	<html:hidden name="ele2" property="wbBillno"/>
    	<html:hidden name="ele2" property="dtJnlno"/>
    </td>
    <td nowrap="nowrap" class="wordtd">丢梯日期:</td>
    <td width="20%">
    	<bean:write name="ele2" property="lostElevatorDate"/>
    	<html:hidden name="ele2" property="lostElevatorDate"/>
    </td>
    <td class="wordtd">原因分析:</td>
    <td width="20%">
    	<bean:write name="ele2" property="causeAnalysisName"/>
    	<html:hidden name="ele2" property="causeAnalysis"/>
    </td>
</tr>
  <logic:present name="custReturnRegisterLssueLssueSort">
 <tr>
    <td class="wordtd">回访详情:</td>
    <td colspan="5">
    	<textarea name="lssueDetail" rows="4" cols="100" styleClass="default_textarea" style=" width: 100%"></textarea>
    </td>
 </tr>
   <logic:iterate id="ele3" name="custReturnRegisterLssueLssueSort">
  <tr>
     
  <td class="inputtd"><bean:write name="ele3" property="pullname"/>
  <html:hidden name="ele2" property="billNo"/>
  <html:hidden name="ele3" property="id.pullid" /></td>
    <td class="inputtd" colspan="5" style="text-align: center;"><textarea name="lssueDetail" rows="2" cols="50" styleClass="default_textarea" style=" width: 100%"></textarea></td>
  </tr>
   </logic:iterate>
   </logic:present>
</table>
<br/>
</logic:iterate>
</logic:present> --%>

 
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
 <tr>
    <td class="wordtd"><bean:message key="custreturnregister.handleId"/>:</td>
    <td width="20%"><bean:write name="ele" property="handleId"/> </td>
    <td class="wordtd"><bean:message key="custreturnregister.handleDate"/>:</td>
    <td width="20%"><bean:write name="ele" property="handleDate"/> </td>
    <td nowrap="nowrap" class="wordtd">&nbsp;</td>
    <td width="20%" >&nbsp;</td>
 </tr>
  <tr>
      <td  class="wordtd"><bean:message key="custreturnregister.handleResult"/>:</td>
    <td colspan="5" class="inputtd"><bean:write name="ele" property="handleResult"/> </td>
  </tr>  
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
<tr>
     <td  class="wordtd"><bean:message key="custreturnregister.returnResult"/>:</td>
    <td colspan="3" class="inputtd"></td>
</tr>
<tr>
     <td  class="wordtd">回访备注:</td>
    <td colspan="3" class="inputtd"></td>
</tr>
</table>
</logic:iterate>
</logic:present>
<%-- </logic:present> --%>
<%-- <logic:notPresent name="contract"><font color="red">该回访记录的丢梯报告不存在，请返回!</font></logic:notPresent> --%>
</html:form>