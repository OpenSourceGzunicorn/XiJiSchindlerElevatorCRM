<%@page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>
<a id="ss"></a>
<html:hidden property="isreturn"/>

<html:hidden property="id" value="${quoteBean.billNo}"/>
<html:hidden name="quoteBean" property="billNo"/>
<html:hidden property="submitType"/>
<table id="companyA" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" >
  <tr>
    <td class="wordtd">委托维改单位名称:</td>
    <td width="30%">
    	<logic:present name="customer">
      		<bean:write name="customer" property="companyName"/>
      	</logic:present>
      <html:hidden name="quoteBean" property="companyId" styleId="companyId"/>
    </td>
    <td class="wordtd">资质级别:</td>
    <td width="30%">
    <logic:present name="customer">
    	<bean:write name="customer" property="qualiLevelWg"/>
    </logic:present>
    </td>
  </tr>
</table>
<br>
<div height="23"  class="tb" style="border-bottom: 0" width="100%" >&nbsp;<b>合同主信息</b></div>
<table  border="0"  width="100%" cellpadding="0" cellspacing="0" class="tb" >
  <tr>
    <td class="wordtd_a">维改合同号:</td>
    <td width="30%">
    	<bean:write name="contractBean" property="maintContractNo"/>
    </td>          
    <td nowrap="nowrap" class="wordtd">业务类别:</td>
    <td width="30%">
    	<logic:match name="contractBean" property="busType" value="G">改造</logic:match>
        <logic:match name="contractBean" property="busType" value="W">维修</logic:match>
    </td>
    </tr>               
  <tr>         
    <td class="wordtd" >所属维保分部:</td>
    <td>
      <bean:write name="contractBean" property="maintDivision"/>
    </td> 
      <td nowrap="nowrap" class="wordtd">所属维保站:</td>
      <td>   
         <bean:write name="contractBean" property="maintStation"/>    
      </td>
  </tr> 
  <tr>
    <td nowrap="nowrap" class="wordtd" >录入人:</td>
    <td>
    	<bean:write name="quoteBean" property="r1"/>
    </td>
    <td nowrap="nowrap" class="wordtd" >录入日期:</td>
    <td>
    	<bean:write name="quoteBean" property="operDate"/>
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
        <td class="wordtd_header" >销售合同号</td>
        <td class="wordtd_header">项目名称</td>
        <td class="wordtd_header" >维改内容</td>
      </tr>
    </thead>
    <tfoot>
      <tr height="10"><td colspan="4"></td></tr>
    </tfoot>
    <tbody>
      <logic:present name="detailList">
        <logic:iterate id="e" name="detailList" >
          <tr>         
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

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" >
  <tr>
    <td class="wordtd">标准委托价格(元):</td>
    <td width="20%">
    	<bean:write name="quoteBean" property="standardPrice"/>
    </td>         
    <td class="wordtd">实际委托价格(元):</td>
    	<td width="20%">
    <bean:write name="quoteBean" property="realPrice"/>
    </td>     
    <td class="wordtd">加价率(%):</td>
    <td width="20%">
    	<bean:write name="quoteBean" property="markups"/>
    </td> 
  </tr>
  <tr>
  <td  class="wordtd_a">备注:</td>
  <td colspan="5"><bean:write name="quoteBean" property="rem"/></td>
  </tr>
</table>
<br>
 
<script type="text/javascript">
$("document").ready(function(){
	setScrollTable("scrollBox","dynamictable_0",10);
})

/* function setbus(){
  var busType='${ServicingContractMasterList.busType}';
  $("#busType").html(busType=="W"?"维修":"改造");
}
setbus();
function openwindowcb(obj){
window.open('<html:rewrite page="/elevatorSaleAction.do"/>?method=toDisplayRecord&isOpen=Y&id='+obj,'','height=500px, width=1000px,scrollbars=yes, resizable=yes,directories=no');
} */

</script>