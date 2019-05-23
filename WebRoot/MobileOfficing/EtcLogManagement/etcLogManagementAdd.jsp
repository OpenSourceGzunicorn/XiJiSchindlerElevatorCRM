<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/etcLogManagementAction.do?method=toAddRecord" >
<html:hidden property="isreturn"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd">录入人:</td>
    <td style="width: 65%">${opername }<input type="hidden" name="operid" value="${operid }"/></td>
  </tr>
  <tr>
    <td class="wordtd">录入日期:</td>
    <td style="width: 65%">${operdate }<input type="hidden" name="operdate" value="${operdate }"/></td>
 </tr>
  <tr>
    <td class="wordtd">合同号:</td>
    <td style="width: 65%"><input type="text" name="contractno" id="contractno" value=""/>&nbsp;<span style="color: red;">*</span></td>
  </tr>
  <tr>
    <td class="wordtd">项目名称:</td>
    <td style="width: 65%"><input type="text" style="width:300px" name="projectname" id="projectname" value=""/>&nbsp;<span style="color: red;">*</span></td>
 </tr>
  <tr>
    <td class="wordtd">安装单位:</td>
    <td style="width: 65%">
    <textarea rows="3" cols="40" name="inscompanyname" id="inscompanyname"></textarea>
    &nbsp;<span style="color: red;">*</span>
    </td>
  </tr>
  <tr>
    <td class="wordtd">配合人数:</td>
    <td style="width: 65%"><input type="text" name="phnum" id="phnum" value=""/>&nbsp;<span style="color: red;">*</span></td>
 </tr>
  <tr>
    <td class="wordtd">厂检/维修:</td>
    <td style="width: 65%"><input type="text" name="iscjwx" id="iscjwx" value=""/>&nbsp;<span style="color: red;">*</span></td>
  </tr>
  <tr> 
    <td class="wordtd">是否自检:</td>
    <td style="width: 65%">
    	<select name="iszj" id="iszj">
    		<option value="">请选择</option>
    		<option value="是">是</option>
    		<option value="否">否</option>
    	</select>
    	&nbsp;<span style="color: red;">*</span>
    </td>
 </tr>
  <tr>
    <td class="wordtd">现场反馈问题:</td>
    <td style="width:65%"><textarea rows="3" cols="40" name="xcfkwt" id="xcfkwt"></textarea>&nbsp;<span style="color: red;">*</span></td>
  </tr>
  <tr>
    <td class="wordtd">厂检完成情况汇总:</td>
    <td style="width: 65%"><textarea rows="3" cols="40" name="workcontent" id="workcontent"></textarea>&nbsp;<span style="color: red;">*</span></td>
 </tr>
   <tr>
    <td class="wordtd">备注:</td>
    <td style="width: 65%" colspan="2"><textarea rows="3" cols="40" name="rem" id="rem"></textarea></td>
 </tr>
 </table>
</html:form>	

