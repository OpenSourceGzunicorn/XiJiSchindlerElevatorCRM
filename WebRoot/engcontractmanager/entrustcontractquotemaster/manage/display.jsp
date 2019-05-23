<%@ page contentType="text/html;charset=GBK" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<style>
  .show{display:block;}
  .hide{display:none;}

</style>
<logic:present name="quoteBean">
<html:hidden name="quoteBean" property="billNo"/>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td  class="wordtd_a">委托维保单位名称:</td>
      <td width="20%">
      <logic:present name="customer">
      <bean:write name="customer" property="companyName"/>
      </logic:present>
      </td>
      <td  class="wordtd_a">资质级别:</td>
      <td width="20%">
      <logic:present name="customer">
      <bean:write name="customer" property="qualiLevelWb"/>
      </logic:present>
      </td>
      <td  class="wordtd_a">评定级别:</td>
      <td width="20%"><bean:write name="quoteBean" property="assLevel"/></td>   
    </tr>
  </table>
  <br>
  
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td  height="23" colspan="6">&nbsp;<b>合同主信息</td>
    </tr>
    <tr>
    <td  class="wordtd_a">维保合同号:</td>
    <td width="20%">
      <bean:write name="contractBean" property="maintContractNo"/>
    </td>          
    <td  class="wordtd_a">合同性质:</td>
    <td width="20%">
    	<logic:match name="contractBean" property="contractNatureOf" value="ZB">自保</logic:match>
        <logic:match name="contractBean" property="contractNatureOf" value="PY">平移</logic:match>
        <logic:match name="contractBean" property="contractNatureOf" value="WT">委托</logic:match>
    </td>
     <td  class="wordtd_a">保养方式:</td>
    <td width="20%">
        <logic:match name="contractBean" property="mainMode" value="FREE">免费</logic:match>
        <logic:match name="contractBean" property="mainMode" value="PAID">收费</logic:match>
    </td>         
  </tr>  
  <tr>
  <td class="wordtd_a">合同有效期（月）:</td>
    <td>
      <bean:write name="quoteBean" property="contractPeriod"/>          
    </td>
    <td  class="wordtd_a">合同开始日期:</td>
    <td>
      <bean:write name="quoteBean" property="contractSdate"/>
    </td>
    <td  class="wordtd_a">结束日期:</td>
    <td>
      <bean:write name="quoteBean" property="contractEdate"/>
    </td>         
         
  </tr>          
  <tr>
    <td  class="wordtd_a">所属维保分部:</td>
    <td>
      <bean:write name="contractBean" property="maintDivision"/>
    </td> 
     <td  class="wordtd_a">所属维保站:</td>
    <td >
      <bean:write name="contractBean" property="maintStation"/>
    </td>
    <td  class="wordtd_a">合同类型:</td>
    <td>
    <bean:write name="quoteBean" property="r2"/>
    </td>  
  </tr>
  <tr>
    <td  class="wordtd_a">录入人:</td>
    <td>
      <bean:write name="quoteBean" property="r1"/>
    </td> 
     <td  class="wordtd_a">录入日期:</td>
    <td >
      <bean:write name="quoteBean" property="operDate"/>
    </td>
    <td  class="wordtd_a"></td>
    <td></td>  
  </tr>
  </table>
  <br>
  
  <div style="width: 100%;padding-top: 4;padding-bottom: 4;border-bottom: 0" class="tb">        
   <b>&nbsp;合同明细</b>
  </div>
  <div id="wrap_0" style="overflow-x:scroll">
    <table id="dynamictable_0" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <thead id="header_0">
        <tr id="titleRow_0">
          <td  class="wordtd_header">电梯编号</td>
          <td  class="wordtd_header">电梯类型</td>
          <td  class="wordtd_header">层</td>
          <td  class="wordtd_header">站</td>
          <td  class="wordtd_header">门</td>
          <td  class="wordtd_header">提升高度</td>
          <td  class="wordtd_header">规格型号</td>
          <td  class="wordtd_header">年检日期</td>
          <td  class="wordtd_header">销售合同号</td>
          <td  class="wordtd_header">购买单位名称</td>
          <td  class="wordtd_header">使用单位名称</td>
          <td  class="wordtd_header">维保开始日期</td>
          <td  class="wordtd_header">维保结束日期</td>
        </tr>
      </thead>
      <tbody>
        <logic:present name="detailList">
          <logic:iterate id="e" name="detailList" >
            <tr>
              <td  align="center">
              	<a onclick="simpleOpenWindow('elevatorSaleAction','${e.elevatorNo}');" class="link">
              		${e.elevatorNo}
              	</a>
              </td>
              <td  align="center">
            	<logic:match name="e" property="elevatorType" value="T">直梯</logic:match>
            	<logic:match name="e" property="elevatorType" value="F">扶梯</logic:match>
              </td>
              <td  align="center">${e.floor}</td>
              <td  align="center">${e.stage}</td>
              <td  align="center">${e.door}</td>
              <td  align="center">${e.high}</td>
              <td  align="center">${e.elevatorParam}</td>
              <td  align="center">${e.annualInspectionDate}</td>
              <td  align="center">${e.salesContractNo}</td>
              <td  align="center">${e.projectName}</td>
              <td  align="center">${e.maintAddress}</td>
              <td  align="center">${e.mainSdate }</td>
              <td  align="center">${e.mainEdate }</td>
            </tr>
          </logic:iterate>
        </logic:present>
      </tbody>    
    </table>
  </div>
  <br>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td  class="wordtd_a">标准委托价格(元):</td>
    <td width="20%">
      <bean:write name="quoteBean" property="standardPrice"/>         
    </td>          
    <td  class="wordtd_a">实际委托价格(元):</td>
    <td width="20%">
      <bean:write name="quoteBean" property="realPrice"/>        
    </td> 
    <td  class="wordtd_a">加价率(%):</td>
    <td width="20%">
    	<bean:write name="quoteBean" property="markups"/>
    </td>        
  </tr>
  <tr>
  <td  class="wordtd_a">备注:</td>
  <td colspan="5"><bean:write name="quoteBean" property="rem"/></td>
  </tr>
  </table>
  
  <script type="text/javascript">
//滚动容器自适应宽度
  $("document").ready(function() {
	  
    $("input[name='rownum']").each(function(i,obj){
      obj.value = i+1;
    })

    setScrollTable("wrap_0","dynamictable_0",11);

})
  
  </script> 
</logic:present>