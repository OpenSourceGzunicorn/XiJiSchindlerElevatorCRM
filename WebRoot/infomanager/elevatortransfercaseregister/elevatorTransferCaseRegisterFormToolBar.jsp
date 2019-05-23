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

  <logic:present name="workisdisplay">  
  		AddToolBarItemEx("colseBtn","../../common/images/toolbar/close.gif","","",'关 闭',"65","0","closeMethod()");
  </logic:present>
  <logic:notPresent name="workisdisplay">  
  		AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  </logic:notPresent>  
  
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nelevatortransfercaseregistermanage" value="Y"> 
    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
    AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="tollbar.saveandrefer"/>',"120","1","saveReturnMethod()");
  </logic:equal>
  </logic:notPresent>
  <logic:present name="pdfplay">
  AddToolBarItemEx("PdfBtn","../../common/images/toolbar/save.gif","","","导出PDF","70","1","pdfMethod()");
  </logic:present>
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
  window.location = '<html:rewrite page="/elevatorTransferCaseRegisterManageSearchAction.do"/>?method=${returnMethod}';
}

//保存
function saveMethod(){
	if(document.getElementsByName("projectProvince")==null || document.getElementsByName("projectProvince").length==0){
		alert("请添加电梯编号！");
		return;
	}else{
		var propv=document.getElementsByName("projectProvince");
		var errorstr="";
		for(var i=0;i<propv.length;i++){
			if(propv[i].value.trim()==""){
				errorstr="项目省份 不能为空！";
				break;
			}
		}
		if(errorstr==""){
			inputTextTrim();  
		  	if(checkColumnInput(elevatorTransferCaseRegisterManageForm)){
		   		 document.elevatorTransferCaseRegisterManageForm.hecirs.value=rowsToJSONArray("party_a","hecis");
		   		 document.elevatorTransferCaseRegisterManageForm.submitType.value = "N";
		   		 document.elevatorTransferCaseRegisterManageForm.isreturn.value = "N";
		   		 document.elevatorTransferCaseRegisterManageForm.submit();
		  	}
		}else{
		  	alert(errorstr);
		}
	}
}

//保存返回
function saveReturnMethod(){
	if(document.getElementsByName("projectProvince")==null || document.getElementsByName("projectProvince").length==0){
		alert("请添加电梯编号！");
		return;
	}else{
		var propv=document.getElementsByName("projectProvince");
		var errorstr="";
		for(var i=0;i<propv.length;i++){
			if(propv[i].value.trim()==""){
				errorstr="项目省份 不能为空！";
				break;
			}
		}
		if(errorstr==""){
			inputTextTrim();  
		  	if(checkColumnInput(elevatorTransferCaseRegisterManageForm)){
		   		 document.elevatorTransferCaseRegisterManageForm.hecirs.value=rowsToJSONArray("party_a","hecis");
		   		 document.elevatorTransferCaseRegisterManageForm.submitType.value = "Y";
		   		 document.elevatorTransferCaseRegisterManageForm.isreturn.value = "Y";
		   		 document.elevatorTransferCaseRegisterManageForm.submit();
		  	}
		}else{
		  	alert(errorstr);
		}
	}
}

function toElevator(actionName,flag)
{
	var idType=document.getElementsByName("contractType")[0].value;
	var projectName = window.location.pathname.replace(window.location.pathname.replace(/\s*\/+.*\/+/g,''),'');
	var url="query/Search.jsp?path="+projectName+actionName+".do?idType="+idType;  
	var returnvalue = window.showModalDialog(url,window,'dialogWidth:770px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');	 
	toSetInputValue2(returnvalue,flag); 
}

function cleanElevator()
{
	document.getElementById("elevatorNo").value="";
	document.getElementById("salesContractNo").value="";
	document.getElementById("projectName").value="";
	document.getElementById("projectAddress").value="";
	document.getElementById("elevatorTypeName").value="";
	document.getElementById("elevatorParam").value="";
	document.getElementById("salesContractType").value="";
	document.getElementById("high").value="";
	document.getElementById("weight").value="";
	document.getElementById("speed").value="";
	document.getElementById("fsd").value="";
}

function cleanElevator1(){
	var table=document.getElementById("party_a");
	var l=table.rows.length;
	for(var i=l-2;i>1;i--){
		table.deleteRow(i);
	}
}

function toinsCompanyName()
{
	var isxjs=document.getElementById("isxjs")
	if(isxjs.value=="Y")
    {
		document.getElementById("insCompanyName").value="西继迅达（许昌）电梯有限公司";
		document.getElementById("insLinkPhone").value="";
    }else
	{
    	if(document.getElementById("insCompanyName").value!=""){
			document.getElementById("insCompanyName").value="";
    	}
		if(document.getElementById("insLinkPhone").value!=""){
			document.getElementById("insLinkPhone").value="";
    	}
	}
}

//添加交接电梯检查项目
/* function addElevators(tableId,type,name,flag){

	var paramstring1 = "checkItems=";
	var paramstring2="examTypes=";
	var paramstring3="issueCodings=";
	var issueCodings=document.getElementsByName("issueCoding");
	var examTypes=document.getElementsByName("examType");
	var checkItems = document.getElementsByName("checkItem");
	var examType="examType="+type;
	var examName=encodeURI("examName="+name);
	examName=encodeURI(examName);
  for(i=0;i<checkItems.length;i++){
    paramstring1 += i<checkItems.length-1 ? checkItems[i].value+"|" : checkItems[i].value;
    paramstring2+=i<examTypes.length-1? examTypes[i].value+"|" : examTypes[i].value;
    paramstring3+=i<issueCodings.length-1? issueCodings[i].value+"|" : issueCodings[i].value;
  }
  paramstring1=encodeURI(paramstring1);
  paramstring1=encodeURI(paramstring1);
  paramstring2=encodeURI(paramstring2);
  paramstring2=encodeURI(paramstring2);
  paramstring3=encodeURI(paramstring3);
  paramstring3=encodeURI(paramstring3);

  var returnarray = openWindowWithParams1("searchHandoverElevatorCheckItemAction","toSearchRecord",paramstring1,paramstring2,paramstring3,examType,examName);//弹出框并返回值

  if(returnarray!=null && returnarray.length>0){          
    addRows(tableId,returnarray.length);//增加行
    toSetInputValue3(returnarray,"elevatorTransferCaseRegisterForm",flag);//向页面对应输入框赋值
  }    
} */

//将table的tbody里所有行封装成json数组格式的字符串，
/* function rowsToJSONArray1(tableId1,tableId2,tableId3,key){
	var result = '{"'+key+'":[';
    result+=rowsToJSONArray2(tableId1);
	result = result+']}';  
	return result;
} */

/* //将table的tbody里所有行封装成json数组格式的字符串，
function rowsToJSONArray2(tableId){
	var table = document.getElementById(tableId);
	var tbody = table.getElementsByTagName("tbody")[0];
	var trs = tbody.childNodes
	var result ='';
  
	for(var i=0;i<trs.length;i++){
		var tds = trs[i].cells;
		var rowJson = "";
    
		for(var j=0;j<tds.length;j++){
			var cNodes = tds[j].childNodes;
           
			for(var k=0;k<cNodes.length;k++){
				if(cNodes[k].value && cNodes[k].name && cNodes[k].name!=''){
					var temp = '"'+cNodes[k].name+'":"'+cNodes[k].value+'"';
					rowJson += j<tds.length-1? temp+',' : temp; 
				}
			}    
    	}
		var rowJson = '{'+rowJson+'}';
		result += i<trs.length-1 ? rowJson+',':rowJson;
	}  
	return result;
} */

/*
检查所有有*号标记的文本输入框的值是否为空并提示，适用于如下格式
例如: <td>姓名</td><td><input type="text"/>*</td> 
            当空值时提醒   “姓名不能为空”
参数 element 例如div，table，form对象
*/
function checkColumnInput(element){
	  var inputs = element.getElementsByTagName("input");
	  var selects = element.getElementsByTagName("select");
	  var msg = "";
	  
	  for(var i=0;i<inputs.length;i++){
	    if(inputs[i].type == "text"
	      && inputs[i].parentNode.innerHTML.indexOf("*")>=0 
	      && inputs[i].value.trim() == ""){
	      
	        msg += inputs[i].parentNode.previousSibling.innerHTML + "不能为空\n";                       
	    }
	  }
	  
	  for(var i=0;i<selects.length;i++){
		  if(selects[i].value.trim()=="" && selects[i].parentNode.innerHTML.indexOf("*")>0){
				  msg+="请选择"+selects[i].parentNode.previousSibling.innerHTML.replace(/:$/gi,"")+"\n";
			  
		  }
	  }
	  if(msg != ""){
		    alert(msg);
		    return false;
		  } 
		  return true;
}

//弹出指定的查询页面的窗口，带参数
function openWindowWithParams1(actionName,method,paramstring1,paramstring2,paramstring3,examType,examName){
	var projectName = window.location.pathname.replace(window.location.pathname.replace(/\s*\/+.*\/+/g,''),'');
	var url="query/Search.jsp?path="+encodeURIComponent(projectName+actionName+".do?method="+method+"&"+paramstring1+"&"+paramstring2+"&"+paramstring3+"&"+examType+"&"+examName);	
	return window.showModalDialog(url,window,'dialogWidth:770px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
}

function addOneRow(tableName,array){
	var adjt_w=document.getElementById(tableName);
	var num=adjt_w.rows.length-1;
	var values=new Array();
	for(var i=0;i<array.length;i++){
		values[i]=array[i].split("=");
	}
	
	adjt_w.insertRow(num);
	var ince0=adjt_w.rows(num).insertCell(0);
	ince0.align="center";
	ince0.innerHTML="<input type=\'checkbox\' onclick=\'cancelCheckAll(this)\'>";
	var ince1=adjt_w.rows(num).insertCell(1);
	ince1.innerHTML=values[1][1]+"<input type=\'hidden\' name=\'"+values[0][0]+"\' value=\'"+values[0][1]+"\'/><input type=\'hidden\' name=\'scjnlno\'/>"
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
	ince4.innerHTML="<input type=\'text\' name=\'projectProvince\' value=\'\' size=\'12\' onblur=\'setProjectProvince(this);\'/>";
	var ince5=adjt_w.rows(num).insertCell(5);
	ince5.innerHTML=values[3][1]+"<input type=\'hidden\' name=\'"+values[3][0]+"\' value=\'"+values[3][1]+"\'/>";
	var ince6=adjt_w.rows(num).insertCell(6);
	ince6.innerHTML=values[4][1]+"<input type=\'hidden\' name=\'"+values[5][0]+"\' value=\'"+values[5][1]+"\'/>";
	var ince7=adjt_w.rows(num).insertCell(7);
	ince7.innerHTML=values[6][1]+"<input type=\'hidden\' name=\'"+values[6][0]+"\' value=\'"+values[6][1]+"\'/>";
	var ince8=adjt_w.rows(num).insertCell(8);
	ince8.innerHTML=values[7][1]+"<input type=\'hidden\' name=\'"+values[7][0]+"\' value=\'"+values[7][1]+"\'/>";
	var ince9=adjt_w.rows(num).insertCell(9);
	ince9.innerHTML=values[11][1]+"<input type=\'hidden\' name=\'"+values[8][0]+"\' value=\'"+values[8][1]+"\'/>"
		+"<input type=\'hidden\' name=\'"+values[9][0]+"\' value=\'"+values[9][1]+"\'/>"
		+"<input type=\'hidden\' name=\'"+values[10][0]+"\' value=\'"+values[10][1]+"\'/>";
	var ince10=adjt_w.rows(num).insertCell(10);
	ince10.innerHTML=values[12][1]+"<input type=\'hidden\' name=\'"+values[12][0]+"\' value=\'"+values[12][1]+"\'/>";
	var ince11=adjt_w.rows(num).insertCell(11);
	ince11.innerHTML=values[13][1]+"<input type=\'hidden\' name=\'"+values[13][0]+"\' value=\'"+values[13][1]+"\'/>";
	var ince12=adjt_w.rows(num).insertCell(12);
	ince12.innerHTML=values[14][1]+"<input type=\'hidden\' name=\'"+values[14][0]+"\' value=\'"+values[14][1]+"\'/>";
	
}

function setProjectProvince(obj){
	var projectProvince=document.getElementsByName("projectProvince");
	var isok=0;
	for(var i=0;i<projectProvince.length;i++){
		if(projectProvince[i]==obj){
			isok=i;
			break;
		}
	}
	
	if(isok==0){
		for(var j=0;j<projectProvince.length;j++){
			projectProvince[j].value=obj.value;
		}
	}
}
//弹出指定的查询页面的窗口，带参数
function openWindowWithParams1(actionName,method,paramstring){
	var projectName = window.location.pathname.replace(window.location.pathname.replace(/\s*\/+.*\/+/g,''),'');
	var url="query/Search.jsp?path="+encodeURIComponent(projectName+actionName+".do?method="+method+"&"+paramstring);	
	return window.showModalDialog(url,window,'dialogWidth:1000px;dialogHeight:600px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
}

//添加
function addElevator1(action,thisobj){
		var tableObj = getFirstSpecificParentNode("table",thisobj);
		var idType=document.getElementsByName("contractType")[0].value;
		var paramstring = "QualityNos=";		
		var elevatorNos = document.getElementsByName("elevatorNo");
		for(i=0;i<elevatorNos.length;i++){
			paramstring += i<elevatorNos.length-1 ? "|"+elevatorNos[i].value+"|," : "|"+elevatorNos[i].value+"|";
		}
		paramstring+="&idType="+idType;
		paramstring=encodeURI(paramstring);
		paramstring=encodeURI(paramstring);
		//alert(paramstring);

		var returnarray = openWindowWithParams1(action,"toSearchRecord",paramstring);

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

//添加维保质检项
function addElevators(action,thisobj){
		var tableObj = getFirstSpecificParentNode("table",thisobj);
		var paramstring = "QualityNos=";		
		var elevatorNos = document.getElementsByName("scId");
		for(i=0;i<elevatorNos.length;i++){
			paramstring += i<elevatorNos.length-1 ? "|"+elevatorNos[i].value+"|," : "|"+elevatorNos[i].value+"|"		
		}
		paramstring=encodeURI(paramstring);
		paramstring=encodeURI(paramstring);
		//alert(paramstring);
		var returnarray = openWindowWithParams(action,"toSearchRecord",paramstring);
		if(returnarray!=null && returnarray.length>0){			
			for(var i=0;i<returnarray.length;i++){
				addOneRow("searchCompany",returnarray[i]);
			}
		}
}

//下载附件
function downloadFile(name){
	var uri = '<html:rewrite page="/elevatorTransferCaseRegisterManageAction.do"/>?method=toDownloadFileRecord';
	var name1=encodeURI(name);
	name1=encodeURI(name1);
		uri +='&filesname='+ name1;
		uri +='&folder=HandoverElevatorCheckItemRegister.file.upload.folder';
	window.location = uri;
}

//pdf导出
function pdfMethod(){
 	window.location='<html:rewrite page="/ContractPdfServlet"/>?flag=ElevatorTransferCaseRegister&htmlName=ElevatorTransferCaseRegister.html';
}

//下载附件
function downloadFile(name,oldName,filePath){
	var uri = '<html:rewrite page="/elevatorTransferCaseRegisterManageAction.do"/>?method=toDownloadFileDispose';
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