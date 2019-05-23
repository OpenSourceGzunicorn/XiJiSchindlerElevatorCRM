<%@page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<link href="/common/DatePicker/开发包/skin/WdatePicker.css"  type="text/style+css">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/math.js"/>"></script>
<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<table width="99%" class="tb"  height="25">
		<tr>
			<td  align="center" width="90%"><b>主表单信息</b></td>
		</tr>
</table>
<table  width="99%" border="0" cellpadding="0" cellspacing="0" class="tb" >
<tr>
    <td  class="wordtd_a" width="10%">报修单号:</td>
    <td  width="23%">
    <input type="text" name="calloutMasterNo" id="calloutMasterNo"  value="${CalloutMasterList.calloutMasterNo }" readonly class="default_input_noborder"/>
    </td>
    <td class="wordtd_a"  width="10%">录入人:</td>
    <td  width="23%">
     <input type="text" name="operId" id="operId"  value="${CalloutMasterList.operId }" readonly class="default_input_noborder"/>
    </td>
    <td class="wordtd_a"  width="10%">报修时间:</td>
    <td  width="23%">
    <input type="text" name="repairTime" id="repairTime"  value="${CalloutMasterList.repairTime }" readonly class="default_input_noborder">
    </td>
</tr>
<tr>
    <td class="wordtd_a" >报修方式:</td>
    <td >
    <html:select property="repairMode" styleId="repairMode">
     <html:options collection="rmList" property="id.pullid" labelProperty="pullname"></html:options>
     </html:select><font color="red">*</font>
    </td>
    <td class="wordtd_a" >报修人:</td>
    <td >
    <div id="bxp">
     <input type="text" name="repairUser" id="repairUser" value="${CalloutMasterList.repairUser }" class="default_input"><font color="red">*</font>
    </div>
    </td>
    <td class="wordtd_a" >报修电话:</td>
    <td >
    <div id="bxt">
    <input type="text" name="repairTel" id="repairTel" value="${CalloutMasterList.repairTel }" ><font color="red">*</font>
    </div>
    </td> 
</tr>
<tr>
    <td class="wordtd_a" >服务对象:</td>
    <td >
    <html:select property="serviceObjects" styleId="serviceObjects">
     <html:options collection="soList" property="id.pullid" labelProperty="pullname"></html:options>
     </html:select>
    <font color="red">*</font>
    </td>
    <td class="wordtd_a" >报修单位:</td>
    <td  nowrap>
     <input type="hidden" name="companyId"  id="companyId" value="${CalloutMasterList.companyId }"/>
     <input type="text" name="companyName"  id="companyName" value="${hashmapbean.companyName }" size="25" readonly >
    <input type="button"  value=".." id="baox" class="default_input"/><font color="red">*</font>
    </td>
    <td class="wordtd_a" >项目名称:</td>
    <td >
    <input type="text" name="projectName" id="projectName" size="30" value="${CalloutMasterList.projectName }" readonly class="default_input_noborder">
    </td>
</tr>
<tr>
    <td class="wordtd_a" >销售合同号:</td>
    <td >
     <input type="text" name="salesContractNo" id="salesContractNo"  value="${CalloutMasterList.salesContractNo }" readonly class="default_input_noborder"> 
    </td>
    <td class="wordtd_a" >电梯编号:</td>
    <td >
     <input type="text" name="elevatorNo" id="elevatorNo"  value="${CalloutMasterList.elevatorNo }" readonly class="default_input_noborder">
    </td>
    <td class="wordtd_a" >规格型号:</td>
    <td >
    <input type="text" name="elevatorParam" id="elevatorParam" value="${CalloutMasterList.elevatorParam }" readonly class="default_input_noborder">
    </td>
</tr>
<tr>
    <td class="wordtd_a" >维保站:</td>
    <td >
     <%-- input type="text" name="storageName" id="storageName"  value="${hashmapbean.storageName }"readonly class="default_input_noborder"--%>
     <input type="hidden" name="maintStation" id="storageId" value="${CalloutMasterList.maintStation }"/> 
     <html:select property="assignMaintStation" styleId="assignstorageId" onchange="Evenmore(this.value);">
	     <html:option value="">请选择</html:option>
     </html:select><font color="red">*</font>
    </td>
    <td class="wordtd_a" >派工对象:</td>
    <td>
    	<input type="hidden" name="maintPersonnel" id="maintPersonnel" value="${CalloutMasterList.assignObject }">
	     <html:select property="assignObject" styleId="assignObject" onchange="changeuser(this);">
		     <html:option value="">请选择</html:option>
	     </html:select><font color="red">*</font>
    </td>
    <td class="wordtd_a" >电话:</td>
    <td >
    <input type="text" name="phone" id="phone" value="${CalloutMasterList.phone }" readonly class="default_input_noborder">
    </td>
</tr>
<tr>
	<td class="wordtd_a" >项目名称及楼栋号:</td>
    <td >
    <input type="text" name="projectAddress" id="address" size="30" value="${CalloutMasterList.projectAddress }" readonly class="default_input_noborder">
    </td>
    <td class="wordtd_a" >是否困人:</td>
    <td>
     <input type="radio" name="isTrap" id="isTrap" value="N" checked="checked">非困人
     <input type="radio" name="isTrap" id="isTrap" value="Y" >困人
    </td>
    <td class="wordtd_a">是否发送安抚短信:</td>
    <td colspan="3">
     	<input type="radio" name="isSendSms2" value="Y" >是
    	<input type="radio" name="isSendSms2" value="N" checked="checked">否
    	<%-- <html:select name="CalloutMasterList" property="isSendSms2">
    		<html:option value="Y">是</html:option>
    		<html:option value="N">否</html:option>
    	</html:select> --%>
    </td>
 </tr>
 <tr>   
    <td class="wordtd_a" >报修描述:</td>
    <td  colspan="5">
     <textarea rows="3" cols="80" name="repairDesc" id="repairDesc" >${CalloutMasterList.repairDesc }</textarea>
    </td>
</tr>
</table>
<%@ include file="calloutMasterAdd2.jsp" %>
<table width="99%" class="tb"  height="25">
		<tr>
			<td  align="center" width="90%"><b>回访用户信息</b></td>
		</tr>
</table>
<table  width="99%" border="0" cellpadding="0" cellspacing="0" class="tb" >
<tr>
    <!-- <td class="wordtd_a" width="10%">故障分类:</td>
    <td width="23%">

    </td> -->
    <td class="wordtd_a" width="10%">回访用户服务评价:</td>
    <td width="23%">

    </td>
    <td class="wordtd_a" width="10%">回访配件更换情况:</td>
    <td width="23%">

    </td>
    <td class="wordtd_a" width="10%"></td>
    <td width="23%">

    </td>
</tr>
<tr>
    <td class="wordtd_a" width="10%">回访收费情况:</td>
    <td width="23%" colspan="5">
 
    </td>   
</tr>
<tr>
    <td class="wordtd_a" width="10%">是否关闭回召:</td>
    <td width="23%" colspan="5">
  
    </td>
</tr>
<tr>
<td class="wordtd_a" width="10%">回访备注:</td>
    <td colspan="5">

    </td>
</tr>
</table>
<script type="text/javascript">
$("document").ready(function (){	
	var isTraps=$("input[name=isTrap]");
	setradiovalue(isTraps,'${CalloutMasterList.isTrap }');
	$("#repairMode").val('${CalloutMasterList.repairMode }')
	$("#serviceObjects").val('${CalloutMasterList.serviceObjects }')
	if($("#repairMode").val()==2){
		$("#bxp").empty();
		$("#bxt").empty();
	}
	if($("#repairMode").val()==1){
		$("#bxp").html("<input type='text' name='repairUser' id='repairUser' value='${CalloutMasterList.repairUser }'><font color='red'>*</font>")
		$("#bxt").html("<input type='text' name='repairTel' id='repairTel' value='${CalloutMasterList.repairTel }'><font color='red'>*</font>")	
	}
	
});

function setradiovalue(obj,value){
	for(var i=0;i<obj.length;i++){
		if(obj[i].value==value){
			obj[i].checked="checked";
			return;
		}
	}
}
function checkvalue(obj){
	var isPhone = /^0\d{2,3}-?\d{7,8}$/;//座机
	var isMob = /^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;//手机
	if(isPhone.test(obj.value) ||isMob.test(obj.value)){
		return true;
	}else{
		alert("请正确的电话号码");
		obj.value="";
		obj.focus();
		return false;
	}
}
$("#repairMode").change(function(){
	if($(this).val()==2){
		$("#bxp").empty();
		$("#bxt").empty();
	}
	if($(this).val()==1){
		$("#bxp").html("<input type='text' name='repairUser' id='repairUser' value='${CalloutMasterList.repairUser }'><font color='red'>*</font>")
		$("#bxt").html("<input type='text' name='repairTel' id='repairTel' value='${CalloutMasterList.repairTel }'><font color='red'>*</font>")	
	}
});

$("#baox").click(function(){

    var array=openWindowWithParams("hotphonexdwSearchAction","toSearchRecord2","");
    toSetInputValue(array,document.getElementsByTagName("form")[0].name)

    var companyId=document.getElementById("companyId").value;
    //设置下拉框的值 判断维保站是否存在。
    if(companyId!=""){
        var storageid=document.getElementById("storageId").value;
    	showstorageid(storageid);
    	Evenmore(storageid);
    }
    
});

//判断维保站是否存在。
var storageid=document.getElementById("storageId").value;
if(storageid!=""){
	showstorageid(storageid);
	Evenmore(storageid);
}

</script>



