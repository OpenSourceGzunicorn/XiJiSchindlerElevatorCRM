<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<html:errors />
<br>
<html:form action="/ServeTable.do">
<html:hidden property="property(genReport)" styleId="genReport" />
  <table>
    <tr>
      <td>
      &nbsp;&nbsp;      
        短信发送号码
        :
      </td>
      <td>
        <html:text property="property(smsTel)" styleClass="default_input"></html:text>
      </td>
       <td>
      &nbsp;&nbsp;      
        短信发送时间
        :
      </td>
      <td>
        <html:text property="property(smsSendTime)" styleClass="default_input" styleClass="Wdate" size="15" onfocus="WdatePicker({isShowClear:true,readOnly:true})" />
      	至
      	<html:text property="property(smsSendTime2)" styleClass="default_input" styleClass="Wdate" size="15" onfocus="WdatePicker({isShowClear:true,readOnly:true})" />
      </td>
      <td>
        &nbsp;&nbsp;               
       是否发送成功
        :
      </td>
      <td>
        <html:select property="property(flag)">
          <html:option value="">全部</html:option>
          <html:option value="1">成功</html:option>
		  <html:option value="0">失败</html:option>
        </html:select>
      </td>
    </tr>
  </table>
  <br>
  <table:table id="guiSmsHistory" name="smsHistoryList">
    <logic:iterate id="element" name="smsHistoryList">
      <%-- <table:define id="c_cb">
        <html:radio property="checkBoxList(ids)" styleId="ids" value="${element.jnlNo}" />
 			
      </table:define> --%>
     <%--  <table:define id="c_jnlNo">
        <bean:write name="element" property="jnlNo" />
      </table:define> --%>
    <table:define id="c_smsTel">
        <bean:write name="element" property="smsTel" />
      </table:define> 
      <table:define id="c_smsContent">
      		<bean:write name="element" property="smsContent" />
      </table:define>
      <table:define id="c_smsSendTime">
        <bean:write name="element" property="smsSendTime" />
      </table:define>
      <table:define id="c_flag">
        <logic:match name="element" property="flag" value="1">成功</logic:match>
        <logic:match name="element" property="flag" value="0">失败</logic:match>
      </table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
</html:form>