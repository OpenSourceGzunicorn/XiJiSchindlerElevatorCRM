<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>
<script language="javascript">

//关于ToolBar
function CreateToolBar(){
  SetToolBarHandle(true);
  SetToolBarHeight(20);
  <logic:equal name="type" value="Yes">
  AddToolBarItemEx("SaveSubmitBtn","../../common/images/toolbar/close.gif","","",'关闭',"60","0","window.close()");
</logic:equal>
<logic:notPresent name="type"> 
AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
</logic:notPresent>
  
  <logic:notEqual name="typejsp" value="totask">  
  <logic:notEqual name="typejsp" value="display">  
		    AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod()");
		    AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","","保存提交并返回","120","1","saveReturnMethod()");	   
 </logic:notEqual>
 </logic:notEqual>
 <logic:equal name="typejsp" value="totask">
 AddToolBarItemEx("SaveBtn","../../common/images/toolbar/save.gif","","",'<bean:message key="toolbar.save"/>',"65","1","saveMethod2()");
 AddToolBarItemEx("SaveReturnBtn","../../common/images/toolbar/save_back.gif","","","保存返回","100","1","saveReturnMethod2()");
 </logic:equal>
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/wgchangeContractSearchAction.do"/>?method=toSearchRecord';
}

//保存
function saveMethod(){
	if(checkInput()){
	    var elevatorNos = document.getElementsByName("elevatorNo");
		if(elevatorNos !=null && elevatorNos.length>0){
			document.wgchangeContractForm.isreturn.value = "N";
  			document.wgchangeContractForm.submit();
		}else{
			alert("不能保存，请添加电梯信息。");
		}
	}
}

//保存 修改返回
function saveReturnMethod(){
	if(checkInput()){
	    var elevatorNos = document.getElementsByName("elevatorNo");
		if(elevatorNos !=null && elevatorNos.length>0){
			document.wgchangeContractForm.isreturn.value = "Y";
  			document.wgchangeContractForm.submit();
		}else{
			alert("不能保存，请添加电梯信息。");
		}
	}
}

//下达保存
function saveMethod2(){
	if(checkInput()){
		var taskSubFlag = document.wgchangeContractForm.taskSubFlag.value;
		if(taskSubFlag !=null && taskSubFlag!="已下达"){
			document.wgchangeContractForm.isreturn.value = "N";
  			document.wgchangeContractForm.submit();
		}else{
			alert("此记录已经下达，不能重复下达！");
		}
	}
}

//下达保存返回
function saveReturnMethod2(){
	if(checkInput()){
	    var taskSubFlag = document.wgchangeContractForm.taskSubFlag.value;
		if(taskSubFlag !=null && taskSubFlag!="已下达"){
			document.wgchangeContractForm.isreturn.value = "Y";
  			document.wgchangeContractForm.submit();
		}else{
			alert("此记录已经下达，不能重复下达！");
		}
	}
}
function switchCell(n) {
	for(i=0;i<navcell.length;i++){
		navcell[i].className="tab-off";
		tb[i].style.display="none";
	}
	navcell[n].className="tab-on";
	tb[n].style.display="";
}

//增加一行
function addOneRow(thisobj,modelrow) {
	var tableObj = getFirstSpecificParentNode("table",thisobj);
	tableObj.getElementsByTagName("tbody")[0].appendChild(modelrow.cloneNode(true));
	cancelCheckAll(thisobj);
}
function fuz(){
	a=document.getElementsByName("xuhao");
	for(i=0;i<a.length;i++){
		a[i].value = i+1;
	}
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
    for(j=0;j<inputs.length;j++){
	  if(inputs[j].type=="checkbox"){
	    inputs[j].checked = false; //table中第一个checkbox取消选中
	    break;
	  }
	}
  }
}

//添加电梯信息
function addElevators(tableId){
		var paramstring = "state=WGBJ&&elevatorNos=";		
		var elevatorNos = document.getElementsByName("elevatorNo");
		for(i=0;i<elevatorNos.length;i++){
			paramstring += i<elevatorNos.length-1 ? "|"+elevatorNos[i].value+"|," : "|"+elevatorNos[i].value+"|"		
		}
		var returnarray = openWindowWithParams("searchElevatorSaleAction","toSearchRecord2",paramstring);

		if(returnarray!=null && returnarray.length>0){
			addRows(tableId,returnarray.length);
			toSetInputValue(returnarray,"wgchangeContractForm");
		}			
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


//返回销售合同信息
function openWindowReturnContractNo(){
  var salesContractNo = maintContractQuoteForm.salesContractNo.value;
  openWindowAndReturnValue('searchElevatorSaleAction','');
  
  //若新旧销售合同号不同，将会删除旧的电梯信息
  if(salesContractNo != maintContractQuoteForm.salesContractNo.value){
    deleteAllRows(document.getElementById("checkAll"));
  }
}

// 删除全选checkbox所在表格的所有行
function deleteAllRows(obj){
	obj.checked="true";
	checkTableAll(obj);
	deleteRow(obj);
}

//判断页面的值
function checkInput(){
	if(checkColumnInput(wgchangeContractForm) && checkRowInput('dynamictable_0','titleRow_0') ){
		return true;
	}else{
		return false;
	}
}

function f_check_number3(){
    if((event.keyCode>=48 && event.keyCode<=57) || event.keyCode==46 ){
    }else{
    event.keyCode=0;
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