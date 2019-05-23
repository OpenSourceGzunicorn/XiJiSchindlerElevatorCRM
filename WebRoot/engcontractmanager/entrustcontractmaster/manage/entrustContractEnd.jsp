<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<style>
  .show{display:block;}
  .hide{display:none;}
</style>

<br>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=GBK">
  <title>XJSCRM</title>
</head>
<body>
  <html:errors/>
  <html:form action="/entrustContractMasterAction.do?method=toEndRecord">
  
<logic:present name="entrustContractMasterBean">
<html:hidden name="entrustContractMasterBean" property="billNo"/>

  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td nowrap="nowrap" height="23" colspan="6">&nbsp;<b>客户信息</td>
    </tr>
    <tr>
      <td nowrap="nowrap" class="wordtd_a">甲方单位名称:</td>
      <td width="20%"><bean:write name="companyA" property="companyName"/></td>
      <td nowrap="nowrap" class="wordtd_a">甲方单位地址:</td>
      <td width="20%"><bean:write name="companyA" property="address"/></td>
      <td nowrap="nowrap" class="wordtd_a">甲方法人:</td>
      <td width="20%"><bean:write name="companyA" property="legalPerson"/></td>   
    </tr>
    <tr>
      <td nowrap="nowrap" class="wordtd_a">甲方委托人:</td>
      <td ><bean:write name="companyA" property="client"/></td>          
      <td nowrap="nowrap" class="wordtd_a">甲方联系人:</td>
      <td ><bean:write name="companyA" property="contacts"/></td>   
      <td nowrap="nowrap" class="wordtd_a">甲方联系电话:</td>
      <td ><bean:write name="companyA" property="contactPhone"/></td>          
    </tr>     
    <tr>
      <td nowrap="nowrap" class="wordtd_a">甲方传真:</td>
      <td ><bean:write name="companyA" property="fax"/></td>   
      <td nowrap="nowrap" class="wordtd_a">甲方邮编:</td>
      <td ><bean:write name="companyA" property="postCode"/></td>          
      <td nowrap="nowrap" class="wordtd_a">地址、电话:</td>
      <td ><bean:write name="companyA" property="accountHolder"/></td>   
    </tr>
    <tr>
      <td nowrap="nowrap" class="wordtd_a">甲方银行账号:</td>
      <td ><bean:write name="companyA" property="account"/></td>          
      <td nowrap="nowrap" class="wordtd_a">甲方开户银行:</td>
      <td ><bean:write name="companyA" property="bank"/></td>   
      <td nowrap="nowrap" class="wordtd_a">纳税人识别号:</td>
      <td ><bean:write name="companyA" property="taxId"/></td>          
    </tr>                 
  </table>
  <br>
  
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td nowrap="nowrap" class="wordtd_a">乙方单位名称:</td>
      <td width="20%"><bean:write name="companyB" property="companyName"/></td>
      <td nowrap="nowrap" class="wordtd_a">乙方单位地址:</td>
      <td width="20%"><bean:write name="companyB" property="address"/></td>
      <td nowrap="nowrap" class="wordtd_a">乙方法人:</td>
      <td width="20%"><bean:write name="companyB" property="legalPerson"/></td>   
    </tr>
    <tr>
      <td nowrap="nowrap" class="wordtd_a">乙方委托人:</td>
      <td ><bean:write name="companyB" property="client"/></td>          
      <td nowrap="nowrap" class="wordtd_a">乙方联系人:</td>
      <td ><bean:write name="companyB" property="contacts"/></td>   
      <td nowrap="nowrap" class="wordtd_a">乙方联系电话:</td>
      <td ><bean:write name="companyB" property="contactPhone"/></td>          
    </tr>     
    <tr>
      <td nowrap="nowrap" class="wordtd_a">乙方传真:</td>
      <td ><bean:write name="companyB" property="fax"/></td>   
      <td nowrap="nowrap" class="wordtd_a">乙方邮编:</td>
      <td ><bean:write name="companyB" property="postCode"/></td>          
      <td nowrap="nowrap" class="wordtd_a">乙方户名:</td>
      <td ><bean:write name="companyB" property="accountHolder"/></td>   
    </tr>
    <tr>
      <td nowrap="nowrap" class="wordtd_a">乙方银行账号:</td>
      <td ><bean:write name="companyB" property="account"/></td>
      <td nowrap="nowrap" class="wordtd_a">&nbsp;</td>
      <td >&nbsp;</td>   
      <td nowrap="nowrap" class="wordtd_a">&nbsp;</td>
      <td >&nbsp;</td>            
    </tr>            
  </table>
  <br>
  
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
     <tr>
      <td nowrap="nowrap" height="23" colspan="6">&nbsp;<b>合同主信息</td>
    </tr>
    <tr>
      <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContract.maintContractNo"/>:</td>
      <td width="20%">
        <bean:write name="entrustContractMasterBean" property="maintContractNo"/>          
      </td>          
      <td nowrap="nowrap" class="wordtd_a">委托合同号：</td>
      <td width="20%"><bean:write name="entrustContractMasterBean" property="entrustContractNo"/> </td>
      <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContract.contractNatureOf"/>:</td>
      <td width="20%">
        <bean:write name="entrustContractMasterBean" property="contractNatureOf"/>        
      </td>        
    </tr>  
    <tr>
      <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContract.contractPeriod"/>(月):</td>
      <td >
        <bean:write name="entrustContractMasterBean" property="contractPeriod"/>          
      </td>          
      <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContract.contractSdate"/>:</td>
      <td >
        <bean:write name="entrustContractMasterBean" property="contractSdate"/>        
      </td>
      <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContract.contractEdate"/>:</td>
      <td ><bean:write name="entrustContractMasterBean" property="contractEdate"/></td>         
    </tr>        
    <tr>
      <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContract.mainMode"/>:</td>     
      <td >
        <logic:match name="entrustContractMasterBean" property="mainMode" value="FREE">免费</logic:match>
        <logic:match name="entrustContractMasterBean" property="mainMode" value="PAID">收费</logic:match>
      </td>
      <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContract.maintDivision"/>:</td>
      <td >
        <bean:write name="entrustContractMasterBean" property="maintDivision"/>
      </td> 
      <td nowrap="nowrap" class="wordtd_a">所属维保站:</td>
      <td >
      <bean:write name="entrustContractMasterBean" property="maintStation"/>
    </td>       
    </tr>
     <tr> 
      <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContract.attn"/>:</td>
      <td >
        <bean:write name="entrustContractMasterBean" property="attn"/>
      </td>        
      <td nowrap="nowrap" class="wordtd_a">录入人:</td>
      <td ><bean:write name="entrustContractMasterBean" property="operId"/></td>   
      <td nowrap="nowrap" class="wordtd_a">录入日期:</td>
      <td ><bean:write name="entrustContractMasterBean" property="operDate"/></td>      
    </tr>
    <%-- 
     <tr> 
      <td nowrap="nowrap" class="wordtd_a">退保日期:</td>
      <td>
        <bean:write name="entrustContractMasterBean" property="tbDate"/>
      </td>        
      <td nowrap="nowrap" class="wordtd_a">退保录入人:</td>
      <td ><bean:write name="entrustContractMasterBean" property="tbOperId"/></td>   
      <td nowrap="nowrap" class="wordtd_a">&nbsp;</td>
      <td >&nbsp;</td>    
    </tr> 
    
    --%>
    <tr> 
      <td nowrap="nowrap" class="wordtd_a">合同终止日期:</td>
      <td>
        <html:text name="entrustContractMasterBean" property="endDate" styleId="endDate" size="13" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})" />
	</td>        
      <td nowrap="nowrap" class="wordtd_a">合同终止录入人:</td>
      <td ><bean:write name="entrustContractMasterBean" property="endOperId"/></td>   
      <td nowrap="nowrap" class="wordtd_a">合同终止录入日期:</td>
      <td ><bean:write name="entrustContractMasterBean" property="endOperDate"/></td>    
    </tr> 
  </table>
  <br>
  
  <div style="width: 100%;padding-top: 4;padding-bottom: 4;border-bottom: 0" class="tb">        
   <b>&nbsp;合同明细</b>
  </div>
  <div id="wrap_0" style="overflow-x:scroll">
    <table id="dynamictable_0" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <thead id="header_0">
        <tr id="titleRow_0">
          <td nowrap="nowrap" class="wordtd_header">电梯编号</td>
          <td nowrap="nowrap" class="wordtd_header">层</td>
          <td nowrap="nowrap" class="wordtd_header">站</td>
          <td nowrap="nowrap" class="wordtd_header">门</td>
          <td nowrap="nowrap" class="wordtd_header">提升高度</td>
          <td nowrap="nowrap" class="wordtd_header">规格型号</td>
          <td nowrap="nowrap" class="wordtd_header">年检日期</td>
          <td nowrap="nowrap" class="wordtd_header">销售合同号</td>
          <td nowrap="nowrap" class="wordtd_header">项目名称</td>
          <td nowrap="nowrap" class="wordtd_header">维保开始日期</td>
          <td nowrap="nowrap" class="wordtd_header">维保结束日期</td>
        </tr>
      </thead>
      <tfoot>
        <tr height="15px"><td colspan="20"></td></tr>
      </tfoot>
      <tbody>
        <logic:present name="entrustContractDetailList">
          <logic:iterate id="e" name="entrustContractDetailList" >
            <tr>
              <td nowrap="nowrap" align="center">${e.elevatorNo}</td>
              <td nowrap="nowrap" align="center">${e.floor}</td>
              <td nowrap="nowrap" align="center">${e.stage}</td>
              <td nowrap="nowrap" align="center">${e.door}</td>
              <td nowrap="nowrap" align="center">${e.high}</td>
              <td nowrap="nowrap" align="center">${e.elevatorParam}</td>
              <td nowrap="nowrap" align="center">${e.annualInspectionDate}</td>
              <td nowrap="nowrap" align="center">${e.salesContractNo}</td>
              <td nowrap="nowrap" align="center">${e.projectName}</td>
              <td nowrap="nowrap" align="center">${e.mainSdate }</td>
              <td nowrap="nowrap" align="center">${e.mainEdate }</td>
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
      <td width="20%">
      		${entrustContractMasterBean.contractTotal}
      </td>          
      <td nowrap="nowrap" class="wordtd_a">其它费用:</td>
      <td width="20%">
      		${entrustContractMasterBean.otherFee}
      </td>    
      <td nowrap="nowrap" class="wordtd_a">&nbsp;</td>
      <td width="20%">&nbsp;</td>      
    </tr>
    <tr>
      <td nowrap="nowrap" class="wordtd_a">付款方式:</td>
      <td colspan="5">
        <bean:write name="entrustContractMasterBean" property="paymentMethod"/>
      </td> 
    </tr>
    <tr>
      <td nowrap="nowrap" class="wordtd_a">附加合同条款:</td>
      <td  colspan="5">     
        <bean:write name="entrustContractMasterBean" property="contractTerms"/> 
      </td>                  
    </tr>  
  </table>
  <br/>
  <div style="width: 100%;padding-top: 4;padding-bottom: 4;border-bottom: 0" class="tb">        
   <b>&nbsp;合同审核</b>
  </div>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  	<tr>
  		<td nowrap="nowrap" class="wordtd_a">审核人：</td>
  		<td width="20%"><bean:write name="entrustContractMasterBean" property="auditOperid"/></td>
  		<td nowrap="nowrap" class="wordtd_a">审核时间：</td>
  		<td width="20%"><bean:write name="entrustContractMasterBean" property="auditDate"/></td>
  		<td nowrap="nowrap" class="wordtd_a">&nbsp;</td>
        <td width="20%">&nbsp;</td>    
  	</tr>
  	<tr>
  		<td nowrap="nowrap" class="wordtd_a">审核意见：</td>
  		<td colspan="5">${entrustContractMasterBean.auditRem}</td>
  	</tr>
  </table>

</logic:present>
  <script type="text/javascript">
//滚动容器自适应宽度
  $("document").ready(function() {
	  
    $("input[name='rownum']").each(function(i,obj){
      obj.value = i+1;
    })

    setScrollTable("wrap_0","dynamictable_0",11);

})
  
  </script> 
</html:form>
</body>

  
  