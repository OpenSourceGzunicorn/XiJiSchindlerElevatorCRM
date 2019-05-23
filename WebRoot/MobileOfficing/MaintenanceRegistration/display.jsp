<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<style>
  .show{display:block;}
  .hide{display:none;}
</style>
<logic:present name="maintenanceWorkPlanDetailBean">
    <table  width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td nowrap="nowrap" class="wordtd">保养站:</td>
      <td  width="25%">
       <bean:write name="mapBean"  property="storagename"/>
      </td>          
      <td nowrap="nowrap" class="wordtd">维保工:</td>
      <td  width="30%">
      <bean:write name="mapBean"  property="username"/>
      </td>        
    </tr>  
    <tr>
      <td class="wordtd">合同号:</td>
      <td>
        <%-- <a href="<html:rewrite page='/maintContractAction.do'/>?method=toDisplayRecord&typejsp=Yes&id=${mapBean.billno}" target="_blnk"> --%><bean:write name="mapBean"  property="maintContractNo"/><!-- </a> -->
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
             <td class="wordtd">&nbsp;</td>
      <td>&nbsp;</td> 
     </tr>       
  </table>
  <br>
   <table  width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
   <td nowrap="nowrap" class="wordtd">接收日期:</td>
   <td width="25%"><bean:write name="maintenanceWorkPlanDetailBean" property="receivingTime"/></td>
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
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="r1" /></td>
   <td nowrap="nowrap" class="wordtd">被转派人电话:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="receivingPhone"/></td>
   </tr>
  </table>
  
  <br>
   <table  width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
   <td nowrap="nowrap" class="wordtd">保养单号:</td>
   <td  width="25%"><bean:write name="maintenanceWorkPlanDetailBean" property="singleno"/></td>
   <td nowrap="nowrap" class="wordtd">电梯编号:</td>
   <td  width="30%">
   <a onclick="simpleOpenWindow('elevatorSaleAction','${mapBean.elevatorNo}');" class="link"><bean:write name="mapBean" property="elevatorNo"/></a></td>
   </tr>
   <tr>
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
   <td class="wordtd">准保养时工:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean"  property="maintDateTime" /></td>
   </tr>
   <tr>
    <td class="wordtd">上次保养日期:</td>
   <td><bean:write name="mapBean"  property="sMaintEndTime" /></td>
   <td class="wordtd"></td>
   <td></td>
   </tr>
   <tr>
   <td class="wordtd">保养开始时间:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="maintStartTime"/></td>
   <td class="wordtd">地址:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="maintStartAddres"/></td>
   </tr>
   <tr>
   <td class="wordtd">保养结束时间:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="maintEndTime"/></td>
   <td class="wordtd">地址:</td>
   <td><bean:write name="maintenanceWorkPlanDetailBean" property="maintEndAddres"/></td>
   </tr>
   <tr>
   <% int i=1; %>
   <td class="wordtd">附件:</td>
   <td colspan="3">
   <logic:present name="fileSidList">
   <logic:iterate id="ele" name="fileSidList">
   <a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${ele.fileSid}')">附件<%=i%></a>&nbsp;&nbsp; <%i++; %>
   </logic:iterate>
   </logic:present>
   </td>   
   </tr>  
   </table>
</logic:present>