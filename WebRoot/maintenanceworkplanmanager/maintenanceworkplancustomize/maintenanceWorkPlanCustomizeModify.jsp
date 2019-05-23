<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
	<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>


<html:form action="/maintenanceWorkPlanCustomizeAction.do?method=toUpdateRecord">
 <html:hidden property="isreturn"/>
 <input type="hidden" name="issaverem" id="issaverem" value="N"/>
<html:errors/>
 <logic:present name="maintContractMasterBean">
   <table id="contractInfoTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td height="23" colspan="6">&nbsp;<b>合同主信息</td>
    </tr>
    <tr>
      <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContract.maintContractNo"/>:</td>
      <td  class="inputtd" nowrap="nowrap" width="25%">
        <a href="<html:rewrite page='/maintContractAction.do'/>?method=toDisplayRecord&typejsp=Yes&id=<bean:write name="maintContractMasterBean"  property="billNo"/>" target="_blnk"><bean:write name="maintContractMasterBean"  property="maintContractNo"/></a>          
        <html:hidden name="maintContractMasterBean"  property="billNo"/>
      </td>          
      <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContract.contractNatureOf"/>:</td>
      <td class="inputtd"  nowrap="nowrap" width="30%">自保</td>
      <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContract.mainMode"/>:</td>
      <td class="inputtd" nowrap="nowrap" width="15%">
        <logic:match name="maintContractMasterBean" property="mainMode" value="FREE">免费</logic:match>
        <logic:match name="maintContractMasterBean" property="mainMode" value="PAID">收费</logic:match>
      </td>         
    </tr>  
    <tr>
      <td class="wordtd_a"><bean:message key="maintContract.contractPeriod"/>:</td>
      <td class="inputtd" >
        <span class="renewal show"><bean:write name="maintContractMasterBean" property="contractPeriod"/></span>  
        <span class="renewal hide"><input type="text" name="contractPeriod" class="default_input_noborder"></span>         
      </td>          
      <td class="wordtd_a"><bean:message key="maintContract.contractSdate"/>:</td>
      <td class="inputtd" >
        <span class="renewal show"><bean:write name="maintContractMasterBean" property="contractSdate"/></span>      
      </td>
      <td class="wordtd_a"><bean:message key="maintContract.contractEdate"/>:</td>
      <td class="inputtd" >
        <span class="renewal show"><bean:write name="maintContractMasterBean" property="contractEdate"/></span>
      </td>         
    </tr>        
    <tr>
      <td class="wordtd_a"><bean:message key="maintContract.maintDivision"/>:</td>
      <td class="inputtd" >
        <bean:write name="maintContractMasterBean" property="maintDivision"/>
      </td> 
      <td class="wordtd_a">所属维保站:</td>
      <td>
      	<bean:write name="maintContractMasterBean" property="maintStation"/>
      </td> 
      <td class="wordtd_a"><bean:message key="maintContract.attn"/>:</td>
      <td class="inputtd" >
        <bean:write name="maintContractMasterBean" property="attn"/>
      </td>
    </tr>
    <tr>
      <td class="wordtd_a">定制备注:</td>
      <td colspan="5">
      	<html:textarea name="maintContractMasterBean" property="customizeRem" styleId="customizeRem" rows="3" cols="100" styleClass="default_textarea"/> 
      </td>       
    </tr>
  </table>
 </logic:present>
 <br/>
  <logic:present name="maintContractDetailList">
  <table id="contractInfoTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr >
      <td height="23" colspan="12">&nbsp;<b>合同明细</td>
    </tr>
    <tr >
    <td class="wordtd_a" style="text-align: center;">电梯编号</td>
    <td class="wordtd_a" style="text-align: center;" nowrap="nowrap">电梯类型</td>
    <td class="wordtd_a" style="text-align: center;">层/站/门</td>
    <td class="wordtd_a" style="text-align: center;">提升高度</td>
    <td class="wordtd_a" style="text-align: center;">年检日期</td>
    <td class="wordtd_a" style="text-align: center;">销售合同号</td>
    <td class="wordtd_a" style="text-align: center;">项目名称</td>
    <td class="wordtd_a" nowrap="nowrap" style="text-align: center;">维保开始时间</td>
    <td class="wordtd_a" nowrap="nowrap" style="text-align: center;">维保计划开始时间</td>
    <td class="wordtd_a" nowrap="nowrap" style="text-align: center;">维保结束时间</td>
    <td class="wordtd_a" style="text-align: center;">下达维保站</td>
    <td class="wordtd_a" style="text-align: center;">维保工</td>
    <td class="wordtd_a" style="text-align: center;">保养逻辑</td>
    </tr>
    
    <logic:empty name="maintContractDetailsize">
    <tr><td class="inputtd" colspan="12" style="text-align: center;">没有可定制保养计划！</td></tr>
    </logic:empty>
    
    <logic:notEmpty name="maintContractDetailsize">
    <logic:iterate id="element" name="maintContractDetailList">
		    <tr>
		    <td nowrap="nowrap" style="text-align: center;">
			<input style="width: 110px;" onclick="simpleOpenWindow('elevatorSaleAction',this.value);" class="link noborder_center" readonly="readonly" value="<bean:write name="element" property="elevatorNo" />" name="elevatorNo">	
			<input type="hidden" name="rowid" value="${element.rowid}" >
			</td>
			<td nowrap="nowrap" style="text-align: center;">${element.elevatorType=='T'?'直梯':'扶梯'}</td>
	        <td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="floor" />/<bean:write name="element" property="stage" />/<bean:write name="element" property="door" /></td>
			<td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="high" /></td>
			<td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="annualInspectionDate" /></td>
			
			<td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="salesContractNo" /></td>
			<td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="projectName" /></td>
			<td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="mainSdate" /></td>
			<td nowrap="nowrap" style="text-align: center;"><html:text name="element" property="shippedDate" size="12" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true})"></html:text> </td>
			<td nowrap="nowrap" style="text-align: center;"><html:text name="element" property="mainEdate" size="12" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true})"></html:text></td>
			<td nowrap="nowrap" style="text-align: center;"><bean:write name="element" property="assignedMainStation" /></td>
		    <td nowrap="nowrap" style="text-align: center;">
		    &nbsp;<a href="<html:rewrite page='/maintenanceWorkPlanCustomizeAction.do'/>?method=toMaintenanceWorkPlanDisplayRecord&isOpen=Y&id=${element.r1}" target="_blnk"><bean:write name="element" property="maintPersonnel"/></a>&nbsp;
			<td nowrap="nowrap" style="text-align: center;">
				<select name="maintlogic">
					<option value="1">逻辑一</option>
					<option value="2">逻辑二</option>
					<option value="3">逻辑三</option>
					<option value="4">逻辑四</option>
					<option value="5">逻辑五</option>
				</select>
				<input type="hidden" name="hmaintlogic" value="${element.r2 }"/>
			</td>
		    </td>      
		    </tr>
		    </logic:iterate>
    </logic:notEmpty>    
    </table>

    </logic:present>
</html:form>

<script>
//界面初始化为上一次分配保养逻辑
	function setMaintPersonnel(){
		var hismp=document.getElementsByName("hmaintlogic");
		var maintlogic=document.getElementsByName("maintlogic");
		for(var i=0;i<hismp.length;i++){
			var hmpval=hismp[i].value;
			if(hmpval!=""){
				var mpsel=maintlogic[i];
				//循环下拉框里面的值
				for(var j=0;j<mpsel.length;j++){
		              if(hmpval==mpsel.options[j].value){  
		            	  mpsel.options[j].selected=true;
		            	  break;
		              }  
		          }
			}
		}
	}
	setMaintPersonnel();
</script>	
	