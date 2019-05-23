<%@page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>
<a id="ss"></a>
<html:hidden property="isreturn"/>
<html:hidden property="id"/>
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
      <input type="text" name="companyName" id="companyName"  readonly="true" size="30"/>
      <html:hidden  property="companyId" styleId="companyId"/>
      <input type="button" value=".." onclick="openWindowAndReturnValue2('searchCustomerAction')" class="default_input"/><font color="red">*</font>
    </td>
    <td class="wordtd">甲方单位地址:</td>
    <td width="20%">
    <font id="address"></font>
    <td nowrap="nowrap" class="wordtd">甲方法人:</td>
    <td width="20%">
    <font id="legalPerson"></font></td>  
  </tr>
  <tr>    
    <td class="wordtd">甲方委托人:</td>
    <td>
    <font id="client"></font>
    </td>
    <td class="wordtd">甲方联系人:</td>
    <td>
    <font id="contacts"></font>
    </td>   
    <td class="wordtd">甲方联系电话:</td>
    <td>
    <font id="contactPhone"></font>
    </td>          
  </tr>     
  <tr>
    <td nowrap="nowrap" class="wordtd">甲方传真:</td>
    <td>
    <font id="fax"></font>
   </td>   
    <td nowrap="nowrap" class="wordtd">甲方邮编:</td>
    <td>
     <font id="postCode"></font>
    </td>          
    <td nowrap="nowrap" class="wordtd">地址、电话:</td>
    <td>
    <font id="postCode"></font>
    </td> 
  </tr>
  <tr>      
    <td class="wordtd">甲方银行账号:</td>
    <td>
    <font id="account"></font>
    </td>          
    <td class="wordtd">甲方开户银行:</td>
    <td>
     <font id="bank"></font>
    </td>   
    <td nowrap="nowrap" class="wordtd">纳税人识别号:</td>
    <td>
    <font id="taxId"></font>
    </td>          
  </tr>                
</table>
<br>

<table id="companyB" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" >
  <tr>
    <td class="wordtd">乙方单位名称:</td>
    <td width="20%">
    <input type="text" name="companyName" id="companyName"  value="${CustomerList.companyName}" style="border: none;"  readonly="true" size="30"/>
    <html:hidden property="companyId2" styleId="companyId2" value=""/>
    </td>
    <td class="wordtd">乙方单位地址:</td>
    <td width="20%">${CustomerList.address}</td>
    <td nowrap="nowrap" class="wordtd">乙方法人:</td>
    <td width="20%">${CustomerList.legalPerson}</td>
  </tr>
  <tr>       
    <td class="wordtd">乙方委托人:</td>
    <td>${CustomerList.client}</td>          
    <td class="wordtd">乙方联系人:</td>
    <td>${CustomerList.contacts}</td>   
    <td class="wordtd">乙方联系电话:</td>
    <td>${CustomerList.contactPhone}</td>          
  </tr>     
  <tr>
    <td nowrap="nowrap" class="wordtd">乙方传真:</td>
    <td>${CustomerList.fax}</td>   
    <td nowrap="nowrap" class="wordtd">乙方邮编:</td>
    <td>${CustomerList.postCode}</td>          
    <td nowrap="nowrap" class="wordtd">乙方户名:</td>
    <td>${CustomerList.accountHolder}</td>   
  </tr>
  <tr>
    <td class="wordtd">乙方银行账号:</td>
    <td>${CustomerList.account}</td> 
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
      <html:text property="maintContractNo" readonly="true" styleClass="default_input_noborder"/>
    </td>          
    <td nowrap="nowrap" class="wordtd">业务类别:</td>
   <td width="20%">
   <html:select property="busType">
    <html:option value="W">维修</html:option>
    <html:option value="G">改造</html:option>
   </html:select>       
    </td>
    <td nowrap="nowrap" class="wordtd">签订日期:</td>
    <td width="20%">
      <html:text property="signingDate" styleId="signingDate" size="15" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/><font color="red">*</font> 
    </td>         
  </tr>             
  <tr>
    <td class="wordtd">所属维保分部:</td>
    <td>
      ${userInfoList.comName }
      <html:hidden property="maintDivision" value="${userInfoList.comName }" />
    </td> 
      <td class="wordtd">所属维保站:</td>
        <td>
      <html:select  property="maintStation">
       <html:option value=" ">请选择</html:option>
       <html:options collection="maintStationList" property="storageid" labelProperty="storagename"/>
      </html:select>
    </td>
    <td nowrap="nowrap" class="wordtd">经办人:</td>
    <td>
    ${userInfoList.userName }
      
    </td>
    </tr>
</table>
<br>
  <div class="tb" style="padding-top: 2;padding-bottom: 2;border-bottom: 0">&nbsp;<input type="button" value=" + " onclick="addElevators('dynamictable_0')">
            <input type="button" value=" - " onclick="deleteRow('dynamictable_0')">&nbsp; <b>电梯信息</b></div>
    <div id="scrollBox" style="overflow:scroll; overflow-y:hidden">    
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" id="dynamictable_0">
      <thead  >
        <tr id="tb_head" class="tb_head" >
          <td class="wordtd_header" width="3%"><input type="checkbox" id="checkAll" onclick="checkTableAll('dynamictable_0',this)"/></td>
          <td class="wordtd_header" width="10%">维改梯编号<font color="red">*</font></td>
          <td class="wordtd_header" width="10%">销售合同号<font color="red">*</font></td>
          <td class="wordtd_header" width="23%">项目名称<font color="red">*</font></td>
          <td class="wordtd_header" width="50%">维改内容<font color="red">*</font></td>
        </tr>
      </thead>
      <tfoot>
        <tr height="23"><td colspan="5"></td></tr>
      </tfoot>
      <tbody>
        <tr id="titleRow_0">
         <td align="center"><input type="checkbox" onclick="cancelCheckAll('dynamictable_0','checkAll')"/></td>
         <td align="center"><input type="text" name="elevatorNo" id="elevatorNo" readonly="readonly" class="noborder_center"/></td>
         <td align="center"><input type="text" name="salesContractNo" id="salesContractNo" readonly="readonly" class="noborder_center"/></td>
         <td align="center"><input type="text" name="projectName" id="projectName" readonly="readonly" class="noborder_center"/></td>
         <td align="center"><textarea name="contents" id="contents" rows="2" cols="80"></textarea></td>
        </tr>
        
      </tbody>    
    </table>
    </div>
    <br>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" >
  <tr>
     <td class="wordtd" nowrap="nowrap">标准报价(元):</td>
    <td width="20%">
      <html:text property="basePrice" onkeypress="f_check_number3()" onchange="checkthisvalue(this);" /><font color="red">*</font>     
    </td>  
    <td class="wordtd" nowrap="nowrap">合同总额(元):</td>
    <td width="20%">
      <html:text  property="contractTotal" onkeypress="f_check_number3()" onchange="checkthisvalue(this);" /><font color="red">*</font>     
    </td>    
    <td class="wordtd" nowrap="nowrap">其它费用(元):</td>
    <td width="20%">
      <html:text property="otherFee" onkeypress="f_check_number3()" onchange="checkthisvalue(this);" /><font color="red">*</font>        
    </td>     
  </tr>
  <tr>
    <td class="wordtd" nowrap="nowrap">付款方式:</td>
    <td colspan="5">
     
      <html:textarea  property="paymentMethod" rows="3" cols="100" /> 
    </td> 
  </tr>
  <tr>
    <td class="wordtd">附加合同条款:</td>
    <td colspan="5">      
      <html:textarea   property="contractTerms" rows="3" cols="100" />       
    </td>                  
  </tr>  
</table>

  <script type="text/javascript">
$("document").ready(function(){

    	setDynamicTable("dynamictable_0","titleRow_0");
	    setScrollTable("scrollBox","dynamictable_0",10);

	}); 
</script>