<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors/>
<html:form action="/FaultProcessAction.do?method=toUpdateRecord">
<table width="100%" class="tb">
<html:hidden property="isreturn"/>
<html:hidden property="appNo" value="${list2.appNo }"/>
<tr>
<td class="wordtd"  >报修人:</td>
<td >${list2.repairName }</td>
<td class="wordtd" >项目地址:</td>
<td width="30%">${list2.projectAddress }</td>
</tr> 

<tr>
<td class="wordtd">报修人电话:</td><td >${list2.repairPhone }</td>
<td class="wordtd">项目名称:</td><td>${list2.projectName }</td>
</tr>

<tr>
<td  class="wordtd">报修描述:</td><td >${list2.repairDesc }</td>
<td  class="wordtd">是否困人:</td><td >
<logic:equal name="list2" property="isTiring" value="Y">
是
</logic:equal>
<logic:equal name="list2" property="isTiring" value="N">
否
</logic:equal>
</td>
</tr>

<tr>
<td  class="wordtd">录入人:</td><td >${list2.operId }</td>
<td  class="wordtd">录入日期:</td><td >${list2.operDate }</td>
</tr>

<tr>
<td  class="wordtd">是否处理:</td><td >
<logic:equal name="list2" property="isProcess" value="Y">
是
</logic:equal>
<logic:notEqual name="list2" property="isProcess" value="Y">
否
</logic:notEqual>
</td>
<td class="wordtd"></td><td ></td>
</tr>

<tr>
<td  class="wordtd">处理人:</td><td >${list2.dealId }</td>
<td  class="wordtd">处理日期:</td><td >${list2.dealDate }</td>
</tr>

<tr>
<td  class="wordtd">备注:</td><td colspan="3"><html:textarea rows="5" cols="50" property="rem" value="${list2.rem }"/></td>
</tr>
</table>
</html:form>