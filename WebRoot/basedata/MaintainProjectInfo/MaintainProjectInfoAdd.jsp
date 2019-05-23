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
<html:form action="/MaintainProjectInfoAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">

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
   <td class="wordtd"> 保养类型:</td>
    <td>
	<html:select property="maintType">
	  <html:option value="">请选择</html:option>
	  <html:options collection="maintTypeList" property="id.pullid" labelProperty="pullname"/>  
	  </html:select>
<font color="red">*</font>
	</td> 
  </tr>
  <tr>
    <td class="wordtd">维保项目:</td>
    <td><html:text size="50" property="maintItem" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd">维保基本要求:</td>
    <td><html:textarea property="maintContents" rows="5" cols="50" styleClass="default_textarea"/><font color="red">*</font></td>
  </tr>
 
   <tr>
    <td class="wordtd">排序号:</td>
    <td nowrap="nowrap"><input type="text" id="orderby"  name="orderby" onpropertychange="checkthisvalue(this);"  Class="default_input"/><font color="red">*（只能填数字）</font></td>
  </tr>
  <tr>
    <td class="wordtd">启用标志:</td>
    <td>
	  <html:radio property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd">备注:</td>
    <td><html:textarea property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="MaintainProjectInfoForm"/>
