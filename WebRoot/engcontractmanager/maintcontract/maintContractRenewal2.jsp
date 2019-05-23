<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=GBK">
  <title>XJSCRM</title>
</head>
<body>
  <html:errors/>
 <html:form action="/maintContractAction.do?method=toRenewalRecord2" enctype="multipart/form-data">
    <html:hidden property="isreturn"/>
<html:hidden property="id"/>
<html:hidden name="maintContractQuoteBean" property="billNo"/>
<html:hidden property="maintContractDetails" styleId="maintContractDetails"/>
<html:hidden name="maintContractQuoteBean" property="historyBillNo" styleId="historyBillNo"/>
<html:hidden name="maintContractQuoteBean" property="histContractNo" styleId="histContractNo"/>
<html:hidden name="maintContractQuoteBean" property="histContractStatus" styleId="histContractStatus"/>
<html:hidden name="maintContractQuoteBean" property="xqType" styleId="xqType"/>

<div class="title-bar">
  &nbsp;<b>主信息</b>
</div>
<table id="table1" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContractQuote.attn"/>:</td>
    <td nowrap="nowrap" class="col_1">
      <bean:write name="attnName"/>
      <html:hidden name="maintContractQuoteBean" property="attn"/>
    </td>
    <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContractQuote.applyDate"/>:</td>
    <td nowrap="nowrap" class="col_2">
      <bean:write name="maintContractQuoteBean" property="applyDate"/>
      <html:hidden name="maintContractQuoteBean" property="applyDate"/>
    </td>
    <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContractQuote.maintDivision"/>:</td>
    <td nowrap="nowrap" class="col_3">
      <bean:write name="maintDivisionName"/>
      <html:hidden name="maintContractQuoteBean" property="maintDivision"/>
    </td>           
  </tr>   
  <tr>
    <td nowrap="nowrap" class="wordtd_a">甲方单位名称:</td>
    <td nowrap="nowrap" nowrap="nowrap">
      <html:text name="maintContractQuoteBean" property="companyName" styleId="companyName" size="35"/><font color="red">*</font>
     </td>
    <td nowrap="nowrap" class="wordtd_a">甲方联系人:</td>
    <td>
    <html:text name="maintContractQuoteBean" property="contacts"/>
    </td>
    <td nowrap="nowrap" class="wordtd_a">甲方联系电话:</td>
    <td>
    <html:text name="maintContractQuoteBean" property="contactPhone"/>
    </td>          
  </tr>
  <tr>
    <td nowrap="nowrap" class="wordtd_a"><bean:message key="maintContractQuote.maintStation"/>:</td>
    <td nowrap="nowrap">
      ${maintStationName}
      <html:hidden name="maintContractQuoteBean" property="maintStation"/>
      <%-- <html:select name="maintContractQuoteBean" property="maintStation">
        <html:options collection="maintStationList" property="storageid" labelProperty="storagename"/><font color="red">*</font>
      </html:select> --%>
    </td>
    <%-- <td nowrap="nowrap" class="wordtd_a">客户区域:</td>
    <td nowrap="nowrap"><html:text name="maintContractQuoteBean" property="customerArea" styleId="customerArea" styleClass="default_input"/></td>   --%>
   	<td nowrap="nowrap" class="wordtd">报价签署方式:</td>
    <td>
    	<logic:equal name="maintContractQuoteBean" property="quoteSignWay" value="ZB">新签</logic:equal>
    	<logic:equal name="maintContractQuoteBean" property="quoteSignWay" value="XB">续签</logic:equal>
    	<logic:equal name="maintContractQuoteBean" property="quoteSignWay" value="BFXB">部分续签</logic:equal>
		<html:hidden name="maintContractQuoteBean" property="quoteSignWay"/>
	</td> 
    <td nowrap="nowrap" class="wordtd_a">&nbsp;</td>
    <td nowrap="nowrap">&nbsp;</td> 
  </tr>         
</table>
<br>

<div id="caption_0" style="width: 100%;padding-top: 2;padding-bottom: 2;border-bottom: 0" class="tb">
   	<logic:equal name="maintContractQuoteBean" property="xqType" value="ALL">
  		&nbsp;<input type="button" value="选择其他合同" onclick="addMaintContracts('dynamictable_0')" class="default_input">
  	</logic:equal>
  <input type="button" value=" + " onclick="addElevators('dynamictable_0')" class="default_input">
  <input type="button" value=" - " onclick="deleteRow('dynamictable_0');sumValuesByName('standardQuote','standardQuoteTotal');jsr8();" class="default_input">           
  <b>&nbsp;电梯信息</b>
  <%-- input type="button" value="计算标准报价" onclick="jsQuote()" class="default_input"--%>  
  <b><font color="red">（注意：年检费=合同执行期间，要支付的年检费总额。）</font></b> 
</div>
<div id="wrap_0" style="overflow-x:scroll">
<table id="dynamictable_0" class="dynamictable tb" width="1500" border="0" cellpadding="0" cellspacing="0">
  <thead>
    <tr id="titleRow_0">
      <td class="wordtd_header"><input type="checkbox" id="cbAll" onclick="checkTableAll('dynamictable_0',this)"/></td>
      <td class="wordtd_header" nowrap="nowrap">签署方式</td>
      <td class="wordtd_header" nowrap="nowrap">电梯编号</td>
      <td class="wordtd_header" nowrap="nowrap">电梯类型</td>
      <td class="wordtd_header">层</td>
      <td class="wordtd_header">站</td>
      <td class="wordtd_header">门</td>
      <td class="wordtd_header" nowrap="nowrap">提升高度</td>
      <td class="wordtd_header" nowrap="nowrap">载重</td>
      <td class="wordtd_header" nowrap="nowrap">速度</td>
      <td class="wordtd_header">规格型号</td>
      <td class="wordtd_header">销售合同号</td>
      <td class="wordtd_header">项目名称</td>
      <td class="wordtd_header">项目地址</td>
      <td class="wordtd_header" nowrap="nowrap">电梯年龄(年)<font color="red">*</font></td>
      <td class="wordtd_header" nowrap="nowrap">合同有效期(月)<font color="red">*</font></td>
      <td class="wordtd_header" nowrap="nowrap">年检费(元)</td>
      <%-- td class="wordtd_header" nowrap="nowrap">标准报价(元)</td--%>
      <td class="wordtd_header" nowrap="nowrap">最终报价(元)</td>
      <%-- td class="wordtd_header" nowrap="nowrap">标准报价计算描述</td--%>
    </tr>
  </thead>
  <tfoot>
    <tr height="23"><td colspan="15"></td></tr>
  </tfoot>
  <tbody>
    <tr id="sampleRow_0" style="display:none">
      <td align="center"><input type="checkbox" onclick="cancelCheckAll('dynamictable_0','cbAll')"/></td>
      <td align="center">
      	<input type="hidden" name="signWay"/>
      	<input type="text" name="signWayName" size="10" readonly="readonly" class="noborder_center"/>
      </td>
      <td align="center"><input type="text" name="elevatorNo" size="14" readonly="readonly" class="noborder_center"/></td>
      <td align="center"><input type="text" name="elevatorType" size="10" readonly="readonly" class="noborder_center"/></td>
      <td align="center"><input type="text" name="floor" size="3" readonly="readonly" class="noborder_center"/></td>
      <td align="center"><input type="text" name="stage" size="3" readonly="readonly" class="noborder_center"/></td>
      <td align="center"><input type="text" name="door" size="3" readonly="readonly" class="noborder_center"/></td>
      <td align="center"><input type="text" name="high" size="3" readonly="readonly" class="noborder_center"/></td>
      <td align="center"><input type="text" name="weight" size="10" readonly="readonly" class="noborder_center"/></td>
      <td align="center"><input type="text" name="speed" size="10" readonly="readonly" class="noborder_center"/></td>
      <td align="center"><input type="text" name="elevatorParam" size="20" readonly="readonly" class="noborder_center"/></td>
      <td align="center"><input type="text" name="salesContractNo" size="12" readonly="readonly" class="noborder_center"/></td>
      <td align="center"><input type="text" name="projectName" size="30" readonly="readonly" class="noborder_center"/></td>
      <td align="center"><input type="text" name="projectAddress" size="38" readonly="readonly" class="noborder_center"/></td>
      <td align="center"><input type="text" name="elevatorAge" size="8" onpropertychange="checkthisvalue(this);setOhterVal(this);"/></td>
     <td align="center"><input type="text" name="contractPeriod" onpropertychange="checkthisvalue(this);setOhterVal(this);" size="8"/></td>
      <td align="center"><input type="text" name="jyMoney" size="8" onpropertychange="checkthisvalue(this);setOhterVal(this);"/></td>
      <%--td align="center"><input type="text" name="standardQuote" size="8" readonly="readonly" class="noborder_center"/></td--%>
      <td align="center">
      		<input type="text" name="finallyQuote" onpropertychange="checkthisvalue(this);setOhterVal(this);jsr8();" size="8"/>
      		<input type="hidden" name="standardQuote" />
      		<input type="hidden" name="standardQuoteDis"/>
      </td>
      <%--td align="center"><textarea name="standardQuoteDis" rows="3" cols="100" readonly="readonly" class="noborder_center">&nbsp;</textarea></td--%>
    </tr>
    <logic:present name="maintContractQuoteDetailList">
      <logic:iterate id="element" name="maintContractQuoteDetailList" >
        <tr>
          <td align="center"><input type="checkbox" onclick="cancelCheckAll(this)"/></td>
          <td align="center">
          	${element.r4}
          	<input type="hidden" name="signWay" value="${element.signWay}"/>
          </td>
          <td align="center"><input type="text" name="elevatorNo" size="14" value="${element.elevatorNo}" readonly="readonly" class="noborder_center"/></td>
          <td align="center"><input type="text" name="elevatorType" size="10" value="${element.elevatorType}" readonly="readonly" class="noborder_center"/></td>
          <td align="center"><input type="text" name="floor" size="3" value="${element.floor}" size="3" readonly="readonly" class="noborder_center"/></td>
          <td align="center"><input type="text" name="stage" size="3" value="${element.stage}" size="3" readonly="readonly" class="noborder_center"/></td>
          <td align="center"><input type="text" name="door" size="3" value="${element.door}" size="3" readonly="readonly" class="noborder_center"/></td>
          <td align="center"><input type="text" name="high" size="3" value="${element.high}" size="3" readonly="readonly" class="noborder_center"/></td>
          <td align="center"><input type="text" name="weight" size="10" value="${element.weight}" readonly="readonly" class="noborder_center"/></td>
      	  <td align="center"><input type="text" name="speed" size="10" value="${element.speed}" readonly="readonly" class="noborder_center"/></td>
          <td align="center"><input type="text" name="elevatorParam" size="20" value="${element.elevatorParam}"  readonly="readonly" class="noborder_center"/></td>
      	  <td align="center"><input type="text" name="salesContractNo" size="12" value="${element.salesContractNo}" readonly="readonly" class="noborder_center"/></td>
          <td align="center"><input type="text" name="projectName" size="30" value="${element.projectName}" readonly="readonly" class="noborder_center"/></td>
          <td align="center"><input type="text" name="projectAddress" size="38" value="${element.projectAddress}" readonly="readonly" class="noborder_center"/></td>          
          <td align="center"><input type="text" name="elevatorAge" value="${element.elevatorAge}" onkeypress="f_check_number2();" size="8" onpropertychange="setOhterVal(this);"/></td>
      	  <td align="center"><input type="text" name="contractPeriod" value="${element.contractPeriod}" onpropertychange="checkthisvalue(this);setOhterVal(this);" size="8"/></td>
      	  <td align="center"><input type="text" name="jyMoney" value="${element.jyMoney}" size="8" onpropertychange="checkthisvalue(this);setOhterVal(this);"/></td>
      	  <%--td align="center"><input type="text" name="standardQuote" value="${element.standardQuote}"  readonly="readonly" class="noborder_center" size="8"/></td--%>
          <td align="center">
          		<input type="text" name="finallyQuote" value="${element.finallyQuote}" onpropertychange="checkthisvalue(this);setOhterVal(this);jsr8();" size="8"/>
          		<input type="hidden" name="standardQuote" value="${element.standardQuote}"/>
          		<input type="hidden" name="standardQuoteDis" value="${element.standardQuoteDis}"/>
          </td>
          <%--td align="center"><textarea name="standardQuoteDis" rows="3" cols="100" readonly="readonly" class="noborder_center">${element.standardQuoteDis}</textarea></td--%>
    	</tr>
      </logic:iterate>
    </logic:present>
  </tbody>    
</table>
</div>
<br>

<table id="table2" width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd">
    	<bean:message key="maintContractQuote.paymentMethodApply"/>:
    </td>
    <td width="28%">
		<html:select name="maintContractQuoteBean" property="paymentMethodApply" styleId="paymentMethodApply" onchange="isShowHidden(this)" >
			<html:option value="">请选择</html:option>
		 	<html:options collection="PaymentMethodList" property="id.pullid" labelProperty="pullname"/>
		</html:select>
		<font color="red">*</font>
    </td>
    <td class="wordtd">业务费合计（元）:</td>
    <td  width="20%" colspan="3"><html:text name="maintContractQuoteBean" property="businessCosts" styleId="businessCosts" onchange="checkthisvalue(this);jssdqtotal(this.value);"/>
    &nbsp;<font color="red">(如申请税前，此处请换算后填写=税前金额乘0.7)</font>
    </td>
    </tr>
    <tr style="display:none;" id="trok">
    <td class="wordtd">
    	付款方式申请备注:
    </td>
    <td colspan="5">
    	<html:textarea name="maintContractQuoteBean" styleId="paymentMethodRem" property="paymentMethodRem" rows="2" cols="80" styleClass="default_textarea"/><font color="red">*</font>
    </td>
    </tr>
    <tr>
    <td class="wordtd">
    	合同包含配件及服务:
    </td>
    <td colspan="5">
		<table id="searchCompany" style="border: 0;margin: 0;" class="tb">
       		<tr>
             <%int specialno=1; %>
       		<logic:present name="ContractContentList">
				  <logic:iterate id="element" name="ContractContentList">
						<td nowrap="nowrap"  style="border: none;" width="5%">
						&nbsp;
                         <input type="checkbox" name="contractContentApply" value="${element.id.pullid}" onclick="isShowHidden2();">${element.pullname }
                        <input type="hidden" name="contractContentApplyName" value="${element.pullname }"/>
                         </td>
					
						<% if(specialno%5==0){ %></tr><tr><%} %>
						<%specialno++; %>
				</logic:iterate>
				</tr>
			</logic:present>
       	</table>
    </td>                               
  </tr>
  <tr style="display:none;" id="trok2">
    <td class="wordtd">
    	合同包含配件及服务备注:
    </td>
    <td colspan="5">
    	<html:textarea name="maintContractQuoteBean" styleId="contractContentRem" property="contractContentRem" rows="2" cols="80" styleClass="default_textarea"/><font color="red">*</font>
    </td>
    </tr>
  <tr>
    <td class="wordtd_a" nowrap="nowrap">最终报价合计（元）:</td>
    <td width="28%">
      <html:text name="maintContractQuoteBean" property="finallyQuoteTotal" styleId="finallyQuoteTotal" readonly="true" styleClass="default_input_noborder"/>
    </td>
    <td class="wordtd_a" nowrap="nowrap"><%--标准报价合计（元）:--%>&nbsp;&nbsp;</td>
    <td width="20%">
    	&nbsp;&nbsp;
    	<html:hidden name="maintContractQuoteBean" property="standardQuoteTotal" styleId="standardQuoteTotal"/>
      <%--html:text name="maintContractQuoteBean" property="standardQuoteTotal" styleId="standardQuoteTotal" readonly="true" styleClass="default_input_noborder"/--%>
    </td>
    <td class="wordtd_a" nowrap="nowrap"><%--折扣率（%）:--%>&nbsp;&nbsp;</td>
    <td width="20%">
    	&nbsp;&nbsp;
    	<html:hidden name="maintContractQuoteBean" property="discountRate" styleId="discountRate"/>
      <%--html:text name="maintContractQuoteBean" property="discountRate" styleId="discountRate"  readonly="true" styleClass="default_input_noborder"/--%>
    </td>
  </tr>
</table>
<br>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd_a" width="100"><bean:message key="maintContractQuote.priceFluctuaApply"/>:</td>
    <td><html:textarea name="maintContractQuoteBean" property="priceFluctuaApply" rows="3" cols="100" styleClass="default_textarea"/></td>
  </tr>      
  <tr>
    <td class="wordtd_a"><bean:message key="maintContractQuote.businessCostsApply"/>:</td>
    <td><html:textarea name="maintContractQuoteBean" property="businessCostsApply" rows="3" cols="100" styleClass="default_textarea"/></td>
  </tr>    
  <tr>
    <td class="wordtd">特殊情况说明:</td>
    <td><html:textarea name="maintContractQuoteBean" property="specialApplication" rows="3" cols="100" styleClass="default_textarea"/></td>
  </tr> 
</table>

<!-- 上传的附件 -->
<%@ include file="/engcontractmanager/maintcontractquote/UpLoadFile.jsp" %> 

<script type="text/javascript">
	window.onload = function(){	 	
		setDynamicTable("dynamictable_0","sampleRow_0");
		setScrollTable("wrap_0","dynamictable_0",10);
		
		for(var n = 0; n < 3; n++){
			$(".col_"+n).each(function(i,obj){			
				var width = $(".col_"+n).get(0).offsetWidth;
				$(obj).hide();	
				$(obj).width(width);
				$(obj).show();
			})
		}
	}
	
	function initapply(){
		//初始化是否显示付款方式备注
		var paymentMethodApply=document.getElementById("paymentMethodApply");
		isShowHidden(paymentMethodApply);
		//初始化勾选合同附件内容申请
		var ccastr='<bean:write name="maintContractQuoteBean" property="contractContentApply"/>';
		var ccarr=document.getElementsByName("contractContentApply");
	    for(var i=0;i<ccarr.length;i++){
	    	if(ccastr.indexOf(ccarr[i].value)>=0){
	    		ccarr[i].checked=true;
	    	}
	    }
	    //初始化是否显示合同包含配件及服务备注
		isShowHidden2();
	}
	initapply();
	
</script>
  </html:form>
</body>



