<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  //AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  AddToolBarItemEx("ReturnTkBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="button.returntask"/>',"110","0","returnTaskMethod()");	
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nmyTaskOA" value="Y"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="button.submit"/>',"65","1","saveMethod()");
  </logic:equal>
  </logic:notPresent>
  
  AddToolBarItemEx("ViewFlow","../../common/images/toolbar/viewdetail.gif","","",'<bean:message key="toolbar.viewflow"/>',"110","1","viewFlow()");
  
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//去掉空格
String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g,""); }

//返回
/* function returnMethod(){
  window.location = '<html:rewrite page="/maintContractQuoteSearchAction.do"/>?method=toSearchRecord';
} */

//返回代办列表
function returnTaskMethod(){
	window.location ='<html:rewrite page="/myTaskOaSearchAction.do"/>?method=toDoList&jumpto=templetdoorapp';
}

//提交
function saveMethod(){
	if(document.getElementById("approveResult").value!="驳回到厂检员"){
		var flags=getSelectValue();
		var taskName=document.getElementById("taskName").value
		if(taskName=="厂检部长审核"){
			var isDelete=document.getElementsByName("isDelete");
			var deleteRem=document.getElementsByName("deleteRem");
			for(var i=0;i<isDelete.length;i++){
				if(isDelete[i].value=="Y"){
					if(deleteRem[i].value==""||deleteRem[i].value==null){
						alert("必须填写删除原因");
						return;
					}
				}
				
			}
		}
		
		if(!checkColumnInput(elevatorTransferCaseRegisterJbpmForm)){
			return;
		}else{
			if(flags!="" && flags!="1"){
				alert("抱歉,您选择了'不同意',请在审批意见栏上填写否决的意见!");
				return;
			}
		}
		/* alert(document.getElementById("deductMoney").value); */
		if(document.getElementById("deductMoney")==null){
			document.getElementById("SaveBtn").disabled=true;
			document.elevatorTransferCaseRegisterJbpmForm.submit();
		}else{
			if(!isNaN(document.getElementById("deductMoney").value)){
				document.getElementById("SaveBtn").disabled=true;
				document.elevatorTransferCaseRegisterJbpmForm.submit();
			}else{
				alert("扣款金额只能输入数字！");
			}
			
		}
	}else{
		var approveRem=document.getElementById("approveRem").value;
		if(approveRem.trim()==""){
			alert("您选择了'驳回到厂检员', 请输入审批意见！");
		}else{
			document.getElementById("SaveBtn").disabled=true;
			document.elevatorTransferCaseRegisterJbpmForm.submit();
		}
	}
}

//查看流程
function viewFlow(){	
	var hiddenReturnStatus=document.getElementById("status");
	if(hiddenReturnStatus.value=="-1"){
		alert("流程未启动，无法查看审批流程图！");
	}else{
		var avf=document.getElementById("avf");
		var tokenid=document.getElementById("tokenid");
		var flowname=document.getElementById("flowname");
		if(tokenid!=null && tokenid.value!=""){
			if(avf!=null && tokenid!=null && flowname!=null){
				avf.href='<html:rewrite page="/viewProcessAction.do"/>?method=toViewProcess&tokenid='+tokenid.value+'&flowname='+flowname.value;
				avf.click();
			}else{
				alert("请选择一条要查看的记录！");
			}
		}else{
			alert("该记录为历史数据，无法查看审批流程图！");
		}
	}
}

//取select的文本值
function getSelectValue(){
	var str="0";
	var obj=document.getElementById("approveResult");

 	var index=obj.selectedIndex; //序号，取当前选中选项的序号	
 	var val = obj.options[index].text;
 	if(val!="" && val.indexOf("不")<=-1){
 	    str="1";
 	}else{
 		if(document.getElementById("approveRem").value.trim()!="" || document.getElementById("approveRem").value.trim().length>0){
 			str="1";
 		}
 	}
 	return str;
	
}

//是否显示扣款金额输入框
function isDeductions(object,is){
	var flag="";
	val=document.getElementById("approveResult").value;
	if(val!="" && (val=="同意" || val.indexOf("提交")>-1)){
		flag="1"
	}
	//var tr=object.parentNode.parentNode;
	var td=document.getElementById("td1");
	if(flag=="1" && is=="Y"){
		td.innerHTML="<input type=\'text\' name=\'deductMoney\' id=\'deductMoney\' onkeypress=\'f_check_number3()\'  /><font color=\'red\'>*</font>"
	}else{
		td.innerHTML="";
	}
}

//检查金额是否为数字,不可以输入负号和可以输入点号
function f_check_number3(){
 	if((event.keyCode>=48 && event.keyCode<=57) || event.keyCode==46 ){
 	}else{
		event.keyCode=0;
  	}
}

function checkthisvalue(obj){  
	var objv=obj.value.substring(0,obj.value.length-1);
	if(isNaN(obj.value)){
		obj.value=0;
	}
}

//下载附件
function downloadFile(name,oldName,filePath){
	var uri = '<html:rewrite page="/elevatorTransferCaseRegisterJbpmAction.do"/>?method=toDownloadFileDispose';
	var name1=encodeURI(name);
	name1=encodeURI(name1);
	var oldName1=encodeURI(oldName);
	oldName1=encodeURI(oldName1);
	filePath=encodeURI(filePath);
	filePath=encodeURI(filePath);
	    uri +='&filePath='+ filePath;
		uri +='&filesname='+ name1;
		uri +='&folder=HandoverElevatorCheckItemRegister.file.upload.folder';
		uri+='&fileOldName='+oldName1;
	window.location = uri;
	//window.open(url);
}
//删除
var tbs;
function deleteFile(td,id,iswbfile){
  tbs=td;
    if(confirm("确定删除吗")){
    	tbs.parentElement.parentElement.style.display='';
		var uri = '<html:rewrite page="/elevatorTransferCaseRegisterJbpmAction.do"/>?method=toDeleteFileDispose';
		uri +='&filesid='+ id;
		uri +='&folder=HandoverElevatorCheckItemRegister.file.upload.folder';
		uri +='&iswbfile='+iswbfile
		sendRequestDelFile(uri);  	
	}
}

var XMLHttpReq = false;
//创建XMLHttpRequest对象       
function createXMLHttpRequest() {
	if(window.XMLHttpRequest) { //Mozilla 浏览器
		XMLHttpReq = new XMLHttpRequest();
	}else if (window.ActiveXObject) { // IE浏览器
		XMLHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
	}
}
//发送请求函数
function sendRequestDelFile(url) {
	createXMLHttpRequest();
	XMLHttpReq.open("post", url, true);
	XMLHttpReq.onreadystatechange = processResponseDelFile;//指定响应函数
	XMLHttpReq.send(null);  // 发送请求
}
// 处理返回信息函数

  function processResponseDelFile() {
   	if (XMLHttpReq.readyState == 4) { // 判断对象状态
       	if (XMLHttpReq.status == 200) { // 信息已经成功返回，开始处理信息        	
       	
          	var res=XMLHttpReq.responseXML.getElementsByTagName("res")[0].firstChild.data;
          	
          	//document.getElementById("messagestring").innerHTML=res;
          	if(res=="Y"){
          		tbs.parentElement.parentElement.parentElement.removeChild(tbs.parentElement.parentElement);
          		isQualified('${elevatorTransferCaseRegisterBean.elevatorType}',${elevatorTransferCaseRegisterBean.checkNum});
          	}else{
          		alert("删除失败！");
          	}
          	
          	//alert(document.getElementById("messagestring").innerHTML+";123");
       	} else { //页面不正常
             window.alert("您所请求的页面有异常。");
       	}
     }
  }

//删除行
function deleteRow(i){
    var $tr=$("#"+i);
    $("#"+i).remove()
    $("#party_b").show();
	var booleam=$("#"+i+" td:eq(1)").css("color")=="red";
    if(booleam){
    	$("#party_b").append($tr);
    }else
    {
    	var my_element=$("#party_b .red").length > 0;
    	if($("#party_b .red").length > 0){
    		$("#party_b .red:first").parent().before($tr);
    	}else{
    		$("#party_b").append($tr);
    	}
    }
    $("#"+i+" td").eq($("#"+i+" td").length-4).show();
    $("#"+i+" td").eq($("#"+i+" td").length-3).show();
    var td="<input type=\"button\" value=\"删除\" onclick=\"returnDeleteRow(\'"+i+"\')\"><input type=\"hidden\" name=\"isDelete\" value=\"Y\">";
    $("#"+i+" td:eq(0)").html(td);
    var $color=$("#"+i+" td:eq(1)").css("color");
    var et=document.getElementById("elevatorType").value;
    var cn=document.getElementById("checkNum").value;
    isQualified(et,cn);
}

//返回删除行
function returnDeleteRow(i){
    var $tr=$("#"+i);
    $("#"+i).remove()
    
    //$("#party_a").show();
    var booleam=$("#"+i+" td:eq(1)").css("color")=="red";
    if(booleam){
    	$("#party_a").append($tr);
    }else
    {
    	var my_element=$("#party_a .red").length > 0;
    	if($("#party_a .red").length > 0){
    		$("#party_a .red:first").parent().before($tr);
    	}else{
    		$("#party_a").append($tr);
    	}
    }
    //$("#"+i+" td:last").hide();
    $("#"+i+" td").eq($("#"+i+" td").length-4).hide();
    $("#"+i+" td").eq($("#"+i+" td").length-3).hide();
    var td="<input type=\"button\" value=\"删除\" onclick=\"deleteRow(\'"+i+"\')\"><input type=\"hidden\" name=\"isDelete\" value=\"N\">";
    $("#"+i+" td:eq(0)").html(td);
    var et=document.getElementById("elevatorType").value;
    var cn=document.getElementById("checkNum").value;
    isQualified(et,cn);
}

/**
扶梯计算公式：
	======扶梯初检======
	1，有任一重大不合格项则为不合格。
	2，没有重大不合格项，但一般不合格项 大于八项的，也为不合格。
	3，没有重大不合格项，且一般不合格项小于等于八项的为初检合格。
	======扶梯复检（包括2次、3次、4次。。。）去掉漏检项======
	1，没有重大不合格项，且一般不合格项小于等于三项的为复检合格。

直梯计算公式： 根据小组编码进行不合格数量统计，相同的小组编码就只表示一个不合格项。
	======直梯初检======
	1，有任一重大不合格项则为不合格。
	2，没有重大不合格项，但 一般不合格项 大于18项的，也为不合格。
	3，没有重大不合格项，且一般不合格项小于等于18项的为初检合格。
	======直梯复检（包括2次、3次、4次。。。）去掉漏检项======
	1，没有重大不合格项，且一般不合格项小于等于8项的为复检合格。
	
厂检分数===计算方法： 根据计算合格不合格的重大项和一般项个数 （满分一百分）-扣除分数
	直梯：一般项一次扣2分，重大项一次扣10分。
	扶梯：一般项一次扣5分，重大项一次扣20分
*/
function isQualified(elevatorType,checkNum){
	var table=document.getElementById("party_a");
	var len=table.rows.length-2;
	var zNum=0;
	var yNum=0;
	var cjfs=0;
	var strarr="";
	
	var firstnum=0;
	if(checkNum>1){
		//复检
		for(var i=0;i<len;i++){
			if(document.getElementsByName("isRecheck")[i].value=="Y"){
				var isyzg=document.getElementsByName("isyzg")[i].value;//是否已整改
				var type="";
				if(document.getElementsByName("examType")[i]!=null){
					type=document.getElementsByName("examType")[i].value;
				}
				//重大不合格项
				if(type=="ZD"){
					if(isyzg=="Y"){
						firstnum++;
					}else{
						zNum+=1;
					}
				}
				//一般不合格项
				if(type=="YB"){
					if(elevatorType=="T"){
						//直梯 根据小组编码进行不合格数量统计，相同的小组编码就只表示一个不合格项。
						var itemgroup=document.getElementsByName("itemgroup")[i].value;
						if(itemgroup!="" && strarr.indexOf(itemgroup+",")>-1){
							
						}else{
							if(isyzg=="Y"){
								firstnum++;
							}else{
								strarr+=itemgroup+",";
								yNum+=1;
							}
						}
					}else{
						//扶梯
						if(isyzg=="Y"){
							firstnum++;
						}else{
							yNum+=1;
						}
					}
				}
			}
			
		}
	}else{
		//初检
		for(var i=0;i<len;i++){
			var isyzg=document.getElementsByName("isyzg")[i].value;//是否已整改
			var type="";
			if(document.getElementsByName("examType")[i]!=null){
				type=document.getElementsByName("examType")[i].value;
			}
			//重大不合格项
			if(type=="ZD"){
				if(isyzg=="Y"){
					firstnum++;
				}else{
					zNum+=1;
				}
			}
			//一般不合格项
			if(type=="YB"){
				if(elevatorType=="T"){
					//直梯 根据小组编码，进行数量统计
					var itemgroup=document.getElementsByName("itemgroup")[i].value;
					if(itemgroup!="" && strarr.indexOf(itemgroup+",")>-1){
						
					}else{
						if(isyzg=="Y"){
							firstnum++;
						}else{
							strarr+=itemgroup+",";
							yNum+=1;
						}
					}
				}else{
					//扶梯
					if(isyzg=="Y"){
						firstnum++;
					}else{
						yNum+=1;
					}
				}
			}
		}
        
	}
	if(checkNum>1){
		//复检
		if(elevatorType=="T"){
			if(zNum>=1 || yNum>8){
				document.getElementById("factoryCheckResult").value="不合格";
			}else{
				document.getElementById("factoryCheckResult").value="合格";
			}
			//直梯：重大项一次扣10分,一般项一次扣2分。【厂检部长审核，2018-06-04 需要修改厂检分数】
			cjfs=(zNum*10)+(yNum*2);
			document.getElementById("r2").value=100-cjfs;
		}
		if(elevatorType=="F"){
			if(zNum>=1 || yNum>3){
				document.getElementById("factoryCheckResult").value="不合格";
			}else{
				document.getElementById("factoryCheckResult").value="合格";
			}
			//扶梯：重大项一次扣20分,一般项一次扣5分。【厂检部长审核，2018-06-04 需要修改厂检分数】
			cjfs=(zNum*20)+(yNum*5);
			document.getElementById("r2").value=100-cjfs;
		}
	}else{
		//初检
		if(elevatorType=="T"){
			if(zNum>=1 || yNum>18){
				document.getElementById("factoryCheckResult").value="不合格";
			}else{
				document.getElementById("factoryCheckResult").value="合格";
			}
			//直梯：重大项一次扣10分,一般项一次扣2分。【厂检部长审核，2018-06-04 需要修改厂检分数】
			cjfs=(zNum*10)+(yNum*2);
			document.getElementById("r2").value=100-cjfs;
		}
		if(elevatorType=="F"){
			if(zNum>=1 || yNum>8){
				document.getElementById("factoryCheckResult").value="不合格";
			}else{
				document.getElementById("factoryCheckResult").value="合格";
			}
			//扶梯：重大项一次扣20分,一般项一次扣5分。【厂检部长审核，2018-06-04 需要修改厂检分数】
			cjfs=(zNum*20)+(yNum*5);
			document.getElementById("r2").value=100-cjfs;
		}
	}
	
	//赋值给厂检结果2
	var factoryCheckResult=document.getElementById("factoryCheckResult").value;
	var factoryCheckResult2=document.getElementById("factoryCheckResult2");
	factoryCheckResult2.value=factoryCheckResult;
	if(factoryCheckResult=="合格"){
		factoryCheckResult2.value="监改合格";
	}
	
}

//删除行
function deleteFileRow(thisobj){
	var tableObj = getFirstSpecificParentNode("table",thisobj);
	
	var inputs = tableObj.getElementsByTagName("input");
	var checkboxs = new Array(); //table的所有checkbox
	for(var i=0;i<inputs.length;i++){
	    if(inputs[i].type=="checkbox"){
	    	checkboxs.push(inputs[i]);	      	
	    }
	}
	
	checkboxs[0].checked = false;//全选按钮取消选中 
	
	//在table中从下至上删除选中的行
	for(var i=checkboxs.length-1;i>0;i--){	
		if(checkboxs[i].checked == true){
		  tableObj.deleteRow(getFirstSpecificParentNode("tr",checkboxs[i]).rowIndex);
		}				
	}
}

var fileGird=0;
function AddRow(uploadtab_2,value,checkItem,examType,issueCoding){
	var upload=uploadtab_2.parentNode.parentNode.parentNode;
	var num=upload.rows.length;
	upload.insertRow(num);
	var ince0=upload.rows(num).insertCell(0);
	ince0.align="center";
	ince0.name="fileNo_"+fileGird
	ince0.innerHTML="<input type='checkbox' name='nodes' onclick=\'cancelCheckAll(this)\'>";
	var ince1=upload.rows(num).insertCell(1);
	ince1.innerHTML="<input id=\'file_"+fileGird+"\' name=\'"+fileGird+"\' type=\'file\' class=\'default_input\' />"
	+"<input type=\'hidden\' name=\'isdelete_"+fileGird+"\' value=\'"+value+"\' />"
	+"<input type=\'hidden\' name=\'item_"+fileGird+"\' value=\'"+checkItem+"\' />"
	+"<input type=\'hidden\' name=\'type_"+fileGird+"\' value=\'"+examType+"\' />"
	+"<input type=\'hidden\' name=\'coding_"+fileGird+"\' value=\'"+issueCoding+"\' />";
	
	fileGird++;
}

function AddRow2(uploadtab_2){
	var upload=uploadtab_2.parentNode.parentNode.parentNode;
	var num=upload.rows.length;
	upload.insertRow(num);
	var ince0=upload.rows(num).insertCell(0);
	ince0.align="center";
	ince0.name="fileNo_"+fileGird
	ince0.innerHTML="<input type='checkbox' name='nodes' onclick=\'cancelCheckAll(this)\'>";
	var ince1=upload.rows(num).insertCell(1);
	ince1.innerHTML="<input id=\'file_"+fileGird+"\' name=\'"+fileGird+"\' type=\'file\' size=\'50\' class=\'default_input\' />"
	+"<input type=\'hidden\' name=\'primary_"+fileGird+"\' value=\'Wbfileinfo\' />"
	+"<input type=\'hidden\' name=\'item_"+fileGird+"\' value=\'Wbfileinfo\' />"
	+"<input type=\'hidden\' name=\'type_"+fileGird+"\' value=\'Wbfileinfo\' />"
	+"<input type=\'hidden\' name=\'coding_"+fileGird+"\' value=\'Wbfileinfo\' />";
	
	fileGird++;
}
//列表全选反选
function checkTableFileAll(thisobj){
	var tableObj = getFirstSpecificParentNode("table",thisobj);
	var rows = tableObj.rows
	for(var i=1;i<rows.length;i++){
	  var inputs = rows[i].cells[0].getElementsByTagName("input");
	  for(j=0;j<inputs.length;j++){
	    if(inputs[j].type=="checkbox"){
	      inputs[j].checked = thisobj.checked;
	    }
	  }
	}
}

//全选checkbox取消选中
function cancelCheckAll(thisobj){ 
  var tableObj = getFirstSpecificParentNode("table",thisobj);
  var inputs = tableObj.getElementsByTagName("input");  
  if(thisobj.checked==false){
    for(var j=0;j<inputs.length;j++){
	  if(inputs[j].type=="checkbox"){
	    inputs[j].checked = false; //table中第一个checkbox取消选中
	    break;
	  }
	}
  }
}


</script>

  <tr>
    <td width="100%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="22" class="table_outline3" valign="bottom" width="100%">
            <div id="toolbar" align="center">
            <script language="javascript">
              CreateToolBar();
            </script>
            </div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>