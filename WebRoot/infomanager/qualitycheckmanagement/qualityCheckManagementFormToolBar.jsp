<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nqualitycheckmanagement" value="Y"> 
	  	<logic:present name="showupdarte"> 
	    	AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveUPMethod()");
	    </logic:present>    
	    <logic:notPresent name="showupdarte"> 
	    	AddToolBarItemEx("SaveSubmitBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="tollbar.saveandrefer"/>',"120","1","saveSubmitMethod()");
	    </logic:notPresent>
    </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/qualityCheckManagementSearchAction.do"/>?method=toSearchRecord';
}

//保存
function saveUPMethod(){
	inputTextTrim();
	var elevatorNo=document.getElementById("elevatorNo").value;
	if(elevatorNo==""){
		alert("电梯编号 不能为空！");
		return;
	}
	
	var maintPersonnel=document.getElementById("maintPersonnel").value;
	if(maintPersonnel==""){
		alert("维保工不能为空！");
		return;
	}
	  document.qualityCheckManagementForm.submit(); 

}

//保存
function saveMethod(){
	inputTextTrim();
	
	var tableObj = document.getElementById("party_a");
	var rows = tableObj.rows
	if(rows.length=='3'){
		alert("必须选择一台检查电梯!");
		return;
	}

	if(checkColumnInput(qualityCheckManagementForm)){
	  document.qualityCheckManagementForm.isreturn.value = "N";
	  document.qualityCheckManagementForm.issubmit.value="N";
	  document.qualityCheckManagementForm.submit(); 
  }
}


//保存提交
function saveSubmitMethod(){
	inputTextTrim();
	var tableObj = document.getElementById("party_a");
	var rows = tableObj.rows
	if(rows.length=='3'){
		alert("必须选择一台检查电梯!");
		return;
	}
	if(checkColumnInput(qualityCheckManagementForm)){
		var objarr=document.getElementsByName("maintPersonnel");
		var onum=0;
		var num=0;
		for(var i=0;i<objarr.length;i++){
			if(objarr[i].value==""){
				onum=i;
				num++;
				break;
			}
		}
		if(num>0){
			var elevatorNo=document.getElementsByName("elevatorNo")[onum].value;
			alert("电梯编号："+elevatorNo+", 请选择维保工！");
			return;
		}
		
	 	document.qualityCheckManagementForm.isreturn.value = "Y";
	 	document.qualityCheckManagementForm.issubmit.value="Y";
	  	document.qualityCheckManagementForm.submit(); 
  }
}

//添加
function addElevator1(action,thisobj){
		var tableObj = getFirstSpecificParentNode("table",thisobj);
		var paramstring = "rowidstr=";		
		var rowid = document.getElementsByName("rowid");
		for(i=0;i<rowid.length;i++){
			paramstring += i<rowid.length-1 ? "|"+rowid[i].value+"|," : "|"+rowid[i].value+"|";
		}

		var returnarray = openWindowWithParams(action,"toSearchRecord",paramstring);

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


function openWindowAndReturnValue3(actionName,method,flag){	 
	var returnvalue=openWindowWithParams(actionName,method,"");
	toSetInputValue2(returnvalue,flag);
}

//下载附件
function downloadFile(name,oledName){
	var uri = '<html:rewrite page="/qualityCheckManagementSearchAction.do"/>?method=toDownloadFileRecord';
	var name1=encodeURI(name);
	name1=encodeURI(name1);
	oledName=encodeURI(oledName);
	oledName=encodeURI(oledName);
		uri +='&filesname='+ name1;
		uri +='&folder=MTSComply.file.upload.folder';
		uri+='&fileOldName='+oledName;
	window.location = uri;
	//window.open(url);
}
	  
function addOne1(tableName,array){
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
	var valobj=null;
	var valms=null;

	for(var j=0;j<values.length;j++){
		if(j==7){
	    	valobj=values[j];
			//adjt_w.rows(num).cells(6).innerHTML+="<input type=\'text\' name=\'"+values[j][0]+"\' value=\'"+values[j][1]+"\'/>";
	    }else if(j==10 || j==11){
			adjt_w.rows(num).cells(9).innerHTML+="<input type=\'hidden\' name=\'"+values[j][0]+"\' value=\'"+values[j][1]+"\'/>";
		}else{
			if(j==6){
				valms=values[j];
			}
			
			var n=j+1;
			if(j>6){
				n=j;
			}
			if(j>11){
				n=j-3;
			}
			var ince1=adjt_w.rows(num).insertCell(n);
			if(j==8){
				//维保工下拉框
		    	var vuid=valobj[1]
		    	var selstr="<select name=\'"+valobj[0]+"\' onchange='changeuser(this);'>";
		    	selstr+="<option value=''>请选择</option>";
		    	
		    	<logic:present name="userstorageidlist">
			    	<logic:iterate id="element" name="userstorageidlist">
					    var sname='<bean:write name="element" property="storagename"/>';
					    if(sname==valms[1]){
						    var uid='<bean:write name="element" property="userid"/>';
						    var uname='<bean:write name="element" property="username"/>';
						    if(uid==vuid){
						    	selstr+="<option value=\'"+uid+"\' selected>"+uname+"</option>";
						    }else{
					    		selstr+="<option value=\'"+uid+"\'>"+uname+"</option>";
						    }
					    }
			    	</logic:iterate>
		    	</logic:present>
		    
		    	selstr+="</select>";
		    	
		    	ince1.innerHTML=selstr;
			}else if(j==9){
				//维保工电话
				ince1.innerHTML="<input type=\'text\' name=\'"+values[j][0]+"\' value=\'"+values[j][1]+"\' readonly class='default_input_noborder'/>";
			}else{
				ince1.innerHTML=values[j][1]+"<input type=\'hidden\' name=\'"+values[j][0]+"\' value=\'"+values[j][1]+"\'/>";
			}
		}
	}
	
	var incen=adjt_w.rows(num).insertCell(10);
	incen.align="center";
	incen.innerHTML="<textarea name=\'checkrem\' cols=\'20\'  rows=\'2\'></textarea>";
}

//下拉框发生变化，更改电话
function changeuser(obj){
	var objarr=document.getElementsByName(obj.name);
	var onum=0;
	for(var i=0;i<objarr.length;i++){
		if(objarr[i]==obj){
			onum=i;
			break;
		}
	}

	var pphone=document.getElementsByName("personnelPhone")[onum];
	if(obj.value==""){
		pphone.value="";
	}else{
		<logic:present name="userstorageidlist">
			<logic:iterate id="element" name="userstorageidlist">
			    var uid='<bean:write name="element" property="userid"/>';
			    var phone='<bean:write name="element" property="phone"/>';
			    if(obj.value==uid){
			    	pphone.value=phone;
				}
			</logic:iterate>
		</logic:present>
	}
}
	 
//下拉框发生变化，更改电话
function changeuser2(){
	var maintper=document.getElementById("maintPersonnel").value;
	var pphone=document.getElementById("personnelPhone");
	if(obj.value==""){
		pphone.value="";
	}else{
		<logic:present name="userstorageidlist">
			<logic:iterate id="element" name="userstorageidlist">
			    var uid='<bean:write name="element" property="userid"/>';
			    var phone='<bean:write name="element" property="phone"/>';
			    if(maintper==uid){
			    	pphone.value=phone;
				}
			</logic:iterate>
		</logic:present>
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