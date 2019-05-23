<%@page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>

<html:hidden property="isreturn"/>
<html:hidden property="submitType"/>
<html:hidden property="id"/>
<html:hidden name="quoteBean" property="wgBillno" />
<html:hidden name="quoteBean" property="billNo"/>
<table id="companyA" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd">委托维改单位名称:</td>
    <td width="30%">
      <input type="text" name="companyName" id="companyName" value="${customer.companyName}" readonly="true" size="23"/>
      <input type="button" value=".." onclick="choiceCustomer()" class="default_input"/><font color="red">*</font>
      <html:hidden name="quoteBean" property="companyId" styleId="companyId"/>
    </td>
    <td class="wordtd">资质级别:</td>
    <td width="30%">
    	<input type="text" name="qualiLevelWg" id="qualiLevelWg" value="${customer.qualiLevelWg}" readonly="true" class="default_input_noborder"/>
    </td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" >
  <tr>
    <td height="23" colspan="6">&nbsp;<b>合同主信息</td>
  </tr>
  <tr>
    <td class="wordtd">维改合同号:</td>
    <td width="30%">
      <bean:write name="contractBean" property="maintContractNo"/>
      <html:hidden name="quoteBean" property="maintContractNo"/>
    </td> 
    <td nowrap="nowrap" class="wordtd">业务类别:</td>
    <td width="30%">
        <logic:match name="contractBean" property="busType" value="G">改造</logic:match>
        <logic:match name="contractBean" property="busType" value="W">维修</logic:match>
    </td>
    </tr>             
  <tr>  
    <td class="wordtd">所属维保分部:</td>
    <td>
      <bean:write name="contractBean" property="maintDivision"/>
      <html:hidden name="quoteBean" property="maintDivision"/>
    </td> 
      <td class="wordtd">所属维保站:</td>
      <td>
    	<bean:write name="contractBean" property="maintStation"/>
    	<html:hidden name="quoteBean" property="maintStation"/>
   	  </td>
  </tr>
  <tr>
  	<td class="wordtd">录入人:</td>
  	<td>
  		<bean:write name="quoteBean" property="r1"/>
  		<html:hidden name="quoteBean" property="operId"/>
  	</td>
  	<td class="wordtd">录入日期:</td>
  	<td>
  		<bean:write name="quoteBean" property="operDate"/>
  		<html:hidden name="quoteBean" property="operDate"/>
  	</td>
  </tr>
</table>
<br>


<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb"><b>&nbsp;维改信息</b></div>
  <div id="scrollBox" style="overflow:scroll; overflow-y:hidden">
  <table id="dynamictable_0" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <thead>
      <tr id="titleRow_0">
        <td class="wordtd_header">维改梯编号</td>
        <td class="wordtd_header" >销售合同号<font color="red">*</font></td>
        <td class="wordtd_header">项目名称<font color="red">*</font></td>
        <td class="wordtd_header" >维改内容<font color="red">*</font> </td>
      </tr>     
    </thead>
    <tfoot>
      <tr height="10"><td colspan="4"></td></tr>
    </tfoot>
    <tbody>
      <logic:present name="detailList">
        <logic:iterate id="e" name="detailList" >
          <tr>
            <td nowrap="nowrap" align="center" style="display:none;"><input type="hidden" name="maintRowid" value="${e.rowid}"/></td>         
            <td align="center" >
	            <a onclick="simpleOpenWindow('elevatorSaleAction','${e.elevatorNo}');" class="link">
	            	${e.elevatorNo}
	            </a>
            </td>
            <td align="center" >${e.salesContractNo}</td>
            <td align="center" >${e.projectName}</td>
            <td>${e.contents}</td>
          </tr>
        </logic:iterate>
      </logic:present>      
    </tbody>    
  </table>
</div>
<br>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" style="table-layout:fixed;">
  <tr>
    <td class="wordtd" nowrap="nowrap">标准委托价格(元):</td>
    <td width="20%">
    	<input name="standardPrice" value="${quoteBean.standardPrice}" onkeypress="f_check_number3()" onpropertychange="calculation();" size="10"/>
    	<font color="red">*</font>
      <!-- <html:text name="quoteBean" property="standardPrice" onkeypress="f_check_number3()"  onchange="checkthisvalue(this);" /> -->
    </td>          
    <td class="wordtd" nowrap="nowrap">实际委托价格(元):</td>
    <td width="20%">
    	<input name="realPrice" value="${quoteBean.realPrice}" onkeypress="f_check_number3()" onpropertychange="calculation();" size="10"/>
    	<font color="red">*</font>
      <!-- <html:text name="quoteBean" property="realPrice" onkeypress="f_check_number3()"  onchange="checkthisvalue(this);" /> -->        
    </td>        
    <td class="wordtd" nowrap="nowrap">加价率(%):</td>
    <td width="20%">
    	<html:text name="quoteBean" property="markups" readonly="readonly" styleClass="default_input_noborder"/> 
    </td> 
  </tr>
   <tr>
  <td class="wordtd">备注:</td>
  <td colspan="5"><html:textarea property="rem" value="${quoteBean.rem}" rows="3" cols="80" styleClass="default_textarea"/></td>
  </tr>
</table>

<script type="text/javascript">
$("document").ready(function(){
	setScrollTable("scrollBox","dynamictable_0",10);
})

/* function setbus(){
  var busType='${ServicingContractMasterList.busType}';
  $("#busType").html(busType=="W"?"维修":"改造");
}
setbus(); */
</script>