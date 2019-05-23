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
      <td width="20%"><bean:write name="companyA" property="companyName"/></td>
      <td class="wordtd">甲方单位地址:</td>
      <td width="20%"><bean:write name="companyA" property="address"/></td>
      <td nowrap="nowrap" class="wordtd">甲方法人:</td>
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
      <%-- td class="wordtd"><logic:present name="display">上一份合同号:</logic:present></td>
      <td>
      	<logic:present name="display">
      		<bean:write name="maintContractBean" property="histContractNo"/>
      	</logic:present>
      </td--%>      
    <td class="wordtd"><bean:message key="maintContract.attn"/>:</td>
    <td>
    	<bean:write name="maintContractBean" property="attn"/>
    </td>       
    </tr>
    <tr>
    <td class="wordtd">报价流水号:</td>
    <td>${maintContractBean.quoteBillNo}</td> 
    <td class="wordtd">报价签署方式:</td>
    <td>
    	<logic:equal name="maintContractBean" property="quoteSignWay" value="ZB">新签</logic:equal>
    	<logic:equal name="maintContractBean" property="quoteSignWay" value="XB">续签</logic:equal>
    	<logic:equal name="maintContractBean" property="quoteSignWay" value="BFXB">部分续签</logic:equal>
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
          <td class="wordtd_header" nowrap="nowrap">使用单位名称</td>
          <td class="wordtd_header" nowrap="nowrap">维保开始日期<logic:equal name="doType" value="renewal"><font color="red">*</font></logic:equal></td>
          <td class="wordtd_header" nowrap="nowrap">维保结束日期<logic:equal name="doType" value="renewal"><font color="red">*</font></logic:equal></td>
          <td class="wordtd_header" nowrap="nowrap">实际结束日期</td>
          <td class="wordtd_header" nowrap="nowrap">电梯性质</td>
          <td class="wordtd_header" nowrap="nowrap">是否发台量奖</td>
          <td class="wordtd_header" nowrap="nowrap">是否为别墅梯</td>
          <%-- <td class="wordtd_header" nowrap="nowrap">发货日期</td>
          <td class="wordtd_header" nowrap="nowrap">质检发证日期</td>
          <td class="wordtd_header" nowrap="nowrap">移交客户日期</td>
          <td class="wordtd_header" nowrap="nowrap">维保确认日期</td>  --%>    
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
              <logic:notEqual name="doType" value="renewal">
              	${e.signWay}
              </logic:notEqual>
              <logic:equal name="doType" value="renewal">
              	${e.r1}
              	<input type="hidden" name="" value="${e.signWay}"/>
              </logic:equal>
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
              <td align="center">
              	<logic:notEqual name="doType" value="aidate">
              		${e.annualInspectionDate}
              	</logic:notEqual>
              	<logic:equal name="doType" value="aidate">
              		<input type="text" name="annualInspectionDate" id="annualInspectionDate" value="${e.annualInspectionDate}" size="12" class="Wdate" onfocus="WdatePicker({readOnly:true})" onpropertychange="setDateChanged(this)" />
              	</logic:equal>
              </td>
              <td align="center">${e.salesContractNo}</td>
              <td align="center">${e.projectName}</td>
              <td align="center">${e.maintAddress}</td>
              <td align="center">
                <span class="renewal show">${e.mainSdate}</span>
                <span class="renewal hide"><input type="text" name="mainSdate" class="Wdate" onfocus="pickStartDay('mainEdate')" size="12"/></span>
              </td>
              <td align="center">
                <span class="renewal show">${e.mainEdate}</span>
                <span class="renewal hide"><input type="text" name="mainEdate" class="Wdate" onfocus="pickEndDay('mainSdate')" size="12"/></span>
              </td>
              <td align="center">${e.realityEdate}</td>
              <td align="center">${e.elevatorNature}</td>
              <td align="center">${e.r4}</td>
              <td align="center">${e.r5}</td>
              <%-- <td align="center">${e.shippedDate}</td>
              <td align="center">${e.issueDate}</td>
              <td align="center">${e.tranCustDate}</td>
              <td align="center">${e.mainConfirmDate}</td> --%>
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
        <bean:write name="maintContractBean" property="r4"/>
      </td> 
    </tr>
  <tr>
    <td class="wordtd">付款方式备注:</td>
    <td colspan="5">
      	<bean:write name="maintContractBean" property="paymentMethodRem"/>
    </td> 
  </tr>
    <tr>
      <td class="wordtd">合同包含配件及服务:</td>
      <td colspan="5">     
        <bean:write name="maintContractBean" property="r5"/> 
      </td>                  
    </tr>  
    <tr>
      <td class="wordtd">合同包含配件及服务备注:</td>
      <td colspan="5">     
        <bean:write name="maintContractBean" property="contractContentRem"/> 
      </td>                  
    </tr> 
    <tr>
      <td class="wordtd">备注:</td>
      <td colspan="5">     
        <bean:write name="maintContractBean" property="rem"/> 
      </td>                  
    </tr> 
  </table>
 
  <br>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td height="23" colspan="6">&nbsp;<b>审核信息</td>
    </tr>
    <tr>
      <td class="wordtd" nowrap="nowrap">审核人:</td>
      <td width="20%">
        ${maintContractBean.auditOperid}
      </td>     
      <td class="wordtd" nowrap="nowrap">审核状态:</td>
      <td width="20%">
        ${maintContractBean.auditStatus == 'Y' ? '已审核' : '未审核'}
      </td>         
      <td class="wordtd" nowrap="nowrap">审核时间:</td>
      <td width="20%">
        ${maintContractBean.auditDate}
      </td>        
    </tr>
    <tr>
      <td class="wordtd" nowrap="nowrap">审核意见:</td>
      <td colspan="5">
	          <logic:notPresent name="auditdisplay">
	          	${maintContractBean.auditRem}
	          </logic:notPresent>
              <logic:present name="auditdisplay">
               <html:textarea name="maintContractBean" property="auditRem" rows="3" cols="100" styleClass="default_textarea"/>
             </logic:present>
      </td> 
    </tr> 
  </table>

  <br>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td height="23" colspan="6">&nbsp;<b>任务下达信息</td>
    </tr>
    <tr>
      <td class="wordtd">任务下达人:</td>
      <td width="20%">
        ${maintContractBean.taskUserId}
      </td>     
      <td class="wordtd">任务下达标志:</td>
      <td width="20%">
        ${maintContractBean.taskSubFlag == 'Y' ? '已下达' : ''}
        ${maintContractBean.taskSubFlag == 'N' ? '未下达' : ''}
        ${maintContractBean.taskSubFlag == 'R' ? '已退回' : ''}
      </td>         
      <td class="wordtd">任务下达日期:</td>
      <td width="20%">
        ${maintContractBean.taskSubDate}
      </td>        
    </tr>
    <tr>
      <td class="wordtd">任务下达备注:</td>
      <td colspan="5">
        <span class="assign show">${maintContractBean.taskRem}</span>
        <span class="assign hide">
          <html:textarea name="maintContractBean" property="taskRem" rows="3" cols="100" styleClass="default_textarea"/>
        </span>
      </td> 
    </tr> 
  </table>

  
  <script type="text/javascript"> 
	$(document).ready(function() {			
		setScrollTable('scrollBox','scrollTable',10); // 设置滚动表格
	})
	//setTopRowDateInputsPropertychange(scrollTable); //动态表格第一行日期控件添加propertychange事件    
  </script> 
</logic:present>