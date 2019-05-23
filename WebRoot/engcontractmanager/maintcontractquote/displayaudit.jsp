<%@ page contentType="text/html;charset=GBK" %>
<logic:present name="maintContractQuoteBean">
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>

<html:hidden name="maintContractQuoteBean" property="billNo"/>
  <div class="title-bar">
    &nbsp;<b>主信息</b>
  </div>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td nowrap="nowrap" class="wordtd"><bean:message key="maintContractQuote.attn"/>:</td>
      <td width="20%">
        <bean:write name="attnName"/>
      </td>
      <td nowrap="nowrap" class="wordtd"><bean:message key="maintContractQuote.applyDate"/>:</td>
      <td width="20%">
        <bean:write name="maintContractQuoteBean" property="applyDate"/>
      </td>
      <td class="wordtd"><bean:message key="maintContractQuote.maintDivision"/>:</td>
      <td width="20%">
        <bean:write name="maintDivisionName"/>
      </td>          
    </tr>   
    <tr>
      <td class="wordtd">甲方单位名称:</td>
      <td>${maintContractQuoteBean.companyName}</td>
      <td class="wordtd">甲方联系人:</td>
      <td>${maintContractQuoteBean.contacts}</td>
      <td class="wordtd">甲方联系电话:</td>
      <td>${maintContractQuoteBean.contactPhone}</td>            
    </tr>  
    <tr>
      <td class="wordtd"><bean:message key="maintContractQuote.maintStation"/>:</td>
      <td>
         <bean:write name="maintStationName"/>
      </td>  
     <%--  <td nowrap="nowrap" class="wordtd">客户区域:</td>
      <td>
      	${maintContractQuoteBean.customerArea }
      </td>  --%>
      <td nowrap="nowrap" class="wordtd">报价签署方式:</td>
	    <td>
	    	<logic:equal name="maintContractQuoteBean" property="quoteSignWay" value="ZB">新签</logic:equal>
	    	<logic:equal name="maintContractQuoteBean" property="quoteSignWay" value="XB">续签</logic:equal>
	    	<logic:equal name="maintContractQuoteBean" property="quoteSignWay" value="BFXB">部分续签</logic:equal>
		</td>  
      <td nowrap="nowrap" class="wordtd">&nbsp;</td>
      <td>&nbsp;</td> 
    </tr>       
  </table>
  <br>
  
  <div id="caption" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">        
    <b>&nbsp;电梯信息</b>
    <b><font color="red">（注意：年检费=合同执行期间，要支付的年检费总额。）</font></b>
  </div>
  <div id="wrap" style="overflow-x:scroll">
  <table id="dynamictable" width="1500" border="0" cellpadding="0" cellspacing="0" class="tb">
    <thead>
      <tr id="tb_head">
      	<td class="wordtd_header" nowrap="nowrap">签署方式</td>
        <td class="wordtd_header" nowrap="nowrap">电梯编号</td>
        <td class="wordtd_header" nowrap="nowrap">电梯类型</td>
        <td class="wordtd_header">层</td>
        <td class="wordtd_header">站</td>
        <td class="wordtd_header">门</td>
        <td class="wordtd_header" nowrap="nowrap">提升高度</td>
      <td class="wordtd_header" nowrap="nowrap">载重</td>
      <td class="wordtd_header" nowrap="nowrap">速度</td>
      	<td class="wordtd_header" nowrap="nowrap">规格型号</td>
        <td class="wordtd_header" nowrap="nowrap">销售合同号</td>
        <td class="wordtd_header" nowrap="nowrap">项目名称</td>
        <td class="wordtd_header" nowrap="nowrap">项目地址</td>
      <td class="wordtd_header" nowrap="nowrap">电梯年龄(年)</td>
      <td class="wordtd_header" nowrap="nowrap">合同有效期(月)</td>
      <td class="wordtd_header" nowrap="nowrap">年检费(元)</td>
        <td class="wordtd_header" nowrap="nowrap">标准报价(元)</td>
        <td class="wordtd_header" nowrap="nowrap">最终报价(元)</td>
      <%-- td class="wordtd_header" nowrap="nowrap">标准报价计算描述</td--%>
      </tr>
    </thead>
    <tfoot>
      <tr height="18"><td colspan="14"></td></tr>
    </tfoot>
    <tbody>
      <logic:present name="maintContractQuoteDetailList">
        <logic:iterate id="element" name="maintContractQuoteDetailList" >
          <tr>
          	<td align="center" nowrap="nowrap">${element.r4}</td>
            <td align="center" nowrap="nowrap">${element.elevatorNo}</td>
            <td align="center" nowrap="nowrap">${element.elevatorType}</td>
            <td align="center">${element.floor}</td>
            <td align="center">${element.stage}</td>
            <td align="center">${element.door}</td>
            <td align="center">${element.high}</td>
            <td align="center">${element.weight}</td>
            <td align="center">${element.speed}</td>
            <td align="center">${element.elevatorParam}</td>
            <td align="center">${element.salesContractNo}</td>
            <td align="center">${element.projectName}</td>
            <td align="center">${element.projectAddress}</td>
            <td align="center">${element.elevatorAge}</td>
            <td align="center">${element.contractPeriod}</td>
            <td align="center">${element.jyMoney}</td>
            <td align="center">${element.standardQuote}</td>
            <td align="center">${element.finallyQuote}</td>
            <%--td align="center">${element.standardQuoteDis}</td--%>
          </tr>
        </logic:iterate>
      </logic:present>
    </tbody>    
  </table>   
  </div> 
  <br>
  
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
    <td class="wordtd">
    	<bean:message key="maintContractQuote.paymentMethodApply"/>:
    </td>
    <td width="20%">
        <bean:write name="maintContractQuoteBean" property="r4"/>
        <input type="hidden" name="paymentMethodApply" id="paymentMethodApply" value="<bean:write name="maintContractQuoteBean" property="paymentMethodApply"/>"/>
    </td>
    <td class="wordtd">税后业务费合计（元）:</td>
    <td width="20%" colspan="3"><bean:write name="maintContractQuoteBean" property="businessCosts"/>
    &nbsp;<font color="red">(如申请税前，此处请换算后填写=税前金额乘0.7)</font>
	</td>
    </tr>
    <logic:notEqual name="maintContractQuoteBean" property="paymentMethodRem" value="">
    	<tr>
	    <td class="wordtd">
	    	付款方式申请备注:
	    </td>
	    <td colspan="5">
	    	<bean:write name="maintContractQuoteBean" property="paymentMethodRem"/>
	    </td>
	    </tr>
    </logic:notEqual>
    <tr>
    <td class="wordtd">
    	合同包含配件及服务:
    </td>
    <td colspan="5">
    	<bean:write name="maintContractQuoteBean" property="contractContentApply"/>
    </td>
    </tr>
    <logic:notEqual name="maintContractQuoteBean" property="contractContentRem" value="">
	    <tr>
	    <td class="wordtd">
	    	合同包含配件及服务备注:
	    </td>
	    <td colspan="5">
	    	<bean:write  name="maintContractQuoteBean" property="contractContentRem"/>
	    </td>
	    </tr>
     </logic:notEqual>
    <tr>
      <td class="wordtd">最终报价合计（元）:</td>
      <td width="20%">
        <bean:write name="maintContractQuoteBean" property="finallyQuoteTotal"/>
      </td>
      <td class="wordtd">标准报价合计（元）:</td>
      <td width="20%">
        <bean:write name="maintContractQuoteBean" property="standardQuoteTotal"/>
      </td>
      <td class="wordtd">折扣率（%）:</td>
      <td width="20%"><bean:write name="maintContractQuoteBean" property="discountRate"/></td>
    </tr>
  </table>
  <br>
  
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <tr>
      <td class="wordtd"><bean:message key="maintContractQuote.priceFluctuaApply"/>:</td>
      <td><bean:write name="maintContractQuoteBean" property="priceFluctuaApply"/></td>
    </tr>      
    <tr>
      <td class="wordtd"><bean:message key="maintContractQuote.businessCostsApply"/>:</td>
      <td><bean:write name="maintContractQuoteBean" property="businessCostsApply"/></td>
    </tr>
    <tr>
      <td class="wordtd">特殊情况说明:</td>
      <td><bean:write name="maintContractQuoteBean" property="specialApplication"/></td>      
    </tr>

  </table> 
  
  <!-- 已上传的附件 -->
<%@ include file="UpLoadFileDisplay.jsp" %>
  
<%@ include file="/workflow/processApproveMessage.jsp" %>
  
  <script type="text/javascript">
	window.onload = function(){	 	
		setScrollTable("wrap","dynamictable",10);
		
		for(var n = 0; n < 3; n++){
			$(".col_"+n).each(function(i,obj){			
				var width = $(".col_"+n).get(0).offsetWidth;
				$(obj).hide();	
				$(obj).width(width);
				$(obj).show();
			})
		}
	}
	
  </script>
</logic:present>