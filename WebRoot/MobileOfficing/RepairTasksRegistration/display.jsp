<%@page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<link href="/common/DatePicker/开发包/skin/WdatePicker.css"  type="text/style+css">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>
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
     <input type="hidden" name="calloutMasterNo" id="calloutMasterNo" value="${CalloutMasterList.calloutMasterNo }" />
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
<tr>
    <td class="wordtd_a">服务对象:</td>
    <td >
     ${CalloutMasterList.serviceObjects } 
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
<tr>
	<td class="wordtd_a">项目地址:</td>
    <td >
    ${CalloutMasterList.projectAddress }
    </td>
    <td class="wordtd_a">是否困人:</td>
    <td>
     ${CalloutMasterList.isTrap }
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
    ${CalloutMasterList.repairDesc }
    </td>
</tr>
</table>
<br>
<table width="99%" class="tb"  height="25">
		<tr>
			<td  align="center" width="90%"><b>流转主要信息</b></td>
		</tr>
</table>
<table  width="99%" border="0" cellpadding="0" cellspacing="0" class="tb" >
<tr>
    <td class="wordtd_a" width="10%">转派时间:</td>
    <td width="23%">
     ${CalloutProcessList.turnSendTime } 
    </td>
    <td class="wordtd_a" width="10%">被转派人:</td>
    <td width="23%">
     ${CalloutProcessList.turnSendId } 
    </td>
    <td class="wordtd_a" width="10%">被转派人电话:</td>
    <td width="23%">
    ${CalloutProcessList.turnSendTel } 
    </td>
</tr>
<tr>
    <td class="wordtd_a" width="10%">派工接受时间:</td>
    <td width="23%">
     ${CalloutProcessList.assignTime }
    </td>
    <td class="wordtd_a" width="10%">派工接收人:</td>
    <td width="23%">
     ${CalloutProcessList.assignUser }
    </td>
    <td class="wordtd_a" width="10%">接收人电话:</td>
    <td width="23%">
     ${CalloutProcessList.assignUserTel }
    </td>
</tr>
</table><br>
<table width="99%" class="tb"  height="25">
		<tr>
			<td  align="center" width="90%"><b>现场维修信息</b></td>
		</tr>
</table>
<table  width="99%" border="0" cellpadding="0" cellspacing="0" class="tb" >
<tr>
    <td class="wordtd_a" width="10%">到现场时间:</td>
    <td width="23%">
     ${CalloutProcessList.arriveDateTime }
    </td>
    <td class="wordtd_a" width="10%">到场位置:</td>
    <td width="23%">
    ${CalloutProcessList.arriveLocation }
    </td> 
    <td class="wordtd_a" width="10%">设备编号:</td>
    <td width="23%">
     ${CalloutProcessList.deviceId }
    </td>      
</tr>
<tr>
    <td class="wordtd_a" width="10%">故障代码:</td>
    <td width="23%">
      ${CalloutProcessList.faultCode }
    </td>
    <td class="wordtd_a" width="10%">故障状态:</td>
    <td width="23%">
      ${CalloutProcessList.faultStatus }
    </td>
    <td class="wordtd_a" width="10%">主板类型:</td>
    <td width="23%">
    ${CalloutProcessList.hmtId }
    </td>
</tr>
<tr>
    <td class="wordtd_a" width="10%">是否停梯:</td>
    <td width="23%" >
     ${CalloutProcessList.isStop }
    </td>
    <td class="wordtd_a" width="10%">停梯时间:</td>
    <td width="23%" colspan="3">
     ${CalloutProcessList.stopTime }
    </td>
</tr>
<tr>
	<td class="wordtd_a" width="10%">停梯备注:</td>
	<td width="23%" colspan="5">
		 ${CalloutProcessList.stopRem}
	</td>
</tr>
</table><br>
<table width="99%" class="tb"  height="25">
		<tr>
			<td  align="center" width="90%"><b>维修完工信息</b></td>
		</tr>
</table>
<table  width="99%" border="0" cellpadding="0" cellspacing="0" class="tb" >
<tr>
    <td class="wordtd_a" width="10%">完工时间:</td>
    <td width="23%">
     ${CalloutProcessList.completeTime }
    </td >
    <td class="wordtd_a" width="10%">完工位置:</td>
    <td  width="23%">
    ${CalloutProcessList.fninishLocation }
    </td>
    <td class="wordtd_a">故障类型:</td>
    <td width="23%"> 
     ${CalloutProcessList.hftId }
    </td>
</tr>
   
<%-- <tr>
    <td class="wordtd_a" width="10%">新换配件名称和数量:</td>
    <td width="23%">
     ${CalloutProcessList.newFittingName }
    </td>     
    <td class="wordtd_a">是否收费:</td>
    <td >
     ${CalloutProcessList.isToll }
    </td>
    <td class="wordtd_a">收费金额:</td>
    <td >
    ${CalloutProcessList.money }
    </td>
</tr> --%>
<tr>
	<%-- <td class="wordtd_a" width="10%">故障分类:</td>
    <td width="23%">
    	${CalloutMasterList.hfcId}
    </td> --%>
    <td class="wordtd_a">维修过程描述:</td>
    <td colspan="5">
     ${CalloutProcessList.processDesc }
    </td>
</tr>
<tr>
    <td class="wordtd_a">维修备注:</td>
    <td colspan="5">
     ${CalloutProcessList.serviceRem }
    </td>
</tr>
</table><br>
