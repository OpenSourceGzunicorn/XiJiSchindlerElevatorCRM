<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/accessoriesRequisitionStoremanAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${accessoriesRequisitionBean.appNo}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  
  <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.singleNo"/>:</td>
    <td style="width: 35%">
    	<bean:write name="accessoriesRequisitionBean" scope="request" property="singleNo"/>
        <html:hidden name="accessoriesRequisitionBean" property="appNo"/>
    </td>
    <td class="wordtd">申请次数:</td>
    <td ><bean:write name="accessoriesRequisitionBean" scope="request" property="r7"/></td>
    
 </tr>
  <tr>
  <td class="wordtd">电梯编号:</td>
    <td style="width: 35%">
    	<logic:notEqual name="contracthmap" property="billno" value="">
    		<a href="<html:rewrite page='/maintContractAction.do'/>?method=toDisplayRecord&typejsp=Yes&id=<bean:write name="contracthmap"  property="billno"/>" target="_blnk">${accessoriesRequisitionBean.elevatorNo }</a>
    	</logic:notEqual>
    	<logic:equal name="contracthmap" property="billno" value="">
    		<bean:write name="accessoriesRequisitionBean" scope="request" property="elevatorNo"/>
    	</logic:equal>
    </td>
    <td class="wordtd">有偿/免保:</td> 
    <td>
	    <logic:equal name="contracthmap" property="mainmode" value="PAID">有偿</logic:equal>
	    <logic:equal name="contracthmap" property="mainmode" value="FREE">免保</logic:equal>
    </td>
 </tr>
   <tr>
    <td class="wordtd">合同结束日期:</td>
    <td colspan="3" style="width: 35%"><bean:write name="contracthmap" property="contractedate" /></td>
 </tr>
 <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.oldNo"/>:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="oldNo"/></td>
    <td class="wordtd"><bean:message key="accessoriesRequisition.oldImage"/>:</td> 
    <td>
    <logic:present name="olgimglist">
	    <logic:iterate id="ele" name="olgimglist" indexId="eid">
	    	<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${ele.filePath}${ele.newFileName}')">图片${eid+1 }</a>
	    </logic:iterate>
    </logic:present>
	</td>
 </tr>
 <tr>
    <td class="wordtd">新件名称/型号:</td>
    <td style="width: 35%">
    	<html:textarea name="accessoriesRequisitionBean" property="newNo" styleId="newNo" rows="3" cols="50" styleClass="default_textarea" value="${accessoriesRequisitionBean.r5 }"/><font color=red>*</font>
    </td>
    <td class="wordtd">&nbsp;</td>
    <td>&nbsp;</td>
 </tr>
  <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.operId"/>:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="operId"/></td>
    <td class="wordtd"><bean:message key="accessoriesRequisition.operDate"/>:</td>
    <td><bean:write name="accessoriesRequisitionBean" scope="request" property="operDate"/></td>
 </tr>
 
  <tr>
    <td class="wordtd"><bean:message key="technologySupport.maintDivision"/>:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="maintDivision"/></td>
    <td class="wordtd"><bean:message key="technologySupport.maintStation"/>:</td>
    <td><bean:write name="accessoriesRequisitionBean" scope="request" property="maintStation"/></td>
 </tr>
 </tr>
    <tr>
    <td class="wordtd">备件名称及型号/备注:</td>
    <td style="width: 35%"><bean:write name="r5str" scope="request" filter="false"/></td>
    <td class="wordtd">初步判断是否收费:</td>
    <td style="width: 35%">
    	<logic:equal name="accessoriesRequisitionBean" property="isCharges" value="Y">收费</logic:equal>
    	<logic:equal name="accessoriesRequisitionBean" property="isCharges" value="N">免费</logic:equal>
    </td>
 </tr>
 <tr>
    <td class="wordtd">故障描述:</td>
    <td ><bean:write name="accessoriesRequisitionBean" scope="request" property="r4"/></td>
    <td class="wordtd">收费金额:</td>
    <td><bean:write name="accessoriesRequisitionBean" scope="request" property="money2"/></td>
 </tr>
 <tr>
    <td class="wordtd">发票类型:</td>
    <td ><bean:write name="accessoriesRequisitionBean" scope="request" property="invoicetype"/></td>
    <td class="wordtd">甲方开票名称:</td>
    <td ><bean:write name="accessoriesRequisitionBean" scope="request" property="expressName"/>
    </td>
 </tr>
  <tr>
    <td class="wordtd">邮寄地址及电话:</td>
    <td ><bean:write name="accessoriesRequisitionBean" scope="request" property="yjaddress"/></td>
    <td class="wordtd">开票信息图片:</td>
    <td>
    <logic:present name="newimglist">
	    <logic:iterate id="ele2" name="invoiceImagelist" indexId="eid2">
	    	<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${ele2.filePath}${ele2.newFileName}')">图片${eid2+1 }</a>
	    </logic:iterate>
    </logic:present>
    </td>
 </tr>
 <tr>
    <td class="wordtd">备件库是否有库存:</td>
    <td style="width: 35%">
    	<html:select name="accessoriesRequisitionBean" property="instock" styleId="instock"> 
          <html:option value=""><bean:message key="pageword.pleaseselect"/></html:option>
          <html:option value="有">有</html:option>
          <html:option value="无">无</html:option>
    	</html:select><font color=red>*</font>
    </td>
    <td class="wordtd">项目名称及楼栋号:</td>
    <td ><bean:write name="elerem" scope="request"/></td>
 </tr>
 </table>
 <br/>
 
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
 <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.isAgree"/>:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="isAgree"/></td>
    <%-- 
    <td class="wordtd">初步判定金额:</td>
    <td><bean:write name="accessoriesRequisitionBean" scope="request" property="money1"/></td>
    
    <td class="wordtd">当地备件库是否有该备件:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="r1"/></td>
    --%>
 </tr>
  <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.personInCharge"/>:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="personInCharge"/></td>
    <td class="wordtd"><bean:message key="accessoriesRequisition.picauditDate"/>:</td>
    <td ><bean:write name="accessoriesRequisitionBean" scope="request" property="picauditDate"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.picauditRem"/>:</td>
    <td colspan="3"><bean:write name="accessoriesRequisitionBean" scope="request" property="picauditRem"/></td>
 </tr>

 </table>

  <br>
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.isAgree"/>:</td>
    <td>
    	<html:select name="accessoriesRequisitionBean" property="wmIsAgree" styleId="wmIsAgree"> 
          <html:option value=""><bean:message key="pageword.pleaseselect"/></html:option>
          <html:option value="Y">同意</html:option>
          <html:option value="N">不同意</html:option>
    	</html:select><font color=red>*</font>
    </td>
    
    <td class="wordtd">领取方式:</td>
    <td style="width: 35%">
    	<html:select name="accessoriesRequisitionBean" property="wmPayment" styleId="wmPayment"> 
          <html:option value=""><bean:message key="pageword.pleaseselect"/></html:option>
          <html:option value="1">分库直接领取</html:option>
          <html:option value="2">总库调拨</html:option>
          <html:option value="3">外购</html:option>
    	</html:select><font color=red>*</font>
    </td>
 </tr>
 <%--
 <tr>
    <td class="wordtd">当地备件库是否有该备件:</td>
    <td style="width: 35%">
    	<html:select name="accessoriesRequisitionBean" property="r3" styleId="r3"> 
          <html:option value=""><bean:message key="pageword.pleaseselect"/></html:option>
          <html:option value="Y">有</html:option>
          <html:option value="N">无</html:option>
    	</html:select><font color=red>*</font>
    </td>
    <td class="wordtd">收费金额:</td>
    <td><html:text property="money2" styleId="money2" styleClass="default_input" size="30"/></td>
 </tr>
 --%>
 <tr>
    <td class="wordtd">配件库管理员:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="warehouseManager"/></td>
    <td class="wordtd">配件库管理员审核日期:</td>
    <td><bean:write name="accessoriesRequisitionBean" scope="request" property="wmdate"/></td>
 </tr>
 <tr>
    <td class="wordtd">配件库管理员审核意见:</td>
    <td colspan="3">
		<html:textarea name="accessoriesRequisitionBean" property="wmRem" styleId="wmRem" rows="3" cols="100" styleClass="default_textarea"></html:textarea>
    </td>
 </tr>
 </table>
 
 </html:form>