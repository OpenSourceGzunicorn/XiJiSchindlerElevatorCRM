<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script type="text/javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<br>
<html:errors/>
<html:form action="/qcLogManagementAction.do?method=toAddRecord" >
<html:hidden property="isreturn"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd">录入人:</td>
    <td style="width: 35%">${opername }<input type="hidden" name="operid" value="${operid }"/></td>
  </tr>
  <tr>
    <td class="wordtd">录入日期:</td>
    <td style="width: 35%">${operdate }<input type="hidden" name="operdate" value="${operdate }"/></td>
 </tr>
  <tr>
    <td class="wordtd">维保站:</td>
    <td style="width: 35%">
    	<html:select property="maintStation" styleId="maintStation" onchange="setMaintPersonnel(this)">
    		<html:option value="">请选择</html:option>
    		<html:options collection="maintStationList" property="storageid" labelProperty="storagename"/>
    	</html:select>
    	&nbsp;<span style="color: red;">*</span>
    </td>
  </tr>
  <tr>
    <td class="wordtd">维保工:</td>
    <td style="width: 35%">
    	<textarea id="MaintPersonnel" name="MaintPersonnel" rows="4" cols="40" readonly="true"></textarea>
	    <input type="button" id="selbtn" value="请选择" onclick="tckselect();" class="default_input" disabled="disabled"/>
	    &nbsp;
   		<input type="button" value="清 空" onclick="delselect();" class="default_input"/>
   		&nbsp;<span style="color: red;">*</span>
    </td>
 </tr>
  <tr>
    <td class="wordtd">电梯编号:</td>
    <td style="width: 65%"><input type="text" name="ElevatorNo" id="ElevatorNo" value=""/>&nbsp;<span style="color: red;">*</span></td>
 </tr>
  <tr>
    <td class="wordtd">月度例会:</td>
    <td style="width: 65%">
    	<select name="ydlh" id="ydlh">
    		<option value="">请选择</option>
    		<option value="有">有</option>
    		<option value="无">无</option>
    	</select>
    	&nbsp;<span style="color: red;">*</span>
    </td>
 </tr>
  <tr>
    <td class="wordtd">维保负责人是否对工作负责:</td>
    <td style="width: 65%">
    	<select name="isgzfz" id="isgzfz">
    		<option value="">请选择</option>
    		<option value="是">是</option>
    		<option value="否">否</option>
    	</select>
    	&nbsp;<span style="color: red;">*</span>
    </td>
  </tr>
  <tr>
    <td class="wordtd">维保负责人是否存在作风问题:</td>
    <td style="width: 65%">
    	<select name="iszfwt" id="iszfwt">
    		<option value="">请选择</option>
    		<option value="是">是</option>
    		<option value="否">否</option>
    	</select>
    	&nbsp;<span style="color: red;">*</span>
    </td>
 </tr>
  <tr>
    <td class="wordtd">甲方反馈问题:</td>
    <td style="width: 65%"><textarea rows="3" cols="40" name="jffkwt" id="jffkwt"></textarea>&nbsp;<span style="color: red;">*</span></td>
  </tr>
  <tr>
    <td class="wordtd">远程监控问题:</td>
    <td style="width: 65%"><textarea rows="3" cols="40" name="ycjkwt" id="ycjkwt"></textarea>&nbsp;<span style="color: red;">*</span></td>
 </tr>
   <tr>
    <td class="wordtd">备注:</td>
    <td style="width: 65%" colspan="3"><textarea rows="3" cols="40" name="rem" id="rem"></textarea></td>
 </tr>
 </table>
</html:form>

<script>
	function setMaintPersonnel(obj){
		var MaintPersonnel = obj.value;
		var selbtn = document.getElementById("selbtn");
		if(MaintPersonnel!= null && MaintPersonnel != ""){
			selbtn.disabled = false;
		}
		
	}
	
	function tckselect(){
		var maintStation=document.getElementById("maintStation").value;
		var returnvalue = openWindowWithParams2('qcLogManagementSearchAction','toGetMaintPersonnel','maintStation='+maintStation,'')
		toSetInputValue5(returnvalue,'');
	}
	
	function delselect(){
		document.getElementById("MaintPersonnel").value="";
	}
	
	//不可滚动
	function openWindowWithParams2(actionName,method,paramstring){
	var projectName = window.location.pathname.replace(window.location.pathname.replace(/\s*\/+.*\/+/g,''),'');
	var url="query/Search.jsp?path="+encodeURIComponent(projectName+actionName+".do?method="+method+"&"+paramstring);
	return window.showModalDialog(url,window,'dialogWidth:970px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes;scroll:no');
}

</script>