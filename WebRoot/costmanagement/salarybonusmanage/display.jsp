<%@ page contentType="text/html;charset=GBK" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<style>
  .show{display:block;}
  .hide{display:none;}

</style>
<logic:present name="salaryBonusManageBean">
<html:hidden property="id"/>
<html:hidden name="salaryBonusManageBean" property="billno"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
   	<td class="wordtd" width="20%">所属维保分部：</td>
   	<td width="30%"><bean:write name="salaryBonusManageBean" property="maintDivision"/></td>
   	<td class="wordtd" width="20%">维保站：</td>
   	<td width="30%"><bean:write name="salaryBonusManageBean" property="maintStation"/></td>
   </tr>
   <tr>
   	<td class="wordtd">产生成本时间：</td>
   	<td colspan="3"><bean:write name="salaryBonusManageBean" property="costsDate"/></td>
   </tr>
</table>
<br>

<div id="caption_0" style="width: 100%;padding-top: 0;padding-bottom: 2;border-bottom: 0;border-top: 0;border-left: 0;border-right: 0" class="tb">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">工资、奖金总额（元）：</td>
		<td width="75%"><bean:write name="salaryBonusManageBean" property="totalAmount"/></td>
	</tr>
</table>
</div>
<table id="prb" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="10%">合同号</td>
			<td width="10%">合同类型</td>
			<td width="8%">台数</td>
			<td width="8%">金额（元）</td>
			<td>备注</td>
		</tr>
	</thead>
	<tfoot>
      <tr height="15"><td colspan="6"></td></tr>
    </tfoot>
	<tbody>
		<logic:present name="salaryBonusDetailList">
          <logic:iterate id="element1" name="salaryBonusDetailList" >
          	<tr id="prbT_1" name="prbT_1">
				<td align="center">${element1.maintContractNo}</td>
				<td align="center">
					<logic:match name="element1" property="busType" value="B">保养</logic:match>
					<logic:match name="element1" property="busType" value="W">维修</logic:match>
					<logic:match name="element1" property="busType" value="G">改造</logic:match>
				</td>
				<td align="center">${element1.num}</td>
				<td align="center">${element1.money}</td>
				<td>${element1.rem}</td>
	         
	        </tr>
          </logic:iterate>
        </logic:present>
	</tbody>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">录入人：</td>
		<td width="30%"><bean:write name="salaryBonusManageBean" property="operId"/></td>
		<td class="wordtd" width="20%">录入日期：</td>
		<td width="30%"><bean:write name="salaryBonusManageBean" property="operDate"/></td>
	</tr>
</table>
</logic:present>