<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nqualitycheckregistration" value="Y"> 
  AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
    AddToolBarItemEx("SaveSubmitBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="tollbar.saveandrefer"/>',"120","2","saveSubmitMethod()");
  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/qualityCheckManagementSearchAction.do"/>?method=toSearchRecordRegistration';
}

//保存
function saveMethod(){
// alert(rowsToJSONArray("mtc",""));
	//alert(detailJson());
	
	if(verify()){
		document.qualityCheckManagementForm.Scores.value=rowsToJSONArray("mtc","scorce");
		document.qualityCheckManagementForm.details.value=detailJson();
		document.qualityCheckManagementForm.isreturn.value = "N";
		document.qualityCheckManagementForm.isdispose.value="1";
		document.qualityCheckManagementForm.submit();	
	}
	
}

//保存提交
function saveSubmitMethod(){
	if(verify()){
		if(confirm("是否确定提交数据，提交后将不能对该项进行修改！")){
			document.qualityCheckManagementForm.Scores.value=rowsToJSONArray("mtc","scorce");
			document.qualityCheckManagementForm.details.value=detailJson();
			document.qualityCheckManagementForm.isreturn.value = "Y";
			document.qualityCheckManagementForm.isdispose.value="2";
			document.qualityCheckManagementForm.submit();
		}
	}
}

function verify(){
	var elevatorNo=document.getElementById("elevatorNo");
	if(elevatorNo.value==""){
		alert("电梯编号不能为空！");
		return false;
	}
	var mtc=document.getElementById("mtc");

	var vag="";
	if(mtc.rows.length<=2){
		vag+="请添加维保质量检查扣分项！\n";
	}

	
	if(vag!=""){
		alert(vag);
		return false;
	}else{
		return true;
	}
}


//检查项计算 总得分，得分等级
/** 
  	总得分=100-分值的合计；
  	--得分等级=根据总得分进行判断：90分以上优秀，80分-90分合格，80分以下不合格
  	得分等级=80-84分为合格，85-89为良好， 90以上为优秀 【2017-05-19修改】
*/
function partitionGrade(){
	var totalPoints=document.getElementsByName("totalPoints")[0];
	var total=parseFloat(totalPoints.value);
	var leve="";
	if(total>=90){
		leve="优秀";
	}else if(total>=85 && total<90){
		leve="良好";
	}else if(total>=80 && total<85){
		leve="合格";
	}else{
		leve="不合格";
	}

	document.getElementsByName("scoreLevel")[0].value=leve;
}

function isQualified(){
	var fractions=document.getElementsByName("fraction");
	var total=0.0;
	if(fractions!=null && fractions.length>0){
		for(var i=0;i<fractions.length;i++){
			total+=parseFloat(fractions[i].value);
		}
	}
	document.getElementsByName("totalPoints")[0].value=100.0-total;
}

function emptyFile(obj){
	var index=obj.id;
	var file=document.getElementById("file_"+index);
	file.outerHTML=file.outerHTML;
	partitionGrade();
}
var detail=0;
function addOneRow(tableName,array,k){
	var adjt_w=document.getElementById(tableName);
	/* var adjt=thisobj.parentElement.parentElement.parentElement;
	alert(adjt[1]); */
	var num=adjt_w.rows.length;
	var len=document.getElementById("mtc").rows.length-2;
	var values=new Array();
	for(var i=0;i<array.length;i++){
		values[i]=array[i].split("=");
	}
	
	adjt_w.insertRow(num);
	var ince0=adjt_w.rows(num).insertCell(0);
	ince0.align="center";
	ince0.innerHTML="<input type=\'checkbox\' onclick=\'cancelCheckAll(this)\'>";
	var ince1=adjt_w.rows(num).insertCell(1);
	
	ince1.innerHTML=values[1][1]+"<input type=\'hidden\' name=\'"+values[0][0]+"\' value=\'"+values[0][1]+"\'/>"
	+"<input type=\'hidden\' name=\'"+values[1][0]+"\' value=\'"+values[1][1]+"\'/>"
	+"<input type=\'hidden\' name=\'"+values[2][0]+"\' value=\'"+values[2][1]+"\'/>";
	if(tableName!="mtc"){
		var inced=adjt_w.rows(num).insertCell(2);
		inced.style.display="none";
	}
	/* for(var i=0;i<values.length;i++){
		if(i>0){
			var ince1=adjt_w.rows(num).insertCell(i);
			if(i==3){
				ince1.align="center";
			}
			ince1.innerHTML=values[i][1]+"<input type=\'hidden\' name=\'"+values[i][0]+"\' value=\'"+values[i][1]+"\' />";
		}
	} */
	//adjt_w.rows(num).cells(1).innerHTML+="<input type=\'hidden\' name=\'"+values[0][0]+"\' value=\'"+values[0][1]+"\' />";
	var td="&nbsp;&nbsp;<input type='button' name='toaddrow' value=' + ' onclick=\'AddRow(this,\""+values[0][1]+"\")\'/>";
	if(tableName=="mtc"){
		var ince2=adjt_w.rows(num).insertCell(2);
		ince2.valign="top";
		ince2.innerHTML="<table width='100%' class='tb' id='msrd"+detail+"'>"+
		"<thead><tr class='wordtd'><td width='5%' align='center'><input type='checkbox' onclick='checkTableAll(this)'/></td>"+
		"<td align='left'>&nbsp;<input type='button' value=' + ' onclick='addElevators(\"searchMarkingScoreRegisterDetailAction\",this,\"msrd"+detail+"\",\"detailId\",\""+values[0][1]+"\")'/>"+
		"&nbsp;<input type='button' value=' - ' onclick='deleteRow(this)'></td>"+
		"</tr></thead><tbody></tbody>"+
		"</table><br>";
		detail++;
		
		var ince3=adjt_w.rows(num).insertCell(3);
		ince3.innerHTML="<textarea name=\'rem\' rows=\'3\' cols=\'20\'></textarea>";
		
		var ince4=adjt_w.rows(num).insertCell(4);
		ince4.innerHTML="<table width='100%' class='tb'>"
		+"<tr class='wordtd'><td width='5%' align='center'><input type='checkbox' onclick='checkTableAll(this)'></td>"
		+"<td align='left'>"+td
		+"&nbsp;<input type='button' name='todelrow' value=' - ' onclick='deleteRow(this)'>"
		+"</td></tr>"
		+"</table><br>";
	}
}

function detailJson(){
	var details="";
	var dn=0;
	for(var i=0;i<detail;i++){
		if(document.getElementById("msrd"+i)){
			details+=i==detail-1 ? rowsToJSONArray("msrd"+i,"detail"+dn) : rowsToJSONArray("msrd"+i,"detail"+dn)+"|";
			dn++;
		}
	}
	return details;
}

var fileGird=0;
function AddRow(uploadtab_2,value){
	var upload=uploadtab_2.parentNode.parentNode.parentNode;
	var num=upload.rows.length;
	upload.insertRow(num);
	var ince0=upload.rows(num).insertCell(0);
	ince0.align="center";
	ince0.innerHTML="<input type='checkbox' name='nodes' onclick=\'cancelCheckAll(this)\'>";
	var ince1=upload.rows(num).insertCell(1);
	ince1.innerHTML="<input id=\'file_"+fileGird+"\' name=\'"+fileGird+"\' type=\'file\' class=\'default_input\' />"
	+"<input type=\'hidden\' name=\'primary_"+fileGird+"\' value=\'"+value+"\' />";
	fileGird++;
}

/* //增加一行
function addOneRow(thisobj,modelrow) {
	var tableObj = getFirstSpecificParentNode("table",thisobj);
	tableObj.getElementsByTagName("tbody")[0].appendChild(modelrow.cloneNode(true));
	cancelCheckAll(thisobj);	
}*/
      
//删除行
function deleteRow(thisobj){
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
	isQualified();
	partitionGrade();
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

//添加维保质检项
function addElevators(action,thisobj,tableName,qId,msId){
		var tableObj = getFirstSpecificParentNode("table",thisobj);
		
		var elevatorType=document.getElementById("elevatorType").value;
		var paramstring = "elevatorType="+elevatorType+"&QualityNos=";		
		var elevatorNos = document.getElementsByName(qId);
		for(i=0;i<elevatorNos.length;i++){
			paramstring += i<elevatorNos.length-1 ? "|"+elevatorNos[i].value+"|," : "|"+elevatorNos[i].value+"|"		
		}
		paramstring+="&msId="+msId;
		paramstring=encodeURI(paramstring);
		paramstring=encodeURI(paramstring);
		//alert(paramstring);

		var returnarray = openWindowWithParams(action,"toSearchRecord",paramstring);

		if(returnarray!=null && returnarray.length>0){
						
			for(var i=0;i<returnarray.length;i++){
				addOneRow(tableName,returnarray[i],i);
			}
		}
		isQualified();	
		partitionGrade();
}

//返回元素指定的父节点对象
function getFirstSpecificParentNode(parentTagName,childObj){
	var parentObj = childObj.parentNode;
	
	while(parentObj){
		if(parentObj.nodeName.toLowerCase() == parentTagName.toLowerCase()){				
			break;
		}
		parentObj = parentObj.parentNode;
	}
	
	return parentObj;
}

//----------------------------AJAX-------------------------------------------------

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
          		tbs.parentElement.parentElement.removeChild(tbs.parentElement);
          		partitionGrade();
          	}else{
          		alert("删除失败！");
          	}
          	
          	//alert(document.getElementById("messagestring").innerHTML+";123");
       	} else { //页面不正常
             window.alert("您所请求的页面有异常。");
       	}
     }
  }

  //删除
  var tbs;
  function deleteFile(td,primary){
	  tbs=td;
	  primary=encodeURI(primary);
	  primary=encodeURI(primary);
	    if(confirm("确定删除吗")){
	    	tbs.parentElement.parentElement.style.display='';
			var uri = '<html:rewrite page="/qualityCheckManagementAction.do"/>?method=toDeleteFileRegistration';
			uri +='&filesid='+ primary;
			uri +='&folder=MTSComply.file.upload.folder';
			sendRequestDelFile(uri);  	
			partitionGrade();
		}
	}

//下载附件
function downloadFile(name,oldName){
	var uri = '<html:rewrite page="/qualityCheckManagementSearchAction.do"/>?method=toDownloadFileRegistration';
	var name1=encodeURI(name);
	name1=encodeURI(name1);
	oledName=encodeURI(oldName);
	oledName=encodeURI(oldName);
		uri +='&filesname='+ name1;
		uri +='&folder=MTSComply.file.upload.folder';
		uri+='&fileOldName='+oldName;
	window.location = uri;
	//window.open(url);
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
                //SetToolBarAttribute();
              //-->
            </script>
            </div>
            </td>
        </tr>
      </table>
    </td>
  </tr>
</table>