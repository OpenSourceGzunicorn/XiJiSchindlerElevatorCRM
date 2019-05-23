<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<br>
<html:errors/>
<html:form action="/markingScoreAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${markingScoreBean.msId}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="markingitems.mtId"/>:</td>
    <td>
      <bean:write name="markingScoreBean" scope="request" property="msId"/>
      <html:hidden name="markingScoreBean" property="msId"/>
    </td>     
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="markingitems.mtName"/>:</td>
    <td><html:textarea name="markingScoreBean" property="msName" rows="2" cols="60" styleClass="default_textarea"/><font color="red">*</font></td>
  </tr> 
  <td class="wordtd"><bean:message key="markingitems.fraction"/>:</td>
    <td><html:text name="markingScoreBean" property="fraction" size="62" styleClass="default_input"  onblur="onlyNumber(this)" onkeypress="f_check_number2()" /><font color="red">*</font></td>
  </tr> 
      <tr>
    <td width="20%" class="wordtd">电梯类型:</td>
   <td width="80%" id="td3">
   <html:select name="markingScoreBean" property="elevatorType">
   <html:option value="">请选择</html:option>
    <html:options collection="elevaorTypeList" property="id.pullid" labelProperty="pullname"/>
   </html:select>
 <font color="red">*</font></td>
  </tr>
   <tr>
    <td class="wordtd">排序号:</td>
    <td nowrap="nowrap">
    <input type="text" id="orderby"  name="orderby" onpropertychange="checkthisvalue(this);"  Class="default_input" value="${markingScoreBean.orderby }"/><font color="red">*（只能填数字）</font>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="markingitems.enabledflag"/>:</td>
    <td>
	  <html:radio name="markingScoreBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="markingScoreBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="markingitems.rem"/>:</td>
    <td><html:textarea name="markingScoreBean" property="rem" rows="5" cols="60" styleClass="default_textarea"/></td>
  </tr>
</table>
<br/>
<table id="table_ms" class="tb" width="70%" border="0" cellpadding="0" cellspacing="0">
    <tr height="23">
      <td colspan="3">            
        <b>评分明细</b>
      </td>
    </tr>
    
    <tr id="tr0">
      <td class="wordtd_header">评分明细代码</td>
      <td class="wordtd_header">评分明细名称<font color="red">*</font></td>
      
    </tr>
 <logic:present name="msdList">
    <logic:iterate id="ele" name="msdList">
    <tr>
    <td width="30%"><center><bean:write name="ele" property="detailId"/>
    	<html:hidden name="ele" property="detailId"/>
    </center></td>
    <td><center><html:textarea rows="2" cols="75" name="ele" property="detailName" /></center></td>
    </tr>
   </logic:iterate>
  </logic:present>  
</table>
<html:javascript formName="markingScoresForm"/>
</html:form>