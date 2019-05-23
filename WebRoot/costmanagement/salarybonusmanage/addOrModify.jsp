<%@page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>

<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden name="salaryBonusManageBean" property="billno"/>
<html:hidden property="projects" />

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
   	<td class="wordtd" width="20%">所属维保分部：</td>
   	<td width="30%">
   		<bean:write name="maintDivisionName"/>
   		<html:hidden name="salaryBonusManageBean" property="maintDivision" />
   	</td>
   	<td class="wordtd" width="20%">维保站：</td>
   	<td width="30%">
   	<logic:notPresent name="ismchang">
   		<html:select name="salaryBonusManageBean" property="maintStation" styleId="maintStation">
	        <html:option value="">--请选择--</html:option>
			<html:options collection="maintStationList" property="storageid" labelProperty="storagename"/>
	    </html:select><font color="red">*</font>
	   </logic:notPresent>
	    <logic:present name="ismchang">
        ${maintStationName }
        <html:hidden name="salaryBonusManageBean" property="maintStation" styleId="maintStation" />
        </logic:present>
	    
   	</td>
   </tr>
   <tr>
   	<td class="wordtd">产生成本时间：</td>
   	<td colspan="3"><html:text name="salaryBonusManageBean" property="costsDate" readonly="true" size="12" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})" /></td>
   </tr>
</table>
<br>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">工资、奖金总额（元）：</td>
		<td width="75%">
			<html:text name="salaryBonusManageBean" property="totalAmount" styleId="totalAmount" styleClass="default_input" onblur="checkthisvalue(this);" />
			<input type="button" id="average" value="平均分配" class="default_input" onclick="Average('prb','money','totalAmount')" /><font color="red">*</font>
		</td>
	</tr>
</table>
<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0;border-top: 0" class="tb">
  &nbsp;<input type="button" value=" + " onclick="addElevators('prb')" class="default_input">
  <input type="button" value=" - " onclick="deleteRow('prb');sumValuesByName('prb','money','totalAmount')" class="default_input">           
  
</div>
<table id="prb" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="5%"><input type="checkbox" name="cbAll" onclick="checkTableAll('prb',this)"/></td>
			<td width="10%">合同号</td>
			<td width="10%">合同类型</td>
			<td width="8%">台数</td>
			<td width="8%">金额（元）<font color="red">*</font></td>
			<td>备注</td>
		</tr>
	</thead>
	<tfoot>
      <tr height="15"><td colspan="6"></td></tr>
    </tfoot>
	<tbody>
		<tr id="prbT_0" name="prbT_0" style="display: none;">
			<td align="center"><input type="checkbox" onclick="cancelCheckAll('prb','cbAll')"/></td>
			<td align="center">
				<input type="hidden" name="billno" id="billno" />
				<input type="text" name="maintContractNo" id="maintContractNo"  readonly="readonly" class="noborder" />
			</td>
			<td align="center"><input type="hidden" name="busType" id="busType" onpropertychange="bustype(this)" class="noborder" />
				<script type="text/javascript">
				function bustype(object){
					var bus=object.value;
					if(bus=="B"){
						object.parentElement.appendChild(document.createTextNode("保养"));
					}
					if(bus=="W"){
						object.parentElement.appendChild(document.createTextNode("维修"));
					}
					if(bus=="G"){
						object.parentElement.appendChild(document.createTextNode("改造"));
					}
				}
					
				</script>
			</td>
			<td align="center"><input type="text" name="num" id="num" class="noborder" size="5" readonly="readonly" /></td>
			<td align="center"><input type="text" name="money" id="money" class="default_input" onpropertychange="checkthisvalue(this);sumValuesByName('prb',this.name,'totalAmount');" /></td>
			<td><textarea rows="" cols="80" name="rem" id="rem" class="default_input"></textarea></td>
		</tr>
		<logic:present name="salaryBonusDetailList">
          <logic:iterate id="element1" name="salaryBonusDetailList" >
          	<tr id="prbT_1" name="prbT_1">
	         	<td align="center"><input type="checkbox" onclick="cancelCheckAll('prb','cbAll')"/></td>
				<td align="center">
					<input type="hidden" name="billno" id="billno" value="${element1.billno}" />
					<input type="text" name="maintContractNo" id="maintContractNo"  readonly="readonly" class="noborder"  value="${element1.maintContractNo}" />
				</td>
				<td align="center">
					<logic:match name="element1" property="busType" value="B">保养</logic:match>
					<logic:match name="element1" property="busType" value="W">维修</logic:match>
					<logic:match name="element1" property="busType" value="G">改造</logic:match>
					<input type="hidden" name="busType" id="busType" value="${element1.busType}" />
				</td>
				<td align="center"><input type="text" name="num" id="num" class="noborder" size="5" readonly="readonly" value="${element1.num}" /></td>
				<td align="center"><input type="text" name="money" id="money" class="default_input" onpropertychange="checkthisvalue(this);sumValuesByName('prb',this.name,'totalAmount');" value="${element1.money}" /></td>
				<td><textarea rows="" cols="80" name="rem" id="rem" class="default_input">${element1.rem}</textarea></td>
	         
	        </tr>
          </logic:iterate>
        </logic:present>
	</tbody>
</table>
<br/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">录入人：</td>
		<td width="30%">
			<bean:write name="operName"/>
			<html:hidden name="salaryBonusManageBean" property="operId" />
		</td>
		<td class="wordtd" width="20%">录入日期：</td>
		<td width="30%"><html:text name="salaryBonusManageBean" property="operDate" readonly="true" styleClass="default_input_noborder" /></td>
	</tr>
</table>
<script type="text/javascript">
setDynamicTable("prb","prbT_0");// 设置动态增删行表格
</script>