<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

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
      <td width="20%">
	      <input type="text" name="companyName" id="companyName" value="${companyA.companyName}" readonly="true" size="23"/>
	      <input type="button" value=".." onclick="openWindowAndReturnValue3('searchCustomerAction','toSearchRecord','&cusNature=JF','')" class="default_input"/><font color="red">*</font>
	      <html:hidden name="maintContractBean" property="companyId" styleId="companyId"/>
	    </td>
      <td class="wordtd">甲方单位地址:</td>
      <td width="20%" id="address">${companyA.address}</td>
      <td nowrap="nowrap" class="wordtd">甲方法人:</td>
      <td width="20%" id="legalPerson">${companyA.legalPerson}</td> 
    </tr>
      <tr>    
    <td class="wordtd">甲方委托人:</td>
    <td id="client">${companyA.client}</td>
    <td class="wordtd">甲方联系人:</td>
    <td id="contacts">${companyA.contacts}</td>   
    <td class="wordtd">甲方联系电话:</td>
    <td id="contactPhone">${companyA.contactPhone}</td>          
  </tr>     
  <tr>
    <td class="wordtd">甲方传真:</td>
    <td id="fax">${companyA.fax}</td>   
    <td class="wordtd">甲方邮编:</td>
    <td id="postCode">${companyA.postCode}</td>          
    <td class="wordtd">地址、电话:</td>
    <td id="accountHolder">${companyA.accountHolder}</td> 
  </tr>
  <tr>      
    <td class="wordtd">甲方银行账号:</td>
    <td id="account">${companyA.account}</td>          
    <td class="wordtd">甲方开户银行:</td>
    <td id="bank">${companyA.bank}</td>   
    <td class="wordtd">纳税人识别号:</td>
    <td id="taxId">${companyA.taxId}</td>          
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
      <td class="wordtd">乙方传真:</td>
      <td><bean:write name="companyB" property="fax"/></td>   
      <td class="wordtd">乙方邮编:</td>
      <td><bean:write name="companyB" property="postCode"/></td>          
      <td class="wordtd">乙方户名:</td>
      <td><bean:write name="companyB" property="accountHolder"/></td>   
    </tr>
    <tr>
      <td class="wordtd">乙方银行账号:</td>
      <td><bean:write name="companyB" property="account"/></td>
      <td class="wordtd"></td>
      <td></td>   
      <td class="wordtd"></td>
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
      <td class="wordtd"><bean:message key="maintContract.contractPeriod"/>（月）:</td>
      <td>
        <span class="renewal show"><bean:write name="maintContractBean" property="contractPeriod"/></span>  
        <span class="renewal hide"><input type="text" name="contractPeriod" class="default_input_noborder"></span>         
      </td>          
      <td class="wordtd"><bean:message key="maintContract.contractSdate"/>:</td>
      <td>
        <span class="renewal show"><bean:write name="maintContractBean" property="contractSdate"/></span>
        <span class="renewal hide"><input type="text" name="contractSdate" class="Wdate" onpropertychange="setContractPeriod();" onfocus="pickStartDay('contractEdate')" size="12"/><font color="red">*</font></span>       
      </td>
      <td class="wordtd"><bean:message key="maintContract.contractEdate"/>:</td>
      <td>
        <span class="renewal show"><bean:write name="maintContractBean" property="contractEdate"/></span>
        <span class="renewal hide"><input type="text" name="contractEdate" class="Wdate" onpropertychange="setContractPeriod();" onfocus="pickEndDay('contractSdate')" size="12"/><font color="red">*</font></span>
      </td>         
    </tr>        
    <tr>
      <td class="wordtd"><bean:message key="maintContract.maintDivision"/>:</td>
      <td>
      	<bean:write name="maintDivisionName" />
      </td>
      <td class="wordtd">所属维保站:</td>
      <td>
        <bean:write name="maintStationName" />
      </td>   
          <td class="wordtd"><bean:message key="maintContract.attn"/>:</td>
	    <td>
	    	<bean:write name="maintContractBean" property="attn"/>
	    </td>  
      </td>       
    </tr>
    <tr>
    <td class="wordtd">报价流水号:</td>
    <td>&nbsp;</td>
    <td class="wordtd">报价签署方式:</td>
    <td>&nbsp;
	</td>
    <td class="wordtd">录入日期:</td>
    <td>
    	<bean:write name="maintContractBean" property="operDate"/>
    </td>       
  </tr>     
  </table>
  <br>
  
  <div style="border:solid #999999 1px;border-bottom:none;padding-top: 4;padding-bottom: 4;background:#ffffff;">        
   <b>&nbsp;合同明细</b>
  </div>
  <div id="scrollBox" style="overflow:scroll;">
    <table id="scrollTable" width="1650px" border="0" cellpadding="0" cellspacing="0" class="tb">
      <thead>
        <tr id="titleRow">
          <td class="wordtd_header" nowrap="nowrap">序号</td>          
          <td class="wordtd_header" nowrap="nowrap">下达维保站<logic:equal name="doType" value="assign"><font color="red">*</font></logic:equal></td>
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
          <td class="wordtd_header" nowrap="nowrap">使用单位名称<logic:equal name="doType" value="renewal"><font color="red">*</font></logic:equal></td>
          <td class="wordtd_header" nowrap="nowrap">维保开始日期<logic:equal name="doType" value="renewal"><font color="red">*</font></logic:equal></td>
          <td class="wordtd_header" nowrap="nowrap">维保结束日期<logic:equal name="doType" value="renewal"><font color="red">*</font></logic:equal></td>
          <td class="wordtd_header" nowrap="nowrap">实际结束日期</td>
          <td class="wordtd_header" nowrap="nowrap">电梯性质<font color="red">*</font></td>
        </tr>
      </thead>
      <tfoot>
        <tr height="15px"><td colspan="20"></td></tr>
      </tfoot>
      <tbody>
        <logic:present name="maintContractDetailList">
          <logic:iterate id="e" name="maintContractDetailList" indexId="i">
            <tr>
              <td align="center">${i+1}
                <html:hidden name="e" property="rowid" />
                <html:hidden name="e" property="elevatorNo" />                                
              </td>
              <td align="center" width="1%">
                       
                <logic:notEqual name="doType" value="assign">             
                  <logic:iterate id="ms" name="maintStationList">
                    ${e.assignedMainStation == ms.storageid ? ms.storagename : ''}                
                  </logic:iterate>
                </logic:notEqual> 
                
                <logic:equal name="doType" value="assign">
                
                  <logic:equal name="e" property="assignedSignFlag" value="Y">
                  	<html:hidden name="e" property="assignedMainStation" /> 
                    <logic:iterate id="ms" name="maintStationList">
                      ${e.assignedMainStation == ms.storageid ? ms.storagename : ''}                                        
                    </logic:iterate>
                  </logic:equal>
                 
                  <logic:notEqual name="e" property="assignedSignFlag" value="Y">
                    <html:select name="e" property="assignedMainStation">
                      <html:option value="">请选择</html:option>
                      <html:options collection="maintStationList" property="storageid" labelProperty="storagename" />                                 
                    </html:select>  
                  </logic:notEqual>
                  
                </logic:equal>
                                  
              </td>
              <td align="center">
              	${e.r1}
              	<input type="hidden" name="signWay" value="${e.signWay}"/>
              </td> 
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
              <td align="center"><input type="text" name="maintAddress" value="${e.maintAddress}"  size="40" /></td>        
              <td align="center">
                <span class="renewal show">${e.mainSdate}</span>
                <span class="renewal hide"><input type="text" name="mainSdate" class="Wdate" onfocus="pickStartDay('mainEdate')" size="12"/></span>
              </td>
              <td align="center">
                <span class="renewal show">${e.mainEdate}</span>
                <span class="renewal hide"><input type="text" name="mainEdate" class="Wdate" onfocus="pickEndDay('mainSdate')" size="12"/></span>
              </td>
              <td align="center">${e.realityEdate}</td>
              <%-- <td align="center">${e.shippedDate}</td>
              <td align="center">${e.issueDate}</td>
              <td align="center">${e.tranCustDate}</td>
              <td align="center">${e.mainConfirmDate}</td> --%>
              <td align="center">
	            <select name="elevatorNature" property="elevatorNature" >
		           <logic:iterate id="s" name="elevatorNatureList" >
			           <logic:equal name="s" property="id.pullid" value="${e.elevatorNature}">
			           		<option value="${s.id.pullid}" selected="selected">${s.pullname}</option>
			           </logic:equal>
			           <logic:notEqual name="s" property="id.pullid" value="${e.elevatorNature}">
			              <option value="${s.id.pullid}">${s.pullname}</option>
		              </logic:notEqual>
	            </logic:iterate>
	        </select>
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
      <td width="20%">
        <bean:write name="maintContractBean" property="contractTotal"/>
      </td>          
      <td class="wordtd" nowrap="nowrap">其它费用:</td>
      <td width="20%">
        <bean:write name="maintContractBean" property="otherFee"/>
      </td> 
      <td class="wordtd" nowrap="nowrap">&nbsp;</td>
      <td width="20%">&nbsp;</td>        
    </tr>
    <tr>
      <td class="wordtd" nowrap="nowrap">付款方式:</td>
      <td colspan="5">
        <html:hidden name="maintContractBean" property="paymentMethod" />
      	<bean:write name="maintContractBean" property="r4"/> 
      </td> 
    </tr>
  <tr>
    <td class="wordtd">付款方式备注:</td>
    <td colspan="5">
    	<html:hidden name="maintContractBean" property="paymentMethodRem" />
      	<bean:write name="maintContractBean" property="paymentMethodRem"/>
    </td> 
  </tr>
    <tr>
      <td class="wordtd">合同包含配件及服务:</td>
      <td colspan="5">
       	<html:hidden name="maintContractBean" property="contractTerms"/>
      	<bean:write name="maintContractBean" property="r5"/>   
      </td>                  
    </tr>  
    <tr>
      <td class="wordtd">合同包含配件及服务备注:</td>
      <td colspan="5">
       	<html:hidden name="maintContractBean" property="contractContentRem"/>
      	<bean:write name="maintContractBean" property="contractContentRem"/>
      </td>                  
    </tr> 
    <tr>
      <td class="wordtd">备注:</td>
      <td colspan="5">     
        <html:textarea name="maintContractBean" property="rem" rows="3" cols="100" styleClass="default_textarea"/>  
      </td>                  
    </tr> 
  </table>

  
  <script type="text/javascript"> 
	$(document).ready(function() {			
		setScrollTable('scrollBox','scrollTable',10); // 设置滚动表格
	})
  </script> 
</logic:present>