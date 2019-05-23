<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  <logic:notPresent name="display">  
  <%-- 是否有可写的权限--%>
  <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nadvisorycomplaintsdispose" value="Y"> 
  AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
    AddToolBarItemEx("SaveSubmitBtn","../../common/images/toolbar/save_back.gif","","",'<bean:message key="tollbar.saveandrefer"/>',"120","2","saveSubmitMethod()");
  </logic:equal>
  </logic:notPresent>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/advisoryComplaintsManageSearchAction.do"/>?method=toSearchRecordDispose';
}

//保存
function saveMethod(){
	var messageSource=document.advisoryComplaintsManageForm.messageSource.value;
	if(checkColumnInput(advisoryComplaintsManageForm)){
		if(messageSource!="400"){
			document.advisoryComplaintsManageForm.isreturn.value = "N";
			document.advisoryComplaintsManageForm.processType.value="S";
			document.advisoryComplaintsManageForm.submit();
		}else{
			var octId=document.getElementById("octId").value;
			if(octId!=""){
				document.advisoryComplaintsManageForm.isreturn.value = "N";
				document.advisoryComplaintsManageForm.processType.value="S";
				document.advisoryComplaintsManageForm.submit();
			}else{
				alert("请选择合同类型！");
			}
		}
	}	
}

//保存提交
function saveSubmitMethod(){
	var messageSource=document.advisoryComplaintsManageForm.messageSource.value;
	if(checkColumnInput(advisoryComplaintsManageForm)){
		if(messageSource!="400"){
			if(confirm("是否确定提交数据，提交后将不能对该项进行修改！")){
				document.advisoryComplaintsManageForm.isreturn.value = "Y";
				document.advisoryComplaintsManageForm.processType.value="Y";
				document.advisoryComplaintsManageForm.submit();
			}
		}else{
			var octId=document.getElementById("octId").value;
			if(octId!=""){
				if(confirm("是否确定提交数据，提交后将不能对该项进行修改！")){
					document.advisoryComplaintsManageForm.isreturn.value = "Y";
					document.advisoryComplaintsManageForm.processType.value="Y";
					document.advisoryComplaintsManageForm.submit();
				}
			}else{
				alert("请选择合同类型！");
			}
		}
	}
		
}

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
function addOneRow(tableName,array){
	var adjt_w=document.getElementById(tableName);
	var num=adjt_w.rows.length;
	var values=new Array();
	for(var i=0;i<array.length;i++){
		values[i]=array[i].split("=");
	}
	
	adjt_w.insertRow(num);
	var ince0=adjt_w.rows(num).insertCell(0);
	ince0.align="center";
	ince0.innerHTML="<input type=\'checkbox\' onclick=\'cancelCheckAll(this)\'>";
	var ince1=adjt_w.rows(num).insertCell(1);
	ince1.innerHTML=values[0][1]+"<input type=\'hidden\' name=\'"+values[1][0]+"\' value=\'"+values[1][1]+"\'/>"
}
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
		var elevatorNos = document.getElementsByName("comid");
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