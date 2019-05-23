<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<br>
<html:errors/>
<html:form action="/handoverElevatorCheckItemAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="handoverelevatorcheckitem.examType"/>:</td>
    <td>
	<html:select property="examType">
	  <html:option value=""> <bean:message key="XJSCRM.zhz" /></html:option>
      <html:options collection="examTypeList" property="id.pullid" labelProperty="pullname"/>
	</html:select><font color="red">*</font>
	</td> 
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="handoverelevatorcheckitem.checkItem"/>:</td>
    <td><html:text property="checkItem" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="handoverelevatorcheckitem.issueCoding"/>:</td>
    <td><html:text property="issueCoding" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="handoverelevatorcheckitem.issueContents"/>:</td>
    <td><html:textarea property="issueContents" rows="2" cols="50" styleClass="default_textarea"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd">电梯类型:</td>
    <td>
	  <html:select property="elevatorType">
	    <html:option value="">请选择</html:option>
	  <html:options collection="elevatorTypeList" property="id.pullid" labelProperty="pullname"/>  
	  </html:select>
	  <font color="red">*</font>
	</td>
  </tr>
   <tr>
    <td class="wordtd">排序号:</td>
    <td nowrap="nowrap"><input type="text" id="orderby"  name="orderby" onpropertychange="checkthisvalue(this);"  Class="default_input"/><font color="red">*（只能填数字）</font></td>
  </tr>
   <tr>
    <td class="wordtd">小组编号:</td>
    <td><html:text property="itemgroup" styleClass="default_input"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="handoverelevatorcheckitem.enabledFlag"/>:</td>
    <td>
	  <html:radio property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="handoverelevatorcheckitem.rem"/>:</td>
    <td><html:textarea property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="handoverElevatorCheckItemForm"/>
