<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/openwindow.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/checkinput.js"/>"></script>
<script language="javascript" src="<html:rewrite page="/common/javascript/dynamictable.js"/>"></script>

<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
   AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()"); 
   <logic:notPresent name="display">  
   <%-- 是否有可写的权限--%>
   <logic:equal name="<%=SysConfig.TOOLBAR_RIGHT%>" property="nmaintenanceregistration" value="Y"> 
   <logic:notEmpty name="btn1">
     AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'${btn1}',"65","1","saveMethod()");
     </logic:notEmpty>
     <logic:notEmpty name="btn2">
     AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","",'${btn2}',"65","1","saveReturnMethod()");
     </logic:notEmpty>
    
     <logic:notEmpty name="btn1">
     <logic:equal name="btn1" value="完 工">
     AddToolBarItemEx("SaveRequisitionBtn","../../common/images/toolbar/add.gif","","","配件申请单","90","1","saveRequisitionMethod()");
     </logic:equal>
     </logic:notEmpty>
     
     </logic:equal>
  </logic:notPresent>
   
   window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");

}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/maintenanceRegistrationSearchAction.do"/>?method=toSearchRecord';
}

function saveMethod(){
      var btn=document.getElementById("btn").value;
      if(btn=="接 收"){
    	var receivingTime=document.getElementById("receivingTime").value;
    	 if(receivingTime==""){
    		 alert("请选择,接收日期!");
    		 return;
    	 } 
    	 btn="1";
      }else if(btn=="到 场"){
    	  var maintStartTime=document.getElementById("maintStartTime").value;
    	  var maintStartAddres=document.getElementById("maintStartAddres").value;
    	  if(maintStartTime==""){
     		 alert("请选择,维保开始时间!");
     		 return;
     	 } if(maintStartAddres==""){
    		 alert("地址不能为空!");
    		 return;
    	 }
     	btn="2";
      }else if(btn=="完 工"){
    	  var maintEndTime=document.getElementById("maintEndTime").value;
    	  var maintEndAddres=document.getElementById("maintEndAddres").value;
    	  if(maintEndTime==""){
     		 alert("请选择,维保结束日期!");
     		 return;
     	 } if(maintEndAddres==""){
     		 alert("地址不能为空!");
    		 return;
    	 } 
     	btn="3";
      }
      document.maintenanceRegistrationForm.btn.value=btn;
      document.maintenanceRegistrationForm.submit();
}

//保存返回
function saveReturnMethod(){
	var btn=document.getElementById("btn2").value;
	var isTransfer=document.getElementById("isTransfer");
	var receivingPerson=document.getElementById("receivingPerson");
	if(isTransfer.value==""){
		alert("请选择,是否转派!");
		return;
	}if(receivingPerson.value==""){
		alert("请选择,转派人!")
		return;
	}
	btn="0";
	 document.maintenanceRegistrationForm.btn.value=btn;
     document.maintenanceRegistrationForm.submit();
}



//下载
function downloadFile(id){
	var uri = '<html:rewrite page="/maintenanceRegistrationAction.do"/>?method=toDownLoadFiles';
		uri +='&filesid='+ id;
		uri +='&folder=MaintenanceWorkPlan.file.upload.folder';
	window.location = uri;
	//window.open(url);
}

var fileGird=0;
function AddRow(uploadtab_2){
	var upload=uploadtab_2.parentNode.parentNode.parentNode;
	var num=upload.rows.length;
	upload.insertRow(num);
	var ince0=upload.rows(num).insertCell(0);
	ince0.align="center";
	ince0.name="fileNo_"+fileGird
	ince0.innerHTML="<input type='checkbox' name='nodes' onclick=\'cancelCheckAll(this)\'>";
	var ince1=upload.rows(num).insertCell(1);
	ince1.innerHTML="<input id=\'file_"+fileGird+"\' name=\'"+fileGird+"\' type=\'file\' size=\'40\' class=\'default_input\' />";
	
	fileGird++;
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

function saveRequisitionMethod(){
	var singleno=document.getElementById("singleno").value;
	var numno=document.getElementById("numno").value;
	window.location = '<html:rewrite page="/maintenanceRegistrationAction.do"/>?singleno='+singleno+'&method=toAccessoriesRequisition&numno='+numno;
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