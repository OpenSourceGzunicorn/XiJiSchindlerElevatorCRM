<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>

<script language="javascript" charset="GBK">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  		AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nContractTransferAssign" value="Y"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
    AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="tollbar.saveandrefer"/>',"120","1","saveReturnMethod()");
  </logic:equal>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}

//去掉空格
String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g,"");}

//关闭
function closeMethod(){
  window.close();
}

//返回
function returnMethod(){
  window.location = '<html:rewrite page="/ContractTransferAssignSearchAction.do"/>?method=${returnMethod}';
}

//保存
function saveMethod(){
	 var filetypes = document.getElementsByName("filetypechoose");
	 if(document.getElementsByName("elevatorno")[0]==null){
		 alert("尚未选择电梯！")
		 return;
	 }
	 
	 var checkflag="N";
	 for(var i=0;i<filetypes.length;i++){
		 if(filetypes[i].checked==true){
			checkflag="Y";
			break; 
		 }
	 }
	 if(checkflag=="N"){
		 alert("附件类型必须选一个！")
	 }else{
		   		document.ContractTransferAssignForm.isreturn.value = "N";
		   		document.ContractTransferAssignForm.submit();
	 }
		
}

//保存返回
function saveReturnMethod(){
	var filetypes = document.getElementsByName("filetypechoose");
	 if(document.getElementsByName("elevatorno")[0]==null){
		 alert("尚未选择电梯！")
		 return;
	 }
	 
	 var checkflag="N";
	 for(var i=0;i<filetypes.length;i++){
		 if(filetypes[i].checked==true){
			checkflag="Y";
			break; 
		 }
	 }
	 if(checkflag=="N"){
		 alert("附件类型必须选一个！")
	 }else{
		   		document.ContractTransferAssignForm.isreturn.value = "Y";
		   		document.ContractTransferAssignForm.submit();
	 }
}


//插入一行数据
function addOne1(tableName,array){

	var adjt_w=document.getElementById(tableName);
	var num=adjt_w.rows.length-1;
	var values=new Array();
	for(var i=0;i<array.length;i++){
		values[i]=array[i].split("=");
	}
	
	//alert(values);
	adjt_w.insertRow(num);
	var ince0=adjt_w.rows(num).insertCell(0);
	ince0.align="center";
	ince0.innerHTML="<input type=\'checkbox\' onclick=\'cancelCheckAll(this)\'>";
	var ince1=adjt_w.rows(num).insertCell(1);
	ince1.innerHTML=values[0][1]+"<input type=\'hidden\' name=\'"+values[0][0]+"\' value=\'"+values[0][1]+"\'/>";
	var ince2=adjt_w.rows(num).insertCell(2);
	ince2.innerHTML=values[1][1]+"<input type=\'hidden\' name=\'"+values[1][0]+"\' value=\'"+values[1][1]+"\'/>";
	var ince3=adjt_w.rows(num).insertCell(3);
	ince3.innerHTML=values[2][1]+"<input type=\'hidden\' name=\'"+values[2][0]+"\' value=\'"+values[2][1]+"\'/>";
	var ince4=adjt_w.rows(num).insertCell(4);
	ince4.innerHTML=values[3][1]+"<input type=\'hidden\' name=\'"+values[3][0]+"\' value=\'"+values[3][1]+"\'/>";
	var ince5=adjt_w.rows(num).insertCell(5);
	ince5.innerHTML=values[4][1]+"<input type=\'hidden\' name=\'"+values[4][0]+"\' value=\'"+values[4][1]+"\'/>";
	var ince6=adjt_w.rows(num).insertCell(6);
	ince6.innerHTML=values[5][1]+"<input type=\'hidden\' name=\'"+values[5][0]+"\' value=\'"+values[5][1]+"\'/>";
	var ince7=adjt_w.rows(num).insertCell(7);
	ince7.innerHTML=values[6][1]+"<input type=\'hidden\' name=\'"+values[6][0]+"\' value=\'"+values[6][1]+"\'/>";
	var ince8=adjt_w.rows(num).insertCell(8);
	ince8.innerHTML=values[7][1]+"<input type=\'hidden\' name=\'"+values[7][0]+"\' value=\'"+values[7][1]+"\'/>"
	+"<input type=\'hidden\' name=\'"+values[8][0]+"\' value=\'"+values[8][1]+"\'/>"+"<input type=\'hidden\' name=\'"+values[9][0]+"\' value=\'"+values[9][1]+"\'/>"
	+"<input type=\'hidden\' name=\'"+values[10][0]+"\' value=\'"+values[10][1]+"\'/>";
	//var ince9=adjt_w.rows(num).insertCell(9);
	//ince9.innerHTML=values[8][1]+"<input type=\'hidden\' name=\'"+values[8][0]+"\' value=\'"+values[8][1]+"\'/>"
	//+"<input type=\'hidden\' name=\'"+values[9][0]+"\' value=\'"+values[9][1]+"\'/>"+"<input type=\'hidden\' name=\'"+values[10][0]+"\' value=\'"+values[10][1]+"\'/>"
	//+"<input type=\'hidden\' name=\'"+values[11][0]+"\' value=\'"+values[11][1]+"\'/>";
	//var ince10=adjt_w.rows(num).insertCell(10);
	//ince10.innerHTML=values[9][1]+"<input type=\'hidden\' name=\'"+values[9][0]+"\' value=\'"+values[9][1]+"\'/>";
	//var ince12=adjt_w.rows(num).insertCell(12);
	//ince12.innerHTML=values[14][1]+"<input type=\'hidden\' name=\'"+values[14][0]+"\' value=\'"+values[14][1]+"\'/>";
	
}



//添加
function addElevator2(action,thisobj){
		var projectName = window.location.pathname.replace(window.location.pathname.replace(/\s*\/+.*\/+/g,''),'');
		var url="query/Search.jsp?path="+encodeURIComponent(projectName+action+".do?method=addElevator");
		var returnarray = window.showModalDialog(url,window,'dialogWidth:1000px;dialogHeight:600px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
		if(returnarray!=null && returnarray.length>0){
						
			for(var i=0;i<returnarray.length;i++){
				addOne1("party_a",returnarray[i]);
			}
		}
}

//删除行
function deleteRow(thisobj){
	var tableObj = getFirstSpecificParentNode("table",thisobj);
	//alert(tableObj);
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

//列表全选反选
function checkTableAll(thisobj){
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


function checktype(obj){
	if(obj.checked==true){
		$("#filetypetd").append("<input type='hidden' name='filetype' id='filetype"+obj.value+"' value='"+obj.value+"'");
	}else{
		obj.parentNode.removeChild(document.getElementById("filetype"+obj.value));
	}
	
}

var fileGird=0;
function AddRow(uploadtab_2,jnlno){
	var upload=uploadtab_2.parentNode.parentNode.parentNode;
	var num=upload.rows.length;
	upload.insertRow(num);
	var ince0=upload.rows(num).insertCell(0);
	ince0.align="center";
	ince0.name="fileNo_"+jnlno+"_"+fileGird
	ince0.innerHTML="<input type='checkbox' name='nodes' onclick=\'cancelCheckAll(this)\'>";
	var ince1=upload.rows(num).insertCell(1);
	ince1.innerHTML="<input id=\'file_"+jnlno+"_"+fileGird+"\' name=\'"+jnlno+"_"+fileGird+"\' type=\'file\' class=\'default_input\' style=\'width:390px;\' />"
	+"<input type=\'hidden\' name=\'jnlno_"+jnlno+"\' value=\'file_"+jnlno+"_"+fileGird+"\' />";
	
	fileGird++;
}



//下载附件
function downloadFile(name,oldName,filePath,folder){
	var uri = '<html:rewrite page="/ContractTransferAssignAction.do"/>?method=toDownloadFile';
	var name1=encodeURI(name);
	name1=encodeURI(name1);
	var oldName1=encodeURI(oldName);
	oldName1=encodeURI(oldName1);
	filePath=encodeURI(filePath);
	filePath=encodeURI(filePath);
	    uri +='&filePath='+ filePath;
		uri +='&filesname='+ name1;
		uri +='&folder='+folder;
		uri+='&fileOldName='+oldName1;
	window.location = uri;
	//window.open(url);
}

//删除附件
var tbs;
function deleteFile(td,id,filePath){
	tbs=td;
    if(confirm("确定删除吗")){
    	tbs.parentElement.parentElement.style.display='';
		var uri = '<html:rewrite page="/ContractTransferAssignAction.do"/>?method=toDeleteFile';
		filePath=encodeURI(filePath);
		filePath=encodeURI(filePath);
		uri +='&filePath='+ filePath;
		uri +='&filesid='+ id;
		uri +='&folder=ContractTransferDebugFileinfo.file.upload.folder';
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
          	
          	if(res=="Y"){
          		tbs.parentElement.parentElement.parentElement.removeChild(tbs.parentElement.parentElement);
          	}else{
          		alert("删除失败！");
          	}
          	
          	//alert(document.getElementById("messagestring").innerHTML+";123");
       	} else { //页面不正常
             window.alert("您所请求的页面有异常。");
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