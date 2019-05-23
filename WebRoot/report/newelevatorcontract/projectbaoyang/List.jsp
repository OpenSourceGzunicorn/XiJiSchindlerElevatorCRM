<%@ page contentType="text/html;charset=gb2312" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<%-- <script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script> --%>

<html>
  <head>
    <title></title>
  </head>
  <body>
    <html:form action="/ProjectBaoYangAction.do?method=toSearchRecord">
      <html:hidden property="property(genReport)" styleId="genReport" value="Y"/>
      <html:hidden property="property(startDate)" styleId="startDate" value="${search.startDate}"/>
      <html:hidden property="property(endDate)" styleId="endDate" value="${search.endDate}"/>
      <html:hidden property="property(maintContractNo)" styleId="maintContractNo" value="${search.maintContractNo}"/>
      <html:hidden property="property(contractStatus)" styleId="contractStatus" value="${search.contractStatus}"/>
      <html:hidden property="property(companyName)" styleId="companyName" value="${search.companyName}"/>
      <html:hidden property="property(maintDivision)" styleId="maintDivision" value="${search.maintDivision}"/>

      <table width="98%"  border="0" cellpadding="0" cellspacing="0" align="center">
        <tr>
          <td align="center" height="50" style="font-size: 15px; color: blue;">      
            ${search.startDate}${search.endDate != '' ? '至' : ''}${search.endDate}电梯保养当期收款查询
          </td>
        </tr>
      </table>
      
      <table id="reportTable" width="98%" border="0" cellpadding="0" class="tb" cellspacing="0" align="center">
        <tr style="height:25px;">
            <td class="wordtd_header" nowrap>序号 </td>
            <td class="wordtd_header" nowrap>收款日期</td>
            <td class="wordtd_header" nowrap>应收款日期</td>
            <td class="wordtd_header" nowrap>维保合同号</td>
            <td class="wordtd_header" nowrap>甲方单位</td>
            <td class="wordtd_header" nowrap>收款金额</td>
            <td class="wordtd_header" nowrap>续保标志</td>
            <td class="wordtd_header" nowrap>所属分部</td>
        </tr>    
        <logic:present name="reportList">
          <logic:iterate id="element" name="reportList" indexId="i">
            <tr style="height:25px;">
              <td nowrap align="center">${i+1}</td>
              <td nowrap align="center">${element.paragraphDate}</td>
              <td nowrap align="center">${element.preDate}</td>
              <td nowrap align="center">${element.maintContractNo}</td>
              <td nowrap align="center">${element.companyName}</td>
              <td nowrap align="right">${element.paragraphMoney}</td>
              <td nowrap align="center">${element.contractStatus=='XB'?'续保':'新签'}</td>
              <td nowrap align="center">${element.maintDivisionName}</td>
            </tr>
          </logic:iterate>
        </logic:present>
        <tr style="height:25px;">
          <td colspan="8">      
            &nbsp;&nbsp;&nbsp;&nbsp;统计：记录数<b>${rowNums}</b>条，收款金额总计为：<b>${totalPrice}</b>（元）
          </td>
        </tr>
      </table>
  
      <br>
      
      <div align="center">
        <input type="button" name="toExcelRecord" value="导出Excel" style="padding-top: 4px;" onclick="this.form.submit();"/>&nbsp;
        <input type="button" name="close" value="&nbsp;关闭&nbsp;" style="padding-top: 4px;" onclick="javascript:window.close()";>
      </div>
      
      <br>
    </html:form>
  </body>
</html>