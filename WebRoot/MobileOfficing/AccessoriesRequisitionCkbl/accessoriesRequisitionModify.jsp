<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/accessoriesRequisitionCkblAction.do?method=toUpdateRecord">
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
    <td colspan="3"><bean:write name="contracthmap" property="contractedate" /></td>
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
    <td class="wordtd"><bean:message key="accessoriesRequisition.newNo"/>:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="newNo"/></td>
    <td class="wordtd"><bean:message key="accessoriesRequisition.newImage"/>:</td>
    <td>
    <logic:present name="newimglist">
	    <logic:iterate id="ele" name="newimglist" indexId="eid">
	    	<a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${ele.filePath}${ele.newFileName}')">图片${eid+1 }</a>
	    </logic:iterate>
    </logic:present>
 	</td>
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
 </tr>   <tr>
    <td class="wordtd">维保工确认状态:</td>
    <td ><bean:write name="accessoriesRequisitionBean" scope="request" property="isConfirm"/></td>
    <td class="wordtd">初步判断是否收费:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="isCharges"/></td>
 </tr>
  <tr>
    <td class="wordtd">备件名称及型号/备注:</td>
    <td ><bean:write name="accessoriesRequisitionBean" scope="request" property="r5" filter="false"/></td>
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
    <td class="wordtd">故障描述:</td>
    <td><bean:write name="accessoriesRequisitionBean" scope="request" property="r4"/></td>
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
    <td class="wordtd">邮寄地址及电话:</td>
    <td ><bean:write name="accessoriesRequisitionBean" scope="request" property="yjaddress"/></td>
    <td class="wordtd">项目名称及楼栋号:</td>
    <td ><bean:write name="elerem" scope="request"/></td>
 </tr>
 <tr>
    <td class="wordtd">备件库是否有库存:</td>
    <td colspan="3"><bean:write name="accessoriesRequisitionBean" scope="request" property="instock"/></td>
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
    <td><bean:write name="accessoriesRequisitionBean" scope="request" property="wmIsAgree"/></td>
    <td class="wordtd">领取方式:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="wmPayment"/></td>
 </tr>
 <%-- 
   <tr>
    <td class="wordtd">当地备件库是否有该备件:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="r3"/></td>
    <td class="wordtd">收费金额:</td>
    <td><bean:write name="accessoriesRequisitionBean" scope="request" property="money2"/></td>
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
    <td colspan="3"><bean:write name="accessoriesRequisitionBean" scope="request" property="wmRem"/></td>
 </tr>
 </table>
 
   <br>
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
 <tr>
    <td class="wordtd">旧件退回:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="jjReturn"/></td>
    <td class="wordtd">旧件检测结果:</td>
    <td><bean:write name="accessoriesRequisitionBean" scope="request" property="jjResult"/></td>
 </tr>
 <tr>
    <td class="wordtd">旧件退回处理人:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="jjOperId"/></td>
    <td class="wordtd">旧件退回处理日期:</td>
    <td><bean:write name="accessoriesRequisitionBean" scope="request" property="jjOperDate"/></td>
 </tr>
 </table>
 
    <br>
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd">出库办理是否完成:</td>
    <td>
    	<html:select name="accessoriesRequisitionBean" property="ckiswc" styleId="ckiswc"> 
          <html:option value=""><bean:message key="pageword.pleaseselect"/></html:option>
          <html:option value="Y">已完成</html:option>
          <html:option value="N">未完成</html:option>
    	</html:select><font color=red>*</font>
    </td>
    
    <td class="wordtd">&nbsp;</td>
    <td style="width: 35%">&nbsp;</td>
 </tr>
 <tr>
    <td class="wordtd">出库办理人:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="ckoperid"/></td>
    <td class="wordtd">出库办理日期:</td>
    <td><bean:write name="accessoriesRequisitionBean" scope="request" property="ckdate"/></td>
 </tr>
 <tr>
    <td class="wordtd">出库办理意见:</td>
    <td colspan="3">
		<html:textarea name="accessoriesRequisitionBean" property="ckrem" styleId="ckrem" rows="3" cols="100" styleClass="default_textarea"></html:textarea>
    </td>
 </tr>
 </table>
 
 </html:form>
 
 
 
 