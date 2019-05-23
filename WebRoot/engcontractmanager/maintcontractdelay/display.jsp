<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>

<style>
  .show{display:block;}
  .hide{display:none;}
</style>

<logic:present name="maintContractBean">
  <html:hidden name="maintContractBean" property="billNo"/>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td height="23" colspan="6">&nbsp;<b>客户信息</td>
    </tr>
    <tr>
      <td class="wordtd">甲方单位名称:</td>
      <td width="20%"><bean:write name="companyA" property="companyName"/></td>
      <td class="wordtd">甲方单位地址:</td>
      <td width="20%"><bean:write name="companyA" property="address"/></td>
      <td class="wordtd">甲方法人:</td>
      <td width="20%"><bean:write name="companyA" property="legalPerson"/></td>   
    </tr>
    <tr>
      <td class="wordtd">甲方委托人:</td>
      <td><bean:write name="companyA" property="client"/></td>          
      <td class="wordtd">甲方联系人:</td>
      <td><bean:write name="companyA" property="contacts"/></td>   
      <td class="wordtd">甲方联系电话:</td>
      <td><bean:write name="companyA" property="contactPhone"/></td>          
    </tr>     
    <tr>
      <td class="wordtd">甲方传真:</td>
      <td><bean:write name="companyA" property="fax"/></td>   
      <td class="wordtd">甲方邮编:</td>
      <td><bean:write name="companyA" property="postCode"/></td>          
      <td class="wordtd">地址、电话:</td>
      <td><bean:write name="companyA" property="accountHolder"/></td>   
    </tr>
    <tr>
      <td class="wordtd">甲方银行账号:</td>
      <td><bean:write name="companyA" property="account"/></td>          
      <td class="wordtd">甲方开户银行:</td>
      <td><bean:write name="companyA" property="bank"/></td>   
      <td class="wordtd">纳税人识别号:</td>
      <td><bean:write name="companyA" property="taxId"/></td>          
    </tr>                 
  </table>
  <br>
  
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td class="wordtd">乙方单位名称:</td>
      <td width="20%"><bean:write name="companyB" property="companyName"/></td>
      <td class="wordtd">乙方单位地址:</td>
      <td width="20%"><bean:write name="companyB" property="address"/></td>
      <td nowrap="nowrap" class="wordtd">乙方法人:</td>
      <td width="20%"><bean:write name="companyB" property="legalPerson"/></td>   
    </tr>
    <tr>
      <td class="wordtd">乙方委托人:</td>
      <td><bean:write name="companyB" property="client"/></td>          
      <td class="wordtd">乙方联系人:</td>
      <td><bean:write name="companyB" property="contacts"/></td>   
      <td class="wordtd">乙方联系电话:</td>
      <td><bean:write name="companyB" property="contactPhone"/></td>          
    </tr>     
    <tr>
      <td nowrap="nowrap" class="wordtd">乙方传真:</td>
      <td><bean:write name="companyB" property="fax"/></td>   
      <td nowrap="nowrap" class="wordtd">乙方邮编:</td>
      <td><bean:write name="companyB" property="postCode"/></td>          
      <td nowrap="nowrap" class="wordtd">乙方户名:</td>
      <td><bean:write name="companyB" property="accountHolder"/></td>   
    </tr>
    <tr>
      <td class="wordtd">乙方银行账号:</td>
      <td><bean:write name="companyB" property="account"/></td>
      <td class="wordtd">&nbsp;</td>
      <td></td>   
      <td class="wordtd">&nbsp;</td>
      <td></td>            
    </tr>            
  </table>
  <br>
  
  <table id="contractInfoTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td height="23" colspan="6">&nbsp;<b>合同主信息</td>
    </tr>
    <tr>
      <td class="wordtd"><bean:message key="maintContract.maintContractNo"/>:</td>
      <td width="20%">
        <bean:write name="maintContractBean" property="maintContractNo"/>          
      </td>          
      <td nowrap="nowrap" class="wordtd"><bean:message key="maintContract.contractNatureOf"/>:</td>
      <td width="20%">自保</td>
      <td nowrap="nowrap" class="wordtd"><bean:message key="maintContract.mainMode"/>:</td>
      <td width="20%">
        <logic:match name="maintContractBean" property="mainMode" value="FREE">免费</logic:match>
        <logic:match name="maintContractBean" property="mainMode" value="PAID">收费</logic:match>
      </td>         
    </tr>  
    <tr>
      <td class="wordtd"><bean:message key="maintContract.contractPeriod"/>:</td>
      <td>
        <bean:write name="maintContractBean" property="contractPeriod"/>    
      </td>          
      <td class="wordtd"><bean:message key="maintContract.contractSdate"/>:</td>
      <td>
        <bean:write name="maintContractBean" property="contractSdate"/>
      </td>
      <td class="wordtd"><bean:message key="maintContract.contractEdate"/>:</td>
      <td>
        <bean:write name="maintContractBean" property="contractEdate"/>
      </td>         
    </tr>        
    <tr>
      <td class="wordtd"><bean:message key="maintContract.maintDivision"/>:</td>
      <td>
        <bean:write name="maintContractBean" property="maintDivision"/>
      </td> 
      <td class="wordtd">所属维保站:</td>
      <td>
        <html:hidden name="maintContractBean" property="maintStation"/>
        <bean:write name="maintStationName"/>
      </td>  
      <td class="wordtd"><bean:message key="maintContract.attn"/>:</td>
      <td>
        <bean:write name="maintContractBean" property="attn"/>
      </td>       
    </tr> 
    <tr>
      <td nowrap="nowrap" class="wordtd">录入人:</td>
      <td>
        <bean:write name="maintContractBean" property="operId"/>
      </td>
      <td nowrap="nowrap" class="wordtd">录入日期:</td>
      <td>
        <bean:write name="maintContractBean" property="operDate"/>
      </td>  
      <td class="wordtd">&nbsp;</td>
      <td>&nbsp;</td>       
    </tr>   
  </table>
  <br>
  
  <div style="border:solid #999999 1px;border-bottom:none;padding-top: 4;padding-bottom: 4;background:#ffffff;">        
   <b>&nbsp;合同明细</b>
  </div>
  <div id="scrollBox" style="overflow:scroll;">
    <table id="scrollTable" width="1800px" border="0" cellpadding="0" cellspacing="0" class="tb">
      <thead>
        <tr id="titleRow">
          <td class="wordtd_header" nowrap="nowrap">序号</td>          
          <td class="wordtd_header" nowrap="nowrap">下达维保站</td>
          <td class="wordtd_header" nowrap="nowrap">签署方式</td>
          <td class="wordtd_header" nowrap="nowrap">电梯编号</td>
          <td class="wordtd_header" nowrap="nowrap">电梯类型</td>
          <td class="wordtd_header" nowrap="nowrap">层</td>
          <td class="wordtd_header" nowrap="nowrap">站</td>
          <td class="wordtd_header" nowrap="nowrap">门</td>
          <td class="wordtd_header" nowrap="nowrap">提升高度</td>
          <td class="wordtd_header" nowrap="nowrap">规格型号</td>
          <td class="wordtd_header" nowrap="nowrap">年检日期</td>
          <td class="wordtd_header" nowrap="nowrap">销售合同号</td>
          <td class="wordtd_header" nowrap="nowrap">购买单位名称</td>
          <td class="wordtd_header" nowrap="nowrap">使用单位名称</td>
          <td class="wordtd_header" nowrap="nowrap">维保开始日期</td>
          <td class="wordtd_header" nowrap="nowrap">维保结束日期</td>
          <td class="wordtd_header" nowrap="nowrap">延保结束日期<span class="hide" style="color:red;">*</span></td> 
        </tr>
      </thead>
      <tfoot>
        <tr height="15px"><td colspan="17"></td></tr>
      </tfoot>
      <tbody>
        <logic:present name="maintContractDetailList">
          <logic:iterate id="e" name="maintContractDetailList" indexId="i">
            <tr>
              <td align="center">${i+1}
                <html:hidden name="e" property="rowid" />
                <html:hidden name="e" property="elevatorNo" />                
                <html:hidden name="e" property="assignedMainStation" />
                <html:hidden name="e" property="mainEdate" />
              </td>
              <td align="center" width="1%">           
                  <logic:iterate id="ms" name="maintStationList">
                    ${e.assignedMainStation == ms.storageid ? ms.storagename : ''}                
                  </logic:iterate>                                
              </td>
              <td align="center">${e.signWay}</td> 
              <td align="center">
                <a onclick="simpleOpenWindow('elevatorSaleAction','${e.elevatorNo}');" class="link">${e.elevatorNo}</a>                                             
              </td>
              <td align="center">${e.elevatorType}</td>
              <td align="center">${e.floor}</td>
              <td align="center">${e.stage}</td>
              <td align="center">${e.door}</td>
              <td align="center">${e.high}</td>
              <td align="center">${e.elevatorParam}</td>
              <td align="center">${e.annualInspectionDate}</td>
              <td align="center">${e.salesContractNo}</td>
              <td align="center">${e.projectName}</td>
              <td align="center">${e.maintAddress}</td>
              <td align="center">${e.mainSdate}</td>
              <td align="center">${e.mainEdate}</td>
              <td align="center">
                <span class="show">${e.realityEdate}</span>
                <span class="hide">
                  <input type="text" name="realityEdate" value="${e.realityEdate}" class="Wdate" onfocus="pickEndDay('${maintContractBean.contractEdate}')" size="12"/>
                </span>
              </td>
            </tr>
          </logic:iterate>
        </logic:present>
      </tbody>   
    </table>
  </div>
  <br>
  
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td class="wordtd" nowrap="nowrap">合同总额:</td>
      <td width="30%">
        <bean:write name="maintContractBean" property="contractTotal"/>
      </td>          
      <td class="wordtd" nowrap="nowrap">其它费用:</td>
      <td width="30%">
        <bean:write name="maintContractBean" property="otherFee"/>
      </td>        
    </tr>
    <tr>
      <td class="wordtd" nowrap="nowrap">付款方式:</td>
      <td colspan="3">
        <bean:write name="maintContractBean" property="r4"/>
      </td> 
    </tr>
   	<tr>
    <td class="wordtd">
    	付款方式申请备注:
    </td>
    <td colspan="3">
    	<bean:write name="maintContractBean" property="paymentMethodRem"/>
    </td>
    </tr>
    <tr>
      <td class="wordtd" nowrap="nowrap">合同包含配件及服务:</td>
      <td colspan="3">     
        <bean:write name="maintContractBean" property="r5"/> 
      </td>                  
    </tr>  
    <tr>
    <td class="wordtd">
    	合同包含配件及服务备注:
    </td>
    <td colspan="3">
    	<bean:write  name="maintContractBean" property="contractContentRem"/>
    </td>
    </tr>
    <tr>
      <td class="wordtd" nowrap="nowrap">延保录入人:</td>
      <td width="30%">
        <bean:write name="username"/>
      </td>          
      <td class="wordtd" nowrap="nowrap">延保录入日期:</td>
      <td width="30%">
        <bean:write name="daytime"/>
      </td>        
    </tr>
	  <tr>
	    <td class="wordtd">备注:</td>
	    <td colspan="5">
	    	 <span class="show"><bean:write name="delayrem"/></span>
             <span class="hide">
                 <html:textarea property="rem" styleId="rem" rows="3" cols="100" styleClass="default_textarea"/><font color="red">*</font>
            </span>   
	    </td>                  
	  </tr> 
  </table>
 

  <script type="text/javascript"> 
	window.onload = function() {			
		setScrollTable('scrollBox','scrollTable',10); // 设置滚动表格
	}
  </script> 
</logic:present>