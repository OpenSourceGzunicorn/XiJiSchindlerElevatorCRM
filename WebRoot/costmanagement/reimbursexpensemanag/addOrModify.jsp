<%@page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>

<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden name="reimbursExpenseManagBean" property="billno"/>
<html:hidden property="prds"/>
<html:hidden property="msrs"/>
<html:hidden property="mdrs"/>
<html:hidden property="nrms"/>
<a href="" id="maintContractNoOpen" target="_blank"></a>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
   	<td class="wordtd" width="20%">所属维保分部：</td>
   	<td width="30%">

   		
   		<logic:notPresent name="ismchang" >
   		<input name="maintDivisionName" id="comName" value="${maintDivisionName }">
   		<html:hidden name="reimbursExpenseManagBean" property="maintDivision" styleId="comId" />
   		<input type="button" value=".." onclick="openWindowAndReturnValue3('searchReimbursPeopleationAction','toSearchRecord','','')" class="default_input"/>
        </logic:notPresent>
        <logic:present name="ismchang">
        ${maintDivisionName }
        <input type="hidden" name="maintDivisionName" id="comName" value="${maintDivisionName }">
        <html:hidden name="reimbursExpenseManagBean" property="maintDivision" styleId="comId" />
        </logic:present>

   	</td>
   	<td class="wordtd" width="20%">报销部门：</td>
   	<td width="30%">
	    <font id="storageName">${storageName }</font>
	    <html:hidden name="reimbursExpenseManagBean" property="maintStation" styleId="storageId"/>
   	</td>
   </tr>
   <tr>
   	<td class="wordtd">报销人：</td>
   	<td id="td1">
		 <font id="userName">${reimbursPeopleName }</font>
		 <html:hidden name="reimbursExpenseManagBean" property="reimbursPeople" styleId="userId"/>
   	</td>
   	<td class="wordtd">报销时间：</td>
   	<td><html:text name="reimbursExpenseManagBean" property="reimbursDate" size="12" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})" /></td>
   </tr>
</table>
<br>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">报销总额（元）：</td>
		<td width="75%"><html:text name="reimbursExpenseManagBean" property="totalAmount" styleId="totalAmount" readonly="true" styleClass="default_input_noborder" /></td>
	</tr>
</table>
<br>

<table width="100%" border="0" cellpadding="0" cellspacing="0"  class="tb">
	<tr>
		<td class="wordtd" width="20%">合同报销金额（元）：</td>
		<td width="75%"><html:text name="reimbursExpenseManagBean" property="projectMoney" styleId="projectMoney" readonly="true" styleClass="default_input_noborder" /></td>
	</tr>
</table>

<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0;border-top: 0" class="tb">
  &nbsp;<input type="button" value=" + " onclick="addElevators('prb')" class="default_input">
  <input type="button" value=" - " onclick="deleteRow('prb');sumValuesByName('prb','money','projectMoney');sumAmount();" class="default_input">           
  &nbsp;&nbsp;&nbsp;输入分配金额（元）<input type="text" id="avgMoney" title="请输入分配金额（元）!" onkeypress="f_check_number2()"  onpropertychange="checkthisvalue(this);">
  &nbsp;<input type="button" value="平均分配" onclick="avgMoney1()" class="default_input">
  <font color="red">注：请勾选需要平均分配金额的合同号！</font>
</div>
<table id="prb" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="5%"><input type="checkbox" name="cbAll" onclick="checkTableAll('prb',this)"/></td>
			<td width="10%">合同号</td>
			<td width="10%">合同类型</td>
			<td width="10%">维保分部</td>
			<td width="15%">维保站</td>
			<td width="4%" nowrap="nowrap">台数</td>
			<td width="8%" nowrap="nowrap">金额（元）<font color="red">*</font></td>
			<td width="10%">报销类型<font color="red">*</font></td>
			<td>备注</td>
		</tr>
	</thead>
	<tfoot>
      <tr height="15"><td colspan="9">&nbsp;</td></tr>
    </tfoot>
	<tbody>
		<tr id="prbT_0" name="prbT_0" style="display: none;">
			<td align="center"><input type="checkbox" onclick="cancelCheckAll('prb','cbAll')"/></td>
			<td align="center">
				<input type="hidden" name="billno" id="billno" />
				<input type="text" name="maintContractNo" id="maintContractNo" onclick="simpleOpenWindowKK(this);" readonly="readonly" class="link noborder_center"/>
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
			
			<td align="center">
			<input type="text" width="100%" name="comNameprb"  id="comNameprb" readonly="readonly"  class="noborder_center"/>
			<font id="comNameprbText"></font>
			<input type="hidden" name="maintDivisionBx" id="maintDivision"/>
			</td>
			<td align="center">
			<input type="text" width="100%" name="storageNameprb"  id="storageNameprb" readonly="readonly"  class="noborder_center"/>
		    <font id="storageNameprbText"></font>
			<input type="hidden" name="maintStationBx" id="maintStation"/>
			</td>
			<td align="center"><input type="text" name="num" id="num" readonly="readonly" class="noborder" size="3" /></td>
			<td align="center"><input type="text" name="money" id="money" class="default_input" size="9"  onpropertychange="checkthisvalue(this);sumValuesByName('prb',this.name,'projectMoney');sumAmount();" /></td>
			<td align="center">
        		<select name="reimburType" id="reimburType" >
		              <option value="">请选择</option>
		            <logic:iterate id="s" name="prTypeList" >
		              <option value="${s.id.pullid}">${s.pullname}</option>
		            </logic:iterate>
          		</select>
			</td>
			<td><textarea rows="" cols="40"name="rem" id="rem" class="default_input" ></textarea></td>
		</tr>
		<logic:present name="projectList">
          <logic:iterate id="element1" name="projectList" >
          	<tr id="prbT_1" name="prbT_1">
	         	<td align="center"><input type="checkbox" onclick="cancelCheckAll('prb','cbAll')"/></td>
				<td align="center">
					<input type="hidden" name="billno" id="billno" value="${element1.billno}" />
					<input type="text" name="maintContractNo" id="maintContractNo" onclick="simpleOpenWindowKK(this);"value="${element1.maintContractNo}" readonly="readonly" class="link noborder_center"/>
			
				</td>
				<td align="center">
					<logic:match name="element1" property="busType" value="B">保养</logic:match>
					<logic:match name="element1" property="busType" value="W">维修</logic:match>
					<logic:match name="element1" property="busType" value="G">改造</logic:match>
					<input type="hidden" name="busType" id="busType" value="${element1.busType}" />
				</td>
				<td align="center">${element1.comName}<input type="hidden" name="maintDivisionBx" id="maintDivision"value="${element1.maintDivisionBx}" /></td>
				<td align="center">${element1.storageName}<input type="hidden" name="maintStationBx" id="maintStation"value="${element1.maintStationBx}" /></td>
				<td align="center"><input type="text" name="num" id="num" readonly="readonly" class="noborder" size="3" value="${element1.num}" /></td>
				<td align="center"><input type="text" name="money" id="money" class="default_input" size="9" onpropertychange="checkthisvalue(this);sumValuesByName('prb',this.name,'projectMoney');sumAmount();" value="${element1.money}" /></td>
				<td align="center">
					<html:select name="element1" property="reimburType">
	        			<html:option value="">全部</html:option>
			  			<html:options collection="prTypeList" property="id.pullid" labelProperty="pullname"/>
	        		</html:select>
				</td>
				<td><textarea rows="" cols="40" name="rem" id="rem" class="default_input" >${element1.rem}</textarea></td>
	         
	        </tr>
          </logic:iterate>
        </logic:present>
	</tbody>
</table>
<br/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">站内报销金额（元）：</td>
		<td width="75%"><html:text name="reimbursExpenseManagBean" property="stationMoney" styleId="stationMoney" readonly="true" styleClass="default_input_noborder" /></td>
	</tr>
</table>

<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0;border-top: 0" class="tb">
  &nbsp;<input type="button" value=" + " onclick="addRow('msr')" class="default_input">
  <input type="button" value=" - " onclick="deleteRow('msr');sumValuesByName('msr','money','stationMoney');sumAmount();" class="default_input">           
</div>

<table id="msr" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="5%"><input type="checkbox" name="cbAll" onclick="checkTableAll('msr',this)"/></td>
			<td width="10%">报销类型<font color="red">*</font></td>
			<td width="15%">维保分部</td>
			<td width="15%">维保站</td>
			<td width="10%">金额（元）<font color="red">*</font></td>
			<td>备注</td>
		</tr>
	</thead>
	<tfoot>
      <tr height="15"><td colspan="6">&nbsp;</td></tr>
    </tfoot>
   
	<tbody>
		<tr id="msrT_0" name="msrT_0" style="display: none;">
			<td align="center"><input type="checkbox" onclick="cancelCheckAll('msr','cbAll')"/></td>
			<td align="center">
        		<select name="reimburType" id="reimburType" >
		              <option value="">请选择</option>
		            <logic:iterate id="s" name="mrTypeList" >
		              <option value="${s.id.pullid}">${s.pullname}</option>
		            </logic:iterate>
          		</select>
			</td>
			<td align="center">
   	        <font id="comNameMsr">${maintDivisionName }</font>
   		    <input type="hidden" name="maintDivisionBx" id="maintDivisionBx">
   		    <input type="button" name="openWindow" value=".." onclick="" class="default_input"/>
         	</td>
         	<td align="center">
	        <font id="storageNameMsr">${storageName }</font>
	        <input type="hidden" name="maintStationBx" id="maintStationBx">
   	        </td>
			<td align="center"><input type="text" name="money" class="default_input" onpropertychange="checkthisvalue(this);sumValuesByName('msr',this.name,'stationMoney');sumAmount();" /></td>
			<td><textarea rows="" cols="80" name="rem" class="default_input"></textarea>
			</td>
			
		</tr>
		<% int i=1; %>
		<logic:present name="stationList">
          <logic:iterate id="element2" name="stationList" >
          	<tr id="msrT_1" name="msrT_1">
	         <td align="center"><input type="checkbox" onclick="cancelCheckAll('msr','cbAll')"/></td>
	         <td align="center">
				<html:select name="element2" property="reimburType">
	        			<html:option value="">全部</html:option>
			  			<html:options collection="mrTypeList" property="id.pullid" labelProperty="pullname"/>
        		</html:select>
			</td>
			<td align="center">
   	        <font id="comNameMsrmsr<%=i%>">${element2.r1 }</font>
   		    <input type="hidden" name="maintDivisionBx" id="maintDivisionBxmsr<%=i%>" value="${element2.maintDivisionBx }">
   		    <input type="button" name="openWindow" value=".." onclick="openWindowmaintDivisionBx(<%=i%>,'msr')" class="default_input"/>
         	</td>
         	<td align="center">
	        <font id="storageNameMsrmsr<%=i%>">${element2.r2 }</font>
	        <input type="hidden" name="maintStationBx" id="maintStationBxmsr<%=i%>" value="${element2.maintStationBx }">
   	        </td>
			<td align="center"><input type="text" value="${element2.money}" name="money" class="default_input" onpropertychange="checkthisvalue(this);sumValuesByName('msr',this.name,'stationMoney');sumAmount();" /></td>
			<td><textarea rows="" cols="80" name="rem" class="default_input" >${element2.rem}</textarea></td>
	        </tr>
	        <%i++; %>
          </logic:iterate>
        </logic:present>
	</tbody>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">分部报销金额（元）：</td>
		<td width="75%"><html:text name="reimbursExpenseManagBean" property="divsionMoney" styleId="divsionMoney" readonly="true" styleClass="default_input_noborder" /></td>
	</tr>
</table>

<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0;border-top: 0" class="tb">
  &nbsp;<input type="button" value=" + " onclick="addRow('mdr')" class="default_input">
  <input type="button" value=" - " onclick="deleteRow('mdr');sumValuesByName('mdr','money','divsionMoney');sumAmount();" class="default_input">           
</div>

<table id="mdr" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="5%"><input type="checkbox" name="cbAll" onclick="checkTableAll('mdr',this)"/></td>
			<td width="10%">报销类型<font color="red">*</font></td>
			<td width="15%">维保分部</td>
			<td width="10%">金额（元）<font color="red">*</font></td>
			<td>备注</td>
		</tr>
	</thead>
	<tfoot>
      <tr height="15"><td colspan="5">&nbsp;</td></tr>
    </tfoot>
   
	<tbody>
		<tr id="mdrT_0" name="mdrT_0" style="display: none;">
			<td align="center"><input type="checkbox" onclick="cancelCheckAll('mdr','cbAll')"/></td>
			<td align="center">
        		<select name="reimburType" id="reimburType" >
		              <option value="">请选择</option>
		            <logic:iterate id="s" name="mrTypeList" >
		              <option value="${s.id.pullid}">${s.pullname}</option>
		            </logic:iterate>
          		</select>
			</td>
			<td align="center">
   		      <font id="comNameMsr">${maintDivisionName }</font>
   		    <input type="hidden" name="maintDivisionBx" id="maintDivisionBx">
   		    <input type="button" name="openWindow" value=".." onclick="" class="default_input"/>
         	</td>
			<td align="center"><input type="text" name="money" class="default_input" onpropertychange="checkthisvalue(this);sumValuesByName('mdr',this.name,'divsionMoney');sumAmount();" /></td>
			<td><textarea rows="" cols="80" name="rem" class="default_input"></textarea>
			</td>
			
		</tr>
		<% int y=1; %>
		<logic:present name="divisionList">
          <logic:iterate id="element3" name="divisionList" >
          	<tr id="msrT_1" name="msrT_1">
	         <td align="center"><input type="checkbox" onclick="cancelCheckAll('msr','cbAll')"/></td>
	         <td align="center">
				<html:select name="element3" property="reimburType">
	        			<html:option value="">全部</html:option>
			  			<html:options collection="mrTypeList" property="id.pullid" labelProperty="pullname"/>
        		</html:select>
			</td>
			<td align="center">
   	        <font id="comNameMsrmdr<%=y%>">${element3.r1 }</font>
   		    <input type="hidden" name="maintDivisionBx" id="maintDivisionBxmdr<%=y%>" value="${element3.maintDivisionBx }">
   		    <input type="button" name="openWindow" value=".." onclick="openWindowmaintDivisionBx(<%=y%>,'mdr')" class="default_input"/>
         	</td>
			<td align="center"><input type="text" value="${element3.money}" name="money" class="default_input" onpropertychange="checkthisvalue(this);sumValuesByName('mdr',this.name,'divsionMoney');sumAmount();" /></td>
			<td><textarea rows="" cols="80" name="rem" class="default_input" >${element3.rem}</textarea></td>
	        </tr>
	        <%y++; %>
          </logic:iterate>
        </logic:present>
	</tbody>
</table>
<br/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<tr>
		<td class="wordtd" width="20%">非维保成本报销金额（元）：</td>
		<td width="75%"><html:text name="reimbursExpenseManagBean" property="noReimMoney" styleId="noReimMoney" readonly="true" styleClass="default_input_noborder" /></td>
	</tr>
</table>

<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0;border-top: 0" class="tb">
  &nbsp;<input type="button" value=" + " onclick="addRow('nrm')" class="default_input">
  <input type="button" value=" - " onclick="deleteRow('nrm');sumValuesByName('nrm','money','noReimMoney');sumAmount();" class="default_input">           
</div>

<table id="nrm" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<thead>
		<tr class="wordtd_header">
			<td width="5%"><input type="checkbox" name="cbAll" onclick="checkTableAll('nrm',this)"/></td>
			<td width="10%">报销类型<font color="red">*</font></td>
			<td width="15%">所属部门</td>
			<td width="10%">金额（元）<font color="red">*</font></td>
			<td>备注</td>
		</tr>
	</thead>
	<tfoot>
      <tr height="15"><td colspan="5">&nbsp;</td></tr>
    </tfoot>
   
	<tbody>
		<tr id="nrmT_0" name="nrmT_0" style="display: none;">
			<td align="center"><input type="checkbox" onclick="cancelCheckAll('nrm','cbAll')"/></td>
			<td align="center">
        		<select name="reimburType" id="reimburType" >
		              <option value="">请选择</option>
		            <logic:iterate id="s" name="mrTypeList" >
		              <option value="${s.id.pullid}">${s.pullname}</option>
		            </logic:iterate>
          		</select>
			</td>
			<td align="center">
   		      <font id="comNameMsr">${maintDivisionName }</font>
   		    <input type="hidden" name="belongsDepart" id="maintDivisionBx">
   		    <input type="button" name="openWindow" value=".." onclick="" class="default_input"/>
         	</td>
			<td align="center"><input type="text" name="money" class="default_input" onpropertychange="checkthisvalue(this);sumValuesByName('nrm',this.name,'noReimMoney');sumAmount();" /></td>
			<td><textarea rows="" cols="80" name="rem" class="default_input"></textarea>
			</td>
		</tr>
		<% int x=1; %>
		<logic:present name="noReimList">
          <logic:iterate id="element4" name="noReimList" >
          	<tr id="msrT_1" name="msrT_1">
	         <td align="center"><input type="checkbox" onclick="cancelCheckAll('msr','cbAll')"/></td>
	         <td align="center">
				<html:select name="element4" property="reimburType">
	        			<html:option value="">全部</html:option>
			  			<html:options collection="mrTypeList" property="id.pullid" labelProperty="pullname"/>
        		</html:select>
			</td>
			<td align="center">
   	        <font id="comNameMsrnrm<%=x%>">${element4.r1 }</font>
   		    <input type="hidden" name="belongsDepart" id="maintDivisionBxnrm<%=x%>" value="${element4.belongsDepart }">
   		    <input type="button" name="openWindow" value=".." onclick="openWindowmaintDivisionBx(<%=x%>,'nrm')" class="default_input"/>
         	</td>
			<td align="center"><input type="text" value="${element4.money}" name="money" class="default_input" onpropertychange="checkthisvalue(this);sumValuesByName('nrm',this.name,'noReimMoney');sumAmount();" /></td>
			<td><textarea rows="" cols="80" name="rem" class="default_input" >${element4.rem}</textarea></td>
	        </tr>
	        <%x++; %>
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
			<html:hidden name="reimbursExpenseManagBean" property="operId" />
		</td>
		<td class="wordtd" width="20%">录入日期：</td>
		<td width="30%"><html:text name="reimbursExpenseManagBean" property="operDate" readonly="true" styleClass="default_input_noborder" /></td>
	</tr>
</table>
<script type="text/javascript">
window.onload = function(){
	setDynamicTable("prb","prbT_0");// 设置动态增删行表格
	setDynamicTable("msr","msrT_0");// 设置动态增删行表格
	setDynamicTable("mdr","mdrT_0");// 设置动态增删行表格
	setDynamicTable("nrm","nrmT_0");// 设置动态增删行表格
}

</script>