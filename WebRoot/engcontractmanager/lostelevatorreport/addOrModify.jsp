<%@page contentType="text/html;charset=GBK"%>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>


<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden name="lostElevatorReportBean" property="jnlno" styleId="deljnlno"/>
<html:hidden name="lostElevatorReportBean" property="billNo" />
<html:hidden property="submitType" />

<table id="companyA" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td nowrap="nowrap" height="23" colspan="4">&nbsp;<b>丢梯报告基础信息</td>
  </tr>
  <tr>
    <td nowrap="nowrap" width="20%" class="wordtd">所属分部:</td>
    <td width="30%" nowrap="nowrap">
    	<bean:write name="maintDivisionName"/>
      <html:hidden name="lostElevatorReportBean" property="maintDivision" styleId="maintDivision"/>
    </td>
    <td nowrap="nowrap" width="20%" class="wordtd">所属维保站:</td>
    <td width="30%">
    	<bean:write name="maintStationName"/>
    	<html:hidden name="lostElevatorReportBean" property="maintStation"/>
    </td>
  </tr>
  <tr>    
    <td class="wordtd">维保合同号:</td>
    <td id="client">
    	<bean:write name="lostElevatorReportBean" property="maintContractNo"/>
    	<html:hidden name="lostElevatorReportBean" property="maintContractNo"/>
    </td>
    <td nowrap="nowrap" class="wordtd">项目名称:</td>
    <td id="contacts">
    	<bean:write name="lostElevatorReportBean" property="projectName"/>
    	<html:hidden name="lostElevatorReportBean" property="projectName"/>
    </td>   
  </tr>     
  <tr>
    <td nowrap="nowrap" class="wordtd">台量:</td>
    <td >
    	<bean:write name="lostElevatorReportBean" property="eleNum"/>
    	<html:hidden name="lostElevatorReportBean" property="eleNum"/>
    </td>   
    <td nowrap="nowrap" class="wordtd">丢梯日期:</td>
    <td >
    	<logic:equal name="lostElevatorReportBean" property="submitType" value="Y">
    		<bean:write name="lostElevatorReportBean" property="lostElevatorDate"/>
    		<html:hidden name="lostElevatorReportBean" property="lostElevatorDate"/>
    	</logic:equal>
    	<logic:notEqual name="lostElevatorReportBean" property="submitType" value="Y">
    		<html:text name="lostElevatorReportBean" property="lostElevatorDate" size="12" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/>
    	</logic:notEqual>
    </td>          
  </tr>
  <tr>      
    <td nowrap="nowrap" class="wordtd">合同类别:</td>
    <td  id="account">
    	<bean:write name="contractNatureOfName"/>
    	<html:hidden name="lostElevatorReportBean" property="contractNatureOf"/>
    </td>          
    <td nowrap="nowrap" class="wordtd">原因分析:</td>
    <td >
    	<logic:equal name="lostElevatorReportBean" property="submitType" value="Y">
    		<bean:write name="causeName"/>
    		<html:hidden name="lostElevatorReportBean" property="causeAnalysis"/>
    	</logic:equal>
    	<logic:notEqual name="lostElevatorReportBean" property="submitType" value="Y">
	    	<html:select name="lostElevatorReportBean" property="causeAnalysis" styleClass="default_input">
	    		<html:option value="">--请选择--</html:option>
	    		<html:options collection="causeAnalysisList" property="id.pullid" labelProperty="pullname"/>
	    	</html:select><font color="red">*</font>
	    </logic:notEqual>
    </td>   
  </tr>
  <tr>      
    <td class="wordtd">使用单位联系人:</td>
    <td >
    	<logic:equal name="lostElevatorReportBean" property="submitType" value="Y">
    		<bean:write name="lostElevatorReportBean" property="contacts"/>
    		<html:hidden name="lostElevatorReportBean" property="contacts"/>
    	</logic:equal>
    	<logic:notEqual name="lostElevatorReportBean" property="submitType" value="Y">
    		<html:text name="lostElevatorReportBean" property="contacts" size="30" styleClass="default_input"/><font color="red">*</font>
    	</logic:notEqual>
    </td>          
    <td nowrap="nowrap" class="wordtd">联系电话:</td>
    <td >
    	<logic:equal name="lostElevatorReportBean" property="submitType" value="Y">
    		<bean:write name="lostElevatorReportBean" property="contactPhone"/>
    		<html:hidden name="lostElevatorReportBean" property="contactPhone"/>
    	</logic:equal>
    	<logic:notEqual name="lostElevatorReportBean" property="submitType" value="Y">
    		<html:text name="lostElevatorReportBean" property="contactPhone" size="30" styleClass="default_input"/><font color="red">*</font>
    	</logic:notEqual>
    </td>   
  </tr>
  <tr>      
    <td class="wordtd">备注详细原因:</td>
    <td colspan="3">
    	<logic:equal name="lostElevatorReportBean" property="submitType" value="Y">
    		<bean:write name="lostElevatorReportBean" property="detailedRem"/>
    		<html:hidden name="lostElevatorReportBean" property="detailedRem"/>
    	</logic:equal>
    	<logic:notEqual name="lostElevatorReportBean" property="submitType" value="Y">
    		<html:textarea name="lostElevatorReportBean" property="detailedRem" rows="3" cols="100" styleClass="default_textarea"/><font color="red">*</font>
    	</logic:notEqual>
    </td>          
  </tr>
</table>

<br>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td nowrap="nowrap" height="23" colspan="2">&nbsp;<b>其它需增加信息</b></td>        
  </tr>
  <tr>
    <td width="20%" class="wordtd">竞争单位名称:</td>
    <td width="75%">
      <html:text name="lostElevatorReportBean" property="competeCompany" size="50" styleClass="default_input"/> 
    </td> 
  </tr>
  <tr>
    <td nowrap="nowrap" class="wordtd">恢复计划:</td>
    <td>      
      <html:textarea name="lostElevatorReportBean" property="recoveryPlan" rows="3" cols="100" styleClass="default_textarea"/>       
    </td>                  
  <html:hidden name="lostElevatorReportBean" property="auditOperid"/>
  <html:hidden name="lostElevatorReportBean" property="auditStatus"/>
  <html:hidden name="lostElevatorReportBean" property="auditDate"/>
  <html:hidden name="lostElevatorReportBean" property="auditRem"/>
  </tr>
</table>


<script type="text/javascript">
/* initPage();
$("document").ready(function() {
	  setScrollTable("wrap_0","dynamictable_0",11);
	  
})*/
</script>