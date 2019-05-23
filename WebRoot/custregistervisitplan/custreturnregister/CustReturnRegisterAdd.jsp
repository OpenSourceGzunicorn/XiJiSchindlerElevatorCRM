<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/custReturnRegisterAction.do?method=toAddRecord">
<input type="hidden" id="isreturn2" value="">
<input type="hidden" id="rmbillno" name="rmbillno" value="${rmbillno }">

<html:hidden property="isreturn"/>
<logic:present name="custReturnRegisterList2">
<logic:iterate id="ele" name="custReturnRegisterList2">

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
     
    <td nowrap="nowrap" class="wordtd"><bean:message key="custreturnregister.contacts"/>:</td>
    <td width="20%"><html:hidden name="ele" property="comId"/>
    <bean:write name="ele" property="contacts"/>
    <input type="hidden" name="contacts1" id="contacts1" value="${ele.contacts }">
    </td>
    <td class="wordtd"><bean:message key="custreturnregister.contactPhone"/>:</td>
    <td  width="20%">
    <bean:write name="ele" property="contactPhone"/>
    <input type="hidden" name="contactPhone1" id="contactPhone1" value="${ele.contactPhone }">
    </td>
  
    <td  class="wordtd">&nbsp;</td>
    <td>&nbsp;</td>
    </tr>
    <tr>
	    <td  class="wordtd">客户回访维护备注:</td>
	    <td colspan="5"><bean:write name="ele" property="mainrem"/></td>
    </tr>
  <tr>
    <td  class="wordtd"><bean:message key="custreturnregister.operId"/>:</td>
    <td><bean:write name="ele" property="username"/></td>
    <td  class="wordtd"><bean:message key="custreturnregister.operDate"/>:</td>
    <td><bean:write name="ele" property="operdate"/></td>
    <td class="wordtd"><bean:message key="custreturnregisterhand.maintDivision"/>:</td>
    <td><bean:write name="ele" property="comname"/></td>
  </tr>
  	<tr>
     <td  class="wordtd">开始回访结果:</td>
    <td class="inputtd" colspan="5" style="text-align: center;"><textarea name="startResult" rows="2" cols="50" styleClass="default_textarea" style=" width: 100%"></textarea></td>
	</tr>
 </table> 
 <br/>
  <logic:present name="ele" property="detailList">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
<logic:iterate id="ele2" name="ele" property="detailList">

<tr>
    <td class="wordtd">合同号:</td>
    <td width="20%">
    	<logic:equal name="ele2" property="r4" value="委托">
	            <a onclick="contractdisplay('wt','${ele2.maintContractNo}');" class="link">${ele2.maintContractNo}</a>
	    </logic:equal>
	    <logic:equal name="ele2" property="r4" value="维保">
	            <a onclick="contractdisplay('wb','${ele2.billNo}');" class="link">${ele2.maintContractNo}</a>
	    </logic:equal>
    	<html:hidden name="ele2" property="maintContractNo"/>
    </td>
    <td nowrap="nowrap" class="wordtd">合同日期:</td>
    <td width="20%">
    	<bean:write name="ele2" property="contractSdate"/>&nbsp;到&nbsp;<bean:write name="ele2" property="contractEdate"/>
    	<html:hidden name="ele2" property="contractSdate"/>
    	<html:hidden name="ele2" property="contractEdate"/>
    </td>
    <td class="wordtd">合同类型:</td>
	<td>
    	<bean:write name="ele2" property="r4"/>
    	<html:hidden name="ele2" property="r4"/>
  <html:hidden name="ele2" property="billNo"/>
    </td>
</tr>

</logic:iterate>
  <logic:present name="custReturnRegisterLssueLssueSort">
 <tr>
     <td class="wordtd" style="text-align: center;">问题分类</td>
    <td class="wordtd" colspan="5" style="text-align: center;">问题详情</td>
 </tr>
   <logic:iterate id="ele3" name="custReturnRegisterLssueLssueSort">
  <tr>
     
  <td class="inputtd"><bean:write name="ele3" property="pullname"/>
  <html:hidden name="ele3" property="id.pullid" /></td>
    <td class="inputtd" colspan="5" style="text-align: center;"><textarea name="lssueDetail" rows="2" cols="50" styleClass="default_textarea" style=" width: 100%"></textarea></td>
  </tr>
   </logic:iterate>
   </logic:present>
</table>
<br/>
</logic:present>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">

  <tr>
  <td  class="wordtd"><bean:message key="custreturnregister.ministerHandle"/>:</td>
    <td width="20%" >
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
    <td  class="wordtd"><bean:message key="custreturnregister.rem"/>:</td>
    <td><html:textarea property="rem" rows="2" cols="130" styleClass="default_textarea" style=" width: 100%"></html:textarea></td>
  </tr>
</table>
 <br/>
 
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
 <tr>
    <td  class="wordtd"><bean:message key="custreturnregister.handleId"/>:</td>
    <td width="20%" class="inputtd"><bean:write name="ele" property="handleId"/> </td>
    <td  class="wordtd"><bean:message key="custreturnregister.handleDate"/>:</td>
    <td width="20%" class="inputtd"><bean:write name="ele" property="handleDate"/> </td>
     <td nowrap="nowrap" class="wordtd"></td>
    <td width="20%" class="inputtd"></td>
 </tr>
  <tr>
      <td  class="wordtd"><bean:message key="custreturnregister.handleResult"/>:</td>
    <td colspan="5" class="inputtd"><bean:write name="ele" property="handleResult"/> </td>
  </tr>  
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
<tr>
     <td  class="wordtd">反馈回访结果:</td>
    <td colspan="3" class="inputtd"></td>
</tr>
<tr>
     <td  class="wordtd">反馈回访备注:</td>
    <td colspan="3" class="inputtd"></td>
</tr>
</table>
</logic:iterate>
</logic:present>

</html:form>