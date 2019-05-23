<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"";
%>

<html:form action="/maintenanceWorkPlanCompletionAction.do?method=toUpdate2Record">
<html:errors/>
<html:hidden property="isreturn"/>
<logic:present name="maintenanceWorkPlanDetailBean">
    <table  width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td nowrap="nowrap" class="wordtd">保养站:</td>
      <td  width="30%">
       <bean:write name="mapBean"  property="storagename"/>
       <html:hidden name="maintenanceWorkPlanDetailBean" property="numno"/>
      </td>          
      <td nowrap="nowrap" class="wordtd">维保工:</td>
      <td  width="30%">
      <bean:write name="mapBean"  property="username"/>
      </td>        
    </tr>  
    <tr>
      <td class="wordtd">合同号:</td>
      <td>
        <a href="<html:rewrite page='/maintContractAction.do'/>?method=toDisplayRecord&typejsp=Yes&id=${mapBean.billno}" target="_blnk"><bean:write name="mapBean"  property="maintContractNo"/></a>
      </td>          
      <td class="wordtd">联系电话:</td>
      <td>
        <span class="renewal show"><bean:write name="mapBean" property="phone"/></span>      
      </td>
        
    </tr>        
    <tr>
      <td class="wordtd">项目名称:</td>
      <td>
        <bean:write name="mapBean" property="projectName"/>
      </td> 
      <td class="wordtd">保养日期:</td>
      <td><bean:write name="maintenanceWorkPlanDetailBean" property="maintDate" />
      </td>
    </tr>
    <tr>
      <td class="wordtd">项目地址:</td>
      <td >
        <bean:write name="mapBean" property="maintAddress"/>
      </td> 
      
      <td class="wordtd">保养人员</td>
      <td><bean:write name="mapBean" property="r5name"/></td> 
     </tr>       
  </table>
  <br>
   <table  width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
   <td nowrap="nowrap" class="wordtd">接收日期:</td>
   <td width="30%"><bean:write name="maintenanceWorkPlanDetailBean" property="receivingTime"/></td>
   <td nowrap="nowrap" class="wordtd">是否转派:</td>
   <td width="30%">
    <logic:notEmpty name="maintenanceWorkPlanDetailBean" property="isTransfer">
   <logic:match name="maintenanceWorkPlanDetailBean" property="isTransfer"  value="Y">是</logic:match>
   <logic:match name="maintenanceWorkPlanDetailBean" property="isTransfer"  value="N">否</logic:match>  
   </logic:notEmpty>
   <logic:empty name="maintenanceWorkPlanDetailBean" property="isTransfer" >
                    否
       </logic:empty>
   </td>
   </tr>
    <tr>
   <td nowrap="nowrap" class="wordtd">被转派人:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="receivingPerson" /></td>
   <td nowrap="nowrap" class="wordtd">被转派人电话:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="receivingPhone"/></td>
   </tr>
  </table>
  <br>
  <div style="border:solid #999999 1px;border-bottom:none;padding-top: 4;padding-bottom: 4;background:#ffffff;">        
	   <b>&nbsp;保养明细</b>
	  </div>
   <table  width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
   <td nowrap="nowrap" class="wordtd">保养单号:</td>
   <td width="30%"><bean:write name="maintenanceWorkPlanDetailBean" property="singleno"/></td>
   <td nowrap="nowrap" class="wordtd">电梯编号:</td>
   <td width="30%">
   <a onclick="simpleOpenWindow('elevatorSaleAction','${mapBean.elevatorNo}');" class="link"><bean:write name="mapBean" property="elevatorNo"/></a></td>
   </tr>
   <tr>
   <td class="wordtd">电梯类型:</td>
   <td>
   		<logic:match name="mapBean" property="elevatorType" value="T" >
		直梯
       </logic:match>
       <logic:match name="mapBean" property="elevatorType" value="F" >
		扶梯
       </logic:match>
   </td>
   <td  class="wordtd">保养类型:</td>
   <td >
    <logic:match name="maintenanceWorkPlanDetailBean" property="maintType" value="halfMonth" >
		半月保养
       </logic:match>
       <logic:match name="maintenanceWorkPlanDetailBean" property="maintType" value="quarter" >
		季度保养 
       </logic:match>
       <logic:match name="maintenanceWorkPlanDetailBean" property="maintType" value="halfYear" >
		半年保养
       </logic:match>
       <logic:match name="maintenanceWorkPlanDetailBean" property="maintType" value="yearDegree" >
		年度保养
       </logic:match>
   </td>
   </tr>
   
   <tr>
   <td class="wordtd">准保养时工(分钟):</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean"  property="maintDateTime" /></td>
    <td class="wordtd">上次保养日期:</td>
   <td><bean:write name="mapBean"  property="sMaintEndTime" /></td>
   </tr>
   <tr>
   <td class="wordtd">保养开始时间:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="maintStartTime"/></td>
   <td class="wordtd">保养开始地址:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="maintStartAddres"/></td>
   </tr>
   <tr>
   <td class="wordtd">保养开始距离(米):</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="startDistance"/></td>
   <td class="wordtd">&nbsp;</td>
   <td>&nbsp;</td>
   </tr>
   <tr>
   <td class="wordtd">保养结束时间:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="maintEndTime"/></td>
   <td class="wordtd">保养结束地址:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="maintEndAddres"/></td>
   </tr>
   <tr>
   <td class="wordtd">保养结束距离(米):</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="endDistance"/></td>
   <td class="wordtd">&nbsp;</td>
   <td>&nbsp;</td>
   </tr>
   
      <tr>
   <td class="wordtd">暂停时间:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="stopTime"/></td>
   <td class="wordtd">暂停地址:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="stopAddres"/></td>
   </tr>
   <tr>
   <td class="wordtd">暂停距离(米):</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="stopDistance"/></td>
   <td class="wordtd">&nbsp;</td>
   <td>&nbsp;</td>
   </tr>
   <tr>
   <td class="wordtd">复工时间:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="restartTime"/></td>
   <td class="wordtd">复工地址:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="restartAddres"/></td>
   </tr>
   <tr>
   <td class="wordtd">复工距离(米):</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="restartDistance"/></td>
   <td class="wordtd">&nbsp;</td>
   <td>&nbsp;</td>
   </tr>
   
   <tr>
   <td class="wordtd">所用时长(分钟):</td>
   <td>
   		<bean:write name="maintenanceWorkPlanDetailBean" property="usedDuration"/>
   </td>
   <td class="wordtd">所用时长评分:</td>
	<td>
		<bean:write name="maintenanceWorkPlanDetailBean" property="dateScore"/>
	</td>
   </tr>
   <tr>
   <td class="wordtd">距离评分:</td>
   <td>
   		<bean:write name="maintenanceWorkPlanDetailBean" property="distanceScore"/>
   	</td>
   <td class="wordtd">保养得分:</td>
	<td>
		<bean:write name="maintenanceWorkPlanDetailBean" property="maintScore"/>
	</td>
   </tr>

	   <tr>
		  <td class="wordtd">保养备注</td>
	      <td colspan="3"><bean:write name="mapBean" property="byrem"/></td> 
      </tr>
   </table>
  
   <br/>
	<div style="border:solid #999999 1px;border-bottom:none;padding-top: 4;padding-bottom: 4;background:#ffffff;">        
	   <b>&nbsp;保养项目明细</b>
	  </div>
     <table  width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
        <tr>
	        <td class="wordtd_header" nowrap="nowrap" >序号</td>
	        <td class="wordtd_header" nowrap="nowrap">维保项目</td>
	        <td class="wordtd_header" nowrap="nowrap">维保基本要求</td>
	        <td class="wordtd_header" nowrap="nowrap">保养情况</td>
        </tr>
         <logic:present name="workinfoList">
        <logic:iterate id="ele" name="workinfoList">
        <tr>
        	<td align="center">${ele.orderby }</td>
        	<td>${ele.maintItem }</td>
        	<td>${ele.maintContents }</td>
        	<td align="center">
        		<logic:equal name="ele" property="isMaintain" value="Y">√</logic:equal>
        		<logic:equal name="ele" property="isMaintain" value="N">－</logic:equal>
        	</td>
        </tr>
        </logic:iterate>
        </logic:present>
     </table>
     <br/>
	<table width="100%" border="0" cellpadding="0" cellspacing="0"  class="tb">
      <tr>
        <td class="wordtd">客户签名:</td>
        <td width="72%" class="inputtd">
        	<logic:notEmpty name="maintenanceWorkPlanDetailBean" property="customerSignature">
				<img src="<%=basePath%>/maintenanceWorkPlanCompletionAction.do?method=toDownloadFileRecord&customerSignature=${maintenanceWorkPlanDetailBean.customerSignature}"
					width="${CSwidth}" height="${CSheight}"  id="${maintenanceWorkPlanDetailBean.customerSignature}_1"> 	
			</logic:notEmpty>
			<logic:notEmpty name="maintenanceWorkPlanDetailBean" property="customerImage">
				<img src="<%=basePath%>/maintenanceWorkPlanCompletionAction.do?method=toDownloadFileRecord&customerImage=${maintenanceWorkPlanDetailBean.customerImage}"
					width="${CIwidth}" height="${CIheight}" id="${maintenanceWorkPlanDetailBean.customerImage}_2">
			</logic:notEmpty>
        </td>
      </tr>
    </table>
  
  <br/>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
   <td nowrap="nowrap" class="wordtd" width="30%">备注:</td>
   <td nowrap="nowrap" colspan="3" width="75%">
  	 <bean:write name="maintenanceWorkPlanDetailBean" property="rem"/>
   </td>
   </tr>
   <tr>
   <td nowrap="nowrap" class="wordtd">审核人:</td>
   <td nowrap="nowrap" width="30%"><bean:write name="maintenanceWorkPlanDetailBean" property="byAuditOperid"/></td>
   <td nowrap="nowrap" class="wordtd">审核日期：</td>
   <td nowrap="nowrap" width="30%"><bean:write name="maintenanceWorkPlanDetailBean" property="byAuditDate"/></td>
   </tr>
   </table>
   <br/>
   <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
   <td nowrap="nowrap" class="wordtd">配件更换情况:</td>
   <td nowrap="nowrap" colspan="3">
  	<html:textarea name="maintenanceWorkPlanDetailBean" rows="3" cols="120" property="fittingsReplace"></html:textarea>
 </td>
   </tr>
   <tr>
   <td nowrap="nowrap" class="wordtd" >是否有单据:</td>
   <td nowrap="nowrap" colspan="3"> 
   	<html:radio name="maintenanceWorkPlanDetailBean" styleId="isInvoice" property="isInvoice" value="Y">是</html:radio>
    <html:radio name="maintenanceWorkPlanDetailBean" styleId="isInvoice" property="isInvoice" value="N">否</html:radio>
   </tr>
   <tr>
   <td nowrap="nowrap" class="wordtd">单据录入人:</td>
   <td nowrap="nowrap" width="30%"><bean:write name="maintenanceWorkPlanDetailBean" property="djOperId2"/></td>
   <td nowrap="nowrap" class="wordtd">单据录入日期：</td>
   <td nowrap="nowrap" width="30%"><bean:write name="maintenanceWorkPlanDetailBean" property="djOperDate2"/></td>
   </tr>
  </table>
  <br> 

   
</logic:present>
</html:form>