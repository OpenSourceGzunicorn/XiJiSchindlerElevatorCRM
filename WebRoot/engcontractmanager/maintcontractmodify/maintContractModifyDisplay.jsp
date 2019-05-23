<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<br>
<head>
  <title>XJSCRM</title>
</head>
<body>

  <html:errors/>
  <html:form action="/maintContractModifyAction.do?method=toAuditRecord">

    <html:hidden name="maintContractBean" property="billNo"/> 

<logic:present name="maintContractBean">
  <html:hidden name="maintContractBean" property="billNo"/>
  <input type="hidden" name="isupdate" value="${isupdate }"/>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td height="23" colspan="6">&nbsp;<b>客户信息</td>
    </tr>
    <tr>
      <td class="wordtd">甲方单位名称:</td>
      <td width="20%" colspan="5">
      	  <%-- bean:write name="companyA" property="companyName"/--%>
      	  <input type="text" name="companyName" id="companyName" value="${companyA.companyName}" readonly="true" size="50"/>
	      <input type="button" value=".." onclick="openWindowAndReturnValue3('searchCustomerAction','toSearchRecord','cusNature=JF','')" class="default_input"/><font color="red">*</font>
	      <html:hidden name="maintContractBean" property="companyId" styleId="companyId"/>
      
      </td>
      </tr>
      <tr>
      <td class="wordtd">甲方单位地址:</td>
      <td width="20%"><bean:write name="companyA" property="address"/></td>
      <td nowrap="nowrap" class="wordtd">甲方法人:</td>
      <td width="20%"><bean:write name="companyA" property="legalPerson"/></td>  
      <td nowrap="nowrap" class="wordtd">&nbsp;</td>
      <td width="20%">&nbsp;</td>   
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
      	<logic:notEqual name="isupdate" value="Y">
	        <logic:match name="maintContractBean" property="mainMode" value="FREE">免费</logic:match>
	        <logic:match name="maintContractBean" property="mainMode" value="PAID">收费</logic:match>
	        <html:hidden name="maintContractBean" property="mainMode" write="false"/>
        </logic:notEqual>
        <logic:equal name="isupdate" value="Y">
           	<html:select name="maintContractBean" property="mainMode" styleId="maintContractBean">
	        	<html:option value="PAID">收费</html:option>
	        	<html:option value="FREE">免费</html:option>
	        </html:select>
        </logic:equal>
      </td>         
    </tr>  
    <tr>
      <td class="wordtd"><bean:message key="maintContract.contractPeriod"/>(月):</td>
      <td>
           <html:text name="maintContractBean" property="contractPeriod" readonly="true" styleClass="default_input_noborder"/>          
    </td>          
      <td class="wordtd"><bean:message key="maintContract.contractSdate"/>:</td>
      <td>
      	<logic:notEqual name="isupdate" value="Y">
            ${maintContractBean.contractSdate }
            <input type="hidden" name="contractSdate" id="contractSdate" value="${maintContractBean.contractSdate }"/>
        </logic:notEqual>
        <logic:equal name="isupdate" value="Y">
      	    <html:text name="maintContractBean" property="contractSdate" styleId="contractSdate" size="12" styleClass="Wdate" onfocus="pickStartDay('contractEdate')" /><font color="red">*</font>  
    	</logic:equal>
      </td>
      <td class="wordtd"><bean:message key="maintContract.contractEdate"/>:</td>
      <td>
      	<logic:notEqual name="isupdate" value="Y">
            ${maintContractBean.contractEdate }
            <input type="hidden" name="contractEdate" id="contractEdate" value="${maintContractBean.contractEdate }"/>
        </logic:notEqual>
        <logic:equal name="isupdate" value="Y">
      	 	<html:text name="maintContractBean" property="contractEdate" styleId="contractEdate" size="12" styleClass="Wdate" onfocus="pickEndDay('contractSdate')" /><font color="red">*</font>   
    	</logic:equal>
      </td>         
    </tr>        
    <tr>
      <td class="wordtd"><bean:message key="maintContract.maintDivision"/>:</td>
      <td>
      	<%--bean:write name="maintDivisionName" /--%>
      	<html:select name="maintContractBean" property="maintDivision"  styleId="maintDivision"  onchange="EvenmoreUP(this,'maintStation')">
          <html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
        </html:select><font color="red">*</font>
      </td>
      <td class="wordtd">所属维保站:</td>
      <td>
        <%-- bean:write name="maintStationName" /   onchange="setAssignedMainStation(this.value);"--%>
        <html:select name="maintContractBean" property="maintStation"  styleId="maintStation">
        	<html:option value="">请选择</html:option>
          <html:options collection="maintStationList" property="storageid" labelProperty="storagename"/>
        </html:select><font color="red">*</font>
      </td>    
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
  <tr>
  <td nowrap="nowrap" class="wordtd">合同状态:</td>
	<td colspan="5">
		<html:select name="maintContractBean" property="contractStatus">
       		<html:options collection="contractStatusList" property="id.pullid" labelProperty="pullname"/>
     	</html:select>
     </td>
  </tr>
  </table>
  <br>
  
  <div style="border:solid #999999 1px;border-bottom:none;padding-top: 4;padding-bottom: 4;background:#ffffff;">
  <logic:equal name="isupdate" value="Y">
  &nbsp;&nbsp;<input type="button" value=" - " onclick="deleteRow('scrollTable')" class="default_input">       
  </logic:equal>
   <b>&nbsp;合同明细</b>
  </div>
  <div id="scrollBox" style="overflow:scroll;">
  	<table id="scrollTable" width="1600px" border="0" cellpadding="0" cellspacing="0" class="tb">
      <thead>
        <tr id="titleRow">
          <logic:equal name="isupdate" value="Y">
          	<td class="wordtd_header" ><input type="checkbox" name="cbAll" onclick="checkTableAll('scrollTable',this)"/></td>
          </logic:equal>
          <td class="wordtd_header" nowrap="nowrap">序号</td>          
          <td class="wordtd_header" nowrap="nowrap">下达维保站<font color="red">*</font></td>
          <td class="wordtd_header" nowrap="nowrap">签署方式<font color="red">*</font></td>
          <td class="wordtd_header" nowrap="nowrap">电梯编号<font color="red">*</font></td>
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
          <td class="wordtd_header" nowrap="nowrap">维保结束日期<logic:equal name="isupdate" value="Y"><font color="red">*</font></logic:equal></td>
          <td class="wordtd_header" nowrap="nowrap">是否退保</td>   
          <td class="wordtd_header" nowrap="nowrap">电梯性质</td> 
          <td class="wordtd_header" nowrap="nowrap">是否已回验收合格证</td>
          <td class="wordtd_header" nowrap="nowrap">是否发台量奖</td>
          <td class="wordtd_header" nowrap="nowrap">是否为别墅梯</td>
        </tr>
      </thead>
      <tfoot>
        <tr height="15px"><td colspan="21"></td></tr>
      </tfoot>
      <tbody>
        <logic:present name="maintContractDetailList">
          <logic:iterate id="e" name="maintContractDetailList" indexId="i">
            <tr>
            	<logic:equal name="isupdate" value="Y">
        			<td align="center"><input type="checkbox" onclick="cancelCheckAll('scrollTable','cbAll')"/></td>
        		</logic:equal>
              <td align="center">${i+1}
                <html:hidden name="e" property="rowid" />
                <%-- html:hidden name="e" property="elevatorNo" /--%>                                
              </td>
             <td align="center" width="1%">
                  <select name="assignedMainStation" id="assignedMainStation">
	              		<option value="">请选择</option>
			           	<logic:iterate id="s" name="maintStationList" >
				           <logic:equal name="s" property="storageid" value="${e.assignedMainStation}">
				           		<option value="${s.storageid}" selected="selected">${s.storagename}</option>
				           </logic:equal>
				           <logic:notEqual name="s" property="storageid" value="${e.assignedMainStation}">
				              <option value="${s.storageid}">${s.storagename}</option>
			              </logic:notEqual>
		            	</logic:iterate>
		        	</select>
              </td>
              <td align="center">
              	<logic:notEqual name="isupdate" value="Y">
              		${e.r1}
              	</logic:notEqual>
              	<logic:equal name="isupdate" value="Y">
              		<select name="signWay" property="signWay" >
			           	<logic:iterate id="s" name="signWayList" >
				           <logic:equal name="s" property="id.pullid" value="${e.signWay}">
				           		<option value="${s.id.pullid}" selected="selected">${s.pullname}</option>
				           </logic:equal>
				           <logic:notEqual name="s" property="id.pullid" value="${e.signWay}">
				              <option value="${s.id.pullid}">${s.pullname}</option>
			              </logic:notEqual>
		            	</logic:iterate>
		        	</select>
              	</logic:equal>
              </td> 
              <td align="center">
                <input type="text" name="elevatorNo" value="${e.elevatorNo}" class="default_input"/>
	              <%-- a onclick="simpleOpenWindow('elevatorSaleAction','${e.elevatorNo}');" class="link">${e.elevatorNo}</a--%>                                             
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
              <td align="center">
	              <logic:notEqual name="isupdate" value="Y">
	              		${e.mainEdate}
	              		<input type="hidden" name="mainEdate" value="${e.mainEdate}"/>
	              </logic:notEqual>
	              <logic:equal name="isupdate" value="Y">
	             	<input type="text" name="mainEdate" size="13" class="Wdate" onfocus="WdatePicker({readOnly:true,isShowClear:true});" value="${e.mainEdate }"/>
	              </logic:equal>
              </td>
              <td align="center">
	              <logic:notEqual name="isupdate" value="Y">
	              		<logic:notEqual name="e" property="isSurrender" value="Y">否</logic:notEqual>
	              		<logic:equal name="e" property="isSurrender" value="Y">是</logic:equal>
	              </logic:notEqual>
	              <logic:equal name="isupdate" value="Y">
	              	<select name="isSurrender">
	              		<option value='N' <logic:notEqual name="e" property="isSurrender" value="Y">selected="selected"</logic:notEqual> >否</option>
	              		<option value='Y' <logic:equal name="e" property="isSurrender" value="Y">selected="selected"</logic:equal> >是</option>
	              	</select>
	              	</logic:equal>
              </td>
              <td align="center">
              <logic:notEqual name="isupdate" value="Y">
              	${e.elevatorNature}
              </logic:notEqual>
              <logic:equal name="isupdate" value="Y">
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
	        	</logic:equal>
              </td>
              <td align="center">
	              <logic:notEqual name="isupdate" value="Y">
	              		<logic:notEqual name="e" property="isCertificate" value="Y">否</logic:notEqual>
	              		<logic:equal name="e" property="isCertificate" value="Y">是</logic:equal>
	              </logic:notEqual>
	              <logic:equal name="isupdate" value="Y">
	              	<select name="isCertificate">
	              		<option value='N' <logic:notEqual name="e" property="isCertificate" value="Y">selected="selected"</logic:notEqual> >否</option>
	              		<option value='Y' <logic:equal name="e" property="isCertificate" value="Y">selected="selected"</logic:equal> >是</option>
	              	</select>
	              	</logic:equal>
              </td>
              <td align="center">
	            <select name="r4" property="r4" >
		           	<option value="是" <logic:equal name="e" property="r4" value="是">selected="selected"</logic:equal>>是</option>
             		<option value="否" <logic:equal name="e" property="r4" value="否">selected="selected"</logic:equal>>否</option>
		        </select>
	        </td>
	        <td align="center">
	            <select name="r5" property="r5" >
	            	<option value="否" <logic:equal name="e" property="r5" value="否">selected="selected"</logic:equal>>否</option>
		           	<option value="是" <logic:equal name="e" property="r5" value="是">selected="selected"</logic:equal>>是</option>
             		
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
          <html:text name="maintContractBean" property="contractTotal" styleClass="default_input" styleId="contractTotal" onchange="checkthisvalue(this);"/><font color="red">*</font>
      </td>          
      <td class="wordtd" nowrap="nowrap">其它费用:</td>
      <td width="20%">
          <html:text name="maintContractBean" property="otherFee" styleClass="default_input" styleId="otherFee" onchange="checkthisvalue(this);"/>
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
      		<html:textarea name="maintContractBean" property="rem" rows="3" cols="100" styleClass="default_textarea"/>
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
      <td colspan="5">${maintContractBean.auditRem}</td> 
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
      <td colspan="5">${maintContractBean.taskRem}</td> 
    </tr> 
  </table>

  
  <script type="text/javascript"> 
	$(document).ready(function() {			
		setScrollTable('scrollBox','scrollTable',10); // 设置滚动表格
	})
	
	  $("#contractSdate,#contractEdate").bind("propertychange",function(){setContractPeriod();}); // 计算合同有效期  
  </script> 
</logic:present>
    
    
    
  </html:form>
</body>