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
    <td class="wordtd_a" width="10%">设备编号:</td>
    <td width="23%">
     ${CalloutProcessList.deviceId }
    </td>
    <td class="wordtd_a" width="10%">到场位置:</td>
    <td width="23%">
    ${hashmapbean.elevatorLocation }
    </td>
    
</tr>
<tr>
    <td class="wordtd_a" width="10%">故障代码:</td>
    <td width="23%">
      ${CalloutProcessList.faultCode }
    </td>
    <td class="wordtd_a" width="10%">故障状态:</td>
    <td width="23%" >
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
     ${CalloutProcessList.IsStop }
    </td>
    <td class="wordtd_a" width="10%">停梯时间:</td>
    <td width="23%" colspan="3">
     ${CalloutProcessList.stopTime }
    </td>
</tr>
<tr>
	<td class="wordtd_a" width="10%">停梯备注:</td>
	<td width="23%" colspan="5">
		${CalloutProcessList.stopRem }
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
    <td class="wordtd_a" width="10%">维修完成时间:</td>
    <td width="23%">
     ${CalloutProcessList.completeTime }
    </td >
    <td class="wordtd_a" width="10%">故障类型:</td>
    <td width="23%"> 
     ${CalloutProcessList.hftId }
    </td>
     <td class="wordtd_a" width="10%">完工位置:</td>
    <td  width="23%">
    ${hashmapbean.elevatorLocation2 }
    </td>
</tr>
<%-- <tr>
    <td class="wordtd_a" width="10%">新换配件名称和数量:</td>
    <td width="23%">
     ${CalloutProcessList.newFittingName }
    </td>
    <td class="wordtd_a" width="10%">是否收费:</td>
    <td width="23%">
     ${CalloutProcessList.IsToll }
    </td>
    <td class="wordtd_a" width="10%">收费金额:</td>
    <td width="23%">
    ${CalloutProcessList.money }
    </td>
</tr> --%>
<tr>
	<td class="wordtd_a" width="10%">故障分类:</td>
    <td width="23%">
    	${CalloutProcessList.hfcId}
    </td>
    <td class="wordtd_a" width="10%">维修过程描述:</td>
    <td colspan="3">
     ${CalloutProcessList.processDesc }
    </td>
</tr>
<tr>
    <td class="wordtd_a" width="10%">坏件品牌型号<br/>或其它备注:</td>
    <td colspan="5">
     ${CalloutProcessList.serviceRem }
    </td>
</tr>
<tr>
    <td class="wordtd_a" width="10%">审核人:</td>
    <td width="23%">
 
    </td>
    <td class="wordtd_a" width="10%">审核日期:</td>
    <td width="23%">

    </td>
    <td class="wordtd_a" width="10%">是否发送短信:</td>
    <td width="23%">

    </td>
</tr>
<tr>
<td class="wordtd_a">审核意见:</td>
<td colspan="5"></td>
</tr>
</table><br>
<%-- <table width="99%" class="tb"  height="25">
		<tr>
			<td  align="center" width="90%"><b>用户回复信息</b></td>
		</tr>
</table>
<table  width="99%" border="0" cellpadding="0" cellspacing="0" class="tb" >
<tr>
    <td class="wordtd_a"  width="10%">用户服务评价:</td>
    <td width="23%">
     ${CalloutSmsList.serviceRating }
    </td>
    <td class="wordtd_a"  width="10%">其他意见:</td>
    <td width="56%">
     ${CalloutSmsList.otherRem }
    </td>

</tr>
</table><br> --%>