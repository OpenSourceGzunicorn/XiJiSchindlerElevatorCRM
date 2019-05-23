<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<br>
<html:errors/>
<html:form action="/MaintainProjectInfoAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${id}"/>
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
    <td class="wordtd">保养类型:</td>
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
    <td><html:text size="50" property="maintItem" styleClass="default_input"/></td>
  </tr>
     <tr>
    <td class="wordtd">维保基本要求:</td>
    <td><html:textarea property="maintContents" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>

    <tr>
  <td class="wordtd">排序号:</td>
    <td><input type="text"  id="orderby" value="${MaintainProjectInfoBean.orderby}" onpropertychange="checkthisvalue(this);" name="orderby"  Class="default_input"/><font color="red">*（只能填数字）</font></td>
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
<html:javascript formName="MaintainProjectInfoForm"/>