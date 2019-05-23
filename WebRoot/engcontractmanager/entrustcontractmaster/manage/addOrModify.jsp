<%@page contentType="text/html;charset=GBK"%>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>


<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<input type="hidden" name="id1" value="${id}"/>
<html:hidden property="maintContractDetails"/>
<html:hidden name="entrustContractMasterBean" property="billNo"/>
<html:hidden name="entrustContractMasterBean" property="maintBillNo" />
<html:hidden name="entrustContractMasterBean" property="quoteBillNo" />
<html:hidden property="submitType" />

<table id="companyA" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td nowrap="nowrap" height="23" colspan="6">&nbsp;<b>客户信息</td>
  </tr>
  <tr>
    <td nowrap="nowrap" class="wordtd_a">甲方单位名称:</td>
    <td width="25%">
    	${companyA.companyName}
      <%-- <input type="text" name="companyName" id="companyName" value="${companyA.companyName}" readonly="true" size="23"/> --%>
      <!-- <input type="button" value=".." onclick="openWindowAndReturnValue2('searchCustomerAction')" class="default_input"/><font color="red">*</font> -->
      <html:hidden name="entrustContractMasterBean" property="companyId" styleId="companyId"/>
    </td>
    <td nowrap="nowrap" class="wordtd_a">甲方单位地址:</td>
    <td width="20%" id="address">${companyA.address}</td>
    <td nowrap="nowrap" class="wordtd_a">甲方法人:</td>
    <td width="20%" id="legalPerson">${companyA.legalPerson}</td>  
  </tr>
  <tr>    
    <td nowrap="nowrap" class="wordtd_a">甲方委托人:</td>
    <td id="client">${companyA.client}</td>
    <td nowrap="nowrap" class="wordtd_a">甲方联系人:</td>
    <td id="contacts">${companyA.contacts}</td>   
    <td nowrap="nowrap" class="wordtd_a">甲方联系电话:</td>
    <td id="contactPhone">${companyA.contactPhone}</td>          
  </tr>     
  <tr>
    <td nowrap="nowrap" class="wordtd_a">甲方传真:</td>
    <td id="fax">${companyA.fax}</td>   
    <td nowrap="nowrap" class="wordtd_a">甲方邮编:</td>
    <td id="postCode">${companyA.postCode}</td>          
    <td nowrap="nowrap" class="wordtd_a">地址、电话:</td>
    <td id="accountHolder">${companyA.accountHolder}</td> 
  </tr>
  <tr>      
    <td nowrap="nowrap" class="wordtd_a">甲方银行账号:</td>
    <td id="account">${companyA.account}</td>          
    <td nowrap="nowrap" class="wordtd_a">甲方开户银行:</td>
    <td id="bank">${companyA.bank}</td>   
    <td nowrap="nowrap" class="wordtd_a">纳税人识别号:</td>
    <td id="taxId">${companyA.taxId}</td>          
  </tr>                 
</table>
<br>

<table id="companyB" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td nowrap="nowrap" class="wordtd_a">乙方单位名称:</td>
    <td width="25%">
    	${companyB.companyName}
      <%-- <input type="text" name="companyName" id="companyName2" value="${companyB.companyName}" readonly="true" size="23"/>
      <input type="button" value=".." onclick="openWindowAndReturnValue3('searchCustomerAction','toSearchRecord','&cusNature=WT','2')" class="default_input"/><font color="red">*</font> --%>
      <html:hidden name="entrustContractMasterBean" property="companyId2" styleId="companyId2" />     
    </td>
    <td nowrap="nowrap" class="wordtd_a">乙方单位地址:</td>
    <td width="20%" id="address2">${companyB.address}</td>
    <td nowrap="nowrap" class="wordtd_a">乙方法人:</td>
    <td width="20%" id="legalPerson2">${companyB.legalPerson}</td>
  </tr>
  <tr>       
    <td nowrap="nowrap" class="wordtd_a">乙方委托人:</td>
    <td id="client2">${companyB.client}</td>          
    <td nowrap="nowrap" class="wordtd_a">乙方联系人:</td>
    <td id="contacts2">${companyB.contacts}</td>   
    <td nowrap="nowrap" class="wordtd_a">乙方联系电话:</td>
    <td id="contactPhone2">${companyB.contactPhone}</td>          
  </tr>     
  <tr>
    <td nowrap="nowrap" class="wordtd_a">乙方传真:</td>
    <td id="fax2">${companyB.fax}</td>   
    <td nowrap="nowrap" class="wordtd_a">乙方邮编:</td>
    <td id="postCode2">${companyB.postCode}</td>          
    <td nowrap="nowrap" class="wordtd_a">乙方户名:</td>
    <td id="accountHolder2">${companyB.accountHolder}</td>   
  </tr>
  <tr>
    <td nowrap="nowrap" class="wordtd_a">乙方银行账号:</td>
    <td id="account2">${companyB.account}</td> 
    <td nowrap="nowrap" class="wordtd_a"></td>
    <td >&nbsp;</td>   
    <td nowrap="nowrap" class="wordtd_a"></td>
    <td >&nbsp;</td>                   
  </tr>            
</table>
<br>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td nowrap="nowrap" height="23" colspan="6">&nbsp;<b>合同主信息</td>
  </tr>
  <tr>
    <td nowrap="nowrap" class="wordtd_a">维保合同号:</td>
    <td width="25%">
      <html:text name="entrustContractMasterBean" property="maintContractNo" readonly="true" styleClass="default_input_noborder"/>
    </td>          
  	<td nowrap="nowrap" class="wordtd_a">委托合同号：</td>
  	<td width="20%"><html:text name="entrustContractMasterBean" property="entrustContractNo" readonly="true" styleClass="default_input_noborder"/></td>
    <td nowrap="nowrap" class="wordtd_a">合同性质:</td>
    <td width="20%">
    ${contractNatureOfName}
    <html:hidden name="entrustContractMasterBean" property="contractNatureOf"></html:hidden>
    	
    </td>         
  </tr>  
  <tr>
    <td class="wordtd_a">合同有效期（月）:</td>
    <td >
      <html:text name="entrustContractMasterBean" property="contractPeriod" readonly="true" styleClass="default_input_noborder"/>          
    </td>          
    <td nowrap="nowrap" class="wordtd_a">合同开始日期:</td>
    <td >
    ${entrustContractMasterBean.contractSdate }
      <html:hidden name="entrustContractMasterBean" property="contractSdate" styleId="contractSdate" />
    </td>
    <td nowrap="nowrap" class="wordtd_a">结束日期:</td>
    <td >
    ${entrustContractMasterBean.contractEdate }
      <html:hidden name="entrustContractMasterBean" property="contractEdate" styleId="contractEdate"/>   
    </td>         
  </tr>          
  <tr>
    <td nowrap="nowrap" class="wordtd_a">保养方式:</td>
    <td >
     
        <logic:match name="entrustContractMasterBean" property="mainMode" value="FREE">免费</logic:match>
        <logic:match name="entrustContractMasterBean" property="mainMode" value="PAID">收费</logic:match>
        <html:hidden name="entrustContractMasterBean" property="mainMode"/>
    
    </td>  
    <td nowrap="nowrap" class="wordtd_a">所属维保分部:</td>
    <td >
      <bean:write name="maintDivisionName"/>
      <html:hidden name="entrustContractMasterBean" property="maintDivision"/>
    </td> 
     <td nowrap="nowrap" class="wordtd_a">所属维保站:</td>
    <td >
      <bean:write name="maintStationName"/>
      <html:hidden name="entrustContractMasterBean" property="maintStation"/>
    </td>       
  </tr> 
  <tr>
    <td nowrap="nowrap" class="wordtd_a">经办人:</td>
    <td >
    <html:text name="entrustContractMasterBean" property="attn" readonly="true" styleClass="default_input_noborder"/>
    </td> 
    <td nowrap="nowrap" class="wordtd_a"></td>
    <td >&nbsp;</td>   
    <td nowrap="nowrap" class="wordtd_a"></td>
    <td >&nbsp;</td>                   
  </tr> 
    
</table>
<br>

<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  &nbsp;<b>&nbsp;合同明细</b>
</div>
<div id="wrap_0" style="overflow-x:scroll">
  <table id="dynamictable_0"  width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <thead>
      <tr id="titleRow_0">
        <td nowrap="nowrap" class="wordtd_header">电梯编号</td>
        <td nowrap="nowrap" class="wordtd_header">电梯类型</td>
        <td nowrap="nowrap" class="wordtd_header">层</td>
        <td nowrap="nowrap" class="wordtd_header">站</td>
        <td nowrap="nowrap" class="wordtd_header">门</td>
        <td nowrap="nowrap" class="wordtd_header">提升高度</td>
        <td nowrap="nowrap" class="wordtd_header">规格型号</td>
        <td nowrap="nowrap" class="wordtd_header">年检日期</td>
        <td nowrap="nowrap" class="wordtd_header">销售合同号</td>
        <td nowrap="nowrap" class="wordtd_header">购买单位名称</td> 
        <td nowrap="nowrap" class="wordtd_header">使用单位名称</td>
        <td nowrap="nowrap" class="wordtd_header">维保开始日期</td>
        <td nowrap="nowrap" class="wordtd_header">维保结束日期</td>  
      </tr>
    </thead>
    <tbody>
      <logic:present name="entrustContractDetailList">
        <logic:iterate id="e" name="entrustContractDetailList" >
          <tr>
          	<td nowrap="nowrap" align="center" style="display:none;"><input type="text" name="maintRowid" value="${e.rowid}" readonly="readonly" class="noborder_center"/></td>
            <td nowrap="nowrap" align="center"><input type="text" name="elevatorNo" value="${e.elevatorNo}" readonly="readonly" class="noborder_center"/></td>
            <td nowrap="nowrap" align="center"><input type="text" name="elevatorType" value="${e.elevatorType}" readonly="readonly" class="noborder_center"/></td>
            <td nowrap="nowrap" align="center"><input type="text" name="floor" size="3" value="${e.floor}" readonly="readonly" class="noborder_center"/></td>
            <td nowrap="nowrap" align="center"><input type="text" name="stage" size="3" value="${e.stage}" readonly="readonly" class="noborder_center"/></td>
            <td nowrap="nowrap" align="center"><input type="text" name="door" size="3" value="${e.door}" readonly="readonly" class="noborder_center"/></td>
            <td nowrap="nowrap" align="center"><input type="text" name="high" size="3" value="${e.high}" readonly="readonly" class="noborder_center"/></td>
            <td nowrap="nowrap" align="center"><input type="text" name="elevatorParam" value="${e.elevatorParam}" readonly="readonly" class="noborder_center"/></td>
            <td nowrap="nowrap" align="center"><input type="text" name="annualInspectionDate" value="${e.annualInspectionDate}" size="12" readonly="readonly" class="noborder_center"/></td>
            <td nowrap="nowrap" align="center"><input type="text" name="salesContractNo" value="${e.salesContractNo}" readonly="readonly" class="noborder_center"/></td>
            <td nowrap="nowrap" align="center"><input type="text" name="projectName" value="${e.projectName}" class="noborder_center" size="30" readonly="readonly"/></td>
            <td nowrap="nowrap" align="center"><input type="text" name="maintAddress" value="${e.maintAddress}" class="noborder_center" size="30" readonly="readonly"/></td>
            <td nowrap="nowrap" align="center"><input type="text" name="mainSdate" value="${e.mainSdate }" readonly="readonly" class="noborder_center"/></td>
            <td nowrap="nowrap" align="center"><input type="text" name="mainEdate" value="${e.mainEdate }" readonly="readonly" class="noborder_center"/></td>
          </tr>
        </logic:iterate>
      </logic:present>
    </tbody>    
  </table>
</div>
<br>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td nowrap="nowrap" class="wordtd_a">合同总额:</td>
    <td width="25%">
       ${entrustContractMasterBean.contractTotal }
      <html:hidden name="entrustContractMasterBean" property="contractTotal" styleId="contractTotal" />       
    </td>          
    <td nowrap="nowrap" class="wordtd_a">其它费用:</td>
    <td width="20%">
      <html:text name="entrustContractMasterBean" property="otherFee" onchange="checkthisvalue(this);" size="10" />        
    </td> 
    <td nowrap="nowrap" class="wordtd_a">&nbsp;</td>
    <td width="20%">&nbsp;</td>        
  </tr>
  <tr>
    <td nowrap="nowrap" class="wordtd_a">付款方式:</td>
    <td colspan="5">
      <html:textarea name="entrustContractMasterBean" property="paymentMethod" rows="3" cols="100" styleClass="default_textarea"/><font color="red">*</font>   
    </td> 
  </tr>
  <tr>
    <td nowrap="nowrap" class="wordtd_a">附加合同条款:</td>
    <td colspan="5">      
      <html:textarea name="entrustContractMasterBean" property="contractTerms" rows="3" cols="100" styleClass="default_textarea"/>       
    </td>                  
  </tr>  
</table>

<script type="text/javascript">
initPage();
$("document").ready(function() {
	  setScrollTable("wrap_0","dynamictable_0",11);
	  
})
function initPage(){
	
   
  //$("#contractSdate,#contractEdate").bind("propertychange",function(){setContractPeriod();}); // 计算合同有效期  
  
  setTopRowDateInputsPropertychange(dynamictable_0); //动态表格第一行日期控件添加propertychange事件
}
</script>