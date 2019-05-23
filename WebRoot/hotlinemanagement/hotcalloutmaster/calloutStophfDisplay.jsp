<%@page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"";
%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<link href="/XJSCRM/common/css/bb.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='contractCSS'/>">
<script language="javascript" src="<html:rewrite forward='calendarJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>

<html:errors/> 
<html:form action="/hotphoneAction.do?method=tosavestophf" enctype="multipart/form-data">
<br>
<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<table width="99%" class="tb"  height="25">
		<tr>
			<td  align="center" width="90%"><b>主表单信息</b></td>
		</tr>
</table>
<table  width="99%" border="0" cellpadding="0" cellspacing="0" class="tb" >
<tr>
    <td class="wordtd_a" width="10%">报修单号:</td>
    <td width="23%">
     ${CalloutMasterList.calloutMasterNo }
     <input type="hidden" name="calloutMasterNo" value="${CalloutMasterList.calloutMasterNo }"/>
    </td>
    <td class="wordtd_a" width="10%">录入人:</td>
    <td width="23%">
     ${CalloutMasterList.operId }
    </td>
    <td class="wordtd_a" width="10%">报修时间:</td>
    <td width="23%">
    ${CalloutMasterList.repairTime }
    </td>
</tr>
<logic:notEqual name="typejsp" value="sh">
<tr>
    <td class="wordtd_a">报修方式:</td>
    <td >
     ${CalloutMasterList.repairMode }
    </td>
    <td class="wordtd_a">报修人:</td>
    <td >
     ${CalloutMasterList.repairUser }
    </td>
    <td class="wordtd_a">报修电话:</td>
    <td >
     ${CalloutMasterList.repairTel }
    </td>
</tr>
</logic:notEqual>
<logic:equal name="typejsp" value="sh">
<tr>
    <td class="wordtd_a" >报修方式:</td>
    <td >
    <html:select name="CalloutMasterList" property="repairMode">
     <html:options collection="rmList" property="id.pullid" labelProperty="pullname"></html:options>
     </html:select><font color="red">*</font>
    </td>
    <td class="wordtd_a" >报修人:</td>
    <td >
    <div id="bxp">
     <input type="text" name="repairUser" value="${CalloutMasterList.repairUser }" class="default_input"><font color="red">*</font>
    </div>
    </td>
    <td class="wordtd_a" >报修电话:</td>
    <td >
    <div id="bxt">
    <input type="text" name="repairTel" value="${CalloutMasterList.repairTel }" ><font color="red">*</font>
    </div>
    </td> 
</tr>
</logic:equal>
<tr>
    <td class="wordtd_a">服务对象:</td>
    <td >
    <logic:equal name="typejsp" value="sh">
    <html:select name="CalloutMasterList" property="serviceObjects" styleId="serviceObjects">
     <html:options collection="soList" property="id.pullid" labelProperty="pullname"></html:options>
     </html:select>
    </logic:equal>
    <logic:notEqual name="typejsp" value="sh">
     ${CalloutMasterList.serviceObjects } 
     </logic:notEqual>
    </td>
    <td class="wordtd_a">报修单位:</td>
    <td >
    ${CalloutMasterList.companyId } 
    </td>
    <td class="wordtd_a">项目名称:</td>
    <td >
    ${CalloutMasterList.projectName }
    </td>
</tr>
<tr>
    <td class="wordtd_a">销售合同号:</td>
    <td >
    ${CalloutMasterList.salesContractNo }
    </td>
    <td class="wordtd_a">电梯编号:</td>
    <td >
    ${CalloutMasterList.elevatorNo }
    </td>
    <td class="wordtd_a">规格型号:</td>
    <td >
    ${CalloutMasterList.elevatorParam }
    </td>
</tr>
<tr>
    <td class="wordtd_a">维保站:</td>
    <td >
    ${CalloutMasterList.maintStation }
    </td>
    <td class="wordtd_a">派工对象:</td>
    <td >
    ${CalloutMasterList.assignObject }
    </td>
    <td class="wordtd_a">电话:</td>
    <td >
    ${CalloutMasterList.phone }
    </td>
</tr>
<tr> <td class="wordtd_a">项目名称及楼栋号:</td>
    <td >
    ${CalloutMasterList.projectAddress }
    </td>
    <td class="wordtd_a">是否困人:</td>
    <td>
    <logic:equal name="typejsp" value="sh">
     <html:radio name="CalloutMasterList" property="isTrap" value="N">非困人</html:radio>
     <html:radio name="CalloutMasterList" property="isTrap" value="Y" >困人</html:radio>
    </logic:equal>
    <logic:notEqual name="typejsp" value="sh">
     ${CalloutMasterList.isTrap }
     </logic:notEqual>
    </td>
    <td class="wordtd_a">是否发送安抚短信:</td>
    <td>
    	<logic:present name="CalloutMasterList" property="isSendSms2">
    		<logic:match name="CalloutMasterList" property="isSendSms2" value="Y">是</logic:match>
    		<logic:match name="CalloutMasterList" property="isSendSms2" value="N">否</logic:match>
    	</logic:present>
    </td>
 </tr>
 <tr>   
    <td class="wordtd_a">报修描述:</td>
    <td  colspan="5">
    <logic:equal name="typejsp" value="sh">
    <textarea rows="3" cols="60" name="repairDesc" id="repairDesc" >${CalloutMasterList.repairDesc }</textarea>
    </logic:equal>
    <logic:notEqual name="typejsp" value="sh">
    ${CalloutMasterList.repairDesc }
    </logic:notEqual>
    </td>
</tr>
</table>

<%@ include file="calloutStophfDisplay2.jsp" %>

<table width="99%" class="tb"  height="25">
		<tr>
			<td  align="center" width="90%"><b>回访用户信息</b></td>
		</tr>
</table>
<table  width="99%" border="0" cellpadding="0" cellspacing="0" class="tb" >
<logic:notEqual name="typejsp" value="ps">
<tr>

    <td class="wordtd_a" width="10%">回访用户服务评价:</td>
    <td width="23%">
     ${CalloutMasterList.serviceAppraisal }
    </td>
    <td class="wordtd_a" width="10%">回访配件更换情况:</td>
    <td width="23%">
    ${CalloutMasterList.fittingSituation }
    </td>
    <td class="wordtd_a">回访收费情况:</td>
    <td  width="23%">
    ${CalloutMasterList.tollSituation }
    </td>
</tr>
<tr>
    <td class="wordtd_a">是否关闭回召:</td>
    <td  width="23%">
     ${CalloutMasterList.isColse }
    </td>
 	<td class="wordtd_a">回访备注:</td>
    <td colspan="3">
     ${CalloutMasterList.visitRem }
    </td>
</tr>
</logic:notEqual>
<logic:equal name="typejsp" value="ps">
<tr>

    <td class="wordtd_a" width="10%">回访用户服务评价:</td>
    <td width="23%">
     <html:select property="serviceAppraisal">
     <html:option value="1">非常满意</html:option>
     <html:option value="2">满意</html:option>
     <html:option value="3">一般</html:option>
     <html:option value="4">不满意</html:option>
     <html:option value="5">非常不满意</html:option>
     </html:select>
    </td>
    <td class="wordtd_a" width="10%">回访配件更换情况:</td>
    <td width="23%">
    <input type="radio" name="fittingSituation" id="fittingSituation" value="1" checked>属实
    <input type="radio" name="fittingSituation" id="fittingSituation" value="2">不属实
    </td>
    <td class="wordtd_a" width="10%">回访收费情况:</td>
    <td width="23%">
    <input type="radio" name="tollSituation" id="tollSituation" value="1" checked>属实
    <input type="radio" name="tollSituation" id="tollSituation" value="2">不属实
    </td>    
</tr>
<tr>
    <td class="wordtd_a" width="10%">是否关闭回召:</td>
    <td  width="23%">
     <input type="radio" name="isColse" id="isColse" value="1" checked>关闭
    <input type="radio" name="isColse" id="isColse" value="2">不关闭
    </td>
	<td class="wordtd_a" width="10%">回访备注:</td>
    <td colspan="3">
     <textarea rows="3" cols="60" name="visitRem" id="visitRem" ></textarea>
    </td>
</tr>
</logic:equal>
</table>

<br/>
<table width="99%" class="tb"  height="25">
		<tr>
			<td  align="center" width="90%"><b>急修短信信息</b></td>
		</tr>
</table>
<table  width="99%" border="0" cellpadding="0" cellspacing="0" class="tb" >
<tr>
    <td class="wordtd_a" width="10%">安抚短信发送号码:</td>
    <td width="23%">
     ${calloutSmsBean.smsTel} 
    </td>
    <td class="wordtd_a" width="10%">安抚短信内容:</td>
    <td width="23%">
     ${calloutSmsBean.smsContent} 
    </td>
    <td class="wordtd_a" width="10%">安抚短信发送时间:</td>
    <td width="23%">
    ${calloutSmsBean.smsSendTime} 
    </td>
</tr>
<tr>
    <td class="wordtd_a" width="10%">回访短信发送号码:</td>
    <td width="23%">
     ${calloutSmsBean.smsTel2}
    </td>
    <td class="wordtd_a" width="10%">回访短信内容:</td>
    <td width="23%">
     ${calloutSmsBean.smsContent2}
    </td>
    <td class="wordtd_a" width="10%">回访短信发送时间:</td>
    <td width="23%">
     ${calloutSmsBean.smsSendTime2}
    </td>
</tr>
</table>
</br>
<table  width="99%" border="0" cellpadding="0" cellspacing="0" class="tb" >
 <tr>
 <td class="wordtd_a" width="10%">客户签名:</td>
    <td class="inputtd" >
    <logic:notEmpty name="CalloutProcessList" property="customerSignature">
		<img src="<%=basePath%>/hotphoneAction.do?method=toDownloadFileRecord&customerSignature=${CalloutProcessList.customerSignature}"
			width="${CSwidth}" height="${CSheight}"  id="${CalloutProcessList.customerSignature}_1"> 	
	</logic:notEmpty>
	<logic:notEmpty name="CalloutProcessList" property="customerImage">
		<img src="<%=basePath%>/hotphoneAction.do?method=toDownloadFileRecord&customerImage=${CalloutProcessList.customerImage}"
			width="${CIwidth}" height="${CIheight}" id="${CalloutProcessList.customerImage}_2">
	</logic:notEmpty>
    </td>
  </tr>
</table>

</html:form>
