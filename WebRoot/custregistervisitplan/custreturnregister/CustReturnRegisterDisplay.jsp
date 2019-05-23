<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="custReturnRegisterMasterBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
<%--     <td nowrap="nowrap" class="wordtd"><bean:message key="custreturnregister.companyName"/>:</td>
    <td width="20%"><bean:write name="custReturnRegisterMasterBean" scope="request" property="r1"/>
   
    </td>
     --%>
    <td nowrap="nowrap" class="wordtd"><bean:message key="custreturnregister.contacts"/>:</td>
    <td width="20%"><bean:write name="custReturnRegisterMasterBean" scope="request" property="contacts"/>
    <input type="hidden" id="isreturn2" value='<bean:write name="custReturnRegisterMasterBean" scope="request" property="r2" />'>
     <input type="hidden" id="contactPhone" value='<bean:write name="custReturnRegisterMasterBean" scope="request" property="contactPhone" />'>
    </td>
    <td class="wordtd"><bean:message key="custreturnregister.contactPhone"/>:</td>
    <td width="20%"><bean:write name="custReturnRegisterMasterBean" scope="request" property="contactPhone"/></td>
 
    <td class="wordtd">&nbsp;</td>
    <td>&nbsp;</td>
     </tr>
  <tr>
    <td nowrap="nowrap" class="wordtd"><bean:message key="custreturnregister.operId"/>:</td>
    <td ><bean:write name="custReturnRegisterMasterBean" scope="request" property="operId"/></td>
    <td nowrap="nowrap" class="wordtd"><bean:message key="custreturnregister.operDate"/>:</td>
    <td ><bean:write name="custReturnRegisterMasterBean" scope="request" property="operDate"/></td>
    <td class="wordtd"><bean:message key="custreturnregisterhand.maintDivision"/>:</td>
    <td ><bean:write name="custReturnRegisterMasterBean" scope="request" property="maintDivision"/></td>
  </tr>
  <tr>
     <td  class="wordtd">开始回访结果:</td>
    <td class="inputtd" colspan="5"><bean:write name="custReturnRegisterMasterBean" scope="request" property="startResult"/></td>
	</tr>
 </table> 
 <br/>
  <logic:present name="custReturnRegisterDetailList">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
<logic:iterate id="ele" name="custReturnRegisterDetailList">

<tr>
    <td class="wordtd">合同号:</td>
    <td width="20%">
    	<logic:equal name="ele" property="r4" value="委托">
	            <a onclick="contractdisplay('wt','${ele.maintContractNo}');" class="link">${ele.maintContractNo}</a>
	    </logic:equal>
	    <logic:equal name="ele" property="r4" value="维保">
	            <a onclick="contractdisplay('wb','${ele.billno}');" class="link">${ele.maintContractNo}</a>
	    </logic:equal>
    </td>
    <td class="wordtd">合同日期:</td>
    <td width="20%" class="inputtd" >
    	<bean:write name="ele" property="contractSdate"/>&nbsp;到&nbsp;<bean:write name="ele" property="contractEdate"/>
    </td>
    <td class="wordtd">合同类型:</td>
	<td width="20%" class="inputtd">
    	<bean:write name="ele" property="r4"/>
    	<html:hidden name="ele" property="r4"/>
    </td>
</tr>
 
</logic:iterate>
 <logic:present name="custReturnRegisterLssues">
 <tr>
     <td class="wordtd" style="text-align: center;">问题分类</td>
    <td class="wordtd" colspan="5" style="text-align: center;">问题详情</td>
 </tr>
  <logic:iterate id="ele2"  name="custReturnRegisterLssues" >
  <tr>
     
  <td class="inputtd"><bean:write name="ele2" property="lssueSort"/></td>
    <td class="inputtd" colspan="5" ><bean:write name="ele2" property="lssueDetail"/></td>
  </tr>
   </logic:iterate>
  </logic:present>
</table>
<br/>

</logic:present>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">

  <tr>
  <td class="wordtd"><bean:message key="custreturnregister.ministerHandle"/>:</td>
    <td width="20%">
	<logic:match name="custReturnRegisterMasterBean" property="ministerHandle" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="custReturnRegisterMasterBean" property="ministerHandle" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
    <td nowrap="nowrap" class="wordtd"><bean:message key="custreturnregister.rem"/>:</td>
    <td ><bean:write name="custReturnRegisterMasterBean" scope="request" property="rem"/></td>
   </tr>
</table>
 <br/>
 
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
 <tr>
    <td  class="wordtd"><bean:message key="custreturnregister.handleId"/>:</td>
    <td width="20%" class="inputtd"><bean:write name="custReturnRegisterMasterBean" scope="request" property="handleId"/></td>
    <td  class="wordtd"><bean:message key="custreturnregister.handleDate"/>:</td>
    <td width="20%" class="inputtd"><bean:write name="custReturnRegisterMasterBean" scope="request" property="handleDate"/></td>
     <td nowrap="nowrap" class="wordtd"></td>
    <td width="20%" class="inputtd">&nbsp;&nbsp;</td>
 </tr>
  <tr>
      <td  class="wordtd"><bean:message key="custreturnregister.handleResult"/>:</td>
    <td colspan="5" class="inputtd"><bean:write name="custReturnRegisterMasterBean" scope="request" property="handleResult"/></td>
  </tr>  
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
<tr>
     <td  class="wordtd">反馈回访结果:</td>
    <td colspan="3" class="inputtd"><bean:write name="custReturnRegisterMasterBean" scope="request" property="returnResult"/></td>
</tr>
<tr>
     <td  class="wordtd">反馈回访备注:</td>
    <td colspan="3" class="inputtd"><bean:write name="custReturnRegisterMasterBean" scope="request" property="returnRem"/></td>
</tr>
</table>
</logic:present>