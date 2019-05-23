<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>
<script language="javascript" src="<html:rewrite forward="jq.js"/>"></script>
    <html:hidden property="isreturn"/>
    <html:hidden property="id"/>
    <html:hidden name="ServicingContractQuoteMaster" property="billNo"/>

    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
        <td height="23" colspan="6">&nbsp;<b>主信息</td>
      </tr>
      
      <tr>
        <td nowrap="nowrap" class="wordtd"><bean:message key="maintContractQuote.attn"/>:</td>
        <td width="20%">
          <bean:write name="ServicingContractQuoteMaster" property="attn"/>
        </td>
        <td nowrap="nowrap" class="wordtd"><bean:message key="maintContractQuote.applyDate"/>:</td>
        <td width="20%">
          <bean:write name="ServicingContractQuoteMaster" property="applyDate"/>
       </td>
        <td class="wordtd"><bean:message key="maintContractQuote.maintDivision"/>:</td>
        <td width="20%">
          <bean:write name="ServicingContractQuoteMaster" property="maintDivision"/>
        </td>             
      </tr>    
      <tr>
        <td class="wordtd">甲方单位名称:</td>
        <td>
        ${ServicingContractQuoteMaster.companyId} 
          </td>
        <td class="wordtd">甲方联系人:</td>
        <td>${contacts }</td>
        <td class="wordtd">甲方联系电话:</td>
        <td >${contactPhone}</td>           
      </tr> 
      
      <tr>
      <td nowrap="nowrap" class="wordtd">业务类别:</td>
      <td>
     ${ServicingContractQuoteMaster.busType }
      </td>
      <td nowrap="nowrap" class="wordtd">所属维保站:</td>
      <td>   
         <bean:write name="maintStationName"/>    
      </td>
      </tr>
      <tr>
    <td class="wordtd">其他客户特殊要求:</td>
    <td colspan="5">${ServicingContractQuoteMaster.otherCustomer }</td>
      </tr>             
    </table>
    <br>
    
    <div id="scrollBox" style="overflow:scroll; overflow-y:hidden">
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" id="table_22">
        <thead>
          <tr height="23">
            <td colspan="4">            
              <b>&nbsp;电梯信息</b>
            </td>
          </tr>
          <tr id="tb_head" class="tb_head">
           <td class="wordtd_header" width="7%">维改梯编号<font color="red">*</font></td>
           <td class="wordtd_header" width="10%">销售合同号<font color="red">*</font></td>
            <td class="wordtd_header" width="23%">项目名称<font color="red">*</font></td>
            <td class="wordtd_header" width="50%">维改内容<font color="red">*</font></td>
          </tr>
        </thead>
        <tfoot>
          <tr height="23"><td colspan="4"></td></tr>
        </tfoot>
        <tbody>
          <logic:present name="scqdList">
            <logic:iterate id="element" name="scqdList" >
              <tr>
                <td align="center">${element.elevatorNo}</td>
                <td align="center">${element.salesContractNo}</td>
                <td align="center">${element.projectName}</td>
                <td align="center">${element.contents}</textarea></td>
                </tr>
            </logic:iterate>
          </logic:present>
        </tbody>    
      </table>
    </div>
  
    <br>
    <table id="uploadtab_1" width="100%">
  <tr>
  <td>
    <!-- 已上传的附件 -->
    <%@ include file="UpLoadedFile.jsp" %>
  </td>
  </tr>
  </table>
    <br>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" id="table_1">
    <thead>
    <tr height="23">
      <td colspan="8"><b>&nbsp;物料明细</b></td>
    </tr>
    <tr class="wordtd_header" id="tb_head">
    <td width="5%">序号 </td>
    <td width="35%">物料名称<font color="red">*</font></td>
    <td width="15%">规格<font color="red">*</font></td>
    <td width="5%">数量<font color="red">*</font></td>
    <td width="5%">单位<font color="red">*</font></td>
    <td width="10%">单价(元)<font color="red">*</font></td>
    <td width="10%">价格(元)<font color="red">*</font></td> 
    <td width="10%">最终价格(元)<font color="red">*</font></td>   
    </tr>
    </thead>
    <tfoot>
        <tr height="23"><td colspan="8"></td></tr>
     </tfoot>
    <tbody>
    <% int i=1; %>
    <logic:present name="scqmdList">
   <logic:iterate name="scqmdList" id="element">
    <tr id="tr1" >
    <td align="center"><%= i %></td>
    <td align="center">${element.materialName}</td>
    <td align="center">${element.materialsStandard}</td>
    <td align="center">${element.quantity}</td>   
    <td align="center">${element.unit}</td>
    <td align="center">${element.unitPrice}</td>
    <td align="center">${element.price}</td>
    <td align="center">${element.finalPrice}</td>
    <%i++; %>
    </tr>
    </logic:iterate>
    </logic:present>
    </tbody>
    </table >
    <br>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
    <thead>
    <tr height="23" >
    <td colspan="4" >
    <b>&nbsp;其他费用明细</b>
    </td>
    </tr>
    <tr height="23" class="wordtd_header" id="tb_head">
    <td align="center">序号</td>
    <td align="center">费用名称</td>
    <td align="center">价格(元)</td>
    <td align="center">最终价格(元)</td>
    </tr>
    </thead>
    <% int j=1;%>
    <logic:present name="scqodList">
   <logic:iterate name="scqodList" id="element">
   <tr>
   <td align="center" width="10%"><%=j %></td>
   <td align="center" width="70%">${element.feeName }</td>
   <td align="center" width="10%">${element.price }</td>
   <td align="center" width="10%">${element.finalPrice }</td>  
   </tr>
   <%j++; %>
   </logic:iterate>
    </logic:present>
    </table>
    <br>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
        <td class="wordtd">标准报价合计(元):</td>
        <td width="20%">
          ${ ServicingContractQuoteMaster.standardQuoteTotal}
        </td>
        <td class="wordtd">最终报价合计(元):</td>
        <td width="20%">
           ${ ServicingContractQuoteMaster.finallyQuoteTotal}
        </td>
        <td class="wordtd">业务费(元):</td>
        <td width="20%"> ${ ServicingContractQuoteMaster.businessCosts}</td>
      </tr>
    </table>
    <br>
    
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
      <tr>
        <td class="wordtd"><bean:message key="maintContractQuote.priceFluctuaApply"/>:</td>
        <td>${ ServicingContractQuoteMaster.priceFluctuaApply}</td>
      </tr>      
      <tr>
        <td class="wordtd"><bean:message key="maintContractQuote.businessCostsApply"/>:</td>
        <td>${ ServicingContractQuoteMaster.businessCostsApply}</td>
      </tr>        
      <tr>
        <td class="wordtd"><bean:message key="maintContractQuote.paymentMethodApply"/>:</td>
        <td>${ ServicingContractQuoteMaster.paymentMethodApply}</td>      
      </tr>
      <%--
      <tr>
        <td class="wordtd"><bean:message key="maintContractQuote.specialApplication"/>:</td>
        <td>${ ServicingContractQuoteMaster.specialApplication}</td>      
      </tr>
      --%>
      <tr>
        <td class="wordtd"><bean:message key="maintContractQuote.contractContentApply"/>:</td>
        <td>${ ServicingContractQuoteMaster.contractContentApply}</td>                                         
      </tr>
    </table>
  <script type="text/javascript">
    $("document").ready(function(){
      setScrollTable("scrollBox","table_22",10);
    }); 
  </script>