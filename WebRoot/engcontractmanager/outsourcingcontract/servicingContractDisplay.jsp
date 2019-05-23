<%@page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>
<a id="ss"></a>
<html:hidden property="isreturn"/>

<html:hidden property="id" value="${ServicingContractMasterList.billno}"/>
<html:hidden property="taskSubFlag"/>
<html:hidden property="auditStatus"/>
<html:hidden property="submitType"/>
<input type="hidden" name="details" id="details">
<input type="hidden" name="isSelectAdd" id="isSelectAdd" value="${isSelectAdd}"/>
<div height="23" width="100%" class="tb" style="border-bottom: 0">&nbsp;<b>客户信息</b></div>
<table id="companyA" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" >
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

<table id="companyB" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" >
  <tr>
    <td class="wordtd">乙方单位名称:</td>
    <td width="20%">
      ${CustomerList2.companyName}
      <html:hidden name="ServicingContractMasterList" property="companyId2" styleId="companyId2" />
   </td>
    <td class="wordtd">乙方单位地址:</td>
    <td width="20%">${CustomerList2.address}</td>
    <td class="wordtd" nowrap="nowrap">乙方法人:</td>
    <td width="20%">${CustomerList2.legalPerson}</td>
  </tr>
  <tr>       
    <td class="wordtd">乙方委托人:</td>
    <td>${CustomerList2.client}</td>          
    <td class="wordtd">乙方联系人:</td>
    <td>${CustomerList2.contacts}</td>   
    <td class="wordtd">乙方联系电话:</td>
    <td>${CustomerList2.contactPhone}</td>          
  </tr>     
  <tr>
    <td nowrap="nowrap" class="wordtd">乙方传真:</td>
    <td>${CustomerList2.fax}</td>   
    <td nowrap="nowrap" class="wordtd">乙方邮编:</td>
    <td>${CustomerList2.postCode}</td>          
    <td nowrap="nowrap" class="wordtd">乙方户名:</td>
    <td>${CustomerList2.accountHolder}</td>   
  </tr>
  <tr>
    <td class="wordtd">乙方银行账号:</td>
    <td>${CustomerList2.account}</td> 
    <td nowrap="nowrap" class="wordtd">&nbsp;</td>
    <td>&nbsp;</td>   
    <td nowrap="nowrap" class="wordtd">&nbsp;</td>
    <td>&nbsp;</td>                   
  </tr>            
</table>
<br>
<div height="23"  class="tb" style="border-bottom: 0" width="100%" >&nbsp;<b>合同主信息</b></div>
<table  border="0"  width="100%" cellpadding="0" cellspacing="0" class="tb" >
  <tr>
    <td class="wordtd_a">维改合同号:</td>
    <td width="20%">
    ${ServicingContractMasterList.maintContractNo }
    </td>          
     <td class="wordtd">委托合同号:</td>
    <td width="20%">
      ${ServicingContractMasterList.outContractNo }
    </td>
    <td nowrap="nowrap" class="wordtd">业务类别:</td>
    <td width="20%" id="busType"></td>
    </tr>               
  <tr>
    <td nowrap="nowrap" class="wordtd">签订日期:</td>
    <td width="20%">
   ${ServicingContractMasterList.signingDate }
    </td>         
    <td class="wordtd" >所属维保分部:</td>
    <td>
      ${ServicingContractMasterList.maintDivision }
    </td> 
      <td nowrap="nowrap" class="wordtd">所属维保站:</td>
      <td>   
         <bean:write name="maintStationName"/>    
      </td>
      </tr> 
      <tr>
    <td nowrap="nowrap" class="wordtd" >经办人:</td>
    <td>
    ${ServicingContractMasterList.attn }
    </td>
	 </tr> 
</table>
<br>


<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb"><b>&nbsp;维改信息</b></div>
<div id="scrollBox" style="overflow:scroll; overflow-y:hidden">
  <table id="dynamictable_0" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <thead>
      <tr id="titleRow_0">
        <td class="wordtd_header">维改梯编号</td>
        <td class="wordtd_header" >销售合同号</td>
        <td class="wordtd_header">项目名称</td>
        <td class="wordtd_header" >维改内容</td>
      </tr>
    </thead>
    <tfoot>
      <tr height="10"><td colspan="4"></td></tr>
    </tfoot>
    <tbody>
      <logic:present name="ServicingContractDetailList">
        <logic:iterate id="e" name="ServicingContractDetailList" >
          <tr>         
            <td align="center" ><a  onclick="openwindowcb('${e.elevatorNo}');" style="cursor:hand;text-decoration: underline;color: blue;">${e.elevatorNo}</a></td>
            <td align="center" >${e.salesContractNo}</td>
            <td align="center" >${e.projectName}</td>
            <td align="center" >${e.contents}</td>
          </tr>
        </logic:iterate>
      </logic:present>    
    </tbody>    
  </table>
</div>
<br>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" >
  <tr>
    <td class="wordtd">合同总额(元):</td>
    <td width="20%">
     ${ServicingContractMasterList.contractTotal}
           
    </td>         
    <td class="wordtd">其它费用(元):</td>
    <td width="20%">
    ${ServicingContractMasterList.otherFee}
        
    </td>     
  </tr>
  <tr>
    <td class="wordtd">付款方式:</td>
    <td colspan="5">
    ${ServicingContractMasterList.paymentMethod}
    </td> 
  </tr>
  <tr>
    <td class="wordtd">附加合同条款:</td>
    <td colspan="5">                
    ${ServicingContractMasterList.contractTerms}
    </td>                  
  </tr>  
</table>
<br>
 <logic:notEqual name="typejsp" value="display">
 <div height="25"  class="tb"  style="border-bottom: 0">&nbsp;<b>审核信息</b></div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" >
  <tr>
    <td nowrap="nowrap" class="wordtd">审核人:</td>
    <td width="20%">
      ${ServicingContractMasterList.auditOperid }
    </td>
    <td nowrap="nowrap" class="wordtd">审核时间:</td>
    <td width="20%">${ServicingContractMasterList.auditDate }
    <input type="hidden" name="auditDate" id="auditDate" value="${ServicingContractMasterList.auditDate }"> 
    </td>  
    <td nowrap="nowrap" class="wordtd"></td>
    <td width="20%"></td >   
  </tr>
  <tr>
  <td nowrap="nowrap" class="wordtd">审核意见:</td>
  <td colspan="5"><textarea name="auditRem" id="auditRem" cols="80" rows="3">${ServicingContractMasterList.auditRem }</textarea></td>
  </tr>
  </table>
<br>
 </logic:notEqual>
 <logic:equal name="typejsp" value="display">
 <div height="25"  class="tb"  style="border-bottom: 0">&nbsp;<b>审核信息</b></div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" >
  <tr>
    <td nowrap="nowrap" class="wordtd">审核人:</td>
    <td width="20%">
      ${ServicingContractMasterList.auditOperid }
    </td> 
    <td nowrap="nowrap" class="wordtd">审核时间:</td>
    <td width="20%">${ServicingContractMasterList.auditDate }
    </td> 
    <td nowrap="nowrap" class="wordtd"></td>
    <td width="20%">
    </td >   
  </tr>
  <tr>
  <td nowrap="nowrap" class="wordtd">审核意见:</td>
  <td colspan="5">${ServicingContractMasterList.auditRem }</td>
  </tr>
  </table>
<br>
</logic:equal>
<script type="text/javascript">
$("document").ready(function(){
	setScrollTable("scrollBox","dynamictable_0",10);
})

function setbus(){
  var busType='${ServicingContractMasterList.busType}';
  $("#busType").html(busType=="W"?"维修":"改造");
}
setbus();
function openwindowcb(obj){
window.open('<html:rewrite page="/elevatorSaleAction.do"/>?method=toDisplayRecord&isOpen=Y&id='+obj,'','height=500px, width=1000px,scrollbars=yes, resizable=yes,directories=no');
}

</script>