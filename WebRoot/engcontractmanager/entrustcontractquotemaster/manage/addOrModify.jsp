<%@page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>


<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<input type="hidden" name="id1" value="${id}"/>
<html:hidden name="quoteBean" property="billNo"/>
<html:hidden name="quoteBean" property="maintBillNo" />
<html:hidden property="submitType" />

<table id="companyA" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <!-- <tr>
    <td nowrap="nowrap" height="23" colspan="6">&nbsp;<b>客户信息</td>
  </tr> -->
  <tr>
    <td class="wordtd_a">委托维保单位名称:</td>
    <td width="25%">
      <html:hidden name="quoteBean" property="companyId" styleId="companyId"/>
      <input type="text" name="companyName" id="companyName" value="${customer.companyName}" readonly="true" size="23"/>
      <input type="button" value=".." onclick="choiceCustomer()" class="default_input"/><font color="red">*</font>
    </td>
    <td class="wordtd_a">资质级别:</td>
    <td width="20%">
    	<input type="text" name="qualiLevelWb" id="qualiLevelWb" value="${customer.qualiLevelWb}" readonly="true" class="default_input_noborder"/>
    </td>
    <td class="wordtd_a">评定级别:</td>
    <td width="20%">
    	<html:text name="quoteBean" property="assLevel" styleClass="default_input"/>
    </td>  
  </tr>               
</table>

<br>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td  height="23" colspan="6">&nbsp;<b>合同主信息</td>
  </tr>
  <tr>
    <td class="wordtd_a">维保合同号:</td>
    <td width="25%">
      <input type="hidden" name="billNo1" value="${contractBean.billNo}"/>
      <bean:write name="contractBean" property="maintContractNo"/>
      <html:hidden name="quoteBean" property="maintContractNo"/>
    </td>          
    <td class="wordtd_a">合同性质:</td>
    <td width="20%">
    	<logic:match name="contractBean" property="contractNatureOf" value="ZB">自保</logic:match>
        <logic:match name="contractBean" property="contractNatureOf" value="PY">平移</logic:match>
        <logic:match name="contractBean" property="contractNatureOf" value="WT">委托</logic:match>
    </td>
     <td class="wordtd_a">保养方式:</td>
    <td width="20%">
        <logic:match name="contractBean" property="mainMode" value="FREE">免费</logic:match>
        <logic:match name="contractBean" property="mainMode" value="PAID">收费</logic:match>
    </td>         
  </tr>  
  <tr>
    <td class="wordtd_a">合同有效期（月）:</td>
    <td>
      <html:text name="quoteBean" property="contractPeriod" readonly="true" styleClass="default_input_noborder"/>          
    </td>  
    <td class="wordtd_a">合同开始日期:</td>
    <td>${contractSdate.contractSdate }
     <html:text name="quoteBean" property="contractSdate" styleId="contractSdate" styleClass="Wdate" size="12"   onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
    </td>
    <td class="wordtd_a">结束日期:</td>
    <td>
    <html:text name="quoteBean" property="contractEdate" styleId="contractEdate" styleClass="Wdate" size="12"   onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
    </td>        
  </tr>          
  <tr>
    <td class="wordtd_a">所属维保分部:</td>
    <td>
      <bean:write name="contractBean" property="maintDivision"/>
      <html:hidden name="quoteBean" property="maintDivision"/>
    </td> 
     <td class="wordtd_a">所属维保站:</td>
    <td >
      <bean:write name="contractBean" property="maintStation"/>
      <html:hidden name="quoteBean" property="maintStation"/>
    </td>
    <td class="wordtd_a">合同类型:</td>
    <td>
       <html:select name="quoteBean" property="contractNatureOf">
       <html:option value="">请选择</html:option>
       <html:options collection="contractNatureOfList" property="id.pullid" labelProperty="pullname"/>
      </html:select><font color="red">*</font>  
    </td>  
  </tr>
  <tr>
    <td class="wordtd_a">录入人:</td>
    <td>
      <bean:write name="quoteBean" property="r1"/>
      <html:hidden name="quoteBean" property="operId"/>
    </td> 
     <td class="wordtd_a">录入日期:</td>
    <td >
      <bean:write name="quoteBean" property="operDate"/>
      <html:hidden name="quoteBean" property="operDate"/>
    </td>
    <td class="wordtd_a">&nbsp;</td>
    <td>&nbsp;</td>  
  </tr> 
</table>
<br>

<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
  &nbsp;<b>&nbsp;合同明细</b>
</div>
<div id="wrap_0" style="overflow-x:scroll">
  <table id="dynamictable_0"  width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <thead>
      <tr id="titleRow_0">
        <td nowrap="nowrap" class="wordtd_header">电梯编号</td>
        <td nowrap="nowrap" class="wordtd_header">电梯类型</td>
        <td nowrap="nowrap" class="wordtd_header">层</td>
        <td nowrap="nowrap" class="wordtd_header">站</td>
        <td nowrap="nowrap" class="wordtd_header">门</td>
        <td nowrap="nowrap" class="wordtd_header">提升高度</td>
        <td nowrap="nowrap" class="wordtd_header">规格型号</td>
        <td nowrap="nowrap" class="wordtd_header">年检日期</td>
        <td nowrap="nowrap" class="wordtd_header">销售合同号</td>
        <td nowrap="nowrap" class="wordtd_header">购买单位名称</td> 
        <td nowrap="nowrap" class="wordtd_header">使用单位名称</td>
        <td nowrap="nowrap" class="wordtd_header">维保开始日期</td>
        <td nowrap="nowrap" class="wordtd_header">维保结束日期</td> 
      </tr>
    </thead>
    <tbody>
      <logic:present name="detailList">
        <logic:iterate id="e" name="detailList" >
          <tr>
          	<td nowrap="nowrap" align="center" style="display:none;"><input type="text" name="maintRowid" value="${e.rowid}" readonly="readonly" class="noborder_center"/></td>
            <td nowrap="nowrap" align="center">
            	<a onclick="simpleOpenWindow('elevatorSaleAction','${e.elevatorNo}');" class="link">
            		${e.elevatorNo}
            	</a>
            </td>
            <td nowrap="nowrap" align="center">
            	<logic:match name="e" property="elevatorType" value="T">直梯</logic:match>
            	<logic:match name="e" property="elevatorType" value="F">扶梯</logic:match>
            </td>
            <td nowrap="nowrap" align="center">${e.floor}</td>
            <td nowrap="nowrap" align="center">${e.stage}</td>
            <td nowrap="nowrap" align="center">${e.door}</td>
            <td nowrap="nowrap" align="center">${e.high}</td>
            <td nowrap="nowrap" align="center">${e.elevatorParam}</td>
            <td nowrap="nowrap" align="center">${e.annualInspectionDate}</td>
            <td nowrap="nowrap" align="center">${e.salesContractNo}</td>
            <td nowrap="nowrap" align="center">${e.projectName}</td>
            <td nowrap="nowrap" align="center">${e.maintAddress}</td>
            <td nowrap="nowrap" align="center"><html:text name="e" property="mainSdate" styleId="mainSdate" styleClass="Wdate" size="12"   onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text></td>
            <td nowrap="nowrap" align="center"><html:text name="e" property="mainEdate" styleId="mainEdate" styleClass="Wdate" size="12"   onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text></td>
          </tr>
        </logic:iterate>
      </logic:present>
    </tbody>    
  </table>
</div>
<br>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd_a">标准委托价格(元):</td>
    <td width="25%">
    	<input name="standardPrice" value="${quoteBean.standardPrice}" onkeypress="f_check_number3()" onpropertychange="calculation();" size="10"/>
      <!-- <html:text name="quoteBean" property="standardPrice" styleId="standardPrice" size="10" onkeypress="f_check_number3();" onkeydown="calculation();"/> -->
      <font color="red">*</font>         
    </td>          
    <td class="wordtd_a">实际委托价格(元):</td>
    <td width="20%">
    	<input name="realPrice" value="${quoteBean.realPrice}" onkeypress="f_check_number3()" onpropertychange="calculation();" size="10"/>
      <!-- <html:text name="quoteBean" property="realPrice" onkeypress="f_check_number3();" onkeydown="calculation();" size="10" /> -->
      <font color="red">*</font>        
    </td> 
    <td class="wordtd_a">加价率(%):</td>
    <td width="20%">
    	<html:text name="quoteBean" property="markups" readonly="true" styleClass="default_input_noborder"/>
    </td>        
  </tr>
  <tr>
  <td class="wordtd_a">备注:</td>
  <td colspan="5"><html:textarea property="rem" value="${quoteBean.rem}" rows="3" cols="80" styleClass="default_textarea"/></td>
  </tr>
</table>

<script type="text/javascript">
//initPage();
$("document").ready(function() {
	  setScrollTable("wrap_0","dynamictable_0",11);
	  setContractPeriod();
	  $("#contractSdate,#contractEdate").bind("propertychange",function(){setContractPeriod();}); // 计算合同有效期  
})
/* function initPage(){
	
   
  $("#contractSdate,#contractEdate").bind("propertychange",function(){setContractPeriod();}); // 计算合同有效期  
  
  setTopRowDateInputsPropertychange(dynamictable_0); //动态表格第一行日期控件添加propertychange事件
} */
</script>