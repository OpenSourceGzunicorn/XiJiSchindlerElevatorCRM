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

  AddToolBarItemEx("ReturnBtn","../../common/images/toolbar/back.gif","","",'<bean:message key="toolbar.return"/>',"65","0","returnMethod()");
  window.document.getElementById("toolbar").innerHTML=GenToolBar("TabToolBar_Manage","TextToolBar_Black","Style_Over","Style_Out","Style_Down","Style_Check");
}
//返回
function returnMethod(){
  window.location = '<html:rewrite page="/wgchangeAuditSearchAction.do"/>?method=toSearchRecord';
}

//保存
function saveMethod(){
	if(checkInput()){
	    var elevatorNos = document.getElementsByName("elevatorNo");
		if(elevatorNos !=null && elevatorNos.length>0){
			document.WeiGchangeForm.isreturn.value = "N";
  			document.WeiGchangeForm.submit();
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
			document.WeiGchangeForm.isreturn.value = "Y";
  			document.WeiGchangeForm.submit();
		}else{
			alert("不能保存，请添加电梯信息。");
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
function addElevators(thisobj,defaultrowobj){
	var salesContractNo = WeiGchangeForm.salesContractNo.value;
	var tableObj = getFirstSpecificParentNode("table",thisobj);
	if(salesContractNo!=null&&salesContractNo!=""){
		var paramstring = "salesContractNo="+salesContractNo+"&elevatorNos=";		
		var elevatorNos = document.getElementsByName("elevatorNo");
		for(i=0;i<elevatorNos.length;i++){
			paramstring += i<elevatorNos.length-1 ? "|"+elevatorNos[i].value+"|," : "|"+elevatorNos[i].value+"|"		
		}

		var returnarray = openWindowWithParams("searchElevatorSaleAction","toSearchRecord2",paramstring);

		if(returnarray!=null && returnarray.length>0){
						
			for(i=0;i<returnarray.length;i++){
				addOneRow(thisobj,modelrow0);
			}

			toSetInputValue(returnarray,"WeiGchangeForm");
		}		
	}else{
		alert("不能添加！请先录入销售合同号。");
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
	if(checkColumnInput(WeiGchangeForm) && checkRowInput('table_22','tb_head') && checkRowInput('table_1','wordtd_header')){
		return true;
	}else{
		return false;
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