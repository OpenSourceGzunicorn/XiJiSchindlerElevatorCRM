<%@page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>

<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<input type="hidden" name="quoteBillNo" value="${quoteBillNo}"/>
<html:hidden name="ServicingContractMasterList" property="wgBillno" />
<html:hidden name="ServicingContractMasterList" property="busType" value="${ServicingContractMasterList.busType }"/>
<table id="companyA" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td height="23" colspan="6">&nbsp;<b>客户信息</td>
  </tr>
  <tr>
    <td class="wordtd">甲方单位名称:</td>
    <td width="20%">
      ${CustomerList.companyName}
      <html:hidden name="ServicingContractMasterList" property="companyId" styleId="companyId"/>
    </td>
    <td class="wordtd">甲方单位地址:</td>
    <td width="20%">${CustomerList.address}</td>
    <td nowrap="nowrap" class="wordtd">甲方法人:</td>
    <td width="20%">${CustomerList.legalPerson}</td>  
  </tr>
  <tr>    
    <td class="wordtd">甲方委托人:</td>
    <td>${CustomerList.client}</td>
    <td class="wordtd">甲方联系人:</td>
    <td>${CustomerList.contacts}</td>   
    <td class="wordtd">甲方联系电话:</td>
    <td>${CustomerList.contactPhone}</td>          
  </tr>     
  <tr>
    <td nowrap="nowrap" class="wordtd">甲方传真:</td>
    <td>${CustomerList.fax}</td>   
    <td nowrap="nowrap" class="wordtd">甲方邮编:</td>
    <td>${CustomerList.postCode}</td>          
    <td nowrap="nowrap" class="wordtd">地址、电话:</td>
    <td>${CustomerList.accountHolder}</td> 
  </tr>
  <tr>      
    <td class="wordtd">甲方银行账号:</td>
    <td>${CustomerList.account}</td>          
    <td class="wordtd">甲方开户银行:</td>
    <td>${CustomerList.bank}</td>   
    <td nowrap="nowrap" class="wordtd">纳税人识别号:</td>
    <td>${CustomerList.taxId}</td>          
  </tr>                 
</table>
<br>

<table id="companyB" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd">乙方单位名称:</td>
    <td width="20%">
    	${companyB.companyName}
      <html:hidden name="ServicingContractMasterList" property="companyId2" styleId="companyId2" />
      <%-- <input type="text" name="companyName2" id="companyName2" value="${CustomerList2.companyName}" readonly="true" size="23"/>
      <input type="button" value=".." onclick="openWindowAndReturnValue3('searchCustomerAction','toSearchRecord','&cusNature=WT','2')" class="default_input"/><font color="red">*</font> --%>
   
    </td>
    <td class="wordtd">乙方单位地址:</td>
    <td width="20%">
    <font id="address2">${companyB.address}</font>
    </td>
    <td nowrap="nowrap" class="wordtd">乙方法人:</td>
    <td width="20%">
    <font id="legalPerson2">${companyB.legalPerson}</font>
    </td>
  </tr>
  <tr>       
    <td class="wordtd">乙方委托人:</td>
    <td>
    <font id="client2">${companyB.client}</font>
    </td>          
    <td class="wordtd">乙方联系人:</td>
    <td>
    <font id="contacts2">${companyB.contacts}</font>
    </td>   
    <td class="wordtd">乙方联系电话:</td>
    <td>
    <font id="contactPhone2">${companyB.contactPhone}</font>
    </td>          
  </tr>     
  <tr>
    <td nowrap="nowrap" class="wordtd">乙方传真:</td>
    <td>
    <font id="fax2">${companyB.fax}</font>
    </td>   
    <td nowrap="nowrap" class="wordtd">乙方邮编:</td>
    <td>
    <font id="postCode2">${companyB.postCode}</font>
    </td>          
    <td nowrap="nowrap" class="wordtd">乙方户名:</td>
    <td>
    <font id="accountHolder2">${companyB.accountHolder}</font>
    </td>   
  </tr>
  <tr>
    <td class="wordtd">乙方银行账号:</td>
    <td>
    <font id="account2">${companyB.account}</font>
    </td> 
    <td nowrap="nowrap" class="wordtd">&nbsp;</td>
    <td>&nbsp;</td>   
    <td nowrap="nowrap" class="wordtd">&nbsp;</td>
    <td>&nbsp;</td>                   
  </tr>            
</table>
<br>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" >
  <tr>
    <td height="23" colspan="6">&nbsp;<b>合同主信息</td>
  </tr>
  <tr>
    <td class="wordtd">维改合同号:</td>
    <td width="20%">
      <html:text name="ServicingContractMasterList" property="maintContractNo" readonly="true" styleClass="default_input_noborder"/>
    </td> 
    <td class="wordtd">委托合同号:</td>
    <td width="20%">
      &nbsp;
    </td>          
    <td nowrap="nowrap" class="wordtd">业务类别:</td>
    <td id="busType" width="20%">           
    </tr>             
  <tr>
    </td>
    <td nowrap="nowrap" class="wordtd">签订日期:</td>
    <td width="20%">
      <html:text name="ServicingContractMasterList" property="signingDate" styleId="signingDate" size="15" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/><font color="red">*</font> 
    </td>         
  
    <td class="wordtd">所属维保分部:</td>
    <td>
      ${userInfoList.comName }
      <html:hidden name="ServicingContractMasterList" property="maintDivision" />
    </td> 
      <td class="wordtd">所属维保站:</td>
        <td>
      ${maintStationName}
      <html:hidden name="ServicingContractMasterList" property="maintStation" />
    </td>
     </tr>
     <tr>
    <td nowrap="nowrap" class="wordtd">经办人:</td>
    <td>
    ${userInfoList.userName }
      <html:hidden name="ServicingContractMasterList" property="attn" />
    </td>
      </tr>
</table>
<br>


<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb"><b>&nbsp;维改信息</b></div>
  <div id="scrollBox" style="overflow:scroll; overflow-y:hidden">
  <table id="dynamictable_0" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <thead>
      <tr id="titleRow_0">
        <!-- <td class="wordtd_header"><input type="checkbox" onclick="checkTableAll(this)"/></td> -->
        <td class="wordtd_header">维改梯编号</td>
        <td class="wordtd_header" >销售合同号<font color="red">*</font></td>
        <td class="wordtd_header">项目名称<font color="red">*</font></td>
        <td class="wordtd_header" >维改内容<font color="red">*</font> </td>
      </tr>     
    </thead>
    <tfoot>
      <tr height="10"><td colspan="4"></td></tr>
    </tfoot>
    <tbody>
      <logic:present name="ServicingContractDetailList">
        <logic:iterate id="e" name="ServicingContractDetailList" >
          <tr>
           <!--  <td align="center" width="10%"><input type="checkbox" onclick="cancelCheckAll(this)"/></td>   -->         
            <td align="center" >${e.elevatorNo}<input type="hidden" name="elevatorNo" id="elevatorNo" value="${e.elevatorNo}" /></td>
            <td align="center" >${e.salesContractNo}<input type="hidden"  name="salesContractNo" id="salesContractNo" value="${e.salesContractNo}" /></td>
            <td align="center" >${e.projectName}<input type="hidden" name="projectName" id="projectName" value="${e.projectName}" /></td>
            <td>${e.contents}<input type="hidden" name="contents" id="contents" value="${e.contents}"></td>
          </tr>
        </logic:iterate>
      </logic:present>      
    </tbody>    
  </table>
</div>
<br>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" style="table-layout:fixed;">
  <tr>
    <td class="wordtd" nowrap="nowrap">合同总额(元):</td>
    <td width="30%">
    ${ServicingContractMasterList.contractTotal }
      <html:hidden name="ServicingContractMasterList" property="contractTotal"/>        
    </td>          
    <td class="wordtd" nowrap="nowrap">其它费用(元):</td>
    <td width="30%">
      <html:text property="otherFee" onkeypress="f_check_number3()"  onchange="checkthisvalue(this);" />        
    </td>        
  </tr>
  <tr>
    <td class="wordtd" nowrap="nowrap">付款方式:</td>
    <td colspan="3">
     
      <html:textarea name="ServicingContractMasterList"  value="${ServicingContractQuoteMasterList.paymentMethodApply}" property="paymentMethod" rows="3" cols="100" styleClass="default_textarea"/> 
    </td> 
  </tr>
  <tr>
    <td class="wordtd">附加合同条款:</td>
    <td colspan="3">      
      <html:textarea name="ServicingContractMasterList" value="${ServicingContractQuoteMasterList.specialApplication}"  property="contractTerms" rows="3" cols="100" styleClass="default_textarea"/>       
    </td>                  
  </tr>  
</table>

<script type="text/javascript">
$("document").ready(function(){
	setScrollTable("scrollBox","dynamictable_0",10);
})

function setbus(){
  var busType='${ServicingContractMasterList.busType}';
  $("#busType").html(busType=="W"?"维修":"改造");
}
setbus();
</script>